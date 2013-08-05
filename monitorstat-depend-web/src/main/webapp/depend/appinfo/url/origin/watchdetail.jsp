<%@page import="com.taobao.csp.depend.po.url.UrlOriginDetail"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>查看URL详细</title>
<%
List<UrlOriginDetail> urlList = ( List<UrlOriginDetail>)request.getAttribute("urlList");
String opsName = (String)request.getAttribute("opsName");
long totalNumber = (Long)request.getAttribute("totalNumber");
String queryUrl = (String)request.getAttribute("queryUrl");
boolean isOrigin = (Boolean)request.getAttribute("isOrigin");
Map<String, ProvideSiteRating> machineMap = (Map<String, ProvideSiteRating>)request.getAttribute("machineMap");

String title = "";
String urlName = "";
if(isOrigin) {
	title = "请求的URL详细信息";
	urlName = "跳转的URL";
} else {
	title = "跳转的URL详细信息";
	urlName = "请求的URL";
}
%>
<style type="text/css">
h2{
	padding-left: 50px;
}
thead{
	background: #fff;
	color: #4f6b72;
}
</style>
</head>
<body>
<H4 align="center">应用<%=opsName%><%=title%></H4>
<H2 align="left">URL：<%=queryUrl%></H2>
<H2 align="left">查询时间：${selectDate}</H2>
<H2 align="left">对比时间：${selectPreDate}</H2>
	<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
		<tr>
			<%for(String name: machineMap.keySet()) {
				%>
				<td><%=name%></td>
				<%
			} %>
			<td>总数</td>
		</tr>
		<tr>
			<%for(ProvideSiteRating sitepo: machineMap.values()) {
				%>
				<td><%=Utlitites.fromatLong(sitepo.getCallAllNum() + "")%></td>
				<%
			} %>
			<td><%=Utlitites.fromatLong(totalNumber + "")%></td>			
		</tr>
	</table>
	<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
		<tr>
			<td class="firstTitleTd"><%=urlName%></td>
			<td class="firstTitleTd">调用总计</td>
			<td class="firstTitleTd">IP</td>
			<td class="firstTitleTd">机房</td>
		</tr>
		<%for(UrlOriginDetail m: urlList){
			long callNumber = 0;
			String url = "";
			if(isOrigin) {
				callNumber = m.getReqeustUrlNum();				
				url = m.getRequestUrl();				
			} else {
				callNumber = m.getOriginUrlNum();
				url = m.getOriginUrl();
			}
			%>
		<tr>
			<td align="left">&nbsp;<%=url%></td>
			<td><%=Utlitites.fromatLong(callNumber + "")%>&nbsp;
			<%=Utlitites.scale(callNumber+"", callNumber+"")%>
			</td>
			<td><%=m.getAppHostIp()%>
			</td>
			<td><%=m.getAppSiteName()%></td>
		</tr>
		<%} %>
	</table>
</body>
</html>