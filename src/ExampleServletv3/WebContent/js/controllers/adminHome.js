app.controller('AdminHomeController', [
	'$scope',
	'$http',
	function($scope, $http) {
	    $scope.statsFriendlyNames = {
		'pending_reviews' : 'Pending Reviews',
		'sales' : 'Sales (All Time)'
	    };
	    var setupPurchaseHistoryGraph = function(purchase_history) {
		// set the dimensions and
		// margins of the graph
		var margin = {
		    top : 20,
		    right : 20,
		    bottom : 30,
		    left : 40
		};
		var width = 410 - margin.left - margin.right;
		var height = 320 - margin.top - margin.bottom;

		// set the ranges
		var x = d3.scaleBand().range([ 0, width ]).padding(0.1);
		var y = d3.scaleLinear().range([ height, 0 ]);

		var svg = d3.select("#purchasesChart").append("svg").attr(
			"width", width + margin.left + margin.right).attr(
			"height", height + margin.top + margin.bottom);
		svg = svg.append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");

		// get the data

		var data = purchase_history.map(function(el) {
		    return {
			date : el.cal.dayOfMonth + '/' + el.cal.month + '/'
				+ el.cal.year,
			sales : el.purchases,
		    }
		});
		// Scale the range of the data
		// in the domains
		x.domain(data.map(function(d) {
		    return d.date;
		}));
		y.domain([ 0, d3.max(data, function(d) {
		    return d.sales;
		}) ]);

		// append the rectangles for the
		// bar chart
		svg.selectAll(".bar").data(data).enter().append("rect").attr(
			"class", "bar").attr("x", function(d) {
		    return x(d.date);
		}).attr("width", x.bandwidth()).attr("y", function(d) {
		    return y(d.sales);
		}).attr("height", function(d) {
		    return height - y(d.sales);
		});

		// add the x Axis
		svg.append("g")
			.attr("transform", "translate(0," + height + ")").call(
				d3.axisBottom(x));

		// add the y Axis
		svg.append("g").call(d3.axisLeft(y));
	    }

	    $http.get(apiUrl + '/admin/stats').then(function(res) {
		$scope.stats = res.data;
		setupPurchaseHistoryGraph(res.data.purchase_history);
	    }, function(res) {
		$scope.gshowError('A server error occurred.', '', true);
	    });
	} ]);