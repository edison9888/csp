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
<title>核心监控-用户管理</title>
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

<%

String appId = request.getParameter("appId");
String action = request.getParameter("action");
String hostId = request.getParameter("hostId");
String appType = request.getParameter("appType");
String appName = request.getParameter("appName");
String opsName = request.getParameter("opsName");

AppInfoPo appInfo = AppCache.get().getKey(Integer.parseInt(appId));
%>


<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_host_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
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
		alert("对不起!您没有选择任何的主机!"); 
		return false;
	}else{
		return true;
	} 
	
}

$(function(){

	// 用另一个复选框来控制全选/全不选
		$("#checkHostId").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][name=selectId]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][name=selectId]').attr("checked", false );
		 	}
		})
		$("#checkSaveData1").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][id=saveData1]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][id=saveData1]').attr("checked", false );
		 	}
		})
		$("#checkSaveData2").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
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
	out.print("你没有权限操作!");
	return;
}
*/
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./rel_app_host_add.jsp" onsubmit="return checkAlarm()"  method="post" name="myform" id="myform">
<%


if("add".equals(action)){
	
	String[] selectHostIps = request.getParameterValues("selectId");
	List<HostPo> selectedHostList = new ArrayList<HostPo>();
	for(String ip:selectHostIps){
		String hostName = request.getParameter("hostName_"+ip);
		String hostSite = request.getParameter("hostSite_"+ip);
		String saveData1 = request.getParameter("saveData1_"+ip);
		String saveData2 = request.getParameter("saveData2_"+ip);
		
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
				
	boolean b = HostAo.get().addHostList(selectedHostList);
		
	%>
		<font size="+3" color="red"><%if(b){out.print("添加成功");}else{out.print("失败!出现异常");} %></font>	
			<a href="./rel_app_host_check.jsp?&appId=<%=appId %>&appName=<%=appName %>&opsName=<%=opsName %>">返回</a>	
	
	<%
}

else{


	int cm3Num = 0;
	int cm4Num = 0;
	List<HostPo>  hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getOpsName());
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
	for(HostPo p : hostList) {
		
		if(p.getHostSite().equals("CM3")){
			
			cm3Num++;
		} else if(p.getHostSite().equals("CM4")) {
			
			cm4Num++;
		}
	}
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加关联主机&nbsp;&nbsp;
cm3总台数：<%=cm3Num %>&nbsp;&nbsp;
cm4总台数：<%=cm4Num %>&nbsp;&nbsp;

</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
	  	<td width="50" align="center">
	  	<input name='check' type='checkbox' id='checkHostId' value='checkbox'></td>
		<td align="center">主机名</td>
		<td align="center">主机IP</td>
		<td align="center">机房</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData1'>&nbsp;&nbsp;临时表</td>
		<td align="center"><input name='check' type='checkbox' id='checkSaveData2'>&nbsp;&nbsp;持久表</td>
  </tr>  
	
	<%	

		int i = 0;
		for(HostPo po : hostList) {
		
			if(!HostAo.get().isExistHostByHostIpAndAppId(Integer.parseInt(appId),po.getHostIp())) {
				cm3Num++;
				i++;
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
		}
	
		if(i != 0) {
			
	%>
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" name="appId" value="<%=appId %>">
		<input type="hidden" name="appName" value="<%=appName %>">
		<input type="hidden" name="opsName" value="<%=opsName %>">
		<input type="hidden" value="add" name="action">
		<!--<input type="button" value="添加主机关联" onclick="checkAlarm()">-->
		<input type="submit" value="添加主机关联">		
		<input type="button" value="返回" onclick="goToCheck()">
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
		所有主机你已经添加，请返回</font>
		<input type="button" value="返回" onclick="goToCheck()"></td>
	</tr>
	</table>
	<%	
		}
	%>
<%} %>
</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>