<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.taobao.csp.time.web.po.AmchartTwoYPo"%>
<%@ page import="com.taobao.csp.time.web.po.AmchartTwoYPo.ValuePo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
        <script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>		        
        <%
                	AmchartTwoYPo chart = (AmchartTwoYPo)request.getAttribute("chart");
        			if(chart == null)
        				chart = new AmchartTwoYPo();
        			List<ValuePo> list = chart.getList(); 
                	StringBuilder sb = new StringBuilder("[");
                	int i=0;
                	for (ValuePo detail : list) {
                		sb.append("{").append("date:new Date(").append(detail.getCollectTimeL())
                				.append("),").append("callnum:")
                				.append(detail.getExecuteSum()).append(",")
                				.append("calltime:").append(detail.getTimeNum())
                				.append("}").append(",");
                	}
                	if (list.size() > 0)
                		sb.deleteCharAt(sb.length() - 1);
                	sb.append("]");
                %>        
        <script type="text/javascript">
            var chartData = eval("<%=sb.toString()%>");
            var chart;
            
            AmCharts.ready(function () {
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.pathToImages = "<%=request.getContextPath()%>/statics/js/amcharts/images/";
                chart.zoomOutButton = {
                    backgroundColor: '#000000',
                    backgroundAlpha: 0.15
                };                
                // listen for "dataUpdated" event (fired when chart is rendered) and call zoomChart method when it happens
                chart.addListener("dataUpdated", zoomChart);      
                
                chart.dataProvider = chartData;
                chart.categoryField = "date";
                chart.marginTop = 0;
                // AXES
                // category axis
                var categoryAxis = chart.categoryAxis;
                categoryAxis.parseDates = true; 
                categoryAxis.minPeriod = "mm"; 
                categoryAxis.gridCount = 50;
                categoryAxis.gridAlpha = 0;
                categoryAxis.gridColor = "#000000";
                categoryAxis.axisColor = "#555555";
                categoryAxis.dateFormats = [{period:'fff',format:'JJ:NN:SS'},{period:'ss',format:'JJ:NN:SS'},{period:'mm',format:'JJ:NN'},{period:'hh',format:'JJ:NN'},{period:'DD',format:'MMM DD'},{period:'MM',format:'MMM'},{period:'YYYY',format:'YYYY'}];

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
                chart.addValueAxis(durationAxis);

                // Distance value axis 
                var distanceAxis = new AmCharts.ValueAxis();
                distanceAxis.title = "平均";//
                distanceAxis.gridAlpha = 0;
                distanceAxis.position = "right";
                distanceAxis.inside = true;
                distanceAxis.unit = "ms";//Y轴的单位
                distanceAxis.axisAlpha = 0;
                chart.addValueAxis(distanceAxis);

                // GRAPHS
                // duration graph
                var durationGraph = new AmCharts.AmGraph();
                durationGraph.title = "调用次数";//图例的名称
                durationGraph.valueField = "callnum";
                durationGraph.type = "smoothedLine";
                durationGraph.valueAxis = durationAxis; 
                durationGraph.lineColor = "#CC0000";
                durationGraph.balloonText = "[[value]]";
                durationGraph.lineThickness = 1;
                durationGraph.legendValueText = "[[value]]";
                //durationGraph.bullet = "square";
                chart.addGraph(durationGraph);
                
                var distanceGraph = new AmCharts.AmGraph();
                distanceGraph.title = "调用时间";//图例的名称
                distanceGraph.valueField = "calltime";
                distanceGraph.type = "smoothedLine";
                distanceGraph.valueAxis = distanceAxis;
                distanceGraph.lineColor = "#2F4F4F";
                distanceGraph.balloonText = "[[value]]";
                distanceGraph.lineThickness = 1;
                distanceGraph.legendValueText = "[[value]]";
                //distanceGraph.bullet = "square";
                chart.addGraph(distanceGraph);                

                // CURSOR,鼠标放上去。            
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.zoomable = false;
                chartCursor.categoryBalloonDateFormat = "JJ:NN";
                chartCursor.cursorAlpha = 0;
                chartCursor.categoryBalloonEnabled = true;
                chart.addChartCursor(chartCursor);

                // LEGEND
                var legend = new AmCharts.AmLegend();
                legend.bulletType = "round";
                legend.equalWidths = false;
                legend.valueWidth = 120;
                legend.color = "#000000";
                chart.addLegend(legend);
                
                // SCROLLBAR 拖动框
                var chartScrollbar = new AmCharts.ChartScrollbar();
                chartScrollbar.graph = durationGraph;
                chartScrollbar.scrollbarHeight = 40;
                chartScrollbar.color = "#FFFFFF";
                chartScrollbar.autoGridCount = true;
                chart.addChartScrollbar(chartScrollbar);

                // WRITE                                
                chart.write("chartdiv")
            });

            // this method is called when chart is first inited as we listen for "dataUpdated" event
            function zoomChart() {
                // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
                chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
            }
        </script>
    </head>
    <body>
    	<div>
    		调用次数汇总:${totalNum}<br/>
    		调用时间汇总:${totalTime}
    	</div>
        <div id="chartdiv" style="width: 100%; height: 400px;"></div>
        </body>
</html>