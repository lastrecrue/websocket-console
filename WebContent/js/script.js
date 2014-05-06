angular.module('YOUR_APP', [ 'angular-websocket', 'controllers' ]).config(function(WebSocketProvider) {
	WebSocketProvider.prefix('').uri('ws://localhost:8025/websockets/test');
});

angular.module('controllers', []).controller('MainCtrl', function($scope, WebSocket) {

	WebSocket.onopen(function() {
		console.log('connection');
		WebSocket.send('start')
	});

	WebSocket.onmessage(function(event) {
		console.log('message: ', event.data);
		$scope.scrambledWord = event.data;
		$scope.doClick = function() {
			WebSocket.send($scope.unScrambledWord)	
		}

	});

});