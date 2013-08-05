<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.taobao.csp.time.web.po.AmchartPo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <%
        //这个页面时单个Y轴，多条线的意思
        //暂时最多支持5条线，如果需要可以自己添加
       	//颜色可以参考:http://playlaughjessie.diandian.com/post/2011-11-28/7219040
        String[] colorArray = new String[]{"#003366","#FFE4C4"," #A52A2A","#b5030d","#00FFFF"};
        AmchartPo chart = (AmchartPo)request.getAttribute("chart");
        %>
        <title>报表</title>
        <script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
        <script type="text/javascript">
        <%
        	if(chart != null) {
        		List<Map<String,Object>> valueList = chart.getValueList();
        		Map<String,String> fieldMap = chart.getFieldMap();
        		String timeUnit = chart.getTimeUnit();
        		String yTitle = chart.getyTitle();
        		String xField = chart.getxField();
        %>
        var chart;
        var chartData = [];
        var chartCursor;
        AmCharts.ready(function () {
        generateChartData();
        // SERIAL CHART    
        chart = new AmCharts.AmSerialChart();
        chart.pathToImages = "<%=request.getContextPath()%>/statics/js/amcharts/images/";
        chart.zoomOutButton = {
            backgroundColor: '#000000',
            backgroundAlpha: 0.15
        };
        chart.categoryField = "<%=xField%>";
        
        // listen for "dataUpdated" event (fired when chart is rendered) and call zoomChart method when it happens
        chart.addListener("dataUpdated", zoomChart);      
        
        // AXES
        // category
        var categoryAxis = chart.categoryAxis;
        categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
        categoryAxis.minPeriod = "<%=timeUnit%>"; // 目前支持mm,hh,DD,MM 注意，区分大小写
        categoryAxis.dashLength = 1;
        categoryAxis.gridAlpha = 0.15;
        categoryAxis.axisColor = "#DADADA";
        
        // value                
        var valueAxis = new AmCharts.ValueAxis();
        valueAxis.axisAlpha = 0.2;
        valueAxis.dashLength = 1;
        valueAxis.title = "<%=yTitle%>";
        chart.addValueAxis(valueAxis);

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
        <%
        int i=0; 
        
		for(Entry<String,String> entry : fieldMap.entrySet()) {
			String field = entry.getKey();
			if(field.equals(xField))	//过滤掉横轴坐标的那个属性，如date
				continue;
		%>
	        // GRAPH
	        var graph = new AmCharts.AmGraph();
	        graph.title = "<%=entry.getValue()%>";
	        graph.valueField = "<%=entry.getKey()%>";
	        graph.valueAxis = valueAxis;
	        graph.bullet = "round";
	        graph.bulletBorderColor = "#FFFFFF";
	        graph.bulletBorderThickness = 2;
	        graph.lineThickness = 2;
	        graph.lineColor = "<%=colorArray[i++]%>";
	        graph.negativeLineColor = "#0352b5";
	        graph.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
	        chart.addGraph(graph);
		<%
		}
        %>
            chart.dataProvider = chartData;
            // WRITE
            chart.write("chartdiv");  
        });

        // generate some random data, quite different range
        function generateChartData() {
        <%
        	for(Map<String,Object> valueMap: valueList) {
	        	StringBuilder sb = new StringBuilder();
	        	sb.append("chartData.push({");
	        	for(String fieldName : fieldMap.keySet()) {
	        		if(fieldName.equals(xField)) {
	        			sb.append(xField).append(":").append("new Date(" + valueMap.get(xField) + "),");		
	        		} else {
	        			sb.append(fieldName).append(":").append(valueMap.get(fieldName) + ",");
	        		}
	        	}
	        	sb.deleteCharAt(sb.length() - 1);	//不做校验
	        	sb.append("});");
	        	System.out.println("********************");
	        	System.out.println(sb.toString());
	        	out.println(sb);
        	}
        %>
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
        <%		
        	} else {
        		out.println("没有查询的数据！");
        	}
        %>
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