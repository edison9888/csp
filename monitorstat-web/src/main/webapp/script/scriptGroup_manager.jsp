<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@ page import="java.util.*"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>


<%@page import="com.taobao.monitor.script.ao.ScriptGroupAo"%>
<%@page import="com.taobao.monitor.script.ScriptGroupPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-脚本组管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<style type="text/css">

div {
	font-size: 12px;
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

$(function(){
	 $(".deleteAction").click(function(){
	   return confirm("想清楚要删除了没有啊?");
	  });
	});
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
String id = request.getParameter("id");
String action = request.getParameter("action");

if("delete".equals(action)){
	ScriptGroupAo.get().deleteScriptGroup(Integer.parseInt(id));
}
List<ScriptGroupPo> groupList = ScriptGroupAo.get().findAllScriptGroup();
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="dialog-title-userSearch" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><label id="dialog-title">脚本组列表</label>&nbsp;&nbsp;
<input type="button" value="添加新脚本组" onclick="location.href='./scriptGroup_add.jsp'">
<a href="./script_manager.jsp"><font color="red"><--返回到脚本列表</font></a>
</div>
<div id="dialog-userSearch" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
		<td align="center">名称</td>
		<td align="center">操作</td>
	</tr>
	<%for(ScriptGroupPo po:groupList){ %>
	<tr >
		<td><%=po.getGroupName() %></td>
		<td><a href="./scriptGroup_update.jsp?groupId=<%=po.getGroupId() %>">修改</a>&nbsp;&nbsp;
		<a id="deleteAction" class="deleteAction" href="./scriptGroup_manager.jsp?id=<%=po.getGroupId() %>&action=delete">删除</a> &nbsp;&nbsp;
		</td>
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>