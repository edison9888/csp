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
<%@page import="com.taobao.monitor.common.ao.center.*"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.common.util.*"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%><html>
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
AppInfoPo appInfo = AppCache.get().getKey(Integer.parseInt(appId));
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
		return true;
	} 
	
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

<form action="./rel_app_host_addSingle.jsp"  method="post" name="myform" id="myform">
<%


if("add".equals(action)){
	
	String hostIp = request.getParameter("hostIp");
	List<HostPo> selectedHostList = new ArrayList<HostPo>();
	String hostName = request.getParameter("hostName");
	String hostSite = request.getParameter("hostSite");
	String saveData1 = request.getParameter("saveData1");
	String saveData2 = request.getParameter("saveData2");
	
	HostPo po = new HostPo();
	po.setAppId(Integer.parseInt(appId));
	po.setHostIp(hostIp);
	po.setHostName(hostName);
	po.setHostSite(hostSite);
	String saveData = (saveData1==null?"0":"1")+(saveData2==null?"0":"1");
	po.setSavedata(saveData);
	po.setUserName(appInfo.getLoginName());
	po.setUserPassword(appInfo.getLoginPassword());
	
	
	selectedHostList.add(po);
				
	boolean b = HostAo.get().addHostList(selectedHostList);
		
	%>
		<font size="+3" color="red"><%if(b){out.print("��ӳɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
		<%
		if("day".equals(type)) {
			
		%>
			<a href="./rel_app_day.jsp?&appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>">����</a>	
	 	<%
		} else {
	 	%>
	 	<a href="./rel_app_time.jsp?&appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>">����</a>	
	 	<%
	 	}
	 	%>
	<%
}

else{

	
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ӹ�������&nbsp;&nbsp;
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>	  	
		<td align="center">������</td>
		<td align="center">����IP</td>
		<td align="center">����</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData1'>&nbsp;&nbsp;��ʱ��</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData2'>&nbsp;&nbsp;�־ñ�</td>
  </tr>
	<tr>
		<td align="center" class="hostName"><input type="text" name="hostName" value=""></td>
		<td align="center" class="hostIp"><input type="text" name="hostIp" value=""></td>
		<td align="center" class="hostSite"><input type="text" name="hostSite" value=""></td>
		<td align="center" class="saveData1"><input type="checkBox" name="saveData1" id="saveData1" class="saveData1" value="1" checked="checked"></td>
		<td align="center" class="saveData2"><input type="checkBox" name="saveData2" id="saveData2" class="saveData2" value="1"></td>
	</tr>	
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
<%} %>
</form>
</body>
</html>