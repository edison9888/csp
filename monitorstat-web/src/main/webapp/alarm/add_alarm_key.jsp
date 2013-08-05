<%@ page language="java" contentType="text/html; charset=gbk"  pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.alarm.po.ExtraKeyAlarmDefine"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.util.UrlCode"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>

<%@page import="com.taobao.monitor.script.KeyScripRelation"%>
<%@page import="com.taobao.monitor.script.ao.ScriptAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>Ӧ�ü��ϵͳ-�����澯key</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<style type="text/css">
div {
	font-size: 12px;
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
$(function(){
	//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
	$("#keyBodyId tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#keyBodyId tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})
})

$(function(){
$(".deleteAction").click(function(){
return confirm("�����Ҫɾ����û�а�");
});
});


$(document).ready(function() {

	$("#addIframe").fancybox({
		'width'				: '70%',
		'height'			: '80%',
		'autoScale'			: false,
		'showCloseButton'	: true,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe'
	});

	$("#addKeyScriptRel").fancybox({
		'width'				: '70%',
		'height'			: '80%',
		'autoScale'			: false,
		'showCloseButton'	: true,
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic',
		'type'				: 'iframe'
	});
});

</script>
</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
AppInfoPo appInfo = null;
if(appId==null){
	String appName = request.getParameter("appName");
	appInfo = AppCache.get().getKey(appName);
	appId = appInfo.getAppId()+"";
}else{
	appInfo = AppCache.get().getKey(Integer.parseInt(appId));	
}

if(!UserPermissionCheck.check(request,"alarmKey",appInfo.getAppId()+"")){
	out.print("��û��Ȩ�޲���!");
	return;
}

KeyPo keyInfo = KeyCache.get().getKey(Integer.parseInt(keyId));



String keyType = request.getParameter("keyType");
String action = request.getParameter("action");
String keySendType = request.getParameter("keySendType");
String alarmData = request.getParameter("alarmData");
String useType = request.getParameter("useType");
String desc = request.getParameter("desc");
boolean addStatus = false;
if("delete".equals(action)){
	
	String hostId = request.getParameter("hostId");
	MonitorAlarmAo.get().deleteExtraKeyAlarmDefine(Integer.parseInt(appId), Integer.parseInt(keyId),Integer.parseInt(hostId));
} 
if("deleteScriptRel".equals(action)){
	
	String scriptId = request.getParameter("scriptId");
	KeyScripRelation ksrpo = new KeyScripRelation();
	ksrpo.setAppId(Integer.parseInt(appId));
	ksrpo.setKeyId(Integer.parseInt(keyId));
	ksrpo.setPerformanceDefine("");
	ksrpo.setScriptId(Integer.parseInt(scriptId));
	ScriptAo.get().deleteKeyScriptRel(ksrpo);
} 

if("add".equals(action)){
	AlarmDataPo po = new AlarmDataPo();
	po.setAlarmDefine(alarmData);
	po.setAlarmType(useType);
	po.setAppId(Integer.parseInt(appId));
	po.setKeyId(keyId);	
	po.setAlarmFeature(desc);
	po.setAlarmAim(keyInfo.getKeyType());
	po.setKeyType(Integer.parseInt(keySendType));
	addStatus = MonitorAlarmAo.get().addKeyAlarm(po);
}
%>
<%
if("add".equals(action)){
%>
<font size="+2" color="red"><%if(addStatus){out.print("�ɹ�");}else{out.print("ʧ��!�Ѿ�����");} %></font>
<input type="button" value="�����б�" onclick="location.href='./manage_key.jsp'">
<%
}else{
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ѯ����</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./add_alarm_key.jsp" method="post">
	<table border="1">
		<tr>
			<td>key����:</td>
			<td><%=keyInfo.getKeyName() %></td>
		</tr>
		<tr>
			<td>����:</td>
			<td><%=keyInfo.getAliasName() %></td>
		</tr>
		<tr>
			<td>key����:</td>
			<td><%=keyInfo.getKeyType() %></td>
		</tr>
		<tr>
			<td>Ӧ��:</td>
			<td><%=appInfo.getAppName() %><input type="hidden" name="appId" value="<%=appInfo.getAppId() %>"/></td>
		</tr>
		<tr>
			<td>����ֵ:</td>
			<td>
			<input type="text" value="" size="100" name="alarmData"/>			
			</td>
		</tr>
		<tr>
			<td>key����Ҫ��:</td>
			<td>
				<select name="keySendType">
					<option value="1">��ͨ</option>
					<option value="2">��Ҫ</option>
					<option value="3">����</option>
				</select>	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>			
			<td  >
				
		<font color="red">	�ṹ��:-1#2500$00:10#23:59;-1#2500$10:10#23:59;
			-1#2500 ��ʾ���޺����ޣ�00:10#10:59 ��ʾ��ʼʱ��ͽ���ʱ��
			-1��ʾ�����޻����ޡ�<br/>
			���ðٷֱȲ��� �� ��:-1#150$00:10#10:59  ��ʾ ��������150%�Ǹ澯
			(���� һ��keyΪ ��Ӧʱ�� ��λ��΢��250��ֵ ��Ҫ����Ϊ250000) <br/>
			���ö�̨ͳ�� �� �磺80$120;70$130 ��ʾ��̨ͳ����ǰһ���ӱ�ֵ����80%���߸���120%������ǰһ���ڱ�ֵ����70%���߸���130%�澯
			
			</font>
			
			</td>
		</tr>
		<tr>
			<td>����:</td>
			<td><select name="useType">
			<option value="1">��ֵ</option>
			<option value="2">�ٷֱȲ���</option>
			<option value="3">��̨ͳ��</option>
			 </select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>			
			<td  >			
				<font color="red">��ֵ:��ʾ��������Ϊ�����������ٷֱȲ�������ʾ�Ե�ǰֵ��ǰ��2��ֵ��ƽ���İٷֱȡ���̨ͳ�ƣ����ռ������л����ٷֱȿ��ƣ��Ƚ϶���Ϊ����ǰ1���ӣ��루ǰ1���Ӽ�ǰһ���ڵ�ǰ1���ӣ���˫�ؿ��ơ�</font>
			</td>
		</tr>
		<tr>
			<td>�澯����:</td>
			<td><input type="text" value="<%=keyInfo.getAliasName()==null?"":keyInfo.getAliasName() %>" size="100" name="desc"/>	</td>
		</tr>
	</table>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�����ű�&nbsp;&nbsp;&nbsp;<a id="addKeyScriptRel" href="./alarm_keyScript_rel.jsp?keyId=<%=keyInfo.getKeyId() %>&appId=<%=appInfo.getAppId()%>" STYLE="text-decoration:underline"><font color='red'>-->ѡ��ű�</font></a> &nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
	List<KeyScripRelation> keyScriptRelList= ScriptAo.get().findScriptRelByKeyIdAndAppId(Integer.parseInt(keyId),Integer.parseInt(appId));
	%>
	<table  border="1">
	<tr>
		<td align="center">�ű���</td>
		<td align="center">����</td>
	</tr>
	<%
	if(keyScriptRelList != null) {
		for(KeyScripRelation ksrel : keyScriptRelList) {
		%>
	<tr>
		<td align="center"><%=ScriptAo.get().findScriptById(ksrel.getScriptId()).getScriptName()%></td>
		<td align="center"><a class="deleteAction" href="./add_alarm_key.jsp?action=deleteScriptRel&appId=<%=appId %>&keyId=<%=keyId %>&scriptId=<%=ksrel.getScriptId() %>">ɾ��</a></td>
	</tr>
		<%
		}
	}
%>
</table>
</div>
</div>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�������&nbsp;&nbsp;&nbsp;<a id="addIframe" href="./alarm_keyHost_rel.jsp?keyId=<%=keyInfo.getKeyId() %>&appId=<%=appInfo.getAppId()%>" STYLE="text-decoration:underline"><font color='red'>-->ѡ������</font></a> &nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
	List<ExtraKeyAlarmDefine> extraKeyAlarmList= MonitorAlarmAo.get().getExtraKeyAlarmDefine(Integer.parseInt(appId),Integer.parseInt(keyId));
	%>
	<table  border="1">
	<tr>
		<td align="center">IP</td>
		<td align="center">����</td>
		<td align="center">����</td>
	</tr>
	<%
	if(extraKeyAlarmList != null) {
		for(ExtraKeyAlarmDefine ek : extraKeyAlarmList) {
		%>
	<tr>
		<td align="center"><%=HostAo.get().findHostByHostId(ek.getHostId()).getHostIp()%></td>
		<td align="center"><%=ek.getAlarmDefine() %></td>
		<td align="center"><a class="deleteAction" href="./add_alarm_key.jsp?action=delete&appId=<%=appId %>&keyId=<%=keyId %>&hostId=<%=ek.getHostId() %>">ɾ��</a></td>
	</tr>
		<%
		}
	}
%>
</table>
</div>
</div>



<table>
	<tr >
			<td colspan="2" align="center">
			<input type="submit" value="�ύ"/><input type="button" value="�ر�" onclick="window.close()">
			<input type="hidden" value="<%=keyId %>" name="keyId">
			<input type="hidden" value="add" name="action">
			</td>			
		</tr>

</table>
	
</form>
</div>
</div>
<%} %>
</body>
</html>