app.controller('openConversationController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    data = $scope.getRedirectData();
	    $http.get(apiUrl + '/userConversation/' + data.id).then(
		    function(res) {
			$scope.msgs = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $scope.reply = function() {
		$http.post(apiUrl + '/adminToUser', {
		    content : $scope.msgContent,
		    user_to : data.id,
		}).then(
			function(res) {
			    alert("mmmm");
			},
			function(res) {
			    $scope.gshowError(res.data ? res.data.message
				    : 'A server error occurred');
			});
	    }
	    $scope.removeMsg = function(id) {
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

	    }
	} ]);
