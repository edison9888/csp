<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.*"%>


<%@page import="com.taobao.monitor.common.po.ServerAppRelPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="com.taobao.monitor.common.ao.center.*"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�û�����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/axurerppage.css" type="text/css" rel="stylesheet">
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.accordion.js"></script>


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

<%
String appId = request.getParameter("appId");
String action = request.getParameter("action");
String hostId = request.getParameter("hostId");
String appType = request.getParameter("appType");
String appName = request.getParameter("appName");
String opsName = request.getParameter("opsName");
String site = request.getParameter("site");
String type = request.getParameter("type");

AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(Integer.parseInt(appId));


%>


<script type="text/javascript">

function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_host_check1.jsp?appId=<%=appId%>&appName=<%=appName%>&opsName=<%=opsName%>&site=<%=site%>";
	location.href=url;
}

function checkAlarm(){
	var objs=document.getElementsByName('selectId');
	var isSel = false;
	for(var i=0;i<objs.length;i++)
	{
	  if(objs[i].checked==true)
	   {
	    isSel=true;
	    break;
	   }
	}
	if(isSel==false){
		alert("�Բ���!��û��ѡ���κε�����!"); 
		return false;
	}else{
		if(!checkAlarmSaveData()){

			return false;
		};
		return true;
	} 
	
}

//�־ñ��������жϣ����ܳ���6��
function checkAlarmSaveData(){
	var sumOfSaveData = <%=HostAo.get().sumOfSaveData(Integer.parseInt(appId))%>;
	var f = $('input[type=checkbox][id=saveData2]');
//	alert(f.get(0).checked);
	var size=0;
	for(var i=0;i<f.length;i++){
		if(f.get(i).checked == true){
			size++;
		}
	}
	
	if(size + sumOfSaveData > 6 ){
		alert("�Բ���!�־ñ��ܳ���6��,�Ѿ�������" + sumOfSaveData + "�����ݿ�"); 
		return false;
	} 

	return true;
}

$(function(){

	// ����һ����ѡ��������ȫѡ/ȫ��ѡ
		$("#checkHostId").click(function(){
	  
			if(this.checked){     //�����ǰ����Ķ�ѡ��ѡ��
		   		$('input[type=checkbox][name=selectId]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][name=selectId]').attr("checked", false );
		 	}
		})
		$("#checkSaveData1").click(function(){
	  
			if(this.checked){     //�����ǰ����Ķ�ѡ��ѡ��
		   		$('input[type=checkbox][id=saveData1]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][id=saveData1]').attr("checked", false );
		 	}
		})
		$("#checkSaveData2").click(function(){
	  
			if(this.checked){     //�����ǰ����Ķ�ѡ��ѡ��
		   		$('input[type=checkbox][id=saveData2]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][id=saveData2]').attr("checked", false );
		 	}
		})

})



</script>

</head>
<body>



<%

request.setCharacterEncoding("gbk");


/*
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
*/
%>

<form action="./rel_app_host_add1.jsp" onsubmit="return checkAlarm()"  method="post" name="myform" id="myform">
<%


if("add".equals(action)){
	
	String[] selectHostIps = request.getParameterValues("selectId");
	List<HostPo> selectedHostList = new ArrayList<HostPo>();
	int sumOfSaveData2 = 0;		//��ǳ־ñ����
	for(String ip:selectHostIps){
		String hostName = request.getParameter("hostName_"+ip);
		String hostSite = request.getParameter("hostSite_"+ip);
		String saveData1 = request.getParameter("saveData1_"+ip);
		String saveData2 = request.getParameter("saveData2_"+ip);
		if(saveData2 != null) {
			
			sumOfSaveData2++;	//ͳ�ƹ�ѡ�־ñ����
		}
		//System.out.println(saveData2);
		HostPo po = new HostPo();
		po.setAppId(Integer.parseInt(appId));
		po.setHostIp(ip);
		po.setHostName(hostName);
		po.setHostSite(hostSite);
		String saveData = (saveData1==null?"0":"1")+(saveData2==null?"0":"1");
		po.setSavedata(saveData);
		po.setUserName(appInfo.getLoginName());
		po.setUserPassword(appInfo.getLoginPassword());
		
		
		
		selectedHostList.add(po);
	}
		int all = sumOfSaveData2 + HostAo.get().sumOfSaveData(Integer.parseInt(appId));	//��ǰѡ��ĳ־ñ�����ݿ��Ѿ����ڵĳ־ñ���ۼ�
		boolean b = false;
		if(all <= 6) {		//����־ñ����6���Ͳ�ִ����ӣ��������ʧ��ҳ��
			
			b = HostAo.get().addHostList(selectedHostList);		
		}
	
	%>
		<font size="+3" color="red"><%if(b){out.print("��ӳɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
		<%
		if("day".equals(type)) {
			
		%>
			<a href="./rel_app_day.jsp?&appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>">����</a>	
	 	<%
		} else {
	 	%>
	 	<a href="./rel_app_time_noAction.jsp?&appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>">����</a>	
	 	<%
	 	}
	 	%>
	<%
}

else{

	List<HostPo>  hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getOpsName());
	List<HostPo>  cmHostList =new ArrayList<HostPo>();
	for(HostPo p : hostList) {
		
		if(!HostAo.get().isExistHostByHostIpAndAppId(Integer.parseInt(appId),p.getHostIp())) {
			
			cmHostList.add(p);
		}
	}
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ӹ�������&nbsp;&nbsp;
�ܸ�����<%=cmHostList.size() %>&nbsp;&nbsp;
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
	  	<td width="50" align="center">
	  	<input name='check' type='checkbox' id='checkHostId' value='checkbox'></td>
		<td align="center">������</td>
		<td align="center">����IP</td>
		<td align="center">����</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData1'>&nbsp;&nbsp;��ʱ��</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData2'>&nbsp;&nbsp;�־ñ�</td>
  </tr>  
	
	<%	

		for(HostPo po : cmHostList) {

	%>	
	<tr>
		<td align="center"><input type="checkbox" name='selectId' id='selectId' value="<%=po.getHostIp() %>"></td>
		<td align="center" class="hostName"><input type="text" name="hostName_<%=po.getHostIp() %>" value="<%=po.getHostName() %>"></td>
		<td align="center" class="hostIp"><input type="text" name="hostIp_<%=po.getHostIp() %>" value="<%=po.getHostIp() %>"></td>
		<td align="center" class="hostSite"><input type="text" name="hostSite_<%=po.getHostIp() %>" value="<%=po.getHostSite() %>"></td>
		<td align="center" class="saveData1"><input type="checkBox" name="saveData1_<%=po.getHostIp() %>" id="saveData1" class="saveData1" value="1" checked="checked"></td>
		<td align="center" class="saveData2"><input type="checkBox" name="saveData2_<%=po.getHostIp() %>" id="saveData2" class="saveData2" value="1"></td>
	</tr>
	<%
		}
	
		if(0 != cmHostList.size()) {
			
	%>
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" name="appId" value="<%=appId %>">
		<input type="hidden" name="appName" value="<%=appName %>">
		<input type="hidden" name="opsName" value="<%=opsName %>">
		<input type="hidden" name="type" value="<%=type %>">
		<input type="hidden" value="add" name="action">
		<!--<input type="button" value="�����������" onclick="checkAlarm()">-->
		<input type="submit" value="�����������">		
		<input type="button" value="����" onclick="goToCheck()">
		</td>
	</tr>
</table>
</div>
	<%
		}
		else {
	%>
	<table>
	<tr>
		<td colspan="6" align="center"><font color="red" size="4">
		�����������Ѿ���ӣ��뷵��</font>
		<input type="button" value="����" onclick="goToCheck()"></td>
	</tr>
	</table>
	<%	
		}
	%>
<%} %>
</form>
</body>
</html>