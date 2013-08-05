<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 

<style type="text/css">

body {
	font-size: 62.5%;
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
}

.report_on{background:#bce774;}

</style>
</head>
<body>

<script type="text/javascript">

function closeDialog(){

	parent.$("#dialog_appRel_add").dialog("close");
}

</script>

<%
/*
request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>

<form action="" method="post">
<!-- 这个jsp是用户关联那些app的弹出框的页面 -->
<% 
String allRel = request.getParameter("allRel");		//获取到报表和应用的拼接字符串
String reportId = request.getParameter("reportId");

String[] reportAndApps = allRel.split(";");			//9:34,54;7:23,53;
List<String> appNameList = new ArrayList<String>(); 
for(String s : reportAndApps) {
	
	String[] a = s.split(":");	//9:34,54
	if(a[0].equals(reportId)) {
		
		String[] b = a[1].split(",");	//34,54
		for(String c : b) {
			appNameList.add(AppCache.get().getKey(Integer.parseInt(c)).getAppName());
		}
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 95%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td align="center">应用名</td>
  </tr>  
  	<%

	for(String appName:appNameList){

		
	%> 	
		<tr>
		<td align="center"><%=appName %></td>
	</tr>
		
	<%} %>
	</table>
</div>


</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="button" onclick="closeDialog()" value="关闭">
		</td>
	</tr>
</table>	
</form>

</body>
</html>