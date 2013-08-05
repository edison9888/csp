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
<title>接口黑名单详细信息</title>
</head>
<body style="padding-top:10px" class="span15">
 
<div class="span15">
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:10%">编号</th>
      	<!--<th style="width:15%">应用名</th>-->
        <!--<th style="width:50%">接口信息</th>-->
        <th style="width:45%">黑名单应用</th>
        <th style="width:45%">机器ip</th>
        <!--<th style="width:10%">操作</th>-->
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${blackListIps}" var="o">
      <tr>
      	<td align="center">${o.number}</td>
      	<!--<td align="center">${appName}</td>-->
      	<!--<td align="center">${interfaceInfo}</td>-->
      	<td align="center">${refApp}</td>
      	<td align="center">${o.ip}</td>
      	<!--
      	<td>
            <a href="./sentinel/manage.do?method=deleteWhiteList&id=${whiteObject.id}">删除 &nbsp;&nbsp;</a>
        </td>
        -->
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">


</script>
</body>
</html>