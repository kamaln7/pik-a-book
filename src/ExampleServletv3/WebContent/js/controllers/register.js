app.controller('RegisterController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.submitted = false;
	    $scope.error = "";
	    $scope.telephonepre = "050";

	    $scope.submit = function() {
		var telephone = $scope.telephonepre + $scope.phone;
		$http.post('/auth/register', JSON.stringify({
		    username: $scope.username,
		    email: $scope.email,
		    password: $scope.password,
		    fullname: $scope.fullname,
		    bio: $scope.bio,
		    street: $scope.street,
		    street_number: $scope.sNumber,
		    city: $scope.city,
		    zip: $scope.zip,
		    telephone: $scope.pre + $scope.phone,
		    nickname: $scope.nickname,
		    bio: $scope.bio,
		    photo: $scope.photo,  
		    
		})).then(function(res) {
			  $scope.state.username = res.data.username;
			    $scope.state.user_id = res.data.id;
			    $scope.state.password = res.data.password;
			    $scope.state.email = res.data.email;
			    $scope.state.fullname = res.data.fullname;
			    $scope.state.street = res.data.street;
			    $scope.state.sNumber = res.data.sNumber;
			    $scope.state.city = res.data.city;
			    $scope.state.zip = res.data.zip;
			    $scope.state.photo = res.data.photo;
			    $scope.state.nickname = res.data.nickname;
			    $scope.state.bio = res.data.bio;
			    $scope.state.is_admin = res.data.is_admin;
			    $scope.state.authed = true;
			    $scope.redirect('home');

		}.catch(function(err) {
		}), function(res) {
		    $scope.error = res.data.message;
		});
	    }
} ]);
		
		