<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-添加报表模板</title>
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
  <td width="20%">报表名称：</td>
  <td><input type="text" id="reportName" name="reportName" value="${report.reportName}" onblur="checkReportName();"/>
  <span id="reportNameError"><font color="blue">如：日报</font></span>
  </td>
 </tr>
 <tr><td>报表类型：</td>
  <td>
    <select id="reportType" name=reportType>
      <option value="1" <c:if test="${report.reportType == 1}">selected="true"</c:if>>区分应用</option>
      <option value="0" <c:if test="${report.reportType == 0}">selected="true"</c:if>>用户自定义</option>
    </select>
  </td>
 </tr>
 <tr><td>报表接受类型：</td>
  <td>
    <select id="type" name="type">
      <option value="email">邮件</option>
    </select>
  </td>
 </tr>
 <tr>
  <td>报表地址：</td>
  <td>
    <textarea rows="4" cols="28" id="path" name="path" onblur="checkUrl(this.value);">${report.path}</textarea>
    <span id="pathError"><font color="blue">页面url地址</font></span>
  </td>
 </tr>
 <tr>
  <td>报表发送时间：</td>
  <td>
    <input type="text" id="quartzCron" name="quartzCron" value="${report.quartzCron}"/>
    <span id="quartzCronError"><font color="blue">请填写Quartz的Cron任务调度表达式</font></span>
  </td>
 </tr>
</table>
<div align="center" style="padding-top:15px;">
  <input type="button" value="保 &nbsp;&nbsp;存" onclick="subForm();" style="width:70px;height:30px;" class="macButton"/>
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
		$("#quartzCronError").html("报表发送时间不能为空");
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
		$("#reportNameError").html("报表名称不能为空");
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
		$("#pathError").html("url地址不能为空");
		$("#pathError").css("color", "red");
		return false;
	}else{
		$("#pathError").html("");
		return true;
	}
	/*
	if(!IsURL(url)){
		$("#pathError").text("请输入合法的url地址");
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
+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
+ "(([0-9]{1,3}.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
+ "|" // 允许IP和DOMAIN（域名）
+ "([0-9a-z_!~*'()-]+.)*" // 域名- www.
+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]." // 二级域名
+ "[a-z]{2,6})" // first level domain- .com or .museum
+ "(:[0-9]{1,4})?" // 端口- :80
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