var Job = can.Model.extend({
	findAll: "GET /snowball/rest/jobs",
	findOne: "GET /snowball/rest/jobs/{id}"
}, {});