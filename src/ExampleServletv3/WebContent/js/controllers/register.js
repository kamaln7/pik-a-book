app.controller('RegisterController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.submitted = false;
	    $scope.error = "";
	    $scope.telephonepre = "050";

	    $scope.submit = function() {
		var telephone = $scope.telephonepre + $scope.phone;
		$http.post('/auth/register', JSON.stringify({
		    username: $scope.username,
		    password: $scope.password,
		    email: $scope.email,
		    bio: $scope.bio,
		    fullname: $scope.fullname,
		    
		})).then(function(res) {
		    
		}).catch(function(err) {
		    
		});
	    }
	} ]);