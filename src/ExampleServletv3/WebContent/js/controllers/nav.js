app.controller('NavController', [
	'$scope',
	'$http',
	'$sce',
	function($scope, $http, $sce) {
	    $scope.logout = function() {
		$http.get(apiUrl + '/auth/logout').then(function() {
		    $scope.reloadAuthState();
		    $scope.redirect('home');
		    $scope.gshowAlert('info', 'Logged out.');
		}, function() {
		    window.location.reload();
		});
	    };
	    if ($scope.state.authed) {
		$http.get(apiUrl + "/userToAdmin").then(
			function(res) {
			    $scope.msgsLength = res.data.length;
			},
			function(res) {
			    $scope.gshowError(res.data ? res.data.message
				    : 'A server error occurred', '', true);
			});
	    }

	    $scope.openInbox = function() {
		$scope.msgsLength = 0;
		$scope.redirect('account.masseges');
		$scope.inbox.hide();
	    }
	} ]);