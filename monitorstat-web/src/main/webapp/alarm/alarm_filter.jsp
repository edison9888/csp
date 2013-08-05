<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.alarm.n.filter.FilterService"%>
<%@page import="com.taobao.monitor.alarm.n.filter.AppTmpStopFilter"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>监控</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
</head>
<body>
<%

String b = request.getParameter("filter");
String appName = request.getParameter("appName");
String time = request.getParameter("time");
String appId = request.getParameter("appId");

if(b!=null&&appName!=null&&time!=null){	
	long t = Long.parseLong(time)*60*1000+System.currentTimeMillis();	
	FilterService.get().register(new AppTmpStopFilter(Integer.parseInt(appId),System.currentTimeMillis(),t));
}

List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();

%>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>
<jsp:include page="../left.jsp"></jsp:include>
<tr><td><table class="datalist"  width="1000">
    <tr class="ui-widget-header ">
		<td>应用名称</td>
		<td>当前是否告警</td>
		<td>操作</td>	
	</tr>
	<%
	for(AppInfoPo po:listApp){
	%>
	<tr>
		<td><%=po.getAppName() %></td>
		<td></td>
		<td>
		<a href="alarm_filter.jsp?filter=false&appName=<%=po.getAppName() %>&time=30&appId=<%=po.getAppId() %>">暂停30分钟告警</a>
		<a href="alarm_filter.jsp?filter=false&appName=<%=po.getAppName() %>&time=60&appId=<%=po.getAppId() %>">暂停60分钟告警</a>
		<a href="alarm_filter.jsp?filter=false&appName=<%=po.getAppName() %>&time=120&appId=<%=po.getAppId() %>">暂停120分钟告警</a>
		<a href="alarm_filter.jsp?filter=false&appName=<%=po.getAppName() %>&time=360&appId=<%=po.getAppId() %>">暂停300分钟告警</a>
		<a href="alarm_filter.jsp?filter=false&appName=<%=po.getAppName() %>&time=600&appId=<%=po.getAppId() %>">暂停600分钟告警</a>
		</td>	
	</tr>
	
	<%} %>	
</table></td></tr>
<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
	<tr><td><jsp:include page="../bottom.jsp"></jsp:include></td></tr>
</body>
</html>