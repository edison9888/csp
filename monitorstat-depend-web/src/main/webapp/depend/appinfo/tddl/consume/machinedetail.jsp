<%@page import="com.taobao.csp.depend.po.tddl.ConsumeDbDetail"%>
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
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>查看Tddl详细</title>
<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

String totalNum = (String)request.getAttribute("totalNum");		
String sqlSuccessNum = (String)request.getAttribute("sqlSuccessNum");
String timeOutNum = (String)request.getAttribute("timeOutNum");
ArrayList<ConsumeDbDetail> detailList = (ArrayList<ConsumeDbDetail>)request.getAttribute("detailList");
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
<H4 align="center">应用<%=opsName%>Tddl调用详细</H4>
<H2 align="left">Sql操作：${sqlText}</H2>
<H2 align="left">执行状态：${executeType}</H2>
<H2 align="left">查询时间：${selectDate}</H2>
<H2 align="left">对比时间：${selectPreDate}</H2>
<H2 align="left">总调用量：${totalNum}</H2>
<H2 align="left">超时总调用量：${timeOutNum}</H2>
	<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
		<tr>
			<td class="firstTitleTd">IP</td>
			<td class="firstTitleTd">机房</td>
			<td class="firstTitleTd">调用总计</td>
		</tr>
		<%for(ConsumeDbDetail m: detailList){
			long callNumber = m.getExecuteSum();	//当前数量
			%>
		<tr>
			<td align="center"><%=m.getAppHostIp()%></td>
			<td><%=m.getAppSiteName()%></td>
			<td><%=Utlitites.fromatLong(callNumber + "")%>&nbsp;
			<%=Utlitites.scale(callNumber+"", callNumber+"")%>
			</td>
		</tr>
		<%} %>
	</table>
</body>
</html>