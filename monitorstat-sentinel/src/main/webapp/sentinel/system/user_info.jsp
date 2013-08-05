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
<title>用户权限信息</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 <td><input type="button" value=" 新  增  " onclick="add()"/>
 </td>
 </tr>
 </table>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:10%">用户权限</th>
      	<th style="width:30%">旺旺名</th>
        <th style="width:30%">邮箱</th>
        <th style="width:30%">应用</th>
        <!--<th style="width:20%">操作</th>-->
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${list}" var="o">
      <tr>
      	<td align="center">${o.number}</td>
      	<td align="center">${o.wangwang}</td>
      	<td align="center">${o.mail}</td>
      	<td align="center">${o.appName}</td>
      	<!--
      	<td>
            <a href="#" onclick='deleteConfig("${o.id}", "${o.appName}")'>删除 &nbsp;&nbsp;</a>
        </td>
        -->
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">

function add() {
	window.open ('manage.do?method=goToAddUserPermission', 'newwindow', 'height=600, width=900, top=150, left=150')
}

function test(id) {
	alert("Test");
}

</script>
</body>
</html>