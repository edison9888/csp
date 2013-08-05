<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.time.util.TairKeyValidator"%>
<%
    String appName = (String)request.getParameter("appName");
    String keyName = (String)request.getParameter("keyName");
%>
<div class="tair-wapper"><%=TairKeyValidator.getTairKeyDescByAppNameAndKeyName(appName, keyName) %></div>