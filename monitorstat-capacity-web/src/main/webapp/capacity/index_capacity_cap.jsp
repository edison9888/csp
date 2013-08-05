<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<title>���������ֶ�ά��</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>
 
<div class="span20">

<table class="condensed-table">
 <tr>
 <td><input type="button" value=" ��  ��  " onclick="add()"/>
 </td>
 </tr>
 </table>

<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
        <th style="width:20%" align="center">Ӧ����</td>
        <th style="width:20%" align="center">��������</td>
        <th style="width:20%" align="center">����ʱ��</td>
        <th style="width:20%" align="center">������</td>
        <th style="width:20%" align="center">����</td>
      </tr>
      </thead>
      <tbody>
      
       <c:forEach items="${capList}" var="o">
      <tr>
      	<td align="center">${o.appName}</td>
      	<td align="center">${o.singleCapacity}</td>
      	<td align="center">${o.time}</td>
      	<td align="center">${o.user}</td>
      	<td>
            <a href="#" onclick='alter("${o.appName}")'>�޸� &nbsp;</a>
            <a href="#" onclick='deleteSingleCapacity("${o.appName}")'>ɾ�� &nbsp;</a> 
        </td>
	  </tr>
	 </c:forEach>
	 
 </tbody>

</table>
</div>

<script type="text/javascript">

function add() {
	window.open ('manage.do?method=showAddCapacityPage', 'newwindow', 'height=300, width=900, top=150, left=150');
}

function alter(appName) {
	var singleCapacity = window.prompt("������Ӧ�õĵ�������ֵ");
	if (singleCapacity == null) {
		return;
	}
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(singleCapacity)) {
		alert("��������ֵ����Ϊ����");
		return;
	}
	var urlDest = "./manage.do?method=alterCapacityCap";
	var parameters = "appName=" + appName + "&singleCapacity=" + singleCapacity;
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

function deleteSingleCapacity(appName) {
	if (!confirm("ȷ��ɾ��? ")) {
		return;
	}
	var urlDest = "./manage.do?method=deleteSingleCapacity";
    var parameters = "appName=" + appName;
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

function test() {
	alert("Test");
}

</script>

</body>
</html>