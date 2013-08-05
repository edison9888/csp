<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import=" com.taobao.monitor.common.util.GroupManager"%>
<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@page import="com.taobao.csp.time.web.po.*"%>
<%@page import="com.taobao.csp.dataserver.*"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.common.ao.center.*"%>
<%@page import=" com.taobao.csp.dataserver.query.QueryUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.taobao.csp.time.util.*"%>
<%@page import="com.taobao.monitor.common.util.*"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<!doctype html>
<html>
        <head>
                <title>≤‚ ‘</title>
</head>
<body>
<%
Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime("chongzhideliver", "chongzhide");
out.println(map);
	
%>
</body>
