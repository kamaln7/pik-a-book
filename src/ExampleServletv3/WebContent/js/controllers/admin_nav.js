app.controller('AdminNavController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $scope.adminNavReviewsCount = {};
	    $http.get(apiUrl + "/admin/reviews").then(
		    function(res) {
			$scope.adminNavReviewsCount.c = res.data.length;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });

	} ]);