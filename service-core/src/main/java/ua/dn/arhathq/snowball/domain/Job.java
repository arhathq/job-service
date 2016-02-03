package ua.dn.arhathq.snowball.domain;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Job {

    private String id;

    private String name;
    private String group;
    private String description;
    private String jobClass;
    private String cron;
    private Map<String, String> parameters = new HashMap<>();

    private JobStatus status = JobStatus.STOPPED;

    private ZonedDateTime lastStartDate;
    private ZonedDateTime lastEndDate;
    private ZonedDateTime nextStartDate;

    public Job(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public ZonedDateTime getLastStartDate() {
        return lastStartDate;
    }

    public void setLastStartDate(ZonedDateTime lastStartDate) {
        this.lastStartDate = lastStartDate;
    }

    public ZonedDateTime getLastEndDate() {
        return lastEndDate;
    }

    public void setLastEndDate(ZonedDateTime lastEndDate) {
        this.lastEndDate = lastEndDate;
    }

    public ZonedDateTime getNextStartDate() {
        return nextStartDate;
    }

    public void setNextStartDate(ZonedDateTime nextStartDate) {
        this.nextStartDate = nextStartDate;
    }
}