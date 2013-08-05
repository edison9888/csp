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
String confId = request.getParameter("confId");
String appName = request.getParameter("appName");
%>
<script type="text/javascript">

function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/app_info_center.jsp";
	location.href=url;
}
function goToAdd(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_dayConf_add.jsp?&appId=<%=appId%>&appName=<%=appName%>";
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





<form action="./rel_app_dayConf_check.jsp" method="post">


<%
if("delete".equals(action)){
	
	DayConfAo.get().deleteDayConfByConfId(Integer.parseInt(confId));
}


List<DayConfPo> dayConfPoList = DayConfAo.get().findAllAppDayConfByAppId(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联日报配置信息  : 共 <%=dayConfPoList.size() %> 个</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%

	if(dayConfPoList.size() != 0) {

%>
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="center">应用名</td>
		<td align="center">日报配置文件名</td>
		<td align="center">日报配置文件路径</td>
		<td align="center">日报配置文件分析器</td>
	</tr>
	<%
		for(DayConfPo po : dayConfPoList) {
	%>
	<tr>
	
		<td align="center"><%=appName %></td>
		<td align="center"><%=po.getAliasLogName() %></td>
		<td align="center"><%=po.getFilePath() %></td>
		<td align="center"><%=po.getClassName() %></td>
		<td>
			<a href="./rel_app_dayConf_update.jsp?appId=<%=po.getAppId() %>&confId=<%=po.getConfId() %>&appName=<%=appName %>">修改</a>&nbsp;&nbsp;
			<a href="./rel_app_dayConf_check.jsp?appId=<%=po.getAppId() %>&confId=<%=po.getConfId() %>&action=delete">删除</a> &nbsp;&nbsp;
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
		<a href="./rel_app_dayConf_add.jsp?appId=<%=appId %>&appName=<%=appName %>"><font size="5">还没有关联的日报配置信息，请添加关联</font></a>
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