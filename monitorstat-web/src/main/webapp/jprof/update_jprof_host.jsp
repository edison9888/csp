<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorJprofAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
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

</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();

String id = request.getParameter("id");
String action = request.getParameter("action");
JprofHost h = MonitorJprofAo.get().getJprofHosts(Integer.parseInt(id));

if("update".equals(action)){
	
	if(!UserPermissionCheck.check(request,"jprof","")){
		out.print("你没有权限操作!");
		return;
	}
	
	String appNameSelect = request.getParameter("appNameSelect");	
	String hostIp = request.getParameter("hostIp");
	String hostUser = request.getParameter("hostUser");
	String hostPasswd = request.getParameter("hostPasswd");
	String filePath = request.getParameter("filePath");
	String runtype = request.getParameter("runtype");
	JprofHost host = new JprofHost();
	host.setId(Integer.parseInt(id));
	host.setRunType(Integer.parseInt(runtype));
	host.setAppName(appNameSelect);
	host.setFilePath(filePath);
	host.setHostIp(hostIp);
	host.setHostPasswd(hostPasswd);
	host.setHostUser(hostUser);
	boolean b = MonitorJprofAo.get().updateJprofHost(host);
	
	out.print(b?"修改成功":"修改失败");
	
	out.print("<input type='button' value='返回机器列表' onclick=\"location.href='manage_jprof_host.jsp'\">");
}else{


%>
<form action="./update_jprof_host.jsp" method="post">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >jprof 机器列表</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content" align="center">	
	<tr align="center">
		<td >应用名:<input type="text" name="appNameSelect" value="<%=h.getAppName() %>" size="40"/>	</td>
	</tr>
	<tr align="center">		
		<td >机器IP:<input type="text" name="hostIp" value="<%=h.getHostIp() %>" size="40"/></td>			
	</tr>
	<tr align="center">
		<td >用户名:<input type="text" name="hostUser" value="<%=h.getHostUser() %>" size="40"/></td>	
	</tr>
	<tr align="center">
		<td >用户名:<input type="text" name="hostPasswd" value="<%=h.getHostPasswd() %>" size="40"/></td>	
	</tr>
	<tr align="center">
		<td >文件路径:<input type="text" name="filePath" value="<%=h.getFilePath() %>" size="40"/></td>	
	</tr>
	<tr align="center">
		<td >
		<input type="hidden" name="runtype" value="<%=h.getRunType() %>" size="40"/>
		<input type="hidden" name="id" value="<%=h.getId() %>" size="40"/>
		<input type="hidden" name="action" value="update"/><input type="submit" value="提交"/></td>	
	</tr>
</table>
</div>
</div>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>

</html>