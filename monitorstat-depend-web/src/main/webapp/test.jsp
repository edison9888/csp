<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.util.*"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom("itemcenter");
for(Map.Entry<String, List<HostPo>> entry:hostMap.entrySet()){
	String siteName = entry.getKey();
	List<HostPo> list = entry.getValue();
	out.println(siteName + "<br/>");
	for(HostPo po : list) {
		out.println(po.getHostIp() + ",");
	}
}
%>
</body>
</html>