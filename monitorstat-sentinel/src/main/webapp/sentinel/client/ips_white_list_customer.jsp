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
<title>自定义白名单查询</title>
</head>
<body style="padding-top:40px" class="span20" onload="fillSelect()">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 <td><strong>应用名</strong>&nbsp;&nbsp;<select name="appNames"  id="appNames" onchange="change()"></select>&nbsp;&nbsp;
 <strong>白名单关键字</strong>&nbsp;&nbsp;<select name="customerNames"  id="customerNames" class="xlarge"></select>&nbsp;&nbsp;
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
      		<a href="#" onclick='efficientConfig("${o.ip}", "${customerInfo}", "${appName}")'>生效配置 &nbsp;&nbsp;</a>
            <a href="#" onclick='result("${o.ip}", "${customerInfo}", "${appName}")'>拦截状况 &nbsp;&nbsp;</a>
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
		
		var defaultCustomer = "${customerInfo}";
		var customerString = "${customers}";
		var customerArray = customerString.split("%");
		
		for (i=0; i<appArray.length; i++) {
			var appName = appArray[i];
			document.getElementById("appNames").options[i]=new Option(appName,appName);
			if (defaultApp == appName) {
				document.getElementById("appNames").options[i].selected=true;
			}
		}
		
		for (j=0; j<customerArray.length; j++) {
			var customerName = customerArray[j];
			document.getElementById("customerNames").options[j]=new Option(customerName,customerName);
			if (defaultCustomer == customerName) {
				document.getElementById("customerNames").options[j].selected=true;
			}
		}
		
}

function change() {
	var selectedAppIndex = document.getElementById("appNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedAppIndex].value;
	var urlDest = "info.do?method=whiteListCustomer";
	var parameters = "appName=" + appName;
	$.ajax({
    	url: urlDest,
    	async:true,
    	type: "GET",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		document.getElementById("customerNames").options.length=0;
    		var customerArray = data.split("%");
    		for (i=0; i<customerArray.length; i++) {
    			var customerName = customerArray[i];
    			document.getElementById("customerNames").options[i]=new Option(customerName,customerName);
    		}
    	}
   	});
}

function viewIps() {
	var selectedAppIndex = document.getElementById("appNames").selectedIndex;
	var selectedCustomerIndex = document.getElementById("customerNames").selectedIndex;
	var appName = document.getElementById("appNames").options[selectedAppIndex].value;
	var customerInfo = document.getElementById("customerNames").options[selectedCustomerIndex].value;
	var url = "info.do?method=ipsWhiteListCustomer&appName=" + appName + "&customerInfo=" + customerInfo;
	window.open(url,"_self");
}
	
function efficientConfig(ip, customerInfo, appName) {
 	var urlAddr = "configFeedback.do?method=checkWhiteListCustomer&ip=" + ip + "&customerInfo=" + customerInfo + "&appName=" + appName;
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}

function result(ip, customerInfo, appName) {
	var urlAddr = "resultFeedback.do?method=resultWhiteListCustomer&ip=" + ip + "&customerInfo=" + customerInfo + "&appName=" + appName;
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');
}


function test(id) {
	alert("Test");
}

</script>
</body>
</html>