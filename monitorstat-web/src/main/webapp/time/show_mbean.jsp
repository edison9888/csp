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
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<html>
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
	String opsName = request.getParameter("opsName");
	AppInfoPo appInfopo =AppInfoAo.get().getAppInfoByOpsName(opsName);
	appId = appInfopo.getAppId()+"";
}
%>

<script type="text/javascript">
	function mbeanFrame(mBeanType){
			document.getElementById("mbeanFrame").src="show_mbean_common.jsp?appId=<%=appId%>&mBeanType="+mBeanType;
	}
</script>
</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >Mbean数据信息</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		<td width="50%" >
			<table>
				<tr>
					<td><a href="javascript:mbeanFrame('SELF_DataSource_')">数据源</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:mbeanFrame('SELF_ThreadPool_')">线程池</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:mbeanFrame('SELF_Thread_')">线程</a></td>
				</tr>
				<tr>
					<td><iframe id="mbeanFrame" src="show_mbean_common.jsp?appId=<%=appId%>&mBeanType=SELF_DataSource_" frameborder="0" height="340" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe></td>
				</tr>
			</table>
		</td>
		<td width="50%" >
			<iframe src="show_mbean_memory.jsp?appId=<%=appId%>" frameborder="0" height="340" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
		</td>
	</tr>	
</table>
</div>
</div>
</body>
</html>