package ua.dn.arhathq.snowball;

/**
 * @author Alexander Kuleshov
 */
public class JobException extends RuntimeException {
    public JobException(String message) {
        super(message);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }
}
