app.controller('NavController', [ '$scope', function($scope) {
    $scope.logout = function() {
	$scope.state = {
	    authed : false,
	};
	$scope.redirect('home');
    }
} ]);