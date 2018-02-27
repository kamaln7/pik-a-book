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
			$scope.gshowError(res.data.message ? res.data.message
				: 'A server error occurred', '', true);
		    });

	    $scope.scrollTo = function(scrollLocation) {
		$location.hash(scrollLocation);
		$anchorScroll();
	    }

	    $scope.redirectToEbook = function(id) {
		$scope.setRedirectData({
		    id : id,
		    prevPage : 'ebooks.index',
		});
		$scope.redirect('ebooks.ebook');
	    }

	}

]);