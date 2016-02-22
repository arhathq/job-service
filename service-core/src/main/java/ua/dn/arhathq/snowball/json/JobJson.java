package ua.dn.arhathq.snowball.json;


import ua.dn.arhathq.snowball.domain.Job;
import ua.dn.arhathq.snowball.domain.JobList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import java.io.Serializable;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JobJson {

    public static String getJobs(JobList jobList) {
        JsonArrayBuilder jobsBuilder = Json.createArrayBuilder();
        for (Job job: jobList.getJobs()) {
            JsonObjectBuilder jobBuilder = Json.createObjectBuilder().
                    add("id", job.getId()).
                    add("name", job.getName()).
                    add("status", job.getStatus() != null ? job.getStatus().name() : "").
                    add("lastStartDate", job.getLastStartDate() != null ? job.getLastStartDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "").
                    add("lastEndDate", job.getLastEndDate() != null ? job.getLastEndDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "").
                    add("nextStartDate", job.getNextStartDate() != null ? job.getNextStartDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "");
            jobsBuilder.add(jobBuilder);
        }
        return Json.createObjectBuilder().
                add("count", jobList.getJobCount()).
                add("data", jobsBuilder).
                build().toString();
    }

    public static String getJob(Job job) {
        JsonObjectBuilder parametersBuilder = Json.createObjectBuilder();
        for (Map.Entry<String, String> entry: job.getParameters().entrySet()) {
            parametersBuilder.add(entry.getKey(), entry.getValue() != null ? entry.getValue() : "");
        }

        JsonObjectBuilder jobBuilder = Json.createObjectBuilder().
                add("id", job.getId()).
                add("name", job.getName()).
                add("status", job.getStatus() != null ? job.getStatus().name() : "").
                add("lastStartDate", job.getLastStartDate() != null ? job.getLastStartDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "").
                add("lastEndDate", job.getLastEndDate() != null ? job.getLastEndDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "").
                add("nextStartDate", job.getNextStartDate() != null ? job.getNextStartDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "").
                add("cron", job.getCron() != null ? job.getCron() : "").
                add("parameters", parametersBuilder);

        return jobBuilder.build().toString();
    }

    public static String getAcknowledge(String jobId) {
        return Json.createObjectBuilder().add("id", jobId).add("ack", true).build().toString();
    }

    public static String getErrorMessage(String errorCode, String errorMessage) {
        return Json.createObjectBuilder().add("errorcode", errorCode).add("errormessage", errorMessage).build().toString();
    }

    public static Map<String, Serializable> parse(String payload) {
        JsonParser jsonParser = Json.createParser(new StringReader(payload));

        Map<String, Serializable> payloadData = new HashMap<>();
        try {
            HashMap<String, String> parameters = new HashMap<>();
            String cron = null;

            String key = null, obj = null;
            while(jsonParser.hasNext()) {

                JsonParser.Event event = jsonParser.next();
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    key = jsonParser.getString();
                } else if (event.equals(JsonParser.Event.VALUE_STRING)) {
                    String value = jsonParser.getString().isEmpty() ? null : jsonParser.getString();
                    if ("parameters".equals(obj)) {
                        parameters.put(key, value);
                    } else {
                        if ("cron".equals(key)) {
                            cron = value;
                        }
                    }
                } else if (event.equals(JsonParser.Event.START_OBJECT)) {
                    obj = key;
                } else if (event.equals(JsonParser.Event.END_OBJECT)) {
                    obj = null;
                    key = null;
                }
            }
            payloadData.put("parameters", parameters);
            payloadData.put("cron", cron);

            return payloadData;
        } finally {
            jsonParser.close();
        }

    }
}
