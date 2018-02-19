app.controller('EbookController', [
	'$scope',
	'$http',
	'$sce',
	function($scope, $http, $sce) {
	    data = $scope.getRedirectData();
	    $scope.book = {
		likes : [],
		reviews : [],
	    }
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

	    $scope.reviewFormError = "";
	    $scope.reviewFormSubmitted = false;
	    $scope.reviewFormSuccess = false;

	    $scope.showReviewForm = function(purchased) {
		if (!purchased) {
		    $scope.redirect('auth.login');
		    return;
		}

		$('#reviewButton').hide();
		$('#reviewForm').collapse('show');
	    };

	    $scope.submitReviewForm = function() {
		console.log($scope);
		$scope.reviewFormSubmitted = false;

		$http.post(apiUrl + "/ebooks/reviews/" + $scope.book.id,
			JSON.stringify({
			    content : $scope.reviewContent,
			})).then(
			function(res) {
			    $scope.reviewFormSuccess = true;
			},
			function(res) {
			    $scope.reviewFormError = res.data.message
				    || 'A server error occurred.';
			});
	    };

	    $scope.readEbook = function() {
		$scope.setRedirectData({
		    id : data.id,
		});

		$scope.redirect('ebooks.read');
	    };
	} ]);