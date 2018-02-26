app.controller('adminToUserController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $http.get(apiUrl + '/adminToUser').then(
		    function(res) {
			$scope.msgs = res.data;
			if (res.data.length == 0) {
			    $scope.gshowAlert('info', 'no new messages');
			}
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $scope.reply = function(id) {
	    }, $scope.deleteConv = function(id) {
		$http['delete'](apiUrl + '/adminToUser', {
		    data : {
			id : id,
		    }
		}).then(
			function(res) {
			    $scope.msgs = $scope.msgs.filter(function(item) {
				return !(item.id == id);
			    });
			},
			function(res) {
			    $scope.gshowError(res.data ? res.data.message
				    : 'A server error occurred');
			});
		$scope.msgFormError = "";
		$scope.msgFormSubmitted = false;
		$scope.msgFormSuccess = false;
		$scope.showmsgForm = function() {
		    if ($scope.msgFormSuccess)
			return;
		    $('#msgButton').hide();
		    $('#msgForm').collapse('show');
		};
	    }, $scope.reply = function(item, id1, count) {
		$http.post(apiUrl + '/adminToUser', {
		    content : repeatMsgcount,
		    id : item.id,
		    user_to : item.user_id,
		}).then(
			function(res) {
			    $scope.msgs = $scope.msgs.filter(function(item1) {
				return !(item1.id == id1);
			    });
			},
			function(res) {
			    $scope.gshowError(res.data ? res.data.message
				    : 'A server error occurred');
			});
	    }, $scope.redirectToUserInbox = function(id) {
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
	    }

	} ]);