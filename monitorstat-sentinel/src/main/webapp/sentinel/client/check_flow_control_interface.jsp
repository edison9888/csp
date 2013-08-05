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
<title>生效接口限流</title>
</head>
<body style="padding-top:5px" class="span15">
<form>
<table class="left_bordered-table"  id="mytable">
    <tr>
    	<td colspan="3"><font size="5">${self}&nbsp;生效接口限流</td></font>
    </tr>
    <tr>
    	<th>序号</th>
    	<th>限流应用</th>
    	<th>线程数</th>
    	<!--<th>操作</th>-->
    </tr>
	<c:forEach items="${ips}" var="entry" varStatus="status">
      <tr>
      	<td align="center">${status.index + 1}</td>
      	<td align="center">${entry.key}</td>
      	<td><input type="text" id="thread${entry.key}" name="thread${entry.key}" value="${entry.value}"/></td>

	  </tr>
	 </c:forEach>
</table>

<script type="text/javascript">


</script>

</body>
</html>