var JobDetailsViewModel = can.Map.extend({
	init : function() {
		var paremeters = new can.Map();
		this.attr('parameters', paremeters);
	},
	define: {
		jobDetails: {
			get: function() {
				var self = this;
				console.log('Getting Job Details [' + this.attr('id') + ']...');
				return Job.findOne({id: this.attr('id')}).done(function(job) {
					console.log("Job [" + job.id + "] was loaded");

					job.parameters.each(function(value, key) {
						console.log(key + "=" + value);
						var parameters = self.attr('parameters');
						parameters.attr(key, value);
					});

				});
			}
		}
	},
	runJob: function() {
		var id = this.attr('id');
		var parameters = this.attr('parameters');
		var data = new can.Map({parameters: parameters});
		console.log('Starting Job [' + id + '] with parameters:');
		console.log(data.serialize());

		can.ajax({
			url: '/snowball/rest/jobs/' + id + '/start',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(data.serialize()),
			success: function(data, status, request) {
				Job.findOne({id: id}).done(function(job) {
					console.log("Job [" + job.id + "] was started");
				});
			},
			error: function(request, status, statusMsg) {
				console.log("Job execution failed [" + status + "]; " + statusMsg);
			}
		});
	},
	editParameter: function(parameter) {
		var value = $('#form-parameter-' + parameter).val();
		var parameters = this.attr('parameters');
		parameters.attr(parameter, value);
	},
	isNotNullParameter: function(value) {
		return value !== undefined && value !== 'null';
	}
});

can.Component.extend({
	tag: 'job-form',
  	viewModel: JobDetailsViewModel,
  	template: can.view('js/app/components/job_form/job_form.stache'),
});