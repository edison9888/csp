<%@page import="com.taobao.csp.depend.po.tair.CMachineDistributeUnit"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.util.ConstantParameters"%>
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
List<CMachineDistributeUnit> machineList = ( List<CMachineDistributeUnit>)request.getAttribute("machineList");

String opsName = (String)request.getAttribute("opsName");
String tairgroupname = (String)request.getAttribute("tairgroupname");
String namespace = (String)request.getAttribute("namespace");
String actiontype = (String)request.getAttribute("actiontype");

String avgRate = (String)request.getAttribute("avgRate");
String avgHit = (String)request.getAttribute("avgHit");
String avgLength = (String)request.getAttribute("avgLength");
String totalNumber = (String)request.getAttribute("totalNumber");
Map<String, Long> siteMap = (Map<String, Long>)request.getAttribute("siteMap");

String preAvgrate = (String)request.getAttribute("preAvgrate");
String preAvgHit = (String)request.getAttribute("preAvgHit");
String preAvgLength = (String)request.getAttribute("preAvgLength");
String preTotalNumber = (String)request.getAttribute("preTotalNumber");
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
<H4 align="center">应用<%=opsName%>调用Tair的机器分布统计</H4>
<H2 align="left">tairgroupname：<%=tairgroupname%></H2>
<H2 align="left">namespace：<%=namespace%></H2>
<H2 align="left">actiontype：<%=actiontype%></H2>
<H2 align="left">查询时间：${selectDate}</H2>
<H2 align="left">对比时间：${selectPreDate}</H2>
	<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
		<thead>
			<tr>
				<td colspan="4" class="firstTitleTd">
				<%
				for(String key: siteMap.keySet()){
					//对比暂未添加
					out.print(key+"(" + Utlitites.fromatLong(siteMap.get(key) + "") + " "
					+ Utlitites.scale(siteMap.get(key)+"", preSiteMap.get(key)+"") + "，"
					+Arith.mul(Arith.div(siteMap.get(key),Double.parseDouble(totalNumber),4), 100) + "%"
					+ ")&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				%>
				</td>
			</tr>
		</thead>
		<tr>
			<td class="firstTitleTd">总调用量</td>
			<td class="firstTitleTd">均值（毫秒/次数）</td>
			<td class="firstTitleTd">长度（字节/次数）</td>
			<td class="firstTitleTd">命中率</td>
		</tr>
		<tr>
			<td><%=Utlitites.fromatLong(totalNumber + "")%>&nbsp;<%=Utlitites.scale(totalNumber+"", preTotalNumber+"")%></td>
			<td><%=avgRate%>&nbsp;<%=Utlitites.scale(avgRate+"", preAvgrate+"")%></td>
			<td><%=avgLength%>&nbsp;<%=Utlitites.scale(avgLength+"", preAvgLength+"")%></td>
			<td><%=Arith.mul(Double.parseDouble(avgHit), 100.0)%>%&nbsp;<%=Utlitites.scale(avgHit+"", preAvgHit+"")%></td>
		</tr>	
	</table>
	<table width="100%" class="table table-striped table-bordered table-condensed">
		<tr>
			<td class="firstTitleTd">机器IP</td>
			<td class="firstTitleTd">调用量</td>
			<td class="firstTitleTd">均值（毫秒/次数）</td>
			<td class="firstTitleTd">长度（字节/次数）</td>
			<td class="firstTitleTd">命中率</td>
			<td class="firstTitleTd">机房</td>
		</tr>
		<%for(CMachineDistributeUnit m: machineList){ %>
		<tr>
			<td><%=m.getIp()%></td>
			<td><%=Utlitites.fromatLong(m.callNumberArray[ConstantParameters.INT_TYPE_NOR] + "")%>&nbsp;
			<%=Utlitites.scale(m.callNumberArray[ConstantParameters.INT_TYPE_NOR]+"", m.preCallNumberArray[ConstantParameters.INT_TYPE_NOR]+"")%>
			</td>
			<td><%=m.getAvgRate()%>
			&nbsp;
			<%=Utlitites.scale(m.getAvgRate()+"", m.getPreAvgrate()+"")%>
			</td>
			<td><%=m.getAvgLength()%>&nbsp;
			<%=Utlitites.scale(m.getAvgLength()+"", m.getPreAvgLength()+"")%>
			</td>
			<td><%=Arith.mul(m.getAvgHit(), 100.0)%> %
			&nbsp;
			<%=Utlitites.scale(m.getAvgHit()+"", m.getPreAvgHit()+"")%>
			</td>
			<td><%=m.getSiteName()%></td>
		</tr>
		<%} %>
	</table>
</body>
</html>