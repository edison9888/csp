<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppExceptionListPo"%>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet">
<title>依赖模块</title>
<%
	List<AppExceptionListPo> appConsumerExceptionList= (ArrayList<AppExceptionListPo>)request.getAttribute("appConsumerExceptionList");
	String opsName = (String) request.getAttribute("opsName");
	String customerName = (String) request.getAttribute("customerName");
	String selectDate = (String) request.getAttribute("selectDate");
%>
</head>
<body>
	<table  width="100%">
		<tr>
			<td><h3><%=opsName%>应用调用<%=customerName%>应用时，异常情况(收集时间：<%=selectDate%>)</h3></td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="table table-striped table-bordered table-condensed">
					<tr>
						<td width="200" align="center">接口名称</td>
						<td align="center">异常总数</td>
					</tr>
					<% 
				  	for(AppExceptionListPo excepInfo: appConsumerExceptionList){		
				  	%>
						<tr>
							<td><%=excepInfo.getKeyname()%></td>
							<td align="center"><%=Utlitites.fromatLong(excepInfo.getAllnum() + "") %></td>
						<tr>
					<% } %>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>