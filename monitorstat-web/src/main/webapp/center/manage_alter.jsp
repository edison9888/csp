<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
<%@page import="com.taobao.monitor.config.ConfigServer"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-实时模板信息配置</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
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

<%

String action = request.getParameter("action");
if("db".equals(action)){
	ConfigServer.get().alterDataBaseConfig();
}
if("time".equals(action)){
	String serverName = request.getParameter("serverName");
	String appId = request.getParameter("appId");
	ConfigServer.get().alterTimeConfig(serverName,Integer.parseInt(appId));
}
if("day".equals(action)){
	String serverName = request.getParameter("serverName");
	String appId = request.getParameter("appId");
	ConfigServer.get().alterDayConfig(serverName,Integer.parseInt(appId));
}
List<ServerInfoPo> serverInfo = ServerInfoAo.get().findAllServerInfo();
%>


 <div id="tabs">
	<ul>
			<li><a href="#tabs-1">实时运行</a></li>
			<li><a href="#tabs-2">日报运行</a></li>
	</ul>
<div id="tabs-1">
<table width="200" border="1">
  <tr>   
    <td>服务器名称</td>
    <td>应用名称</td>
	<td>应用状态</td>
	<td>实时运行</td>
    <td>操作</td>
  </tr>
  <%
  Date current = new Date();
  List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
  Map<String,Map<String,Date>> timeMap =  ConfigServer.get().getTimeAppStatus();
  Map<String,Map<String,Date>> dayMap = ConfigServer.get().getDayAppStatus();
  for(ServerInfoPo server:serverInfoList){
	  boolean f = true;
	  List<AppInfoPo> timeList = new ArrayList<AppInfoPo>();
	  List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerId(server.getServerId(),"time");
	  for(AppInfoPo app:appList){ 
		  if(app.getTimeDeploy()==1)
		  	timeList.add(app);
	  }
	  for(AppInfoPo app:timeList){
		  
  %>
  <tr> 
  <%if(f){ %>  
    <td rowspan="<%=timeList.size() %>" width="200"><%=server.getServerName() %></td>
    <%f=false;} %>
    <td><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId=<%=app.getAppId() %>"><%=app.getAppName() %></a></td>
    <td><%=app.getAppStatus()==0?"正常":"删除" %></td>
	<td>
	<%
	String timestatus = "无心跳";
	String daystatus = "无心跳";
	Map<String,Date> appMap = timeMap.get(server.getServerName());
	
	if(appMap != null){
		Date date = appMap.get(app.getAppName());
		if(date != null){
			if(current.getTime()-date.getTime() <120000){
				timestatus="正常";
			}
		}
	}
	%>
	<%=timestatus %></td>
    <td>
    <a href="./manage_alter.jsp?action=time&serverName=<%=server.getServerName() %>&appId=<%=app.getAppId() %>">实时设置变更</a>&nbsp;&nbsp;
    </td>
  </tr>
  <%}} %>
</table>
</div>
<div id="tabs-2">
<table width="200" border="1">
  <tr>   
    <td>服务器名称</td>
    <td>应用名称</td>
	<td>应用状态</td>
	<td>实时运行</td>
	<td>日报运行</td>
  </tr>
  <%
  for(ServerInfoPo server:serverInfoList){
	  boolean f = true;
	 
	  List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerId(server.getServerId(),"day");
	  List<AppInfoPo> dayList = new ArrayList<AppInfoPo>();
	  for(AppInfoPo app:appList){ 
		  if(app.getDayDeploy()==1)
			  dayList.add(app);
	  }
	  for(AppInfoPo app:dayList){ 
		  AppInfoPo po = AppCache.get().getKey(app.getAppName());
  %>
  <tr> 
  <%if(f){ %>  
    <td rowspan="<%=dayList.size() %>" width="200"><%=server.getServerName() %></td>
    <%f=false;} %>
    <td><a target="_blank" href='http://cm.taobao.net:9999/monitorstat/app_detail.jsp?selectAppId=<%=po.getAppDayId() %>'><%=app.getAppName() %></a></td>
    <td><%=app.getAppStatus()==0?"正常":"删除" %></td>
	<td>
	<%
	String daystatus = "无心跳";	
	
	Map<String,Date> appMap = dayMap.get(server.getServerName());
	if(appMap != null){
		Date date = appMap.get(app.getAppName());
		if(date != null){
			if(current.getTime()-date.getTime() <60000){
				daystatus="正常";
			}
		}
	}
	
	%>
	<%=daystatus%></td>
    <td></td>
  </tr>
  <%}} %>
</table>
</div>
</div>
<!-- 
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">库表变更通知</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<a href="./manage_alter.jsp?action=db">库表变更通知</a>&nbsp;&nbsp;	
</div>
</div>
 -->
<script type="text/javascript">

$(function() {
	$("#tabs").tabs();
});
</script>
</body>
</html>