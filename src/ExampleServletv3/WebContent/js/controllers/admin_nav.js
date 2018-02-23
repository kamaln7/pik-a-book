app.controller('AdminNavController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $http.get(apiUrl + "/admin/reviews").then(
		    function(res) {
			$scope.reviews = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });

	} ]);