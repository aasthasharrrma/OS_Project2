# Development Log

## Session 1 - Initial Design and Implementation

**Recorded:** Tuesday, November 10, 2026 at 10:46:33 PM CST

**Plan:** Read the project specification, grading rubric, sample output, and Java thread/semaphore examples. Then design a simple Java solution with three teller threads, fifty customer threads, shared pairing data, and semaphores for every required shared resource.

**Reflection:** I implemented the simulation in `Project2.java`. Each customer has semaphores for being called by a teller, introducing itself, receiving the transaction question, giving the transaction, receiving completion, and leaving the teller. The shared customer line is protected by a semaphore, the manager has one permit, the safe has two permits, and the door has two permits. The opening semaphore holds customers at the bank entrance until all three teller threads announce readiness. I compiled and ran the program repeatedly, checked that all customers and tellers terminate, and packaged the source files with a README and devlog.

## Session 2 - Synchronization Review and Validation

**Recorded:** Saturday, November 14, 2026 at 12:47:37 PM CST

**Plan:** Review the semaphore handshakes between customers and tellers, then run the full simulation again. I wanted to make sure a teller does not begin serving another customer until its current customer leaves, and that the manager and safe limits are enforced.

**Reflection:** The program compiled with Java lint checks and completed a full run. The output showed all 50 customers leaving the bank, all 3 tellers leaving for the day, and the final bank-closing message. I also checked the trace: no more than 1 teller used the manager at once, and no more than 2 tellers were in the safe at once. The customer/teller semaphores kept the introduction, transaction request, transaction response, completion notice, and departure in order. That confirms the teller cannot reuse its service position before its current customer leaves.

## Session 3 - Final Requirements Audit

**Recorded:** Saturday, November 14, 2026 at 10:26:54 PM CST

**Plan:** Do a final pass against the project specification and rubric. I planned to compile with warnings enabled, run several complete simulations, verify the opening order, resource limits, shutdown behavior, and inspect the contents of the submission package.

**Reflection:** I compiled `Project2.java` with Java lint checks and completed five fresh simulation runs. In every run, all three tellers announced readiness before the first customer entered, all 50 customers left the bank, all three tellers terminated, and the bank-closing message was last. I checked the resource trace in each run: manager access never exceeded one teller and safe access never exceeded two tellers. The package contains `Project2.java`, `README.md`, `DEVLOG.md`, and `.gitignore`. The remaining submission task was to place these files in my course Git repository and make genuine commits as I continued working.

## Session 4 - Repository Setup and Submission Check

**Recorded:** Sunday, November 15, 2026 at 12:04:33 PM CST

**Plan:** Put the completed project in the course GitHub repository, confirm the repository has the required source and documentation files, and test the project again from that repository before preparing a commit.

**Reflection:** I added `Project2.java`, `README.md`, `DEVLOG.md`, and `.gitignore` to the repository. I compiled the project with lint checks and ran three new full simulations directly from the repository. Each run completed with 50 customer departures, 3 teller departures, the final bank-closing message, no more than 1 teller using the manager, and no more than 2 tellers in the safe. I also confirmed that generated class files are ignored, so the commit contains only the required source and documentation files. The repository's current commit is authored with my configured GitHub identity and is tracked on the `main` branch.

## Session 5 - Final Rubric Review and Trace Check

**Recorded:** Sunday, November 15, 2026 at 6:07:52 PM CST

**Plan:** Review the program against every project-specific requirement one more time, run the version stored in the Git repository, and verify that the documented submission files and Git state are consistent. I also wanted to identify whether any rubric wording required a change to the Java design.

**Reflection:** I compiled the repository copy with Java lint checks and ran a complete new trace. The trace showed all 50 customer transaction announcements and departures, all three teller threads serving and shutting down, and the bank-close message as the final line. The manager trace never had more than one teller interacting with it, and the safe trace never had more than two tellers inside it. The implementation uses threads, Java semaphores, and shared-memory communication exactly as the project specification requests: customer objects and the shared line carry state, while semaphores order access and conversation steps. The rubric's references to processes and pipes do not match this thread-based assignment; I did not add pipes because doing so would conflict with the required Java thread and shared-memory design. The repository is clean, the source and devlog are tracked, and the only generated files are ignored.
