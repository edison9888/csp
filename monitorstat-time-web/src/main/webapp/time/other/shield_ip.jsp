<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.csp.time.cache.ShieldIpCache"%>
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
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
</head>
<body>
<table>
		<tr>
			<td>应用</td>
			<td>ip</td>
			<td>操作</td>
		</tr>
	<%
	
	String m = request.getParameter("m");
	String ip = request.getParameter("ip");
	if(m!= null){
		if("delete".equals(m)){
			if(ip != null)
				 ShieldIpCache.get().deleteShieldIp(ip);
		}
		if("add".equals(m)){
			if(ip != null)
				 ShieldIpCache.get().addShieldIp(ip);
		}
	}
	
	List<String> list = ShieldIpCache.get().getShieldIps();
	
	for(String ip1:list){
		
		HostPo po = CspCacheTBHostInfos.get().getHostInfoByIp(ip1);
	%>
		<tr>
			<td><%=po==null?"":po.getOpsName() %></td>
			<td><%=ip1 %></td>
			<td><a href="shield_ip.jsp?ip=<%=ip1 %>&m=delete">删除</a></td>
		</tr>
	<%} %>
	</table>
	<form action="shield_ip.jsp">
		<input type="hidden"  name="m" value="add">
		<input type="text"  name="ip" value="">
		<input type="submit" value="屏蔽告警">
	</form>
</body>
</html>
