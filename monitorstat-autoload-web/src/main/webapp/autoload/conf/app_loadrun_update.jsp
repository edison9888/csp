<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("title","�޸�ѹ������"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width:1050px;">
     <div style="width:1050px; height: auto; margin: 0 auto">
<form action="<%=request.getContextPath() %>/loadrun/config.do?method=update" method="post" id="signupForm">
<div class="alert-message block-message success">
			<button type="submit" class="btn primary"> ȷ�ϸ��� </button>
			<button type="button" class="btn primary" onclick="window.close()"> ȡ�� </button>
			<button type="button" class="btn primary" onclick="check()"> ���IP </button>
</div>
<table   id="myTable" style="width:1050px;">
  <tr>
    <td >ѹ��Ӧ��</td>
    <td >
		<font size="4" color="#000">${loadconfig.appName}</font>
		<input type="hidden" name="appId" value="${loadconfig.appId }"/>
		<input type="hidden" name="appName" value="${loadconfig.appName}"/>
	</td>
  </tr>
  <tr>
    <td>ѹ��Ŀ�����IP��</td>
    <td><input type="text" value="${loadconfig.hostIp }" name="hostIp" id="hostIp"/><span for="hostIp"></span>
    <font color="red" id="checkApp"> ˵��:����·��ļ��IP��ť����У��IP��Ӧ���Ƿ�ƥ�� </td>
  </tr>
  <tr>
    <td>ѹ��ʱ��:</td>
    <td>
    <input type="text" value="${loadconfig.startTime }" name="startTime" id="startTime"/>
    <span for="startTime"></span>
    <font color="red" > ˵��:ʹ��cron ���ʽ ��:0 10 9 * * ? ��ʾÿ��9��10ִ��</font></td>
  </tr>
   <tr>
    <td>�Ƿ��Զ�ѹ��</td>
    <td>
    	<select id="loadrunAuto" name="loadAuto">
    		<option value="1" <c:if test="${loadconfig.loadAuto == 1}"> selected</c:if>>�Զ�</option>
			<option value="0" <c:if test="${loadconfig.loadAuto == 0}"> selected</c:if>>���Զ�</option>
		</select>
	</td>
   </tr>
   <tr>
		<td>ѹ������: </td>
		<td>
			<select id="loadrunType" name="loadType" onchange="showDivByType()">
				<c:forEach items="${loadrunTypes}" var="type">
					<option value="${type}" <c:if test="${loadconfig.loadType == type}"> selected</c:if>>${type}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>ѹ��ģʽ: </td>
		<td>
			<select id="loadrunMode" name="loadrunMode" onchange="showDivByType()">
				<c:forEach items="${loadrunModes}" var="mode">
					<option value="${mode}" <c:if test="${loadconfig.loadrunMode == mode}"> selected</c:if>>${mode}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
    	<td width="160">ѹ����Ϣ���գ�������:</td>
    	<td><input type="text" value="${loadconfig.wangwangs }" name="wangwangs" id="wangwangs" style="width:400px;"/> 
    	<font color="red"><span for="wangwangs"></span></font>
    	<font color="red"> ˵��:����ѹ������з��ͳ�������Ϣ ����������÷ֺŷָ� �磺 С��;С��</font>
    	</td>
    </tr>
    <tr>
    	<td>ѹ���������ã�</td>
    	<td>
    	<input type="text" value="${loadconfig.limitFeature }" name="limitFeature" size="60" id="limitFeature" style="width:480px;"/> 
    	<font color="red"><span for="limitFeature"></span></font></td>
    </tr>
     <tr>
    	<td colspan="2">
    	 <font color="red">��������˵��:ѹ�����ݵ������õ�����ʱ����ֹͣ,
    	��ʽΪ:key:value;Qps,Rest,CPU,Load,Apache_Pv,Apache_Rest,Tomcat_Pv,Tomcat_Rest,Jvm_Memeory,Hsf_pv,Hsf_Rest;</font></td>
    </tr>
  <tr>
    <td>�Զ�ѹ�����ã�</td>
    <td><input type="text" style="width:480px;" value="${loadconfig.loadFeature }" name="loadFeature" size="70" id="loadFeature"/>
     <span for="loadFeatureDesc"></span>
     <font id="loadFeatureDesc" style="color:red;">˵��:httpload:ѹ�Ⲣ����5,10,15...,apache ���ip1,ip2,ip3,...,hsf ���ip���� 1,2,3,4,5</font>
     </td>
  </tr>
  <tr>
    <td>���ö���˵����</td>
    <td><input type="text" style="width:480px;" value="${loadconfig.configFeature }" name="configFeature" size="70"/> 
    <font id="configFeatureDesc" style="color:red;"> ˵��:hsf���͵���д�ӿ���Ϣ��apache ��д�������� 4_1,4_2,4_3,5_9��</font>
    </td>
  </tr>
  <tr >
    <td >�����˻���</td>
    <td ><input type="text" value="${loadconfig.userName }" name="userName"/>
    <span for="userName"></span>
    <font color="red"> ˵��:����дӵ��sudoȨ�޵��˻�</font></td>
  </tr>
  <tr >
    <td>�������룺</td>
    <td><input type="password" value="${loadconfig.password }" name="password"/><span for="password"></span>
    <font color="red"> ˵��:����дӵ��sudoȨ�޵��˻�����</font></td>
  </tr> 
  <tr >
    <td width="114">ѹ�⿪ʼִ��url��</td>
    <td><input type="text" style="width:480px;" name="startUrl" size="100"  value="${loadconfig.startUrl }" />
    <font color="red"> url��ʽ�������Э��(http://),��ѹ�⿪ʼǰִ��,���Բ���</font></td>
  </tr >
  <tr >
    <td width="114">ѹ�����ִ��url��</td>
    <td><input type="text" style="width:480px;" name="endUrl" size="100"  value="${loadconfig.endUrl }" />
    <font color="red"> url��ʽ�������Э��(http://),��ѹ�����ǰִ��,���Բ���</font></td>
  </tr >
  <tr>
    <td>Hsf��qps�����Ƿ�ͨ���ɼ�eagleeye:</td>
    <td>
    	<select id="collectEagleeye" name="collectEagleeye">
    		<option value="1" <c:if test="${loadconfig.collectEagleeye == 1}"> selected</c:if>>��</option>
			<option value="0" <c:if test="${loadconfig.collectEagleeye == 0}"> selected</c:if>>�� </option>
		</select>
	</td>
   </tr>
  
</table>
<div id="httpload_set" style="width:1050px; height: auto; margin: 0 auto">
<table id="myTable" >
<tr >
    <td >��־���ͣ� &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
    <td colspan="2">httploadѹ�ⷽʽ�� 
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <select name="httploadProxy">
			<option value="no" <c:if test="${loadconfig.httploadProxy == 'no'}"> selected</c:if>>�޸�ԭʼurlΪip:7001ѹjboss</option>
			<option value="proxy" <c:if test="${loadconfig.httploadProxy == 'proxy'}"> selected</c:if>>����ԭʼurlѹjboss</option>
			<option value="directProxy" <c:if test="${loadconfig.httploadProxy == 'directProxy'}"> selected</c:if>>ֱ��ѹapache(nginx)</option>
			<option value="ssl" <c:if test="${loadconfig.httploadProxy == 'ssl'}"> selected</c:if>>ssl��ʽֱ��ѹapache(nginx)</option>
	</select>
	</td>
  </tr >
  <tr >
    <td>httpload·����
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
    <td colspan="2">�����ű���<span id="start"></span>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" value="${loadconfig.startScript }" name="startScript" size="70"/></td>
  </tr >
   <tr >
    <td colspan="2">ֹͣ�ű���<span id="end" ></span> 
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
	if(type == 'apache' && mode == 'SSH') { //��������ʾ
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
		$("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3...");
		$("#configText").text("jk�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
	} else if(type == 'hsf' && mode == 'SSH'){
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
		$("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
	} else if(type == 'httpLoad' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
		$("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
	} else if(type == 'apacheProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
	} else if(type == 'nginxProxy' && mode == 'SSH') {
		$("#script_set").hide();
		$("#apache_set").show()
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("nginx����·���� ");
	} else if(type == 'apache' && mode == 'SCRIPT') { //��������ʾ
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
		$("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3,...");
		$("#configText").text("jk�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
	} else if(type == 'hsf' && mode == 'SCRIPT'){
		$("#script_set").show;
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
		$("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
	} else if(type == 'httpLoad' && mode == 'SCRIPT') {
		$("#script_set").hide();
		$("#apache_set").hide();
		$("#httpload_set").show();
		$("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
		$("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
	} else if(type == 'apacheProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("SSH��ʽ������д��B2b��SCRIPT��ʽΪ�������ʽ:80:2_80:4,80:2_80:5���˿ں�:Ȩ��)");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("apache����·���� ");
	} else if(type == 'nginxProxy' && mode == 'SCRIPT') {
		$("#script_set").show();
		$("#apache_set").hide();
		$("#httpload_set").hide();
		$("#configFeatureDesc").text("������д");
		$("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
		$("#configText").text("proxy�����ļ�·���� ");
		$("#runText").text("nginx����·���� ");
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
