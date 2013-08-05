<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	isELIgnored="false" pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/easyui/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/easyui/icon.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/index.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easyui.min.js"></script>

<title>应用热点接口分布</title>
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

table {
	padding-left: 10px;
}
.outside{ 
	width:64px;
	height:64px;
	overflow:hidden;
} 
</style>
<%
	List<String> appList = new ArrayList<String>();
	appList.add("login");
	appList.add("hesper");
	appList.add("search");
	appList.add("detail");
	appList.add("buy");
	String[] timeArray = (String[])request.getAttribute("timeArray");
	if(timeArray == null)
		timeArray = new String[Constants.CACHE_TIME_INTERVAL];
	Map<String, Map<String, TimeDataInfo[]>> valueMapMain = (Map<String, Map<String, TimeDataInfo[]>>)request.getAttribute("valueMapMain");
	if(valueMapMain == null)
		valueMapMain = new HashMap<String, Map<String, TimeDataInfo[]>>();	
	
%>
</head>
<body>
	<%@ include file="../../header.jsp"%>
	<div align="center">
		<h1>核心应用热点接口信息</h1>
	</div>
	
	<table class="table table-striped table-condensed table-bordered">
	<thead>
		<tr>
			<td width="300px"><strong>时间</strong></td>
			<%
				for(int i=0; i<Constants.CACHE_TIME_INTERVAL;i++) {
			%>
				<td><strong><%=timeArray[i]%></strong></td>
			<%
				}
			%>
		</tr>	
	</thead>
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>" >
			<strong>login</strong>
			</td>
		</tr>
	</thead>
	<%
		Map<String, TimeDataInfo[]> valueMap = valueMapMain.get("login");
		int i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	<tr><td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
	<div align="center" class="outside"><img alt="跳转图片" src="<%=request.getContextPath()%>/statics/img/arrow_down.jpg" align="middle"></div>
	</td></tr>
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
			<strong>hesper</strong>
			</td>
		</tr>
	</thead>
	<%
		valueMap = valueMapMain.get("hesper");
		i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
			<strong>search</strong>
			</td>
		</tr>
	</thead>	
	<%
		valueMap = valueMapMain.get("search");
		i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	<tr><td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
	<div align="center" class="outside"><img alt="跳转图片" src="<%=request.getContextPath()%>/statics/img/arrow_down.jpg"></div>
	</td></tr>			
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
			<strong>detail</strong>
			</td>
		</tr>
	</thead>
	<%
		valueMap = valueMapMain.get("detail");
		i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	<tr><td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
	<div align="center" class="outside"><img alt="跳转图片" src="<%=request.getContextPath()%>/statics/img/arrow_down.jpg"></div>
	</td></tr>	
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
			<strong>buy</strong>
			</td>
		</tr>
	</thead>
	<%
		valueMap = valueMapMain.get("buy");
		i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	<tr><td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
	<div align="center" class="outside"><img alt="跳转图片" src="<%=request.getContextPath()%>/statics/img/arrow_down.jpg"></div>
	</td></tr>	
	<thead>
		<tr>
			<td colspan="<%=Constants.CACHE_TIME_INTERVAL + 1%>">
			<strong>consign</strong>
			</td>
		</tr>
	</thead>
	<%
		valueMap = valueMapMain.get("consign");
		i = 1;
		for(Map.Entry<String, TimeDataInfo[]> entry : valueMap.entrySet()) {
			String url = entry.getKey();
			TimeDataInfo[] array = entry.getValue();
		%>
		<tr>
		<td title="<%=url%>" width="300px"><a href="#"><%=url%></a></td>
		<%
			for(TimeDataInfo po:array) {
		%>
			<td><%=po.getMainValue()%></td>
		<%	
			}
		%>
		</tr>
		<%
		}
	%>
	</table>	
</body>
</html>