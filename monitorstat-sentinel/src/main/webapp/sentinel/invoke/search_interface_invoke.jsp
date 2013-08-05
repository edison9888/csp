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
<title>接口调用申请</title>
</head>
<body style="padding-top:40px" class="span20" onload="fillSelect()">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 	<td><strong>应用名</strong>&nbsp;&nbsp;<select name="appNames"  id="appNames"></select>&nbsp;&nbsp;
 	<strong>被调用应用名</strong>&nbsp;&nbsp;<select name="refAppNames"  id="refAppNames"></select>&nbsp;&nbsp;<input type="button" value=" 查  询 " onclick="search()"/>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value=" 申请接口调用  " onclick="add()"/>
 	</td>
 </tr>
 </table>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:5%">编号</th>
      	<th style="width:10%">应用</th>
        <th style="width:10%">依赖应用</th>
        <th style="width:45%">接口信息</th>
        <th style="width:10%">QPS预估</th>
        <th style="width:8%">依赖强度</th>
        <th style="width:12%">操作</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${list}" var="o">
      <tr>
      	<td align="center">${o.number}</td>
      	<td align="center">${o.appName}</td>
      	<td align="center">${o.refApp}</td>
      	<td align="center">${o.interfaceInfo}</td>
      	<td align="center">${o.estimateQps}</td>
      	<td align="center">${o.strong}</td>
      	<td>
      		<a href="#" onclick='viewInvoke("${o.id}")'>查看&nbsp;&nbsp;</a>
            <a href="#" onclick='updateInvoke("${o.id}")'>修改&nbsp;&nbsp;</a>
            <a href="#" onclick='deleteInvoke("${o.id}")'>删除 &nbsp;&nbsp;</a>
        </td>
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">

function fillSelect(){
		var defaultApp = "${appName}";
		var appString = "${apps}";
		var defaultRef = "${refApp}";
		var refAppString = "${refApps}";
		var appArray = appString.split(",");
		var refAppArray = refAppString.split(",");
		
		for (i=0; i<appArray.length; i++) {
			var appName = appArray[i];
			document.getElementById("appNames").options[i]=new Option(appName,appName);
			if (defaultApp == appName) {
				document.getElementById("appNames").options[i].selected=true;
			}
		}
		
		for (i=0; i<refAppArray.length; i++) {
			var refApp = refAppArray[i];
			document.getElementById("refAppNames").options[i]=new Option(refApp,refApp);
			if (defaultRef == refApp) {
				document.getElementById("refAppNames").options[i].selected=true;
			}
		}
}

function add() {
	window.open ('invokeManage.do?method=goToAddIntefaceInvoke', 'newwindow', 'height=600, width=900, top=150, left=150');
}

function viewInvoke(id) {
	var urlAddr = "invokeShow.do?method=viewInterfaceInvoke&id=" + id;
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
	
}

function updateInvoke(id) {
	var urlDest = "./invokeManage.do?method=gotoUpdateInterfaceInvoke&id=" + id;
	window.open (urlDest, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}

function deleteInvoke(id) {
	if (!confirm("确定删除? ")) {
		return;
	}
	var urlDest = "./invokeManage.do?method=deleteInterfaceInvoke";
    var parameters = "id=" + id;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		window.location.reload();
    	}
   	});
}

function search() {
	var selectedIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedIndex].value;
	var refSelectedIndex = document.getElementById("refAppNames").selectedIndex;
	var refApp = document.getElementById("refAppNames").options[refSelectedIndex].value;
	var urlDest = "./invokeShow.do?method=searchInterfaceInvoke&appName=" + appName + "&refApp=" + refApp;
	window.open(urlDest, '_self');

}

function test(id) {
	alert("Test");
}

</script>
</body>
</html>