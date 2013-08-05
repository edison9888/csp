<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-ά��Selenium Server</title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/ajaxtabs/ajaxtabs.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/ajaxtabs/ajaxtabs.js"></script>
<style>
td{
  height:34px;
}
</style>
<script type="text/javascript">
if("${operate}"=="add"){
 window.parent.location.reload(true);
}
</script>
</head>
<body style="top:0px;">
<form id="seleniumServerForm" name="seleniumServerForm" 
<c:choose><c:when test="${operate=='update'}">action="updateSeleniumServer.do" </c:when>
<c:otherwise>action="addSeleniumServer.do"</c:otherwise>
</c:choose>method="post">
<c:if test="${operate == 'update'}">
<input type="hidden" id="id" name="id" value="${seleniumServer.id}"/>
</c:if>
<table style="width:99%;margin-top:10px;margin-left:auto;margin-right:auto;" border="1" cellspacing="0" cellpadding="0" bordercolor="#4F81BD">
 <tr>
  <td align="right" width="23%">Selenium Server����<font color="red">(*)</font>��</td>
  <td><input type="text" id="seleniumRcName" name="seleniumRcName" value="${seleniumServer.seleniumRcName}" onblur="checkSeleniumRcName();"/>
  <span id="seleniumRcNameError"><font color="blue">�磺*.CM4.*</font></span>
  </td>
 </tr>
 <tr>
  <td align="right">Selenium Server IP<font color="red">(*)</font>��</td>
  <td><input type="text" id="seleniumRcIp" name="seleniumRcIp" value="${seleniumServer.seleniumRcIp}" onblur="checkSeleniumRcIp();"/>
  <span id="seleniumRcIpError"><font color="blue">�磺121.14.24.241</font></span>
  </td>
 </tr>
 <tr>
  <td align="right">Selenium Server Port<font color="red">(*)</font>��</td>
  <td><input type="text" id="seleniumRcPort" name="seleniumRcPort" value="${seleniumServer.seleniumRcPort}" onblur="checkSeleniumRcPort();"/>
  <span id="seleniumRcPortError"><font color="blue">�磺4444</font></span>
  </td>
 </tr>
 <tr>
  <td align="right">����ϵͳ<font color="red">(*)</font>��</td>
  <td>
  <select id="operateSystemType" name="operateSystemType">
   <option value="windows" <c:if test="${operateSystemType == seleniumServer.operateSystemType}">selected="true"</c:if>>windows</option>
   <option value="linux" <c:if test="${operateSystemType == seleniumServer.operateSystemType}">selected="true"</c:if>>linux</option>
  </select>
  <span id="operateSystemTypeError"><font color="blue">�磺linux</font></span>
  </td>
 </tr>
 <tr><td align="right">�����<font color="red">(*)</font>��</td>
  <td style="align:left;valign:center">
     <c:forEach items="${browsers}" var="entity">
       ${entity.value}��<input type="checkbox" <c:if test="${fn:contains(seleniumServer.browsers, entity.key)}">checked="true"</c:if> 
       id="browsers" name="browsers" class="browsers" value="${entity.key}"/>&nbsp;     
     </c:forEach>
     <span id="selectIdError"><font color="blue"></font></span>
  </td>
 </tr>
 <tr>
  <td align="right">��������ʱ��<font color="red">(*)</font>��</td>
  <td>
    <input type="text" id="quartzCron" name="quartzCron" value="${seleniumServer.quartzCron}" onblur="checkQuartzCron();"/>
    <span id="quartzCronError"><font color="blue">����дQuartz��Cron������ȱ��ʽ</font></span>
  </td>
 </tr>
</table>
<div align="center" style="padding-top:15px;">
 <input type="button" value="�� &nbsp;&nbsp;��" onclick="subForm();" style="width:70px;height:30px;" class="macButton"/>
</div>
</form>
<script type="text/javascript">
$("#reportName").focus();
function subForm(){
	if(checkSeleniumRcName() && checkSeleniumRcIp() && checkSeleniumRcPort() && checkSelectId() && checkQuartzCron()){
		document.seleniumServerForm.submit();	
	}
}
/*
function subAddForm(){
	if(checkSeleniumRcName() && checkSeleniumRcIp() && checkSeleniumRcPort() && checkSelectId() && checkQuartzCron()){
		var browserList = [];
		$("input[type=checkbox][name=browsers]").each(function(){
			if($(this).attr("checked") == true)
				browserList.push($(this).val());
	��     })
		parent.$("#seleniumRcName").val($("#seleniumRcName").val());
		parent.$("#seleniumRcIp").val($("#seleniumRcIp").val());
		parent.$("#seleniumRcPort").val($("#seleniumRcPort").val());
		parent.$("#quartzCron").val($("#quartzCron").val());
		parent.$("#operateSystemType").val($("#operateSystemType").val());
		parent.$("#browsers").val(browserList);
		parent.document.addSeleniumServerForm.submit();	
	}
}
*/
function checkSelectId(){
	var flag = false;
	$("input[type=checkbox][name=browsers]").each(function(){
		if($(this).attr("checked") == true){
			flag = true;
		}
	});
	if(!flag){
		$("#selectIdError").html("����ѡ�������");
		$("#selectIdError").css("color", "red");
		return false;
	}else{
		$("#selectIdError").html("");
		return true;
	}
}

function checkQuartzCron(){
	var quartzCron = $("#quartzCron").val();
	if(quartzCron == ''){
		$("#quartzCronError").html("������ʱ�䲻��Ϊ��");
		$("#quartzCronError").css("color", "red");
		return false;
	}else{
		$("#quartzCronError").html("");
		return true;
	}
}
function checkSeleniumRcName(){
	var seleniumRcName = $("#seleniumRcName").val();
	if(seleniumRcName == ''){
		$("#seleniumRcNameError").html("Selenium Server���Ʋ���Ϊ��");
		$("#seleniumRcNameError").css("color", "red");
		return false;
	}else{
		$("#seleniumRcNameError").html("");
		return true;
	}
}
function checkSeleniumRcIp(){
	var seleniumRcIp = $("#seleniumRcIp").val();
	if(seleniumRcIp == ''){
		$("#seleniumRcIpError").html("Selenium Server IP����Ϊ��");
		$("#seleniumRcIpError").css("color", "red");
		return false;
	}else{
		$("#seleniumRcIpError").html("");
		return true;
	}
}
function checkSeleniumRcPort(){
	var seleniumRcPort = $("#seleniumRcPort").val();
	if(seleniumRcPort == ''){
		$("#seleniumRcPortError").html("Selenium Server Port����Ϊ��");
		$("#seleniumRcPortError").css("color", "red");
		return false;
	}else{
		$("#seleniumRcPortError").html("");
		return true;
	}
}
</script>
</body>
</html>