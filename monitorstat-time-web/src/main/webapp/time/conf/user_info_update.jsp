<%@page import="com.taobao.monitor.common.po.ProductLine"%>
<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@page import="com.taobao.csp.time.util.MonitorAppUtil"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.monitor.common.po.CspUserInfoPo"%>
<%@page import="java.util.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>ʵʱ���ϵͳ</title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/datatable/css/demo_page.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/datatable/css/demo_table.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/datatable/js/jquery.dataTables.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<style type="text/css">
body {
	padding-top: 60px;
}

.td1 {
	width: 200px
}

.input_time {
	width: 80px
}

.input_count {
	width: 30px
}
</style>
<script type="text/javascript">
$(document).ready(function(){

	$("#selectAppTable").dataTable();
	
	$("#phone").blur(function(){
		var phonePattern =/^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/; 
		var phone = $("#phone").val();
		if(phone == "") {
			$("#note-phone").text("������Ϊ��");
			$("#note-phone").css("visibility","visible");							
		}
		else if(phonePattern.test(phone) == false) {
			$("#note-phone").text("�绰�����ʽ����");
			$("#note-phone").css("visibility","visible");	
		}else{
			$("#note-phone").text("OK");
		}
	});
	
	$("#wangwang").blur(function(){
		var phone = $("#wangwang").val();
		if(phone == "") {
			$("#note-wangwang").text("������Ϊ��");
			$("#note-wangwang").css("visibility","visible");							
		}else{
			$("#note-phone").text("OK");
		}
	});
	$("#mail").blur(function(){
		var mail = $("#mail").val();
		var emailPattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		if(mail == "") {
			$("#note-mail").text("������Ϊ��");
			$("#note-mail").css("visibility","visible");							
		}
		else if(emailPattern.test(mail)== false) {
			$("#note-mail").text("email��ʽ����");
			$("#note-mail").css("visibility","visible");	
		}else{
			$("#note-mail").text("OK");
		}
	});
});

function CheckForm(){ 
	if ($("#name").val()=="")  {  
		alert("����û�����������!");
		$("#name").focus();
		return  false;
	} else if($("#wangwang").val()=="") {
		alert("���������������!");
		$("#wangwang").focus();
		return false;
	} else if($("#phone").val()=="") {
		alert("��ѵ绰��������!");
		$("#phone").focus();
	} else if($("#mail").val()=="") {
		alert("��������������!");
		$("#mail").focus();
	}
	
	
	
	//���շ�ʽ�ǿ���֤ 
	for(var i=1;i<=7;i++){
			var pwi = $("input[name='phone_week_"+i+"']");
			if($.trim(pwi.val())==""){
				pwi.focus();
				alert("���ֻ����������ã����� " +  nToWeekDay(i) +" �ġ�ʱ�䡯������!");
				return false;
			}
			var pni = $("input[name='phone_num_"+i+"']");
			if($.trim(pni.val())==""){
				pni.focus();
				alert("���ֻ����������ã����� " +  nToWeekDay(i) +" �ġ��澯����������!");
				return false;
			}
			var wwi = $("input[name='wangwang_week_"+i+"']");
			if($.trim(wwi.val())==""){
				wwi.focus();
				alert("���������������ã����� " +  nToWeekDay(i) +" �ġ�ʱ�䡯������!");
				return false;
			}
			var wni = $("input[name='wangwang_num_"+i+"']");
			if($.trim(wni.val())==""){
				wni.focus();
				alert("���������������ã����� " +  nToWeekDay(i) +" �ġ��澯����������!");
				return false;
			}
	}		
	//ѡ��Ӧ����֤
	var sa = $("input[name='selectApp']");
	//alert("sa: "+ sa+"\t sa == \"\"�� "+ (sa == ""));
	if($.trim(sa.val())==""){
		sa.focus();
		alert("Ӧ����ѡ��!");
		return false;
	}
	return  true;
	
	
	function nToWeekDay(n){
		var temp = 6 +n;
		if(temp>7)
			temp = temp -7;

		
		return temp;
	}
}


function getVal(obj){
	 if(obj.checked==true){
		 var appStr = $("#selectApp").val()
		 appStr += obj.value + ",";
		 $("#selectApp").val(appStr);
	 }else{
		 var appStr = $("#selectApp").val()
		 var apps = appStr.split(",");
		 var tmp="";
		 for(var i=0;i<apps.length;i++){
			var app = apps[i];
			if(app != obj.value){
				tmp+=app+",";
			}
		}	
		 $("#selectApp").val(tmp);
	 }
}

</script>
</head>
<body>
	<%
			CspUserInfoPo userinfo = (CspUserInfoPo) request.getAttribute("userinfo");
			
		%>
	<div class="container">
		<div ><font color="red">${msg }</font></div>
		<form action="<%= request.getContextPath()%>/user.do" method="post" 	onsubmit="return CheckForm()">
		<div style="text-align: center; margin-top: 5px">
			<input type="submit" class="btn btn-primary span1" value="�ύ" /><a href="<%= request.getContextPath()%>/index.jsp">������ҳ</a>
		</div>
			<input type="hidden" name="method"  value="update" />
			<div class="row">
			<div class="span12 ">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�û�������Ϣ</div>
				<table class="table table-bordered">
						<tr>
							<td>����:&nbsp;&nbsp;&nbsp; <input type="text" id="mail"
								name="mail" class="register-input" value="${userinfo.mail }">
								<font color="red" id="note-mail"></font>
							</td>
							<td>������: <input type="text" id="wangwang" name="wangwang"
								class="register-input" value="${userinfo.wangwang }">
								<font color="red" id="note-wangwang"></font>
							</td>
							<td>�ֻ���: <input type="text" id="phone" name="phone"
								class="register-input" value="${userinfo.phone }">
								<font color="red" id="note-phone"></font>
							</td>
						</tr>
					</table>
			</div>
				<div class="span12">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�澯������Ϣ��ʽ</div>
					<table width="100%"  class="table table-bordered">
						<tr>
							<td colspan="7">�ֻ���������:</td>
						</tr>
						<%
							
								//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
								String phoneFeature = userinfo.getPhone_feature();
							if(StringUtils.isBlank(phoneFeature)){
								phoneFeature = "0#1#00:00#23:59$0#2#00:00#23:59$0#3#00:00#23:59$0#4#00:00#23:59$0#5#00:00#23:59$0#6#00:00#23:59$0#7#00:00#23:59$";
							}
						
								String[] week = new String[7];
								String[] num = new String[7];
								if (phoneFeature != null) {
									String[] featrues = phoneFeature.split("\\$");
									for (String featrue : featrues) {
										String[] _tmp = featrue.split("\\#");
										if (_tmp.length == 4) {
											int w = Integer.parseInt(_tmp[1]);
											String time1 = _tmp[2];
											String time2 = _tmp[3];
											week[w - 1] = time1 + "#" + time2;
											num[w - 1] = _tmp[0];
										}
									}
								}
							%>
						<tr>
							<td>��һ: <input type="text" name="phone_week_2"
								value="<%=week[1] == null ? "" : week[1]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_2"
								value="<%=num[1] == null ? "" : num[1]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>�ܶ�: <input type="text" name="phone_week_3"
								value="<%=week[2] == null ? "" : week[2]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_3"
								value="<%=num[2] == null ? "" : num[2]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="phone_week_4"
								value="<%=week[3] == null ? "" : week[3]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_4"
								value="<%=num[3] == null ? "" : num[3]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="phone_week_5"
								value="<%=week[4] == null ? "" : week[4]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_5"
								value="<%=num[4] == null ? "" : num[4]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="phone_week_6"
								value="<%=week[5] == null ? "" : week[5]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_6"
								value="<%=num[5] == null ? "" : num[5]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="phone_week_7"
								value="<%=week[6] == null ? "" : week[6]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_7"
								value="<%=num[6] == null ? "" : num[6]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="phone_week_1"
								value="<%=week[0] == null ? "" : week[0]%>" class="input_time">
								<br /> �澯 <input type="text" name="phone_num_1"
								value="<%=num[0] == null ? "" : num[0]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
						</tr>
					</table>

					<%
							//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
							String wangwangFeature = userinfo.getWangwang_feature();
							if(StringUtils.isBlank(wangwangFeature)){
								wangwangFeature = "3#1#00:00#23:59$3#2#18:00#23:59$3#3#18:00#23:59$3#4#18:00#23:59$3#5#18:00#23:59$3#6#18:00#23:59$3#7#00:00#23:59$";
							}
							week = new String[7];
							num = new String[7];
							if (wangwangFeature != null) {
								String[] featrues = wangwangFeature.split("\\$");
								for (String featrue : featrues) {
									String[] _tmp = featrue.split("\\#");
									if (_tmp.length == 4) {
										int w = Integer.parseInt(_tmp[1]);
										String time1 = _tmp[2];
										String time2 = _tmp[3];
										week[w - 1] = time1 + "#" + time2;
										num[w - 1] = _tmp[0];
									}
								}
							}
						%>
					<table width="100%" class="table table-bordered">
						<tr>
							<td colspan="7">������������:</td>
						</tr>
						<tr>
							<td>��һ: <input type="text" name="wangwang_week_2"
								value="<%=week[1] == null ? "" : week[1]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_2"
								value="<%=num[1] == null ? "" : num[1]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>�ܶ�: <input type="text" name="wangwang_week_3"
								value="<%=week[2] == null ? "" : week[2]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_3"
								value="<%=num[2] == null ? "" : num[2]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="wangwang_week_4"
								value="<%=week[3] == null ? "" : week[3]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_4"
								value="<%=num[3] == null ? "" : num[3]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="wangwang_week_5"
								value="<%=week[4] == null ? "" : week[4]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_5"
								value="<%=num[4] == null ? "" : num[4]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="wangwang_week_6"
								value="<%=week[5] == null ? "" : week[5]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_6"
								value="<%=num[5] == null ? "" : num[5]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="wangwang_week_7"
								value="<%=week[6] == null ? "" : week[6]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_7"
								value="<%=num[6] == null ? "" : num[6]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
							<td>����: <input type="text" name="wangwang_week_1"
								value="<%=week[0] == null ? "" : week[0]%>" class="input_time">
								<br /> �澯 <input type="text" name="wangwang_num_1"
								value="<%=num[0] == null ? "" : num[0]%>" size="2"
								class="input_count"> ���Ϸ���
							</td>
						</tr>
					</table>

				</div>
			</div>
			<div class="row">
				<div class="span12">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ѡ����Ҫ�澯Ӧ��</div>
					<!-- ���table��������ajax���Ӵ����л�� -->
					<div>
					<table id="selectAppTable"  class="table table-bordered" >
						<thead>
							<tr>
								<th align='center'>x</th>
								<th align='center'>Ӧ����</th>
								<th align='center'>��Ʒ��</th>
								<th align='center'>������</th>
								<th align='center'>TL</th>
								<th align='center'>PE</th>
								<th align='center'>����</th>
							</tr>
							
						</thead>
						<tbody>
						<%
						
							String appStr = userinfo.getAccept_apps();
						    if(appStr == null){
						    	appStr = "";
						    }
							String[] apps = appStr.split(",");
							List<String> appNameList = MonitorAppUtil.getMonitorApps();
							for(String appid : apps) {
								AppInfoPo info = null;
								try{
									 info = AppInfoCache.getAppInfoById(Integer.parseInt(appid));
								}catch(Exception e){
								}
								if(info == null){
									continue;
								}
								if(!appNameList.contains(info.getAppName())){
									continue;
								}
								appNameList.remove(info.getAppName());
								
								ProductLine pl = TBProductCache.getProductLineByAppName(info.getAppName());
						%>
				
							<tr >
								<td align='center'><input type="checkbox"  checked="checked" value="<%=info.getAppId()%>"onclick="getVal(this)"></td>
								<td align='center'><%=info.getAppName()%></td>
								<td align='center'><%=pl.getProductline() %></td>
								<td align='center'><%=pl.getDevelopGroup() %></td>
								<td align='center'><%=pl.getTeamLeader() %></td>
								<td align='center'><%=pl.getPe()%></td>
								<td align='center'></td>
							</tr>
						<%
						}
							
							for(String appName:appNameList){
								AppInfoPo info = null;
								try{
									 info = AppInfoCache.getAppInfoByAppName(appName);
								}catch(Exception e){
								}
								
								if(info ==null){
									continue;
								}
								
								ProductLine pl = TBProductCache.getProductLineByAppName(info.getAppName());
						%>
								<tr >
								<td align='center'><input type="checkbox"  value="<%=info.getAppId()%>" onclick="getVal(this)"></td>
								<td align='center'><%=info.getAppName()%></td>
								<td align='center'><%=pl.getProductline() %></td>
								<td align='center'><%=pl.getDevelopGroup() %></td>
								<td align='center'><%=pl.getTeamLeader() %></td>
								<td align='center'><%=pl.getPe()%></td>
								<td align='center'></td>
							</tr>
						<%
							}
						%>
						</tbody>
					</table>
					</div>
					
				</div>
				<!-- span5 -->
			</div>
			<!-- row -->
			<input type="hidden"  id="selectApp" name="selectApp"	value="<%= userinfo.getAccept_apps() %>"> 
			<input type="hidden" name="permission_desc" value="<%= userinfo.getPermission_desc() %>">
			<input type="hidden" name="id" value="<%= userinfo.getId() %>">
			<div style="text-align: center; margin-top: 5px">
						<input type="submit" class="btn btn-primary span1" value="�ύ" /><a href="<%= request.getContextPath()%>/index.jsp">������ҳ</a>
					</div>
		</form>
	</div>
	<!-- containter -->
</body>

</html>