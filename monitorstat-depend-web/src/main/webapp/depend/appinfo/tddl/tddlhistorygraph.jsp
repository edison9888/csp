<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.taobao.csp.depend.po.tddl.ConsumeTDDLDetail"%>
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
			String startDate = (String)request.getAttribute("startDate");
			String endDate = (String)request.getAttribute("endDate");
			String name = (String)request.getAttribute("name");
			String type = (String)request.getAttribute("type");
        
                	List<ConsumeTDDLDetail> list = (List<ConsumeTDDLDetail>) request
                			.getAttribute("list");
                	if (list == null)
                		list = new ArrayList();

                	StringBuilder sb = new StringBuilder("[");
                	for (ConsumeTDDLDetail detail : list) {
                		Date date = detail.getCollect_time();
                		sb.append("{").append("date:new Date(").append(date.getTime())
                				.append("),").append("callnum:")
                				.append(detail.getExecuteSum()).append(",")
                				.append("calltime:").append(detail.getTimeAverage())
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
                durationGraph.title = "调用次数";//图例的名称
                durationGraph.valueField = "callnum";
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
                distanceGraph.balloonText = "[[value]]ms";
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
	<div>
		<div align="center">
			<h1>
				<%
					if ("1".equals(type)) {
						out.println("应用" + name + "历史调用信息统计");
					} else {
						out.println("数据库" + name + "历史信息统计");
					}
				%>
			</h1>
		</div>
		<form action="<%=request.getContextPath()%>/tddl/show.do"
			method="get">
			<input type="hidden" value="${name}" name="name" id="name"> <input
				type="hidden" value="<%=type%>" name="type" id="type"> <input
				type="hidden" value="gotoTddlHistoryGraph" name="method" id="type">
			<table>
				<tr>
					<td>开始日期:<input type="text" id="startDate"
						value="${startDate}" name="startDate"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />
					<td>
			
					<td>结束日期:<input type="text" id="endDate" value="${endDate}"
						name="endDate"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />
					<td>
					<td><input type="submit" value="查询" class="btn btn-success"></td>
				</tr>
	</table>
	</form>
	</div>    
        <div id="chartdiv" style="width: 100%; height: 400px;"></div></body
					>
</html>