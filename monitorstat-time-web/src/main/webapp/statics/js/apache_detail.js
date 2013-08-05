// Top N��� begin
$(function() {
			var chartWidget = new ChartWidget();

		});


function ChartWidget() {
	var chart1DivId = "chart1Div";
	var rest1DivId = "rest1Div";
	var chart;
	var restChart;

	function init() {
		draw();
	}
	function draw() {

		// ��ʼ��chart�õ����ݣ����루generateChartData()��ִ�к󣬻�����������Щֵ
		var chartData = [{
					"pv" : "7",
					"ftime" : "15:58",
					"rest" : "4",
					"c302" : "6",
					"c200" : "5"
				}];
		AmCharts.ready(function() {

					// ///����chart
					// SERIAL CHART
					chart = new AmCharts.AmSerialChart();
					chart.dataProvider = chartData;
					chart.pathToImages = base + "/statics/js/amcharts/images/";
					chart.categoryField = "ftime";
					chart.marginTop = 0;
					chart.marginRight = 0;

					// value
					var valueAxis = new AmCharts.ValueAxis();
					valueAxis.axisColor = "#DADADA";
					valueAxis.dashLength = 1;
					chart.addValueAxis(valueAxis);

					// GRAPH pv
					var graphPv = new AmCharts.AmGraph();
					graphPv.lineColor = "#00FF00";
					graphPv.negativeLineColor = "#00FF00"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graphPv.bullet = "round";
					graphPv.bulletSize = 5;
					graphPv.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graphPv.lineThickness = 2;
					graphPv.type = "smoothedLine";
					graphPv.title = "ȫ������";
					graphPv.valueField = "pv";
					chart.addGraph(graphPv);

					// GRAPH c200
					var graph200 = new AmCharts.AmGraph();
					graph200.lineColor = "#99CC33";
					graph200.negativeLineColor = "#99CC33"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graph200.bullet = "round";
					graph200.bulletSize = 5;
					graph200.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graph200.lineThickness = 2;
					graph200.type = "smoothedLine";
					graph200.title = "����200";
					graph200.valueField = "c200";
					chart.addGraph(graph200);

					// GRAPH c302
					var graph302 = new AmCharts.AmGraph();
					graph302.lineColor = "#FF0000";
					graph302.negativeLineColor = "#FF0000"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graph302.bullet = "round";
					graph302.bulletSize = 5;
					graph302.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graph302.lineThickness = 2;
					graph302.type = "smoothedLine";
					graph302.title = "����302";
					graph302.valueField = "c302";
					chart.addGraph(graph302);

					// CURSOR
					var chartCursor = new AmCharts.ChartCursor();
					chartCursor.cursorAlpha = 0;
					chartCursor.cursorPosition = "mouse";
					chart.addChartCursor(chartCursor);

					// SCROLLBAR

					// LEGEND
					var legend = new AmCharts.AmLegend();
					legend.marginLeft = 110;
					chart.addLegend(legend);

					// WRITE
					chart.write(chart1DivId);

					// ��Ӧʱ�� chart

					restChart = new AmCharts.AmSerialChart();
					restChart.dataProvider = chartData;
					restChart.pathToImages = base
							+ "/statics/js/amcharts/images/";
					restChart.categoryField = "ftime";
					restChart.marginTop = 0;
					restChart.marginRight = 0;

					restChart.addValueAxis(valueAxis);

					// GRAPH rest
					var graphrest = new AmCharts.AmGraph();
					graphrest.lineColor = "#00FF00";
					graphrest.negativeLineColor = "#00FF00"; // this line
					// makes the
					// graph
					// to change color when it
					// drops below 0
					graphrest.bullet = "round";
					graphrest.bulletSize = 5;
					graphrest.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graphrest.lineThickness = 2;
					graphrest.type = "smoothedLine";
					graphrest.title = "ȫ��ƽ����Ӧʱ��";
					graphrest.valueField = "rest";
					restChart.addGraph(graphrest);

					// CURSOR
					var restCursor = new AmCharts.ChartCursor();
					restCursor.cursorAlpha = 0;
					restCursor.cursorPosition = "mouse";
					restChart.addChartCursor(restCursor);

					var restlegend = new AmCharts.AmLegend();
					restlegend.marginLeft = 110;
					restChart.addLegend(restlegend);

					restChart.write(rest1DivId);

					generateChartData();
				});

	}

	function generateChartData() {
		$.ajax({
			url : base
					+ "/app/detail/apache/show.do?method=ipdetail&appName="+ page.appName +"&ip="+page.ip,
			success : function(data) {
				chart.dataProvider = data;
				restChart.dataProvider = data;
				chart.validateData();
				restChart.validateData();
			}
		});


	}
	init();
}// ChartWidget

