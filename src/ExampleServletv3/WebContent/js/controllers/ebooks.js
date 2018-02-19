app.controller('EbooksController', [
	'$scope',
	'$http',
	'$location',
	'$anchorScroll',
	'$locale',
	function($scope, $http, $location, $anchorScroll, $locale) {
	    $http.get(apiUrl + '/ebooks').then(
		    function(res) {
			$scope.ebooks = res.data;
		    },
		    function(res) {
			$scope.error = res.data ? res.data.message
				: 'A server error occurred';
		    });

	    $scope.scrollTo = function(scrollLocation) {
		$location.hash(scrollLocation);
		$anchorScroll();
		console.log(scrollLocation);
	    }

	    $scope.currentYear = new Date().getFullYear();
	    $scope.currentMonth = new Date().getMonth() + 1;
	    $scope.months = $locale.DATETIME_FORMATS.MONTH;
	    $scope.paymentInfo = {
		type : undefined
	    }
	    $scope.save = function(data) {
		if ($scope.paymentForm.$valid) {
		    console.log(data) // valid data saving stuff here
		}
	    }

	}

]);