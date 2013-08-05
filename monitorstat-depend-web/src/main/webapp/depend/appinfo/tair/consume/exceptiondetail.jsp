<%@page import="com.taobao.csp.depend.po.tair.CExceptionUnit"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
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
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>调用tair的应用的机器分布</title>
<%
List<CExceptionUnit> exceptionList = ( List<CExceptionUnit>)request.getAttribute("exceptionList");
String bigTotal = (String)request.getAttribute("bigTotal");
String preBigTotal = (String)request.getAttribute("preBigTotal");
Map<String, Long> siteMap = (Map<String, Long>)request.getAttribute("siteMap");
Map<String, Long> preSiteMap = (Map<String, Long>)request.getAttribute("preSiteMap");
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
<H4 align="center">应用${opsName}调用Tair异常情况统计</H4>
<H2 align="left">查询时间：${selectDate}</H2>
<H2 align="left">对比时间：${selectPreDate}</H2>
<H2 align="left">总异常数:<%=bigTotal%><%=Utlitites.scale(bigTotal, preBigTotal)%></H2>
	<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
		<thead>
			<tr>
				<td class="firstTitleTd">
					异常类型
				</td>
				<%
				for(String key: siteMap.keySet()){
				%>
				<td class="firstTitleTd">
					<%=key%>（<%=Utlitites.fromatLong(siteMap.get(key) + "")%>
					<%=Utlitites.scale(siteMap.get(key) + "", preSiteMap.get(key) + "")%>，
					<%=Arith.mul(Arith.div(siteMap.get(key), Double.parseDouble(bigTotal), 4), 100)%> %）
				</td>
				<% 
				}	//end of for
				%>
				<td>
				合计
				</td>
			</tr>
		</thead>
		<%
			for(CExceptionUnit unit: exceptionList) {
		%>
			<tr>
				<td><%=unit.getExceptionName()%></td>
				<%
				for(String key: siteMap.keySet()){
				%>
				<td>
					<%
						if(unit.machineData.containsKey(key)) {
						%>
							<%=unit.machineData.get(key)%>
							<%=Utlitites.scale(unit.machineData.get(key) + "", unit.preMachineData.get(key) + "")%>
						<%
						} else {
						%>
							-
						<%		
						}
					%>
				</td>
				<% 
				}	//end of for
				%>
				<td>
					<%=unit.getTotal()%>
					<%=Utlitites.scale(unit.getTotal() + "", unit.getPreTotal() + "")%>
				</td>
			</tr>
		<%
			}
		%>
	</table>	
</body>
</html>