var app = angular.module('booksforall', []);

app.factory('State', function() {
    return {};
});

app.factory('Redirect', function() {
    return function($scope, page) {
	$scope.currentPage = page;
    };
})

app.controller('StateManager', [ '$scope', 'State', function($scope, State) {

    $scope.state = State;
    $scope.$watch('state', function(state) {
	localStorage.setItem("State", JSON.stringify(state));
    }, true);

    var savedState = localStorage.getItem("State");
    if (savedState == null) {
	savedState = {
	    authed : false,
	};
    } else {
	savedState = JSON.parse(savedState);
    }
    $scope.state = savedState;

} ]);

app.controller('MainController', [ '$scope', 'State', 'Redirect',
	function($scope, State, Redirect) {

	    $scope.currentPage = 'home';
	    $scope.redirect = Redirect.bind(this, $scope);
	    $scope.state = State;
	    
	    

	} ]);

app.controller('NavController', [ '$scope', function($scope) {

} ]);

app.controller('HomeController', [ '$scope', function($scope) {

    $scope.name = 'Steve';

} ]);
