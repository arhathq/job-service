package ua.dn.arhathq.snowball.repository;

import ua.dn.arhathq.snowball.core.FilterOptions;
import ua.dn.arhathq.snowball.domain.Job;

import java.util.Collection;

public interface JobRepository {

    Collection<Job> getJobList(FilterOptions filter);

    int getJobCount();

    Job getJob(String id);

    void saveJob(Job job);

}
