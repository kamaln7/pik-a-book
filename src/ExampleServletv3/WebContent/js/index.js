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

app.controller('LoginController', [ '$scope', '$http', function($scope, $http) {
    $scope.submitted = false;
    $scope.error = "";

    $scope.submit = function() {
	$scope.submitted = true;

	$http.post(apiUrl + "/auth/login", JSON.stringify({
	    username : $scope.username,
	    password : $scope.password,
	})).then(function(data, status, headers, config) {
	    alert('success');
	}, function(res) {
	    $scope.error = res.data.message;
	});
    }
} ]);