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
<title>自定义白名单配置</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>
 
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
      	<th style="width:10%">自定义白名单</th>
      	<th style="width:10%">应用名</th>
        <th style="width:45%">自定义关键字</th>
        <th style="width:10%">白名单应用</th>
        <th style="width:5%">状况</th>
        <th style="width:7%">过时标志</th>
        <th style="width:13%">操作</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${whiteList}" var="o">
      <tr>
      	<td align="center">${o.number}</td>
      	<td align="center">${o.appName}</td>
      	<td align="center" style= "word-break:break-all">${o.customerInfo}</td>
      	<td align="center">${o.refAppName}</td>
      	<td align="center">${o.stateInfo}</td>
      	<td align="center">${o.outOfDate}</td>
      	<td>
      		<a href="#" onclick='viewIps("${o.id}", "${o.appName}", "${o.customerInfo}", "${o.refAppName}")'>查看ip &nbsp;</a>
            <a href="#" onclick='changeState("${o.id}", "${o.opositeStateInfo}", "${o.appName}")'>${o.opositeStateInfo}&nbsp;&nbsp;</a>
            <a href="#" onclick='deleteConfig("${o.id}", "${o.appName}")'>删除 &nbsp;</a>
        </td>
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">

function add() {
	window.open ('manage.do?method=goToAddWhiteListCustomer', 'newwindow', 'height=600, width=900, top=150, left=150')
}

function viewIps(refId, appName, customerInfo, refApp) {
	var urlAddr = "show.do?method=showWhiteListCustomerIps&refId=" + refId + "&appName=" + appName + "&customerInfo=" + customerInfo + "&refApp=" + refApp;
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
	
}

function changeState(id, operator, appName) {
	var confirmMessage = "确定" + operator + "?";
	if (!confirm(confirmMessage)) {
		return;
	}
	
	var urlDest = "./manage.do?method=changeState"
    var parameters = "id=" + id + "&type=WHITE_LIST_CUSTOMER" + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:true,
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

function deleteConfig(id, appName) {
	if (!confirm("确定删除? ")) {
		return;
	}
	var urlDest = "./manage.do?method=deleteWhiteListCustomerConfig";
    var parameters = "id=" + id + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="true") {
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