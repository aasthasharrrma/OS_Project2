import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Project2 {
    private static final int TELLER_COUNT = 3;
    private static final int CUSTOMER_COUNT = 50;

    private static final Semaphore bankOpen = new Semaphore(0);
    private static final Semaphore readyLock = new Semaphore(1);
    private static final Semaphore lineLock = new Semaphore(1);
    private static final Semaphore customerWaiting = new Semaphore(0);
    private static final Semaphore manager = new Semaphore(1);
    private static final Semaphore safe = new Semaphore(2);
    private static final Semaphore door = new Semaphore(2);
    private static final ArrayDeque<Customer> line = new ArrayDeque<>();
    private static int readyTellers = 0;

    private static synchronized void log(String message) {
        System.out.println(message);
    }

    private static void pause(int minimum, int maximum) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(minimum, maximum + 1));
    }

    private static class Customer extends Thread {
        private final int id;
        private final boolean withdrawal;
        private Teller teller;
        private final Semaphore called = new Semaphore(0);
        private final Semaphore introduced = new Semaphore(0);
        private final Semaphore askedForTransaction = new Semaphore(0);
        private final Semaphore gaveTransaction = new Semaphore(0);
        private final Semaphore transactionFinished = new Semaphore(0);
        private final Semaphore leftTeller = new Semaphore(0);

        Customer(int id) {
            this.id = id;
            withdrawal = ThreadLocalRandom.current().nextBoolean();
        }

        public void run() {
            try {
                String transaction = withdrawal ? "withdrawal" : "deposit";
                log("Customer " + id + " []: wants to perform a " + transaction + " transaction");
                pause(0, 100);
                bankOpen.acquire();
                log("Customer " + id + " []: going to bank.");
                door.acquire();
                log("Customer " + id + " []: entering bank.");
                log("Customer " + id + " []: getting in line.");
                door.release();

                lineLock.acquire();
                line.addLast(this);
                lineLock.release();
                customerWaiting.release();

                log("Customer " + id + " []: selecting a teller.");
                called.acquire();
                log("Customer " + id + " [Teller " + teller.id + "]: selects teller");
                log("Customer " + id + " [Teller " + teller.id + "] introduces itself");
                introduced.release();
                askedForTransaction.acquire();
                log("Customer " + id + " [Teller " + teller.id + "]: asks for " + transaction + " transaction");
                gaveTransaction.release();
                transactionFinished.acquire();
                log("Customer " + id + " [Teller " + teller.id + "]: leaves teller");
                leftTeller.release();
                log("Customer " + id + " []: goes to door");
                log("Customer " + id + " []: leaves the bank");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Teller extends Thread {
        private final int id;

        Teller(int id) {
            this.id = id;
        }

        public void run() {
            try {
                log("Teller " + id + " []: ready to serve");
                readyLock.acquire();
                readyTellers++;
                if (readyTellers == TELLER_COUNT) {
                    bankOpen.release(CUSTOMER_COUNT);
                }
                readyLock.release();

                while (true) {
                    log("Teller " + id + " []: waiting for a customer");
                    customerWaiting.acquire();

                    lineLock.acquire();
                    Customer customer = line.pollFirst();
                    lineLock.release();
                    if (customer == null) {
                        break;
                    }

                    customer.teller = this;
                    customer.called.release();
                    customer.introduced.acquire();
                    log("Teller " + id + " [Customer " + customer.id + "]: serving a customer");
                    log("Teller " + id + " [Customer " + customer.id + "]: asks for transaction");
                    customer.askedForTransaction.release();
                    customer.gaveTransaction.acquire();

                    String transaction = customer.withdrawal ? "withdrawal" : "deposit";
                    log("Teller " + id + " [Customer " + customer.id + "]: handling " + transaction + " transaction");
                    if (customer.withdrawal) {
                        log("Teller " + id + " [Customer " + customer.id + "]: going to the manager");
                        manager.acquire();
                        log("Teller " + id + " [Customer " + customer.id + "]: getting manager's permission");
                        pause(5, 30);
                        log("Teller " + id + " [Customer " + customer.id + "]: got manager's permission");
                        manager.release();
                    }

                    log("Teller " + id + " [Customer " + customer.id + "]: going to safe");
                    safe.acquire();
                    log("Teller " + id + " [Customer " + customer.id + "]: enter safe");
                    pause(10, 50);
                    log("Teller " + id + " [Customer " + customer.id + "]: leaving safe");
                    safe.release();
                    log("Teller " + id + " [Customer " + customer.id + "]: finishes " + transaction + " transaction.");
                    customer.transactionFinished.release();
                    log("Teller " + id + " [Customer " + customer.id + "]: wait for customer to leave.");
                    customer.leftTeller.acquire();
                    log("Teller " + id + " []: ready to serve");
                }
                log("Teller " + id + " []: leaving for the day");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Teller[] tellers = new Teller[TELLER_COUNT];
        Customer[] customers = new Customer[CUSTOMER_COUNT];

        for (int i = 0; i < TELLER_COUNT; i++) {
            tellers[i] = new Teller(i);
            tellers[i].start();
        }
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers[i] = new Customer(i);
            customers[i].start();
        }

        for (Customer customer : customers) {
            customer.join();
        }
        customerWaiting.release(TELLER_COUNT);
        for (Teller teller : tellers) {
            teller.join();
        }
        log("The bank closes for the day.");
    }
}
