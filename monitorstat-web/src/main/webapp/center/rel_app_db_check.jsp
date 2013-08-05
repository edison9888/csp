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
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
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

<%

String appId = request.getParameter("appId");
String action = request.getParameter("action");
String databaseId = request.getParameter("databaseId");
String appType = request.getParameter("appType");
String appName = request.getParameter("appName");
%>
<script type="text/javascript">

function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/app_info_center.jsp";
	location.href=url;
}
function goToAdd(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_db_add.jsp?&appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}

</script>


</head>
<body>



<%

request.setCharacterEncoding("gbk");
/*
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./rel_app_db_check.jsp" method="post">


<%
if("delete".equals(action)){
	
	DatabaseAppRelPo relPo = new DatabaseAppRelPo();
	relPo.setAppId(Integer.parseInt(appId));
	relPo.setAppType(appType);
	relPo.setDatabaseId(Integer.parseInt(databaseId));
	
	AppInfoAo.get().deleteRel(relPo);
}


List<AppInfoPo> appInfoPoList = AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联数据库   : 共 <%=appInfoPoList.size() %> 个</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%

	if(appInfoPoList.size() != 0) {

%>
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="center">应用名</td>
		<td align="center">关联的数据库</td>
		<td align="center">应用类型</td>
		<td align="center">操作</td>
	</tr>
	<%
		for(AppInfoPo po : appInfoPoList) {
	%>
	<tr>
		<td align="center"><%=po.getAppName() %></td>
		<td align="center"><%=po.getDataBaseInfoPo().getDbName() %></td>
		<td align="center"><%=po.getAppType() %></td>
		<td>
			<a href="./rel_app_db_update.jsp?appId=<%=po.getAppId() %>&appName=<%=po.getAppName() %>&serverId=<%=po.getDataBaseInfoPo().getDbId() %>&appType=<%=po.getAppType()%>">修改</a>&nbsp;&nbsp;
			<a href="./rel_app_db_check.jsp?appId=<%=po.getAppId() %>&databaseId=<%=po.getDataBaseInfoPo().getDbId() %>&appName=<%=po.getAppName() %>&appType=<%=po.getAppType() %>&action=delete">删除</a> &nbsp;&nbsp;
		</td>
	</tr>
	
	<%
		}
	%>
		
</table>

<%
	}//end if
	else {
%>
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">

	<tr>
	<td align="center" >
		<a href="./rel_app_db_add.jsp?appId=<%=appId %>&appName=<%=appName %>"><font size="5">还没有关联的数据库，请添加关联</font></a>
	</td>
	</tr>
		<%
	}
		%>
</table>
</div>
</div>
<table width="100%">
	<tr>
		<td align="center"><input type="button" value="添加" onclick="goToAdd()">
		<input type="button" value="返回" onclick="goToCenter()"></td>
	</tr>
</table>

</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>