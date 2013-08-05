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
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/axurerppage.css" type="text/css" rel="stylesheet">
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

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

String databaseId = request.getParameter("databaseId");
String action = request.getParameter("action");


%>


<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/db_rel_app_day_center.jsp?databaseId=<%=databaseId%>";
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
		alert("对不起!您没有选择任何的应用!"); 
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


<!--
function clock(i){i=i-1
document.title="本窗口将在"+i+"秒后自动关闭!";
if(i>0)setTimeout("clock();",1000);
else self.close();}
clock();
//-->

			
</script>

</head>
<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
	return;
}
%>
<form action="./db_rel_app_day_add.jsp" onsubmit="return checkAlarm()"  method="post" name="myform" id="myform">
<%


if("add".equals(action)){
	
	String[] selectAppIds = request.getParameterValues("selectId");
	List<DatabaseAppRelPo> selectedAppList = new ArrayList<DatabaseAppRelPo>();
	for(String id : selectAppIds){
		
		DatabaseAppRelPo po = new DatabaseAppRelPo();
		po.setAppId(Integer.parseInt(id));
		po.setAppType("day");
		po.setDatabaseId(Integer.parseInt(databaseId));
		
		selectedAppList.add(po);
	}
				
	boolean b = AppInfoAo.get().addDatabaseRelList(selectedAppList);
		
	%>
		<font size="+3" color="red"><%if(b){out.print("添加成功! 	<input type='button' value='关闭' onclick='parent.location.reload()'>");}else{out.print("失败!出现异常");} %></font>	
			<!--<a href="./db_rel_app_day_center.jsp?serverId=<%=databaseId%>">关闭</a>	-->
			<%
}

else{


	List<AppInfoPo>  appList = AppInfoAo.get().findAllAppWithoutDatabaseRel("day");

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
<font size="3">添加还没没有关联的应用&nbsp;&nbsp;</font>

</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">

	<tr>
	  	<td width="50" align="center">
	  	<input name='check' type='checkbox' id='checkHostId' value='checkbox'></td>
		<td align="center">应用名</td>
  </tr>  
	
	<%	
		int i = 0;
		for(AppInfoPo po : appList) {
			i++;

	%>
	<tr>
		<td align="center"><input type="checkbox" name='selectId' id='selectId' value="<%=po.getAppId() %>"></td>
		<td align="center" class="appName"><%=po.getAppName() %></td>
	</tr>
	<%
		}
	
		if(i != 0) {
			
	%>
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" name="databaseId" value="<%=databaseId %>">
		<input type="hidden" value="add" name="action">
		<input type="submit" value="添加">		
		<input type="button" value="关闭" onclick='parent.$.fancybox.close()'>
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
		没有未关联的应用哦，请返回</font><br>
		<input type="button" value="关闭" onclick='parent.$.fancybox.close()'>
		</td>
	</tr>
	</table>
	<%	
		}
	%>
<%} %>
</form>

</body>
</html>