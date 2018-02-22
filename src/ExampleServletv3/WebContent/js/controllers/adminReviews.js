app.controller('AdminReviewsController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	function($scope, $http, $location, $anchorScroll) {
	    $http.post(apiUrl + "/admin/reviews").then(
		    function(res) {
			$scope.reviews = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });
	    $scope.getUser = function(id) {
		$http.get(apiUrl + "/getUser/" + id).then(

			function(res) {
			    $scope.user = res.data;
			},
			function(res) {
			    $scope.error = res.data ? res.data.message
				    : 'A server error occurred';
			});
	    }
	} ]);