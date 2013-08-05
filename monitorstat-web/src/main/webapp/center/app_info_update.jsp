<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-Ӧ����Ϣ����</title>
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
<form action="./app_info_update.jsp" method="post">
<%
String action = request.getParameter("action");
if("update".equals(action)){	
	String appName = request.getParameter("appName");
	String sortIndex = request.getParameter("sortIndex");
	String featrue =request.getParameter("feature");
	String opsName =request.getParameter("opsName");
	String groupName =request.getParameter("groupName");
	String appId =request.getParameter("id");
	String dayDeploy = request.getParameter("dayDeploy");
	String timeDeploy = request.getParameter("timeDeploy");
	String status = request.getParameter("status");
	String opsFiled = request.getParameter("opsFiled");
	String loginName = request.getParameter("loginName");
	String loginpassword = request.getParameter("loginpassword");
	
	String appRushHours = request.getParameter("appRushHours");
	
	String appType = request.getParameter("appType");
	
	
	AppInfoPo po = new AppInfoPo();
	po.setAppId(Integer.parseInt(appId));
	po.setSortIndex(Integer.parseInt(sortIndex));
	po.setAppName(appName);
	po.setFeature(featrue);
	po.setOpsName(opsName);
	po.setGroupName(groupName);
	po.setDayDeploy(Integer.parseInt(dayDeploy));
	po.setTimeDeploy(Integer.parseInt(timeDeploy));
	po.setAppStatus(Integer.parseInt(status));
	po.setOpsField(opsFiled);
	po.setLoginName(loginName);
	po.setLoginPassword(loginpassword);
	po.setAppRushHours(appRushHours);
	
	po.setAppType(appType);
	
	boolean b = AppInfoAo.get().updateappInfo(po);
	
	
	%>
	<font size="+3" color="red" >
	
	<%if(b){out.print("�ɹ�");}else{out.print("ʧ��!�����쳣");} %>
	</font>	
	<a href="./app_info_center.jsp">����</a>
	<%
}else{

String id =request.getParameter("id");
AppInfoPo po = AppInfoAo.get().findAppInfoById(Integer.parseInt(id));

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Ӧ�õĻ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td>Ӧ����: </td>
		<td><input type="text" name="appName" value="<%=po.getAppName() %>"></td>
	</tr>
	<tr>
		<td>Ӧ������: </td>
		<td>
			<select name="appType">
				<option value="pv" <%if("pv".equals(po.getAppType())){out.print("selected");} %>>ǰ��Ӧ��</option>
				<option value="center" <%if("center".equals(po.getAppType())){out.print("selected");} %>>centerӦ��</option>
				<option value="tair" <%if("tair".equals(po.getAppType())){out.print("selected");} %>>tair</option>
				<option value="db" <%if("db".equals(po.getAppType())){out.print("selected");} %>>���ݿ�</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>�����: </td>
		<td><input type="text" name="sortIndex" value="<%=po.getSortIndex() %>"></td>
	</tr>
	<tr>
		<td>Feature:</td>
		<td><input type="text" name="feature" value="<%=po.getFeature() %>"></td>
	</tr>
	<tr>
		<td>ops_name:</td>
		<td><input type="text" name="opsName" value="<%=po.getOpsName() %>"></td>
	</tr>
	<tr>
		<td>ops_filed:</td>
		<td><input type="text" name="opsFiled" value="<%=po.getOpsField() %>"></td>
	</tr>
	<tr>	
		<td>Group:</td>
		<td><input type="text" name="groupName" value="<%=po.getGroupName() %>"></td>
	</tr>
	<tr>	
		<td>�߷���ʱ���:</td>
		<td>
			<input type="text" name="appRushHours" value="<%=po.getAppRushHours() %>">HH-HH��ʽ			
		</td>
	</tr>
	<tr>	
		<td>��½�����û���:</td>
		<td>
			<input type="text" name="loginName" value="<%=po.getLoginName() %>">			
		</td>
	</tr>
	<tr>	
		<td>��½����������:</td>
		<td>
			<input type="password" name="loginpassword" value="<%=po.getLoginPassword() %>">			
		</td>
	</tr>
	<tr>	
		<td>day_Deploy:</td>
		<td>
			<select name="dayDeploy" id="dayDeploy">
				
				<option value="<%=(po.getDayDeploy() == 0)? 0 : 1%>"><%=(po.getDayDeploy() == 0 )? "����Ч" : "��Ч"%></option>
				<option value="<%=(po.getDayDeploy() == 0) ? 1 : 0%>"><%=(po.getDayDeploy() == 0) ? "��Ч" : "����Ч"%></option>
			</select>
		</td>
	</tr>
	<tr>	
		<td>time_Deploy:</td>
			<td>
			<select name="timeDeploy" id="timeDeploy">
				<option value="<%=(po.getTimeDeploy() == 0) ? 0 : 1%>"><%=po.getTimeDeploy() == 0 ? "����Ч" : "��Ч"%></option>
				<option value="<%=(po.getTimeDeploy() == 0) ? 1 : 0%>"><%=po.getTimeDeploy() == 0 ? "��Ч" : "����Ч"%></option>
			</select>
		</td>
	</tr>
	<tr>	
		<td>״̬:</td>
			<td>
			<select name="status" id="status">
	<option value="<%=(po.getAppStatus() == 0) ? 0 : 1%>"><%=po.getAppStatus() == 0 ? "����" : "ɾ��"%></option>
				<option value="<%=(po.getAppStatus() == 0) ? 1 : 0%>"><%=po.getAppStatus() == 0 ? "ɾ��" : "����"%></option>
			</select>
		</td>
	</tr>
	
	
</table>
</div>
</div>


<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="<%=id %>" name="id"><input type="hidden" value="update" name="action"><input type="submit" value="�ύ����"><input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>