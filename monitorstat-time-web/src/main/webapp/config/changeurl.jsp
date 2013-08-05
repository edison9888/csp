<%@page import="com.taobao.csp.common.ZKClient"%>
<%@page import="org.apache.zookeeper.data.Stat"%>
<%@page import="org.apache.zookeeper.CreateMode"%>
<%@page import="org.apache.zookeeper.ZooDefs.Ids"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
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
Date date = new Date();
String time = date.getTime() + "";
try{
	ZKClient.get().setData("/csp/url_cache", time.getBytes());
	out.println("success, /csp/url_cache -> " + time);
}catch(Exception e){
	out.println(e);
}
%>
</body>
</html>