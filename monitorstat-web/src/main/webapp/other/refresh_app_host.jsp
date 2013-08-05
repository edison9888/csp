<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.util.CspSyncOpsHostInfos"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>刷新缓存</title>
</head>
<body>
<%
Thread thread = new Thread(new Runnable() {
	public void run() {
		CspSyncOpsHostInfos.immediatelySync();
	}
});

thread.start();
%>
</body>
</html>