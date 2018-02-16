app.controller('RegisterController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.submitted = false;
	    $scope.error = "";
	    $scope.pre = "050";

	    $scope.submit = function() {
		$scope.submitted = true;
		$http.post(apiUrl + "/auth/registration", JSON.stringify({
		    username : $scope.username,
		    email : $scope.email,
		    password : $scope.password,
		    fullname : $scope.fullname,
		    bio : $scope.bio,
		    street : $scope.street,
		    street_number : $scope.sNumber,
		    city : $scope.city,
		    zip : $scope.zip,
		    telephone : $scope.pre + $scope.phone,
		    nickname : $scope.nickname,
		    bio : $scope.bio,
		    photo : $scope.photo,
		})).then(function(res) {
		    $scope.state.username = res.data.username;
		    $scope.state.user_id = res.data.id;
		    $scope.state.nickname = res.data.nickname;
		    $scope.state.is_admin = res.data.is_admin;
		    $scope.state.authed = true;

		    $scope.redirect('home');
		}, function(res) {
		    $scope.error = res.data.message;
		});
	    }
	} ]);
