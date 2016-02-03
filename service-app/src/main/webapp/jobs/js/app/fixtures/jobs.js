/*
var job_list = [
	{id: 'job-id1', name: 'Job1', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: ''},
	{id: 'job-id2', name: 'Job2', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: ''},
	{id: 'job-id3', name: 'Job3', status: 'FIRED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '21-01-2016T12:00:00+0300'},
	{id: 'job-id4', name: 'Job4', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: ''}									
];

var job_details = [
	{id: 'job-id1', name: 'Job1', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* * * * *', params: {p1: '/', p2: 'enc'}},
	{id: 'job-id2', name: 'Job2', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* * * * ?', params: {p3: 'true', p5: '1', encoding: 'UTF-8'}},
	{id: 'job-id3', name: 'Job3', status: 'FIRED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '21-01-2016T12:00:00+0300', cron: '* 0/1 * * ?', params: {ast: 'str', file: 'assist'}},
	{id: 'job-id4', name: 'Job4', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* 1 * * *', params: {foo: 'true', bar: '1'}}
];

function getJob(id) {
	var job = undefined; 
	$.each(job_list, function(index, el) {
		if (id === el.id) {
			job = el;
		}
    }); 
    return job;
};

function getJobDetails(id) {
	var job = undefined; 
	$.each(job_details, function(index, el) {
		if (id === el.id) {
			job = el;
		}
    }); 
    return job;
}
*/

var job_repo = new can.Map({
	'job-id1': {id: 'job-id1', name: 'Job1', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* * * * *', params: {p1: '/', p2: 'enc'}},
	'job-id2': {id: 'job-id2', name: 'Job2', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* * * * ?', params: {p3: 'true', p5: '1', encoding: 'UTF-8'}},
	'job-id3': {id: 'job-id3', name: 'Job3', status: 'FIRED',   lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '21-01-2016T12:00:00+0300', cron: '* 0/1 * * ?', params: {ast: 'str', file: 'assist'}},
	'job-id4': {id: 'job-id4', name: 'Job4', status: 'STOPPED', lastStartDate: '21-12-2015T12:34:00+0300', nextStartDate: '', cron: '* 1 * * *', params: {foo: 'true', bar: '1'}}
});

can.fixture({
	"GET /action/jobs": function() {
		console.log("Returning Job List");
		var jobs = [];
		job_repo.each(function(value, key) {
		    jobs.push(value);
		});

		return jobs;
	},
	"GET /action/jobs/{id}": function(request, response) {
		var id = request.data.id;
		console.log("Return Job [" + id + "]");

        var job = job_repo.attr(request.data.id); 
        var res;
		res = job;
        console.log(res);
		return res;
	},
	"POST /action/jobs/{id}/start": function(request, response) {
		console.log("Job [" + request.data.id + "] started ACK");
		var job = job_repo.attr(request.data.id); 
		if (job === undefined) {
			response(404,"{result: 'Job " + request.data.id + " not found'}");
		} else {
			job.status = "FIRED";
			job.lastStartDate = "06-01-2016T22:00:00+0300";
			job.params = request.data.params;
			job.cron = request.data.cron;
			console.log(job);
			job_repo.attr(job.id, job);
			response(200,"{result: 'Job " + request.data.id + " started'}");
		}
	}
});