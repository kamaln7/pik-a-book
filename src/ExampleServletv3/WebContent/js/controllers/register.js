app.controller('RegisterController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.submitted = false;
	    $scope.error = "";
	    $scope.telephonepre = "050";

	    $scope.submit = function() {
		var telephone = $scope.telephonepre + $scope.phone;
	    }
	} ]);