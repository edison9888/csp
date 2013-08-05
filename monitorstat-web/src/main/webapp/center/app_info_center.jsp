<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-应用信息配置</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
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
}
.report_on{background:#bce774;}
</style>
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#userInfoTableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#userInfoTableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})

		$(".deleteAction").click(function(){
			return confirm("想清楚要删除了没有啊");
		});
	})
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>

<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
	return;
}
%>

<%

String appId = request.getParameter("appId");

String action = request.getParameter("action");
if("delete".equals(action)){
	AppInfoAo.get().ModifyAppStatus(1,Integer.parseInt(appId));
}

else if("cancel".equals(action)){
	AppInfoAo.get().ModifyAppStatus(0,Integer.parseInt(appId));
}
%>

<input type="button" value="添加新应用" onclick="location.href='./app_info_add.jsp'">
<input type="button" value="返回" onclick="location.href='./manage_center.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">应用列表</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
List<AppInfoPo> listAppInfo = AppInfoAo.get().findAllAppInfo();
Collections.sort(listAppInfo);
%>
<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
		<td align="center" width="100">应用ID</td>
		<td align="center" width="100">应用名</td>
		<td align="center" width="100">Feature</td>
		<td align="center" width="100">ops名称</td>
		<td align="center" width="100">分组名称</td>
		<td align="center" width="100">状态</td>
		<td align="center">  操作</td>
	</tr>
	<%
	for(AppInfoPo po: listAppInfo){ 
		
	%>
	<tr >
		<td align="center"><%=po.getAppId()%></td>
		<td align="center"><%=po.getAppName()%></td>
		<td align="center"><%=po.getFeature() %></td>
		<td align="center"><%=po.getOpsName()%></td>
		<td align="center"><%=po.getGroupName()%></td>
		<td align="center"><%=po.getAppStatus() == 0 ? "正常" : "删除"%></td>
		<%
			if(po.getAppStatus() == 0) {
		%>
		<td align="center">
			<a href="./app_info_update.jsp?id=<%=po.getAppId() %>">修改</a>&nbsp;&nbsp;
			<a id="deleteAction" class="deleteAction" "href="./app_info_center1.jsp?appId=<%=po.getAppId() %>&action=delete">删除</a> &nbsp;&nbsp;
			<a href="./app_info_check.jsp?appId=<%=po.getAppId() %>">查看</a> &nbsp;&nbsp;
			<a target="_blank" href="./rel_app_day.jsp?appId=<%=po.getAppId() %>&appName=<%=po.getAppName() %>">日报</a>&nbsp;&nbsp;
			<a target="_blank" href="./rel_app_time.jsp?appId=<%=po.getAppId() %>&appName=<%=po.getAppName() %>">实时</a>&nbsp;&nbsp;
		</td>
		
		<%
			} else {
		%>
		<td align="center">
			<a href="./app_info_center1.jsp?appId=<%=po.getAppId() %>&action=cancel">取消删除</a> &nbsp;&nbsp;
		</td>
		
		<%
			}
		%>
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>