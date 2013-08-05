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
<title>���ļ��-Ӧ����Ϣ����</title>
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

<%

request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
AppInfoPo app = AppCache.get().getKey(Integer.parseInt(appId));
String appName = app.getAppName();
String opsName = app.getOpsName();

/*
if(!UserPermissionCheck.check(request,"center",appId)){
	out.print("��û��Ȩ�޲���!");
	return;
}*/
%>

<script type="text/javascript">

function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/app_info_center.jsp";
	location.href=url;
}

function openDialogModifyDb() {
	
	$(".validateTips").html("ֻ���޸��Ѿ����ڵĹ���");
	$( "#dialog-form-db-modify" ).dialog( "open" );
}


function openDialogModifyServer() {
	$(".validateTips").html("ֻ���޸��Ѿ����ڵĹ���");
	$( "#dialog-form-server-modify" ).dialog( "open" );
}


function openDialogModifyTimeConf(confId, appId) {

	$("#iframe_time_modify").attr("src","<%=request.getContextPath () %>/center/rel_app_timeConf_update1.jsp?confId="+confId + "&appId=" + appId );
	$("#dialog_time_modify").dialog("open");
}

$(document).ready(function(){

		tips = $( ".validateTips" );

	//�޸�dialog�е���ʾ����
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
		out.print("��û��Ȩ�޲���!");
		return;
	}
	
	String confId = request.getParameter("confId");
	TimeConfAo.get().deleteTimeConfByConfId(Integer.parseInt(confId));
}
AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));
ServerInfoPo server =  ServerInfoAo.get().findServerInfoByAppIdAndType(Integer.parseInt(appId),"time");

%>
<!-- ��ʾӦ�õ���Ϣ  -->
 <table width="100%">
 	<tr>
 		<td align="center">Ӧ�á�<%=appInfoPo.getAppName() %>��(<%=((appInfoPo.getTimeDeploy() == 0) ? "����Ч":"��Ч")%>)ʵʱ���ý���    --<a target="_blank" href="alter_message.jsp?appId=<%=appInfoPo.getAppId() %>&action=time&serverName=<%=server==null?"":server.getServerName() %>"> ���ñ��֪ͨ</a></td>
 	</tr>
 </table>
 
<!-- �����������Ϣ -->
<%
List<AppInfoPo> appInfo4ServerPoList = AppInfoAo.get().findServerRel(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
����������   : &nbsp;&nbsp;&nbsp;
</div>

<div id="dialog-server" class="ui-dialog-content ui-widget-content">
<table id="server-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" >��������</td>
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
<!-- ��ʾ���ݿ����Ϣ  -->
<%
List<AppInfoPo> appInfo4DbPoList = AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�������ݿ�   : &nbsp;&nbsp;&nbsp;
</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
<table id="database-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" width="100">���ݿ���</td>
			<td align="center" width="800">URL</td>
			<!--<td align="center">����</td>-->
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
<!-- ʵʱ��� -->
<%
List<TimeConfPo> timeConfPoList = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">����ʵʱ������Ϣ  :
<a id="btn-add-time" href="javascript:openAddConf()">���ʵʱ����</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table id="timeConf-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
	<tr>
		<td align="center">ʵʱ�����ļ���</td>
		<td align="center">ʵʱ�����ļ�·��</td>
		<td align="center">ʵʱ�����ļ�������</td>
		<td align="center" width="100">����</td>
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
			<a href="javascript: openDialogModifyTimeConf(<%=po.getConfId()%>,<%=po.getAppId() %>);">�޸�</a>&nbsp;&nbsp;
			<a href="./rel_app_time_noAction.jsp?confId=<%=po.getConfId()%>&appId=<%=po.getAppId() %>&appName=<%=AppCache.get().getKey(po.getAppId()).getAppName() %>&action=deleteTimeConfRel">ɾ��</a>&nbsp;&nbsp;
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
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��������   :&nbsp;&nbsp;<a href="./rel_app_host_check1.jsp?appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>&type=time">�����ػ���</a>&nbsp;&nbsp;</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<%
	for(String name:siteSet){ 
		Integer[] siteNum=	siteMap.get(name);
		if(siteNum == null){siteNum = new Integer[]{0,0,0};}
	%>
	<tr>
		<td width="150"><%=name %>:  �� <%=siteMap.get(name)[0] %> �� </td>
		<td>&nbsp;&nbsp; ��ʱ����<%=siteMap.get(name)[1] %>��</td>
		<td> &nbsp; &nbsp; �־ñ���<%=siteMap.get(name)[2] %>��</td>
	</tr>
	<%} %>
</table>
</div>
</div>

<!-- ��������Ϊ�˲��õ���dialog��ʱ��ҳ���м������� -->
<!-- ���ʵʱ���õĵ����� -->
<div id="dialog_time_add" title="���ʵʱ����">
	<iframe id="iframe_time_add" src="" frameborder="0" height="550" width="750" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_time_add").dialog({bgiframe: false,height: 600,	width:780,modal: true,draggable:true,resizable:false,autoOpen:false});
function openAddConf() {
	$("#iframe_time_add").attr("src","<%=request.getContextPath () %>/center/rel_app_timeConf_add1.jsp?appId=<%=appId%>");
	$("#dialog_time_add").dialog("open");
}
</script>


<!-- �޸�ʵʱ���õĵ����� -->
<div id="dialog_time_modify" title="�޸�ʵʱ����">
	<iframe id="iframe_time_modify" src="" frameborder="0" height="500" width="750" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_time_modify").dialog({bgiframe: false,height: 550, width:780,modal: true,draggable:true,resizable:false,autoOpen:false});
</script>

</body>
</html>