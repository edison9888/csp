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
<%@page import="com.taobao.monitor.web.cache.*"%>
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
String tmpId = request.getParameter("tmpId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_dayConf_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}

function openDialogModifyServer() {
	$(".validateTips").html('<font size="4" color="red">出现异常，添加失败，请重新添加.');

}

function addDayConfFunc() {

	$.post("<%=request.getContextPath () %>/center/manage.mc", {
		appId: "<%=appId%>",
		aliasLogName: $("#aliasLogName").text(),
		className:	$("#className").text(),
		splitChar: $("#splitChar").text(),
		filePath: $("#filePath").val(),
		future: $("#future").text(),
		action: "addDayConf"},
		
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
							"</tr>" );	
				}
				    
				parent.$("#dialog_day_add").dialog("close");
	         }
		  else {

				updateTips("已经存在关联");
			
	      }
		}, 

	    "json");
}
</script>
<body>



<%


List<DayConfTmpPo> dayConfTmpList = DayConfAo.get().findAllDayConfTmp();
if(tmpId == null || tmpId.equals("") ) {
	
	for(DayConfTmpPo tmpPo : dayConfTmpList){
		
		tmpId = tmpPo.getTmpId() + "";
		break;
	}
	
}
/*
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>
<form action="./rel_app_dayConf_add1.jsp" method="get">
<p class="validateTips" ><font size="4">请填写表单.</font></p>

<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">

<select name="tmpId" id="tmpId">
	<%
	
	for(DayConfTmpPo tmpPo : dayConfTmpList){ 		
		
	%>
	<option value="<%=tmpPo.getTmpId() %>"<%if(tmpPo.getTmpId() == Integer.parseInt(tmpId)){out.print("selected");} %>><%=tmpPo.getAliasLogName() %></option>
	<%} %>
</select>
<input type="submit"  value="获取模板">
<input type="hidden" name="appId" value="<%=appId %>">
<input type="hidden" name="appName" value="<%=appName %>">
</div>

<%

	DayConfTmpPo tmp = DayConfAo.get().findDayConfTmpById(Integer.parseInt(tmpId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font size="5">添加日报配置的基本信息</font></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="left" width="600" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td align="center" width="200">日志文件别名: </td>
		<td id="aliasLogName"><%=tmp.getAliasLogName() %></td>
	</tr>
	<tr>
		<td align="center" width="200">分析器: </td>
		<td id="className"><%=tmp.getClassName() %></td>
	</tr>
	<tr>
		<td align="center" width="200">行分隔符: </td>
		<td id="splitChar"><%=tmp.getSplitChar() %></td>
	</tr>
	<tr>
		<td align="center" width="200">文件路径: </td>
		<td ><input type="text" name="filePath" id="filePath" size="100" value="<%=tmp.getFilePath() %>"></td>
	</tr>
	<tr>
		<td>分析器的附带参数: </td>
		<td ><input type="text" name="future" id="future" size="100"></td>
	</tr>
</table>
<table>
	<tr>
		<td align="center">
		<input type="button" value="添加" id="btnAdd" onclick="addDayConfFunc()">
		<input type="button" value="关闭" onclick='parent.$("#dialog_day_add").dialog("close")'></td>
	</tr>
</table>
</div>
</div>


</form>
</body>
</html>