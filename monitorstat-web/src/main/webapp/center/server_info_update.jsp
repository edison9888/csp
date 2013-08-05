<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-��������Ϣ����</title>
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



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./server_info_update.jsp" method="post">
<%



String action = request.getParameter("action");
if("update".equals(action)){
	String serverName = request.getParameter("serverName");
	String ip = request.getParameter("ip");
	String desc = request.getParameter("desc");
	String id =request.getParameter("id");
	
	ServerInfoPo po = new ServerInfoPo();
	po.setServerId(Integer.parseInt(id));
	po.setServerName(serverName);
	po.setServerIp(ip);
	po.setServerDesc(desc);
	boolean b = ServerInfoAo.get().updateServerInfo(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("�ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
	<a href="./server_info_center.jsp">����</a>
	<%
}else{

	String id =request.getParameter("id");
	ServerInfoPo po = ServerInfoAo.get().findAllServerInfoById(Integer.parseInt(id));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���·������Ļ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>��������: </td>
		<td><input type="text" name="serverName" value="<%=po.getServerName() %>"></td>
	</tr>
	<tr>
		<td>IP: </td>
		<td><input type="text" name="ip" value="<%=po.getServerIp() %>"></td>
	</tr>
		<tr>	
		<td>����:</td>
		<td>
			<textarea rows="4" cols="100" name="desc" id="desc"><%=po.getServerDesc() %></textarea>
		</td>
	</tr>
	
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="<%=id %>" name="id">
		<input type="hidden" value="update" name="action">
		<input type="submit" value="���·�������Ϣ">
		<input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>