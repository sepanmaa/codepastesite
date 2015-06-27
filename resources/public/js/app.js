var app = angular.module('codepasteapp', [
    'hljs',
    'monospaced.elastic',
    'ngRoute',
    'pasteControllers'
]);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
	when('/pastes', {
	    templateUrl: 'partials/pastes.html',
	    controller: 'PasteCtrl'
	}).
	when('/pastes/:pasteId', {
	    templateUrl: 'partials/pastedetail.html',
	    controller: 'PasteDetailCtrl'
	}).
	otherwise({
	    redirectTo: '/pastes'
	});
}]);
	
	    
