var JobViewModel = can.Map.extend({
	define: {
		items: {
			get: function() {
				console.log('Getting jobs...');
				return Job.findAll();
			}
		}
	},
	startJob: function(id) {
		console.log('Showing form for job [' + id + ']');
		can.view(can.stache("<job-form {id}='{id}'></job-form>"), {id: id}, function(frag) {
			$('#run-modal-' + id).html(frag);
		});
    },
	stopJob: function(id) {
		console.log('Stopping Job [' + id + ']...');
		can.ajax({
			url: '/snowball/rest/jobs/' + id + '/stop',
			method: 'POST',
			contentType: 'application/json',
			success: function(data, status, request) {
				Job.findOne({id: id}).
				done(function(job) {
					console.log("Job [" + job.id + "] was stopped");
				});
			},
			error: function(request, status, statusMsg) {
				console.log("Job execution failed [" + status + "]; " + statusMsg);
			}
		});

	}
});

can.Component.extend({
	tag: 'jobs-table',
	template: can.view('js/app/components/jobs_table/jobs_table.stache'),
	viewModel: JobViewModel,
  	helpers: {
		isNotFired: function(status) {
			return status !== 'FIRED';
  	    }
  	}
});