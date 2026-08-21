package printingsystem;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Technician thread periodically attempts to refill printer paper.
 */
public class Technician implements Runnable {

    private final Printer printer;
    private final AtomicBoolean printingFinished;

    public Technician(Printer printer, AtomicBoolean printingFinished) {
        this.printer = printer;
        this.printingFinished = printingFinished;
    }

    @Override
    public void run() {
        try {
            while (!printingFinished.get()) {

                // Technician attempts refill periodically
                Thread.sleep(6000);

                if (!printingFinished.get()) {
                    printer.refillPaper();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Technician thread stopped.");
    }
}