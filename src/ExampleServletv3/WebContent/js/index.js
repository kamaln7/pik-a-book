var app = angular.module('booksforall', []);
var apiUrl = '/ExampleServletv3';

app.factory('State', function() {
    return {};
});

app.factory('Redirect', function() {
    return function($scope, page) {
	$scope.currentPage = page;

	var reg = /^([a-z]+)\./i;
	if (reg.test(page)) {
	    var matches = page.match(reg);
	    $scope.currentSection = matches[1];
	} else {
	    $scope.currentSection = '';
	}

	$('#navbar').collapse('hide');
    };
})

app.controller('MainController', [
	'$scope',
	'$http',
	'State',
	'Redirect',
	function($scope, $http, State, Redirect) {
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

	    $scope.currentPage = 'home';
	    $scope.redirect = Redirect.bind(this, $scope);
	    $scope.userHasLiked = function(likes, user_id) {
		return likes.filter(function(el) {
		    return el.user_id == user_id;
		}).length > 0;
	    };
	    $scope.toggleLike = function(scope, book) {
		if (!$scope.state.authed)
		    return;

		var liked = $scope.userHasLiked(book.likes,
			$scope.state.user_id);

		if (liked) {
		    $http['delete'](
			    apiUrl + '/ebooks/likes/' + book.id + '?user_id='
				    + $scope.state.user_id).then(
			    function(res) {
				book.likes = book.likes.filter(function(el) {
				    return el.user_id != $scope.state.user_id;
				});
			    },
			    function(res) {
				scope.error = res.data ? res.data.message
					: 'A server error occurred';
			    });
		} else {
		    $http['post'](
			    apiUrl + '/ebooks/likes/' + book.id + '?user_id='
				    + $scope.state.user_id).then(
			    function(res) {
				book.likes.unshift({
				    user_id : $scope.state.user_id,
				    ebook_id : book.id,
				    user_nickname : $scope.state.nickname,
				});
			    },
			    function(res) {
				scope.error = res.data ? res.data.message
					: 'A server error occurred';
			    });
		}
	    };
	} ]);