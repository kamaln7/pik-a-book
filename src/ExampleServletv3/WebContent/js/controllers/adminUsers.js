app.controller('adminUsersController', [
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

	    $http.get(apiUrl + "/admin/users/").then(
		    function(res) {
			$scope.users = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	    $scope.removeUser = function(user) {
		$http['delete'](apiUrl + '/admin/users/' + user.id).then(
			function(res) {
			    $scope.users = $scope.users.filter(function(item) {
				return item.id != user.id;
			    });
			    $('#removeUser' + user.id).modal('hide');
			},
			function(res) {
			    scope.error = res.data ? res.data.message
				    : 'A server error occurred';
			});

	    }

	    $scope.showRemoveUser = function(user) {
		if (user.is_admin)
		    return;
		$('#removeUser' + user.id).modal('show');
	    }
	}, ]);