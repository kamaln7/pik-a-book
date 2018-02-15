var app = angular.module('registerApp', []);
var apiUrl = '/ExampleServletv3';


app.controller('RegisterController', [
	'$scope', '$http',
	function($scope, $http) {
	    $scope.valid = {
		username : false,
		password : false,
		city : false,
		street : false,
		sNumber : false,
		zip : false,
		phone : false,
	    };
	    $scope.changed = false;
	    $scope.attempted = false;
	    $scope.submit = function() {
			$scope.changed = true;
			$scope.valid.username = ($scope.username != undefined)
				&& ($scope.username.length <= 10);
			$scope.valid.password = ($scope.password != undefined)
				&& ($scope.password.length <= 8);
			$scope.valid.phone = ($scope.phone != undefined)
				&& ($scope.phone.length == 7);
			$scope.valid.city($scope.city != undefined)
				&&($scope.city.length >3);
			$scope.valid.street($scope.street != undefined)
				&&($scope.street.length>3);
			$scope.valid.sNumber($scope.sNumber!=undefined)
				&&($scope.sNumber.value>0);
			$scope.valid.zip($scope.zip != undefined)
				&&($scope.zip.length == 7);
			var valid = true;

	    }
	}
		 ]);