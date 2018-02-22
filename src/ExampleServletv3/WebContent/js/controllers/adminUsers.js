app.controller('adminUsersController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	'$locale',
	'$sce',
	function($scope, $http, $location, $anchorScroll, $locale, $sce) {
	    $http.get(apiUrl + "/admin/users/").then(

		    function(res) {
			$scope.users = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	    $scope.removeUser = function(scope, user) {
		var name = user.fullname;
		var id = user.id;
		$http['delete'](apiUrl + '/admin/users/' + user.id).then(
			function(res) {
			    $("#user" + id).unwrap();
			    $(id).unwrap();
			    $("#user" + id).remove();
			    $("#" + id).remove();
			    $('.modal-backdrop').remove();
			},
			function(res) {
			    scope.error = res.data ? res.data.message
				    : 'A server error occurred';
			});

	    }

	}, ]);