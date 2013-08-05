<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.util.OpsFreeHostCache"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>应用主机列表</title>


<%
String opsName = request.getParameter("opsName");
AppInfoPo appInfo;
if (opsName == null) {
	opsName = "detail";
}
List<HostPo>  hostList = new ArrayList<HostPo>();
String[] appArray = opsName.split("_");
for (String appStr : appArray) {
	List<HostPo>  tempHostList = OpsFreeHostCache.get().getHostListNoCache(appStr);
	if (tempHostList != null && tempHostList.size() > 0) {
		hostList.addAll(tempHostList);
	}
}
%>
</head>
<body>

<table border="1"  align="center" width="300">
	<tr align="center">
		<td>应用：<%=opsName %>   主机列表(<%=hostList.size() %>)</td>
	</tr>
<%
for (HostPo po : hostList) {
%>
	<tr align="center">
		<td><%=po.getHostName() %></td>
	</tr>
<%
}
%>	
</table>
</body>
</html>