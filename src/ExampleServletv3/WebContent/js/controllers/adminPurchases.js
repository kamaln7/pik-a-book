app.controller('AdminPurchasesController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $scope.setupPopovers = function() {
		$("[data-toggle=popover]").popover({
		    html : true,
		    content : function() {
			var content = $(this).attr("data-popover-content");
			return $(content).children(".popover-body").html();
		    },
		    title : function() {
			var title = $(this).attr("data-popover-content");
			return $(title).children(".popover-heading").html();
		    }
		});
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