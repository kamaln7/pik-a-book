app.controller('AdminPurchasesController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $scope.setupTooltips = function() {
		$('[data-toggle="tooltip"]').tooltip();
	    }

	    $http.get(apiUrl + "/admin/purchases").then(
		    function(res) {
			$scope.purchases = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });

	} ]);