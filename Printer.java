package printingsystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Printer thread that retrieves print jobs from the queue
 * and prints them if enough paper is available.
 */
public class Printer implements Runnable {

    private final BlockingQueue<PrintJob> printQueue;
    private final ReentrantLock printerLock;
    private final AtomicBoolean printingFinished;

    private int availablePages = 100;

    public Printer(BlockingQueue<PrintJob> printQueue,
                   ReentrantLock printerLock,
                   AtomicBoolean printingFinished) {
        this.printQueue = printQueue;
        this.printerLock = printerLock;
        this.printingFinished = printingFinished;
    }

    @Override
    public void run() {
        try {
            while (true) {

                // poll waits briefly, then returns null if queue is empty
                PrintJob job = printQueue.poll(2, TimeUnit.SECONDS);

                if (job == null) {
                    System.out.println("Print queue exhausted.");
                    break;
                }

                /**
                 * Lock ensures printer and technician cannot access
                 * printer paper count at the same time.
                 */
                printerLock.lock();
                try {
                    if (availablePages >= job.getPages()) {

                        System.out.println("Started printing "
                                + job.getPages() + " pages for "
                                + job.getStudentName());

                        // Printing time proportional to number of pages
                        Thread.sleep(job.getPages() * 50L);

                        availablePages -= job.getPages();

                        System.out.println("Completed printing for "
                                + job.getStudentName()
                                + ". Pages remaining: "
                                + availablePages);

                    } else {
                        System.out.println("Skipped job for "
                                + job.getStudentName()
                                + " (" + job.getPages()
                                + " pages). Not enough paper. Remaining: "
                                + availablePages);
                    }
                } finally {
                    printerLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printingFinished.set(true);
        System.out.println("Printer thread finished.");
    }

    /**
     * Refill printer paper to full capacity.
     * This method is called by technician thread.
     */
    public void refillPaper() {
        printerLock.lock();
        try {
            System.out.println("Technician started refilling printer...");
            Thread.sleep(1000);

            availablePages = 100;

            System.out.println("Technician refilled printer to 100 pages.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            printerLock.unlock();
        }
    }
}