var app = angular.module('booksforall', []);
var apiUrl = '.';

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

	if ($scope.globalAlert.undismissable) {
	    $("#globalAlert").hide();
	    $scope.globalAlert.type = null;
	    $scope.globalAlert.title = null;
	    $scope.globalAlert.message = null;
	    $scope.globalAlert.undismissable = null;
	}

	$('#navbar').collapse('hide');
    };
})

app.filter('trustAsResourceUrl', [ '$sce', function($sce) {
    return function(val) {
	return $sce.trustAsResourceUrl(val);
    };
} ])

app
	.controller(
		'MainController',
		[
			'$scope',
			'$http',
			'State',
			'Redirect',
			function($scope, $http, State, Redirect) {
			    $scope.state = State;

			    $scope.reloadAuthState = function() {
				$http
					.get(apiUrl + '/auth/state')
					.then(
						function(res) {
						    $scope.state.authed = res.data.authed;
						    $scope.state.user = res.data.user
							    || null;
						}, function(res) {
						    window.location.reload();
						});
			    };

			    $scope.globalAlert = {};
			    $scope.gshowAlert = function(type, message, title,
				    undismissable) {
				$scope.globalAlert.type = type;
				$scope.globalAlert.title = title;
				$scope.globalAlert.message = message;
				$scope.globalAlert.undismissable = undismissable;

				var ga = $("#globalAlert");
				ga.hide().removeClass('hidden').fadeTo(2000,
					500);
				if (!undismissable) {
				    ga.slideUp(500, function() {
					$("#globalAlert").slideUp(500);
				    });
				}
			    };
			    $scope.gshowError = $scope.gshowAlert.bind(null,
				    'danger');
			    $scope.gshowWarning = $scope.gshowAlert.bind(null,
				    'warning');

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
				if (!$scope.state.authed) {
				    return;
				}

				var liked = $scope.userHasLiked(book.likes,
					$scope.state.user.id);

				if (liked) {
				    $http['delete']
					    (apiUrl + '/ebooks/likes', {
						data : {
						    ebook_id : book.id
						},
					    })
					    .then(
						    function(res) {
							book.likes = book.likes
								.filter(function(
									el) {
								    return el.user_id != $scope.state.user.id;
								});
							$scope
								.gshowAlert(
									'info',
									'Unliked e-book.');
						    },
						    function(res) {
							scope.error = res.data.message ? res.data.message
								: 'A server error occurred';
						    });
				} else {
				    $http['post']
					    (apiUrl + '/ebooks/likes', {
						ebook_id : book.id
					    })
					    .then(
						    function(res) {
							book.likes
								.unshift({
								    user_id : $scope.state.user.id,
								    ebook_id : book.id,
								    user_nickname : $scope.state.user.nickname,
								});
							$scope
								.gshowAlert(
									'success',
									'Liked e-book.');
						    },
						    function(res) {
							scope.error = res.data.message ? res.data.message
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

			    $scope.time_ago = function(time) {
				switch (typeof time) {
				case 'number':
				    break;
				case 'string':
				    time = +new Date(time);
				    break;
				case 'object':
				    if (time.constructor === Date)
					time = time.getTime();
				    break;
				default:
				    time = +new Date();
				}
				var time_formats = [ [ 60, 'seconds', 1 ], // 60
				[ 120, '1 minute ago', '1 minute from now' ], // 60*2
				[ 3600, 'minutes', 60 ], // 60*60, 60
				[ 7200, '1 hour ago', '1 hour from now' ], // 60*60*2
				[ 86400, 'hours', 3600 ], // 60*60*24, 60*60
				[ 172800, 'Yesterday', 'Tomorrow' ], // 60*60*24*2
				[ 604800, 'days', 86400 ], // 60*60*24*7,
				// 60*60*24
				[ 1209600, 'Last week', 'Next week' ], // 60*60*24*7*4*2
				[ 2419200, 'weeks', 604800 ], // 60*60*24*7*4,
				// 60*60*24*7
				[ 4838400, 'Last month', 'Next month' ], // 60*60*24*7*4*2
				[ 29030400, 'months', 2419200 ], // 60*60*24*7*4*12,
				// 60*60*24*7*4
				[ 58060800, 'Last year', 'Next year' ], // 60*60*24*7*4*12*2
				[ 2903040000, 'years', 29030400 ], // 60*60*24*7*4*12*100,
				// 60*60*24*7*4*12
				[ 5806080000, 'Last century', 'Next century' ], // 60*60*24*7*4*12*100*2
				[ 58060800000, 'centuries', 2903040000 ] // 60*60*24*7*4*12*100*20,
				// 60*60*24*7*4*12*100
				];
				var seconds = (+new Date() - time) / 1000, token = 'ago', list_choice = 1;

				if (seconds == 0) {
				    return 'Just now'
				}
				if (seconds < 0) {
				    seconds = Math.abs(seconds);
				    token = 'from now';
				    list_choice = 2;
				}
				var i = 0, format;
				while (format = time_formats[i++])
				    if (seconds < format[0]) {
					if (typeof format[2] == 'string')
					    return format[list_choice];
					else
					    return Math.floor(seconds
						    / format[2])
						    + ' '
						    + format[1]
						    + ' '
						    + token;
				    }
				return time;
			    }
			} ]);

function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
}