<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% request.setAttribute("title","新增压测配置"); %>
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<form
						action="<%=request.getContextPath() %>/loadrun/config.do?method=add"
						method="post" id="signupForm">
						<div class="alert-message block-message success">
							<button type="submit" class="btn primary">
								提交
							</button>
							<button type="button" class="btn primary"
								onclick="window.close()">
								取消
							</button>
							<button type="button" class="btn primary" onclick="check()">
								检查IP
							</button>
						</div>
						<table id="myTable">
							<tr>
								<td>
									选择压测应用：
								</td>
								<td>
									<select id="parantProductSelect" onchange="productChange(this)"></select>
									<select id="parentGroupSelect" onchange="groupChange(this)"></select>
									<select id="appNameSelect" name="appId"></select>
								</td>
							</tr>
							<tr>
								<td>
									压测机器IP：
								</td>
								<td>
									<input type="text" value="" name="hostIp" id="hostIp"
										placeholder="压测机器IP不能为空" />
									<font color="red" id="checkApp">说明:点击下方的检查IP按钮，可校验IP与应用是否匹配</font>
									<span for="hostIp"></span>
								</td>
							</tr>
							<tr>
								<td>
									压测时间：
								</td>
								<td>
									<input type="text" value="" name="startTime" id="startTime"
										placeholder="压测时间不能为空" />
									<font color="red">说明:使用cron 表达式 如:0 10 9 * * ?
										表示每天9点10执行</font>
									<span for="startTime"></span>
								</td>
							</tr>
							<tr>
								<td>
									是否自动压测：
								</td>
								<td>
									<select id="loadrunAuto" name="loadAuto">
										<option value="1">
											自动
										</option>
										<option value="0">
											不自动
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>
									压测类型：
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
									压测模式：
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
									压测信息接收(旺旺)：
								</td>
								<td>
									<input type="text" style="width:400px;" value="游骥;" name="wangwangs" id="wangwangs" />
									<span for="wangwangs"></span>
									<font color="red">说明:接收压测过程中发送出来的信息 如果多人请用分号分割 如： 游骥;小赌</font>
								</td>
							</tr>
							<tr>
								<td>
									压测限制设置：
								</td>
								<td>
									<input type="text" style="width:480px;" value="CPU:75;Load:10;" name="limitFeature"
										size="60" id="limitFeature" />
									<span for="limitFeature"></span>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<font color="red">限制设置说明:压测数据到达设置的上限时立即停止,格式为:key:value;
										Qps,Rest,CPU,Load,Apache_Pv,Apache_Rest,Tomcat_Pv,Tomcat_Rest,Jvm_Memeory,Hsf_pv,Hsf_Rest;
									</font>
								</td>
							</tr>
							<tr>
								<td>
									自动压测设置：
								</td>
								<td>
									<input type="text"  style="width:480px;" value="" name="loadFeature" size="70"
										id="loadFeature" placeholder="自动压测设置不能为空" />
									<span for="loadFeature"></span>
									<font id="loadFeatureDesc" style="color: red;">
										说明:httpload:压测并发数5,10,15...,apache 存放ip1,ip2,ip3...,hsf 存放ip倍数
										1,2,3,4,5</font>
								</td>
							</tr>
							<tr>
								<td>
									配置额外说明：
								</td>
								<td>
									<input type="text"  style="width:480px;" value="" name="configFeature" size="100"
										id="configFeature" />
									<font id="configFeatureDesc" style="color: red;">
										说明:hsf类型的填写接口信息，apache 填写分流比例 4_1,4_2,4_3,5_9等</font>
								</td>
							</tr>
							<tr>
								<td>
									个人账户：
								</td>
								<td>
									<input type="text" value="" name="userName" id="userName"
										placeholder="个人账户不能为空" />
									<span for="userName"></span>
									<font color="red"> 说明:填写拥有sudo权限的账户</font>
								</td>
							</tr>
							<tr>
								<td>
									个人密码：
								</td>
								<td>
									<input type="password" value="" name="password" id="password"
										placeholder="个人密码不能为空" />
									<span for="password"></span><font color="red">
										说明:填写拥有sudo权限的账户密码</font>
								</td>
							</tr>
							<tr>
								<td>
									压测开始执行url：
								</td>
								<td>
									<input type="text"  style="width:480px;" name="startUrl" size="100" />
									<font color="red">url格式必须包含协议(http://)</font>
								</td>
							</tr>
							<tr>
								<td>
									压测结束执行url：
								</td>
								<td>
									<input type="text"  style="width:480px;" name="endUrl" size="100" />
									<font color="red">url格式必须包含协议(http://)</font>
								</td>
							</tr>
							<tr>
								<td>
									Hsf的qps计算是否通过采集eagleeye：
								</td>
								<td>
									<select id="collectEagleeye" name="collectEagleeye">
										<option value="0">
											否
										</option>
										<option value="1">
											是
										</option>
									</select>
								</td>
							</tr>
						</table>
						<div id="httpload_set">
							<table id="myTable">
								<tr>
									<td>
										日志类型：
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
										<font color="red">多个Url请求@分割</font>
									</td>
								</tr>

								<tr>
									<td>
										httpload压测方式：
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<select name="httploadProxy">
											<option value="no">
												修改原始url为ip:7001压jboss
											</option>
											<option value="proxy">
												保留原始url压jboss
											</option>
											<option value="directProxy">
												直接压apache(nginx)
											</option>
											<option value="ssl">
												ssl方式直接压apache(nginx)
											</option>
										</select>
										<font color="red">jboss的端口必须为7001，apache（nginx）的端口必须为80,ssl方式直接压apache(nginx)暂时只支持手动压测</font>
									</td>
								</tr>
								<tr>
									<td>
										httpload路径：
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
											启动脚本：
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
											停止脚本：
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
	if(type == 'apache' && mode == 'SSH') { //分流就显示
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("填写分流比例 4_1,4_2,4_3,5_9 。前面表示本地流量，后面表示分流流量");
		$("#loadFeatureDesc").text("说明:apache 存放ip1,ip2,ip3...");
		$("#configText").text("jk配置文件路径： ");
		$("#runText").text("apache启动路径： ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'hsf' && mode == 'SSH'){
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
		$("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
		$("#apachectlPath").val("");
	} else if(type == 'httpLoad' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
		$("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
		$("#apachectlPath").val("");
	} else if(type == 'apacheProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("apache启动路径： ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'nginxProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("nginx启动路径： ");
		$("#apachectlPath").val("/home/admin/cai/bin/nginx-proxy");
	} else if(type == 'apache' && mode == 'SCRIPT') { //分流就显示
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("填写分流比例 4_1,4_2,4_3,5_9 。前面表示本地流量，后面表示分流流量");
		$("#loadFeatureDesc").text("说明:apache 存放ip1,ip2,ip3...");
		$("#configText").text("jk配置文件路径： ");
		$("#runText").text("apache启动路径： ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'hsf' && mode == 'SCRIPT'){
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
		$("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
		$("#apachectlPath").val("");
	} else if(type == 'httpLoad' && mode == 'SCRIPT') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
		$("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
		$("#apachectlPath").val("");
	} else if(type == 'apacheProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("B2b的SCRIPT方式为必填项，格式:80:2_80:4,80:2_80:5（端口号:权重)");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("apache启动路径： ");
		$("#apachectlPath").val("/home/admin/cai/bin/apachectl");
	} else if(type == 'nginxProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("nginx启动路径： ");
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