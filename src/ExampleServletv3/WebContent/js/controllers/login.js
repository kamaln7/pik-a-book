app.controller('LoginController', [ '$scope', '$http', function($scope, $http) {
    $scope.submitted = false;
    $scope.error = "";

    $scope.submit = function() {
	$scope.submitted = true;

	$http.post(apiUrl + "/auth/login", {
	    username : $scope.username,
	    password : $scope.password,
	}).then(function(res) {
	    $scope.reloadAuthState();
	    $scope.redirect('home');
	}, function(res) {
	    $scope.error = res.data.message;
	});
    }
} ]);