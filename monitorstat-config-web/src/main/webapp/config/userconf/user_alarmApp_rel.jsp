<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery-ui-1.8.custom.min.js"></script> 

<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/demo_table.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/demo_page.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.dataTables.js"></script>

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
img {
cursor:pointer;
}

</style>

<%
String selectedApp = (String)request.getAttribute("selectedApp");
List<AppInfoPo> listApp = (ArrayList<AppInfoPo>)request.getAttribute("listApp");
List<String> appIdList = (ArrayList<String>)request.getAttribute("appIdList");
%>

<script type="text/javascript">

$(document).ready(function() {
	$('#example').dataTable();
	} );


function addRelAlarmAppFunc() {

	var appStr = $("#selectappInput").val();
	if(appStr == "") {
		alert("对不起!您没有选择任何的应用!"); 
		return false;
	}
	
	$.post("<%=request.getContextPath () %>/show/UserConf.do?method=addAppInPage", {
		appGroups: appStr},	//值是app的id
		function(json) {
			if (json != "ERROR") {
				var nameStr = "";
				parent.$("#selectAppTable").html("");
				parent.$("#selectAppTable").append("<thead><tr><td align='center'>应用名</td><td align='center'>操作</td></tr></thead><tbody>");

				
				for(var key in json){
					
					nameStr += key + ",";
					parent.$("#selectAppTable").append("<tr id='" + key+"'><td align='center'>" + json[key] + "</td><td align='center'><a href=\"javascript:deleteTableTr('"+ key +"')\">删除</a></td></tr>");
			    }
				parent.$("#selectAppTable").append("</tbody>");
				parent.$("#selectApp").val(nameStr);		//为了在父窗口中保存选择的应用id，在父窗口中是隐藏域
				parent.$("#dialog_appRel_add").dialog("close");
	         }
		}, 

	    "json");
}

function ck(b){
var input = document.getElementsByTagName("input");
for (var i=0;i<input.length ;i++ ){
	if(input[i].type=="checkbox")
	input[i].checked = b;
}
}

//取消或勾选应用时将结果写入id为selectappInput的表单中
function getVal(obj){
	 if(obj.checked==true){
		 var appStr = $("#selectappInput").val()
		 appStr += obj.value + ",";
		 $("#selectappInput").val(appStr);
	 }else{
		 var appStr = $("#selectappInput").val()
		 var apps = appStr.split(",");
		 var tmp="";
		 for(var i=0;i<apps.length;i++){
			var app = apps[i];
			if(app != obj.value){
				tmp+=app+",";
			}
		}	
		 $("#selectappInput").val(tmp);
	 }
}


$(function(){

	// 用另一个复选框来控制全选/全不选
	$("#chkAll").click(function(){
		if(this.checked){ //如果当前点击的多选框被选中
			$('input[type=checkbox][name=selectId]').attr("checked", true );
		}else{ 
			$('input[type=checkbox][name=selectId]').attr("checked", false );
		}
	})
})

</script>

</head>
<body>

<form action="" method="post">
<!-- 这个jsp是报表report与app的弹出框的页面 -->


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 95%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加告警应用</div>
<div id="dialog">

<table id="example" border="1" >
	<thead>
	
	<tr>
	  	<th width="50" align="center">
	  	<input name='check' type='checkbox' id='chkAll' value='chkAll'></th>
		<th align="center">应用名</th>
  </tr>  
  </thead>
  
  <tbody>
  	<%
	String s = "";
	for(AppInfoPo app:listApp){
		%> 	
		<tr>
		<%
		if(appIdList.contains(app.getAppId() + "")) {
			s+=app.getAppId() + ",";
			%>
			<td align="center"><input type="checkbox" name='selectId' class='selectId' checked="checked"  value="<%=app.getAppId() %>" onclick="getVal(this)"></td>
			<%
		} else {
		%>
		<td align="center"><input type="checkbox" name='selectId' class='selectId' value="<%=app.getAppId() %>" onclick="getVal(this)"></td>
		<%
		} 
		%>
		<td align="center"><%=app.getAppName() %></td>
	</tr>
		
	<%} %>
	</tbody>
	</table>
</div>


</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="button" onclick="ck(true)" value="全选">
		<input type="button" onclick="ck(false)" value="取消全选">
		<input type="button" onclick="addRelAlarmAppFunc()" value="确认提交">
		<input type="hidden" value="<%=s %>" id="selectappInput">
		</td>
	</tr>
</table>	
</form>

</body>
</html>