package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.quartz.utils.Key;

public interface JobHelperMethods {

    String createJobId(Key<?> key);

    JobKey createJobKey(String jobId);

    TriggerKey createTriggerKey(String jobId);

}
