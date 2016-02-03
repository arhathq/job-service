package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.listeners.JobListenerSupport;
import ua.dn.arhathq.snowball.domain.Job;
import ua.dn.arhathq.snowball.domain.JobStatus;
import ua.dn.arhathq.snowball.repository.JobRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class AppJobListener extends JobListenerSupport {

    private JobRepository jobRepository;

    private JobHelperMethods jobHelperMethods;

    public AppJobListener(JobRepository jobRepository, JobHelperMethods jobHelperMethods) {
        this.jobRepository = jobRepository;
        this.jobHelperMethods = jobHelperMethods;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobId = jobHelperMethods.createJobId(context.getTrigger().getKey());
        Job job = jobRepository.getJob(jobId);
        if (job != null) {
            job.setLastStartDate(context.getTrigger().getPreviousFireTime().toInstant().atZone(ZoneId.systemDefault()));
            job.setLastEndDate(null);
            job.setNextStartDate(null);
            job.setStatus(JobStatus.FIRED);
            jobRepository.saveJob(job);
            getLog().info(String.format("job %s started", job.getName()));
        }
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        Trigger trigger = context.getTrigger();
        String jobId = jobHelperMethods.createJobId(context.getTrigger().getKey());
        Job job = jobRepository.getJob(jobId);
        if (job != null) {
            job.setLastEndDate(ZonedDateTime.now());
            job.setNextStartDate(getNextFireTime(context));

            if (jobException != null) {
                job.setStatus(JobStatus.FAILED);
            } else if (job.getNextStartDate() != null) {
                job.setStatus(JobStatus.PENDING);
            } else {
                job.setStatus(JobStatus.STOPPED);
            }

            getLog().debug(String.format("job %s ended", job.getId()));
            jobRepository.saveJob(job);
        } else {
            try {
                context.getScheduler().unscheduleJob(trigger.getKey());
            } catch (Exception e) {
                getLog().error(e.getMessage(), e);
            }
        }
    }

    public ZonedDateTime getNextFireTime(JobExecutionContext context) {
        ZonedDateTime res = null;
        try {
            List<? extends Trigger> triggers = context.getScheduler().getTriggersOfJob(context.getJobDetail().getKey());
            Date nextFireTime = new Date();
            if (context.getFireTime() != null) {
                nextFireTime = context.getFireTime();
            }
            for (Trigger trigger: triggers) {
                Date iterDate = trigger.getNextFireTime();
                if (iterDate != null && nextFireTime.getTime() < iterDate.getTime()) {
                    nextFireTime = iterDate;
                    res = ZonedDateTime.ofInstant(nextFireTime.toInstant(), ZoneId.systemDefault());
                }
            }
        } catch (Exception e) {
            getLog().error(e.getMessage(), e);
            return null;
        }
        return res;
    }
}
