<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.report.PhoneOut"%>
<%@page import="com.taobao.monitor.web.util.Config"%>
<head>
<body>
<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -1);
        PhoneOut.sendPhone(parseLogFormatDate.format(cal.getTime()),Config.getValue("monitor.phone"));
  %>

</body>
</html>