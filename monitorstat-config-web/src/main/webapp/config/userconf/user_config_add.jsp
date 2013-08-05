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

<script type="text/javascript">
function CheckForm()
{  
	if ($("#name").val().length  ==  0)  {  
		alert("你把用户名给忘填了!");
		$("#name").focus();
	return  false;
	} else if($("#wangwang").val().length == 0) {
		alert("你把旺旺给忘填了!");
		$("#wangwang").focus();
		return false;
	} else if($("#phone").val().length == 0) {
		alert("你把电话给忘填了!");
		$("#phone").focus();
	} else if($("#mail").val().length == 0) {
		alert("你把邮箱给忘填了!");
		$("#mail").focus();
	}
	return  true;
}
</script>
</head>
<body>
<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
	<form action="<%=request.getContextPath() %>/show/UserConf.do?method=addUser" method="post" onsubmit="return CheckForm()">
		<div class="page-header">
			<h2>
				新增用户
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
				<th><input type="text" id="name" name="name" value="" class="register-input"><font class="blue"><div id="note-name">&nbsp;</div></font></th>
				<th><input type="text" id="wangwang" name="wangwang" value="" class="register-input"><font class="blue"><div id="note-wangwang">&nbsp;</div></font></th>
				<th><input type="text" id="phone" name="phone" value="" class="register-input"><font class="blue"><div id="note-phone">&nbsp;</div></font></th>
				<th><input type="text" id="mail" name="mail" value="" class="register-input"><font class="blue"><div id="note-mail">&nbsp;</div></font></th>
			</tr>	
		</table>
		
		<table class="zebra-striped condensed-table bordered-table">
		<tr>
			<th class="blue" colspan="4">权限说明</th>
		</tr>
		<tr>
			<td colspan="4"><input type="text" name="permissionDesc" style="width:1050px" value=""></td>
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
				<td><input type="text" name="phone_week_2"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_3"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_4"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_5"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_6"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_7"  value="00:00#23:59"></td>
				<td><input type="text" name="phone_week_1"  value="00:00#23:59"></td>
			</tr>
			<tr>
				<td class="blue">报警几次后发送</td>
				<td><input type="text" name="phone_num_2"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_3"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_4"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_5"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_6"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_7"  value="3" size="2"></td>
				<td><input type="text" name="phone_num_1"  value="3" size="2"></td>
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
				<td><input type="text" name="wangwang_week_2"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_3"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_4"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_5"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_6"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_7"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_1"  value="00:00#23:59"></td>
			</tr>
			<tr>
				<td class="blue">报警几次后发送</td>
				<td><input type="text" name="wangwang_num_2"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_3"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_4"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_5"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_6"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_7"  value="0" size="2"></td>
				<td><input type="text" name="wangwang_num_1"  value="0" size="2"></td>
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
	//alert("aaaaaaaa");
	location.href="<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserConf";
}

var info={
		'name' : "请输入用户名!",
		'wangwang' : "请输入旺旺!",
		'phone' : "请输入手机号码!",
		'mail' : "请输入mail"
	}

	$(document).ready(function(){

		$("input.register-input").click(function() {
			var id = $(this).attr("id");
			$("#note-"+id).text(info[id]);
			$("#note-"+$(this).attr("id")).css("visibility","visible")
		});

		//重复代码太多，要改，但是改不好
		$("#name").blur(function(){
			$.post('<%=request.getContextPath () %>/user/manage.mc',
					{name:$("#name").val()},
					function(data){

						if(data != "ERROR") {

							if(data.name == "") {
								$("#note-name").text("不允许为空");
								$("#note-name").css("visibility","visible");							
							}
							else if(data.nameExist == "yes") {
								
								$("#note-name").text("已经有人用了");
								$("#note-name").css("visibility","visible");
							} else {

								$("#note-name").text("恭喜，可以用");
								$("#note-name").css("visibility","visible");	
							} 
						}
					
					},
					"json");
		});

		$("#wangwang").blur(function(){
			$.post('<%=request.getContextPath () %>/user/manage.mc',
					{wangwang:$("#wangwang").val()},
					function(data){

						if(data != "ERROR") {

							if(data.wangwang == "") {
								$("#note-wangwang").text("不允许为空");
								$("#note-wangwang").css("visibility","visible");							
							}
							else if(data.wangwangExist == "yes") {
								
								$("#note-wangwang").text("已经有人用了");
								$("#note-wangwang").css("visibility","visible");
							} else {

								$("#note-wangwang").text("恭喜，可以用");
								$("#note-wangwang").css("visibility","visible");	
							} 
						}
					
					},
					"json");
		});

		$("#phone").blur(function(){
			$.post('<%=request.getContextPath () %>/user/manage.mc',
					{phone:$("#phone").val()},
					function(data){

						if(data != "ERROR") {
							var phonePattern =/^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/;     //判断 手机  
							if(data.phone == "") {
								$("#note-phone").text("不允许为空");
								$("#note-phone").css("visibility","visible");							
							}
							else if(phonePattern.test(data.phone) == false) {
								$("#note-phone").text("电话号码格式不对");
								$("#note-phone").css("visibility","visible");	
							}
							else if(data.phoneExist == "yes") {
								
								$("#note-phone").text("已经有人用了");
								$("#note-phone").css("visibility","visible");
							} else {

								$("#note-phone").text("恭喜，可以用");
								$("#note-phone").css("visibility","visible");	
							} 
						}
					
					},
					"json");
		});

		$("#mail").blur(function(){
			$.post('<%=request.getContextPath () %>/user/manage.mc',
					{mail:$("#mail").val()},
					function(data){

						if(data != "ERROR") {
							var emailPattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
						
							if(data.mail == "") {
								$("#note-mail").text("不允许为空");
								$("#note-mail").css("visibility","visible");							
							}
							else if(emailPattern.test(data.mail)== false) {
								
								$("#note-mail").text("email格式不对");
								$("#note-mail").css("visibility","visible");	
							}
							else if(data.mailExist == "yes") {
								
								$("#note-mail").text("已经有人用了");
								$("#note-mail").css("visibility","visible");
							} else{

								$("#note-mail").text("恭喜，可以用");
								$("#note-mail").css("visibility","visible");	
							} 
						}
					
					},
					"json");
		});
	});
</script>		
		<div class="actions">
			<input type="hidden" id="selectApp" name="selectApp" value="">
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