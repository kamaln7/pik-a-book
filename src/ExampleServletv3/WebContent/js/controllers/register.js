app.controller('RegisterController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.submitted = false;
	    $scope.error = "";

	    $scope.submit = function() {
		alert("register!");
	    }
	} ]);