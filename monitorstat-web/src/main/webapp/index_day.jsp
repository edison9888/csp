
<%@page import="com.taobao.monitor.web.cache.CacheJsp"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%
String b = request.getParameter("reset");
if(b == null){
	b = "false";
}
%>
<%=CacheJsp.get().getDayHtml("http://127.0.0.1:9999/monitorstat/index_day1.jsp",Utlitites.getMonitorDate(),Boolean.parseBoolean(b))%>
