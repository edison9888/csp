<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.ServerAppRelPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�û�����</title>
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

String appId = request.getParameter("appId");
String action = request.getParameter("action");
String serverId = request.getParameter("serverId");
String appType = request.getParameter("appType");
String appName = request.getParameter("appName");

%>


<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_server_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}
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

<form action="./rel_app_server_add.jsp" method="post">
<%


if("add".equals(action)){

	ServerAppRelPo relPo = new ServerAppRelPo();
	relPo.setAppId(Integer.parseInt(appId));
	relPo.setAppType(appType);
	relPo.setServerId(Integer.parseInt(serverId));	
	
	if(!AppInfoAo.get().isExistServerAppRel(relPo)) {
		boolean b = AppInfoAo.get().addRel(relPo);
		
	%>
		<font size="+3" color="red"><%if(b){out.print("��ӳɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
			<a href="./rel_app_server_check.jsp?&appId=<%=appId %>&appName=<%=appName %>">����</a>	
	
	<%
	}else {
		
		%>
		<font size="+3" color="red"><%{out.print("һ��Ӧ��ֻ��ָ��һ�����������ߴ˹����Ѿ�����");} %></font>	
			<a href="./rel_app_server_check.jsp?&appId=<%=appId %>&appName=<%=appName %>">����</a>	
<%
	}
}

else{


	List<ServerInfoPo>  serverInfoList = ServerInfoAo.get().findAllServerInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ӹ���������</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>������: </td>
		<td>
			<select name="serverId" id="serverId">
				<%for(ServerInfoPo serverInfo: serverInfoList){%> 
					<option value="<%=serverInfo.getServerId()%>"><%=serverInfo.getServerName() %></option>
				<%} %>
			</select>
		</td>
	</tr>
	
	<tr>
		<td>Ӧ������: </td>
		<td>
			<select name="appType" id="appType">
				<option value="time">ʵʱ</option>
				<option value="day">�ձ�</option>
			</select>
		</td>
	</tr>
	
	

	<tr>
		<td>Ӧ����: </td>
		<td><input type="text" readonly="readonly" name="appName" value="<%=appName %>"></td>
	</tr>
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" name="appId" value="<%=appId %>">
		<input type="hidden" value="add" name="action"><input type="submit" value="��ӷ���������">		
				<input type="button" value="����" onclick="goToCheck()">
		</td>
	</tr>
</table>
</div>


</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>