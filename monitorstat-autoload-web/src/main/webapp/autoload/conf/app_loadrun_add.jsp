<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% request.setAttribute("title","����ѹ������"); %>
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<form
						action="<%=request.getContextPath() %>/loadrun/config.do?method=add"
						method="post" id="signupForm">
						<div class="alert-message block-message success">
							<button type="submit" class="btn primary">
								�ύ
							</button>
							<button type="button" class="btn primary"
								onclick="window.close()">
								ȡ��
							</button>
							<button type="button" class="btn primary" onclick="check()">
								���IP
							</button>
						</div>
						<table id="myTable">
							<tr>
								<td>
									ѡ��ѹ��Ӧ�ã�
								</td>
								<td>
									<select id="parantProductSelect" onchange="productChange(this)"></select>
									<select id="parentGroupSelect" onchange="groupChange(this)"></select>
									<select id="appNameSelect" name="appId"></select>
								</td>
							</tr>
							<tr>
								<td>
									ѹ�����IP��
								</td>
								<td>
									<input type="text" value="" name="hostIp" id="hostIp"
										placeholder="ѹ�����IP����Ϊ��" />
									<font color="red" id="checkApp">˵��:����·��ļ��IP��ť����У��IP��Ӧ���Ƿ�ƥ��</font>
									<span for="hostIp"></span>
								</td>
							</tr>
							<tr>
								<td>
									ѹ��ʱ�䣺
								</td>
								<td>
									<input type="text" value="" name="startTime" id="startTime"
										placeholder="ѹ��ʱ�䲻��Ϊ��" />
									<font color="red">˵��:ʹ��cron ���ʽ ��:0 10 9 * * ?
										��ʾÿ��9��10ִ��</font>
									<span for="startTime"></span>
								</td>
							</tr>
							<tr>
								<td>
									�Ƿ��Զ�ѹ�⣺
								</td>
								<td>
									<select id="loadrunAuto" name="loadAuto">
										<option value="1">
											�Զ�
										</option>
										<option value="0">
											���Զ�
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									ѹ�����ͣ�
								</td>
								<td>
									<select id="loadrunType" name="loadType"
										onchange="showDivByType()">
										<c:forEach items="${loadrunTypes}" var="type">
											<option value="${type}">
												${type}
											</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									ѹ��ģʽ��
								</td>
								<td>
									<select id="loadrunMode" name="loadrunMode"
										onchange="showDivByType()">
										<c:forEach items="${loadrunModes}" var="mode">
											<option value="${mode}">
												${mode}
											</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td width="150">
									ѹ����Ϣ����(����)��
								</td>
								<td>
									<input type="text" style="width:400px;" value="����;" name="wangwangs" id="wangwangs" />
									<span for="wangwangs"></span>
									<font color="red">˵��:����ѹ������з��ͳ�������Ϣ ����������÷ֺŷָ� �磺 ����;С��</font>
								</td>
							</tr>
							<tr>
								<td>
									ѹ���������ã�
								</td>
								<td>
									<input type="text" style="width:480px;" value="CPU:75;Load:10;" name="limitFeature"
										size="60" id="limitFeature" />
									<span for="limitFeature"></span>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<font color="red">��������˵��:ѹ�����ݵ������õ�����ʱ����ֹͣ,��ʽΪ:key:value;
										Qps,Rest,CPU,Load,Apache_Pv,Apache_Rest,Tomcat_Pv,Tomcat_Rest,Jvm_Memeory,Hsf_pv,Hsf_Rest;
									</font>
								</td>
							</tr>
							<tr>
								<td>
									�Զ�ѹ�����ã�
								</td>
								<td>
									<input type="text"  style="width:480px;" value="" name="loadFeature" size="70"
										id="loadFeature" placeholder="�Զ�ѹ�����ò���Ϊ��" />
									<span for="loadFeature"></span>
									<font id="loadFeatureDesc" style="color: red;">
										˵��:httpload:ѹ�Ⲣ����5,10,15...,apache ���ip1,ip2,ip3...,hsf ���ip����
										1,2,3,4,5</font>
								</td>
							</tr>
							<tr>
								<td>
									���ö���˵����
								</td>
								<td>
									<input type="text"  style="width:480px;" value="" name="configFeature" size="100"
										id="configFeature" />
									<font id="configFeatureDesc" style="color: red;">
										˵��:hsf���͵���д�ӿ���Ϣ��apache ��д�������� 4_1,4_2,4_3,5_9��</font>
								</td>
							</tr>
							<tr>
								<td>
									�����˻���
								</td>
								<td>
									<input type="text" value="" name="userName" id="userName"
										placeholder="�����˻�����Ϊ��" />
									<span for="userName"></span>
									<font color="red"> ˵��:��дӵ��sudoȨ�޵��˻�</font>
								</td>
							</tr>
							<tr>
								<td>
									�������룺
								</td>
								<td>
									<input type="password" value="" name="password" id="password"
										placeholder="�������벻��Ϊ��" />
									<span for="password"></span><font color="red">
										˵��:��дӵ��sudoȨ�޵��˻�����</font>
								</td>
							</tr>
							<tr>
								<td>
									ѹ�⿪ʼִ��url��
								</td>
								<td>
									<input type="text"  style="width:480px;" name="startUrl" size="100" />
									<font color="red">url��ʽ�������Э��(http://)</font>
								</td>
							</tr>
							<tr>
								<td>
									ѹ�����ִ��url��
								</td>
								<td>
									<input type="text"  style="width:480px;" name="endUrl" size="100" />
									<font color="red">url��ʽ�������Э��(http://)</font>
								</td>
							</tr>
							<tr>
								<td>
									Hsf��qps�����Ƿ�ͨ���ɼ�eagleeye��
								</td>
								<td>
									<select id="collectEagleeye" name="collectEagleeye">
										<option value="0">
											��
										</option>
										<option value="1">
											��
										</option>
									</select>
								</td>
							</tr>
						</table>
						<div id="httpload_set">
							<table id="myTable">
								<tr>
									<td>
										��־���ͣ�
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<select name="httploadlogtype" onchange="httploadselfLog()"
											id="httploadlogtype">
											<c:forEach items="${httploadlogtype}" var="type">
												<option value="${type}">
													${type.logDesc}
												</option>
											</c:forEach>
										</select>
										<textarea rows="5"  style="width:480px;" cols="100" name="httploadloglog"
											id="httploadloglog"></textarea>
										<font color="red">���Url����@�ָ�</font>
									</td>
								</tr>

								<tr>
									<td>
										httploadѹ�ⷽʽ��
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<select name="httploadProxy">
											<option value="no">
												�޸�ԭʼurlΪip:7001ѹjboss
											</option>
											<option value="proxy">
												����ԭʼurlѹjboss
											</option>
											<option value="directProxy">
												ֱ��ѹapache(nginx)
											</option>
											<option value="ssl">
												ssl��ʽֱ��ѹapache(nginx)
											</option>
										</select>
										<font color="red">jboss�Ķ˿ڱ���Ϊ7001��apache��nginx���Ķ˿ڱ���Ϊ80,ssl��ʽֱ��ѹapache(nginx)��ʱֻ֧���ֶ�ѹ��</font>
									</td>
								</tr>
								<tr>
									<td>
										httpload·����
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input  style="width:480px;" type="text" value="/home/admin/bin/http_load"
											name="httploadpath" size="50" />
									</td>
								</tr>

							</table>
						</div>

						<div id="apache_set">
							<table id="myTable">
								<tr>
									<td colspan="2">
										<span id="configText"></span>
									    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input  style="width:480px;" type="text" value="/home/admin/cai/conf/extra/"
											name="jkConfigPath" size="50" id="jkConfigPath" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<span id="runText"></span>
									    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input  style="width:480px;" type="text" name="apachectlPath" size="50"
											id="apachectlPath" />
									</td>
								</tr>
							</table>

						</div>

						<div id="script_set">
							<table id="myTable" class="tablesorter">
								<tr>
									<td>
										<label id="start">
											�����ű���
										</label>
									</td>
									<td>
										<input type="text" value="start_load.sh" name="startScript"
											size="50" id="startScript" />
									</td>
								</tr>
								<tr>
									<td>
										<label id="end">
											ֹͣ�ű���
									</td>
									<td>
										<input type="text" value="stop_load.sh" name="endScript"
											size="50" id="endScript" />
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
				
			</div>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
});

function httploadselfLog(){
	var type = $("#httploadlogtype").val();
	if("self" == type || "scriptApache" == type){
		$("#httploadloglog").show();
	}else{
		$("#httploadloglog").hide();
	}
}

function showDivByType(){
	var type = $("#loadrunType").val();
	var mode = $("#loadrunMode").val();
	if(type == 'apache' && mode == 'SSH') { //��������ʾ
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
		$("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3...");
		$("#configText").text("jk�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'hsf' && mode == 'SSH'){
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
		$("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
		$("#apachectlPath").val("");
	} else if(type == 'httpLoad' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
		$("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
		$("#apachectlPath").val("");
	} else if(type == 'apacheProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'nginxProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("nginx����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/nginx-proxy");
	} else if(type == 'apache' && mode == 'SCRIPT') { //��������ʾ
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
		$("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3...");
		$("#configText").text("jk�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'hsf' && mode == 'SCRIPT'){
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
		$("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
		$("#apachectlPath").val("");
	} else if(type == 'httpLoad' && mode == 'SCRIPT') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
		$("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
		$("#apachectlPath").val("");
	} else if(type == 'apacheProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("B2b��SCRIPT��ʽΪ�������ʽ:80:2_80:4,80:2_80:5���˿ں�:Ȩ��)");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'nginxProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("nginx����·���� ");
		$("#apachectlPath").val("/home/admin/cai/bin/nginx-proxy");
	}
}

var productMap ={}
var groupMap ={}

function addAppGroup(productName,groupName,appName,appId){
		if(!productMap[productName]) {
			productMap[productName]={};
		}
		if(!productMap[productName][groupName]) {
			productMap[productName][groupName]=groupName;
		}
		
		if(!groupMap[groupName]){
			groupMap[groupName]={};
		}
		if(!groupMap[groupName][appName]){
			groupMap[groupName][appName]=appId;
		}			
}
	
	function productChange(selectObj){
		var productName = selectObj.options[selectObj.selectedIndex].value;
		var product = productMap[productName];
		if(product){
			clearGroupSelect();
			fillGroupSelect(productName);
		}
		
		var groupSelect = document.getElementById("parentGroupSelect");		
		groupChange(groupSelect);
	}
	
	function groupChange(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}
	
	function clearGroupSelect(){
		 document.getElementById("parentGroupSelect").options.length=0;			
	}
	
	function clearSubSelect(){
		 document.getElementById("appNameSelect").options.length=0;			
	}
	
	function fillGroupSelect(productName,value){
		var product = productMap[productName];
	
		var ops = document.getElementById("parentGroupSelect").options;
		var len = ops.length;
		for (group in product){
			document.getElementById("parentGroupSelect").options[len++]=new Option(group, product[group]);
			if(group == value){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
	}
	
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appNameSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appNameSelect").options[len-1].selected=true;
			}
		}
	}
	
	function initProductSelect(pname, gname){
		clearGroupSelect();
		var len = document.getElementById("parantProductSelect").options.length;
		for (pruduct in productMap){
			document.getElementById("parantProductSelect").options[len++]=new Option(pruduct,pruduct);
			if(pruduct == pname){
				document.getElementById("parantProductSelect").options[len-1].selected=true;
			}
		}
		if(pname&&gname){
			fillGroupSelect(pname,gname);
		}else{
			productChange(document.getElementById("parantProductSelect"));
		}
	
	}
	
	function initParentSelect(gname,gvalue){
		clearSubSelect();
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	
	}
	
	<c:forEach items="${appList}" var="app">
	addAppGroup("${app.productName}","${app.groupName}","${app.appName}","${app.appId}");
	</c:forEach>
	 initProductSelect("${app.productName}","${appInfo.groupName}");
	 initParentSelect("${appInfo.groupName}","${appInfo.appName}");
	 showDivByType();
	 httploadselfLog();
</script>

		<script type="text/javascript">

$().ready(function() {
	// validate signup form on keyup and submit
	$("#signupForm").validate({
		rules: {
			startTime: "required",
			hostIp: "required",
			wangwangs:"required",
			limitFeature:"required",
			loadFeature:"required",
			userName:"required",
			password:"required"
							
		}
		
	});
	
});

function check() {
	var target = $("#hostIp").attr("value");
	var loadType = $("#loadrunType").val();
	var loadFuture = $("#loadFeature").attr("value");
    var appName = $("#appNameSelect").find("option:selected").text();
    var method = "check";
    var urlDest = "config.do";
   	
   	 $.ajax({
      url: urlDest,
      async: true,
      contentType: "application/json",
      cache: false,
      data:{'method':method,'appName':appName,'target':target,'loadType':loadType,'loadFuture':loadFuture},
      success: function(data) {
        $('#checkApp').text(data);
      }
    });
	
}
</script>
		<jsp:include page="../footer.jsp"></jsp:include>