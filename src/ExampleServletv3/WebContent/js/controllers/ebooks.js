app.controller('EbooksController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $http.get(apiUrl + "/ebooks").then(
		    function(res) {
			$scope.ebooks = res.data;
			console.log($scope.ebooks);
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });
	} ]);