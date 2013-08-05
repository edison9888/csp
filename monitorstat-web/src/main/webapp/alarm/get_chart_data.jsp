<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.time.ao.HistoryHSFQueryAo"%>
<%
	try{
	    String appName = (String)request.getParameter("appName");
	    String keyName = (String)request.getParameter("keyName");
	    out.print(HistoryHSFQueryAo.get().queryFlashData(appName, keyName));
	} catch( Exception e){
		out.print(e.getStackTrace());
	}
%>
