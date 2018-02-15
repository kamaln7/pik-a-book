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