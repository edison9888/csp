<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.csp.common.ZKClient"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
List<String> monitorlist = null;
try {
	monitorlist = ZKClient.get().list("/csp/monitor/app");
} catch (Exception e) {
}
for (String  app : monitorlist) {
	AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
	if(po == null){
		out.print(app);
	}
}
%>
</body>
</html>