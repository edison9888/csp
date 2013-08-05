<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">



<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/application.js"></script>


<%
List<HostPo>  hostList = (ArrayList<HostPo>)request.getAttribute("hostList");

Set<HostPo> monitorHostSet = (HashSet<HostPo>)request.getAttribute("monitorHostSet");

String opsName = (String)request.getAttribute("opsName");
%>

<title><%=opsName %> 主机列表</title>
</head>
<body>
<div class="container">
	<br>
	<table class="bordered-table zebra-striped">
		<thead>
			<tr>
				<th class="blue">应用名</th>
				<th class="blue">主机数</th>
				<th class="blue">监控主机数</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><%=opsName %></td>
				<td><%=hostList.size() %></td>
				<td><%=hostList.size() - monitorHostSet.size() %></td>
			</tr>
		</tbody>
	</table>
	<br>
	<table class="zebra-striped condensed-table" id="sortTable3">
		<thead>
			<tr>
				<th class="blue">主机名</th>
				<th class="blue">IP</th>
				<th class="blue">机房</th>
				<th class="blue">实时监控</th>
			</tr>
		</thead>
		<tbody>
		<%
		for (HostPo po : hostList) {
		%>
			<tr>
				<td><%=po.getHostName() %></td>
				<td><%=po.getHostIp() %></td>
				<td><%=po.getHostSite() %></td>
				<td><%=!monitorHostSet.contains(po) %></td>
			</tr>
		<%
		}
		%>
		</tbody>
		
	</table>
</div>
</body>
</html>