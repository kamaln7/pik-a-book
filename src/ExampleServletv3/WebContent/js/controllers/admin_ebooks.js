app.controller('AdminEbooksController', [
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
		    },
		    trigger : "hover",
		});
	    }

	    $http.get(apiUrl + "/admin/users").then(
		    function(res) {
			$scope.users = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	    $http.get(apiUrl + '/admin/ebooks').then(
		    function(res) {
			$scope.ebooks = res.data;
		    },
		    function(res) {
			$scope.gshowError(res.data ? res.data.message
				: 'A server error occurred', '', true);
		    });
	}

]);