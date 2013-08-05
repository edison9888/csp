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
<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
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


<form action="./rel_app_db_update.jsp" method="post">
<%


String appId = request.getParameter("appId");
String appName = request.getParameter("appName");
String action = request.getParameter("action");
String serverId = request.getParameter("serverId");
String appType = request.getParameter("appType");

if("update".equals(action)){

	ServerAppRelPo relPo = new ServerAppRelPo();
	relPo.setAppId(Integer.parseInt(appId));
	relPo.setAppType(appType);
	relPo.setServerId(Integer.parseInt(serverId));	
	
	
	boolean b = AppInfoAo.get().updateRel(relPo);
%>	

		<font size="+3" color="red"><%if(b){out.print("���³ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
		<a href="./rel_app_server_check.jsp?appId=<%=appId %>">����</a>
	
<%
}

else{
	
	List<ServerInfoPo>  serverInfoList = ServerInfoAo.get().findAllServerInfo();

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�������ݿ���Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>������: </td>
		<td>
			<select name="serverId" id="serverId">
				<%for(ServerInfoPo serverInfo: serverInfoList){%> 
					<option value="<%=serverInfo.getServerId()%>"><%=serverInfo.getServerName()%></option>
				<%} %>
			</select>
		</td>
	</tr>
	
	<tr>
		<td>Ӧ������: </td>
		<td>
			<select name="appType" id="appType">
				<option value="<%=((appType.equals("time")) ? "time" : "day")%>"><%=((appType.equals("time")) ? "time" : "day")%></option>
				<option value="<%=(!(appType.equals("time")) ? "time" : "day")%>"><%=(!(appType.equals("time")) ? "time" : "day")%></option>
					
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
		<input type="hidden" value="update" name="action"><input type="submit" value="���·���������">		
		<input type="button" value="�ر�" onclick="window.close()">
		</td>
	</tr>
</table>
</div>

</form>

<%
}
%>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>