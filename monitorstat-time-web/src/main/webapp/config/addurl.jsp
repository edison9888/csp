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
String url = request.getParameter("url");
String site = request.getParameter("site");
if(StringUtils.isNotBlank(appName)&&StringUtils.isNotBlank(url)&&StringUtils.isNotBlank(site)){
CheckUrl check = new CheckUrl();
check.setAppName(appName);
check.setUrl(url);
for(String e:site.split(","))
	check.getSiteSet().add(e);

UrlMonitorThread.addCheckUrl(check);
}
%>
<form action="./addurl.jsp" method="post">
<table>
	<tr>
		<td>appName:</td><td><input type="text" name="appName"></td>
		<td>URL:</td><td><input type="text" name="url"></td>
		<td>Site:</td><td><input type="text" name="site"></td>
	</tr>
</table>
<input type="submit"  value="提交">
</form>

</body>
</html>