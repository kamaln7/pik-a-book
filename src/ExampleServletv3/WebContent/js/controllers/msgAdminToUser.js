app.controller('adminToUserController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $http.get(apiUrl + '/adminToUser').then(
		    function(res) {
			alert("suc")
			$scope.msgs = res.data;
		    },
		    function(res) {
			alert("err")
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    })
	} ]);