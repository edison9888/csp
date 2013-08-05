<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.util.JspFormatUtil"%>
<%@page import="com.taobao.monitor.common.po.CspMapKeyInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ReleaseInfo"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<style>
<!--
body {
	padding-bottom: 40px;
	padding-top: 60px;
}
-->
</style>
<%%>
<title>依赖地图-发布信息页面</title>
<%
String appName = (String)request.getAttribute("appName");
List<ReleaseInfo> list = (List<ReleaseInfo>)request.getAttribute("list");
%>
</head>
<body>
<h2>
${beginDate}至${endDate},${opsName}的发布信息：
</h2>
<table class="table table-bordered table-striped">
<thead>
	<tr>
		<td>发布单id</td>
		<td>应用名</td>
		<td>发布计划时间</td>
		<td>发布类型</td>
		<td>发布种类</td>
		<td>发布级别</td>
		<td>调用系统</td>
		<td>通知类型</td>
		<td>结束时间</td>
		<td>发布结果</td>
	</tr>
</thead>
<tbody>	
<%
	if(list == null) {
		out.println("查询无数据！");
	} else {
		for(ReleaseInfo po : list) {
			%>
			<tr>
			<td><%=po.getPlanId()%></td>
			<td><%=po.getAppName()%></td>
			<td><%=po.getPlanTime()%></td>
			<td><%=po.getPubType()%></td>
<%
			if(po.getPlanKind().equals("normal"))
				out.println("<td>发布</td>");
			else
				out.println("<td>回滚</td>");

			if(po.getPubLevel().equals("prepub"))
				out.println("<td>预发</td>");
			else if(po.getPubLevel().equals("beta"))
				out.println("<td>beta发布</td>");
			else
				out.println("<td>正式发布</td>");
%>			
			<td><%=po.getCallSystem()%></td>
<%
			if(po.getNotifyType().equals("start"))
				out.println("<td>发布开始</td>");
			else
				out.println("<td>发布结束</td>");

			if(po.getFinishTime() == null)
				out.println("<td>-</td>");
			else
				out.println("<td>" + po.getFinishTime() + "</td>");
			
			if(po.getResult() != null) {
				if(po.getResult().equals("SUC")) 
					out.println("<td>发布成功</td>");
				else
					out.println("<td><strong style='color: red;'>发布失败</strong></td>");						
			} else {
				out.println("<td>发布中...</td>");
			}
%>				
			</tr>
			<%	
		}
	}
%>
</tbody>
</table>
</body>
</html>