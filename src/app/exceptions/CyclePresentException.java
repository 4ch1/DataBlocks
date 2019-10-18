package app.exceptions;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Custom exception to indicate cycle errors in schema.
 */


public class CyclePresentException extends Exception {
    private final String causedByBlockId;

    /**
     * @param message Message of the exception. Passed to java.lang.Exception.
     * @param causedByBlock Block that is reason of cycle.
     */
    public CyclePresentException(String message, String causedByBlock) {
        super(message);
        this.causedByBlockId = causedByBlock;
    }

    public String getCausedByBlockId() {
        return causedByBlockId;
    }
}
