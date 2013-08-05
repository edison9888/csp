<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@ page import="java.util.*"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>




<%@page import="com.taobao.monitor.script.ao.ScriptAo"%>
<%@page import="com.taobao.monitor.script.ScriptlibraryInfo"%>
<%@page import="com.taobao.monitor.script.RolePo"%>
<%@page import="com.taobao.monitor.script.ao.RoleAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-脚本管理</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript">

$(function(){
	 $(".deleteAction").click(function(){
	   return confirm("想清楚要删除了没有啊?");
	  });
	});
</script>
</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
<tr><td>
<jsp:include page="../top.jsp"></jsp:include>
</td></tr>
<%
String id = request.getParameter("id");
String action = request.getParameter("action");

if("delete".equals(action)){
	ScriptAo.get().deleteScript(Integer.parseInt(id));
}
List<ScriptlibraryInfo> scriptList = ScriptAo.get().findAllScript();
List<RolePo> roleList = RoleAo.get().findAllRole();
%>
<tr><td width="1000">
<div class="ui-widget-content" >
<div id="dialog-title-userSearch" class=" ui-widget-header ui-corner-all "><label id="dialog-title">脚本列表</label>
<input type="button" value="添加新脚本" onclick="location.href='./script_add.jsp'">
<a href="./scriptGroup_manager.jsp"><font color="red">-->脚本组管理<--</font></a>

</div>

<table width="1000" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
		<td align="center">名称</td>
		<td align="center">类型</td>
		<td align="center">执行权限</td>
		<td align="center">操作</td>
	</tr>
	<%for(ScriptlibraryInfo po:scriptList){ %>
	<tr >
		<td><%=po.getScriptName() %></td>
		<td><%=po.getScriptType() %></td>
		<td><%=RoleAo.get().findAllRoleById(po.getRole()).getRuleName() %></td>
		<td><a href="./script_update.jsp?scriptId=<%=po.getScriptId() %>">修改</a>&nbsp;&nbsp;
		<a id="deleteAction" class="deleteAction" href="./script_manager.jsp?id=<%=po.getScriptId() %>&action=delete">删除</a> &nbsp;&nbsp;
		<a href="./script_info.jsp?scriptId=<%=po.getScriptId() %>">查看</a>
		<a href="./script_group_rel_check.jsp?scriptId=<%=po.getScriptId() %>">脚本组</a>
		<a href="./script_singleRun.jsp?scriptId=<%=po.getScriptId() %>">执行</a></td>
	</tr>
	<%} %>
</table>
</div>

</td></tr>

<tr>
	<td><input type="button" value="批量执行" onclick="location.href='./script_groupRun.jsp'"></td>
</tr>

<tr><td>
<jsp:include page="../bottom.jsp"></jsp:include>
</td></tr>
</table>
</body>
</html>