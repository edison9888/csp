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
<title>接口限流查询</title>
</head>
<body style="padding-top:40px" class="span20" onload="fillSelect()">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 <td><strong>应用名</strong>&nbsp;&nbsp;<select name="appNames"  id="appNames" onchange="change()"></select>&nbsp;&nbsp;
 <strong>接口信息</strong>&nbsp;&nbsp;<select name="interfaceNames"  id="interfaceNames" class="xxlarge"></select>&nbsp;&nbsp;
 <input type="button" value=" 查询ip  " onclick="viewIps()"/>
 </td>
 </tr>
 </table>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:10%">编号</th>
      	<th style="width:30%">应用名</th>
        <th style="width:30%">应用ip</th>
        <th style="width:30%">操作</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${ips}" var="o">
      <tr>
      	<td align="center">${o.order}</td>
      	<td align="center">${appName}</td>
      	<td align="center">${o.ip}</td>
      	<td>
      		<a href="#" onclick='efficientConfig("${o.ip}", "${interfaceInfo}", "${appName}")'>生效配置 &nbsp;&nbsp;</a>
            <a href="#" onclick='result("${o.ip}", "${interfaceInfo}", "${appName}")'>拦截状况 &nbsp;&nbsp;</a>
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
		var appArray = appString.split(",");
		
		var defaultInterface = "${interfaceInfo}";
		var interfaceString = "${interfaces}";
		var interfaceArray = interfaceString.split("%");
		
		for (i=0; i<appArray.length; i++) {
			var appName = appArray[i];
			document.getElementById("appNames").options[i]=new Option(appName,appName);
			if (defaultApp == appName) {
				document.getElementById("appNames").options[i].selected=true;
			}
		}
		
		for (j=0; j<interfaceArray.length; j++) {
			var interfaceName = interfaceArray[j];
			document.getElementById("interfaceNames").options[j]=new Option(interfaceName,interfaceName);
			if (defaultInterface == interfaceName) {
				document.getElementById("interfaceNames").options[j].selected=true;
			}
		}
		
}

function change() {
	var selectedAppIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedAppIndex].value;
	var urlDest = "info.do?method=flowControlInterface" + "&strategy=${strategy}";
	var parameters = "appName=" + appName;
	$.ajax({
    	url: urlDest,
    	async:true,
    	type: "GET",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		document.getElementById("interfaceNames").options.length=0;
    		var interfaceArray = data.split("%");
    		for (i=0; i<interfaceArray.length; i++) {
    			var interfaceName = interfaceArray[i];
    			document.getElementById("interfaceNames").options[i]=new Option(interfaceName,interfaceName);
    		}
    	}
   	});
}

function viewIps() {
	var selectedAppIndex = document.getElementById("appNames").selectedIndex;
	var selectedInterfaceIndex = document.getElementById("interfaceNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedAppIndex].value;
	var interfaceInfo = document.getElementById("interfaceNames").options[selectedInterfaceIndex].value;
	var url = "info.do?method=ipsFlowControlInterface&appName=" + appName + "&interfaceInfo=" + interfaceInfo + "&strategy=${strategy}";
	window.open(url,"_self");
}
	
function efficientConfig(ip, interfaceInfo, appName) {
 	var urlAddr = "configFeedback.do?method=checkFlowControlInterface&ip=" + ip + "&interfaceInfo=" + interfaceInfo + "&appName=" + appName + "&strategy=${strategy}";
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}

function result(ip, interfaceInfo, appName) {
	var urlAddr = "resultFeedback.do?method=resultFlowControlInterface&ip=" + ip + "&interfaceInfo=" + interfaceInfo+ "&appName=" + appName + "&strategy=${strategy}";
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}


function test(id) {
	alert("Test");
}

</script>
</body>
</html>