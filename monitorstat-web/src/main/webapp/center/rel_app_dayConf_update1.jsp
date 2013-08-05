<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.common.ao.center.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-实时模板信息配置</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>

<style type="text/css">

body {
	font-size: 62.5%;
}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}


</style>
</head>
<%
request.setCharacterEncoding("gbk");
String appName = request.getParameter("appName");
String appId = request.getParameter("appId");	//从上一个check页面传递过来
String confId = request.getParameter("confId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_day.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}

function modifyDayConfFunc() {

	$.post("<%=request.getContextPath () %>/center/manage.mc", 
	   	{
			appId: "<%=appId%>",
			confId: "<%=confId %>",
			aliasLogName: $("#aliasLogName").val(),
			className:	$("#className").val(),
			splitChar: $("#splitChar").val(),
			filePath: $("#filePath").val(),
			future: $("#future").val(),
			action: "modifyDayConf"},
		
		function(json) {

			if (json != "ERROR") {

				parent.$("#dayConf-table tbody").html(""); 
				for(var i = 0; i < json.length; i++) {

					parent.$("#dayConf-table tbody").append( 
				            "<tr>" +
								"<td align='center'>" + json[i].aliasLogName + "</td>" + 
								"<td align='center'>" + json[i].filePath + "</td>" +
								"<td align='center'>" + json[i].className + "</td>" + 
								"<td align='center'>" +
								"<a href='javascript: openDialogModifyDayConf(" + json[i].confId + ", " + json[i].appId + ");'>修改</a>&nbsp;&nbsp;" +
								"<a href='./rel_app_day.jsp?confId=" + json[i].confId + "&appId=" + json[i].appId + "&action=deleteDayConfRel'>删除</a>&nbsp;&nbsp;" + 
								"</td></tr>" );	
				}
				    
				parent.$("#dialog_day_modify").dialog("close");
	         }else {

				updateTips("已经存在关联");
			
	      }
		}, 

	    "json");
}
</script>

<body>

<form>
<%
	DayConfPo tmp = DayConfAo.get().findAppDayConfByConfId(Integer.parseInt(confId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td align="center">日志文件别名: </td>
		<td><input type="text" name="aliasLogName" id="aliasLogName" size="100" value="<%=tmp.getAliasLogName() %>"></td>
	</tr>
	<tr>
		<td align="center">分析器: </td>
		<td><input type="text" name="className" id="className" size="100" value="<%=tmp.getClassName() %>"></td>
	</tr>
	<tr>
		<td align="center" width="200">行分隔符: </td>
		<td><input type="text" name="splitChar" id="splitChar" size="100" value="<%=tmp.getSplitChar() %>"></td>
	</tr>
	<tr>
		<td align="center" width="200">文件路径: </td>
		<td ><input type="text" name="filePath" id="filePath" size="100" value="<%=tmp.getFilePath() %>"></td>
	</tr>
	<tr>
		<td>分析器的附带参数: </td>
		<td ><input type="text" name="future" id="future" value="" size="100"></td>
	</tr>
</table>
</div>
</div>

<table>
	<tr>
		<td align="center">
		<input type="button" value="修改" id="btnAdd" onclick="modifyDayConfFunc()">
		<input type="button" value="关闭" onclick='parent.$("#dialog_day_modify").dialog("close")'></td>
	</tr>
</table>
</form>
</body>
</html>