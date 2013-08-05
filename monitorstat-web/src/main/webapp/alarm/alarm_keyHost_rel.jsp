<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.alarm.po.ExtraKeyAlarmDefine"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="sun.security.krb5.internal.HostAddress"%><html>
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
	});


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
<!-- 这个jsp是报表report与app的弹出框的页面 -->
<%

String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
String action = request.getParameter("action");
String define = request.getParameter("define");		


if("add".equals(action)){
	
	String[] selectHostIds = request.getParameterValues("selectId");
	List<String> hostList = new ArrayList<String>();
	for(String h : selectHostIds) {
	
		hostList.add(h);
	}
	
	boolean b = MonitorAlarmAo.get().addExtraKeyAlarmDefine(Integer.parseInt(appId), Integer.parseInt(keyId), define, hostList);

%>
	<font size="+3" color="red"><%if(b){out.print("添加成功! 	<input type='button' value='关闭' onclick='parent.location.reload()'>");}else{out.print("失败!出现异常");} %></font>	
<%
}


else {

List<HostPo> hostList = HostAo.get().findAllHost();
List<ExtraKeyAlarmDefine> extraKeyAlarmList= MonitorAlarmAo.get().getExtraKeyAlarmDefine(Integer.parseInt(appId),Integer.parseInt(keyId));
List<Integer> selectedHostIdList = new ArrayList<Integer>();
if(extraKeyAlarmList != null) {
	for(ExtraKeyAlarmDefine ek : extraKeyAlarmList) {
		
		selectedHostIdList.add(ek.getHostId());
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 95%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息</div>
<div id="dialog">

<table id="example" border="1">
	<thead>
	
	<tr>
	  	<th width="50" align="center">
	  	<input name='check' type='checkbox' id='chkAll' value='chkAll'></th>
		<th align="center">主机名</th>
  </tr>  
  </thead>
  
  <tbody>
  	<%

	for(HostPo host:hostList){

		if(!selectedHostIdList.contains(host.getHostId())) {
	%>
	<tr>
		<td align="center"><input type="checkbox" name='selectId' class='selectId' value="<%=host.getHostId() %>"></td>
		<td align="center"><%=host.getHostName() %></td>
	</tr>
		
	<%} 
	}%>
	</tbody>
	</table>
</div>
</div>
<br>
<br>
<br>

<div>

<table border="1">
	<tr>
	<td width="100">额外配置</td>
	</tr>
	<tr>
	<td ><input size="100%" type="text" id="define" name="define" value="" ></td>
	</tr>
</table>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" id="appId" name="appId" value="<%=appId %>" >
		<input type="hidden" id="keyId" name="keyId" value="<%=keyId %>" >
		<input type="hidden" id="action" name="action" value="add" >
		
		<input type="button" onclick="ck(true)" value="全选">
		<input type="button" onclick="ck(false)" value="取消全选">
		<input type="submit" value="确认提交">
				<input type="button" value="关闭" onclick='parent.$.fancybox.close()'>
		</td>
	</tr>
</table>	
</form>
<%} %>
</body>
</html>