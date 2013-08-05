<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>




<%@page import="com.taobao.monitor.script.ao.ScriptAo"%>
<%@page import="com.taobao.monitor.script.ScriptGroupPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-脚本管理</title>
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
function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/script/script_manager.jsp";
	location.href=url;
}
function goToAdd(){
	
	var url = "<%=request.getContextPath () %>/script/script_group_rel_add.jsp?&scriptId=<%=scriptId%>";
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
		alert("对不起!您没有选择任何脚本组!"); 
		return false;
	}else{
		return true;
	} 
	
}


function ck(b)
{
	var input = document.getElementsByTagName("input");

    for (var i=0;i<input.length ;i++ )
    {
        if(input[i].type=="checkbox")
            input[i].checked = b;
    }
}

$(function(){

	// 用另一个复选框来控制全选/全不选
		$("#chkAll").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][name=selectId]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][name=selectId]').attr("checked", false );
		 	}
		})
})

$(function(){
	$("#deleteAction").click(function(){
		return confirm("想清楚要删除了没有啊");
	});
});

</script>
</head>
<body>



<%

request.setCharacterEncoding("gbk");

%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./script_group_rel_check.jsp" onsubmit="return checkAlarm()" method="post" >
<%
if("delete".equals(action)){

	String[] deleteIdList = request.getParameterValues("selectId");
	ScriptAo.get().deleteGroupRel(Integer.parseInt(scriptId),deleteIdList);
}

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">脚本组列表</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">

	<tr>
		<td width="50" align="center">
		<input name='chkAll' type='checkbox' id='chkAll'  value='checkbox'></td>
		
		<td align="center">脚本组名</td>
	</tr>
	<%
		for(ScriptGroupPo groupPo : ScriptAo.get().findAllRelGroupByScriptId(Integer.parseInt(scriptId))) {
	%>
	<tr>
		<td align="center"><input type="checkbox" id='selectId' name="selectId" value="<%=groupPo.getGroupId() %>"></td>
		<td align="center"><%=groupPo.getGroupName()%></td>
		
	</tr>
		
	<%
		}
	%>
</table>
</div>

<table width="100%">
	<tr>	
		<td align="center">
		<input type="hidden" name="action" value="delete">
		<input type="hidden" name="scriptId" value="<%=scriptId %>">
		<input type="button" onclick="ck(true)" value="全选">
		<input type="button" onclick="ck(false)" value="取消全选">
		<input type="button" value="添加" onclick="goToAdd()">
		<input id="deleteAction" type="submit" value="删除">
		<input type="button" value="返回" onclick="goToCenter()"></td>
	</tr>
</table>
</div>


</form>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>