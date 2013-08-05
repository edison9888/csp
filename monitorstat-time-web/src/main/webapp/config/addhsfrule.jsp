<%@page import="com.taobao.csp.alarm.configserver.ConfigServerListen"%>
<%@page import="com.taobao.csp.alarm.configserver.HsfRuleInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.csp.alarm.url.CheckUrl"%>
<%@page import="com.taobao.csp.alarm.url.UrlMonitorThread"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
<%
String appName = request.getParameter("appName");
String interfaceName = request.getParameter("interfaceName");
String methods = request.getParameter("methods");
String version = request.getParameter("version");
if(StringUtils.isNotBlank(appName)&&StringUtils.isNotBlank(interfaceName)){
	HsfRuleInfo check = new HsfRuleInfo();
check.setAppName(appName);
check.setInterfaceName(interfaceName);
if(StringUtils.isNotBlank(methods)){
	for(String e:methods.split(","))
		check.getMethods().add(e);
}
check.setVersion(version);
ConfigServerListen.addHsfRuleInfo(check);
}
%>
<form action="./addhsfrule.jsp" method="post">
<table>
	<tr>
		<td>appName:</td><td><input type="text" name="appName"></td>
		<td>HSF-Interface:</td><td><input type="text" name="interfaceName"></td>
		<td>methods:</td><td><input type="text" name="methods"></td>
		<td>version:</td><td><input type="text" name="version"></td>
	</tr>
</table>
<input type="submit"  value="提交">
</form>

</body>
</html>