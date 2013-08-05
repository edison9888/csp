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
<title>���ļ��-�û�����</title>
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
	font-family: "����";
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
	'name' : "�������û���!",
	'wangwang' : "����������!",
	'phone' : "�������ֻ�����!",
	'mail' : "������mail"
}

$(document).ready(function(){

	$("input.register-input").click(function() {
		var id = $(this).attr("id");
		$("#note-"+id).text(info[id]);
		$("#note-"+$(this).attr("id")).css("visibility","visible")
	});

	//�ظ�����̫�࣬Ҫ�ģ����ǸĲ���
	$("#name").blur(function(){
		$.post('<%=request.getContextPath () %>/user/manage.mc',
				{name:$("#name").val()},
				function(data){

					if(data != "ERROR") {

						if(data.name == "") {
							$("#note-name").text("������Ϊ��");
							$("#note-name").css("visibility","visible");							
						}
						else if(data.nameExist == "yes") {
							
							$("#note-name").text("�Ѿ���������");
							$("#note-name").css("visibility","visible");
						} else {

							$("#note-name").text("��ϲ��������");
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
							$("#note-wangwang").text("������Ϊ��");
							$("#note-wangwang").css("visibility","visible");							
						}
						else if(data.wangwangExist == "yes") {
							
							$("#note-wangwang").text("�Ѿ���������");
							$("#note-wangwang").css("visibility","visible");
						} else {

							$("#note-wangwang").text("��ϲ��������");
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
						var phonePattern =/^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/;     //�ж� �ֻ�  
						if(data.phone == "") {
							$("#note-phone").text("������Ϊ��");
							$("#note-phone").css("visibility","visible");							
						}
						else if(phonePattern.test(data.phone) == false) {
							$("#note-phone").text("�绰�����ʽ����");
							$("#note-phone").css("visibility","visible");	
						}
						else if(data.phoneExist == "yes") {
							
							$("#note-phone").text("�Ѿ���������");
							$("#note-phone").css("visibility","visible");
						} else {

							$("#note-phone").text("��ϲ��������");
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
							$("#note-mail").text("������Ϊ��");
							$("#note-mail").css("visibility","visible");							
						}
						else if(emailPattern.test(data.mail)== false) {
							
							$("#note-mail").text("email��ʽ����");
							$("#note-mail").css("visibility","visible");	
						}
						else if(data.mailExist == "yes") {
							
							$("#note-mail").text("�Ѿ���������");
							$("#note-mail").css("visibility","visible");
						} else{

							$("#note-mail").text("��ϲ��������");
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
	alert("����û�����������!");
	$("#name").focus();
return  false;
} else if($("#wangwang").val().length == 0) {
	alert("���������������!");
	$("#wangwang").focus();
	return false;
} else if($("#phone").val().length == 0) {
	alert("��ѵ绰��������!");
	$("#phone").focus();
} else if($("#mail").val().length == 0) {
	alert("��������������!");
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
	out.print("��û��Ȩ�޲���!");
	return;
}

%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="user_info_add2.jsp" method="post" onsubmit="return CheckForm()">

<%
List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();

String action = request.getParameter("action");
String selectApp = request.getParameter("selectApp");					//ƴ�Ӻø澯��Ӧ�ã�   appId,appId,appId,
String selectReportApp = request.getParameter("selectReportApp");		//ƴ�Ӻñ���ѡ���Ӧ�ã�   reportId:appId,appId,appId;
String relKey = request.getParameter("relKey");							//ƴ�Ӻ�Ӧ�õĸ澯key��   appId:keyId,keyId;
if("add".equals(action)){
	String name = request.getParameter("name");
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String mail =request.getParameter("mail");
	String id =request.getParameter("id");
	String phoneDesc = "";

	
	//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
	for(int i=1;i<=7;i++){
		String phone_week = request.getParameter("phone_week_"+i);
		String phone_num = request.getParameter("phone_num_"+i);
		if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
			phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
		}
	}
	String wangwangDesc = "";
	//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
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
	loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");	//Ӧ�õ�ƴ��
	
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
	
	report += selectReportApp;		//�����ƴ��
	loginUserPo.setReportDesc(report);
	boolean b = MonitorUserAo.get().addLoginUserPo(loginUserPo);
	//===========
	boolean bb = true;
	
	if(b) {
		int userId = MonitorUserAo.get().getLoginUserPo(name).getId();
		
		if(relKey != null && !relKey.equals("")) {
			String[] appKeyStrs = relKey.split(";");
			for(String  appKeyStr: appKeyStrs) {		//appKeyStr��ֵΪ  appId:keyId,keyId;
				
				String[] appAndKey = appKeyStr.split(":");
				String app = appAndKey[0];			//����appId
				String[] keyIds = appAndKey[1].split(",");
				List<String> keyList = new ArrayList<String>();
				for(String key : keyIds) {
					
					keyList.add(key);
				}
				
				bb = MonitorExtraKeyAo.get().addUserAndKeyRel(userId, Integer.parseInt(app),keyList);
		}
		
		}
		%>
		<font size="+3" color="red"><%if(b && bb){out.print("�ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
		<a href="./manage_user.jsp">����</a>
	<%
	}
}else{
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�û�������Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td width="179">�û���:<input id="name" type="text" name="name" class="register-input" value="" width="179"></td>
		<td><font color="red"><div id="note-name">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>������:<input type="text" id="wangwang" name="wangwang" class="register-input" value=""></td>
		<td><font color="red"><div id="note-wangwang">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>�ֻ���:<input type="text" id="phone" name="phone" class="register-input" value=""></td>
		<td><font color="red"><div id="note-phone">&nbsp;</div></font></td>
	</tr>
	<tr>
		<td>����:&nbsp;&nbsp;&nbsp;<input type="text" id="mail" name="mail" class="register-input" value=""></td>
		<td><font color="red"><div id="note-mail">&nbsp;</div></font></td>
	</tr>
</table>
</div>
</div>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�澯������Ϣ��ʽ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>
		<table>
			<tr >
				<td>�ֻ���������:</td>
			</tr>
			<tr>
				<td>��һ:<input type="text" name="phone_week_2"  value="18:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_2"  value="3" size="2">���Ϸ���</td>
				<td>�ܶ�:<input type="text" name="phone_week_3"  value="18:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_3"  value="3" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="phone_week_4"  value="18:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_4"  value="3" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="phone_week_5"  value="18:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_5"  value="3" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="phone_week_6"  value="18:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_6"  value="3" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="phone_week_7"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_7"  value="3" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="phone_week_1"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_1"  value="3" size="2">���Ϸ���</td>
			</tr>			
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
			<tr >
				<td>������������:</td>
			</tr>
			<tr>
				<td>��һ:<input type="text" name="wangwang_week_2"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_2"  value="0" size="2">���Ϸ���</td>
				<td>�ܶ�:<input type="text" name="wangwang_week_3"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_3"  value="0" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="wangwang_week_4"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_4"  value="0" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="wangwang_week_5"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_5"  value="0" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="wangwang_week_6"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_6"  value="0" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="wangwang_week_7"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_7"  value="0" size="2">���Ϸ���</td>
				<td>����:<input type="text" name="wangwang_week_1"  value="00:00#23:59"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_1"  value="0" size="2">���Ϸ���</td>
			</tr>		
		</table>
	</td>
</tr>
</table>
</div>
</div>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ѡ����Ҫ�澯Ӧ��
<!-- <button id="btn-add-appRel" onclick="openAppSelect()">ѡ��</button> -->
<input type="button" value="ѡ��Ӧ��" onclick="openAppSelect()">
</div>
<!-- ���table��������ajax���Ӵ����л�� -->
<table id="selectAppTable" border="1">
	

	
</table>
</div>

<!-- ��Ӹ澯Ӧ�õĵ����� -->
<div id="dialog_appRel_add" title="dialog">
	<iframe id="iframe_appRel_add" src="" frameborder="0" height="670" width="780" marginheight="0" marginwidth="0" scrolling="yes" ></iframe>
</div>

<script type="text/javascript">

$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
//��Ӹ澯Ӧ��
function openAppSelect () {
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_alarmApp_rel.jsp?selectedApp=" + $("#selectApp").val());
	$("#dialog_appRel_add").dialog("open");
}

function deleteTableTr(appName){

	$("#" + appName).remove();
	var appStrs = $("#selectApp").val().split(",");
	var newAppStr = "";
	for(var i=0; i < appStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���
	
		if(appStrs[i] != appName) {			//app[0]��Ӧ�õ�Id
			newAppStr += appStrs[i] + ",";
		}
	}
	$("#selectApp").val(newAppStr);

	//ɾ��hidden�е�key
	var keyStrs = $("#relKey").val().split(";");//<appID:keyid,keyid>���ƣ�98:21793,21818; 
	var newKeyStr = "";
	for(var i=0; i < keyStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���

		var app = keyStrs[i].split(":");	//98:21793,21818
		
		if(app[0] != appName) {			//app[0]��Ӧ�õ�Id
			newKeyStr += keyStrs[i] + ";";
		}
	}
	$("#relKey").val(newKeyStr);
}

//Ӧ��ѡ��key
function selectKey(appId){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appKey_rel.jsp?appId=" + appId + "&relKey=" + $("#relKey").val());
	$("#dialog_appRel_add").dialog("open");
}

//�������Ӧ�ù���
function selectApp(reportId,selectApp){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src",("<%=request.getContextPath () %>/user/user_appReport_rel.jsp?reportId=" + reportId + "&relApp=" + selectApp));

	$("#dialog_appRel_add").dialog("open");
}

//�鿴�Ѿ���ӵ�Ӧ��
function checkApp(reportId, reportIdAndApps){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	//console.log($("#selectReportApp").val());
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appReport_rel_check.jsp?reportId=" + reportId + "&allRel=" + reportIdAndApps);

	$("#dialog_appRel_add").dialog("open");
}

</script>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���������Ϣ</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">

<table border="1">

	<tr>
		<td align="center">������</td>
		<td align="center">����</td>
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
		<a href="javascript:selectApp(<%=info.getId() %>,$('#selectReportApp').val())">����Ӧ��</a>
		<a href="javascript:checkApp(<%=info.getId() %>,$('#selectReportApp').val())">�鿴������Ӧ��</a>
		
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
		<input type="submit" value="������û�">
		<input type="button" value="�ر�" onclick="window.close()">
		</td>
	</tr>
</table>
</form>
<%} %>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>