app
	.controller(
		'openConversationController',
		[
			'$scope',
			'$http',
			function($scope, $http) {
			    data = $scope.getRedirectData();
			    $http
				    .get(
					    apiUrl + '/userConversation/'
						    + data.id)
				    .then(
					    function(res) {
						$scope.msgs = res.data;
					    },
					    function(res) {
						$scope
							.gshowError(
								res.data.message ? res.data.message
									: 'A server error occurred',
								'', true);
					    });
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
			    }
				    $scope.reply = function() {
					$http
						.post(
							apiUrl + '/adminToUser',
							{
							    content : $scope.msgContent,
							    user_to : data.id,
							})
						.then(
							function(res) {
							    $scope.msgFormSuccess = true;
							},
							function(res) {
							    $scope
								    .gshowError(res.data.message ? res.data.message
									    : 'A server error occurred');
							});
				    },
				    $scope.removeMsg = function(id) {
					$http['delete']
						(apiUrl + '/adminToUser', {
						    data : {
							id : id,
						    }
						})
						.then(
							function(res) {
							    $scope.msgs = $scope.msgs
								    .filter(function(
									    item) {
									return !(item.id == id);
								    });
							},
							function(res) {
							    $scope
								    .gshowError(res.data.message ? res.data.message
									    : 'A server error occurred');
							});

				    }
			} ]);
