var app = angular.module('booksforall', []);

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
		console.log(state);
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

app.controller('NavController', [ '$scope', function($scope) {
    $scope.logout = function() {
	$scope.state.authed = false;
	$scope.redirect('home');
	$scope.state.username = $scope.state.user_id = null;
    }
} ]);

app.controller('HomeController', [ '$scope', function($scope) {

    $scope.name = 'Steve';

} ]);
