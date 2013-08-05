<%@page import="java.io.IOException"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>


<%
JSONObject jsonData = (JSONObject)request.getAttribute("jsonData");
try {
		response.getWriter().write(jsonData.toString());
		response.flushBuffer();
	} catch (IOException e) {
	}	
	return ;	
%>