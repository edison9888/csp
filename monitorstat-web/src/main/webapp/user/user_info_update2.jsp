<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.util.SessionUtil"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>

<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorExtraKeyAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�û�����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script>
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


</head>
<body>



<%

request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("��û��Ȩ�޲���!");
	return;
}

%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="user_info_update2.jsp" method="post" onsubmit="return CheckForm()">

<%
String id =request.getParameter("id");
List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();

String action = request.getParameter("action");
String selectApp = request.getParameter("selectApp");					//ƴ�Ӻø澯��Ӧ�ã�   appId,appId,appId,
String selectReportApp = request.getParameter("selectReportApp");		//ƴ�Ӻñ���ѡ���Ӧ�ã�   reportId:appId,appId,appId;
String relKey = request.getParameter("relKey");							//ƴ�Ӻ�Ӧ�õĸ澯key��   appId:keyId,keyId;
if("update".equals(action)){
	String name = request.getParameter("name");
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String permissionDesc =request.getParameter("permissionDesc");
	String mail =request.getParameter("mail");
	
	String phoneDesc = "";

	
	//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
	for(int i=1;i<=7;i++){
		String phone_week = request.getParameter("phone_week_"+i);
		String phone_num = request.getParameter("phone_num_"+i);
		if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
			phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
		}
	}
	String wangwangDesc = "";
	//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
	for(int i=1;i<=7;i++){
		String ww_week = request.getParameter("wangwang_week_"+i);
		String ww_num = request.getParameter("wangwang_num_"+i);
		if(ww_week!=null&&!"".equals(ww_week.trim())&&ww_num!=null&&!"".equals(ww_num.trim())){
			wangwangDesc+=ww_num+"#"+i+"#"+ww_week+"$";
		}
	}
	
	
	LoginUserPo loginUserPo = new LoginUserPo();
	loginUserPo.setName(name);
	loginUserPo.setPhone(phone);
	loginUserPo.setWangwang(wangwang);
	loginUserPo.setSendPhoneFeature(phoneDesc);
	loginUserPo.setSendWwFeature(wangwangDesc);
	loginUserPo.setMail(mail);
	loginUserPo.setId(Integer.parseInt(id));
	if(permissionDesc != null) {
		loginUserPo.setPermissionDesc(permissionDesc);
	}
	loginUserPo.setGroup(selectApp);
	if(loginUserPo.getPermissionDesc()==null){
		loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");
	}else if(!permissionDesc.contains("alarmKey:ALL;")){
		loginUserPo.setPermissionDesc(loginUserPo.getPermissionDesc().replaceAll("alarmKey:[\\w,]*;","alarmKey:"+selectApp+";"));
	}
	//loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");	//Ӧ�õ�ƴ��
	
	String report="";
	for(ReportInfoPo info:listReportList){
		int reportId = info.getId();
		if(info.getType()==0){
			String value = request.getParameter("report_"+reportId);
			if(value!=null){
				report += reportId+";";
			}
		}else{
			continue;
		}
	}
	
	report += selectReportApp;		//�����ƴ��
	loginUserPo.setReportDesc(report);
	boolean b = MonitorUserAo.get().updateLoginUserPo(loginUserPo);
	//===========
	boolean bb = false;
	
	if(b) {
		int userId = MonitorUserAo.get().getLoginUserPo(name).getId();
		boolean del = MonitorExtraKeyAo.get().deleteRelWithUserId(userId);	//ɾ��user��ǰ�Ĺ���

		

		if(!relKey.equals("")) {
			String[] appKeyStrs = relKey.split(";");
			for(String  appKeyStr: appKeyStrs) {
				
				//appKeyStr��ֵΪ  appId:keyId,keyId;
				String[] appAndKey = appKeyStr.split(":");
				String app = appAndKey[0];			//����appId
				String[] keyIds = appAndKey[1].split(",");
				List<String> keyList = new ArrayList<String>();
				for(String key : keyIds) {
					
					keyList.add(key);
				}
				bb = MonitorExtraKeyAo.get().addUserAndKeyRel(userId, Integer.parseInt(app),keyList);
			}
		} else {
			
			bb = true;		//���ǵ�û��key������ʱ����ô���ǰ���ǰ��ɾ������Ȼ����Ϊ���³ɹ�
		}

	}
	%>
	<font size="+3" color="red"><%if(b && bb){out.print("�ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
	<a href="./manage_user.jsp">����</a>
<%
}else{
LoginUserPo po = MonitorUserAo.get().getLoginUserPo(Integer.parseInt(id));
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
List<String> appList = new ArrayList<String>();	//��������Ѿ�ѡ���app

String appGroup = po.getGroup();	//alarmKey:98,97,2,96,1,; ���� alarmKey:;
String initReportDesc = po.getReportDesc();			//�û��Ѿ�ѡ��ı���
String initSelectedApp = "";						//�û��Ѿ�ѡ���app
if(appGroup != null) {
	
	String[] apps = appGroup.split(",");

	for(String a : apps) {
		if(!a.equals("")) {
			initSelectedApp += a + ",";
			appList.add(a);
		}
	}
}

//ƴ�ӳ�  appId:keyId,keyId;
String initAppKeyRel = "";
for(String a : appList) {

	List<Integer> key = MonitorExtraKeyAo.get().findUserAndKeyRel(Integer.parseInt(id), Integer.parseInt(a));

	if(key.size() != 0) {
		initAppKeyRel += a + ":";
		for(int i =0; i < key.size();i++) {
			System.out.println(key.size());
			if(i != key.size()-1) {
				
				initAppKeyRel += key.get(i) + ",";
			} else {
				
				initAppKeyRel += key.get(i) + ";";
			}
		}
	}
}

%>
<script type="text/javascript">



</script>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�û�������Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td>�û���:<%=po.getName() %>
		<input type="hidden" name="name" value="<%=po.getName() %>"></td>
		<td>������:<input type="text" name="wangwang" value="<%=po.getWangwang() %>"></td>
		<td>�ֻ���:<input type="text" name="phone" value="<%=po.getPhone() %>"></td>
		<td>����:<input type="text" name="mail" value="<%=po.getMail() %>"></td>
		</tr>
		<tr >
	
		<%
		LoginUserPo loginUserPo = SessionUtil.getUserSession(request);
		if(loginUserPo.getWangwang().equals("С��") || loginUserPo.getWangwang().equals("xiaodu") ||loginUserPo.getWangwang().equals("test")) {
			%>
				<td colspan="4">Ȩ���޸�:<input type="text" name="permissionDesc" size="133" value="<%=po.getPermissionDesc() %>"></td>
			<%
		}
		%>
	
	</tr>
</table>
</div>
</div>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�澯������Ϣ��ʽ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">

<tr >
	<td>�ֻ���������:</td>
</tr>
<%
//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
String phoneFeature = po.getSendPhoneFeature();
String[] week = new String[7];
String[] num = new String[7];			
if(phoneFeature!=null){
	String[] featrues = phoneFeature.split("\\$");
	for(String featrue:featrues){
		String[]  _tmp= featrue.split("\\#");
		if(_tmp.length==4){						
			int w = Integer.parseInt(_tmp[1]);
			String time1 = _tmp[2];
			String time2 = _tmp[3];
			week[w-1] = time1+"#"+time2;
			num[w-1] = _tmp[0];
		}
	}
}

%>
<tr>
	<td>��һ:<input type="text" name="phone_week_2"  value="<%=week[1]==null?"":week[1] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_2"  value="<%=num[1]==null?"":num[1] %>" size="2">���Ϸ���</td>
	<td>�ܶ�:<input type="text" name="phone_week_3"  value="<%=week[2]==null?"":week[2] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_3"  value="<%=num[2]==null?"":num[2] %>" size="2">���Ϸ���</td>
	<td>����:<input type="text" name="phone_week_4"  value="<%=week[3]==null?"":week[3] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_4"  value="<%=num[3]==null?"":num[3] %>" size="2">���Ϸ���</td>
	<td>����:<input type="text" name="phone_week_5"  value="<%=week[4]==null?"":week[4] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_5"  value="<%=num[4]==null?"":num[4] %>" size="2">���Ϸ���</td>
	<td>����:<input type="text" name="phone_week_6"  value="<%=week[5]==null?"":week[5] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_6"  value="<%=num[5]==null?"":num[5] %>" size="2">���Ϸ���</td>
	<td>����:<input type="text" name="phone_week_7"  value="<%=week[6]==null?"":week[6] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_7"  value="<%=num[6]==null?"":num[6] %>" size="2">���Ϸ���</td>
	<td>����:<input type="text" name="phone_week_1"  value="<%=week[0]==null?"":week[0] %>"> &nbsp;&nbsp;�澯<input type="text" name="phone_num_1"  value="<%=num[0]==null?"":num[0] %>" size="2">���Ϸ���</td>
	</tr>			
</table>

<%
//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
String wangwangFeature = po.getSendWwFeature();
week = new String[7];
num = new String[7];			
if(wangwangFeature!=null){
	String[] featrues = wangwangFeature.split("\\$");
	for(String featrue:featrues){
		String[]  _tmp= featrue.split("\\#");
		if(_tmp.length==4){						
			int w = Integer.parseInt(_tmp[1]);
			String time1 = _tmp[2];
			String time2 = _tmp[3];
			week[w-1] = time1+"#"+time2;
			num[w-1] = _tmp[0];
		}
	}
}

%>


<table>
	<tr >
		<td>������������:</td>
	</tr>
	<tr>
		<td>��һ:<input type="text" name="wangwang_week_2"  value="<%=week[1]==null?"":week[1] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_2"  value="<%=num[1]==null?"":num[1] %>" size="2">���Ϸ���</td>
		<td>�ܶ�:<input type="text" name="wangwang_week_3"  value="<%=week[2]==null?"":week[2] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_3"  value="<%=num[2]==null?"":num[2] %>" size="2">���Ϸ���</td>
		<td>����:<input type="text" name="wangwang_week_4"  value="<%=week[3]==null?"":week[3] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_4"  value="<%=num[3]==null?"":num[3] %>" size="2">���Ϸ���</td>
		<td>����:<input type="text" name="wangwang_week_5"  value="<%=week[4]==null?"":week[4] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_5"  value="<%=num[4]==null?"":num[4] %>" size="2">���Ϸ���</td>
		<td>����:<input type="text" name="wangwang_week_6"  value="<%=week[5]==null?"":week[5] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_6"  value="<%=num[5]==null?"":num[5] %>" size="2">���Ϸ���</td>
		<td>����:<input type="text" name="wangwang_week_7"  value="<%=week[6]==null?"":week[6] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_7"  value="<%=num[6]==null?"":num[6] %>" size="2">���Ϸ���</td>
		<td>����:<input type="text" name="wangwang_week_1"  value="<%=week[0]==null?"":week[0] %>"> &nbsp;&nbsp;�澯<input type="text" name="wangwang_num_1"  value="<%=num[0]==null?"":num[0] %>" size="2">���Ϸ���</td>
</tr>			
</table>

</div>
</div>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ѡ����Ҫ�澯Ӧ��
<!-- <button id="btn-add-appRel" onclick="openAppSelect()">ѡ��</button> -->
<input type="button" value="ѡ��Ӧ��" onclick="openAppSelect()">
</div>
<!-- ���table��������ajax���Ӵ����л�� -->
<table id="selectAppTable" border="1">
	
<thead>
	<tr>
		<td align='center'>Ӧ����</td>
		<td align='center'>����</td>
	</tr>
</thead>
<tbody>
	<%
	for(String appid : appList) {
		
		%>
		
		<tr id="<%=appid %>"><td align='center'><%=AppCache.get().getKey(Integer.parseInt(appid)).getAppName() %></td>
		<td align='center'>
		<a href="javascript:selectKey('<%=appid %>')">���ø澯key</a>&nbsp;&nbsp;
		<a href="javascript:deleteTableTr('<%=appid %>')">ɾ��</a>
		</td>
		</tr>
		<%
	}
	%>
	</tbody>
	
</table>
</div>

<!-- ��Ӹ澯Ӧ�õĵ����� -->
<div id="dialog_appRel_add" title="dialog">
	<iframe id="iframe_appRel_add" src="" frameborder="0" height="670" width="780" marginheight="0" marginwidth="0" scrolling="yes" ></iframe>
</div>

<script type="text/javascript">

$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
//��Ӹ澯Ӧ��
function openAppSelect () {
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_alarmApp_rel.jsp?selectedApp=" + $("#selectApp").val());
	$("#dialog_appRel_add").dialog("open");
}

function deleteTableTr(appName){

	$("#" + appName).remove();
	var appStrs = $("#selectApp").val().split(",");
	var newAppStr = "";
	for(var i=0; i < appStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���
	
		if(appStrs[i] != appName) {			//app[0]��Ӧ�õ�Id
			newAppStr += appStrs[i] + ",";
		}
	}
	$("#selectApp").val(newAppStr);

	//ɾ��hidden�е�key
	var keyStrs = $("#relKey").val().split(";");//<appID:keyid,keyid>���ƣ�98:21793,21818; 
	var newKeyStr = "";
	for(var i=0; i < keyStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���

		var app = keyStrs[i].split(":");	//98:21793,21818
		
		if(app[0] != appName) {			//app[0]��Ӧ�õ�Id
			newKeyStr += keyStrs[i] + ";";
		}
	}
	$("#relKey").val(newKeyStr);
}

//Ӧ��ѡ��key
function selectKey(appId){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appKey_rel.jsp?appId=" + appId + "&relKey=" + $("#relKey").val());
	$("#dialog_appRel_add").dialog("open");
}

//�������Ӧ�ù���
function selectApp(reportId,selectApp){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	
	$("#iframe_appRel_add").attr("src",("<%=request.getContextPath () %>/user/user_appReport_rel.jsp?reportId=" + reportId + "&relApp=" + selectApp));

	$("#dialog_appRel_add").dialog("open");
}

//�鿴�Ѿ���ӵ�Ӧ��
function checkApp(reportId, reportIdAndApps){

	$("#dialog_appRel_add").dialog({bgiframe: false,height: 710,width:800,modal: true,draggable:false,resizable:false,autoOpen:false});
	//console.log($("#selectReportApp").val());
	$("#iframe_appRel_add").attr("src","<%=request.getContextPath () %>/user/user_appReport_rel_check.jsp?reportId=" + reportId + "&allRel=" + reportIdAndApps);

	$("#dialog_appRel_add").dialog("open");
}

</script>

<table>
	<tr>
		<td align="center">
		<input type="hidden" value="update" name="action">
		<input type="hidden" id="selectApp" name="selectApp" value="<%=initSelectedApp %>">
		<input type="hidden" id="id" name="id" value="<%=id %>">
		<input type="hidden" id="selectReportApp" name="selectReportApp" value="<%=initReportDesc %>">
		<input type="hidden" id="relKey" name="relKey" value="<%=initAppKeyRel %>">
		<input type="submit" value="����">
		<input type="button" value="�ر�" onclick="window.close()">
		</td>
	</tr>
</table>
</form>
<%} %>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>