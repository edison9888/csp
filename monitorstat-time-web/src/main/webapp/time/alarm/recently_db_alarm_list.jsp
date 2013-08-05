<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
<%@page import="com.taobao.csp.time.web.po.BeiDouAlarmRecordPo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>实时监控系统</title>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
</head>

<body>

<%
List<BeiDouAlarmRecordPo> list = ( List<BeiDouAlarmRecordPo>)request.getAttribute("alarmList");
%>

	<div class="container-fluid">
		<div class="row-fluid" style="text-align: center">
			<table  class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<td>应用名</td>
						<td>数据库</td>
						<td>主机名</td>
						<td>IP</td>
						<td>告警原因</td>
						<td>发生时间</td>
					</tr>
				</thead>
				<tbody>
				<%
				for(BeiDouAlarmRecordPo record:list){
					
				%>
					<tr>
						<td><%=record.getGroupName() %></td>
						<td><%=record.getDbName() %></td>
						<td><%=record.getHostName() %></td>
						<td><%=record.getIp() %></td>
						<td><%=record.getAlarmMsg()%></td>
						<td><%=record.getTime()%></td>
					</tr>
				<%} %>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
