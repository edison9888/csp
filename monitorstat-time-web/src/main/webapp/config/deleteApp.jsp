<%@page import="com.taobao.csp.common.ZKClient"%>
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

ZKClient.get().delete("/csp/monitor/app/"+appName);



%>
</body>
</html>