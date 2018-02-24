app.controller('userMsgController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $http.get(apiUrl + '/userToAdmin').then(
		    function(res) {
			$scope.msgs = res.data;
			$scope.msgsLength = res.data.length
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
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
	    $scope.submitmsgForm = function() {

		$http.post(apiUrl + "/userToAdmin", {
		    content : $scope.msgContent,
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