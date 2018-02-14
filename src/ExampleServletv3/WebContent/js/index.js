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

app.controller('LoginController', [
	'$scope', '$http',
	function($scope, $http) {
	    $scope.valid = {
		username : false,
		password : false,
	    };
	    $scope.changed = false;
	    $scope.attempted = false;

	    $scope.submit = function() {
		$scope.changed = true;
		$scope.valid.username = ($scope.username != undefined)
			&& ($scope.username.length <= 10);
		$scope.valid.password = ($scope.password != undefined)
			&& ($scope.password.length <= 8);

		var valid = true;
		Object.keys($scope.valid).forEach(function(key) {
		    if (key == 'loginData') { return; }
		    if (!$scope.valid[key]) {
			valid = false;
			$scope.attempted = false;
		    }
		});

		if (valid) {
		    $scope.attempted = true;

		    $http.post(apiUrl + "/auth/login", {
			username: $scope.username,
			password: $scope.password,
		    }, function(data, status, headers, config) {
			alert(status);
		    })
		}
	    }
	} ]);