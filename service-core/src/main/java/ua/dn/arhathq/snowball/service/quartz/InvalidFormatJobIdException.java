package ua.dn.arhathq.snowball.service.quartz;

import ua.dn.arhathq.snowball.JobException;

/**
 * @author Alexander Kuleshov
 */
public class InvalidFormatJobIdException extends JobException {
    public InvalidFormatJobIdException(String id) {
        super(String.format("Invalid format of job id: %s", id));
    }
}