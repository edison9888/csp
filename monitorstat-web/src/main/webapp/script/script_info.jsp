<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.*"%>

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>



<%@page import="com.taobao.monitor.script.ScriptlibraryInfo"%>
<%@page import="com.taobao.monitor.script.ao.ScriptAo"%>
<%@page import="com.taobao.monitor.script.ao.RoleAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�ű�����</title>
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
	font-family: "����";
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
//�����ʱ����ִ�б����ã�
$(document).ready(function(){
var type = $("#scriptExist").text();
if(type == "��") { //���ھ���ʾfilePath��
	$("#showFilePath").show();
	$("#showContent").hide();

} else {
	$("#showContent").show();
	$("#showFilePath").hide();
}
}); 
</script>
</head>
<body>



<%
request.setCharacterEncoding("gbk");
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./script_update.jsp" method="post">
<%
	String action = request.getParameter("action");
	String scriptId = request.getParameter("scriptId");
	ScriptlibraryInfo po = ScriptAo.get().findScriptById(Integer.parseInt(scriptId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ű��Ļ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>����: </td>
		<td><%=po.getScriptName()%></td>
	</tr>
	<tr>	
		<td>����:</td>
		<td><%=po.getScriptType() %></td>
	</tr>
	<tr>	
		<td>��Ҫ�������: </td>		
		<td><%=po.getNeedParam() == 0 ? "��" : "��" %></td>
	</tr>
	<tr>	
		<td>Ȩ��:</td>
		<td><%=RoleAo.get().findAllRoleById(po.getRole()).getRuleName()%></td>
	</tr>
	<tr>	
		<td>�������Ѿ�����:</td>
		<td id="scriptExist" ><%=po.getScriptStatus() == 0 ? "��" : "��" %></td>
	</tr>
	<tr id="showFilePath">
		<td>�ű�·��:</td>
		<td><%=po.getFilePath() %></td>
	</tr>
	<tr id="showContent">	
		<td>�ű�����:</td>
		
			<td><textarea rows="40" cols="120" name="content" id="content" readonly="readonly"><%=po.getScriptContext() %></textarea>
				
		</td>
	</tr>

	<tr>
		<td>˵��:</td>
		<td>	<textarea rows="10" cols="120" name="desc" id="desc" readonly="readonly"><%=po.getDesc() %></textarea></td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="button" value="����" onclick="location.href='./script_manager.jsp'"></td>
	</tr>
</table>
</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>