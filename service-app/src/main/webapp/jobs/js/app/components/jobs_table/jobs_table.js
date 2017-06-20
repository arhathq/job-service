steal('jquery',
    'can',
    'app/models/job.js',
    'app/components/jobs_table/jobs_table.mustache',
    'app/components/jobs_table/jobs_table.js',
    function ($, can, Job, JobsTableView) {

        var PageableModel = can.Map.extend({
            asc: true,
            sortBy: 'id',
            define: {
                count: {
                    type: "number",
                    value: Infinity,
                    // Keeps count above 0.
                    set: function(newCount) {
                        return newCount < 0 ? 0 : newCount;
                    }
                },
                offset: {
                    type: "number",
                    value: 0,
                    // Keeps offset between 0 and count
                    set: function(newOffset) {
                        var count = this.attr("count");
                        return newOffset < 0 ?
                            0 :
                            Math.min(newOffset, !isNaN( count - 1) ?
                                count - 1 :
                                Infinity);
                    }
                },
                limit: {
                    type: "number",
                    value: 5
                },
                page: {
                    // Setting page changes the offset
                    set: function(newVal) {
                        this.attr('offset', (parseInt(newVal) - 1) *
                            this.attr('limit'));
                    },
                    // The page value is derived from offset and limit.
                    get: function () {
                        return Math.floor(this.attr('offset') /
                                this.attr('limit')) + 1;
                    }
                }
            }
        });

        var JobViewModel = can.Map.extend({
            init : function() {
            },
            define: {
                items: {
                    get: function() {
                        var params = this.attr('params').serialize();
                        console.log('Getting jobs with params [' + JSON.stringify(params) + '] ...');
                        return Job.findAll(params).then(function(data) {
                            console.log('Returned jobs [' + JSON.stringify(data) + '] ...');
                            return data;
                        }, function(error) {
                            console.log('Error ' + error);
                        });
                    }
                },
                params: {
                    value: new PageableModel(),
                    set: function(newVal) {
                        return newVal;
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

        var template1 = JobsTableView({template: JobViewModel.items});

        can.Component.extend({
            tag: 'jobs-table',
            template: template1,
            viewModel: JobViewModel,
            helpers: {
                isNotFired: function(status) {
                    return status !== 'FIRED';
                }
            }
        });

    });