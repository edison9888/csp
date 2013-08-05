<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>

<style type="text/css">
div {
	font-size: 12px;
}
table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>

<%
	String appId = request.getParameter("appId");
	if(appId == null){
		appId = "1";
	}
	String key = request.getParameter("key");
	if(key == null){
		key = "qps";
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	
	String collectTime1 =sdf1.format(sdf.parse(request.getParameter("collectTime1"))) ;
	AppInfoPo appInfopo = AppCache.get().getKey(Integer.parseInt(appId));	
	
%>
</head>
<body>


<%if(key.equals("qps") || key.equals("rt")){ %>
<jsp:include page="../head.jsp"></jsp:include>
<%if(appInfopo!=null&&appInfopo.getFeature()!=null&&appInfopo.getFeature().indexOf("pv")>-1){  %>
	<iframe src="<%=request.getContextPath () %>/time/flash_show_pv.jsp?appId=<%=appInfopo.getAppId()%>&appName=<%=appInfopo.getAppName() %>&collectTime1=<%=collectTime1 %>" frameborder="0" height="370" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
	<table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>
<%}else{
	if(appInfopo.getAppName().equals("ic")||appInfopo.getAppName().equals("shopcenter")||appInfopo.getAppName().equals("tbuic")||appInfopo.getAppName().equals("tc")){
%>	
	<iframe src="<%=request.getContextPath () %>/time/flash_show_pv.jsp?appId=<%=appInfopo.getAppId()%>&appName=<%=appInfopo.getAppName() %>&collectTime1=<%=collectTime1 %>" frameborder="0" height="370" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
	<table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>
<%}else{
    response.sendRedirect("../time/app_time.jsp?appName="+appInfopo.getAppName());
  	}
  } 
}else if(key.equals("fgc")){
	%>
	<%String message = RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/time/key_detail_time_table.jsp?keyId=167&appName="+appInfopo.getAppName()+"&aimName=FullGC&collectTime1="+collectTime1+""); %>
	<%if(message.length()<6000000){ 
		response.sendRedirect("../time/app_time.jsp?appName="+appInfopo.getAppName());
	}else{ %>
		<%=message %>
	<%} %>
	<table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>
<%
}else if(key.equals("cpu")){
%>
	<%=RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/time/key_detail_time.jsp?appId="+appInfopo.getAppId()+"&keyId=3113&appName="+appInfopo.getAppName()+"&aimName=Cpu&collectTime1="+collectTime1) %>   
   <table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>
<%	
}else if(key.equals("load")  ){ 
%>
	<%=RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/time/key_detail_time.jsp?appId="+appInfopo.getAppId()+"&keyId=944&appName="+appInfopo.getAppName()+"&aimName=load&collectTime1="+collectTime1) %>
   <table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>
<%}else if(key.equals("jvm")){ 
%>
	<% String message = RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/time/key_detail_time.jsp?appId="+appInfopo.getAppId()+"&keyId=7621&appName="+appInfopo.getAppName()+"&aimName=JVM_Memory-JvmUseRate&collectTime1="+collectTime1); %>
   <%if(message.length()<6000000){ 
		response.sendRedirect("../time/app_time.jsp?appName="+appInfopo.getAppName());
	}else{ %>
		<%=message %>
	<%} %>
   <table width="100%"><tr><td align="center"><a href="<%=request.getContextPath()%>/time/app_time.jsp?appName=<%=appInfopo.getAppName()%>&collectTime1=<%=collectTime1 %>"><strong><u>More……</u></strong></a></td></tr></table>

<%}else{
	response.sendRedirect("../time/app_time.jsp?appName="+appInfopo.getAppName());
}%>

</body>
</html>