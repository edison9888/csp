<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.*"%>




<%@page import="com.taobao.monitor.script.ScriptGroupPo"%>
<%@page import="com.taobao.monitor.script.ao.ScriptAo"%><html>
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

String scriptId = request.getParameter("scriptId");
String action = request.getParameter("action");

%>


<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/script/script_group_rel_check.jsp?scriptId=<%=scriptId%>";
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
		alert("对不起!您没有选择任何的脚本组!"); 
		return false;
	}else{
		return true;
	} 
	
}

$(function(){

	// 用另一个复选框来控制全选/全不选
		$("#checkGroupId").click(function(){
	  
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
<form action="./script_group_rel_add.jsp" onsubmit="return checkAlarm()" onsubmit="return checkAlarm()"onsubmit="return checkAlarm()" onsubmit="return checkAlarm()" method="post" name="myform" id="myform">
<%


if("add".equals(action)){
	
	String[] selectGroupIds = request.getParameterValues("selectId");
	List<ScriptGroupPo> selectedGroupList = new ArrayList<ScriptGroupPo>();
	for(String groupId:selectGroupIds){
		String groupName = request.getParameter("groupName_"+groupId);
		
		ScriptGroupPo po = new ScriptGroupPo();
		po.setGroupId(Integer.parseInt(groupId));
		po.setGroupName(groupName);
		selectedGroupList.add(po);
	}
		
	//
	boolean b = ScriptAo.get().addRelGroupList(Integer.parseInt(scriptId),selectedGroupList);
		
	%>
		<font size="+3" color="red"><%if(b){out.print("添加成功");}else{out.print("失败!出现异常");} %></font>
				<a href="./script_group_rel_check.jsp?scriptId=<%=scriptId %>">返回</a>
	<%
}

else{

	//脚本还未添加的脚本组
	List<ScriptGroupPo> groupList = ScriptAo.get().findAllWithoutRelGroupByScriptId(Integer.parseInt(scriptId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加关联脚本组&nbsp;&nbsp;
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
	  	<td width="50" align="center">
	  	<input name='check' type='checkbox' id='checkGroupId' value='checkbox'></td>
		<td align="center">脚本组名</td>
	</tr>
	<%	

	for(ScriptGroupPo groupPo : groupList) {

	%>	
	<tr>
		<td align="center"><input type="checkbox" name='selectId' id='selectId' value="<%=groupPo.getGroupId() %>"></td>
		<td align="center" class="groupName"><input type="text" name="groupName_<%=groupPo.getGroupId() %>" value="<%=groupPo.getGroupName() %>"></td>
	</tr>
	<%
		}
	
		if(0 != groupList.size()) {
			
	%>
</table>


</div>
<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="add" name="action">
		<input type="hidden" value="<%=scriptId %>" name="scriptId">
		<input type="submit" value="添加关联脚本组">		
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
		所有脚本组你已经添加，请返回</font>
		<input type="button" value="返回" onclick="goToCheck()"></td>
	</tr>
	</table>
	<%	
		}
	%>
<%} %>
</form>
</body>
</html>