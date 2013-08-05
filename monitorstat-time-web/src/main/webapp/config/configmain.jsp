<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.management.MBeanOperationInfo"%>
<%@page import="javax.management.MBeanInfo"%>
<%@page import="javax.management.MBeanServerFactory"%>
<%@page import="javax.management.MBeanServer"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="com.taobao.common.smonitor.MBeanServerService"%>
<%@page import="javax.management.ObjectName"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<title>配置主界面</title>
</head>
<body>
<table  class="table table-striped table-condensed table-bordered" style="table-layout: fixed">
<tr>
	<td>快速添加一个URL</td>
	<td><a href="<%=request.getContextPath()%>/config/addurl.jsp" target="_blank"><%=request.getContextPath()%>/config/addurl.jsp</a></td>
</tr>
<tr>
	<td>修改url，刷新采集缓存</td>
	<td><a href="<%=request.getContextPath()%>/config/changeurl.jsp" target="_blank"><%=request.getContextPath()%>/config/changeurl.jsp</a></td>
</tr>
<tr>
	<td>查看当前数据源配置(<strong style="color: red">目前有错500</strong>)</td>
	<td><a href="<%=request.getContextPath()%>/config/a.jsp" target="_blank"><%=request.getContextPath()%>/config/a.jsp</a></td>
</tr>
<tr>
	<td>为实时采集端添加app</td>
	<td><a href="<%=request.getContextPath()%>/config/addapp.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/addapp.jsp?appName=</a></td>
</tr>
<tr>
	<td>为实时采集端删除app</td>
	<td><a href="<%=request.getContextPath()%>/config/deleteApp.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/deleteApp.jsp?appName=</a></td>
</tr>
<tr>
	<td>addhsfrule</td>
	<td><a href="<%=request.getContextPath()%>/config/addhsfrule.jsp" target="_blank"><%=request.getContextPath()%>/config/addhsfrule.jsp</a></td>
</tr>
<tr>
	<td>计算某个应用的基线数据</td>
	<td><a href="<%=request.getContextPath()%>/config/appbaseline.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/appbaseline.jsp?appName=</a></td>
</tr>
<tr>
	<td>查看基线数据(参数:appName,keyName,propertyName)</td>
	<td><a href="<%=request.getContextPath()%>/config/basecheck.jsp?appName=&keyName=&propertyName=" target="_blank"><%=request.getContextPath()%>/config/basecheck.jsp?appName=&keyName=&propertyName=</a></td>
</tr>
<tr>
	<td>计算重要app的基线(<strong style="color: red">app列表都注释了，现在列表size为0</strong>)</td>
	<td><a href="<%=request.getContextPath()%>/config/baseline.jsp" target="_blank"><%=request.getContextPath()%>/config/baseline.jsp</a></td>
</tr>
<tr>
	<td>打印出采集端存在，但是应用缓存不存在的信息</td>
	<td><a href="<%=request.getContextPath()%>/config/c_app_monitor.jsp" target="_blank"><%=request.getContextPath()%>/config/c_app_monitor.jsp</a></td>
</tr>
<tr>
	<td>清空基线的缓存</td>
	<td><a href="<%=request.getContextPath()%>/config/clean_basecache.jsp" target="_blank"><%=request.getContextPath()%>/config/clean_basecache.jsp</a></td>
</tr>
<tr>
	<td>从opsfree那边同步机器信息</td>
	<td><a href="<%=request.getContextPath()%>/config/runhostcache.jsp" target="_blank"><%=request.getContextPath()%>/config/runhostcache.jsp</a></td>
</tr>
<tr>
	<td>启动AlarmKeyContainer</td>
	<td><a href="<%=request.getContextPath()%>/config/t.jsp" target="_blank"><%=request.getContextPath()%>/config/t.jsp</a></td>
</tr>
<tr>
	<td>time添加一个应用</td>
	<td><a href="<%=request.getContextPath()%>/test/addapp.jsp?appName=smt" target="_blank"><%=request.getContextPath()%>/test/addapp.jsp?appName=smt</a></td>
</tr>
<tr>
	<td>time修改一个应用</td>
	<td><a href="<%=request.getContextPath()%>/test/changeapp.jsp?appName=smt" target="_blank"><%=request.getContextPath()%>/test/changeapp.jsp?appName=smt</a></td>
</tr>
<tr>
	<td>修改导航</td>
	<td><a href="<%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi" target="_blank"><%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi</a></td>
</tr>
<tr>
	<td>模板信息配置</td>
	<td><a href="<%=request.getContextPath()%>/analyseconfig.do?appId=1&method=editAppNavi" target="_blank"><%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi</a></td>
</tr>
<tr>
	<td>删除ZK节点</td>
	<td><a href="<%=request.getContextPath()%>/config/deletezknode.jsp?path=you" target="_blank"><%=request.getContextPath()%>/config/deletezknode.jsp?path=you</a></td>
</tr>
<tr>
	<td>清空菜单的缓存</td>
	<td><a href="<%=request.getContextPath()%>/config/clear_menu.jsp" target="_blank"><%=request.getContextPath()%>/config/clear_menu.jsp</a></td>
</tr>
<tr>
	<td>清空网址的缓存</td>
	<td><a href="<%=request.getContextPath()%>/config/clear_urlcache.jsp" target="_blank"><%=request.getContextPath()%>/config/clear_urlcache.jsp</a></td>
</tr>
</table>


</body>
</html>