package ua.dn.arhathq.snowball.repository;

import ua.dn.arhathq.snowball.core.FilterOptions;
import ua.dn.arhathq.snowball.domain.Job;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class JobRepositoryImpl implements JobRepository {

    private AtomicInteger idGenerator = new AtomicInteger();

    private Map<String, Job> jobs = new ConcurrentHashMap<>();

    public JobRepositoryImpl() {
        mockJobs();
    }

    @Override
    public Collection<Job> getJobList(FilterOptions filter) {
        return jobs.values();
    }

    @Override
    public int getJobCount() {
        return jobs.size();
    }

    @Override
    public Job getJob(String id) {
        return jobs.get(id);
    }

    @Override
    public void saveJob(Job job) {
        jobs.put(job.getId(), job);
    }

    private void mockJobs() {
        Job job1 = new Job(String.valueOf(idGenerator.incrementAndGet()));
        job1.setName("JobA");
        job1.setJobClass("jobs.JobA");
        job1.setCron("* * * * * ?");

        Map<String, String> job1Params = new HashMap<>();
        job1Params.put("param1", "11");
        job1Params.put("param2", "/srv/path/www");
        job1.setParameters(job1Params);
        jobs.put(job1.getId(), job1);

        Job job2 = new Job(String.valueOf(idGenerator.incrementAndGet()));
        job2.setName("JobB");
        job2.setJobClass("jobs.JobB");

        Map<String, String> job2Params = new HashMap<>();
        job2Params.put("param3", "true");
        job2Params.put("param4", "UTF-8");
        job2Params.put("param5", "10.050");
        job2.setParameters(job2Params);

        jobs.put(job2.getId(), job2);
    }
}