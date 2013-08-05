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
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-ʵʱģ����Ϣ����</title>
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

%>

<%

String action = request.getParameter("action");
if("db".equals(action)){
	if(!UserPermissionCheck.check(request,"center","")){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	ConfigServer.get().alterDataBaseConfig();
}
if("time".equals(action)){
	String serverName = request.getParameter("serverName");
	String appId = request.getParameter("appId");
	if(!UserPermissionCheck.check(request,"center",appId)){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	ConfigServer.get().alterTimeConfig(serverName,Integer.parseInt(appId));
	out.print("�����ɹ�!");
}
if("day".equals(action)){
	String serverName = request.getParameter("serverName");
	String appId = request.getParameter("appId");
	if(!UserPermissionCheck.check(request,"center",appId)){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	ConfigServer.get().alterDayConfig(serverName,Integer.parseInt(appId));
}
%>
�����ɹ�!<a href="javascript:window.close()">�ر�</a>
</body>
</html>