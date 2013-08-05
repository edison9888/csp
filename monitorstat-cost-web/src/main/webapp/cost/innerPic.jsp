<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<%
String app=request.getParameter("app");
String vinfo=request.getParameter("vinfo");
String dinfo=request.getParameter("dinfo");

String conId="con-"+app;
%>

	<div id="<%=conId %>" style="width:920px;height:200px;margin-bottom:20px;">
		

	</div>
<hr  style="width:1020px;"/>
<script type="text/javascript">
    $(document).ready(function() {

        chart = new Highcharts.Chart({
            chart: {
                renderTo: '<%=conId %>',
                type: 'line',
                marginRight: 130,
                marginBottom: 25
            },
            title: {
                text: '<%=app %>-成本变化',
                x: -20 //center
            },
            xAxis: {
                categories: <%=dinfo %>
            },
            yAxis: {
                title: {
                    text: '成本'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        this.x +': '+ this.y +'￥';
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -10,
                y: 100,
                borderWidth: 0
            },
            series: [{
                name: '<%=app %>',
                data: <%=vinfo %>
            }]
        });
    });

</script>
