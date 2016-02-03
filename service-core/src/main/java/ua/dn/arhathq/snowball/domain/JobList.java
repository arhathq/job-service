package ua.dn.arhathq.snowball.domain;

import java.util.Collection;

/**
 * @author Alexander Kuleshov
 */
public class JobList {

    private Collection<Job> jobs;
    private int jobCount;

    public JobList(Collection<Job> jobs, int jobCount) {
        this.jobs = jobs;
        this.jobCount = jobCount;
    }

    public Collection<Job> getJobs() {
        return jobs;
    }

    public int getJobCount() {
        return jobCount;
    }
}
