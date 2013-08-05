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
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorExtraKeyAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script>
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
<script type="text/javascript">
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



<%

request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}

%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="user_info_add2.jsp" method="post" onsubmit="return CheckForm()">

<%
List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();

String action = request.getParameter("action");
String selectApp = request.getParameter("selectApp");					//拼接好告警的应用：   appId,appId,appId,
String selectReportApp = request.getParameter("selectReportApp");		//拼接好报表选择的应用：   reportId:appId,appId,appId;
String relKey = request.getParameter("relKey");							//拼接好应用的告警key：   appId:keyId,keyId;
if("add".equals(action)){
	String name = request.getParameter("name");
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String mail =request.getParameter("mail");
	String id =request.getParameter("id");
	String phoneDesc = "";

	
	//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
	for(int i=1;i<=7;i++){
		String phone_week = request.getParameter("phone_week_"+i);
		String phone_num = request.getParameter("phone_num_"+i);
		if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
			phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
		}
	}
	String wangwangDesc = "";
	//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
	for(int i=1;i<=7;i++){
		String ww_week = request.getParameter("wangwang_week_"+i);
		String ww_num = request.getParameter("wangwang_num_"+i);
		if(ww_week!=null&&!"".equals(ww_week.trim())&&ww_num!=null&&!"".equals(ww_num.trim())){
			wangwangDesc+=ww_num+"#"+i+"#"+ww_week+"$";
		}
	}
	
	
	LoginUserPo loginUserPo = new LoginUserPo();
	loginUserPo.setName(name);
	loginUserPo.setPhone(phone);
	loginUserPo.setWangwang(wangwang);
	loginUserPo.setSendPhoneFeature(phoneDesc);
	loginUserPo.setSendWwFeature(wangwangDesc);
	loginUserPo.setMail(mail);
	
	loginUserPo.setGroup(selectApp);
	loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");	//应用的拼接
	
	String report="";
	for(ReportInfoPo info:listReportList){
		int reportId = info.getId();
		if(info.getType()==0){
			String value = request.getParameter("report_"+reportId);
			if(value!=null){
				report += reportId+";";
			}
		}else{
			continue;
		}
	}
	
	report += selectReportApp;		//报表的拼接
	loginUserPo.setReportDesc(report);
	boolean b = MonitorUserAo.get().addLoginUserPo(loginUserPo);
	//===========
	boolean bb = true;
	
	if(b) {
		int userId = MonitorUserAo.get().getLoginUserPo(name).getId();
		
		if(relKey != null && !relKey.equals("")) {
			String[] appKeyStrs = relKey.split(";");
			for(String  appKeyStr: appKeyStrs) {		//appKeyStr的值为  appId:keyId,keyId;
				
				String[] appAndKey = appKeyStr.split(":");
				String app = appAndKey[0];			//这是appId
				String[] keyIds = appAndKey[1].split(",");
				List<String> keyList = new ArrayList<String>();
				for(String key : keyIds) {
					
					keyList.add(key);
				}
				
				bb = MonitorExtraKeyAo.get().addUserAndKeyRel(userId, Integer.parseInt(app),keyList);
		}
		
		}
		%>
		<font size="+3" color="red"><%if(b && bb){out.print("成功");}else{out.print("失败!出现异常");} %></font>	
		<a href="./manage_user.jsp">返回</a>
	<%
	}
}else{
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">用户基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td width="179">用户名:<input id="name" type="text" name="name" class="register-input" value="" width="179"></td>
		<td><font color="red"><div id="note-name">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>旺旺号:<input type="text" id="wangwang" name="wangwang" class="register-input" value=""></td>
		<td><font color="red"><div id="note-wangwang">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>手机号:<input type="text" id="phone" name="phone" class="register-input" value=""></td>
		<td><font color="red"><div id="note-phone">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>邮箱:&nbsp;&nbsp;&nbsp;<input type="text" id="mail" name="mail" class="register-input" value=""></td>
		<td><font color="red"><div id="note-mail">&nbsp;</div></font></td>
	</tr>
</table>
</div>
</div>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息方式</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>
		<table>
			<tr >
				<td>手机接收设置:</td>
			</tr>
			<tr>
				<td>周一:<input type="text" name="phone_week_2"  value="18:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_2"  value="3" size="2">以上发送</td>
				<td>周二:<input type="text" name="phone_week_3"  value="18:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_3"  value="3" size="2">以上发送</td>
				<td>周三:<input type="text" name="phone_week_4"  value="18:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_4"  value="3" size="2">以上发送</td>
				<td>周四:<input type="text" name="phone_week_5"  value="18:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_5"  value="3" size="2">以上发送</td>
				<td>周五:<input type="text" name="phone_week_6"  value="18:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_6"  value="3" size="2">以上发送</td>
				<td>周六:<input type="text" name="phone_week_7"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_7"  value="3" size="2">以上发送</td>
				<td>周日:<input type="text" name="phone_week_1"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="phone_num_1"  value="3" size="2">以上发送</td>
			</tr>			
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
			<tr >
				<td>旺旺接收设置:</td>
			</tr>
			<tr>
				<td>周一:<input type="text" name="wangwang_week_2"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_2"  value="0" size="2">以上发送</td>
				<td>周二:<input type="text" name="wangwang_week_3"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_3"  value="0" size="2">以上发送</td>
				<td>周三:<input type="text" name="wangwang_week_4"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_4"  value="0" size="2">以上发送</td>
				<td>周四:<input type="text" name="wangwang_week_5"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_5"  value="0" size="2">以上发送</td>
				<td>周五:<input type="text" name="wangwang_week_6"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_6"  value="0" size="2">以上发送</td>
				<td>周六:<input type="text" name="wangwang_week_7"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_7"  value="0" size="2">以上发送</td>
				<td>周日:<input type="text" name="wangwang_week_1"  value="00:00#23:59"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_1"  value="0" size="2">以上发送</td>
			</tr>		
		</table>
	</td>
</tr>
</table>
</div>
</div>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">选择需要告警应用
<!-- <button id="btn-add-appRel" onclick="openAppSelect()">选择</button> -->
<input type="button" value="选择应用" onclick="openAppSelect()">
</div>
<!-- 这个table的数据由ajax在子窗口中获得 -->
<table id="selectAppTable" border="1">
	

	
</table>
</div>

<!-- 添加告警应用的弹出框 -->
<div id="dialog_appRel_add" title="dialog">
	<iframe id="iframe_appRel_add" src="" frameborder="0" height="670" width="780" marginheight="0" marginwidth="0" scrolling="yes" ></iframe>
</div>

<script type="text/javascript">

$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
//添加告警应用
function openAppSelect () {
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_alarmApp_rel.jsp?selectedApp=" + $("#selectApp").val());
	$("#dialog_appRel_add").dialog("open");
}

function deleteTableTr(appName){

	$("#" + appName).remove();
	var appStrs = $("#selectApp").val().split(",");
	var newAppStr = "";
	for(var i=0; i < appStrs.length-1; i++) {			//用；切分后的数组最后会是空白字符串
	
		if(appStrs[i] != appName) {			//app[0]是应用的Id
			newAppStr += appStrs[i] + ",";
		}
	}
	$("#selectApp").val(newAppStr);

	//删除hidden中的key
	var keyStrs = $("#relKey").val().split(";");//<appID:keyid,keyid>类似：98:21793,21818; 
	var newKeyStr = "";
	for(var i=0; i < keyStrs.length-1; i++) {			//用；切分后的数组最后会是空白字符串

		var app = keyStrs[i].split(":");	//98:21793,21818
		
		if(app[0] != appName) {			//app[0]是应用的Id
			newKeyStr += keyStrs[i] + ";";
		}
	}
	$("#relKey").val(newKeyStr);
}

//应用选择key
function selectKey(appId){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appKey_rel.jsp?appId=" + appId + "&relKey=" + $("#relKey").val());
	$("#dialog_appRel_add").dialog("open");
}

//报表添加应用关联
function selectApp(reportId,selectApp){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src",("<%=request.getContextPath () %>/user/user_appReport_rel.jsp?reportId=" + reportId + "&relApp=" + selectApp));

	$("#dialog_appRel_add").dialog("open");
}

//查看已经添加的应用
function checkApp(reportId, reportIdAndApps){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	//console.log($("#selectReportApp").val());
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appReport_rel_check.jsp?reportId=" + reportId + "&allRel=" + reportIdAndApps);

	$("#dialog_appRel_add").dialog("open");
}

</script>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">报表接收信息</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">

<table border="1">

	<tr>
		<td align="center">报表名</td>
		<td align="center">操作</td>
	</tr>
	<%
	for(ReportInfoPo info:listReportList){
		
		if(info.getType()==0){
	%>
		<tr>
		<td>
			<%=info.getName()%>:<input type="checkbox" name="report_<%=info.getId() %>"  value="<%=info.getId() %>" >
		</td>	
		</tr>	
	<%	}
		else{
	%>
	<tr>
		<td align="center"><%=info.getName() %></td>
		<td align="center">
		<a href="javascript:selectApp(<%=info.getId() %>,$('#selectReportApp').val())">备置应用</a>
		<a href="javascript:checkApp(<%=info.getId() %>,$('#selectReportApp').val())">查看已配置应用</a>
		
		</td>
	</tr>
	
	<%
		}
	}%>

</table>
	
</div>
</div>

<table>
	<tr>
		<td align="center">
		<input type="hidden" value="add" name="action">
		
		<input type="hidden" id="selectApp" name="selectApp" value="">
		<input type="hidden" id="selectReportApp" name="selectReportApp" value="">
		<input type="hidden" id="relKey" name="relKey" value="">
		<input type="submit" value="添加新用户">
		<input type="button" value="关闭" onclick="window.close()">
		</td>
	</tr>
</table>
</form>
<%} %>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>