app.controller('NavController', [ '$scope', function($scope) {
    $scope.logout = function() {
	$scope.state.authed = false;
	$scope.redirect('home');
	$scope.state.username = $scope.state.user_id = null;
    }
} ]);