<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-应用信息配置</title>
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

<script type="text/javascript">
function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/app_info_center1.jsp";
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

<form action="./app_info_relDataBase.jsp" method="post">
<%
String appId = request.getParameter("appId");
AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加应用的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td width="200">应用名: </td>
		<td><%=appInfoPo.getAppName() %></td>
	</tr>
	<tr>
		<td>排序号: </td>
		<td><%=appInfoPo.getSortIndex() %></td>
	</tr>
	<tr>
		<td>Feature:</td>
		<td><%=appInfoPo.getFeature() %></td>
	</tr>
	<tr>
		<td>OPS:</td>
		<td><%=appInfoPo.getOpsName() %></td>
	</tr>
	<tr>	
		<td>Group:</td>
		<td><%=appInfoPo.getGroupName() %></td>
	</tr>
	<tr>	
		<td>day_Deploy:</td>
		<td><%=((appInfoPo.getDayDeploy() == 0) ? "不生效":"生效")%></td>
	</tr>
	<tr>	
		<td>day_timeDeploy:</td>
		<td><%=((appInfoPo.getDayDeploy() == 0) ? "不生效":"生效")%></td>
	</tr>
	
	
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="button" value="返回" onclick="goToCenter()">
		</td>
	</tr>
</table>
</div>


</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>