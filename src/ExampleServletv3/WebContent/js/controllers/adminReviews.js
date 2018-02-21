app.controller('AdminReviewsController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	function($scope, $http, $location, $anchorScroll) {
	    $http.post(apiUrl + "/admin/reviews").then(
		    function() {
			alert("kkkk");

			$scope.reviews = res.data;
			var review = $scope.reviews.id

		    },
		    function(res) {
			alert("oooo");
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });
	} ]);