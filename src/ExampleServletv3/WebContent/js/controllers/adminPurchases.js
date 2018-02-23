app
	.controller(
		'AdminPurchasesController',
		[
			'$scope',
			'$http',
			function($scope, $http) {
			    $scope.setupTooltips = function() {
				$('[data-toggle="tooltip"]').tooltip();
			    }
			    
			    var setupPiechart = function() {
				var data = [];
				for (var key in purchasesGraphData) {
				  data.push({
				    name: key,
				    value: purchasesGraphData[key]
				  })
				};
				
				var svg = d3.select("#piechart"),
				    width = +svg.attr("width"),
				    height = +svg.attr("height"),
				    radius = Math.min(width, height) / 2,
				    g = svg.append("g").attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

				var color = d3.scaleOrdinal(["#98abc5",
				    "#8a89a6",
				    "#7b6888",
				    "#6b486b",
				    "#a05d56", 
				    "#d0743c",
				    "#ff8c00"]);

				var pie = d3.pie()
				    .sort(null)
				    .value(function(d) { return d.value; });

				var path = d3.arc()
				    .outerRadius(radius - 10)
				    .innerRadius(0);

				var label = d3.arc()
				    .outerRadius(radius - 40)
				    .innerRadius(radius - 40);

				  var arc = g.selectAll(".arc")
				    .data(pie(data)).enter().append("g").attr("class", "arc");

				  arc.append("path")
				      .attr("d", path)
				      .attr("fill", function(d) { return color(d.data.name); });

				  arc.append("text")
				      .attr("transform", function(d) { return "translate(" + label.centroid(d) + ")"; })
				      .attr("dy", "0.35em")
				      .attr("fill", "#ffffff")
				      .text(function(d) { return d.data.name; });
			    };

			    var purchasesGraphData = {};
			    $scope.ebooks = [];
			    var addedScopeEbooks = [];
			    $http
				    .get(apiUrl + "/admin/purchases")
				    .then(
					    function(res) {
						$scope.purchases = res.data;

						$scope.purchases
							.forEach(function(
								purchase) {
							    var id = purchase.ebook_id;
							    if (purchasesGraphData[id])  {
								purchasesGraphData[id]++;
							    } else { 
								purchasesGraphData[id] = 1;
							    }
							    
							    if (addedScopeEbooks.indexOf(id) == -1) {
								$scope.ebooks.push({
								    id: id,
								    name: purchase.ebook_name,
								});
								addedScopeEbooks.push(id);
							    }
							});

						$scope.ebooks.sort(function(a,b) {return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0);} ); 
						setupPiechart();
						
					    },
					    function(res) {
						$scope
							.gshowError(
								res.data ? res.data.message
									: 'A server error occurred',
								'', true);
					    });
			    

			} ]);