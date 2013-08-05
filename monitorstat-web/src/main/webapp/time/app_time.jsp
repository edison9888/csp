<%@page import="com.taobao.csp.common.ZKClient"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
</head>
<body>
<%
	String appName = request.getParameter("appName");
	String appId = request.getParameter("appId"); 
	
	AppInfoPo appInfopo = null;
	if(appId!=null){
		appInfopo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));
	}else if(appName!=null){
		appInfopo = AppInfoAo.get().getAppInfoByOpsName(appName);
	}else{
		appInfopo = AppInfoAo.get().getAppInfoByOpsName("hesper");
	}
	appName = appInfopo.getAppName();
	try{
	List<String> list = ZKClient.get().list("/csp/monitor/app");
	if(list.contains(appName)){
		out.print("<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appInfopo.getAppId()+"'>进入老实时日志采集系统</a>");
		out.print("</br>");
		out.print("</br>");
		out.print("此应用已经加入到<a href='http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId="+appInfopo.getAppId()+"'>新的实时日志采集系统</a>");
	}else{
		out.print(appName+"  还没有加入到 新的实时监控中，请联系小赌||游骥||中亭||尊严协助加入!");
		out.print("<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appInfopo.getAppId()+"'>进入老实时日志采集系统</a>");
	}}catch(Exception e){
		
	}
%>
</body>
</html>