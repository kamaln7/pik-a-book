var readEbookScrollStuff = {
    handler: function() {},
    forward: function() {
        this.handler.apply(this, arguments);
    },
    active: true,
    timeOut: function() {
        setTimeout(function() {
            this.active = true;
        }.bind(this), 600);
    },
};

window.document.addEventListener('scroll', readEbookScrollStuff.forward
    .bind(readEbookScrollStuff), true);

app.controller('EbookReadController', [
    '$scope',
    '$http',
    '$sce',
    function($scope, $http, $sce) {
        data = $scope.getRedirectData();
        $scope.pos = 0;
        var ebookId = data.id;

        readEbookScrollStuff.handler = function(scope, e) {
            var pos = e.pageY || (e.srcElement && e.srcElement.scrollingElement && e.srcElement.scrollingElement.scrollTop) || 0;
            if (!this.active || scope.currentPage != 'ebooks.read' ||
                pos == 0)
                return;

            scope.$apply(function() {
                scope.pos = pos;
            });
            this.active = false;
        }.bind(readEbookScrollStuff, $scope);

        $scope.$watch('pos', function(val) {
            if (val == 0)
                return;

            $http.post(apiUrl + '/ebooks/scrolling-position/' + ebookId, {
                position: val,
            }).finally(function() {
                readEbookScrollStuff.timeOut.bind(readEbookScrollStuff)();
            });
        });

        var getReadingPosition = function() {
            $http.get(apiUrl + '/ebooks/scrolling-position/' + ebookId)
                .then(function(res) {
                    window.scroll(0, res.data.position);
                }, function(res) {

                });
        };

        var getSH = function() {
            return $('body')[0].scrollHeight;
        };
        var initialScrollHeight = getSH(),
            checkSHandScroll = function() {
                console.log(getSH(), (initialScrollHeight + 1000));
                if (getSH() > (initialScrollHeight + 1000)) {
                    setTimeout(getReadingPosition, 50);
                } else {
                    setTimeout(checkSHandScroll, 100)
                }
            };

        $http.get(apiUrl + '/ebooks/' + ebookId)
            .then(
                function(res) {
                    $scope.book = res.data;
                    if ($scope.book.has_purchased) {
                        $scope.book.iframe_url = $sce
                            .trustAsResourceUrl('./books/' +
                                $scope.book.path +
                                '/ebook.html');

                        // setTimeout(getReadingPosition, 600);
                        setTimeout(checkSHandScroll, 100)
                    }
                },
                function(res) {
                    $scope.gshowError(res.data ? res.data.message :
                        'A server error occurred', '', true);
                });

        $scope.redirectBack = function() {
            $scope.setRedirectData({
                id: ebookId,
            });

            $scope.redirect('ebooks.ebook');
        }

    }
]);