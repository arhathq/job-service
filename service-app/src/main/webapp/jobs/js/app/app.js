$(function () {
//can.fixture.delay = 1000;

  	var AppState = can.Map.extend({});

  	var appState = new AppState();

  	$('#jobs-main').html(can.view('js/app/templates/jobs-list.stache', appState));

	can.route(':page', { page: 'jobs' });

  	can.route.map(appState);

  	can.route.ready();

	var fromEvent = Rx.Observable.fromEvent;

	var ws = new WebSocket("ws://localhost:8080/snowball/jobs");
	//var ws = new WebSocket("ws://wiki-update-sockets.herokuapp.com/");

	var openStream = fromEvent(ws, 'open');
	var closeStream = fromEvent(ws,'close');

	var messageStream = fromEvent(ws, 'message').delaySubscription(openStream).takeUntil(closeStream);

	openStream.subscribe(function () {
		console.log("Connection opened");
	});

	closeStream.subscribe(function () {
		console.log("Connection is closed...");
	});

	var updateStream = messageStream.map(function(event) {
		console.log("Update Stream " + event);
		var dataString = event.data;
		return JSON.parse(dataString);
	});

	// Calculate the rate of updates over time
	var updateCount = updateStream.scan(0, function(value) {
		return ++value;
	});

	var samplingTime = 2000;
	var sampledUpdates = updateCount.sample(samplingTime);

	sampledUpdates.subscribe(function(value) {
		console.log(value);
	});
});