package printingsystem;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class PrintingSystem {

    public static void main(String[] args) {

        BlockingQueue<PrintJob> printQueue = new LinkedBlockingQueue<>();
        ReentrantLock printerLock = new ReentrantLock(true);
        AtomicBoolean printingFinished = new AtomicBoolean(false);

        PrintJob[] jobs = {
                new PrintJob("Kalani", 70),
                new PrintJob("Nimal", 60),     // should be skipped before refill
                new PrintJob("Amal", 40),      // should be skipped before refill
                new PrintJob("Saman", 30),     // should print with remaining paper
                new PrintJob("Dilani", 90),    // should print after technician refill
                new PrintJob("Kamal", 95),     // should print after next refill
                new PrintJob("InvalidStudent", 120)
        };

        for (PrintJob job : jobs) {
            if (job.isValid()) {
                try {
                    printQueue.put(job);
                    System.out.println("Print job submitted: "
                            + job.getStudentName()
                            + " requested " + job.getPages()
                            + " pages");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("Invalid print job rejected for "
                        + job.getStudentName());
            }
        }

        Printer printer = new Printer(printQueue, printerLock, printingFinished);
        Technician technician = new Technician(printer, printingFinished);

        Thread printerThread = new Thread(printer, "Printer-Thread");
        Thread technicianThread = new Thread(technician, "Technician-Thread");

        printerThread.start();
        technicianThread.start();

        try {
            printerThread.join();
            technicianThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nAll print jobs processed or skipped.");
    }
}