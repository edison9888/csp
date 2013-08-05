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
<title>配置推送</title>
</head>
<body style="padding-top:40px" class="span20" onload="fillStatus()">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 	<tr>
 		<td><strong>应用名</strong>&nbsp;&nbsp;<select name="appNames"  id="appNames"></select>&nbsp;&nbsp;
 			<input type="button" value=" 更新ip " onclick="updateIps()"/> &nbsp;&nbsp;&nbsp;
 			<input type="button" value=" 预览配置 " onclick="viewConfig()"/> &nbsp;&nbsp;&nbsp;
 			<input type="button" value=" 推送配置 " onclick="pushConfig()"/>
 		</td>
 	</tr>
 	
 	<tr>
		<td>
			<textarea id="configInfo" name="configInfo" rows="35" class="span19" readonly>
	
			</textarea>
		</td>
	</tr>
 </table>

</div>
<script type="text/javascript">

function fillStatus(){
		var defaultApp = "${appName}";
		var appString = "${apps}";
		var configInfo = '${configInfo}';
		var appArray = appString.split(",");
		
		for (i=0; i<appArray.length; i++) {
			var appName = appArray[i];
			document.getElementById("appNames").options[i]=new Option(appName,appName);
			if (defaultApp == appName) {
				document.getElementById("appNames").options[i].selected=true;
			}
		}
		
		if (configInfo == null || configInfo == "") {
		
		} else {
			$("#configInfo").val(configInfo);
		}
}

function updateIps() {
	var selectedIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedIndex].value;
	var urlDest = "./manage.do?method=updateIps"
    var parameters = "appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:true,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data == "0") {
				alert("关联应用没有任何ip被更新");
			} else if (data == "-1") {
				alert("没有该应用的权限.");
			} else {
				alert(data + "存在ip更新");
			}
    	}
   	});
}

function viewConfig() {
	var selectedIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedIndex].value;
	
	if (appName == null || appName == "") {
		alert("没有选择应用");
		return;
	}
	
	var urlDest = "./manage.do?method=goToPushConfig&appName=" + appName;
	window.open(urlDest, '_self');
}

function pushConfig() {
	
	var selectedIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedIndex].value;
	
	if (appName == null || appName == "") {
		alert("没有选择应用");
		return;
	}
	
	if (!confirm("确定推送配置? ")) {
		return;
	}
	
	var urlDest = "./manage.do?method=pushConfig";
    var parameters = "appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if(data == "true") {
    			alert("推送成功");
    		} else {
    			alert("没有权限或者其它原因导致推送失败");
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