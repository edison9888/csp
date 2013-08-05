<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-ʵʱģ����Ϣ����</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
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
function obtainChange(type){
	if(type == 1){
		$("#obtainId").text("�ļ�·��:");
		$("#tailId").show();
	}
	if(type == 2){
		$("#obtainId").text("shell�ű�:");
		$("#tailId").hide();
	}
	if(type == 3){
		$("#obtainId").text("http-url:");
		$("#tailId").hide();
	}
}

function analyseChange(type){
	if(type == 1){
		$("#classId").show();
		$("#scriptId").show();
		$("#scriptContentId").text("�������ĸ�������:");
		
	}
	if(type == 2){
		$("#classId").hide();
		$("#scriptId").show();
		$("#scriptContentId").text("�ű�����:");
	}
}



$(document).ready(function(){
	obtainChange($("#obtainTypeid").val());
	analyseChange($("#analyseTypeid").val());
	}); 

</script>
</head>
<%
request.setCharacterEncoding("gbk");
String appName = request.getParameter("appName");
String appId = request.getParameter("appId");	//����һ��checkҳ�洫�ݹ���
String tmpId = request.getParameter("tmpId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_timeConf_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}

function addTimeConfFunc() {

	$.post("<%=request.getContextPath () %>/center/manage.mc", {
		appId: "<%=appId%>",
		aliasLogName: $("#aliasLogName").text(),
		split:	$("#split").val(),
		frequency: $("#frequency").val(),
		obtainType: $("#obtainTypeid").val(),
		future: $("#future").val(),
		filePath: $("#filePath").val(),
		analyseClass: $("#analyseClass").val(),
		analyseType: $("#analyseTypeid").val(),
		tailType: $("#tailType").val(),
		analyseDesc: $("#desc").text(),
		action: "addTimeConf"},
		
		function(json) {

			if (json != "ERROR") {
				
				parent.$("#timeConf-table tbody").html(""); 	//�����
				for(var i = 0; i < json.length; i++) {

					
					parent.$("#timeConf-table tbody").append( 
				            "<tr>" +
								"<td align='center'>" + json[i].aliasLogName + "</td>" + 
								"<td align='left'>" + json[i].filePath + "</td>" +
								"<td align='left'>" + json[i].className + "</td>" + 
								"<td align='center'>" +
								"<a href='javascript: openDialogModifyTimeConf(" + json[i].confId + ", " + json[i].appId + ");'>�޸�</a>&nbsp;&nbsp;" +
								"<a href='./rel_app_time_noAction.jsp?confId=" + json[i].confId + "&appId=" + json[i].appId + "&action=deleteTimeConfRel'>ɾ��</a>&nbsp;&nbsp;" + 
							"</tr>" );	
				}
				    
				parent.$("#dialog_time_add").dialog("close");
	         }
		  else {

				updateTips("�Ѿ����ڹ���");
			
	      }
		}, 

	    "json");
}
</script>

<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>

<%


String action = request.getParameter("action");

	
	List<TimeConfTmpPo> timeConfTmpList = TimeConfAo.get().findAllAppTimeConfTmp();
	
	TimeConfTmpPo tmp = null;
	if(tmpId != null){
		tmp = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(tmpId));
	}
%>

<%
List<TimeConfPo> timeConfPoList = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));

%>

<form action="./rel_app_timeConf_add1.jsp" method="post">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<select name="tmpId">
	<%
	
	for(TimeConfTmpPo tmpPo : timeConfTmpList){ 		
		
	%>
	<option value="<%=tmpPo.getTmpId() %>"<%if(tmp!=null&&tmpPo.getTmpId() == tmp.getTmpId()){out.print("selected");} %>><%=tmpPo.getAliasLogName() %></option>
	<%} %>
</select>
<input type="submit"  value="��ȡģ��">
<input type="hidden" name="appId" value="<%=appId %>">
<input type="hidden" name="appName" value="<%=appName %>">
</div>
</form>
<%if(tmpId != null){ %>
<form action="./rel_app_timeConf_add.jsp" method="post">
<table align="center" width="700" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td width="200">ģ������: </td>
		<td id="aliasLogName"><%=tmp.getAliasLogName() %><input type="hidden" name="tmpConfigId" value="<%=tmpId %>"/></td>
	</tr>
	<tr>
		<td width="200">�����зָ���: </td>
		<td>
			<input type="text"  name="split"   id="split"  value="<%=tmp.getSplitChar() %>" >
		</td>
	</tr>	
	<tr>
		<td width="200">�ռ�Ƶ��: </td>
		<td><input type="text" name="frequency" id="frequency" value="<%=tmp.getAnalyseFrequency() %>"></td>
	</tr>	
	<tr>
		<td width="200">���ݻ�ȡ: </td>
		<td width="200">
			<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getObtainType()==1){out.print("selected");} %>>��־�ļ�</option>
				<option value="2" <%if(tmp.getObtainType()==2){out.print("selected");} %>>shell��ʽ</option>
				<option value="3" <%if(tmp.getObtainType()==3){out.print("selected");} %>>http get��ʽ</option>
				<option value="41" <%if(tmp.getObtainType()==41){out.print("selected");} %>>http post��ʽ</option>
				<option value="4" <%if(tmp.getObtainType()==4){out.print("selected");} %>>jmx��ʽ</option>
				<option value="5" <%if(tmp.getObtainType()==5){out.print("selected");} %>>configserver����</option>
				<option value="44" <%if(tmp.getObtainType()==44){out.print("selected");} %>>json</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="200" id="obtainId">�ļ�·��: </td>
		<td><input type="text" name="filePath" id="filePath" size="100" value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">&nbsp;&nbsp;�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����</td>
	</tr>
	<tr id="tailId">
		<td>tailģʽ: </td>
		<td>
			<select name="tailType" id="tailType">
				<option value="line" <%if(tmp.getTailType().equals("line")){out.print("selected");} %>>��</option>
				<option value="char" <%if(tmp.getTailType().equals("char")){out.print("selected");} %>>�ֽ�</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td width="200">������ʽ: </td>
		<td>
			<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getAnalyseType()==1){out.print("selected");} %>>java-class</option>
				<option value="2" <%if(tmp.getAnalyseType()==2){out.print("selected");} %>>javscript</option>
				<option value="11" <%if(tmp.getAnalyseType()==11){out.print("selected");} %>>dynamic-java</option>
			</select>
		</td>
	</tr>	
	<tr id="classId">
		<td width="200">java������: </td>
		<td><input type="text" id="analyseClass" name="analyseClass" value="<%=tmp.getClassName() %>" size="100"></td>
	</tr>	
	<tr id="scriptId">
		<td width="200" id="scriptContentId">�������ĸ�������: </td>
		<td><textarea rows="10" cols="100" name="future" id="future"><%=tmp.getAnalyseFuture() %></textarea>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����</td>
	</tr>
	<tr>
		<td width="200">����˵��: </td>
		<td id="desc" width="200">
			<textarea rows="3" cols="80" readonly="readonly" ><%=tmp.getAnalyseDesc() %></textarea>
		</td>
	</tr>
</table>

<table>
	<tr>
		<td align="center">
		<input type="button" value="���" id="btnAdd" onclick="addTimeConfFunc()">
		<input type="button" value="�ر�" onclick='parent.$("#dialog_time_add").dialog("close")'></td>
	</tr>
</table>
</form>
<%} %>
</body>
</html>