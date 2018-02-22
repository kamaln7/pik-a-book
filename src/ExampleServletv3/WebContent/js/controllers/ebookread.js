app.controller('EbookReadController', [
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
				$scope.gshowError(res.data ? res.data.message
					: 'A server error occurred', '', true);
			    });

	    $scope.redirectBack = function() {
		$scope.setRedirectData({
		    id : data.id,
		});

		$scope.redirect('ebooks.ebook');
	    }

	} ]);