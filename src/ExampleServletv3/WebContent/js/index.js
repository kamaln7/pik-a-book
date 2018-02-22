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

app.filter('trustAsResourceUrl', [ '$sce', function($sce) {
    return function(val) {
	return $sce.trustAsResourceUrl(val);
    };
} ])

app.controller('MainController', [
	'$scope',
	'$http',
	'State',
	'Redirect',
	function($scope, $http, State, Redirect) {
	    $scope.state = State;

	    $scope.reloadAuthState = function() {
		$http.get(apiUrl + '/auth/state').then(function(res) {
		    console.log(res);
		    $scope.state.authed = res.data.authed;
		    $scope.state.user = res.data.user || null;
		});
	    };

	    $scope.state.authed = false;
	    $scope.reloadAuthState();

	    $scope.currentPage = 'home';
	    $scope.redirectData = {
		data : null
	    };
	    $scope.redirect = Redirect.bind(this, $scope);
	    $scope.userHasLiked = function(likes, user_id) {
		return likes.filter(function(el) {
		    return el.user_id == user_id;
		}).length > 0;
	    };
	    $scope.userHasReviewed = $scope.userHasLiked;
	    $scope.getRedirectData = function() {
		var data = $scope.redirectData.data;
		$scope.redirectData.data = null;
		return data;
	    }
	    $scope.setRedirectData = function(data) {
		$scope.redirectData.data = data;
	    }
	    $scope.trimLength = function(str, len) {
		if (str.length <= len)
		    return str;

		return str.substr(0, len) + '...';
	    }

	    $scope.toggleLike = function(scope, book) {
		if (!$scope.state.authed)
		    return;

		var liked = $scope.userHasLiked(book.likes,
			$scope.state.user.id);

		if (liked) {
		    $http['delete'](apiUrl + '/ebooks/likes', {
			data : {
			    ebook_id : book.id
			},
		    }).then(
			    function(res) {
				book.likes = book.likes.filter(function(el) {
				    return el.user_id != $scope.state.user.id;
				});
			    },
			    function(res) {
				scope.error = res.data ? res.data.message
					: 'A server error occurred';
			    });
		} else {
		    $http['post'](apiUrl + '/ebooks/likes', {
			ebook_id : book.id
		    }).then(
			    function(res) {
				book.likes.unshift({
				    user_id : $scope.state.user.id,
				    ebook_id : book.id,
				    user_nickname : $scope.state.user.nickname,
				});
			    },
			    function(res) {
				scope.error = res.data ? res.data.message
					: 'A server error occurred';
			    });
		}
	    };

	    $scope.getCreditCardType = function(number) {
		if (/^3[47]/.test(number)) {
		    return 'amex';
		} else if (/^4/.test(number)) {
		    return 'visa';
		} else if (/^5[0-5]/.test(number)) {
		    return 'mastercard';
		} else {
		    return undefined;
		}
	    };

	    $scope.validCCV = function(ccv, type) {
		switch (type) {
		case 'amex':
		    return ccv.length == 4;
		    break;
		case 'visa':
		    return ccv.length == 3;
		    break;
		case 'mastercard':
		    return ccv.length == 3;
		    break;
		default:
		    return false;
		}
	    }
	} ]);

function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
}