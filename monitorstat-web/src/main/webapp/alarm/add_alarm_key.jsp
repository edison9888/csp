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
<title>应用监控系统-新增告警key</title>
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
return confirm("想清楚要删除了没有啊");
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
	out.print("你没有权限操作!");
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
<font size="+2" color="red"><%if(addStatus){out.print("成功");}else{out.print("失败!已经存在");} %></font>
<input type="button" value="返回列表" onclick="location.href='./manage_key.jsp'">
<%
}else{
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">查询条件</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./add_alarm_key.jsp" method="post">
	<table border="1">
		<tr>
			<td>key名称:</td>
			<td><%=keyInfo.getKeyName() %></td>
		</tr>
		<tr>
			<td>别名:</td>
			<td><%=keyInfo.getAliasName() %></td>
		</tr>
		<tr>
			<td>key类型:</td>
			<td><%=keyInfo.getKeyType() %></td>
		</tr>
		<tr>
			<td>应用:</td>
			<td><%=appInfo.getAppName() %><input type="hidden" name="appId" value="<%=appInfo.getAppId() %>"/></td>
		</tr>
		<tr>
			<td>配置值:</td>
			<td>
			<input type="text" value="" size="100" name="alarmData"/>			
			</td>
		</tr>
		<tr>
			<td>key的重要性:</td>
			<td>
				<select name="keySendType">
					<option value="1">普通</option>
					<option value="2">重要</option>
					<option value="3">紧急</option>
				</select>	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>			
			<td  >
				
		<font color="red">	结构如:-1#2500$00:10#23:59;-1#2500$10:10#23:59;
			-1#2500 表示下限和上限，00:10#10:59 表示开始时间和结束时间
			-1表示无上限或下限。<br/>
			设置百分比波动 是 如:-1#150$00:10#10:59  表示 波动超过150%是告警
			(特殊 一个key为 响应时间 单位是微妙250阀值 需要设置为250000) <br/>
			设置多台统计 是 如：80$120;70$130 表示多台统计与前一分钟比值低于80%或者高于120%并且与前一星期比值低于70%或者高于130%告警
			
			</font>
			
			</td>
		</tr>
		<tr>
			<td>策略:</td>
			<td><select name="useType">
			<option value="1">阀值</option>
			<option value="2">百分比波动</option>
			<option value="3">多台统计</option>
			 </select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>			
			<td  >			
				<font color="red">阀值:表示以上下限为报警条件。百分比波动：表示以当前值与前面2个值的平均的百分比。多台统计：对收集的所有机器百分比控制，比较对象为（当前1分钟）与（前1分钟及前一星期当前1分钟）的双重控制。</font>
			</td>
		</tr>
		<tr>
			<td>告警描述:</td>
			<td><input type="text" value="<%=keyInfo.getAliasName()==null?"":keyInfo.getAliasName() %>" size="100" name="desc"/>	</td>
		</tr>
	</table>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">关联脚本&nbsp;&nbsp;&nbsp;<a id="addKeyScriptRel" href="./alarm_keyScript_rel.jsp?keyId=<%=keyInfo.getKeyId() %>&appId=<%=appInfo.getAppId()%>" STYLE="text-decoration:underline"><font color='red'>-->选择脚本</font></a> &nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
	List<KeyScripRelation> keyScriptRelList= ScriptAo.get().findScriptRelByKeyIdAndAppId(Integer.parseInt(keyId),Integer.parseInt(appId));
	%>
	<table  border="1">
	<tr>
		<td align="center">脚本名</td>
		<td align="center">操作</td>
	</tr>
	<%
	if(keyScriptRelList != null) {
		for(KeyScripRelation ksrel : keyScriptRelList) {
		%>
	<tr>
		<td align="center"><%=ScriptAo.get().findScriptById(ksrel.getScriptId()).getScriptName()%></td>
		<td align="center"><a class="deleteAction" href="./add_alarm_key.jsp?action=deleteScriptRel&appId=<%=appId %>&keyId=<%=keyId %>&scriptId=<%=ksrel.getScriptId() %>">删除</a></td>
	</tr>
		<%
		}
	}
%>
</table>
</div>
</div>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">例外机器&nbsp;&nbsp;&nbsp;<a id="addIframe" href="./alarm_keyHost_rel.jsp?keyId=<%=keyInfo.getKeyId() %>&appId=<%=appInfo.getAppId()%>" STYLE="text-decoration:underline"><font color='red'>-->选择主机</font></a> &nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
	List<ExtraKeyAlarmDefine> extraKeyAlarmList= MonitorAlarmAo.get().getExtraKeyAlarmDefine(Integer.parseInt(appId),Integer.parseInt(keyId));
	%>
	<table  border="1">
	<tr>
		<td align="center">IP</td>
		<td align="center">备置</td>
		<td align="center">操作</td>
	</tr>
	<%
	if(extraKeyAlarmList != null) {
		for(ExtraKeyAlarmDefine ek : extraKeyAlarmList) {
		%>
	<tr>
		<td align="center"><%=HostAo.get().findHostByHostId(ek.getHostId()).getHostIp()%></td>
		<td align="center"><%=ek.getAlarmDefine() %></td>
		<td align="center"><a class="deleteAction" href="./add_alarm_key.jsp?action=delete&appId=<%=appId %>&keyId=<%=keyId %>&hostId=<%=ek.getHostId() %>">删除</a></td>
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
			<input type="submit" value="提交"/><input type="button" value="关闭" onclick="window.close()">
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