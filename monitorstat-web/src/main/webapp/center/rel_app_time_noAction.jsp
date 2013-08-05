<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.*"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.web.cache.*"%>

<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-应用信息配置</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/ui.button.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.position.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.resizable.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.dialog.js"></script>
	<script src="<%=request.getContextPath() %>/statics/js/ui/jquery.effects.core.js"></script>
	
	
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

request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
AppInfoPo app = AppCache.get().getKey(Integer.parseInt(appId));
String appName = app.getAppName();
String opsName = app.getOpsName();

/*
if(!UserPermissionCheck.check(request,"center",appId)){
	out.print("你没有权限操作!");
	return;
}*/
%>

<script type="text/javascript">

function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/app_info_center.jsp";
	location.href=url;
}

function openDialogModifyDb() {
	
	$(".validateTips").html("只能修改已经存在的关联");
	$( "#dialog-form-db-modify" ).dialog( "open" );
}


function openDialogModifyServer() {
	$(".validateTips").html("只能修改已经存在的关联");
	$( "#dialog-form-server-modify" ).dialog( "open" );
}


function openDialogModifyTimeConf(confId, appId) {

	$("#iframe_time_modify").attr("src","<%=request.getContextPath () %>/center/rel_app_timeConf_update1.jsp?confId="+confId + "&appId=" + appId );
	$("#dialog_time_modify").dialog("open");
}

$(document).ready(function(){

		tips = $( ".validateTips" );

	//修改dialog中的提示文字
	function updateTips( t ) {
		tips
			.text( t )
			.addClass( "ui-state-highlight" );
		setTimeout(function() {
			tips.removeClass( "ui-state-highlight", 1500 );
		}, 500 );
	}
	
});



function showMessage(flag,select){
	var options = select.options;
	for(var i=0;i<options.length;i++){
		var option = options[i];
		$("#"+flag+option.value).hide();
	}
	$("#"+flag+select.options[select.selectedIndex].value).show();
	
	
}

</script>
</head>
<body>
<%
String action = request.getParameter("action");
String appType = request.getParameter("appType");
String databaseId = request.getParameter("databaseId");
String serverId = request.getParameter("serverId");

if("deleteTimeConfRel".equals(action)){
	
	if(!UserPermissionCheck.check(request,"center",appId)){
		out.print("你没有权限操作!");
		return;
	}
	
	String confId = request.getParameter("confId");
	TimeConfAo.get().deleteTimeConfByConfId(Integer.parseInt(confId));
}
AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));
ServerInfoPo server =  ServerInfoAo.get().findServerInfoByAppIdAndType(Integer.parseInt(appId),"time");

%>
<!-- 显示应用的信息  -->
 <table width="100%">
 	<tr>
 		<td align="center">应用【<%=appInfoPo.getAppName() %>】(<%=((appInfoPo.getTimeDeploy() == 0) ? "不生效":"生效")%>)实时配置界面    --<a target="_blank" href="alter_message.jsp?appId=<%=appInfoPo.getAppId() %>&action=time&serverName=<%=server==null?"":server.getServerName() %>"> 配置变更通知</a></td>
 	</tr>
 </table>
 
<!-- 服务器相关信息 -->
<%
List<AppInfoPo> appInfo4ServerPoList = AppInfoAo.get().findServerRel(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
关联服务器   : &nbsp;&nbsp;&nbsp;
</div>

<div id="dialog-server" class="ui-dialog-content ui-widget-content">
<table id="server-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" >服务器名</td>
			<td align="center" >IP</td>
		</tr>
	</thead>
	<tbody>
	<%if(server != null){ %>	
	<tr>
		<td align="center" ><%=server.getServerName()%></td>
		<td align="center" ><%=server.getServerIp()%></td>
	</tr>
	<%} %>
	</tbody>
</table>
</div>
</div>
<!-- 显示数据库的信息  -->
<%
List<AppInfoPo> appInfo4DbPoList = AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联数据库   : &nbsp;&nbsp;&nbsp;
</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
<table id="database-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" width="100">数据库名</td>
			<td align="center" width="800">URL</td>
			<!--<td align="center">操作</td>-->
		</tr>
	</thead>
	<tbody>
	<%
		for(AppInfoPo po : appInfo4DbPoList) {
			
			if(po.getAppType().equals("day")){
				
				continue;
			}
	%>
	<tr>
	
		<td align="center" id="databaseName"><%=po.getDataBaseInfoPo().getDbName()%></td>
		<td align="left" id="url"><%=po.getDataBaseInfoPo().getDbUrl()%></td></tr>	
	<%
		}
	%>
	</tbody>	
</table>
</div>
</div>
<!-- 实时相关 -->
<%
List<TimeConfPo> timeConfPoList = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联实时配置信息  :
<a id="btn-add-time" href="javascript:openAddConf()">添加实时配置</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table id="timeConf-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
	<tr>
		<td align="center">实时配置文件名</td>
		<td align="center">实时配置文件路径</td>
		<td align="center">实时配置文件分析器</td>
		<td align="center" width="100">操作</td>
	</tr>
	</thead>
	
	<tbody>
	<%
		for(TimeConfPo po : timeConfPoList) {
	%>
	<tr>
	
		<td align="center"><%=po.getAliasLogName() %></td>
		<td align="left"><%=po.getFilePath() %></td>
		<td align="left"><%=po.getClassName() %></td>
		<td align="center">
			<a href="javascript: openDialogModifyTimeConf(<%=po.getConfId()%>,<%=po.getAppId() %>);">修改</a>&nbsp;&nbsp;
			<a href="./rel_app_time_noAction.jsp?confId=<%=po.getConfId()%>&appId=<%=po.getAppId() %>&appName=<%=AppCache.get().getKey(po.getAppId()).getAppName() %>&action=deleteTimeConfRel">删除</a>&nbsp;&nbsp;
		</td>
	</tr>
	<%
		}
	%>
	</tbody>
</table>
</div>
</div>



<%


AppInfoPo appInfoPo1 = AppInfoAo.get().findAppWithHostListByAppId(Integer.parseInt(appId));
Set<String> siteSet = new HashSet<String>();
for(HostPo hostPo : appInfoPo1.getHostList()) {
	siteSet.add(hostPo.getHostSite());
}

Map<String,Integer[]> siteMap = new HashMap<String,Integer[]>();
for(HostPo hostPo : appInfoPo1.getHostList()) {
	for(String siteName:siteSet){
		if(siteName.equals(hostPo.getHostSite())){
			Integer[] num = siteMap.get(siteName);
			if(num == null){
				num = new Integer[]{0,0,0};
				siteMap.put(siteName,num);
			}
			num[0]++;
			if(hostPo.getSavedata().charAt(0) == '1') {
				
				num[1]++;
			}
			if(hostPo.getSavedata().charAt(1) == '1') {
				
				num[2]++;
			}			
			break;
		}
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联主机   :&nbsp;&nbsp;<a href="./rel_app_host_check1.jsp?appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>&type=time">管理监控机器</a>&nbsp;&nbsp;</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<%
	for(String name:siteSet){ 
		Integer[] siteNum=	siteMap.get(name);
		if(siteNum == null){siteNum = new Integer[]{0,0,0};}
	%>
	<tr>
		<td width="150"><%=name %>:  共 <%=siteMap.get(name)[0] %> 个 </td>
		<td>&nbsp;&nbsp; 临时表：共<%=siteMap.get(name)[1] %>个</td>
		<td> &nbsp; &nbsp; 持久表：共<%=siteMap.get(name)[2] %>个</td>
	</tr>
	<%} %>
</table>
</div>
</div>

<!-- 放这里是为了不让弹出dialog的时候页面中间往下拉 -->
<!-- 添加实时配置的弹出框 -->
<div id="dialog_time_add" title="添加实时配置">
	<iframe id="iframe_time_add" src="" frameborder="0" height="550" width="750" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_time_add").dialog({bgiframe: false,height: 600,	width:780,modal: true,draggable:true,resizable:false,autoOpen:false});
function openAddConf() {
	$("#iframe_time_add").attr("src","<%=request.getContextPath () %>/center/rel_app_timeConf_add1.jsp?appId=<%=appId%>");
	$("#dialog_time_add").dialog("open");
}
</script>


<!-- 修改实时配置的弹出框 -->
<div id="dialog_time_modify" title="修改实时配置">
	<iframe id="iframe_time_modify" src="" frameborder="0" height="500" width="750" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_time_modify").dialog({bgiframe: false,height: 550, width:780,modal: true,draggable:true,resizable:false,autoOpen:false});
</script>

</body>
</html>