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
<title>�û�����</title>

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
<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
	<form action="<%=request.getContextPath() %>/show/UserConf.do?method=addUser" method="post" onsubmit="return CheckForm()">
		<div class="page-header">
			<h2>
				�����û�
			</h2>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<th class="blue">�û���</th>
				<th class="blue">������</th>
				<th class="blue">�ֻ���</th>
				<th class="blue">����</th>
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
			<th class="blue" colspan="4">Ȩ��˵��</th>
		</tr>
		<tr>
			<td colspan="4"><input type="text" name="permissionDesc" style="width:1050px" value=""></td>
		</tr>
		</table>
		<div class="page-header">
			<h4>
				�澯������Ϣ��ʽ
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table" id="phone">
			<tr>
				<th class="blue">�ֻ�����</th>
				<th class="blue">��һ</th>
				<th class="blue">�ܶ�</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
			</tr>
			<tr>
				<td class="blue">����ʱ�䷶Χ</td>
				<td><input type="text" name="phone_week_2"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_3"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_4"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_5"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_6"  value="18:00#23:59"></td>
				<td><input type="text" name="phone_week_7"  value="00:00#23:59"></td>
				<td><input type="text" name="phone_week_1"  value="00:00#23:59"></td>
			</tr>
			<tr>
				<td class="blue">�������κ���</td>
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
				<th class="blue">��������</th>
				<th class="blue">��һ</th>
				<th class="blue">�ܶ�</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="blue">����ʱ�䷶Χ</td>
				<td><input type="text" name="wangwang_week_2"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_3"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_4"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_5"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_6"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_7"  value="00:00#23:59"></td>
				<td><input type="text" name="wangwang_week_1"  value="00:00#23:59"></td>
			</tr>
			<tr>
				<td class="blue">�������κ���</td>
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
				ѡ����Ҫ�澯Ӧ��
				<input class="btn primary pull-right" type="button" value="ѡ��Ӧ��" onClick="openAppSelect()">
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table" id="selectAppTable">
			<thead>
				<tr>
					<th class="blue">Ӧ����</th>
					<th class="blue">����</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		
		<!-- ��Ӹ澯Ӧ�õĵ����� -->
		<div id="dialog_appRel_add" title="dialog">
			<iframe id="iframe_appRel_add" src="" frameborder="0" height="670" width="780" marginheight="0" marginwidth="0" scrolling="yes" ></iframe>
		</div>
<script type="text/javascript">

$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
//��Ӹ澯Ӧ��
function openAppSelect () {
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/show/UserConf.do?method=gotoRelAppAdd&selectedApp="+$("#selectApp").val());
	$("#dialog_appRel_add").dialog("open");
}

function deleteTableTr(appId){

	$("#" + appId).remove();
	var appStrs = $("#selectApp").val().split(",");
	var newAppStr = "";
	for(var i=0; i < appStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���
	
		if(appStrs[i] != appId) {			//app[0]��Ӧ�õ�Id
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
</script>		
		<div class="actions">
			<input type="hidden" id="selectApp" name="selectApp" value="">
			<center>
				<input type="submit" class="btn primary" value="�ύ����" >
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" onclick="goback1()" value="����" >
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