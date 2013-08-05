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

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>



<%@page import="com.taobao.monitor.script.ScriptlibraryInfo"%>
<%@page import="com.taobao.monitor.script.ao.ScriptAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�ű�����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_page.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_table.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
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
	font-family: "����";
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

<script type="text/javascript">



$(document).ready(function(){


	
	$("#hostShow").ajaxSend(function(){
		$("#wait").css("display","block");
		//$('#host-table').dataTable().fnClearTable();
		
	  });
	  
	$("#hostShow").ajaxComplete(function(event,request, settings){

		$("#wait").css("display","none");
		//$('#host-table').dataTable();
	  });
	  
	$("#getHost").click(function(){		
		$("#datatable").hide();		
		$.post('<%=request.getContextPath () %>/script/runScript.json',
				{appName:$("#appName").val(),
				 action:"showHost",
				 m:Math.random()},
				function(data){
					//alert(data);
					if(data != "ERROR") {
						$("#hostIPTbody").html("");
							for(var i = 0; i < data.length; i++) {
								$("#hostIPTbody").append( 
								"<tr>" +
								"<td align='center'><input type='checkbox' name='selectId' id='selectId' value='" + data[i].hostIp + "'></td>" + 
								"<td align='center' name='hostName_'" + data[i].hostName + "' value='" + data[i].hostName + "'>" + data[i].hostName + "</td>" + 
								"<td align='center' name='hostIp_'" + data[i].hostIp + "' value='" + data[i].hostIp + "'>" + data[i].hostIp + "</td>" + 
								"<td align='center' name='hostSite_'" + data[i].hostSite + "' value='" + data[i].hostSite + "'>" + data[i].hostSite + "</td>" + 
								"</tr>" ); 
							}
					}
					$('#host-table').dataTable().fnPageChange( 'first' ); 			//��ʾ��һҳ,����������Ļ�������еĶ��г���

					//ѡ��һ����ɫ
					$("#host-table tbody").click(function(event) {
						$($('#host-table').dataTable().fnSettings().aoData).each(function (){
							$(this.nTr).removeClass('row_selected');
						});
						$(event.target.parentNode).addClass('row_selected');
					});
					
					$("#datatable").show();
				},
				"json");
		
	});
	
	
});



/* Get the rows which are currently selected */
function fnGetSelected( oTableLocal )
{
	var aReturn = new Array();
	var aTrs = oTableLocal.fnGetNodes();
	
	for ( var i=0 ; i<aTrs.length ; i++ )
	{
		if ( $(aTrs[i]).hasClass('row_selected') )
		{
			aReturn.push( aTrs[i] );
		}
	}
	return aReturn;
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


function ck(b)
{
	var input = document.getElementsByTagName("input");
    for (var i=0;i<input.length ;i++ )
    {
        if(input[i].type=="checkbox")
            input[i].checked = b;
    }
}

</script>
</head>
<body>



<%
/*
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
*/
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./script_singleRunResult.jsp" onsubmit="return checkAlarm()" method="post">
<%

	String scriptId = request.getParameter("scriptId");
	ScriptlibraryInfo po = ScriptAo.get().findScriptById(Integer.parseInt(scriptId));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ű�������Ϣ��</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div class="ui-widget-content ui-corner-all"  style="font-size: 1.2em; margin: .6em 0;">
<table border="1">
	<tr>
		<td align="center">����</td>
		<td align="center">����</td>
		<td align="center">����</td>
	</tr>
	<tr>
		<td align="center"><%=po.getScriptName() %></td>
		<td align="center"><%=po.getScriptType() %></td>
		<td align="center" width="100"><%=po.getDesc() %>
		</td>
	</tr>
	
</table>
</div>

<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��Ҫִ�е�Ӧ�ã�</div>
<table width="100%" border="1">
	<thead>
		<tr>
			<td>��дӦ��</td>
			<td align="left"><input type="text" id="appName" name="appName" value=""><input type="button" value="��ȡ����" id="getHost"/></td>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<div id="hostShow">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Ӧ�ö�Ӧ��������</div>
<div id="datatable">
<table width="100%" id="host-table" border="1" class="display">
	<thead>
		<tr>
			<th align="center"><input type="checkbox" onclick="ck(this.checked)"></th>
			<th align="center">������</th>
			<th align="center">����IP</th>
			<th align="center">����</th>
		</tr>
	</thead>
	<tbody id="hostIPTbody">
	</tbody>
</table>
</div>
</div>
</div>
</div>

<div id="wait" align="center" style="display:none;"><img src='<%=request.getContextPath() %>/statics/images/script_wait.gif' width="64" height="64" /><br />Loading..</div>
<table>
<% if(po.getNeedParam() == 1){%>
<tr>
	<td >��Ҫ��д�������:<input type="text" id="param" name="param" value="" size="100"></td>
</tr>
<%} %>
<tr>	
	<td align="center">
	<input type="hidden" name="action" value="run">
	<input type="hidden" name="scriptId" value="<%=scriptId %>">
	<input type="button" onclick="ck(true)" value="ȫѡ">
	<input type="button" onclick="ck(false)" value="ȡ��ȫѡ">
	<input type="submit" value="ִ��"></td>
	</tr>
</table>
</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>