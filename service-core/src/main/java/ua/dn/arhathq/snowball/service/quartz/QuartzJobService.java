package ua.dn.arhathq.snowball.service.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.dn.arhathq.snowball.JobNotFoundException;
import ua.dn.arhathq.snowball.core.FilterOptions;
import ua.dn.arhathq.snowball.LaunchOptions;
import ua.dn.arhathq.snowball.domain.Job;
import ua.dn.arhathq.snowball.domain.JobList;
import ua.dn.arhathq.snowball.repository.JobRepository;
import ua.dn.arhathq.snowball.service.*;
import ua.dn.arhathq.snowball.JobExecutionException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class QuartzJobService implements JobService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ReentrantLock lock = new ReentrantLock();

    private Scheduler scheduler;

    private JobRepository jobRepository;

    private JobHelperMethods jobHelperMethods;

    public QuartzJobService(Scheduler scheduler, JobRepository jobRepository, JobHelperMethods jobHelperMethods) {
        this.scheduler = scheduler;
        this.jobRepository = jobRepository;
        this.jobHelperMethods = jobHelperMethods;
    }

    @Override
    public JobList getJobList(FilterOptions filter) {

        Collection<Job> jobList = jobRepository.getJobList(filter);
        int jobCount = jobRepository.getJobCount();

        return new JobList(jobList, jobCount);
    }

    @Override
    public Job getJob(String id) throws JobNotFoundException {

        Job job = jobRepository.getJob(id);
        if (job == null) {
            throw new JobNotFoundException(id);
        }
        return job;
    }

    @Override
    public void start(String id, Map<String, Object> params, LaunchOptions options) throws JobNotFoundException, JobExecutionException {

        Job job  = jobRepository.getJob(id);
        if (job == null) {
            throw new JobNotFoundException(id);
        }

        TriggerKey triggerKey = jobHelperMethods.createTriggerKey(job.getId());
        JobKey jobKey = jobHelperMethods.createJobKey(job.getId());

        lock.lock();
        try {

            Trigger trigger = scheduler.getTrigger(triggerKey);
            if(null == trigger) {
                Class<? extends org.quartz.Job> jobClass = (Class<? extends org.quartz.Job>) Class.forName(job.getJobClass());
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
                jobDetail.getJobDataMap().putAll(params);

                TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(triggerKey);
                if(options.getCron() != null) {
                    triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(options.getCron()));
                } else if (options.getStartAt() != null) {
                    // todo: Implement
//                    triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(DateBuilder.futureDate());
                } else {
                    triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule()).startNow();
                }
                scheduler.scheduleJob(jobDetail, triggerBuilder.build());
            } else {
                scheduler.rescheduleJob(triggerKey, trigger);
            }

        } catch (SchedulerException e) {
            String err = String.format("Scheduler error: %s", e.getMessage());
            logger.error(err, e);
            throw new JobExecutionException(err, e);
        } catch (ClassNotFoundException e) {
            String err = String.format("Job class not found: %s", job.getJobClass());
            logger.error(err, e);
            throw new JobExecutionException(err);
        } catch (Exception e) {
            String err = String.format("Error: '%s'", options.getCron());
            logger.error(err, e);
            throw new JobExecutionException(err);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop(String id) throws JobNotFoundException, JobExecutionException {

        Job job  = jobRepository.getJob(id);
        if (job == null) {
            throw new JobNotFoundException(id);
        }

        TriggerKey triggerKey = jobHelperMethods.createTriggerKey(job.getId());

        try {
            boolean unscheduled = scheduler.unscheduleJob(triggerKey);
            if (!unscheduled) {
                List listeners = scheduler.getListenerManager().getSchedulerListeners();
                for (Object o : listeners) {
                    SchedulerListener listener = (SchedulerListener) o;
                    listener.jobUnscheduled(triggerKey);
                }
            }
        } catch (SchedulerException e) {
            String err = "Scheduler error: " + e.getMessage();
            logger.error(err, e);
            throw new JobExecutionException(err, e);
        }
    }

}