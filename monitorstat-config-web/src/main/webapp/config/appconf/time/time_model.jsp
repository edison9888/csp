<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʵʱ�������</title>

<%
request.setCharacterEncoding("gbk");
AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");

int appId = appInfoPo.getAppId();
String appName = appInfoPo.getAppName();

List<TimeConfTmpPo> timeConfTmpList = (List<TimeConfTmpPo>)request.getAttribute("timeConfTmpList");
TimeConfTmpPo tmp = (TimeConfTmpPo)request.getAttribute("tmp");
%>

	<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
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
</head>
<body>
<form action="./rel_app_timeConf_add1.jsp" method="post">

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
</form>

<%if(tmp != null){ %>
<form action="./rel_app_timeConf_add.jsp" method="post">
<table align="center" width="700" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td width="200">ģ������: </td>
		<td id="aliasLogName"><%=tmp.getAliasLogName() %><input type="hidden" name="tmpConfigId" value="<%=tmp.getTmpId() %>"/></td>
	</tr>
	<tr>
		<td width="200">�����зָ���: </td>
		<td>
			<select name="split" id="split">
				<option value="\n" <%if(tmp.getSplitChar().equals("\\n")){out.print("selected");} %>>\n</option>
				<option value="\02" <%if(tmp.getSplitChar().equals("\\02")){out.print("selected");} %>>\02</option>
			</select>
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
				<option value="3" <%if(tmp.getObtainType()==3){out.print("selected");} %>>http��ʽ</option>
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