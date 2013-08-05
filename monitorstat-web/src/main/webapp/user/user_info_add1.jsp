<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script> 
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



</script>

</head>
<body>



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
<jsp:include page="../head.jsp"></jsp:include>

<form id="f" name="f" action="./user_info_add1.jsp" method="post" onsubmit="getNewListOptionsValue()">

<%
List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();

String action = request.getParameter("action");
if("add".equals(action)){
	String name = request.getParameter("name");
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String mail =request.getParameter("mail");
	String[] groups =request.getParameterValues("new_list");
	String id =request.getParameter("id");
	String phoneDesc = "";
	String gs = request.getParameter("newListValue_app");

	
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
	
	loginUserPo.setGroup(gs);
	loginUserPo.setPermissionDesc("alarmKey:"+gs+";");
	
	String report="";
	for(ReportInfoPo info:listReportList){
		int reportId = info.getId();
		if(info.getType()==0){
			String value = request.getParameter("report_"+reportId);
			if(value!=null){
				report += reportId+";";
			}
		}else{
			String appIds = request.getParameter("newListValue_" + reportId);
			if(appIds!=null && !appIds.equals("")){
				String appStr = "";
				appStr += appIds;	
				report +=reportId+":"+appStr+";";
			}
		}
	}
	
	loginUserPo.setReportDesc(report);
	
	
	boolean b = MonitorUserAo.get().addLoginUserPo(loginUserPo);
	
	
	%>
	<font size="+3" color="red"><%if(b){out.print("成功");}else{out.print("失败!出现异常");} %></font>	
	<a href="./manage_user.jsp">返回</a>
	<%
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
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>
<div style="float:left;">
    接收告警应用:<br/>
<select id="old_list_app" multiple="multiple" name="old_list_app" size="10" style="width:200px;">

  	<%
	for(AppInfoPo app:listApp){
	%> 	
		<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
	<%} %>
</select>
</div>
<div style="float:left;"><br/><br/><br/><br/><br/>
    <input type="button" id="opt_add_app" value="ADD">
    <input type="button" id="opt_add_all_app" value="ADD ALL">
    <input type="button" id="opt_del_app" value="DEL">
    <input type="button" id="opt_del_all_app" value="DEL ALL">
</div>
<div style="float:left;">
          已添加告警应用:<br/>
    <select id="new_list_app" name="new_list_app" size="10" multiple="multiple" style="width:200px;">
    </select>
    <input type="hidden" name="newListValue_app" id="newListValue_app">
</div>
</td>
</tr>
</table>

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
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">报表接收信息</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
	<%
	for(ReportInfoPo info:listReportList){
		
	%>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	
		<%if(info.getType()==0){
		%>
		<tr class="ui-widget-header ">
			<td>
				<%=info.getName()%>:<input type="checkbox" name="report_<%=info.getId() %>"  value="<%=info.getId() %>" >
			</td>	
		</tr>	
		<%	
		}else{
		%>
		<tr class="ui-widget-header ">
			<td>
				<%=info.getName()%>:
			</td>	
		</tr>	
<!-- 这里是左右选择列表框 -->
<tr>
<td>
<div style="float:left;">
	未添加应用:<br/>
<select id="old_list_<%=info.getId() %>" multiple="multiple" name="old_list_<%=info.getId() %>" size="10" style="width:200px;">

  	<%
	for(AppInfoPo app:listApp){
	%> 	
		<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
	<%} %>
</select>
</div>
<div style="float:left;"><br/><br/><br/><br/><br/>
    <input type="button" id="opt_add_<%=info.getId() %>" value="ADD">
    <input type="button" id="opt_add_all_<%=info.getId() %>" value="ADD ALL">
    <input type="button" id="opt_del_<%=info.getId() %>" value="DEL">
    <input type="button" id="opt_del_all_<%=info.getId() %>" value="DEL ALL">
</div>
<div style="float:left;">
   	 已添加应用:<br/>
    <select id="new_list_<%=info.getId() %>" name="new_list_<%=info.getId() %>" size="10" multiple="multiple" style="width:200px;">
    </select>
    <input type="hidden" name="newListValue_<%=info.getId() %>" id="newListValue_<%=info.getId() %>">
</div>
</td>
</tr>	

		<%	
		} 
		%>
		
</table>
<%} %>
</div>
</div>
<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="add" name="action"><input type="submit" value="添加新用户"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>

<script type="text/javascript">

//获取左右选择列表的添加值
function getNewListOptionsValue(){

	//拼接告警应用的添加值。
	for(var i=0; i < document.getElementById("new_list_app").length; i++) {

		document.getElementById("newListValue_app").value += document.getElementById("new_list_app").options[i].value;
		if(i != (document.getElementById("new_list_app").length)) {

			document.getElementById("newListValue_app").value += ",";
		}
	}

	<%
	
	//拼接报表接收信息的添加值
	for(ReportInfoPo info:listReportList){
		
	%>

		for(var i=0; i < document.getElementById("new_list_<%=info.getId()%>").length; i++) {
	
			document.getElementById("newListValue_<%=info.getId()%>").value += document.getElementById("new_list_<%=info.getId()%>").options[i].value;
			if(i != (document.getElementById("new_list_<%=info.getId()%>").length)) {
	
				document.getElementById("newListValue_<%=info.getId()%>").value += ",";
			}
		}
	<%
	}		
	%>
	return true;
}

//这里是为左右列表选择框添加响应事件
function SelectOperation(id){

		this.target = id;
		var self = this;
		this.init=function() {
			//alert(this.target);
			document.getElementById('opt_add_'+this.target).onclick = this.add;
			document.getElementById('opt_add_all_'+this.target).onclick = this.add_all;
	        document.getElementById('opt_del_'+this.target).onclick = this.del;
	        document.getElementById('opt_del_all_'+this.target).onclick = this.del_all;
			self.old_lsit = document.getElementById('old_list_'+this.target);
	        self.new_lsit = document.getElementById('new_list_'+this.target);
		},
		
		this.add=function() {

			//alert(1);
			var tmp_id = [];
            for (var i = 0; i < self.old_lsit.length; i++) {
                if (self.old_lsit.options[i].selected) {
                    self.new_lsit.options[self.new_lsit.options.length] = new Option(self.old_lsit.options[i].text, self.old_lsit.options[i].value);
                    tmp_id.push(self.old_lsit.options[i]);
                }
            }
            for (var del = 0; del < tmp_id.length; del++) {
                for (var j = 0; j < self.old_lsit.options.length; j++) {
                    if (tmp_id[del].value == self.old_lsit.options[j].value) {
                        self.old_lsit.remove(j);
                    }
                }
            }
		},
        this.del=function() {
            var tmp_id = [];
            for (var i = 0; i < self.new_lsit.options.length; i++) {
                if (self.new_lsit.options[i].selected) {
                    self.old_lsit.options[self.old_lsit.options.length] = new Option(self.new_lsit.options[i].text, self.new_lsit.options[i].value);
                    tmp_id.push(self.new_lsit.options[i]);
                }
            }
            for (var del = 0; del < tmp_id.length; del++) {
                for (var j = 0; j < self.new_lsit.length; j++) {
                    if (tmp_id[del].value == self.new_lsit.options[j].value) {
                        self.new_lsit.remove(j);
                    }
                }
            }
        },
        this.add_all=function() {

            for (var i = 0; i < self.old_lsit.length; i++) {
                self.new_lsit.options[self.new_lsit.options.length] = new Option(self.old_lsit.options[i].text, self.old_lsit.options[i].value);
            }
            self.old_lsit.options.length = 0;
        },
        this.del_all=function() {
            for (var i = 0; i < self.new_lsit.length; i++) {
                self.old_lsit.options[self.old_lsit.options.length] = new Option(self.new_lsit.options[i].text, self.new_lsit.options[i].value);
            }
            self.new_lsit.options.length = 0;
        }
	};

	var a = new SelectOperation("app");
	a.init();
	<%
	for(ReportInfoPo info:listReportList){
		
	%>
		var <%="ReportInfo_" + info.getId()%> = new SelectOperation("<%=info.getId()%>");
		<%="ReportInfo_" + info.getId()%>.init();
	<%
	}		
	%>
</script>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>