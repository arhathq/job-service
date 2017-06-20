steal('jquery',
	'can',
    function($, can) {

        return can.Model.extend({
            findAll: function(params) {
                var self = this;
                return $.get('/snowball/rest/jobs', params, undefined, "json").
                then(function(data) {
                    return self.models(data);
                });
            },
            findOne: "GET /snowball/rest/jobs/{id}",
            start: "POST /snowball/rest/jobs/{id}/start",
            stop: "POST /snowball/rest/jobs/{id}/stop"
        }, {});

    });