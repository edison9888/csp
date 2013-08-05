<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-��ӱ���ģ��</title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<style>
td{
  height:34px;
}
</style>
<script type="text/javascript">
if("${operate}"=="add" || "${operate}"=="update"){
 window.parent.location.reload(true);
}
</script>
</head>
<body style="top:0px;">
<c:choose>
<c:when test="${report.qcUpdate=='update'}">
<form id="addReportForm" name="addReportForm" action="updateReport.do" method="post">
<input type="hidden" id="reportId" name="reportId" value="${report.reportId}"/>
</c:when>
<c:otherwise>
<form id="addReportForm" name="addReportForm" action="addReport.do" method="post">
</c:otherwise>
</c:choose>
<table style="width:100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#4F81BD">
 <tr>
  <td width="20%">�������ƣ�</td>
  <td><input type="text" id="reportName" name="reportName" value="${report.reportName}" onblur="checkReportName();"/>
  <span id="reportNameError"><font color="blue">�磺�ձ�</font></span>
  </td>
 </tr>
 <tr><td>�������ͣ�</td>
  <td>
    <select id="reportType" name=reportType>
      <option value="1" <c:if test="${report.reportType == 1}">selected="true"</c:if>>����Ӧ��</option>
      <option value="0" <c:if test="${report.reportType == 0}">selected="true"</c:if>>�û��Զ���</option>
    </select>
  </td>
 </tr>
 <tr><td>����������ͣ�</td>
  <td>
    <select id="type" name="type">
      <option value="email">�ʼ�</option>
    </select>
  </td>
 </tr>
 <tr>
  <td>�����ַ��</td>
  <td>
    <textarea rows="4" cols="28" id="path" name="path" onblur="checkUrl(this.value);">${report.path}</textarea>
    <span id="pathError"><font color="blue">ҳ��url��ַ</font></span>
  </td>
 </tr>
 <tr>
  <td>������ʱ�䣺</td>
  <td>
    <input type="text" id="quartzCron" name="quartzCron" value="${report.quartzCron}"/>
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
	if(checkReportName() && checkUrl() && checkQuartzCron()){
		document.addReportForm.submit();	
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
function checkReportName(){
	var reportName = $("#reportName").val();
	if(reportName == ''){
		$("#reportNameError").html("�������Ʋ���Ϊ��");
		$("#reportNameError").css("color", "red");
		return false;
	}else{
		$("#reportNameError").html("");
		return true;
	}
}
function checkUrl(){
	var url = $("#path").val();
	if(url == ''){
		$("#pathError").html("url��ַ����Ϊ��");
		$("#pathError").css("color", "red");
		return false;
	}else{
		$("#pathError").html("");
		return true;
	}
	/*
	if(!IsURL(url)){
		$("#pathError").text("������Ϸ���url��ַ");
		$("#pathError").css("color", "red");
		return false;
	}else{
		$("#pathError").text("");
		return true;
	}
	*/
}
function IsURL(str_url){
var reportType = $("#reportType").val();
if(reportType == 0){
	var index = str_url.indexOf("?");
	if(index != -1){
		str_url = str_url.substring(0,index);
	}
}
var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp��user@
+ "(([0-9]{1,3}.){3}[0-9]{1,3}" // IP��ʽ��URL- 199.194.52.184
+ "|" // ����IP��DOMAIN��������
+ "([0-9a-z_!~*'()-]+.)*" // ����- www.
+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]." // ��������
+ "[a-z]{2,6})" // first level domain- .com or .museum
+ "(:[0-9]{1,4})?" // �˿�- :80
+ "((/?)|" // a slash isn't required if there is no file name
+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
var re=new RegExp(strRegex);
if (re.test(str_url)){
return (true);
}else{
return (false);
}
}
</script>
</body>
</html>