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
    public JobKey createJobKey(String jobId) {
        String[] idParts= jobId.split(DELIMITER);
        return JobKey.jobKey(idParts[0], idParts[1]);
    }

    @Override
    public TriggerKey createTriggerKey(String jobId) {
        String[] idParts= jobId.split(DELIMITER);
        return TriggerKey.triggerKey(idParts[0], idParts[1]);
    }

    @Override
    public String createJobId(Key<?> key) {
        return String.format("%s%s%s", key.getGroup(), DELIMITER, key.getName());
    }

}
