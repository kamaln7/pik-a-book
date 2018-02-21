app.controller('adminController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	'$locale',
	'$sce',
	function($scope, $http, $location, $anchorScroll, $locale, $sce) {
	    $http.post(apiUrl + "/auth/admin").then(

		    function(res) {
			$scope.users = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	    $scope.removeUser = function(scope, user) {
		var name = user.fullname;
		$http['delete'](apiUrl + '/auth/admin/' + user.id).then(
			function(res) {
			    alert("rm" + " " + name + " " + user.id);
			    // $scope.users.splice($scope.users.indexOf(remUs));
			},
			function(res) {
			    alert("ss");
			    scope.error = res.data ? res.data.message
				    : 'A server error occurred';
			});

	    }

	}, ]);