<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.taobao.monitor.web.distinct.TimeDistinctManage"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.distinct.DistinctInterface"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.util.RequestByUrl"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link type="text/css" href="http://cm.taobao.net:9999/monitorstat/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<title>���ܼ��-��ص�仯</title>

<style type="text/css">
div {
	font-size: 12px;
}
table {
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}


img {
cursor:pointer;
border:0;
}
</style>

</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
String appId = request.getParameter("appId");
if(appId == null){
	appId = "1";
}
%>

<%=RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/distinct/distinct.jsp?appId="+appId) %>
</body>
</html>
