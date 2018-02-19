app.controller('NavController', [ '$scope', '$http', function($scope, $http) {
    $scope.logout = function() {
	$http.get(apiUrl + '/auth/logout').then(function() {
	    $scope.reloadAuthState();
	    $scope.redirect('home');
	}, function() {
	    window.location.reload();
	});
    }
} ]);