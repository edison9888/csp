function line(config){
		var config = {
			method : config.method,
			appName : config.appName,
			keyName : config.keyName,
			property : config.property,
			properties : config.properties
		};
	 		var chart;
            var graph;
            AmCharts.ready(function () {
            	
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.pathToImages = base+"/time/custom/amcharts/images/";
                generateData();
                chart.marginLeft = 10;
                chart.categoryField = "ftime";
                chart.zoomOutButton = {
                    backgroundColor: '#000000',
                    backgroundAlpha: 0.15
                };

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
//              categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
//                categoryAxis.minPeriod = "MM";
                categoryAxis.gridAlpha = 0;
				for(var i =0;i<config.properties.length;i++){
					var color = randomColor();
					 
					  // value
               	 	var valueAxis = new AmCharts.ValueAxis();
               	 	valueAxis.offset = 40*i; 
                	//valueAxis.axisAlpha = 0;
                	valueAxis.axisThickness = 2;
                	valueAxis.axisColor = color;
                	chart.addValueAxis(valueAxis);

                	// GRAPH                
                	graph = new AmCharts.AmGraph();
                	graph.title = config.properties[i];
               		graph.type = "smoothedLine"; // this line makes the graph smoothed line.
                	graph.lineColor = color;
                	graph.valueAxis = valueAxis;
               // 	graph.negativeLineColor = "#637bb6"; // this line makes the graph to change color when it drops below 0
                	graph.bullet = "round";
                	graph.bulletSize = 5;
                	graph.lineThickness = 2;
                	graph.valueField = config.properties[i];
                	chart.addGraph(graph);
				}
              

                // CURSOR
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorAlpha = 0;
                chartCursor.cursorPosition = "mouse";
                chartCursor.categoryBalloonDateFormat = "MM";
                chart.addChartCursor(chartCursor);

                // SCROLLBAR
                var chartScrollbar = new AmCharts.ChartScrollbar();
                chartScrollbar.graph = graph;
                chartScrollbar.backgroundColor = "#DDDDDD";
                chartScrollbar.scrollbarHeight = 30;
                chartScrollbar.selectedBackgroundColor = "#FFFFFF";
                chart.addChartScrollbar(chartScrollbar);
                // WRITE
                chart.write("chartdiv");
            });
			 function generateData(){
				$.ajax({
				asyc : false,
				url : base+"/app/detail/custom/show.do?method="+config.method+"&appName="+config.appName+"&keyName="+config.keyName+"&property="+config.property,	
				success : function (data){
					chart.dataProvider = data.list;
					chart.validateData();
				}
				});
				window.setTimeout(generateData,60000);
			}
			
}
function randomColor(){
    var rand = Math.floor(Math.random() * 0xFFFFFF).toString(16);
    if(rand.length == 6){
        return rand;
    }else{
        return randomColor();
    }
}