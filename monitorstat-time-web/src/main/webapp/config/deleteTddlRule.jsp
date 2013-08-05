<%@ page import="com.taobao.wwnotify.web.StringUtil" %>
<%@ page import="com.taobao.csp.alarm.tddl.TddlChecker" %>
<%--
  Created by IntelliJ IDEA.
  User: shuquan.ljh
  Date: 12-8-13
  Time: 下午8:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    String appName = request.getParameter("appName");
    String dbName = request.getParameter("dbName");
    if(StringUtil.isNotBlank(appName) && StringUtil.isNotBlank(dbName)) {
        TddlChecker.deleteByAppName(appName, dbName);
    }
    response.sendRedirect("./addTddlRule.jsp");
%>

</body>
</html>