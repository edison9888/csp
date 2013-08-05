<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.DataBaseInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfPo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/bootstrap-modal.js"></script>

<%
AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
List<AppInfoPo> appInfo4DbPoList = (List<AppInfoPo>)request.getAttribute("appInfo4DbPoList");
List<DataBaseInfoPo> dbInfoList = (List<DataBaseInfoPo>)request.getAttribute("dbInfoList");
List<AppInfoPo> appInfo4ServerPoList = (List<AppInfoPo>)request.getAttribute("appInfo4ServerPoList");
List<ServerInfoPo>  serverInfoList = (List<ServerInfoPo>)request.getAttribute("serverInfoList");
List<TimeConfPo> timeConfPoList = (List<TimeConfPo>)request.getAttribute("timeConfPoList");
List<TimeConfTmpPo> timeConfTmpList = (List<TimeConfTmpPo>)request.getAttribute("timeConfTmpList");
Map<String, Integer> hostDataMap = (HashMap<String, Integer>)request.getAttribute("hostDataMap");

boolean hasDBrel = false; // ����Ƿ������ݿ����

boolean hasServerRel = false; // ����Ƿ��з���������

TimeConfPo timeConfPo = null;
%>

<script>
function showMessage(flag,select){
	var options = select.options;
	for(var i=0;i<options.length;i++){
		var option = options[i];
		
		$("#"+flag+option.value).hide();
	}
	$("#"+flag+select.options[select.selectedIndex].value).show();
}

function deleteDB(appId, appType, databaseId) {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=deleteDB&appId="+appId+"&appType="+appType+"&databaseId="+databaseId;
	location.href = str;
}

function addDBRel() {

	var dbId = $("#add-databaseId").val();
	var appId = <%=appInfoPo.getAppId() %>;
	var appType = "time";
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=addDBRel&appId="+appId+"&appType="+appType+"&databaseId="+dbId;

	location.href = str;
}

function addServerRel() {
	
	var serverId = $("#add-serverId").val();
	var appId = <%=appInfoPo.getAppId() %>;
	var appType = "time";
	var type = "add";
	//alert(serverId);
	updateServerRel(appId, serverId, appType, type);
}

function updateServerRel(appId, serverId, appType, type) {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=updateServer&appId="+appId+"&appType="+appType+"&serverId="+serverId+"&type="+type;
	location.href = str;
}

function getModel() {
	$.post("<%=request.getContextPath()%>/show/appconfig.do?method=getModelById", {tmpId:$("#tmpId").val()},
		function callback(json) {
			if (json != "ERROR" && json.isSuccess != "false") {
				$("#aliasLogName").attr("value", json.aliasLogName);
				$("#splitChar").val(json.splitChar);
				$("#analyseFrequency").attr("value", json.analyseFrequency);
				$("#obtainType").val(json.obtainType);
				$("#filePath").attr("value", json.filePath);
				$("#tailType").val(json.tailType);
				$("#analyseType").val(json.analyseType);
				$("#className").attr("value", json.className);
				$("#analyseFuture").text(json.analyseFuture);
				$("#analyseDesc").text(json.analyseDesc);
			}
			else {
				alert("error");
				 }
			}, 
	    "json");
}

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

function obtainChange1(type){
	if(type == 1){
		$("#obtainId1").text("�ļ�·��:");
		$("#tailId1").show();
	}
	if(type == 2){
		$("#obtainId1").text("shell�ű�:");
		$("#tailId1").hide();
	}
	if(type == 3){
		$("#obtainId1").text("http-url:");
		$("#tailId1").hide();
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

function analyseChange1(type){
	if(type == 1){
		$("#classId1").show();
		$("#scriptId1").show();
		$("#scriptContentId1").text("�������ĸ�������:");
		
	}
	if(type == 2){
		$("#classId1").hide();
		$("#scriptId1").show();
		$("#scriptContentId1").text("�ű�����:");
	}
}

function getConfPo(confId) {
	$.post("<%=request.getContextPath()%>/show/appconfig.do?method=getConfPo&confId="+confId, {},
			function callback(json) {
				if (json != "ERROR" && json.isSuccess != "false") {
					$("#aliasLogName1").attr("value", json.aliasLogName);
					$("#splitChar1").val(json.splitChar);
					$("#analyseFrequency1").attr("value", json.analyseFrequency);
					$("#obtainType1").val(json.obtainType);
					$("#filePath1").attr("value", json.filePath);
					$("#tailType1").val(json.tailType);
					$("#analyseType1").val(json.analyseType);
					$("#className1").attr("value", json.className);
					$("#analyseFuture1").text(json.analyseFuture);
					$("#confId1").attr("value", json.confId);
				}
				else {
					alert("error");
					 }
				}, 
		    "json");
}

function checkAppHost() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=checkAppHost&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}
</script>

<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>

<title>ʵʱ�������</title>
</head>
<body>

<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container-fluid">

	<jsp:include page="../../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2><%=appInfoPo.getAppName() %>ʵʱ�������</h2>
		</div>
		<table align="center" class="zebra-striped condensed-table bordered-table">
			<tr>
				<td class="blue">Ӧ����: </td>
				<td id="myname"><%=appInfoPo.getAppName() %></td>
				<td class="blue">�����: </td>
				<td><%=appInfoPo.getSortIndex() %></td>
			</tr>
			<tr>
				<td class="blue">OPS:</td>
				<td><%=appInfoPo.getOpsName() %></td>
				<td class="blue">Feature:</td>
				<td><%=appInfoPo.getFeature() %></td>		
			</tr>
			<tr>
				<td class="blue">Group:</td>
				<td><%=appInfoPo.getGroupName() %></td>
				<td class="blue">day_Deploy:</td>
				<td><%=((appInfoPo.getDayDeploy() == 0) ? "����Ч":"��Ч")%></td>
			</tr>
			<tr>
				<td class="blue">day_timeDeploy:</td>
				<td><%=((appInfoPo.getTimeDeploy() == 0) ? "����Ч":"��Ч")%></td>
				<td class="blue">״̬</td>
				<td><%=((appInfoPo.getAppStatus() == 0) ? "����":"ɾ��")%></td>
			</tr>
		</table><!-- Ӧ�û�����Ϣ�� -->
		<div class="page-header">
			<h4>
			�������ݿ�
			<button class="btn primary pull-right" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-db">��ӹ���</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
				<tr>
					<th class="blue">���ݿ���</th>
					<th class="blue">URL</th>
					<th class="blue">����</th>
				</tr>
			</thead>
			<tbody>
			<%
				for(AppInfoPo po : appInfo4DbPoList) {
					if(po.getAppType().equals("day")){
						continue;
					}
			%>
			<tr>
				<td align="center" id="databaseName"><%=po.getDataBaseInfoPo().getDbName()%></td>
				<td align="left" id="url"><%=po.getDataBaseInfoPo().getDbUrl()%></td>
				<td align="center">
					<a id="deleteAction" class="deleteAction" href="javascript:deleteDB('<%=po.getAppId() %>','<%=po.getAppType() %>','<%=po.getDataBaseInfoPo().getDbId() %>')">ɾ��</a> &nbsp;&nbsp;
				</td>
			</tr>
			<%
					hasDBrel = true;
				}
			%>
			</tbody>	
		</table><!-- ���ݿ���ʾ -->
	
		<div class="page-header">
			<h4>
			����������
			<button class="btn primary pull-right" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-server">��ӹ���</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
				<tr>
					<th class="blue">��������</th>
					<th class="blue">IP</th>
					<th class="blue">����</th>
				</tr>
			</thead>
			<tbody>
			<%
				for(AppInfoPo po : appInfo4ServerPoList) {
					if(po.getAppType().equals("day")){		//����day��ֻ��ʾtime����
						continue;
					}
			%>
			<tr>
				<td><%=po.getServerInfoPo().getServerName()%></td>
				<td><%=po.getServerInfoPo().getServerIp()%></td>
				<td>
					<a href="javascript:updateServerRel('<%=po.getAppId() %>', '<%=po.getServerInfoPo().getServerId() %>', 'time', 'delete')">ɾ��</a> &nbsp;&nbsp;
				</td>
			</tr>
			<%
					hasServerRel = true;
				}
			%>
			</tbody>
		</table><!-- ��������ʾ -->

		<div class="page-header">
			<h4>
			����ʵʱ����ģ��
			<button class="btn primary pull-right" id="btn-add-time" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-model">���ģ��</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
			<tr>
				<th class="blue">ʵʱ�����ļ���</th>
				<th class="blue">ʵʱ�����ļ�·��</th>
				<th class="blue">ʵʱ�����ļ�������</th>
				<th class="blue">����</th>
			</tr>
			</thead>
			
			<tbody>
			<%
				for(TimeConfPo po : timeConfPoList) {
			%>
			<tr>
			
				<td align="center"><%=po.getAliasLogName() %></td>
				<td align="left"><%=po.getFilePath() %></td>
				<td align="left"><%=po.getClassName() %></td>
				<td align="center">
					<a data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-changeModel" onclick="getConfPo('<%=po.getConfId() %>')">�޸�</a>&nbsp;&nbsp;
					<a href="<%=request.getContextPath() %>/show/appconfig.do?method=deleteAppModel&appId=<%=appInfoPo.getAppId() %>&confId=<%=po.getConfId() %>">ɾ��</a>&nbsp;&nbsp;
					
				</td>
			</tr>
			<%
				}
			%>
			</tbody>
		</table><!-- ģ����ʾ -->
	
		<div class="page-header">
			<h4>
			��������
			<button class="btn primary pull-right" onclick="checkAppHost()">�޸�����</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<td width="150">CM3:  �� <%=hostDataMap.get("sumCM3") %> �� </td>
				<td>&nbsp;&nbsp; ��ʱ����<%=hostDataMap.get("sumCM3Limit") %>��</td>
				<td> &nbsp; &nbsp; �־ñ���<%=hostDataMap.get("sumCM3Save") %>��</td>
			</tr>
			<tr>
				<td width="150">CM4:  �� <%=hostDataMap.get("sumCM4") %> �� </td>
				<td>&nbsp;&nbsp; ��ʱ����<%=hostDataMap.get("sumCM4Limit") %>��</td>
				<td> &nbsp; &nbsp; �־ñ���<%=hostDataMap.get("sumCM4Save") %>��</td>
			</tr>
		</table><!-- ����������ʾ -->
		<div id="modal-from-dom-db" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">��</a>
				<h3>����µ����ݿ����</h3>
			</div>
			<% 
			if (hasDBrel) {
			%>
			<div class="alert-message error">
		        <p>���ݿ�����Ѵ��ڣ���ɾ��������</p>
		    </div>
			<%
			} else {
			%>
			<div class="modal-body">
			<table>
				<tr>
					<td align="center">���ݿ�: </td>
					<td align="center">
						<select name="add-databaseId" id="add-databaseId" onchange="showMessage('db_add_',this)">
							<%for(DataBaseInfoPo dbInfo: dbInfoList){
								if(dbInfo.getDbType()==1){
							%> 
								<option value="<%=dbInfo.getDbId()%>"><%=dbInfo.getDbName() %></option>
							<%}} %>
						</select>
					</td>
				</tr>
				<%for(DataBaseInfoPo dbInfo: dbInfoList){%> 
				<tr id="db_add_<%=dbInfo.getDbId() %>" style="display:none">
					<td colspan="2" align="center"><%=dbInfo.getDbUrl() %></td>
				</tr>
				<%} %>	
			</table>
			</div>
			<div class="modal-footer">
				<a class="btn primary" href="javascript:addDBRel()">���</a>
			</div>
			<%
			}
			%>
		</div><!-- ���ݿⵯ������ -->
		
		<div id="modal-from-dom-server" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">��</a>
				<h3>����µķ���������</h3>
			</div>
			<%
			if (hasServerRel) {
			%>
				<div class="alert-message error">
			        <p>�����������Ѵ��ڣ���ɾ��������</p>
			    </div>
			<%	
			} else {
			%>
			<div class="modal-body">
			<table>
				<tr>
					<td>������: </td>
					<td>
						<select name="add-serverId" id="add-serverId" onchange="showMessage('server_add_',this)">
							<%for(ServerInfoPo serverInfo: serverInfoList){%> 
								<option value="<%=serverInfo.getServerId()%>"><%=serverInfo.getServerName() %></option>
							<%} %>
						</select>
					</td>
				</tr>
				<%for(ServerInfoPo serverInfo: serverInfoList){%> 
				<tr id="server_add_<%=serverInfo.getServerId() %>" style="display:none">
					<td colspan="2"><%=serverInfo.getServerIp()%></td>
				</tr>
				<%} %>
			</table>
			</div>
			<div class="modal-footer">
				<a class="btn primary" href="javascript:addServerRel()">���</a>
			</div>
			<%
			}
			%>
		</div><!-- �������������� -->
		
		<div id="modal-from-dom-model" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">��</a>
				<h3>����µ�ģ�����</h3>
			</div>
			
			<div class="modal-body">
				<select id="tmpId">
					<%
					for(TimeConfTmpPo tmpPo : timeConfTmpList){ 		
					%>
					<option value="<%=tmpPo.getTmpId() %>"><%=tmpPo.getAliasLogName() %></option>
					<%} %>
				</select>
				&nbsp;&nbsp;&nbsp;
				<button class="btn primary small" onclick="getModel()">��ȡģ��</button>
				
				<form action="<%=request.getContextPath()%>/show/appconfig.do?method=addAppModel&appId=<%=appInfoPo.getAppId() %>" method="post">
				<p>
				<table class="bordered-table zebra-striped condensed-table">
					<tr>
						<td>ģ������: </td>
						<td><input type="text" name="aliasLogName" id="aliasLogName" value="" readonly="readonly"></td>
					</tr>
					<tr>
						<td>�����зָ���: </td>
						<td>
							<select name="splitChar" id="splitChar">
								<option value="\n">\n</option>
								<option value="\02">\02</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>�ռ�Ƶ��: </td>
						<td><input type="text" name="analyseFrequency" id="analyseFrequency" value=""></td>
					</tr>	
					<tr>
						<td>���ݻ�ȡ: </td>
						<td>
							<select id="obtainType" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
								<option value="1">��־�ļ�</option>
								<option value="2">shell��ʽ</option>
								<option value="3">http��ʽ</option>
							</select>
						</td>
					</tr>
					<tr>
						<td id="obtainId">�ļ�·��: </td>
						<td><input style = "width:360px" width="500px" type="text" name="filePath" id="filePath" value=""></td>
					</tr>
					<tr id="tailId">
						<td>tailģʽ: </td>
						<td>
							<select name="tailType" id="tailType">
								<option value="line">��</option>
								<option value="char">�ֽ�</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>������ʽ: </td>
						<td>
							<select id="analyseType" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
								<option value="1">java-class</option>
								<option value="2">javscript</option>
							</select>
						</td>
					</tr>	
					<tr id="classId">
						<td>java������: </td>
						<td><input style = "width:360px" type="text" id="className" name="className" value=""></td>
					</tr>	
					<tr id="scriptId">
						<td id="scriptContentId">�������ĸ�������: </td>
						<td><textarea rows="1" name="analyseFuture" id="analyseFuture" style = "width:360px"></textarea></td>
					</tr>
					<tr>
						<td>����˵��: </td>
						<td id="desc">
							<textarea rows="2" readonly="readonly" id="analyseDesc" name="analyseDesc" style = "width:360px"></textarea>
						</td>
					</tr>
				</table>
				<input type="submit" class="btn primary pull-right" value="���" >
				</form>
			</div>
		</div><!-- ʵʱģ�嵯������ -->
		
		<div id="modal-from-dom-changeModel" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">��</a>
				<h3>�޸�ģ�����</h3>
			</div>
			<form action="<%=request.getContextPath()%>/show/appconfig.do?method=updateAppModel&appId=<%=appInfoPo.getAppId() %>" method="post">
			<div class="modal-body">
				
				<table class="bordered-table zebra-striped condensed-table">
					
					<tr>
						<td>ģ������: </td>
						<td><input type="text" name="aliasLogName" id="aliasLogName1" value="" readonly="readonly"></td>
					</tr>
					<tr>
						<td>�����зָ���: </td>
						<td>
							<select name="splitChar" id="splitChar1">
								<option value="\n">\n</option>
								<option value="\02">\02</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>�ռ�Ƶ��: </td>
						<td><input type="text" name="analyseFrequency" id="analyseFrequency1" value=""></td>
					</tr>	
					<tr>
						<td>���ݻ�ȡ: </td>
						<td>
							<select id="obtainType1" name="obtainType" onchange="obtainChange1(this.options[this.selectedIndex].value)">
								<option value="1">��־�ļ�</option>
								<option value="2">shell��ʽ</option>
								<option value="3">http��ʽ</option>
							</select>
						</td>
					</tr>
					<tr>
						<td id="obtainId1">�ļ�·��: </td>
						<td><input style = "width:360px" width="500px" type="text" name="filePath" id="filePath1" value=""></td>
					</tr>
					<tr id="tailId1">
						<td>tailģʽ: </td>
						<td>
							<select name="tailType" id="tailType1">
								<option value="line">��</option>
								<option value="char">�ֽ�</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>������ʽ: </td>
						<td>
							<select id="analyseType1" name="analyseType" onchange="analyseChange1(this.options[this.selectedIndex].value)">
								<option value="1">java-class</option>
								<option value="2">javscript</option>
							</select>
						</td>
					</tr>	
					<tr id="classId1">
						<td>java������: </td>
						<td><input style = "width:360px" type="text" id="className1" name="className" value=""></td>
					</tr>	
					<tr id="scriptId1">
						<td id="scriptContentId1">�������ĸ�������: </td>
						<td><textarea rows="1" name="analyseFuture" id="analyseFuture1" style = "width:360px"></textarea></td>
					</tr>
				</table>
				<input type="hidden" name="confId" id="confId1" value="">
			</div>
			<div class="modal-footer">
				<input type="submit" class="btn primary pull-right" value="�޸�" >
			</div>
			</form>
			
		</div><!-- �޸�ʵʱģ�嵯������ -->
		
		<footer>
			<p>&copy; Taobao 2011</p>
		</footer>
	</div>	
	
</div>
</body>
</html>