app
	.controller(
		'EbookReadController',
		[
			'$scope',
			'$http',
			'$sce',
			function($scope, $http, $sce) {
			    data = $scope.getRedirectData();
			    $http
				    .get(apiUrl + '/ebooks/' + data.id)
				    .then(
					    function(res) {
						$scope.book = res.data;
						if ($scope.book.has_purchased) {
						    $scope.book.iframe_url = $sce
							    .trustAsResourceUrl('./books/'
								    + $scope.book.path
								    + '/ebook.html');
						    $http
							    .get(
								    './books/'
									    + $scope.book.path
									    + '/ebook.html')
							    .then(
								    function(
									    res) {
									$scope.bookHTML = $sce
										.trustAsHtml(res.data);
								    },
								    function(
									    res) {
									$scope.error = res.data ? res.data.message
										: 'A server error occurred';
								    });
						}

					    },
					    function(res) {
						$scope.error = res.data ? res.data.message
							: 'A server error occurred';
					    });

			    $scope.redirectBack = function() {
				$scope.setRedirectData({
				    id : data.id,
				});

				$scope.redirect('ebooks.ebook');
			    }

			} ]);