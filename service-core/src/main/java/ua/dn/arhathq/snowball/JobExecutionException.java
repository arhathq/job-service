package ua.dn.arhathq.snowball;

/**
 * @author Alexander Kuleshov
 */
public class JobExecutionException extends JobException {
    public JobExecutionException(String message) {
        super(message);
    }

    public JobExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
