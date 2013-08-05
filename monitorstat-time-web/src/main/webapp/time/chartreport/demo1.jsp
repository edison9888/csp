<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <%
        //暂时最多支持5条线，如果需要可以自己添加
       	//颜色可以参考:http://playlaughjessie.diandian.com/post/2011-11-28/7219040
        String[] colorArray = new String[]{"#003366","#00FFFF","#FFE4C4"," #A52A2A","#b5030d"};
        
        %>
        <title>报表</title>
        <script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
        <script type="text/javascript">
            var chart;
            var chartData = [];
            var chartCursor;
            AmCharts.ready(function () {
                // generate some data first
                generateChartData();

                // SERIAL CHART    
                chart = new AmCharts.AmSerialChart();
                chart.pathToImages = "<%=request.getContextPath()%>/statics/js/amcharts/images/";
                chart.zoomOutButton = {
                    backgroundColor: '#000000',
                    backgroundAlpha: 0.15
                };
                chart.dataProvider = chartData;
                chart.categoryField = "date";

                // listen for "dataUpdated" event (fired when chart is rendered) and call zoomChart method when it happens
                chart.addListener("dataUpdated", zoomChart);

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
                categoryAxis.minPeriod = "mm"; // our data is daily, so we set minPeriod to DD
                categoryAxis.dashLength = 1;
                categoryAxis.gridAlpha = 0.15;
                categoryAxis.axisColor = "#DADADA";

                // value                
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.axisAlpha = 0.2;
                valueAxis.dashLength = 1;
                valueAxis.title = "调用次数";
                chart.addValueAxis(valueAxis);

                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.title = "平均时间";
                graph.valueField = "visits";
                graph.valueAxis = valueAxis;
                graph.bullet = "round";
                graph.bulletBorderColor = "#FFFFFF";
                graph.bulletBorderThickness = 2;
                graph.lineThickness = 2;
                graph.lineColor = "#b5030d";
                graph.negativeLineColor = "#0352b5";
                graph.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
                chart.addGraph(graph);
                
                // GRAPH
                var graph2 = new AmCharts.AmGraph();
                graph2.title = "red line";
                graph2.valueField = "times";
                graph2.bullet = "round";
                graph2.bulletBorderColor = "#FFFFFF";
                graph2.bulletBorderThickness = 2;
                graph2.lineThickness = 2;
                graph2.lineColor = "#0000FF";
                graph2.negativeLineColor = "#0352b5";
                graph2.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
                chart.addGraph(graph2);                

                // CURSOR 鼠标手型
                chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorPosition = "mouse";
                chart.addChartCursor(chartCursor);


                // LEGEND 图例显示
                var legend = new AmCharts.AmLegend();
                legend.align = "center";
                chart.addLegend(legend);
                
                // SCROLLBAR 拖动框
                var chartScrollbar = new AmCharts.ChartScrollbar();
                chartScrollbar.graph = graph;
                chartScrollbar.scrollbarHeight = 40;
                chartScrollbar.color = "#FFFFFF";
                chartScrollbar.autoGridCount = true;
                chart.addChartScrollbar(chartScrollbar);

                // WRITE
                chart.write("chartdiv");
            });

            // generate some random data, quite different range
            function generateChartData() {
                var firstDate = new Date();
                firstDate.setMinutes(firstDate.getMinutes() - 1440);
                for (var i = 0; i < 1440; i++) {
                    var newDate = new Date(firstDate);
                    newDate.setMinutes(newDate.getMinutes() + i);
                    var visits = Math.round(Math.random() * 40) + 20;
                    var times = Math.round(Math.random() * 40) + 30;
                    chartData.push({
                        date: newDate,
                        visits: visits,
                        times: times
                    });
                }
            }

            // this method is called when chart is first inited as we listen for "dataUpdated" event
            function zoomChart() {
                // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
                chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
            }
            
            // changes cursor mode from pan to select
            function setPanSelect() {
                if (document.getElementById("rb1").checked) {
                    chartCursor.pan = false;
                    chartCursor.zoomable = true;
                    
                } else {
                    chartCursor.pan = true;
                }
                chart.validateNow();
            }            
        </script>
    </head>
    
    <body>
        <div id="chartdiv" style="width: 100%; height: 400px;"></div>
        <div id="chartdiv" style="margin-left:35px;">
            <input type="radio" checked="true" name="group" id="rb1" onclick="setPanSelect()">画区域
            <input type="radio" name="group" id="rb2" onclick="setPanSelect()">拖动
		</div>        
    </body>
</html>