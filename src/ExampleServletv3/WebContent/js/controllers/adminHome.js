app.controller('AdminHomeController', [ '$scope', '$http',
	function($scope, $http) {
	    $scope.statsFriendlyNames = {
		'pending_reviews' : 'Pending Reviews',
		'sales' : 'Sales (All Time)'
	    }
	    $http.get(apiUrl + '/admin/stats').then(function(res) {
		$scope.stats = res.data;
	    }, function(res) {
		console.log(res);
	    });
	} ]);