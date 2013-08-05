<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.*"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>

<%@page import="com.taobao.monitor.script.ScriptlibraryInfo"%>
<%@page import="com.taobao.monitor.script.ao.ScriptAo"%>
<%@page import="com.taobao.monitor.script.ao.ScriptGroupAo"%>
<%@page import="com.taobao.monitor.script.ao.RoleAo"%>
<%@page import="com.taobao.monitor.script.ScriptGroupPo"%>
<%@page import="com.taobao.monitor.script.RolePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-脚本管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script> 


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
<script type="text/javascript">
//载入的时候先执行必须用：
$(document).ready(function(){
var type = $("#scriptExist").val();
if(type == 1) { //存在就显示filePath框
	$("#showFilePath").show();
	$("#showContent").hide();

} else {
	$("#showContent").show();
	$("#showFilePath").hide();
}
}); 

function showDivByType(){
	var type = $("#scriptExist").val();

	if(type == 1) { //存在就显示filePath框
		$("#showFilePath").show();
		$("#showContent").hide();

	} else {
		$("#showContent").show();
		$("#showFilePath").hide();
	}
}
</script>
</head>
<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
	return;
}
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./script_add.jsp" method="post">
<%



String action = request.getParameter("action");
if("add".equals(action)){
	String scriptName = request.getParameter("scriptName");
	String scriptType = request.getParameter("scriptType");
	String group = request.getParameter("group");
	String role =request.getParameter("role");
	String scriptExist =request.getParameter("scriptExist");
	String filePath =request.getParameter("filePath");
	String content = request.getParameter("content");
	String desc = request.getParameter("desc");
	String needParam = request.getParameter("needParam");
	
	
	ScriptlibraryInfo po = new ScriptlibraryInfo();
	po.setScriptName(scriptName);
	po.setScriptType(scriptType);
	po.setRole(Integer.parseInt(role));
	po.setScriptStatus(Integer.parseInt(scriptExist));
	po.setFilePath(filePath);
	po.setDesc(desc);
	po.setScriptContext(content);
	po.setNeedParam(Integer.parseInt(needParam));
	boolean b = ScriptAo.get().addScript(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("成功");}else{out.print("失败!出现异常");} %></font>	
	<a href="./script_manager.jsp">返回</a>
	<%
}else{

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加脚本的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>	
		<td>脚本组:</td>
		<td>
			<select name="group" id="group">
			<option value="">无</option>
			<%
				for(ScriptGroupPo groupPo : ScriptGroupAo.get().findAllScriptGroup()) {
					
					%>
					
				<option value="<%=groupPo.getGroupId() %>"><%=groupPo.getGroupName()%></option>
					<%
				}
			%>
			</select>
		</td>
	</tr>
	<tr>
		<td>名称: </td>
		<td><input type="text" name="scriptName" value=""></td>
	</tr>
	<tr>	
		<td>类型:</td>
		<td>
			<select name="scriptType" id="scriptType">
				<option value="shell">shell</option>
				<option value="python">python</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>需要额外参数: </td>
		<td>
			<select name="needParam" id="needParam">
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
		</td>
	</tr>
	<tr>	
		<td>权限:</td>
		<td>
			<select name="role" id="role">
			<%
				for(RolePo role : RoleAo.get().findAllRole()) {
					
					%>
					
				<option value="<%=role.getId() %>"><%=role.getRuleName()%></option>
					<%
				}
			%>
			</select>
		</td>
	</tr>
	<tr>	
		<td>机器中已经存在:</td>
		<td>
			<select name="scriptExist" id="scriptExist" onchange="showDivByType()">
			
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
		</td>
	</tr>
	<tr id="showFilePath">
		<td>脚本路径:</td>
		<td><input type="text" name="filePath" id="filePath" value="" size="123"></td>
	</tr>
	<tr id="showContent">	
		<td>填入脚本内容:</td>
		
			<td><textarea rows="30" cols="120" name="content" id="content"></textarea>
				
		</td>
	</tr>

	<tr>
		<td>说明:</td>
		<td>	<textarea rows="10" cols="120" name="desc" id="desc"></textarea></td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="add" name="action"><input type="submit" value="添加新脚本">
		<input type="button" value="返回" onclick="location.href='./script_manager.jsp'"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>