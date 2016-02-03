package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.quartz.listeners.SchedulerListenerSupport;
import ua.dn.arhathq.snowball.repository.JobRepository;

public class AppSchedulerListener extends SchedulerListenerSupport {

    private JobRepository jobRepository;

    private JobHelperMethods jobHelperMethods;

    public AppSchedulerListener(JobRepository jobRepository, JobHelperMethods jobHelperMethods) {
        this.jobRepository = jobRepository;
        this.jobHelperMethods = jobHelperMethods;
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        getLog().info("Job Deleted: " + jobHelperMethods.createJobId(jobKey));
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        getLog().info("Job Unscheduled: " + jobHelperMethods.createJobId(triggerKey));
    }
}
