<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDistribFlowAo"%>
<%@page import="com.taobao.monitor.web.distrib.DistribFlowPo"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.distrib.AppDistribFlowBo"%>
<%@page import="com.taobao.monitor.web.distrib.KeyDistribFlowBo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.web.util.AmLineFlash"%>
<%@page import="com.taobao.monitor.web.cache.CacheProviderCustomer"%>
<%@page import="com.taobao.monitor.web.distrib.ProviderDisribFlowBo"%>
<%@page import="java.util.Comparator"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>ºËÐÄ¼à¿Ø</title>
</head>
<body>
<%
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -1);
String opsName = request.getParameter("opsName");
if(opsName==null){
	opsName = "itemcenter";
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
response.sendRedirect("http://110.75.2.75:9999/depend/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName="+opsName+"&selectDate="+sdf.format(cal.getTime())) ;
%>
</body>
</html>