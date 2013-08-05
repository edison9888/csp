<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataForPageViewPo"%>
<%@page import="com.taobao.monitor.web.util.DateFormatUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<!doctype html>
<head>
<title>无线系统告警日报</title>
<style type="text/css">
body {
	font-family:"Microsoft YaHei", verdana, arial, times, sans-serif;
	font-size:14px;
	background-color:#f9f8f7;
	margin:0;
	padding:0;
	border:0;
}
a {
	color: #c75f3e;
}
.container {
	margin:12px auto;
}
.report-title {
	margin:0 24px;
	border-radius:6px;
	background:#F7F2D7;
	position: relative;
	height: 38px;
	border-bottom: 1px solid #F8C186;
	box-shadow: 0 -1px 0 #F7F2D7 inset,0 0 4px rgba(0, 0, 0, .15);
	-moz-shadow: 0 -1px 0 #F7F2D7 inset,0 0 4px rgba(0,0,0,.15);
	-webkit-shadow: 0 -1px 0 #F7F2D7 inset,0 0 4px rgba(0,0,0,.15);
	text-align:center;
	vertical-align:middle;
	line-height:38px;
	font-size:22px;
}
.report-tip {
	margin:0 24px;
}
.tbcontainer {
	margin:12px 24px;
}
.gradient-style {
	font-size: 12px;
	text-align: left;
	border-collapse: collapse;
	margin:0;
	border: 1px solid #D3DDFF;
}
.gradient-styleb tr {
	overflow:auto;
	background: #f00;
}
.gradient-style th {
	font-size: 14px;
	font-weight: normal;
	background: #B9C9FE url(http://img02.taobaocdn.com/tps/i2/T1_TN.XCNcXXaBuAo.-1-33.png) repeat-x;
	border-top: 2px solid #D3DDFF;
	border-bottom: 1px solid #69C;
	padding: 8px;
	text-align:center;
}
.gradient-style .topic {
	background: #B9C9FE url(http://img02.taobaocdn.com/tps/i2/T1_TN.XCNcXXaBuAo.-1-33.png) repeat-x;
	border-top: 1px solid #69C;
}
.gradient-style th.title {
	font-size:18px;
	width: auto;
	background: #E8EDFF;
}
.gradient-style td {
	border-bottom: 1px solid white;
	border-right: 1px solid white;
	background: #E8EDFF url(http://img01.taobaocdn.com/tps/i1/T1Lqd.XEBfXXb1d7o.-1-32.png) repeat-x;
	padding: 8px;
	white-space:normal;
	word-wrap:break-word;
	word-break:break-all;
}
.gradient-style tbody tr:hover td {
	background: #D0DAFD url(http://img04.taobaocdn.com/tps/i4/T1gg8YXvtfXXb1d7o.-1-32.png) repeat-x;
	color: #339;
}
.gradient-style tfoot tr td {
	background: #E8EDFF;
	font-size: 12px;
	border-top: 1px solid white;
	border-bottom: 1px solid #D3DDFF;
}
#rounded-corner {
	font-size: 12px;
	text-align: left;
	border-collapse: collapse;
}
#rounded-corner th {
	font-weight: normal;
	font-size: 13px;
	color: #039;
	background: #B9C9FE;
	padding: 8px;
}
#rounded-corner td {
	background: #E8EDFF;
	border-top: 1px solid white;
	border-right: 1px solid white;
	border-bottom: 1px solid white;
	color: #669;
	padding: 8px;
}
#rounded-corner th {
	font-weight: normal;
	font-size: 13px;
	color: #039;
	background: #B9C9FE;
	padding: 8px;
}
#rounded-corner tbody tr:hover td {
	background: #D0DAFD;
}
.color-red {
	color:#FF3333;
}
.color-black {
	color:black;
}
.color-yellow{
	color:#F27B04;
}
.time-emp {
	font-weight:bold;
}
.date-emp {
	margin:0 4px;
	color:#FF3300;
}
.no-alarm{
	margin:0 auto;
	color:#04c204;
}
</style>
</head>
<body>
<div class="container">
	<div class="report-title">交易关联系统应用告警日报</div>
	<div class="report-tip"></div>
<%
	SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String appName = (String)request.getParameter("appName");
	String day = (String)request.getParameter("day");
	String startTimeStr = (String)request.getParameter("start");
	String endTimeStr = (String)request.getParameter("end");
	if(day != null){
		startTimeStr = day + " 00:00:00";
		endTimeStr = day + " 23:59:59";
	}
	Date startTime = DateFormatUtil.stringToDate(startTimeStr);
	Date endTime = DateFormatUtil.stringToDate(endTimeStr);
	
	String pageSizeStr = (String)request.getParameter("size");
	String pageNumStr = (String)request.getParameter("num");
	String totalStr = (String)request.getParameter("total");
	int total = 0;
	int pageSize = 30;
	int pageNum = 1;
	int totalPageNum = 1;
	try{
		if(pageSizeStr != null){
			pageSize = Integer.parseInt(pageSizeStr);
			if(pageSize<10){
				pageSize = 10;
			}
		}
		if(totalStr != null){
			totalPageNum = Integer.parseInt(totalStr);
			if(totalPageNum<1){
				totalPageNum = 1;
			}
		} else {
			total = MonitorAlarmAo.get().getSingleAppAlarmCount(appName, startTime, endTime);
			totalPageNum = total/pageSize;
		}
		if( pageNumStr!= null ){
			pageNum = Integer.parseInt(pageNumStr);
			if(pageNum < 1){
				pageNum = 1;
			}
		}
	} catch(Exception e){
		%><div style="display:none">count数据异常<%=e.toString()%></div><% 
	}
	
%>
	<div class="tbcontainer">
		<table class="gradient-style" cellpadding="0" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th colspan="6" class="title"><%=appName%>系统<span class="date-emp"><%=DateFormatUtil.formatMailParameterTime(startTime)%></span>告警日报</th>
				</tr>
				<tr>
					<th width="60" text-align="center">告警级别</th>
					<th>告警指标</th>
					<th width="120">告警阀值</th>
					<th>告警原因</th>
					<th width="92">告警IP</th>
					<th width="124">告警时间</th>
				</tr>
			</thead>
			<tbody>
<%
	List<AlarmDataForPageViewPo> appAlarmDataList = MonitorAlarmAo.get().getSingleAppAlarmList(appName, startTime, endTime, pageSize, pageNum);
	if(appAlarmDataList != null && appAlarmDataList.size() > 0){
		for(AlarmDataForPageViewPo vpo:appAlarmDataList){
%>
				<tr>
					<td>P<%=vpo.getLevel()%></td>
					<td class="color-yellow"><%=vpo.getKeyName()%></td>		
					<td><%=vpo.getAlarmValue()%></td>
					<td class="color-red"><%=vpo.getAlarmCause()%></td>
					<td><%=vpo.getIpString()%></td>
					<td class="time-emp"><%=standardSdf.format( vpo.getAlarmTime() )%></td>
				</tr>
<%
		}
		%>
		<tr>
			<td class="no-alarm">当前第<%=pageNum%>页</td>
			<td colspan="4" class="no-alarm">
			 <%
			 for(int i=1; i<=totalPageNum; i++){
				 %>
				 <a href="app_alarm_report.jsp?appName=<%=appName%>&start=<%=startTimeStr%>&end=<%=endTimeStr%>&size=<%=pageSize%>&num=<%=i%>&total=<%=totalPageNum%>" target="_self" <% if(i==pageNum) {%>style="font-weight:bold;color:#F30"<%}%>><%=i%></a>
				 <%
			 }
			 %>
			 </td>
			 <td class="no-alarm">共<%=totalPageNum%>页</td>
		</tr>
		<%
	} else {
		%>
		<tr>
			<td colspan="6" class="no-alarm"><%=appName%>系统没有更多告警了</td>
		</tr>
		<%
	}
%>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6"><em>CSP每天统计发送无线系统告警邮件给应用负责人</em></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
</body>
</html>
