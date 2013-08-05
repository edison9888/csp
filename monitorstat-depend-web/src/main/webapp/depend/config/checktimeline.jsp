<%@ page language="java" contentType="text/html; charset=GB18030" isELIgnored="false" pageEncoding="GB18030"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.depend.po.CheckupDependConfig"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>检测进度</title>
</head>
<body>
<%@ include file="../dailyheader.jsp"%>
<%
List<CheckupDependConfig> list = (List<CheckupDependConfig>)request.getAttribute("list");
if(list == null) {
	out.println("当前没有正在检测的项目!");
} else {
%>
	<table class="table table-striped table-bordered table-condensed"
		align="center">
		<tr>
			<td>序号</td>
			<td>应用</td>
			<td>依赖应用</td>
			<td>自动|手动</td>
			<td>阻隔方式</td>
			<td>延迟方式</td>
			<td>检查方式</td>
			<td>添加时间</td>
			<td>当前状态</td>
		</tr>
<%
	int iIndex = 0;
	for(CheckupDependConfig configPo: list) {
		iIndex ++;
		if(configPo == null)
			continue;
%>
		<tr>
			<td><%=iIndex%></td>
			<td><%=configPo.getOpsName()%></td>
			<td><%=configPo.getTargetOpsName()%></td>
			<td>
			<%
				if("no".equals(configPo.getAutoRun())) {
					out.println("手动");
				} else {
					out.println("自动");
				}
			%>
			</td>
			<td><%=configPo.getPreventType()%></td>
			<td><%=configPo.getDelayType()%></td>
			<td><%=configPo.getCheckupType()%></td>
			<td><%=configPo.getAddTime()%></td>
			<td>
			<%
				if("doing".equals(configPo.getStatus())) {
					out.println("<span style='color: red;'>执行中</span>");
				} else {
					out.println("等待中");
				}
			%>
			</td>
		</tr>	
<%}%>
	</table>
	<%
}
%>
</body>
</html>