app.controller('EbookController', [ '$scope', '$http', function($scope, $http) {
    data = $scope.getRedirectData();
    $http.get(apiUrl + '/ebooks/' + data.id).then(function(res) {
	$scope.book = res.data;
	console.log($scope.book);
    }, function(res) {
	$scope.error = res.data ? res.data.message : 'A server error occurred';
    });
} ]);