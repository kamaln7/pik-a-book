app.controller('LoginController', [ '$scope', '$http', function($scope, $http) {
    $scope.submitted = false;
    $scope.error = "";

    $scope.submit = function() {
	$scope.submitted = true;

	$http.post(apiUrl + "/auth/login", JSON.stringify({
	    username : $scope.username,
	    password : $scope.password,
	})).then(function(res) {
	    $scope.state.username = res.data.username;
	    $scope.state.user_id = res.data.id;
	    $scope.state.is_admin = res.data.is_admin;
	    $scope.state.authed = true;

	    $scope.redirect('home');
	}, function(res) {
	    $scope.error = res.data.message;
	});
    }
} ]);