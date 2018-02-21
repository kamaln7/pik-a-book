app.controller('AdminReviewsController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	function($scope, $http, $location, $anchorScroll) {
	    $http.post(apiUrl + "/admin/reviews").then(
		    function() {
			$scope.reviews = res.data;

		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });
	} ]);