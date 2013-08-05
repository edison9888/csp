<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.biz.DataInterface"%>
<%
	String app_name = (String)request.getParameter("app_name");
	String collect_date = (String)request.getParameter("collect_date");
	
	out.print(DataInterface.getReuqestUrl(app_name, collect_date));
%>
