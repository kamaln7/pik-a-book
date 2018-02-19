app.controller('EbookController', [
	'$scope',
	'$http',
	'$sce',
	function($scope, $http, $sce) {
	    data = $scope.getRedirectData();
	    $http.get(apiUrl + '/ebooks/' + data.id)
		    .then(
			    function(res) {
				$scope.book = res.data;
				if ($scope.book.has_purchased) {
				    $scope.book.iframe_url = $sce
					    .trustAsResourceUrl('./books/'
						    + $scope.book.path
						    + '/ebook.html');
				}
			    },
			    function(res) {
				$scope.error = res.data ? res.data.message
					: 'A server error occurred';
			    });

	    $scope.showReviewForm = function(purchased) {
		if (!purchased) {
		    $scope.redirect('auth.login');
		    return;
		}

		$('#reviewButton').hide();
		$('#reviewForm').collapse('show');
	    }
	} ]);