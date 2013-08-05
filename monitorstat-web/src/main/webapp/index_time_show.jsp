<%@page import="com.taobao.csp.common.ZKClient"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.time.MonitorTimeAo"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.taobao.monitor.web.cache.TimeCache"%>
<%@page import="com.taobao.monitor.web.cache.CacheJsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

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
		out.print("新的csp 实时监控地址为:http://time.csp.taobao.net:9999/time/index_table.jsp");
		out.print("</br>");
		out.print("</br>");
	}else{
		out.print(appName+"  还没有加入到 新的实时监控中，请联系小赌||游骥||中亭||尊严协助加入!");
		out.print("<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appInfopo.getAppId()+"'>进入老实时日志采集系统</a>");
	}}catch(Exception e){
		
	}
%>
