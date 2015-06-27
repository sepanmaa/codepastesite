var c = angular.module('pasteControllers', []);

c.controller('PasteCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {
    $scope.paste = {
	visibility: 'public',
	syntax: 'auto',
	expires: 'never'
    };
    $scope.languages = [
	{value: "nohighlight", name: "None"},
	{value: "c++", name: "C++"},
	{value: "clojure", name: "Clojure"},
	{value: "haskell", name: "Haskell"},
	{value: "java", name: "Java"},
	{value: "javascript", name: "JavaScript"},
	{value: "scala", name: "Scala"},
	{value: "scheme", name: "Scheme"},
	{value: "python", name: "Python"},
	{value: "ruby", name: "Ruby"},
	{value: "auto", name: "Automatically detect"}
    ];
    $scope.times = [
	{value: "never", name: "Never"},
	{value: "10min", name: "10 minutes"},
	{value: "1d", name: "1 day"},
	{value: "1w", name: "1 week"},
	{value: "2w", name: "2 weeks"}
    ];
    $http.get('/api/pastes').success(function(data) {
	$scope.pastes = data;
    });
    $scope.send = function(paste) {
	$scope.paste = paste
	$http.post('/api/pastes', $scope.paste).
	    success(function(data, status, headers, config) {
		$location.path('/pastes/' + data.id);
	    });
    }
}]);

c.controller('PasteDetailCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
    $http.get('/api/pastes/' + $routeParams.pasteId).success(function(data) {
	$scope.paste = data;

	if (data.syntax == "auto")
	    $scope.syntax = null;
	else if (hljs.listLanguages().indexOf(data.syntax) != -1)
	    $scope.syntax = data.syntax;
	else {
	    $scope.syntax = null;
	    // workaround because nohighlight class doesn't seem to work with angular-highlightjs
	    $scope.nohighlight = true;
	}
    });
}]);

