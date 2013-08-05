<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.taobao.moinitor.matrix.*" %>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*" %>
<%@page import="com.taobao.moinitor.matrix.util.Utils"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>test</title>
</head>
<body>
<%--
	QpsDayData qpsDayData = new QpsDayData();
	qpsDayData.setAppName("matrix_apps");
	qpsDayData.setCount(15392384L);
	qpsDayData.setDayDate(Utils.zerolizedTime(new Date()));
	qpsDayData.setGmtCreated(new Date());
	qpsDayData.setGmtModified(new Date());
	qpsDayData.setIsDeteted((char)'n');
	if(QpsDayDataAO.instance.insertQpsDayData(qpsDayData)>0){
		out.println("success");
	}else{
		out.println("failed");
	}
--%>
<%
	EmailSupport emailSupport = new EmailSupport();
	emailSupport.sendEmail();
%>
</body>
</html>