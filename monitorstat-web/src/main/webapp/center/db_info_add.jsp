<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.DataBaseInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.DataBaseInfoAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>��������-���ݿ���Ϣ����</title>
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
<form action="./db_info_add.jsp" method="post">
<%



String action = request.getParameter("action");
if("add".equals(action)){
	String dbName = request.getParameter("dbName");
	String url = request.getParameter("url");
	String userName =request.getParameter("userName");
	String pwd =request.getParameter("pwd");
	String dbType =request.getParameter("dbType");
	String desc = request.getParameter("desc");
	String id =request.getParameter("id");
//	String databaseId = request.getParameter("databaseId");
//	String serverId = request.getParameter("serverId");
//	String appType = request.getParameter("appType");
	
	DataBaseInfoPo po = new DataBaseInfoPo();
	po.setDbName(dbName);
	po.setDbUrl(url);
	po.setDbUser(userName);
	po.setDbPwd(pwd);
	po.setDbType(Integer.parseInt(dbType));
	po.setDbDesc(desc);
	boolean b = DataBaseInfoAo.get().addDataBaseInfoData(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("�ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
	<a href="./db_info_center.jsp">����</a>
	<%
}else{

	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">������ݿ�Ļ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>���ݿ���: </td>
		<td><input type="text" name="dbName" value=""></td>
	</tr>
	<tr>	
		<td>����:</td>
		<td>
			<select name="dbType" id="dbType">
				<option value="0">���Ŀ�</option>
				<option value="1">ҵ���</option>
				<option value="2">�ⲿ��</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>�û���:</td>
		<td><input type="text" name="userName" value=""></td>
	</tr>
	<tr>
		<td>����:</td>
		<td><input type="text" name="pwd" value=""></td>
	</tr>
	<tr>
		<td>URL: </td>
		<td><input type="text" name="url" value="" size="100"></td>
	</tr>

	<tr>	
		<td>����:</td>
		<td>
			<textarea rows="4" cols="100" name="desc" id="desc"></textarea>
		</td>
	</tr>

	
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="add" name="action"><input type="submit" value="��������ݿ�"><input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>