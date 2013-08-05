<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.time.custom.arkclient.ArkDomain" %> 
<%@page import="com.taobao.monitor.common.po.CspUserInfoPo" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>实时监控系统</title>
<%@ include file="/time/common/base.jsp"%>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
<script type="text/javascript">

$(document).ready(function(){

	$("#phone").blur(function(){
		var phonePattern =/^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/; 
		var phone = $("#phone").val();
		if(phone == "") {
			$("#note-phone").text("不允许为空");
			$("#note-phone").css("visibility","visible");							
		}
		else if(phonePattern.test(phone) == false) {
			$("#note-phone").text("电话号码格式不对");
			$("#note-phone").css("visibility","visible");	
		}else{
			$("#note-phone").text("OK");
		}
	});

	$("#mail").blur(function(){
		var mail = $("#mail").val();
		var emailPattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		if(mail == "") {
			$("#note-mail").text("不允许为空");
			$("#note-mail").css("visibility","visible");							
		}
		else if(emailPattern.test(mail)== false) {
			$("#note-mail").text("email格式不对");
			$("#note-mail").css("visibility","visible");	
		}else{
			$("#note-mail").text("OK");
		}
	});
});

function CheckForm()
{  
if ($("#name").val().length  ==  0)  {  
	alert("你把用户名给忘填了!");
	$("#name").focus();
return  false;
} else if($("#wangwang").val().length == 0) {
	alert("你把旺旺给忘填了!");
	$("#wangwang").focus();
	return false;
} else if($("#phone").val().length == 0) {
	alert("你把电话给忘填了!");
	$("#phone").focus();
} else if($("#mail").val().length == 0) {
	alert("你把邮箱给忘填了!");
	$("#mail").focus();
}
return  true;
}

</script>
</head>
<body>
<%
String userName = request.getParameter("userName");
if(userName != null){
	request.setAttribute("userName", userName);
}
%>
<%@include file="/header.jsp" %>
<div class="container-fluid">
<div class="span5 offset4">
<form action="register.do?method=register" method="post" onsubmit="return CheckForm()">
<table class="table table-bordered">
	<tr>
		<td>用户名:<input id="name" type="text" name="name" class="register-input" value="${userName }" readonly="readonly"></td>
		<td style="width:120px;"><font color="red"><div id="note-name">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>旺旺号:<input type="text" id="wangwang" name="wangwang" class="register-input" value="${po.wangwang }"></td>
		<td style="width:120px;"><font color="red"><div id="note-wangwang">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>手机号:<input type="text" id="phone" name="phone" class="register-input" value="${po.phone }"></td>
		<td style="width:120px;"><font color="red"><div id="note-phone">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>邮箱:&nbsp;&nbsp;&nbsp;<input type="text" id="mail" name="mail" class="register-input" value="${po.mail }"></td>
		<td style="width:120px;"><font color="red"><div id="note-mail">&nbsp;</div></font></td>
	</tr>
</table>
<div style="text-align: center">
<input type="submit" style="align:center;" value="提交" />	
</div>
</form>
</div>
</div>
</body>
</html>