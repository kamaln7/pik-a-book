app.controller('AccountReviewsController', [ '$scope', '$http', '$location',
	'$anchorScroll', function($scope, $http, $location, $anchorScroll) {
	    $http.get(apiUrl + '/ebooks/reviews/mine').then(function(res) {
		$scope.ebooks = res.data;
	    }, function(res) {
		$scope.gshowError('A server error occurred.', '', true);
	    });

	    $scope.scrollTo = function(scrollLocation) {
		$location.hash(scrollLocation);
		$anchorScroll();
	    }

	    $scope.redirectToEbook = function(id) {
		$scope.setRedirectData({
		    id : id,
		    prevPage : 'account.reviews',
		});
		$scope.redirect('ebooks.ebook');
	    }

	}

]);