app.controller('userMsgController', [
	'$scope',
	'$http',
	'$sce',
	function($scope, $http, $sce) {
	    $http.get(apiUrl + '/userToAdmin').then(
		    function(res) {
			$scope.msgs = res.data;
			$scope.msgsLength = res.data.length
			if ($scope.msgsLength > 0) {
			    $scope.gshowAlert('warning',
				    'you have new messages.');
			} else {

			    $scope.gshowAlert('info', 'no new messages');

			}
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $scope.removeMsg = function(id) {
		$http['delete'](apiUrl + '/userToAdmin', {
		    data : {
			id : id
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
	    }
	    $scope.msgFormError = "";
	    $scope.msgFormSubmitted = false;
	    $scope.msgFormSuccess = false;
	    $scope.msgFormSuccess1 = false;
	    $scope.replybutton1 = false;
	    $scope.replybutton = false;
	    $scope.showmsgForm = function() {
		if ($scope.msgFormSuccess)
		    return;
		$('#msgButton').hide();
		$('#msgForm').collapse('show');
	    };
	    $scope.sendRequest = function() {
		$scope.replybutton = true;
		$http.post(apiUrl + "/userToAdmin", {
		    content : $scope.msgContent,
		    user_id : $scope.state.user.id,
		}).then(
			function(res) {
			    $scope.msgFormSuccess = true;
			},
			function(res) {
			    $scope.msgFormError = res.data.message
				    || 'A server error occurred.';
			});
	    };

	} ]);