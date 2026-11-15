# Development Log

## Session 1 - Initial Design and Implementation (Tuesday, November 10)

**Plan:** Read the project specification, grading rubric, sample output, and Java thread/semaphore examples. Then design a simple Java solution with three teller threads, fifty customer threads, shared pairing data, and semaphores for every required shared resource.

**Reflection:** I implemented the simulation in `Project2.java`. Each customer has semaphores for being called by a teller, introducing itself, receiving the transaction question, giving the transaction, receiving completion, and leaving the teller. The shared customer line is protected by a semaphore, the manager has one permit, the safe has two permits, and the door has two permits. I compiled and ran the program repeatedly, checked that all customers and tellers terminate, and packaged the source files with a README and devlog.

## Session 2 - Synchronization Review and Validation (Friday, November 14)

**Plan:** Review the semaphore handshakes between customers and tellers, then run the full simulation again. I wanted to make sure a teller does not begin serving another customer until its current customer leaves, and that the manager and safe limits are enforced.

**Reflection:** The program compiled with Java lint checks and completed a full run. The output showed all 50 customers leaving the bank, all 3 tellers leaving for the day, and the final bank-closing message. I also checked the trace: no more than 1 teller used the manager at once, and no more than 2 tellers were in the safe at once. The customer/teller semaphores kept each conversation in order, so I did not need to change the synchronization design during this review.

## Session 3 - Final Requirements Audit (Friday, November 14)

**Plan:** Do a final pass against the project specification and rubric. I planned to compile with warnings enabled, run several complete simulations, verify the opening order, resource limits, shutdown behavior, and inspect the contents of the submission package.

**Reflection:** I compiled `Project2.java` with Java lint checks and completed five fresh simulation runs. In every run, all three tellers announced readiness before the first customer entered, all 50 customers left the bank, all three tellers terminated, and the bank-closing message was last. I checked the resource trace in each run: manager access never exceeded one teller and safe access never exceeded two tellers. The package contains `Project2.java`, `README.md`, `DEVLOG.md`, and `.gitignore`. The remaining submission task is to place these files in my course Git repository and make genuine commits as I continue working; the current folder does not yet track these files in Git.

## Session 4 - Repository Setup and Submission Check (Saturday, November 15)

**Plan:** Put the completed project in the course GitHub repository, confirm the repository has the required source and documentation files, and test the project again from that repository before preparing a commit.

**Reflection:** I added `Project2.java`, `README.md`, `DEVLOG.md`, and `.gitignore` to the repository. I compiled the project with lint checks and ran three new full simulations directly from the repository. Each run completed with 50 customer departures, 3 teller departures, the final bank-closing message, no more than 1 teller using the manager, and no more than 2 tellers in the safe. I also confirmed that generated class files are ignored, so the commit will contain only the required source and documentation files.

## Session 5

**Plan:**

**Reflection:**
