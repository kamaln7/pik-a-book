app.controller('adminController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	'$locale',
	function($scope, $http, $location, $anchorScroll, $locale) {
	    $http.post(apiUrl + "/auth/admin").then(
		    function(res) {
			$scope.users = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	}, ]);