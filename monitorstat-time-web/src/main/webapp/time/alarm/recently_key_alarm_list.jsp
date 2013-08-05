<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
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
List<CspTimeKeyAlarmRecordPo> list = ( List<CspTimeKeyAlarmRecordPo>)request.getAttribute("alarmList");
%>

	<div class="container-fluid">
		<div class="row-fluid" style="text-align: center">
			<table  class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<td>范围</td>
						<td>告警点</td>
						<td>判断模型</td>
						<td>告警值</td>
						<td>告警原因</td>
						<td>发生时间</td>
					</tr>
				</thead>
				<tbody>
				<%
				for(CspTimeKeyAlarmRecordPo record:list){
					String keSocpe = record.getKey_scope();
					
				%>
					<tr>
						<td><%if(keSocpe.equals("APP")){out.print("全网");}else{out.print(record.getIp());} %></td>
						<td><%=record.getKey_name()+"_"+record.getProperty_name() %></td>
						<td><%=record.getMode_name() %></td>
						<td><%=record.getAlarm_value() %></td>
						<td><%=record.getAlarm_cause()%></td>
						<td><%=record.getAlarm_time()%></td>
					</tr>
				<%} %>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
