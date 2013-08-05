<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<title>异常信息</title>
</head>
<body>
<%
List<TimeDataInfo> list  = (List<TimeDataInfo>)request.getAttribute("exceptionList");

if(list==null||list.size()==0){
	out.print("最近十分钟没有出现异常信息");
	return;
}

if(list != null){
	for(TimeDataInfo td:list){
		Object tmp = td.getOriginalPropertyMap().get("desc");
		if(tmp == null){
			continue;
		}
		String msg = tmp.toString().replaceAll("\n", "<br/>");
%>
	<div class="container-fluid">
		<%=msg %>
	</div>
<%} }%>
</body>
</html>