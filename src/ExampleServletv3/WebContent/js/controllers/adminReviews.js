app
	.controller(
		'AdminReviewsController',
		[
			'$scope',
			'$http',
			function($scope, $http) {
			    $scope.setupPopovers = function() {
				$("[data-toggle=popover]")
					.popover(
						{
						    html : true,
						    content : function() {
							var content = $(this)
								.attr(
									"data-popover-content");
							return $(content)
								.children(
									".popover-body")
								.html();
						    },
						    title : function() {
							var title = $(this)
								.attr(
									"data-popover-content");
							return $(title)
								.children(
									".popover-heading")
								.html();
						    }
						});
				$('[data-toggle="tooltip"]').tooltip();
			    }

			    $http
				    .post(apiUrl + "/admin/reviews")
				    .then(
					    function(res) {
						$scope.reviews = res.data;
					    },
					    function(res) {
						$scope.error = res.data ? res.data.message
							: 'A server error occurred';
					    });
			    $scope.removeReview = function(user_id, ebook_id) {
				$http['delete']
					(apiUrl + '/admin/reviews', {
					    data : {
						user_id : user_id,
						ebook_id : ebook_id,
					    }
					})
					.then(
						function(res) {
						    $scope.reviews = $scope.reviews
							    .filter(function(
								    item) {
								return !(item.user_id == user_id && item.ebook_id == ebook_id);
							    });
						},
						function(res) {
						    alert("err")
						    $scope.error = res.data ? res.data.message
							    : 'A server error occurred';
						});

			    }
			} ]);