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
<%@page import="com.taobao.monitor.web.ao.MonitorExtraKeyAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�û�����</title>
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

<script type="text/javascript">

$(document).ready(function() {
	$('#example').dataTable();
	} );

function addRelAppKeyFunc() {

	var objs=document.getElementsByName('selectId');
	var keyStr = "";
	for(var i=0;i<objs.length;i++)
	{
	  if(objs[i].checked==true)
	   {
	    	keyStr += objs[i].value + ",";
	   }
	}
	
	if(keyStr == "") {

		alert("�Բ���!��û��ѡ���κε�Key!"); 
		return false;
	}
	//ɾ��hidden�е��ظ���key,Ҳ���ǵڶ���ѡ���ʱ�����ǰ��ͬapp�е�keyȥ��
	var oldKeys = "";
	var keyStrs = parent.$("#relKey").val().split(";");//<appID:keyid,keyid>���ƣ�98:21793,21818; 
	for(var i=0; i < keyStrs.length-1; i++) {			//�ã��зֺ�����������ǿհ��ַ���

		var app = keyStrs[i].split(":");	//98:21793,21818
		
		if(app[0] != $('#appId').val()) {			//app[0]��Ӧ�õ�Id
			oldKeys += keyStrs[i] + ";";
		}
	}
	
	$.post("<%=request.getContextPath () %>/center/relAlarm.mc", {
		action:"addRelAppKey",
		appId:"$('#appId').val()",
		keyGroups: keyStr},	//ֵ��ƴ�ӵ�keyStr
		function(json) {
			if (json != "ERROR") {
				var nameStr = $('#appId').val() + ":";
				for(var i=0;i<json.length;i++) {

					if(i != (json.length - 1)) {
						nameStr += json[i] + ",";
					} else {
						nameStr += json[i] + ";";
					}
			    }
			  //  console.log(nameStr);
			    var newKeys = oldKeys + nameStr;
				parent.$("#relKey").val(newKeys);		//Ϊ���ڸ������б���ѡ���Ӧ��id���ڸ���������������
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
		// ����һ����ѡ��������ȫѡ/ȫ��ѡ
		$("#chkAll").click(function(){
			if(this.checked){ //�����ǰ����Ķ�ѡ��ѡ��
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
	out.print("��û��Ȩ�޲���!");
	return;
}
*/
%>
<!-- ���jsp��Ӧ�ù�����Щkey�ĵ������ҳ�� -->
<form id="f" name="f" action="./user_appKey_rel.jsp" method="post">

<%

String appId = request.getParameter("appId");
String relKey = request.getParameter("relKey");
List<Integer> keyList = MonitorExtraKeyAo.get().findAppAndKeyRelByAppId(Integer.parseInt(appId));
 
if(relKey == null) {
	
	relKey = "";
}

//������Ϊ�˵ڶ���ѡ���ʱ������ǰѡ��Ĺ�ѡ��
String[] appAndKeys = relKey.split(";");			//9:34,54;7:23,53;
List<String> keyIdList = new ArrayList<String>(); 
for(String s : appAndKeys) {
	
	String[] a = s.split(":");	//9:34,54
	if(a[0].equals(appId)) {
		
		String[] b = a[1].split(",");	//34,54
		for(String c : b) {
			keyIdList.add(c);		//��ŵ�ǰӦ��id
		}
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 95%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Ӧ�á�<%=AppCache.get().getKey(Integer.parseInt(appId)).getAppName() %>��������Key</div>
<div id="dialog">

<table border="1" id="example" >
	
	<thead>
	<tr>
	  	<th width="50" align="center"><input name='chkAll' type='checkbox' id='chkAll' value='chkAll'></th>
		<th  align="center">key����</th>
  	</tr>  
  </thead>
  <tbody>
  	<%	
		for(Integer keyId : keyList) {
		
	%>
			<tr>
	<%
		if(keyIdList.contains(keyId + "")) {
			
			%>
				<td align="center"><input type="checkbox" name='selectId' id='selectId' checked="checked" value="<%=keyId%>"></td>
			<%
			
		} else {
		%>
		<td align="center"><input type="checkbox" name='selectId' id='selectId' value="<%=keyId%>"></td>
		<%} %>
		<td align="left"><%=KeyCache.get().getKey(keyId).getKeyName()%></td>
	</tr>
		
	<%} %>
	</tbody>
	</table>
</div>


</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" id="appId" value="<%=appId %>">
		<input type="button" onclick="ck(true)" value="ȫѡ">
		<input type="button" onclick="ck(false)" value="ȡ��ȫѡ">
		<input type="button" onclick="addRelAppKeyFunc()" value="ȷ���ύ">
		</td>
	</tr>
</table>	
</form>
</body>
</html>