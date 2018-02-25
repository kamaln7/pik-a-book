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
	    $http.get(apiUrl + "/adminToUser").then(
		    function(res) {
			$scope.msgs = res.data;
			$scope.inboxLen = res.data.length;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $http.get(apiUrl + "/userToAdmin").then(
		    function(res) {
			$scope.msgsLength = 0;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $scope.redirectToUserInbox = function(id) {
		$http.post(apiUrl + "/userConversation/" + id).then(
			function(res) {
			    $scope.msgs = $scope.msgs.filter(function(item) {
				return !(item.user_id == id);
			    });
			    $scope.inboxLen = $scope.msgs.length;

			},
			function(res) {
			    $scope.gshowError(res.data ? res.data.message
				    : 'A server error occurred', '', true);
			})
		$scope.setRedirectData({
		    id : id,
		});
		$scope.redirect('inbox.masseges');
	    };
	    $scope.openInbox = function() {
		// alert("btata")
		// $http.post(apiUrl + "/userToAdmin").then(
		// function(res) {
		$scope.msgsLength = $scope.msgs.length;

		// },
		// function(res) {
		// $scope.gshowError(res.data ? res.data.message
		// : 'A server error occurred', '', true);
		// })
		$scope.redirect('account.masseges');
	    }
	} ]);