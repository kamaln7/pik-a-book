app.controller('AccountEbooksController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	function($scope, $http, $location, $anchorScroll) {
	    $http.get(apiUrl + '/ebooks/mine').then(
		    function(res) {
			$scope.ebooks = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });

	    $scope.scrollTo = function(scrollLocation) {
		$location.hash(scrollLocation);
		$anchorScroll();
	    }

	    $scope.redirectToEbook = function(id) {
		$scope.setRedirectData({
		    id : id,
		    prevPage : 'account.ebooks',
		});
		$scope.redirect('ebooks.ebook');
	    }

	}

]);