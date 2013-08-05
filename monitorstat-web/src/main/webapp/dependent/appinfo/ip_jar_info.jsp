<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.dependent.ao.AppJarAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.dependent.appinfo.AppStatus"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.dependent.appinfo.AppJar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Locale"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
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
</style>
</head>

<%
String id = request.getParameter("id");

AppStatus appStatus = AppJarAo.get().getAppStatus(id);
List<AppJar> listJar = AppJarAo.get().getAppJar(id);
SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH);
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);

%>

<body>
<jsp:include page="../../head.jsp"></jsp:include>
<table border="1" width="100%">
	<tr>
		<td>应用名称:</td><td><%=appStatus.getAppName() %></td>
	</tr>
	<tr>
		<td>IP:</td><td><%=appStatus.getHostIp() %></td>
	</tr>
	<tr>
		<td>机房:</td><td><%=appStatus.getHostSite() %></td>
	</tr>
	<tr>
		<td>jboss启动时间:</td><td><%=appStatus.getJbossStartTime() %></td>
	</tr>
	<tr>
		<td>apache启动时间:</td><td><%=appStatus.getHttpdStartTime().replace("\n","<br/>") %></td>
	</tr>
	<tr>
		<td>web.xml:</td><td><%=appStatus.getWebInfo()==null?"":appStatus.getWebInfo().replace("<","&lt;").replace(">","&gt;")%></td>
	</tr>
	<tr>
		<td>webx.xml:</td><td><%=appStatus.getWebxInfo()==null?"":appStatus.getWebxInfo().replace("<","&lt;").replace(">","&gt;")%></td>
	</tr>	
</table>

<table border="1" width="100%">
	<tr>
		<td>名称</td>
		<td>大小</td>
		<td>修改时间</td>
	</tr>
	<%for(AppJar jar:listJar){ %>
	<tr>
		<td><%=jar.getJarName() %></td>
		<td><%=jar.getJarSize() %></td>
		<td><%=sdf2.format(sdf1.parse(jar.getModifyTime()))%></td>
	</tr>
	<%} %>
</table>
<jsp:include page="../../buttom.jsp"></jsp:include>
</body>
</html>