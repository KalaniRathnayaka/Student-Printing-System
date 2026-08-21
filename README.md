Student Printing System

A concurrent Java printing system that simulates students submitting print jobs while a printer and technician work concurrently.

Technologies / Concepts Used
Java Threads
BlockingQueue
ReentrantLock
AtomicBoolean
Thread synchronization
Shared resource management
Thread interruption
Classes
PrintJob – Represents a student's print request.
Printer – Processes print jobs and manages the available paper.
Technician – Refills the printer when required.
PrintingSystem – Main class that creates the print jobs and starts the printer and technician threads.
