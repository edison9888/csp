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
<title>核心监控-实时模板信息配置</title>
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



$(document).ready(function(){
	obtainChange($("#obtainTypeid").val());
	analyseChange($("#analyseTypeid").val());
	}); 

</script>
</head>
<%
request.setCharacterEncoding("gbk");
String appName = request.getParameter("appName");
String appId = request.getParameter("appId");	//从上一个check页面传递过来
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
				
				parent.$("#timeConf-table tbody").html(""); 	//先清空
				for(var i = 0; i < json.length; i++) {

					
					parent.$("#timeConf-table tbody").append( 
				            "<tr>" +
								"<td align='center'>" + json[i].aliasLogName + "</td>" + 
								"<td align='left'>" + json[i].filePath + "</td>" +
								"<td align='left'>" + json[i].className + "</td>" + 
								"<td align='center'>" +
								"<a href='javascript: openDialogModifyTimeConf(" + json[i].confId + ", " + json[i].appId + ");'>修改</a>&nbsp;&nbsp;" +
								"<a href='./rel_app_time_noAction.jsp?confId=" + json[i].confId + "&appId=" + json[i].appId + "&action=deleteTimeConfRel'>删除</a>&nbsp;&nbsp;" + 
							"</tr>" );	
				}
				    
				parent.$("#dialog_time_add").dialog("close");
	         }
		  else {

				updateTips("已经存在关联");
			
	      }
		}, 

	    "json");
}
</script>

<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
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
<input type="submit"  value="获取模板">
<input type="hidden" name="appId" value="<%=appId %>">
<input type="hidden" name="appName" value="<%=appName %>">
</div>
</form>
<%if(tmpId != null){ %>
<form action="./rel_app_timeConf_add.jsp" method="post">
<table align="center" width="700" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td width="200">模版名称: </td>
		<td id="aliasLogName"><%=tmp.getAliasLogName() %><input type="hidden" name="tmpConfigId" value="<%=tmpId %>"/></td>
	</tr>
	<tr>
		<td width="200">数据行分隔符: </td>
		<td>
			<input type="text"  name="split"   id="split"  value="<%=tmp.getSplitChar() %>" >
		</td>
	</tr>	
	<tr>
		<td width="200">收集频率: </td>
		<td><input type="text" name="frequency" id="frequency" value="<%=tmp.getAnalyseFrequency() %>"></td>
	</tr>	
	<tr>
		<td width="200">数据获取: </td>
		<td width="200">
			<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getObtainType()==1){out.print("selected");} %>>日志文件</option>
				<option value="2" <%if(tmp.getObtainType()==2){out.print("selected");} %>>shell方式</option>
				<option value="3" <%if(tmp.getObtainType()==3){out.print("selected");} %>>http get方式</option>
				<option value="41" <%if(tmp.getObtainType()==41){out.print("selected");} %>>http post方式</option>
				<option value="4" <%if(tmp.getObtainType()==4){out.print("selected");} %>>jmx方式</option>
				<option value="5" <%if(tmp.getObtainType()==5){out.print("selected");} %>>configserver推送</option>
				<option value="44" <%if(tmp.getObtainType()==44){out.print("selected");} %>>json</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="200" id="obtainId">文件路径: </td>
		<td><input type="text" name="filePath" id="filePath" size="100" value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">&nbsp;&nbsp;如果分析器类型为执行ssh后执行分析器，请将ssh指令写这里</td>
	</tr>
	<tr id="tailId">
		<td>tail模式: </td>
		<td>
			<select name="tailType" id="tailType">
				<option value="line" <%if(tmp.getTailType().equals("line")){out.print("selected");} %>>行</option>
				<option value="char" <%if(tmp.getTailType().equals("char")){out.print("selected");} %>>字节</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td width="200">分析方式: </td>
		<td>
			<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getAnalyseType()==1){out.print("selected");} %>>java-class</option>
				<option value="2" <%if(tmp.getAnalyseType()==2){out.print("selected");} %>>javscript</option>
				<option value="11" <%if(tmp.getAnalyseType()==11){out.print("selected");} %>>dynamic-java</option>
			</select>
		</td>
	</tr>	
	<tr id="classId">
		<td width="200">java分析器: </td>
		<td><input type="text" id="analyseClass" name="analyseClass" value="<%=tmp.getClassName() %>" size="100"></td>
	</tr>	
	<tr id="scriptId">
		<td width="200" id="scriptContentId">分析器的附带参数: </td>
		<td><textarea rows="10" cols="100" name="future" id="future"><%=tmp.getAnalyseFuture() %></textarea>如果是读取日志后执行javascript分析器,请将javascript写这里</td>
	</tr>
	<tr>
		<td width="200">描述说明: </td>
		<td id="desc" width="200">
			<textarea rows="3" cols="80" readonly="readonly" ><%=tmp.getAnalyseDesc() %></textarea>
		</td>
	</tr>
</table>

<table>
	<tr>
		<td align="center">
		<input type="button" value="添加" id="btnAdd" onclick="addTimeConfFunc()">
		<input type="button" value="关闭" onclick='parent.$("#dialog_time_add").dialog("close")'></td>
	</tr>
</table>
</form>
<%} %>
</body>
</html>