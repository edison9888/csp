<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("title","修改压测配置"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width:1050px;">
     <div style="width:1050px; height: auto; margin: 0 auto">
<form action="<%=request.getContextPath() %>/loadrun/config.do?method=update" method="post" id="signupForm">
<div class="alert-message block-message success">
			<button type="submit" class="btn primary"> 确认更新 </button>
			<button type="button" class="btn primary" onclick="window.close()"> 取消 </button>
			<button type="button" class="btn primary" onclick="check()"> 检查IP </button>
</div>
<table   id="myTable" style="width:1050px;">
  <tr>
    <td >压测应用</td>
    <td >
		<font size="4" color="#000">${loadconfig.appName}</font>
		<input type="hidden" name="appId" value="${loadconfig.appId }"/>
		<input type="hidden" name="appName" value="${loadconfig.appName}"/>
	</td>
  </tr>
  <tr>
    <td>压测目标机器IP：</td>
    <td><input type="text" value="${loadconfig.hostIp }" name="hostIp" id="hostIp"/><span for="hostIp"></span>
    <font color="red" id="checkApp"> 说明:点击下方的检查IP按钮，可校验IP与应用是否匹配 </td>
  </tr>
  <tr>
    <td>压测时间:</td>
    <td>
    <input type="text" value="${loadconfig.startTime }" name="startTime" id="startTime"/>
    <span for="startTime"></span>
    <font color="red" > 说明:使用cron 表达式 如:0 10 9 * * ? 表示每天9点10执行</font></td>
  </tr>
   <tr>
    <td>是否自动压测</td>
    <td>
    	<select id="loadrunAuto" name="loadAuto">
    		<option value="1" <c:if test="${loadconfig.loadAuto == 1}"> selected</c:if>>自动</option>
			<option value="0" <c:if test="${loadconfig.loadAuto == 0}"> selected</c:if>>不自动</option>
		</select>
	</td>
   </tr>
   <tr>
		<td>压测类型: </td>
		<td>
			<select id="loadrunType" name="loadType" onchange="showDivByType()">
				<c:forEach items="${loadrunTypes}" var="type">
					<option value="${type}" <c:if test="${loadconfig.loadType == type}"> selected</c:if>>${type}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>压测模式: </td>
		<td>
			<select id="loadrunMode" name="loadrunMode" onchange="showDivByType()">
				<c:forEach items="${loadrunModes}" var="mode">
					<option value="${mode}" <c:if test="${loadconfig.loadrunMode == mode}"> selected</c:if>>${mode}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
    	<td width="160">压测信息接收（旺旺）:</td>
    	<td><input type="text" value="${loadconfig.wangwangs }" name="wangwangs" id="wangwangs" style="width:400px;"/> 
    	<font color="red"><span for="wangwangs"></span></font>
    	<font color="red"> 说明:接收压测过程中发送出来的信息 如果多人请用分号分割 如： 小赌;小赌</font>
    	</td>
    </tr>
    <tr>
    	<td>压测限制设置：</td>
    	<td>
    	<input type="text" value="${loadconfig.limitFeature }" name="limitFeature" size="60" id="limitFeature" style="width:480px;"/> 
    	<font color="red"><span for="limitFeature"></span></font></td>
    </tr>
     <tr>
    	<td colspan="2">
    	 <font color="red">限制设置说明:压测数据到达设置的上限时立即停止,
    	格式为:key:value;Qps,Rest,CPU,Load,Apache_Pv,Apache_Rest,Tomcat_Pv,Tomcat_Rest,Jvm_Memeory,Hsf_pv,Hsf_Rest;</font></td>
    </tr>
  <tr>
    <td>自动压测设置：</td>
    <td><input type="text" style="width:480px;" value="${loadconfig.loadFeature }" name="loadFeature" size="70" id="loadFeature"/>
     <span for="loadFeatureDesc"></span>
     <font id="loadFeatureDesc" style="color:red;">说明:httpload:压测并发数5,10,15...,apache 存放ip1,ip2,ip3,...,hsf 存放ip倍数 1,2,3,4,5</font>
     </td>
  </tr>
  <tr>
    <td>配置额外说明：</td>
    <td><input type="text" style="width:480px;" value="${loadconfig.configFeature }" name="configFeature" size="70"/> 
    <font id="configFeatureDesc" style="color:red;"> 说明:hsf类型的填写接口信息，apache 填写分流比例 4_1,4_2,4_3,5_9等</font>
    </td>
  </tr>
  <tr >
    <td >个人账户：</td>
    <td ><input type="text" value="${loadconfig.userName }" name="userName"/>
    <span for="userName"></span>
    <font color="red"> 说明:请填写拥有sudo权限的账户</font></td>
  </tr>
  <tr >
    <td>个人密码：</td>
    <td><input type="password" value="${loadconfig.password }" name="password"/><span for="password"></span>
    <font color="red"> 说明:请填写拥有sudo权限的账户密码</font></td>
  </tr> 
  <tr >
    <td width="114">压测开始执行url：</td>
    <td><input type="text" style="width:480px;" name="startUrl" size="100"  value="${loadconfig.startUrl }" />
    <font color="red"> url格式必须包含协议(http://),在压测开始前执行,可以不填</font></td>
  </tr >
  <tr >
    <td width="114">压测结束执行url：</td>
    <td><input type="text" style="width:480px;" name="endUrl" size="100"  value="${loadconfig.endUrl }" />
    <font color="red"> url格式必须包含协议(http://),在压测结束前执行,可以不填</font></td>
  </tr >
  <tr>
    <td>Hsf的qps计算是否通过采集eagleeye:</td>
    <td>
    	<select id="collectEagleeye" name="collectEagleeye">
    		<option value="1" <c:if test="${loadconfig.collectEagleeye == 1}"> selected</c:if>>是</option>
			<option value="0" <c:if test="${loadconfig.collectEagleeye == 0}"> selected</c:if>>否 </option>
		</select>
	</td>
   </tr>
  
</table>
<div id="httpload_set" style="width:1050px; height: auto; margin: 0 auto">
<table id="myTable" >
<tr >
    <td >日志类型： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	<select name="httploadlogtype" onchange="httploadselfLog()" id="httploadlogtype">
    		<c:forEach items="${httploadlogtype}" var="type" >
					<option value="${type}" <c:if test="${loadconfig.httploadlogtype == type}"> selected</c:if>>${type.logDesc}</option>
				</c:forEach>
		</select>
		<textarea rows="5" cols="100" name="httploadloglog" id="httploadloglog">${loadconfig.httploadloglog}</textarea>		
	</td>
</tr >

<tr >
    <td colspan="2">httpload压测方式： 
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <select name="httploadProxy">
			<option value="no" <c:if test="${loadconfig.httploadProxy == 'no'}"> selected</c:if>>修改原始url为ip:7001压jboss</option>
			<option value="proxy" <c:if test="${loadconfig.httploadProxy == 'proxy'}"> selected</c:if>>保留原始url压jboss</option>
			<option value="directProxy" <c:if test="${loadconfig.httploadProxy == 'directProxy'}"> selected</c:if>>直接压apache(nginx)</option>
			<option value="ssl" <c:if test="${loadconfig.httploadProxy == 'ssl'}"> selected</c:if>>ssl方式直接压apache(nginx)</option>
	</select>
	</td>
  </tr >
  <tr >
    <td>httpload路径：
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" style="width:480px;" value="${loadconfig.httploadpath }" name="httploadpath" size="50"/></td>
  </tr > 
 
</table>
</div>
<div id="apache_set" style="width:1050px; height: auto; margin: 0 auto">
<table style="width:100%;align:center">
  <tr >
    <td colspan="2"><span id="configText" "></span>
    <c:choose>
    <c:when test="${loadconfig.loadType == 'apache'}">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </c:when>
    <c:otherwise>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </c:otherwise>
    </c:choose> 
    <input type="text" style="width:480px;" value="${loadconfig.jkConfigPath }" name="jkConfigPath" size="70" />
    </td>
  </tr >
   <tr >
    <td colspan="2"><span id="runText" ></span>
    <c:choose>
    <c:when test="${loadconfig.loadType == 'apache'}">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </c:when>
    <c:otherwise>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </c:otherwise>
    </c:choose>
    <input type="text" style="width:480px;" value="${loadconfig.apachectlPath }" name="apachectlPath" size="50"/></td>
  </tr>  

</table>
</div>

<div id="script_set" style="width:1050px; height: auto; margin: 0 auto">
<table style="width:100%;align:center">
  <tr >
    <td colspan="2">启动脚本：<span id="start"></span>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" value="${loadconfig.startScript }" name="startScript" size="70"/></td>
  </tr >
   <tr >
    <td colspan="2">停止脚本：<span id="end" ></span> 
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" value="${loadconfig.endScript }" name="endScript" size="50"/></td>
  </tr>  

</table>
</div>
</form>
</div>
</div>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
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
	} else if(type == 'hsf' && mode == 'SSH'){
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
		$("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
	} else if(type == 'httpLoad' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
		$("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
	} else if(type == 'apacheProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("apache启动路径： ");
	} else if(type == 'nginxProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show()
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("nginx启动路径： ");
	} else if(type == 'apache' && mode == 'SCRIPT') { //分流就显示
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("填写分流比例 4_1,4_2,4_3,5_9 。前面表示本地流量，后面表示分流流量");
		$("#loadFeatureDesc").text("说明:apache 存放ip1,ip2,ip3,...");
		$("#configText").text("jk配置文件路径： ");
		$("#runText").text("apache启动路径： ");
	} else if(type == 'hsf' && mode == 'SCRIPT'){
		$("#script_set").show;
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
		$("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
	} else if(type == 'httpLoad' && mode == 'SCRIPT') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
		$("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
	} else if(type == 'apacheProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("SSH方式无需填写；B2b的SCRIPT方式为必填项，格式:80:2_80:4,80:2_80:5（端口号:权重)");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("apache启动路径： ");
	} else if(type == 'nginxProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("无需填写");
		$("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
		$("#configText").text("proxy配置文件路径： ");
		$("#runText").text("nginx启动路径： ");
	}
}

showDivByType();
httploadselfLog();

function check() {
	var appName = '${loadconfig.appName}';
	var target = $("#hostIp").attr("value");
	var loadType = $("#loadrunType").val();
	var loadFuture = $("#loadFeature").attr("value");
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
