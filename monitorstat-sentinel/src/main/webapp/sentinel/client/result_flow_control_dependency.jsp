<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>依赖限流拦截状态</title>
</head>
<body style="padding-top:5px" class="span15">
<form>
<table class="left_bordered-table"  id="mytable">
    <tr>
    	<td colspan="4"><font size="5">${self}&nbsp;依赖限流拦截状态</td>
    </tr>
    <tr>
    	<th style="width:10%">序号</th>
    	<th style="width:15%">依赖应用</th>
    	<th style="width:60%">接口信息</th>
    	<th style="width:15%">拦截次数</th>
    </tr>
	<c:forEach items="${ips}" var="entry1" varStatus="status1">
	<c:forEach items="${entry1.value}" var="entry2" varStatus="status2">
      <tr>
      	<td align="center">${status2.index + 1}</td>
      	<td align="center">${entry1.key}</td>
      	<td align="center" style= "word-break:break-all">${entry2.key}</td>
      	<td align="center">${entry2.value}</td>
	  </tr>
	 </c:forEach>
	 </c:forEach>
</table>

<script type="text/javascript">

setInterval( "window.location.reload() ",15000);

</script>

</body>
</html>