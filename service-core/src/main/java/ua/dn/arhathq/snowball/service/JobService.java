package ua.dn.arhathq.snowball.service;

import ua.dn.arhathq.snowball.JobExecutionException;
import ua.dn.arhathq.snowball.JobNotFoundException;
import ua.dn.arhathq.snowball.core.FilterOptions;
import ua.dn.arhathq.snowball.LaunchOptions;
import ua.dn.arhathq.snowball.domain.Job;
import ua.dn.arhathq.snowball.domain.JobList;

import java.io.Serializable;
import java.util.Map;

public interface JobService {

    JobList getJobList(FilterOptions filter);

    Job getJob(String id) throws JobNotFoundException;

    void start(String id, Map<String, Serializable> params, LaunchOptions options) throws JobNotFoundException, JobExecutionException;

    void stop(String id) throws JobNotFoundException, JobExecutionException;

}