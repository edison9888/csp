<%@ page import="com.taobao.monitor.web.schedule.GetWebWWOnlineNumber" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: rentong
  Date: 2010-7-26
  Time: 14:37:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    String n = request.getParameter("number");
    int oldNumber = GetWebWWOnlineNumber.getInstance().getDirtyNumber();
    if (n != null && StringUtils.isNumeric(n)) {
        int number = Integer.parseInt(n);
        GetWebWWOnlineNumber.getInstance().setDirtyNumber(number);
    }
    int newNumber = GetWebWWOnlineNumber.getInstance().getDirtyNumber();
%>
<html>
<head><title>更新WEB旺旺</title></head>
<body>
<%
    if (oldNumber != newNumber) {
        out.print("<div>更新成功：</div>");
        out.print("<div>新：" + newNumber + "</div>");
        out.print("<div>旧：" + oldNumber + "</div>");
    }else{
        out.print("<div>数据：" + oldNumber + "</div>");
    }

%>
<div>
<form action="#" method="post">
    更新不准确数据：
    <input type="text" name="number" value="">
    <input type="submit" value="提交">
</form></div>
</body>
</html>