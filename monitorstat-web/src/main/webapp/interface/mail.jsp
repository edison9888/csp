
<%@page import="com.taobao.monitor.common.messagesend.EmailMessageSend"%>
<%@page import="java.io.IOException"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%

String subject = request.getParameter("subject");
String content = request.getParameter("content");
String emailList = request.getParameter("emailList");
String msg = "fail";
if(subject!=null&&content!=null&&emailList!=null){
	
	EmailMessageSend send = new EmailMessageSend();
	
	send.send(emailList,subject,content);
	
	msg = "ok";	
}
response.setContentType("text/html;charset=gbk"); 
try {
	response.getWriter().write(msg);
	response.flushBuffer();
} catch (IOException e) {
}



%>
