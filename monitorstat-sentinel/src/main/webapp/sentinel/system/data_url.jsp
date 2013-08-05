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
<title>应用数据url</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:15%">应用数据url</th>
      	<th style="width:15%">应用名</th>
        <th style="width:60%">数据url地址</th>
        <th style="width:20%">操作</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${list}" var="o">
      <tr>
      	<td align="center">${o.number}</td>
      	<td align="center">${o.appName}</td>
      	<td align="center">${o.dataUrl}</td>
      	<td>
            <a href="#" onclick='alter("${o.appName}")'>修改 &nbsp;</a>
        </td>
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">

function alter(appName) {
	var dataUrl = window.prompt("请输入新的数据url地址");
	if (dataUrl == null) {
		return;
	}
	
	var urlDest = "./manage.do?method=alterDataUrl";
	var parameters = "dataUrl=" + dataUrl + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="success") {
    			window.location.reload();
    		} else {
    			alert("没有该应用的权限");
    		}	
    	}
   	});
}

function test(id) {
	alert("Test");
}

</script>
</body>
</html>