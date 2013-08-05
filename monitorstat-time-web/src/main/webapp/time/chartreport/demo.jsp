<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
        <script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
        <script type="text/javascript">
            var chartData = [{
                date: new Date(2011, 12, 31),
                calltime: 227,
                calltime: 408
            }, {
                date: new Date(2012, 1, 1),
                calltime: 10,
                calltime: 408
            }, {
                date: new Date(2012, 1, 2),
                calltime: 371,
                calltime: 482
            }, {
                date: new Date(2012, 1, 3),
                calltime: 433,
                calltime: 562
            }, {
                date: new Date(2012, 1, 4),
                calltime: 20,
                calltime: 379
            }, {
                date: new Date(2012, 1, 5),
                calltime: 480,
                calltime: 501
            }, {
                date: new Date(2012, 1, 6),
                calltime: 386,
                calltime: 443
            }, {
                date: new Date(2012, 1, 7),
                calltime: 348,
                calltime: 405
            }, {
                date: new Date(2012, 1, 8),
                calltime: 238,
                calltime: 309
            }, {
                date: new Date(2012, 1, 9),
                calltime: 218,
                calltime: 287
            }, {
                date: new Date(2012, 1, 10),
                calltime: 349,
                calltime: 485
            }, {
                date: new Date(2012, 1, 11),
                calltime: 603,
                calltime: 890
            }, {
                date: new Date(2012, 1, 12),
                calltime: 534,
                calltime: 810
            }];
            var chart;

            AmCharts.ready(function () {
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.dataProvider = chartData;
                chart.categoryField = "date";
                chart.marginTop = 0;

                // AXES
                // category axis
                var categoryAxis = chart.categoryAxis;
                categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
                categoryAxis.minPeriod = "DD"; // our data is daily, so we set minPeriod to DD                
                categoryAxis.autoGridCount = false;
                categoryAxis.gridCount = 50;
                categoryAxis.gridAlpha = 0;
                categoryAxis.gridColor = "#000000";
                categoryAxis.axisColor = "#555555";
                // we want custom date formatting, so we change it in next line
                categoryAxis.dateFormats = [{
                    period: 'DD',
                    format: 'DD'
                }, {
                    period: 'WW',
                    format: 'MMM DD'
                }, {
                    period: 'MM',
                    format: 'MMM'
                }, {
                    period: 'YYYY',
                    format: 'YYYY'
                }];

                // as we have data of different units, we create two different value axes
                // Duration value axis            
                var durationAxis = new AmCharts.ValueAxis();
                durationAxis.title = "调用次数";//Y轴的显示
                durationAxis.gridAlpha = 0.05;
                durationAxis.axisAlpha = 0;
                durationAxis.inside = true;
                // the following line makes this value axis to convert values to duration
                // it tells the axis what duration unit it should use. mm - minute, hh - hour...                
                durationAxis.duration = "";//Y轴的单位
                durationAxis.durationUnits = {
                    DD: "d. ",
                    hh: "h ",
                    mm: "min",
                    ss: ""
                };
                chart.addValueAxis(durationAxis);

                // Distance value axis 
                var distanceAxis = new AmCharts.ValueAxis();
                distanceAxis.title = "平均时间";//
                distanceAxis.gridAlpha = 0;
                distanceAxis.position = "right";
                distanceAxis.inside = true;
                distanceAxis.unit = "ms";//Y轴的单位
                distanceAxis.axisAlpha = 0;
                chart.addValueAxis(distanceAxis);

                // GRAPHS
                // duration graph
                var durationGraph = new AmCharts.AmGraph();
                durationGraph.title = "调用次数1";//图例的名称
                durationGraph.valueField = "calltime";
                durationGraph.type = "line";
                durationGraph.valueAxis = durationAxis; // indicate which axis should be used
                durationGraph.lineColor = "#CC0000";
                durationGraph.balloonText = "[[value]]";
                durationGraph.lineThickness = 1;
                durationGraph.legendValueText = "[[value]]";
                durationGraph.bullet = "square";
                chart.addGraph(durationGraph);

                // distance graph
                var distanceGraph = new AmCharts.AmGraph();
                distanceGraph.valueField = "calltime";
                distanceGraph.title = "平均时间";//图例的title
                distanceGraph.type = "column";
                distanceGraph.fillAlphas = 0.1;
                distanceGraph.valueAxis = distanceAxis; // indicate which axis should be used
                distanceGraph.balloonText = "[[value]] miles";
                distanceGraph.legendValueText = "[[value]] mi";
                distanceGraph.lineColor = "#000000";
                distanceGraph.lineAlpha = 0;
                chart.addGraph(distanceGraph);

                // CURSOR                
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.zoomable = false;
                chartCursor.categoryBalloonDateFormat = "DD";
                chartCursor.cursorAlpha = 0;
                chart.addChartCursor(chartCursor);

                // LEGEND
                var legend = new AmCharts.AmLegend();
                legend.bulletType = "round";
                legend.equalWidths = false;
                legend.valueWidth = 120;
                legend.color = "#000000";
                chart.addLegend(legend);

                // WRITE                                
                chart.write("chartdiv")
            });
        </script>
    </head>
    <body>
        <div id="chartdiv" style="width:100%; height:400px;"></div>
    </body>
</html>