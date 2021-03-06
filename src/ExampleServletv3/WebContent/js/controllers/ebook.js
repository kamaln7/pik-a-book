app
	.controller(
		'EbookController',
		[
			'$scope',
			'$http',
			'$sce',
			function($scope, $http, $sce) {
			    data = $scope.getRedirectData();
			    $scope.redirectBack = function() {
				$scope
					.redirect(data.prevPage
						|| 'ebooks.index');
			    };
			    $scope.book = {
				likes : [],
				reviews : [],
			    }
			    $http
				    .get(apiUrl + '/ebooks/' + data.id)
				    .then(
					    function(res) {
						$scope.book = res.data;
						if ($scope.book.has_purchased) {
						    $scope.book.iframe_url = $sce
							    .trustAsResourceUrl('./books/'
								    + $scope.book.path
								    + '/ebook.html');
						}
					    },
					    function(res) {
						$scope
							.gshowError(
								res.data.message ? res.data.message
									: 'A server error occurred',
								'', true);
					    });

			    $scope.reviewFormError = "";
			    $scope.reviewFormSubmitted = false;
			    $scope.reviewFormSuccess = false;

			    $scope.likeBook = function(scope, book) {
				if (!$scope.state.authed) {
				    $scope
					    .gshowWarning(
						    'You must log in in order to like a book.',
						    'Unauthenticated.');
				    $scope.redirect('auth.login');
				    return;
				}

				if (!book.has_purchased) {
				    $('#buy').modal('show');
				    return;
				}

				$scope.toggleLike(scope, book);
			    }

			    $scope.showReviewForm = function() {
				if ($scope.reviewFormSuccess)
				    return;
				if (!$scope.state.authed) {
				    $scope
					    .gshowWarning(
						    'You must log in in order to review a book.',
						    'Unauthenticated.');
				    $scope.redirect('auth.login');
				    return;
				}

				if (!$scope.book.has_purchased) {
				    $scope
					    .gshowWarning('You must buy this e-book first.');
				    $('#buy').modal('show');
				    return;
				}

				$('#reviewButton').hide();
				$('#reviewForm').collapse('show');
			    };

			    $scope.showBuyModal = function() {
				if (!$scope.state.authed) {
				    $scope.redirect('auth.login');
				    return;
				}

				$('#buy').modal('show');
			    };

			    $scope.submitReviewForm = function() {
				if ($scope.reviewFormSuccess)
				    return;
				$scope.reviewFormSubmitted = false;

				$http
					.post(
						apiUrl + "/ebooks/reviews/"
							+ $scope.book.id,
						{
						    content : $scope.reviewContent,
						})
					.then(
						function(res) {
						    $scope.reviewFormSuccess = true;
						},
						function(res) {
						    $scope.reviewFormError = res.data.message
							    || 'A server error occurred.';
						});
			    };

			    $scope.readEbook = function() {
				if (!$scope.state.authed) {
				    $scope.redirect('auth.login');
				    return;
				}

				if (!$scope.book.has_purchased) {
				    $('#buy').modal('show');
				    return;
				}

				$scope.setRedirectData({
				    id : data.id,
				});

				$scope.redirect('ebooks.read');
			    };

			    $scope.paymentFullname = $scope.state.user
				    && $scope.state.user.fullname;

			    $scope.purchaseFormSubmitted = false;
			    $scope.purchaseFormSubmit = function() {
				$scope.purchaseFormSubmitted = true;

				if ($scope.paymentCCCompany == null
					|| $scope.paymentExpiryDateYear == null
					|| $scope.paymentExpiryDateMonth == null) {
				    $scope.paymentFormError = "Please fill out all fields.";
				    return;
				}

				if ($scope.paymentCCCompany != $scope
					.getCreditCardType($scope.paymentCCNumber)
					|| !$scope.validCCV($scope.paymentCCV,
						$scope.paymentCCCompany)) {
				    $scope.paymentFormError = "Invalid credit card company, number, and CCV combination.";
				    return;
				}

				var date = new Date();
				if (date.getFullYear() >= $scope.paymentExpiryDateYear
					&& date.getMonth() >= $scope.paymentExpiryDateMonth) {
				    $scope.paymentFormError = 'Your card has expired.';
				    return;
				}

				$scope.paymentFormError = '';
				$http
					.post(
						apiUrl + '/ebooks/purchases/'
							+ $scope.book.id,
						{
						    fullname : $scope.paymentFullname,
						    cc_number : $scope.paymentCCNumber,
						    cc_company : $scope.paymentCCCompany,
						    cc_expiry_month : $scope.paymentExpiryDateMonth,
						    cc_expiry_year : $scope.paymentExpiryDateYear,
						    cc_cvv : $scope.paymentCCV,
						})
					.then(
						function(res) {
						    $scope.book.has_purchased = true;
						    $('#buy').modal('hide');
						    $scope.paymentCCNumber = $scope.paymentCCCompany = $scope.paymentExpiryDateMonth = $scope.paymentExpiryDateYear = $scope.paymentCCV = null;
						},
						function(res) {
						    $scope.paymentFormError = res.data.message ? res.data.message
							    : 'A server error occured.';
						});
			    }
			} ]);