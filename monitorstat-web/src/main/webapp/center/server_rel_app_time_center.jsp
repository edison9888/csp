<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ServerAppRelPo"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>



<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-��������Ϣ����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/axurerppage.css" type="text/css" rel="stylesheet">
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

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
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#userInfoTableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#userInfoTableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
		$(".deleteAction").click(function(){
			return confirm("�����Ҫɾ����û�а�");
		});
	})

$(document).ready(function() {

	$("#addIframe").fancybox({
		'width'				: '50%',
		'height'			: '75%',
		'autoScale'			: false,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe'
	});
});
	
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>

<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>

<%
String id = request.getParameter("serverId");
ServerInfoPo serverPo = ServerInfoAo.get().findAllServerInfoById(Integer.parseInt(id));

String action = request.getParameter("action");

if("delete".equals(action)){
String appId = request.getParameter("appId");
ServerAppRelPo relPo = new ServerAppRelPo();
relPo.setAppId(Integer.parseInt(appId));
relPo.setAppType("time");
relPo.setServerId(Integer.parseInt(id));
	AppInfoAo.get().deleteRel(relPo);
}
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�������Ļ�����Ϣ
<a href="./server_info_center.jsp" STYLE="text-decoration:underline" >&nbsp;<font color="red">���ص���������ҳ<--</font></a>
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>��������: </td>
		<td><%=serverPo.getServerName() %></td>
	</tr>
	<tr>
		<td>IP: </td>
		<td><%=serverPo.getServerIp() %></td>
	</tr>
	<tr>	
		<td>����:</td>
		<td>
			<textarea rows="1"  cols="100" name="desc" id="desc" ><%=serverPo.getServerDesc() %></textarea>
		</td>
	</tr>
	
</table>
</div>
</div>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ǰ���ã�&nbsp;&nbsp;
<a id="addIframe" href="./server_rel_app_time_add.jsp?serverId=<%=id %>" STYLE="text-decoration:underline"><font color='red'>-->���Ӧ�ù���</font></a> &nbsp; 
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerIdAndAppType(Integer.parseInt(id),"time");
Map<String, List<HostPo>> appRelHost = new HashMap<String, List<HostPo>>();

int number = 0;

%>
<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
		<td align="center">���</td>
		<td align="center">Ӧ����</td>
		<td align="center">��ʱ��</td>
		<td align="center">�־ñ�</td>
		<td align="center">����</td>
	</tr>
	<%for(AppInfoPo po: appList){
	
		number++;
	 %>
	<tr >
		<td align="center"><%=number%></td>
		<td align="left"><%=po.getAppName()%></td>
		<%
		int limitData = 0;	//��ʱ��
		int saveData = 0;	//�־ñ�
		for(HostPo hostPo : AppInfoAo.get().findAppWithHostListByAppId(po.getAppId()).getHostList()) {
			
			if(hostPo.getSavedata().charAt(0) == '1') {
				
				limitData++;
			} 
			if(hostPo.getSavedata().charAt(1) == '1') {
				
				saveData++;
			}
		}
		
		%>
		<td align="center"><%=limitData%></td>
		<td align="center"><%=saveData%></td>
		<td align="center"><a id="deleteAction" class="deleteAction"  href="./server_rel_app_time_center.jsp?appId=<%=po.getAppId() %>&serverId=<%=id %>&action=delete">ɾ��</a></td>
	
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>