<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.*"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="net.sf.json.JSONObject"%>

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-脚本管理</title>
<link type="text/css"
	href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>


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
	background: url(<%= request.getContextPath () %>/ statics/ images/ 4_17
		. gif );
}

img {
	cursor: pointer;
}

.report_on {
	background: #bce774;
}
</style>

</head>
<body>

<jsp:include page="../head.jsp"></jsp:include>

<form action="" onsubmit="return checkAlarm()" method="post">
<%
	String action = request.getParameter("action");
	String scriptId = request.getParameter("scriptId");
	String[] selectHostIps = request.getParameterValues("selectId");
	String param = request.getParameter("param");
	
	//测试编写固定IP
	//String[] selectHostIps = {"10.232.12.113"};
	
%>

<script type="text/javascript"><!--

$(document).ready(function(){
	$("#dialogResult").ajaxSend(function(){
		$("#wait").css("display","block");
	  });
	  
	$("#dialogResult").ajaxComplete(function(event,request, settings){

		$("#wait").css("display","none");
		if(number < ips.length) {
			number++;
			exec(ips[number],'<%=scriptId%>');
			}
	  });

});

var ips = [];
<%
	for(String ip : selectHostIps){
		out.println("ips.push('" + ip + "');");		
	}
%>

var startExe = false,number=1;
function doScriptExecuter(){
	exec(ips[0],'<%=scriptId%>');
}


function exec(ip,scriptId){

	var thisIp = ip;
	var scriptId = scriptId;

	$.post('<%=request.getContextPath () %>/script/runScript.json',
			{action:"run",
			 ip: thisIp,
			 scriptId:scriptId,
			 params:"<%=param%>"
			},
			function(data){
				$("#result-table tbody").append(
				"<tr>" +
				"<td align='left'><font size='5'>" + "主机(" + data.ip + ")中脚本(" + data.scriptName + ")运行结果 </font></td>" +
				"</tr>" +
				"<tr>" + 
				"<td>" +
				"<div id='result' >" + data.msg + "</div>" +
				"</td>" +
				"</tr>"
				);
			},
			"json");
}
</script>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">机器名称：</div>
<div id="dialogResult" class="ui-dialog-content ui-widget-content">
<table width="100%" id="result-table" border="1">
<tbody>

</tbody>
</table>
</div>
</div>
<div id="wait" align="center" style="display:none;"><img src='<%=request.getContextPath() %>/statics/images/script_wait.gif' width="64" height="64" /><br />Loading..</div>
<table>
<tr>	
	<td align="center">
		<input type="button" value="返回" onclick="location.href='./script_manager.jsp?<%=scriptId %>'">
	</td>
	</tr>
</table>
</form>

<jsp:include page="../buttom.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(doScriptExecuter);
</script>
</body>
</html>