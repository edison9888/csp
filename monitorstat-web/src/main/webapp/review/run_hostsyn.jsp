<%@page import="com.taobao.monitor.common.util.CspSyncOpsHostInfos"%>
<%@page import="ch.ethz.ssh2.Connection"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="java.io.IOException"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
CspSyncOpsHostInfos.immediatelySync();
%>
</body>
</html>