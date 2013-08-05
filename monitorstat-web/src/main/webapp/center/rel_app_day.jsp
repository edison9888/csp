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
<html>
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
String appName = AppCache.get().getKey(Integer.parseInt(appId)).getAppName();
//String opsName = request.getParameter("opsName");
String opsName = "detail";

/*
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
*/
%>

<script type="text/javascript">

$(function(){
	$(".deleteAction").click(function(){
	return confirm("�����Ҫɾ����û�а�");
	});
	});


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


function openDialogModifyDayConf(confId, appId) {

	$("#iframe_day_modify").attr("src","<%=request.getContextPath () %>/center/rel_app_dayConf_update1.jsp?confId="+confId + "&appId=" + appId );
	$("#dialog_day_modify").dialog("open");
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

	//=========================���ݿ����================================
	//�������ݿ�
	$( "#dialog-form-db-add" ).dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
		
			"ȡ��": function() {
				$( this ).dialog( "close" );
			},
	
			"���": function() {
					$.post("<%=request.getContextPath () %>/center/manage.mc", {
						appId: "<%=appId%>",
						dbId: $("#add-databaseId").val(), 
						appType: "day", 
						appName: "<%=appName%>",
						action: "addDatabase"},
						
						function(json) {
							if (json != "ERROR" && json.isSuccess != "false") {
								$("#database-table tbody").append( 
							            "<tr>" +
							        	"<td align='center'>" + json.databaseName + "</td>" + 
										"<td align='left'>" + json.databaseUrl + "</td>" +	
											"<td align='center'>" +
												"<a href='javascript: openDialogModifyDb();'>�޸�</a>&nbsp;&nbsp;" +
												"<a href='./rel_app_day.jsp?appId=" + json.appId + "&databaseId=" + json.databaseId + "&appType=" + json.appType + "&action=deleteDbRel>ɾ��</a>&nbsp;&nbsp;" + 
											"</td>" +
										"</tr>" );
								 $( "#dialog-form-db-add" ).dialog( "close" ); 	
					         }
						  else {

								updateTips("�Ѿ����ڹ���");
							
					      }
						}
						, "json");
			}
		}
	});

	//���������ݿⰴť���¼�
	$( "#btn-add-db" )
		.button()
		.click(function() {
			 updateTips("ÿ���ձ�����Ӧ��ֻ�ܹ���һ�����ݿ�.");	
			$( "#dialog-form-db-add" ).dialog( "open" );
		});

	//�޸����ݿ��dialog
	$( "#dialog-form-db-modify" ).dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
		
			"ȡ��": function() {
				$( this ).dialog( "close" );
			},
	
			"�޸�": function() {
					
					$.post("<%=request.getContextPath () %>/center/manage.mc", {
						appId: "<%=appId%>",
						dbId: $("#modify-databaseId").val(), 
						appType: "day", 
						appName: "<%=appName%>",
						action: "modifyDatabase"},
						
						function(json) {

							if (json != "ERROR" && json.isSuccess != "false") {
								$("#database-table tbody").html( 
							            "<tr>" +
							        	"<td align='center'>" + json.databaseName + "</td>" + 
										"<td align='left'>" + json.databaseUrl + "</td>" +	
											"<td align='center'>" +
												"<a href='javascript: openDialogModifyDb();'>�޸�</a>&nbsp;&nbsp;"+
												"<a href='./rel_app_day.jsp?appId=" + json.appId + "&databaseId=" + json.databaseId + "&appType=" + json.appType + "&action=deleteDbRel>ɾ��</a>&nbsp;&nbsp;" + 
											"</td>" +
										"</tr>" );
								 $( "#dialog-form-db-modify" ).dialog( "close" ); 	
					         }
						  else {

								updateTips("��û�й�����������ӹ���");
							
					      }
						}, 

					    "json");
			}
		}
	});
	
	//=========================���������================================
	//���ӷ�����
	$( "#dialog-form-server-add" ).dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
		
			"ȡ��": function() {
				$( this ).dialog( "close" );
			},
	
			"���": function() {
					$.post("<%=request.getContextPath () %>/center/manage.mc", {
						appId: "<%=appId%>",
						serverId: $("#add-serverId").val(), 
						appType: "day", 
						appName: "<%=appName%>",
						action: "addServer"},
						
						function(json) {

							if (json != "ERROR" && json.isSuccess != "false") {
								$("#server-table tbody").append( 
							            "<tr>" +
							            "<td align='center'>" + json.serverName + "</td>" + 
										"<td align='center'>" + json.serverIp + "</td>" +
											"<td align='center'>" +
											"<a href='javascript: openDialogModifyServer();'>�޸�</a>&nbsp;&nbsp;"+
											"<a href='./rel_app_day.jsp?appId=" + json.appId + "&serverId=" + json.serverId + "&appType=" + json.appType + "&action=deleteServerRel>ɾ��</a>&nbsp;&nbsp;" + 
										"</td>" +		
										"</tr>" );
								
							 	$( "#dialog-form-server-add" ).dialog( "close" ); 	
					         }
						  else {

								updateTips("�Ѿ����ڹ���");
							
					      }
						}, 

					    "json");
					// $( this ).dialog( "close" );
			}
		}
	});

	$( "#btn-add-server" )
		.button()
		.click(function() {
			 updateTips("ÿ���ձ�����Ӧ��ֻ�ܹ���һ��������.");	
			$( "#dialog-form-server-add" ).dialog( "open" );
		});


	//�޸ķ�������dialog
	$( "#dialog-form-server-modify" ).dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
		
			"ȡ��": function() {
				$( this ).dialog( "close" );
			},
	
			"�޸�": function() {
					
					$.post("<%=request.getContextPath () %>/center/manage.mc", {
						appId: "<%=appId%>",
						serverId: $("#modify-serverId").val(), 
						appType: "day", 
						appName: "<%=appName%>",
						action: "modifyServer"},
						
						function(json) {

							if (json != "ERROR" && json.isSuccess != "false") {
								$("#server-table tbody").html( 
							            "<tr>" +
							            "<td align='center'>" + json.serverName + "</td>" + 
										"<td align='center'>" + json.serverIp + "</td>" +
											"<td align='center'>" +
											"<a href='javascript: openDialogModifyServer();'>�޸�</a>&nbsp;&nbsp;"+
											"<a href='./rel_app_day.jsp?appId=" + json.appId + "&serverId=" + json.serverId + "&appType=" + json.appType + "&action=deleteServerRel>ɾ��</a>&nbsp;&nbsp;" + 
										"</td>" +		
										"</tr>" );
								 $( "#dialog-form-server-modify" ).dialog( "close" ); 	
					         }
						  else {

								updateTips("��û�й�����������ӹ���");
							
					      }
						}, 

					    "json");
					// $( this ).dialog( "close" );
			}
		}
	});

	$( "#btn-modify-server" )
		.button()
		.click(function() {
			 updateTips("ֻ���޸��Ѿ����ڵĹ���.");	
			$( "#dialog-form-server-modify" ).dialog( "open" );
		});
	

	//=============�ձ��������============================
	//���ӷ�����
	$( "#dialog-form-day-add" ).dialog({
		autoOpen: false,
		height: 250,
		width: 800,
		modal: true,
		buttons: {
		
			"ȡ��": function() {
				$( this ).dialog( "close" );
			},
	
			"���": function() {
					
			}
		}
	});

});

</script>
</head>
<body>




<jsp:include page="../head.jsp"></jsp:include>


<%
String action = request.getParameter("action");
String appType = request.getParameter("appType");
String databaseId = request.getParameter("databaseId");
String serverId = request.getParameter("serverId");
if("deleteDbRel".equals(action)){
	
	
	DatabaseAppRelPo relPo = new DatabaseAppRelPo();
	relPo.setAppId(Integer.parseInt(appId));
	relPo.setAppType(appType);
	relPo.setDatabaseId(Integer.parseInt(databaseId));
	AppInfoAo.get().deleteRel(relPo);
}

if("deleteServerRel".equals(action)){
	
	ServerAppRelPo relPo = new ServerAppRelPo();
	relPo.setAppId(Integer.parseInt(appId));
	relPo.setAppType(appType);
	relPo.setServerId(Integer.parseInt(serverId));
	
	AppInfoAo.get().deleteRel(relPo);
}
if("deleteDayConfRel".equals(action)){
	String confId = request.getParameter("confId");
	DayConfAo.get().deleteDayConfByConfId(Integer.parseInt(confId));
}
AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));
%>
<!-- ��ʾӦ�õ���Ϣ  -->
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Ӧ�á�<%=appInfoPo.getAppName() %>���ձ����ý���
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath () %>/center/app_info_center1.jsp"><font color="red">���ص�Ӧ���б�<--</font></a>
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td width="300">Ӧ����: </td>
		<td id="myname"><%=appInfoPo.getAppName() %></td>
	</tr>
	<tr>
		<td>�����: </td>
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
		<td><%=((appInfoPo.getDayDeploy() == 0) ? "����Ч":"��Ч")%></td>
	</tr>
	<tr>
		<td>day_timeDeploy:</td>
		<td><%=((appInfoPo.getTimeDeploy() == 0) ? "����Ч":"��Ч")%></td>
	</tr>


</table>
</div>
</div>


<!-- ��ʾ���ݿ����Ϣ  -->
<%
List<AppInfoPo> appInfo4DbPoList = AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�������ݿ�   : &nbsp;&nbsp;&nbsp;
<button id="btn-add-db">������ݿ����</button>
</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./rel_app_day.jsp" method="post">
<table id="database-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" width="100">���ݿ���</td>
			<td align="center" width="800">URL</td>
			<td align="center">����</td>
		</tr>
	</thead>
	<tbody>
	<%
		for(AppInfoPo po : appInfo4DbPoList) {
			
			if(po.getAppType().equals("time")){
				
				continue;
			}
	%>
	<tr>
	
		<td align="center" id="databaseName"><%=po.getDataBaseInfoPo().getDbName()%></td>
		<td align="left" id="url"><%=po.getDataBaseInfoPo().getDbUrl()%></td>
		<td align="center">
			<a href='javascript: openDialogModifyDb();'>�޸�</a>&nbsp;&nbsp;	
			<a id="deleteAction" class="deleteAction" href="./rel_app_day.jsp?appId=<%=po.getAppId() %>&databaseId=<%=po.getDataBaseInfoPo().getDbId() %>&appName=<%=po.getAppName() %>&appType=<%=po.getAppType() %>&action=deleteDbRel">ɾ��</a> &nbsp;&nbsp;
		</td>
	</tr>
	
	<%
		}
	%>
	</tbody>	
</table>
</form>
</div>
</div>

<!-- ������ݿ�ĵ����� -->

<div class="demo1">
<div id="dialog-form-db-add" title="����µ����ݿ����">
	<p class="validateTips">��������Ѿ����ڵ����ݿ�.</p>
<%
		List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
%>
	<form>
	<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td>���ݿ�: </td>
		<td>
			<select name="add-databaseId" id="add-databaseId">
				<%for(DataBaseInfoPo dbInfo: dbInfoList){%> 
					<option value="<%=dbInfo.getDbId()%>"><%=dbInfo.getDbName() %></option>
				<%} %>
			</select>
		</td>
	</tr>
</table>
</form>
</div>
</div>

<!--�޸����ݿ�ĵ����� -->
<div class="demo2">
<div id="dialog-form-db-modify" title="�޸����ݿ����">
<p class="validateTips">ֻ���޸����ݿ���.</p>
	<form>
	<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>���ݿ�: </td>
		<td>
			<select name="modify-databaseId" id="modify-databaseId">
				<%for(DataBaseInfoPo dbInfo: dbInfoList){%> 
					<option value="<%=dbInfo.getDbId()%>"><%=dbInfo.getDbName() %></option>
				<%} %>
			</select>
		</td>
	</tr>
</table>
</form>
</div>
</div>

<!-- �����������Ϣ -->
<%
List<AppInfoPo> appInfo4ServerPoList = AppInfoAo.get().findServerRel(Integer.parseInt(appId));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="databaseTitle" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
����������   : &nbsp;&nbsp;&nbsp;
<button id="btn-add-server">��ӷ���������</button>
</div>

<div id="dialog-server" class="ui-dialog-content ui-widget-content">
<form action="./rel_app_day.jsp" method="post">
<table id="server-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" width="250">��������</td>
			<td align="center" width="250">IP</td>
			<td align="center">����</td>
		</tr>
	</thead>
	<tbody>
	<%
		for(AppInfoPo po : appInfo4ServerPoList) {
			
			if("time".equals(po.getAppType())) {
				
				continue;
			}
	%>
	<tr>
		<td align="center" ><%=po.getServerInfoPo().getServerName()%></td>
		<td align="center" ><%=po.getServerInfoPo().getServerIp()%></td>
		<td align="center">
			<a href='javascript: openDialogModifyServer();'>�޸�</a>&nbsp;&nbsp;	
			<a id="deleteAction" class="deleteAction" href="./rel_app_day.jsp?appId=<%=po.getAppId() %>&serverId=<%=po.getServerInfoPo().getServerId() %>&appName=<%=po.getAppName() %>&appType=<%=po.getAppType() %>&action=deleteServerRel">ɾ��</a> &nbsp;&nbsp;
		</td>
	</tr>
	
	<%
		}
	%>
	</tbody>
		
</table>
</form>
</div>
</div>

<!-- ��ӷ������ĵ����� -->

<div class="demo1">
<div id="dialog-form-server-add" title="����µķ���������">
	<p class="validateTips">��������Ѿ����ڵķ�����.</p>
<%
List<ServerInfoPo>  serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
	<form>
	<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>������: </td>
		<td>
			<select name="add-serverId" id="add-serverId">
				<%for(ServerInfoPo serverInfo: serverInfoList){%> 
					<option value="<%=serverInfo.getServerId()%>"><%=serverInfo.getServerName() %></option>
				<%} %>
			</select>
		</td>
	</tr>
</table>
</form>
</div>
</div>

<!--�޸ķ������ĵ����� -->
<div class="demo2">
<div id="dialog-form-server-modify" title="�޸ķ���������">
<p class="validateTips">ֻ���޸ķ�������.</p>
	<form>
	<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>������: </td>
		<td>
			<select name="modify-serverId" id="modify-serverId">
				<%for(ServerInfoPo serverInfo: serverInfoList){%> 
					<option value="<%=serverInfo.getServerId()%>"><%=serverInfo.getServerName() %></option>
				<%} %>
			</select>
		</td>
	</tr>
</table>
</form>
</div>
</div>

<!-- �ձ���� -->
<%
	List<DayConfPo> dayConfPoList = DayConfAo.get().findAllAppDayConfByAppId(Integer.parseInt(appId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�����ձ�������Ϣ  :
<button id="btn-add-day">����ձ�����</button></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<form action="./rel_app_day.jsp" method="post">
<table id="dayConf-table" align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<thead>
		<tr>
			<td align="center" width="98">ʵʱ�����ļ���</td>
			<td align="center" width="395">ʵʱ�����ļ�·��</td>
			<td align="center" width="120">ʵʱ�����ļ�������</td>
			<td align="center" width="120">����</td>
		</tr>
	</thead>
	<tbody>
		<%
			for(DayConfPo po : dayConfPoList) {
		%>
		<tr>
			<td align="center"><%=po.getAliasLogName() %></td>
			<td align="center"><%=po.getFilePath() %></td>
			<td align="center"><%=po.getClassName() %></td>
			<td align="center">
				<a href="javascript: openDialogModifyDayConf(<%=po.getConfId()%>,<%=po.getAppId() %>);">�޸�</a>&nbsp;&nbsp;
				<a id="deleteAction" class="deleteAction" href="./rel_app_day.jsp?confId=<%=po.getConfId()%>&appId=<%=po.getAppId() %>&appName=<%=AppCache.get().getKey(po.getAppId()).getAppName() %>&action=deleteDayConfRel">ɾ��</a>&nbsp;&nbsp;
			</td>
		</tr>
		
		<%
			}
		%>
	</tbody>
</table>
</form>
</div>
</div>



<!-- host��� -->
<%


AppInfoPo appInfoPo1 = AppInfoAo.get().findAppWithHostListByAppId(Integer.parseInt(appId));
int sumCM3 = 0;
int sumCM4 = 0;
int sumCM3Limit = 0;	//��ʱ��
int sumCM3Save = 0;	//�־ñ�
int sumCM4Limit = 0;	//��ʱ��
int sumCM4Save = 0;	//�־ñ�
char a;
for(HostPo hostPo : appInfoPo1.getHostList()) {
	
	if("CM3".equals(hostPo.getHostSite())) {
		
		sumCM3++;

		if(hostPo.getSavedata().charAt(0) == '1') {
			
			sumCM3Limit++;
		}
		if(hostPo.getSavedata().charAt(1) == '1') {
			
			sumCM3Save++;
		}
	} else if("CM4".equals(hostPo.getHostSite())) {
		
		sumCM4++;
		if(hostPo.getSavedata().charAt(0) == '1') {
			
			sumCM4Limit++;
		}
		if(hostPo.getSavedata().charAt(1) == '1') {
			
			sumCM4Save++;
		}
	}
}

%>

<!--<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��������   :
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">

	<tr>
		<td width="150">CM3:  �� <%=sumCM3 %> �� </td>
		<td>&nbsp;&nbsp; ��ʱ����<%=sumCM3Limit %>��</td>
		<td> &nbsp; &nbsp; �־ñ���<%=sumCM3Save %>��</td>
		<td><a target="_blank" href="./rel_app_host_check1.jsp?appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>&site=CM3&type=day">�鿴����</a>&nbsp;&nbsp;</td>
		
	</tr>
		
	<tr>
		<td width="150">CM4:  �� <%=sumCM4 %> �� </td>
		<td>&nbsp;&nbsp; ��ʱ����<%=sumCM4Limit %>��</td>
		<td> &nbsp; &nbsp; �־ñ���<%=sumCM4Save %>��</td>
	<td><a target="_blank" href="./rel_app_host_check1.jsp?appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>&site=CM4&type=day">�鿴����</a>&nbsp;&nbsp;</td>
			
	</tr>
		
</table>
</div>
</div>


--><!-- ����ձ����õĵ����� -->
<div id="dialog_day_add" title="Basic modal dialog">
	<iframe id="iframe_day_add" src="" frameborder="0" height="450" width="700" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_day_add").dialog({bgiframe: false,height: 520,	width:730,modal: true,draggable:true,resizable:false,autoOpen:false});
$( "#btn-add-day" ).button().click(function() {
	$("#iframe_day_add").attr("src","<%=request.getContextPath () %>/center/rel_app_dayConf_add1.jsp?appId="+<%=appId%>);
	$("#dialog_day_add").dialog("open");
});
</script>

<!-- �޸��ձ����õĵ����� -->
<div id="dialog_day_modify" title="�޸��ձ�����">
	<iframe id="iframe_day_modify" src="" frameborder="0" height="450" width="700" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_day_modify").dialog({bgiframe: false,height: 300, width:730,modal: true,draggable:true,resizable:false,autoOpen:false});
</script>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>