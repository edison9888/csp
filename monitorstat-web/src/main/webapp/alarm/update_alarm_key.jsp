<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>Ӧ�ü��ϵͳ-����澯key</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

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
$(function(){
	//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
	$("#keyBodyId tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#keyBodyId tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})
})

</script>
</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
String keyId = request.getParameter("keyId");
String keyName = request.getParameter("keyName");
String keyType = request.getParameter("keyType");
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
String action = request.getParameter("action");
String appId = request.getParameter("appId");

if(!UserPermissionCheck.check(request,"alarmKey",appId)){
	out.print("��û��Ȩ�޲���!");
	return;
}

String alarmData = request.getParameter("alarmData");
String useType = request.getParameter("useType");
String desc = request.getParameter("desc");
String keySendType = request.getParameter("keySendType");
String[] keys =  keyName.split("_");
if(keyName.equals("PV_VISIT_COUNTTIMES")){
	keyType = "pv";
}else if(keyName.equals("PV_REST_AVERAGEUSERTIMES")){
	keyType = "rt";
}else if(keyName.equals("PV_PAGESIZE_AVERAGEUSERTIMES")){
	keyType = "pagesize";
}else if(keys.length>=3){
	keyType = keys[1];	
}else {
	keyType = keyName;	
} 

boolean addStatus = false;
if("add".equals(action)){
	AlarmDataPo po = new AlarmDataPo();
	po.setAlarmDefine(alarmData);
	po.setAlarmType(useType);
	po.setAppId(Integer.parseInt(appId));
	po.setKeyId(keyId);	
	po.setAlarmFeature(desc);
	po.setAlarmAim(keyType);
	po.setKeyType(Integer.parseInt(keySendType));
	addStatus = MonitorAlarmAo.get().addKeyAlarm(po);
}
%>
<%
if("add".equals(action)){
%>
<font size="+3" color="red"><%if(addStatus){out.print("�ɹ�");}else{out.print("ʧ��!�����Ѿ����ڣ�������쳣");} %></font>
<%
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ѯ����</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./add_alarm_key.jsp" method="post">
	<table>
		<tr>
			<td>key����:</td>
			<td><%=keyName %></td>
		</tr>
		<tr>
			<td>key����:</td>
			<td><%=keyType %></td>
		</tr>
		<tr>
			<td>Ӧ��:</td>
			<td><select name="appId">
			<%for(AppInfoPo app:listApp){%> 
			<option value="<%=app.getAppId() %>"><%=app.getAppName() %></option>
			 <%} %>
			 </select></td>
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
				
		<font color="red">	�ṹ��:-1#2500$00:10#10:59;-1#2500$10:10#23:59;
			-1#2500 ��ʾ���޺����ޣ�00:10#10:59 ��ʾ��ʼʱ��ͽ���ʱ��
			-1��ʾ�����޻����ޡ�<br/>
			���ðٷֱȲ��� �� ��:-1#150$00:10#10:59  ��ʾ ��������150%�Ǹ澯 <br/>
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
			<td><input type="text" value="" size="100" name="desc"/>	</td>
		</tr>
		
		<tr >
			<td colspan="2">
			<input type="submit" value="�ύ"/><input type="button" value="�ر�" onclick="window.close()">
			<input type="hidden" value="<%=keyId %>" name="keyId">
			<input type="hidden" value="add" name="action">
			<input type="hidden" value="<%=keyName %>" name="keyName">
			</td>			
		</tr>
	</table>

	
</form>
</div>
</div>

</body>
</html>