app.controller('EbooksController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	function($scope, $http, $location, $anchorScroll) {
	    $http.get(apiUrl + '/ebooks').then(
		    function(res) {
			$scope.ebooks = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	    $scope.scrollTo = function(scrollLocation) {
		$location.hash(scrollLocation);
		$anchorScroll();
	    }
	} ]);