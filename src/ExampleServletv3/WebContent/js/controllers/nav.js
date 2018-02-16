app
	.controller(
		'NavController',
		[
			'$scope',
			function($scope) {
			    $scope.logout = function() {
				$scope.state.authed = false;
				$scope.state.user_id = $scope.state.username = $scope.state.nickname = $scope.state.is_admin = null;
				$scope.redirect('home');
			    }
			} ]);