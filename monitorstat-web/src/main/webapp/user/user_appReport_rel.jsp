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
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 

<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_page.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_table.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/datatable/js/jquery.dataTables.js"></script>


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
<script type="text/javascript">


$(document).ready(function() {
	$('#example').dataTable();
	} );

function addRelAlarmAppFunc() {

	var objs=document.getElementsByName('selectId');
	var appStr = "";
	for(var i=0;i<objs.length;i++)
	{
	  if(objs[i].checked==true)
	   {
	    	appStr += objs[i].value + ",";
	   }
	}
	if(appStr == "") {

		alert("对不起!您没有选择任何的应用"); 
		return false;
	}
	var appReportStrs = parent.$("#selectReportApp").val().split(";");//<repordID:appId,appId>类似：98:21793,21818; 
	var oldKeys = "";
	for(var i=0; i < appReportStrs.length-1; i++) {			//用；切分后的数组最后会是空白字符串

		var str = appReportStrs[i].split(":");	//98:21793,21818
		
		if(str[0] != $('#reportId').val()) {			//str[0]是reportId
			oldKeys += appReportStrs[i] + ";";
		}
	}

    
	$.post("<%=request.getContextPath () %>/center/relAlarm.mc", {
		action:"addRelAppReport",
		appGroups: appStr},	//值是拼接的keyStr
		function(json) {
		   
			if (json != "ERROR") {
				
				var nameStr = $('#reportId').val() + ":";
				for(var i=0;i<json.length;i++) {

					if(i != (json.length - 1)) {
						nameStr += json[i] + ",";
					} else {
						nameStr += json[i] + ";";
					}
			    }
			   // console.log(nameStr);
			    var newKeys = oldKeys + nameStr;
				parent.$("#selectReportApp").val(newKeys);		//为了在父窗口中保存选择的应用id，在父窗口中是隐藏域
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



<%
/*
request.setCharacterEncoding("gbk");
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>

<form action="" method="post">
<!-- 这个jsp是用户关联那些app的弹出框的页面 -->
<% 
String reportId = request.getParameter("reportId");
String relApp = request.getParameter("relApp");
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();

if(relApp == null) {
	
	relApp = "";
}
//以下是为了第二次选择的时候会把以前选择的勾选了
String[] reportAndApps = relApp.split(";");			//9:34,54;7:23,53;
List<String> appIdList = new ArrayList<String>(); 
for(String s : reportAndApps) {
	
	String[] a = s.split(":");	//9:34,54
	if(a[0].equals(reportId)) {
		
		String[] b = a[1].split(",");	//34,54
		for(String c : b) {
			appIdList.add(c);		//存放当前应用id
		}
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 95%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息</div>
<div id="dialog">

<table align="center" id="example" border="1">
	<thead>
	<tr>
	  	<th width="50" align="center">
	  	<input name='chkAll' type='checkbox' id='chkAll' value='chkAll'></th>
		<th align="center">应用名</th>
  </tr>  
  </thead>
  <tbody>
  	<%

	for(AppInfoPo app:listApp){

		
	%> 	
		<tr>
		<%
		if(appIdList.contains(app.getAppId() + "")) {
			
			%>
			<td align="center"><input type="checkbox" name='selectId' class='selectId' checked="checked"  value="<%=app.getAppId() %>"></td>
			<%
			
		} else {
		%>
		<td align="center"><input type="checkbox" name='selectId' class='selectId' value="<%=app.getAppId() %>"></td>
		<%} 
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
		<input type="hidden" id="reportId" value="<%=reportId %>">
		<input type="button" onclick="ck(true)" value="全选">
		<input type="button" onclick="ck(false)" value="取消全选">
		<input type="button" onclick="addRelAlarmAppFunc()" value="确认提交">
		</td>
	</tr>
</table>	
</form>

</body>
</html>