app.controller('NavController', [ '$scope', '$http', function($scope, $http) {
    $scope.logout = function() {
	$http.get(apiUrl + '/auth/logout').then(function() {
	    $scope.reloadAuthState();
	    $scope.redirect('home');
	    $scope.gshowAlert('info', 'Logged out.');
	}, function() {
	    window.location.reload();
	});
    };
    if ($scope.state.auth) {
	$http.get(apiUrl + '/userToAdmin').then(function(res) {
	    $scope.msgsLength = res.data.length;
	    $scope.msgs = res.data
	    if ($scope.msgs.length > 0)
		alert("noooooooo")
	}, function(res) {
	    $scope.error = res.data.message;
	}

	)
    }
} ]);