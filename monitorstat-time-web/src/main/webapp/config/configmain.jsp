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
<title>����������</title>
</head>
<body>
<table  class="table table-striped table-condensed table-bordered" style="table-layout: fixed">
<tr>
	<td>�������һ��URL</td>
	<td><a href="<%=request.getContextPath()%>/config/addurl.jsp" target="_blank"><%=request.getContextPath()%>/config/addurl.jsp</a></td>
</tr>
<tr>
	<td>�޸�url��ˢ�²ɼ�����</td>
	<td><a href="<%=request.getContextPath()%>/config/changeurl.jsp" target="_blank"><%=request.getContextPath()%>/config/changeurl.jsp</a></td>
</tr>
<tr>
	<td>�鿴��ǰ����Դ����(<strong style="color: red">Ŀǰ�д�500</strong>)</td>
	<td><a href="<%=request.getContextPath()%>/config/a.jsp" target="_blank"><%=request.getContextPath()%>/config/a.jsp</a></td>
</tr>
<tr>
	<td>Ϊʵʱ�ɼ������app</td>
	<td><a href="<%=request.getContextPath()%>/config/addapp.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/addapp.jsp?appName=</a></td>
</tr>
<tr>
	<td>Ϊʵʱ�ɼ���ɾ��app</td>
	<td><a href="<%=request.getContextPath()%>/config/deleteApp.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/deleteApp.jsp?appName=</a></td>
</tr>
<tr>
	<td>addhsfrule</td>
	<td><a href="<%=request.getContextPath()%>/config/addhsfrule.jsp" target="_blank"><%=request.getContextPath()%>/config/addhsfrule.jsp</a></td>
</tr>
<tr>
	<td>����ĳ��Ӧ�õĻ�������</td>
	<td><a href="<%=request.getContextPath()%>/config/appbaseline.jsp?appName=" target="_blank"><%=request.getContextPath()%>/config/appbaseline.jsp?appName=</a></td>
</tr>
<tr>
	<td>�鿴��������(����:appName,keyName,propertyName)</td>
	<td><a href="<%=request.getContextPath()%>/config/basecheck.jsp?appName=&keyName=&propertyName=" target="_blank"><%=request.getContextPath()%>/config/basecheck.jsp?appName=&keyName=&propertyName=</a></td>
</tr>
<tr>
	<td>������Ҫapp�Ļ���(<strong style="color: red">app�б�ע���ˣ������б�sizeΪ0</strong>)</td>
	<td><a href="<%=request.getContextPath()%>/config/baseline.jsp" target="_blank"><%=request.getContextPath()%>/config/baseline.jsp</a></td>
</tr>
<tr>
	<td>��ӡ���ɼ��˴��ڣ�����Ӧ�û��治���ڵ���Ϣ</td>
	<td><a href="<%=request.getContextPath()%>/config/c_app_monitor.jsp" target="_blank"><%=request.getContextPath()%>/config/c_app_monitor.jsp</a></td>
</tr>
<tr>
	<td>��ջ��ߵĻ���</td>
	<td><a href="<%=request.getContextPath()%>/config/clean_basecache.jsp" target="_blank"><%=request.getContextPath()%>/config/clean_basecache.jsp</a></td>
</tr>
<tr>
	<td>��opsfree�Ǳ�ͬ��������Ϣ</td>
	<td><a href="<%=request.getContextPath()%>/config/runhostcache.jsp" target="_blank"><%=request.getContextPath()%>/config/runhostcache.jsp</a></td>
</tr>
<tr>
	<td>����AlarmKeyContainer</td>
	<td><a href="<%=request.getContextPath()%>/config/t.jsp" target="_blank"><%=request.getContextPath()%>/config/t.jsp</a></td>
</tr>
<tr>
	<td>time���һ��Ӧ��</td>
	<td><a href="<%=request.getContextPath()%>/test/addapp.jsp?appName=smt" target="_blank"><%=request.getContextPath()%>/test/addapp.jsp?appName=smt</a></td>
</tr>
<tr>
	<td>time�޸�һ��Ӧ��</td>
	<td><a href="<%=request.getContextPath()%>/test/changeapp.jsp?appName=smt" target="_blank"><%=request.getContextPath()%>/test/changeapp.jsp?appName=smt</a></td>
</tr>
<tr>
	<td>�޸ĵ���</td>
	<td><a href="<%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi" target="_blank"><%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi</a></td>
</tr>
<tr>
	<td>ģ����Ϣ����</td>
	<td><a href="<%=request.getContextPath()%>/analyseconfig.do?appId=1&method=editAppNavi" target="_blank"><%=request.getContextPath()%>/app/config.do?appId=1&method=editAppNavi</a></td>
</tr>
<tr>
	<td>ɾ��ZK�ڵ�</td>
	<td><a href="<%=request.getContextPath()%>/config/deletezknode.jsp?path=you" target="_blank"><%=request.getContextPath()%>/config/deletezknode.jsp?path=you</a></td>
</tr>
<tr>
	<td>��ղ˵��Ļ���</td>
	<td><a href="<%=request.getContextPath()%>/config/clear_menu.jsp" target="_blank"><%=request.getContextPath()%>/config/clear_menu.jsp</a></td>
</tr>
<tr>
	<td>�����ַ�Ļ���</td>
	<td><a href="<%=request.getContextPath()%>/config/clear_urlcache.jsp" target="_blank"><%=request.getContextPath()%>/config/clear_urlcache.jsp</a></td>
</tr>
</table>


</body>
</html>