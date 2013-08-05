<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.*"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
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

<form action="./app_info_add.jsp" method="post">
<%



String action = request.getParameter("action");
if("add".equals(action)){
	String appName = request.getParameter("appName");
	String sortIndex = request.getParameter("sortIndex");
	String feature =request.getParameter("feature");
	String opsName =request.getParameter("opsName");
	String groupName =request.getParameter("groupName");
	String dayDeploy = request.getParameter("dayDeploy");
	String timeDeploy = request.getParameter("timeDeploy");
	String opsFiled = request.getParameter("opsFiled");
	
	String loginName = request.getParameter("loginName");
	String loginpassword = request.getParameter("loginpassword");
	String appRushHours = request.getParameter("appRushHours");
	String appType = request.getParameter("appType");
	
	String id =request.getParameter("id");
//	String databaseId = request.getParameter("databaseId");
//	String serverId = request.getParameter("serverId");
//	String appType = request.getParameter("appType");
	
	AppInfoPo po = new AppInfoPo();
	po.setAppName(appName);
	po.setSortIndex(Integer.parseInt(sortIndex));
	po.setFeature(feature);
	po.setOpsName(opsName);
	po.setGroupName(groupName);
	po.setOpsField(opsFiled);
	po.setDayDeploy(Integer.parseInt(dayDeploy));
	po.setTimeDeploy(Integer.parseInt(timeDeploy));
	po.setLoginName(loginName);
	po.setLoginPassword(loginpassword);
	po.setAppRushHours(appRushHours);
	po.setAppType(appType);
	boolean b = AppInfoAo.get().addAppInfoData(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("成功");}else{out.print("失败!出现异常");} %></font>	
	<a href="./app_info_center.jsp">返回</a>
	<%
}else{

	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加应用的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>应用名: </td>
		<td><input type="text" name="appName" value=""></td>
	</tr>
	<tr>
		<td>应用类型: </td>
		<td>
			<select name="appType">
				<option value="pv">前端应用</option>
				<option value="center">center应用</option>
				<option value="db">数据库</option>
				<option value="tair">tair</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>排序号: </td>
		<td><input type="text" name="sortIndex" value=""></td>
	</tr>
	<tr>
		<td>Feature:</td>
		<td><input type="text" name="feature" value=""></td>
	</tr>
	<tr>
		<td>ops_name:</td>
		<td><input type="text" name="opsName" value=""></td>
	</tr>
	<tr>
		<td>ops_field:</td>
		<td><input type="text" name="opsFiled" value="module_name"></td>
	</tr>
	<tr>	
		<td>Group:</td>
		<td>
			<input type="text" name="groupName" value="">			
		</td>
	</tr>
	<tr>	
		<td>高峰期时间段:</td>
		<td>
			<input type="text" name="appRushHours" value="20-22">HH-HH格式			
		</td>
	</tr>
	<tr>	
		<td>登陆机器用户名:</td>
		<td>
			<input type="text" name="loginName" value="nobody">			
		</td>
	</tr>
	<tr>	
		<td>登陆机器用密码:</td>
		<td>
			<input type="password" name="loginpassword" value="">			
		</td>
	</tr>
	<tr>	
		<td>day_Deploy:</td>
		<td>
			<select name="dayDeploy" id="dayDeploy">
				<option value="0">不生效</option>
				<option value="1">生效</option>
			</select>
		</td>
	</tr>
	<tr>	
		<td>time_Deploy:</td>
		<td>
			<select name="timeDeploy" id="timeDeploy">
				<option value="0">不生效</option>
				<option value="1">生效</option>
			</select>
		</td>
	</tr>
	<tr>	
		<td>状态:</td>
		<td>
			<select name="status" id="status">
				<option value="0">正常</option>
				<option value="1">删除</option>
			</select>
		</td>
	</tr>

	
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="add" name="action"><input type="submit" value="添加新应用"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>