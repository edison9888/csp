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

boolean hasDBrel = false; // 标记是否有数据库关联

boolean hasServerRel = false; // 标记是否有服务器关联

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
		$("#obtainId").text("文件路径:");
		$("#tailId").show();
	}
	if(type == 2){
		$("#obtainId").text("shell脚本:");
		$("#tailId").hide();
	}
	if(type == 3){
		$("#obtainId").text("http-url:");
		$("#tailId").hide();
	}
}

function obtainChange1(type){
	if(type == 1){
		$("#obtainId1").text("文件路径:");
		$("#tailId1").show();
	}
	if(type == 2){
		$("#obtainId1").text("shell脚本:");
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
		$("#scriptContentId").text("分析器的附带参数:");
		
	}
	if(type == 2){
		$("#classId").hide();
		$("#scriptId").show();
		$("#scriptContentId").text("脚本程序:");
	}
}

function analyseChange1(type){
	if(type == 1){
		$("#classId1").show();
		$("#scriptId1").show();
		$("#scriptContentId1").text("分析器的附带参数:");
		
	}
	if(type == 2){
		$("#classId1").hide();
		$("#scriptId1").show();
		$("#scriptContentId1").text("脚本程序:");
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

<title>实时监控配置</title>
</head>
<body>

<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container-fluid">

	<jsp:include page="../../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2><%=appInfoPo.getAppName() %>实时监控配置</h2>
		</div>
		<table align="center" class="zebra-striped condensed-table bordered-table">
			<tr>
				<td class="blue">应用名: </td>
				<td id="myname"><%=appInfoPo.getAppName() %></td>
				<td class="blue">排序号: </td>
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
				<td><%=((appInfoPo.getDayDeploy() == 0) ? "不生效":"生效")%></td>
			</tr>
			<tr>
				<td class="blue">day_timeDeploy:</td>
				<td><%=((appInfoPo.getTimeDeploy() == 0) ? "不生效":"生效")%></td>
				<td class="blue">状态</td>
				<td><%=((appInfoPo.getAppStatus() == 0) ? "正常":"删除")%></td>
			</tr>
		</table><!-- 应用基本信息表 -->
		<div class="page-header">
			<h4>
			关联数据库
			<button class="btn primary pull-right" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-db">添加关联</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
				<tr>
					<th class="blue">数据库名</th>
					<th class="blue">URL</th>
					<th class="blue">操作</th>
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
					<a id="deleteAction" class="deleteAction" href="javascript:deleteDB('<%=po.getAppId() %>','<%=po.getAppType() %>','<%=po.getDataBaseInfoPo().getDbId() %>')">删除</a> &nbsp;&nbsp;
				</td>
			</tr>
			<%
					hasDBrel = true;
				}
			%>
			</tbody>	
		</table><!-- 数据库显示 -->
	
		<div class="page-header">
			<h4>
			关联服务器
			<button class="btn primary pull-right" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-server">添加关联</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
				<tr>
					<th class="blue">服务器名</th>
					<th class="blue">IP</th>
					<th class="blue">操作</th>
				</tr>
			</thead>
			<tbody>
			<%
				for(AppInfoPo po : appInfo4ServerPoList) {
					if(po.getAppType().equals("day")){		//跳过day来只显示time类型
						continue;
					}
			%>
			<tr>
				<td><%=po.getServerInfoPo().getServerName()%></td>
				<td><%=po.getServerInfoPo().getServerIp()%></td>
				<td>
					<a href="javascript:updateServerRel('<%=po.getAppId() %>', '<%=po.getServerInfoPo().getServerId() %>', 'time', 'delete')">删除</a> &nbsp;&nbsp;
				</td>
			</tr>
			<%
					hasServerRel = true;
				}
			%>
			</tbody>
		</table><!-- 服务器显示 -->

		<div class="page-header">
			<h4>
			关联实时配置模板
			<button class="btn primary pull-right" id="btn-add-time" data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-model">添加模板</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
			<tr>
				<th class="blue">实时配置文件名</th>
				<th class="blue">实时配置文件路径</th>
				<th class="blue">实时配置文件分析器</th>
				<th class="blue">操作</th>
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
					<a data-keyboard="true" data-backdrop="true" data-controls-modal="modal-from-dom-changeModel" onclick="getConfPo('<%=po.getConfId() %>')">修改</a>&nbsp;&nbsp;
					<a href="<%=request.getContextPath() %>/show/appconfig.do?method=deleteAppModel&appId=<%=appInfoPo.getAppId() %>&confId=<%=po.getConfId() %>">删除</a>&nbsp;&nbsp;
					
				</td>
			</tr>
			<%
				}
			%>
			</tbody>
		</table><!-- 模板显示 -->
	
		<div class="page-header">
			<h4>
			关联主机
			<button class="btn primary pull-right" onclick="checkAppHost()">修改设置</button>
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<td width="150">CM3:  共 <%=hostDataMap.get("sumCM3") %> 个 </td>
				<td>&nbsp;&nbsp; 临时表：共<%=hostDataMap.get("sumCM3Limit") %>个</td>
				<td> &nbsp; &nbsp; 持久表：共<%=hostDataMap.get("sumCM3Save") %>个</td>
			</tr>
			<tr>
				<td width="150">CM4:  共 <%=hostDataMap.get("sumCM4") %> 个 </td>
				<td>&nbsp;&nbsp; 临时表：共<%=hostDataMap.get("sumCM4Limit") %>个</td>
				<td> &nbsp; &nbsp; 持久表：共<%=hostDataMap.get("sumCM4Save") %>个</td>
			</tr>
		</table><!-- 关联主机显示 -->
		<div id="modal-from-dom-db" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">×</a>
				<h3>添加新的数据库关联</h3>
			</div>
			<% 
			if (hasDBrel) {
			%>
			<div class="alert-message error">
		        <p>数据库关联已存在，请删除后重试</p>
		    </div>
			<%
			} else {
			%>
			<div class="modal-body">
			<table>
				<tr>
					<td align="center">数据库: </td>
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
				<a class="btn primary" href="javascript:addDBRel()">添加</a>
			</div>
			<%
			}
			%>
		</div><!-- 数据库弹出窗口 -->
		
		<div id="modal-from-dom-server" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">×</a>
				<h3>添加新的服务器关联</h3>
			</div>
			<%
			if (hasServerRel) {
			%>
				<div class="alert-message error">
			        <p>服务器关联已存在，请删除后重试</p>
			    </div>
			<%	
			} else {
			%>
			<div class="modal-body">
			<table>
				<tr>
					<td>服务器: </td>
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
				<a class="btn primary" href="javascript:addServerRel()">添加</a>
			</div>
			<%
			}
			%>
		</div><!-- 服务器弹出窗口 -->
		
		<div id="modal-from-dom-model" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">×</a>
				<h3>添加新的模板关联</h3>
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
				<button class="btn primary small" onclick="getModel()">获取模板</button>
				
				<form action="<%=request.getContextPath()%>/show/appconfig.do?method=addAppModel&appId=<%=appInfoPo.getAppId() %>" method="post">
				<p>
				<table class="bordered-table zebra-striped condensed-table">
					<tr>
						<td>模版名称: </td>
						<td><input type="text" name="aliasLogName" id="aliasLogName" value="" readonly="readonly"></td>
					</tr>
					<tr>
						<td>数据行分隔符: </td>
						<td>
							<select name="splitChar" id="splitChar">
								<option value="\n">\n</option>
								<option value="\02">\02</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>收集频率: </td>
						<td><input type="text" name="analyseFrequency" id="analyseFrequency" value=""></td>
					</tr>	
					<tr>
						<td>数据获取: </td>
						<td>
							<select id="obtainType" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
								<option value="1">日志文件</option>
								<option value="2">shell方式</option>
								<option value="3">http方式</option>
							</select>
						</td>
					</tr>
					<tr>
						<td id="obtainId">文件路径: </td>
						<td><input style = "width:360px" width="500px" type="text" name="filePath" id="filePath" value=""></td>
					</tr>
					<tr id="tailId">
						<td>tail模式: </td>
						<td>
							<select name="tailType" id="tailType">
								<option value="line">行</option>
								<option value="char">字节</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>分析方式: </td>
						<td>
							<select id="analyseType" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
								<option value="1">java-class</option>
								<option value="2">javscript</option>
							</select>
						</td>
					</tr>	
					<tr id="classId">
						<td>java分析器: </td>
						<td><input style = "width:360px" type="text" id="className" name="className" value=""></td>
					</tr>	
					<tr id="scriptId">
						<td id="scriptContentId">分析器的附带参数: </td>
						<td><textarea rows="1" name="analyseFuture" id="analyseFuture" style = "width:360px"></textarea></td>
					</tr>
					<tr>
						<td>描述说明: </td>
						<td id="desc">
							<textarea rows="2" readonly="readonly" id="analyseDesc" name="analyseDesc" style = "width:360px"></textarea>
						</td>
					</tr>
				</table>
				<input type="submit" class="btn primary pull-right" value="添加" >
				</form>
			</div>
		</div><!-- 实时模板弹出窗口 -->
		
		<div id="modal-from-dom-changeModel" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<a class="close" href="#">×</a>
				<h3>修改模板关联</h3>
			</div>
			<form action="<%=request.getContextPath()%>/show/appconfig.do?method=updateAppModel&appId=<%=appInfoPo.getAppId() %>" method="post">
			<div class="modal-body">
				
				<table class="bordered-table zebra-striped condensed-table">
					
					<tr>
						<td>模版名称: </td>
						<td><input type="text" name="aliasLogName" id="aliasLogName1" value="" readonly="readonly"></td>
					</tr>
					<tr>
						<td>数据行分隔符: </td>
						<td>
							<select name="splitChar" id="splitChar1">
								<option value="\n">\n</option>
								<option value="\02">\02</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>收集频率: </td>
						<td><input type="text" name="analyseFrequency" id="analyseFrequency1" value=""></td>
					</tr>	
					<tr>
						<td>数据获取: </td>
						<td>
							<select id="obtainType1" name="obtainType" onchange="obtainChange1(this.options[this.selectedIndex].value)">
								<option value="1">日志文件</option>
								<option value="2">shell方式</option>
								<option value="3">http方式</option>
							</select>
						</td>
					</tr>
					<tr>
						<td id="obtainId1">文件路径: </td>
						<td><input style = "width:360px" width="500px" type="text" name="filePath" id="filePath1" value=""></td>
					</tr>
					<tr id="tailId1">
						<td>tail模式: </td>
						<td>
							<select name="tailType" id="tailType1">
								<option value="line">行</option>
								<option value="char">字节</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td>分析方式: </td>
						<td>
							<select id="analyseType1" name="analyseType" onchange="analyseChange1(this.options[this.selectedIndex].value)">
								<option value="1">java-class</option>
								<option value="2">javscript</option>
							</select>
						</td>
					</tr>	
					<tr id="classId1">
						<td>java分析器: </td>
						<td><input style = "width:360px" type="text" id="className1" name="className" value=""></td>
					</tr>	
					<tr id="scriptId1">
						<td id="scriptContentId1">分析器的附带参数: </td>
						<td><textarea rows="1" name="analyseFuture" id="analyseFuture1" style = "width:360px"></textarea></td>
					</tr>
				</table>
				<input type="hidden" name="confId" id="confId1" value="">
			</div>
			<div class="modal-footer">
				<input type="submit" class="btn primary pull-right" value="修改" >
			</div>
			</form>
			
		</div><!-- 修改实时模板弹出窗口 -->
		
		<footer>
			<p>&copy; Taobao 2011</p>
		</footer>
	</div>	
	
</div>
</body>
</html>