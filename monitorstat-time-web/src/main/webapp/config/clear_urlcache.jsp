<%@page import=" com.taobao.csp.time.cache.TimeCache"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>ˢ����ַ�Ļ���</title>
</head>
<body>
<%
	TimeCache.get().refreshNow();
	out.println("ˢ�»���ɹ�!");
%>
</body>
</html>