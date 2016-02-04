package ua.dn.arhathq.snowball.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dn.arhathq.snowball.core.FilterOptions;
import ua.dn.arhathq.snowball.domain.Job;
import ua.dn.arhathq.snowball.domain.JobList;
import ua.dn.arhathq.snowball.json.JobJson;
import ua.dn.arhathq.snowball.JobNotFoundException;
import ua.dn.arhathq.snowball.service.JobService;
import ua.dn.arhathq.snowball.LaunchOptions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
public class JobsController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Object> jobs(@ModelAttribute FilterOptions filterOptions) {
        try {
            JobList jobList = jobService.getJobList(filterOptions);
            String json = JobJson.getJobs(jobList);

            return new ResponseEntity<>(json, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("[GET /jobs] - Error: " + e.getMessage(), e);
            return new ResponseEntity<>("{error: \"" + e.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Object> job(@PathVariable String id) {
        try {
            Job job = jobService.getJob(id);
            String json = JobJson.getJob(job);

            return new ResponseEntity<>(json, HttpStatus.OK);

        } catch (JobNotFoundException e) {
            logger.error("[GET /jobs/{id}] - Error: " + e.getMessage());
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("[GET /jobs/{id}] - Error: " + e.getMessage(), e);
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jobs/{id}/start", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> start(@PathVariable String id, @RequestBody String payload) {
        try {

            Map<String, Serializable> payloadData = JobJson.parse(payload);
            Map<String, Serializable> params = new HashMap<>();
            for (Map.Entry<String, Serializable> entry: payloadData.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
            jobService.start(id, params, new LaunchOptions()); //todo: implement launch options

            String json = JobJson.getAcknowledge(id);
            return new ResponseEntity<>(json, HttpStatus.OK);

        } catch (JobNotFoundException e) {
            logger.error("[POST /jobs/{id}/start] - Error: " + e.getMessage());
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("[POST /jobs/{id}/start] - Error: " + e.getMessage(), e);
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jobs/{id}/stop", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> stop(@PathVariable String id) {
        try {
            jobService.stop(id);

            String json = JobJson.getAcknowledge(id);
            return new ResponseEntity<>(json, HttpStatus.OK);

        } catch (JobNotFoundException e) {
            logger.error("[POST /jobs/{id}/stop] - Error: " + e.getMessage());
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("[POST /jobs/{id}/stop] - Error: " + e.getMessage(), e);
            String jsonError = JobJson.getErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage());
            return new ResponseEntity<>(jsonError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}