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
<title>应用限流查询</title>
</head>
<body style="padding-top:40px" class="span20" onload="fillSelect()">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 <td><strong>应用名</strong>&nbsp;&nbsp;<select name="appNames"  id="appNames"></select>&nbsp;&nbsp;<input type="button" value=" 查询ip  " onclick="viewIps()"/>
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
      		<a href="#" onclick='efficientConfig("${o.ip}", "${appName}")'>生效配置 &nbsp;&nbsp;</a>
            <a href="#" onclick='result("${o.ip}", "${appName}")'>拦截状况 &nbsp;&nbsp;</a>
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
		
		for (i=0; i<appArray.length; i++) {
			var appName = appArray[i];
			document.getElementById("appNames").options[i]=new Option(appName,appName);
			if (defaultApp == appName) {
				document.getElementById("appNames").options[i].selected=true;
			}
		}
}

function viewIps() {
	var selectedIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedIndex].value;
	var url = "info.do?method=ipsFlowControlApp&appName=" + appName + "&strategy=${strategy}";
	window.open(url,"_self");
}
	
function efficientConfig(ip, appName) {
 	var urlAddr = "configFeedback.do?method=checkFlowControlApp&ip=" + ip + "&appName=" + appName + "&strategy=${strategy}";
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}

function result(ip, appName) {
	var urlAddr = "resultFeedback.do?method=resultFlowControlApp&ip=" + ip + "&appName=" + appName + "&strategy=${strategy}";
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}


function test(id) {
	alert("Test");
}

</script>
</body>
</html>