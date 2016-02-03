package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import ua.dn.arhathq.snowball.repository.JobRepository;

public class AppTriggerListener extends TriggerListenerSupport {

    private JobRepository jobRepository;

    private JobHelperMethods jobHelperMethods;

    public AppTriggerListener(JobRepository jobRepository, JobHelperMethods jobHelperMethods) {
        this.jobRepository = jobRepository;
        this.jobHelperMethods = jobHelperMethods;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        getLog().info("Trigger misfired for job " + jobHelperMethods.createJobId(trigger.getKey()));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        getLog().info("Trigger completed for job " + jobHelperMethods.createJobId(trigger.getKey()));
    }
}
