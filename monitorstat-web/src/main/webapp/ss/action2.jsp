<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.ss.SsdetailReportManager2"%>
<%@page import="com.taobao.monitor.web.ss.StableSwitch"%>
<%@page import="com.taobao.monitor.web.ss.StableSwitchGroup"%>
<%
String checkcode = request.getParameter("checkcode").trim();
if (!"dev123".equals(checkcode)) {
    out.print("checkCode error!");
    return ;
}
String[] keys = request.getParameterValues("key");
String[] countvalves = request.getParameterValues("countvalve");
String[] avgvalves = request.getParameterValues("avgvalve");
String[] types = request.getParameterValues("type");
String hostgroup = request.getParameter("hostgroup");
String app = request.getParameter("app");
StringBuffer command = new StringBuffer();
for (int i = 0; i < keys.length; i++) {
    try {
    if (!countvalves[i].trim().isEmpty()) {
        command.append(keys[i] + ".cv=" + Integer.parseInt(countvalves[i].trim()));
        command.append("&");
    }
    if (!avgvalves[i].trim().isEmpty()) {
        command.append(keys[i] + ".av=" + Integer.parseInt(avgvalves[i].trim()));
        command.append("&");
    }
    if (!types[i].trim().isEmpty()) {
        command.append(keys[i] + ".t=" + Integer.parseInt(types[i].trim()));
        command.append("&");
    }
    }catch (Exception e) {
        out.print("input format error!");
        return;
    }
   	
}
String commands = command.toString();
if (!commands.isEmpty()) {
    commands = "/command.sph?command=set&" + commands;
    String host = request.getParameter("hosts");
    if (host.trim().isEmpty() && (hostgroup == null ||  hostgroup.isEmpty())){
        out.print("no hosts!");
    }else   {
        String[] hosts = host.split(",");
        out.print("commands:" + commands);
        out.print("<br>");
        String[] results = SsdetailReportManager2.setValue(app, hosts, hostgroup, "7001", commands);
        for (String r : results){
       		out.print(r);
       		out.print("<br>");
        }
    }
    
} else {
    out.print("no command!");
}
%>
<a href="<%=request.getHeader("referer") %>">BACK</a>
<%
//response.sendRedirect("index.jsp?app=");
%>