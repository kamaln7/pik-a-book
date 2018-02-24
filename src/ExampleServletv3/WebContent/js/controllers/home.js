app.controller('HomeController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $scope.activeEbook = 0;

	    $('#ebooks-carousel').bind('slide.bs.carousel', function(e) {
		var id = $(e.relatedTarget).attr('data-ebook-id');
		$scope.$apply(function() {
		    $scope.activeEbook = id;
		});
	    });

	    $http.get(apiUrl + '/ebooks/top-homepage').then(
		    function(res) {
			$scope.ebooks = res.data;
			$scope.activeEbook = $scope.ebooks[0].id;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	} ]);