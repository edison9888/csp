<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.config.po.LoginUserPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户管理</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/ui.all.css" rel="stylesheet" />

<style type="text/css">
	body {
	  padding-top: 60px;
	}
#phone input {
	width: 80px;
}

#wangwang input {
	width: 80px;
}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery-ui-1.8.custom.min.js"></script>
<%
LoginUserPo po = (LoginUserPo)request.getAttribute("loginUserPo");
String[] week = (String[])request.getAttribute("week");
String[] num = (String[])request.getAttribute("num");
String[] week1 = (String[])request.getAttribute("week1");
String[] num1 = (String[])request.getAttribute("num1");
List<AppInfoPo> appList = (ArrayList<AppInfoPo>)request.getAttribute("appList");
String initAppKeyRel = (String)request.getAttribute("initAppKeyRel");
String initSelectedApp = (String)request.getAttribute("initSelectedApp");
%>

</head>
<body>
<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
	<form action="<%=request.getContextPath() %>/show/UserConf.do?method=updateUser&id=<%=po.getId() %>" method="post">
		<div class="page-header">
			<h2>
				用户配置更新
			</h2>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<th class="blue">用户名</th>
				<th class="blue">旺旺号</th>
				<th class="blue">手机号</th>
				<th class="blue">邮箱</th>
			</tr>
			<tr>
				<th><%=po.getName() %></th>
				<input type="hidden" name="name" value="<%=po.getName() %>">
				<th><input type="text" name="wangwang" value="<%=po.getWangwang() %>"></th>
				<th><input type="text" name="phone" value="<%=po.getPhone() %>"></th>
				<th><input type="text" name="mail" value="<%=po.getMail() %>"></th>
			</tr>	
		</table>
		
		<table class="zebra-striped condensed-table bordered-table">
		<tr>
			<th class="blue" colspan="4">权限说明</th>
		</tr>
		<tr>
			<td colspan="4"><input type="text" name="permissionDesc" style="width:1050px" value="<%=po.getPermissionDesc() %>"></td>
		</tr>
		</table>
		<div class="page-header">
			<h4>
				告警接收信息方式
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table" id="phone">
			<tr>
				<th class="blue">手机报警</th>
				<th class="blue">周一</th>
				<th class="blue">周二</th>
				<th class="blue">周三</th>
				<th class="blue">周四</th>
				<th class="blue">周五</th>
				<th class="blue">周六</th>
				<th class="blue">周日</th>
			</tr>
			<tr>
				<td class="blue">报警时间范围</td>
				<td><input type="text" name="phone_week_2"  value="<%=week[1]==null?"":week[1] %>"></td>
				<td><input type="text" name="phone_week_3"  value="<%=week[2]==null?"":week[2] %>"></td>
				<td><input type="text" name="phone_week_4"  value="<%=week[3]==null?"":week[3] %>"></td>
				<td><input type="text" name="phone_week_5"  value="<%=week[4]==null?"":week[4] %>"></td>
				<td><input type="text" name="phone_week_6"  value="<%=week[5]==null?"":week[5] %>"></td>
				<td><input type="text" name="phone_week_7"  value="<%=week[6]==null?"":week[6] %>"></td>
				<td><input type="text" name="phone_week_1"  value="<%=week[0]==null?"":week[0] %>"></td>
			</tr>
			<tr>
				<td class="blue">报警几次后发送</td>
				<td><input type="text" name="phone_num_2"  value="<%=num[1]==null?"":num[1] %>" size="2"></td>
				<td><input type="text" name="phone_num_3"  value="<%=num[2]==null?"":num[2] %>" size="2"></td>
				<td><input type="text" name="phone_num_4"  value="<%=num[3]==null?"":num[3] %>" size="2"></td>
				<td><input type="text" name="phone_num_5"  value="<%=num[4]==null?"":num[4] %>" size="2"></td>
				<td><input type="text" name="phone_num_6"  value="<%=num[5]==null?"":num[5] %>" size="2"></td>
				<td><input type="text" name="phone_num_7"  value="<%=num[6]==null?"":num[6] %>" size="2"></td>
				<td><input type="text" name="phone_num_1"  value="<%=num[0]==null?"":num[0] %>" size="2"></td>
			</tr>
		</table>
		<table class="zebra-striped condensed-table bordered-table" id="wangwang">
		<thead>
			<tr>
				<th class="blue">旺旺报警</th>
				<th class="blue">周一</th>
				<th class="blue">周二</th>
				<th class="blue">周三</th>
				<th class="blue">周四</th>
				<th class="blue">周五</th>
				<th class="blue">周六</th>
				<th class="blue">周日</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="blue">报警时间范围</td>
				<td><input type="text" name="wangwang_week_2"  value="<%=week1[1]==null?"":week1[1] %>"></td>
				<td><input type="text" name="wangwang_week_3"  value="<%=week1[2]==null?"":week1[2] %>"></td>
				<td><input type="text" name="wangwang_week_4"  value="<%=week1[3]==null?"":week1[3] %>"></td>
				<td><input type="text" name="wangwang_week_5"  value="<%=week1[4]==null?"":week1[4] %>"></td>
				<td><input type="text" name="wangwang_week_6"  value="<%=week1[5]==null?"":week1[5] %>"></td>
				<td><input type="text" name="wangwang_week_7"  value="<%=week1[6]==null?"":week1[6] %>"></td>
				<td><input type="text" name="wangwang_week_1"  value="<%=week1[0]==null?"":week1[0] %>"></td>
			</tr>
			<tr>
				<td class="blue">报警几次后发送</td>
				<td><input type="text" name="wangwang_num_2"  value="<%=num1[1]==null?"":num1[1] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_3"  value="<%=num1[2]==null?"":num1[2] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_4"  value="<%=num1[3]==null?"":num1[3] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_5"  value="<%=num1[4]==null?"":num1[4] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_6"  value="<%=num1[5]==null?"":num1[5] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_7"  value="<%=num1[6]==null?"":num1[6] %>" size="2"></td>
				<td><input type="text" name="wangwang_num_1"  value="<%=num1[0]==null?"":num1[0] %>" size="2"></td>
			</tr>
		</tbody>
		</table>
		
		<div class="page-header">
			<h4>
				选择需要告警应用
				<input class="btn primary pull-right" type="button" value="选择应用" onClick="openAppSelect()">
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table" id="selectAppTable">
			<thead>
				<tr>
					<th class="blue">应用名</th>
					<th class="blue">操作</th>
				</tr>
			</thead>
			<tbody>
				<%
				for(AppInfoPo app : appList) {
					%>
					<tr id="<%=app.getAppId() %>">
						<td align='center'><%=app.getAppName() %></td>
						<td><a href="javascript:deleteTableTr('<%=app.getAppId() %>')">删除</a></td>
					</tr>
					<%
				}
				%>
			</tbody>
		</table>
		
		<!-- 添加告警应用的弹出框 -->
		<div id="dialog_appRel_add" title="dialog">
			<iframe id="iframe_appRel_add" src="" frameborder="0" height="670" width="780" marginheight="0" marginwidth="0" scrolling="yes" ></iframe>
		</div>
<script type="text/javascript">

$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
//添加告警应用
function openAppSelect () {
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/show/UserConf.do?method=gotoRelAppAdd&selectedApp="+$("#selectApp").val());
	$("#dialog_appRel_add").dialog("open");
}

function deleteTableTr(appId){

	$("#" + appId).remove();
	var appStrs = $("#selectApp").val().split(",");
	var newAppStr = "";
	for(var i=0; i < appStrs.length-1; i++) {			//用；切分后的数组最后会是空白字符串
	
		if(appStrs[i] != appId) {			//app[0]是应用的Id
			newAppStr += appStrs[i] + ",";
		}
	}
	$("#selectApp").val(newAppStr);

}

function goback1() {
	location.href="<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserConf";
}
</script>		
		<div class="actions">
			<input type="hidden" id="selectApp" name="selectApp" value="<%=initSelectedApp %>">
			<center>
				<input type="submit" class="btn primary" value="提交更新" >
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" onclick="goback1()" value="返回" >
			</center>
		</div>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
		</form>
	</div>
	
</div>

</body>
</html>