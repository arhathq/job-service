package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.quartz.utils.Key;

/**
 * @author Alexander Kuleshov
 */
public class JobHelperMethodsImpl implements JobHelperMethods {

    public static final String DELIMITER = "||";

    @Override
    public JobKey createJobKey(String jobId) throws InvalidFormatJobIdException {
        String[] idParts= getIdParts(jobId);
        return JobKey.jobKey(idParts[0], idParts[1]);
    }

    @Override
    public TriggerKey createTriggerKey(String jobId) throws InvalidFormatJobIdException {
        String[] idParts= getIdParts(jobId);
        return TriggerKey.triggerKey(idParts[0], idParts[1]);
    }

    @Override
    public String createJobId(Key<?> key) {
        return String.format("%s%s%s", key.getGroup(), DELIMITER, key.getName());
    }

    private String[] getIdParts(String id) throws InvalidFormatJobIdException {
        String[] idParts= id.split(DELIMITER);
        if (idParts.length != 2) {
            throw new InvalidFormatJobIdException(id);
        }
        return idParts;
    }

}