<%@page import="com.taobao.csp.depend.po.tair.TairConsumeMachine"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<!-- JQuery相关的JS都在leftmenu.jsp中定义了 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>调用tair的应用的机器分布</title>
<%
List<TairConsumeMachine> list = ( List<TairConsumeMachine>)request.getAttribute("list");
Collections.sort(list);

String opsName = (String)request.getAttribute("opsName");
String tairgroupname = (String)request.getAttribute("tairgroupname");
String namespace = (String)request.getAttribute("namespace");
String actiontype = (String)request.getAttribute("actiontype");
%>
<style type="text/css">
h2{
	padding-left: 50px;
}
</style>
</head>
<body>
<H4 align="center">应用<%=opsName%>调用Tair的机器分布统计</H4>
<H2 align="left">tairgroupname：<%=tairgroupname%></H2>
<H2 align="left">namespace：<%=namespace%></H2>
<% 
	if(actiontype != null) {
%>
	<H2 align="left">actiontype：<%=actiontype%></H2>
<%		
	}
%>
<table   width="100%">
		<tr>				
			<td>
				<table width="100%" class="table table-striped table-bordered table-condensed">
				   <tr>
				   		<td>机器IP</td>
				   		<td>机房</td>
				   		<td>调用量</td>
				   </tr>
				   <%for(TairConsumeMachine m:list){ %>
				   <tr>
				   		<td><%=m.getConsumeMachineIp()%></td>
				   		<td><%=m.getConsumeMachineCm()%></td>
				   		<td><%=Utlitites.fromatLong(m.getCallnum()+"")%></td>
				   </tr>
				   <%} %>
				</table>				
			</td>
		</tr>
	</table>
</body>
</html>