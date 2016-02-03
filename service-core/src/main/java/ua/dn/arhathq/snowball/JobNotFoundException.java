package ua.dn.arhathq.snowball;

/**
 * @author Alexander Kuleshov
 */
public class JobNotFoundException extends JobException {
    public JobNotFoundException(String id) {
        super("Job [" + id + "] not found");
    }
}
