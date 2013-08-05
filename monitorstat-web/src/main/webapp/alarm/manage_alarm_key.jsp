<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.*"%>
<%@page import="java.util.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.taobao.monitor.web.vo.*"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>Ӧ�ü��ϵͳ-��ѯ�澯key</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

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
	$("#alarmbody tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#alarmbody tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})
})

$(document).ready(function(){
	$("#getform").submit(function(){
		$("#ecodeKey").val( encodeURI($("#ecodeKey").val()) );
	})
});
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<input type="button" value="�澯�����ҳ��" onclick="location.href='manage_alarm_key.jsp'">
<input type="button" value="key����ҳ��" onclick="location.href='manage_key.jsp'">
<%
String keyName = request.getParameter("keyName");
if(StringUtils.isNotBlank(keyName)){
	keyName = URLDecoder.decode(keyName, "UTF8");
}
String appId = request.getParameter("appId");
String action = request.getParameter("action");
String id = request.getParameter("id");
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
if(appId==null){
	
	appId = listApp.get(0).getAppId()+"";
}




AppInfoPo appInfoPo = AppCache.get().getKey(Integer.parseInt(appId));

if("delete".equals(action)){
	
	if(!UserPermissionCheck.check(request,"alarmKey",appId)){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	
	MonitorAlarmAo.get().deleteKeyAlarm(id);
}
AlarmDataPo alarmDataPo = null;

List<AlarmDataPo> allalarmKeyList = MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(Integer.parseInt(appId),keyName);
if(id!=null){
	for(AlarmDataPo po:allalarmKeyList){
		if(id.equals(po.getId())){
			alarmDataPo = po;
			break;
		}
	}
}

if("update2".equals(action)){
	
	if(!UserPermissionCheck.check(request,"alarmKey",appId)){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	
	String alarmData = request.getParameter("alarmData");
	String useType = request.getParameter("useType");
	String keySendType = request.getParameter("keySendType");
	String desc = request.getParameter("desc");
	if(alarmDataPo!=null){
		alarmDataPo.setAlarmDefine(alarmData);
		alarmDataPo.setAlarmType(useType);
		alarmDataPo.setAlarmFeature(desc);
		alarmDataPo.setKeyType(Integer.parseInt(keySendType));
		MonitorAlarmAo.get().updateKeyAlarm(alarmDataPo);
	}
	
}




%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ѯ����</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form id="getform" action="./manage_alarm_key.jsp" method="get">
	key����:<input id="ecodeKey" name="keyName" value="<%=keyName==null?"":keyName %>">&nbsp;&nbsp;	
	Ӧ����:
	<select id="parentGroupSelect" onchange="groupChange(this)">	
	</select>
	Ӧ����:
	<select name="appId"  id="appIdId">	
	</select>	
	<input value="��ѯ" type="submit">
	<a href="#" onClick="fuck();return false;">ssss</a>
</form>
</div>
</div>

<%
if("update".equals(action)&&alarmDataPo!=null){ 
	if(!UserPermissionCheck.check(request,"alarmKey",appId)){
		out.print("��û��Ȩ�޲���!");
		return;
	}
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�޸�key</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./manage_alarm_key.jsp?action=update2" method="post">
	<table width="100%" border="1" class="ui-widget ui-widget-content">
		<tr>
			<td>Ӧ�����ƣ�</td>
			<td><%=alarmDataPo.getAppName() %></td>
		</tr>
		<tr>
			<td>key����:</td>
			<td><%=alarmDataPo.getKeyName()%></td>
		</tr>
		<tr>
			<td>����ֵ:</td>
			<td>
			<input type="text" value="<%=alarmDataPo.getAlarmDefine() %>" size="100" name="alarmData"/>			
			</td>
		</tr>
		<tr>
			<td>key����Ҫ��:</td>
			<td>
				<select name="keySendType">
					<option value="1" <%if(1==alarmDataPo.getKeyType()){out.print("selected");} %>>��ͨ</option>
					<option value="2" <%if(2==alarmDataPo.getKeyType()){out.print("selected");} %>>��Ҫ</option>
					<option value="3" <%if(3==alarmDataPo.getKeyType()){out.print("selected");} %>>����</option>
				</select>	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>			
			<td  >
				
		<font color="red">	�ṹ��:-1#2500$00:10#10:59;-1#2500$10:10#23:59;
			-1#2500 ��ʾ���޺����ޣ�00:10#10:59 ��ʾ��ʼʱ��ͽ���ʱ��
			-1��ʾ�����޻����ޡ�<br/>
			���ðٷֱȲ��� �� ��:-1#150$00:10#10:59  ��ʾ ��������150%�Ǹ澯<br/>
			���ö�̨ͳ�� �� �磺80$120;70$130 ��ʾ��̨ͳ����ǰһ���ӱ�ֵ����80%���߸���120%������ǰһ���ڱ�ֵ����70%���߸���130%�澯
			</font>
			
			</td>
		</tr>
		<tr>
			<td>����:</td>
			<td><select name="useType">
				<option value="1" <%if("1".equals(alarmDataPo.getAlarmType())){out.print("selected");} %>>��ֵ</option>
				<option value="2" <%if("2".equals(alarmDataPo.getAlarmType())){out.print("selected");} %>>�ٷֱȲ���</option>
				<option value="3" <%if("3".equals(alarmDataPo.getAlarmType())){out.print("selected");} %>>��̨ͳ��</option>
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
			<td><input type="text" value="<%=alarmDataPo.getAlarmFeature() %>" size="100" name="desc"/>	</td>
		</tr>
	
		<tr>
			<td colspan="2"><input type="submit" value="�޸�">
			<input type="hidden" name="id" value="<%=id %>"/>
		<input type="hidden" name="keyName" value="<%=keyName==null?"":keyName %>"/>
		<input type="hidden" name="appId" value="<%=appId==null?"":appId %>"/></td>	
		</tr>
	</table>
</form>
</div>
</div>
<%} %>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�澯key�б�</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>	
	<td>key����</td>	
	<td>����</td>
	<td>��Ҫ��</td>
	<td>����</td>
</tr>	
<%
for(AlarmDataPo po:allalarmKeyList){
	KeyPo keypo = KeyCache.get().getKey(Integer.parseInt(po.getKeyId()));
 	String featrue ="";
 	
 	if(po.getAlarmFeature()!=null&&!"".equals(po.getAlarmFeature().trim())){
 		featrue = po.getAlarmFeature();
 	}else if(keypo.getAliasName()!=null&&!"".equals(keypo.getAliasName().trim())){
 		featrue = keypo.getAliasName();
 	}else {
 		featrue = keypo.getKeyName();
 	}
%>

<tr>	
	<td title="<%=keypo.getKeyName() %>">
	 
	<a style="text-decoration: none;" href='../comm/keyInfo_update.jsp?keyId=<%=keypo.getKeyId()%>'> <%=featrue%></a>
	 </td>
	<td ><%=po.getAlarmDefine()%></td>
	<td >
	<%if(1==po.getKeyType()){out.print("��ͨ");} %>
	<%if(2==po.getKeyType()){out.print("��Ҫ");} %>
	<%if(3==po.getKeyType()){out.print("����");} %>
	</td>
	<td><a href="#" onclick="deleteAlarmKey('<%=po.getId() %>','<%=appId==null?"":appId %>')">ɾ��</a>&nbsp;&nbsp;<a href="manage_alarm_key.jsp?action=update&id=<%=po.getId() %>&keyName=<%=keyName==null?"":keyName %>&appId=<%=appId==null?"":appId %>">�޸�</a>&nbsp;&nbsp;<a target="_blank"  href="<%=request.getContextPath() %>/time/key_detail_time.jsp?appId=<%=po.getAppId() %>&keyId=<%=po.getKeyId() %>&appName=<%=appInfoPo.getAppName() %>&aimName=<%=po.getKeyName()%>">�鿴</a></td>
</tr>	
<%} %>
</table>

</div>
</div>

<script type="text/javascript">
	$(function(){		
		$("#appIdId").val("<%=appId%>");
		})
		
		
		function deleteAlarmKey(id,appId){
			if(window.confirm('ȷ��ɾ��?')){
				location.href="manage_alarm_key.jsp?action=delete&id="+id+"&appId="+appId;
			}
		}

</script>

<script type="text/javascript">
var groupMap ={}

function addAppGroup(groupName,appName,appId){
		
		if(!groupMap[groupName]){
			groupMap[groupName]={};
		}
		if(!groupMap[groupName][appName]){
			groupMap[groupName][appName]=appId;
		}			
}
	
	function groupChange(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}
	
	function clearSubSelect(){
		 document.getElementById("appIdId").options.length=0;		
		
	}
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appIdId").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appIdId").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appIdId").options[len-1].selected=true;
			}
		}
	}
	
	function initParentSelect(gname,gvalue){
		clearSubSelect();
		var len = document.getElementById("parentGroupSelect").options.length;
		for (name in groupMap){
			document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
			if(name == gname){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
				
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	
	}
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:listApp){
	%>
	addAppGroup("<%=app.getGroupName()%>","<%=app.getAppName()%>","<%=app.getAppId()%>")
	<%				
	}
	%>
	 initParentSelect("<%=appInfoPo.getGroupName()%>","<%=appInfoPo.getAppName()%>");
</script>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>