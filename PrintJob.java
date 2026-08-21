package printingsystem;

/**
 * Represents a student print job.
 */
public class PrintJob {

    private final String studentName;
    private final int pages;

    public PrintJob(String studentName, int pages) {
        this.studentName = studentName;
        this.pages = pages;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getPages() {
        return pages;
    }

    /**
     * Validates print job before adding to queue.
     * Page count must be between 1 and 100.
     */
    public boolean isValid() {
        return studentName != null && !studentName.isBlank()
                && pages > 0 && pages <= 100;
    }
}
