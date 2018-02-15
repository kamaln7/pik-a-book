var app = angular.module('booksforall', []);
var apiUrl = '/ExampleServletv3';

app.factory('State', function() {
    return {};
});

app.factory('Redirect', function() {
    return function($scope, page) {
	$scope.currentPage = page;
    };
})

app.controller('MainController', [ '$scope', 'State', 'Redirect',
	function($scope, State, Redirect) {
	    $scope.state = State;

	    $scope.$watch('state', function(state) {
		localStorage.setItem("State", JSON.stringify(state));
	    }, true);

	    var savedState = localStorage.getItem("State");
	    if (savedState == null) {
		savedState = {
		    authed : false,
		};
	    } else {
		savedState = JSON.parse(savedState);
	    }
	    $scope.state = savedState;

	    $scope.currentPage = 'home';
	    $scope.redirect = Redirect.bind(this, $scope);
	} ]);