<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.TimeConfPo"%>
<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
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
String confId = request.getParameter("confId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_timeConf_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
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
<jsp:include page="../head.jsp"></jsp:include>



<form action="./rel_app_timeConf_update.jsp" method="post">
<%


String action = request.getParameter("action");
if("update".equals(action)){
	
	String tmpConfigId = request.getParameter("tmpConfigId");
	TimeConfPo po = TimeConfAo.get().findTimeConfByConfId(Integer.parseInt(tmpConfigId));
	String filePath =request.getParameter("filePath");
	String future = request.getParameter("future");
	String frequency = request.getParameter("frequency");
	String className = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String tailType = request.getParameter("tailType");
	String analyseType = request.getParameter("analyseType");
	String analysedesc = request.getParameter("analyseDesc");
	String obtainType = request.getParameter("obtainType");
	
	po.setClassName(className);
	po.setSplitChar(split);
	po.setFilePath(filePath);
	po.setAnalyseFuture(future);
	po.setAnalyseFrequency(Integer.parseInt(frequency));
	po.setTailType(tailType);
	po.setAnalyseType(Integer.parseInt(analyseType));
	po.setObtainType(Integer.parseInt(obtainType));
	

	boolean b = TimeConfAo.get().updateTimeConf(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("更新成功");}else{out.print("失败!出现异常");} %></font>
	<input type="button" onclick="goToCheck()" name="返回" value="返回 ">
	<%
}else{

	TimeConfPo tmp = TimeConfAo.get().findTimeConfByConfId(Integer.parseInt(confId));
	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加实时配置的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>日志文件别名: </td>
		<td><%=tmp.getAliasLogName() %><input type="hidden" name="tmpConfigId" value="<%=confId %>"/></td>
	</tr>
	<tr>
		<td>数据行分隔符: </td>
		<td>
			<input type="text"  name="split" value="<%=tmp.getSplitChar() %>" >
		</td>
	</tr>	
	<tr>
		<td>收集频率: </td>
		<td><input type="text" name="frequency" value="<%=tmp.getAnalyseFrequency() %>"></td>
	</tr>	
	<tr>
		<td>数据获取: </td>
		<td>
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
		<td id="obtainId">文件路径: </td>
		<td><input type="text" name="filePath" size="100" value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">如果分析器类型为执行ssh后执行分析器，请将ssh指令写这里</td>
	</tr>	
	<tr id="tailId">
		<td>tail模式: </td>
		<td>
			<select name="tailType">
				<option value="line" <%if(tmp.getTailType().equals("line")){out.print("selected");} %>>行</option>
				<option value="char" <%if(tmp.getTailType().equals("char")){out.print("selected");} %>>字节</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td>分析方式: </td>
		<td>
			<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getAnalyseType()==1){out.print("selected");} %>>java-class</option>
				<option value="2" <%if(tmp.getAnalyseType()==2){out.print("selected");} %>>javscript</option>
				<option value="11" <%if(tmp.getAnalyseType()==11){out.print("selected");} %>>dynamic-java</option>
			</select>
		</td>
	</tr>	
	<tr id="classId">
		<td>java分析器: </td>
		<td><input type="text" name="analyseClass" value="<%=tmp.getClassName() %>" size="100"></td>
	</tr>	
	<tr id="scriptId">
		<td id="scriptContentId">分析器的附带参数: </td>
		<td><textarea rows="10" cols="100" name="future"><%=tmp.getAnalyseFuture() %></textarea>如果是读取日志后执行javascript分析器,请将javascript写这里</td>
	</tr>	
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="update" name="action">
		<input type="hidden" value="<%=appId %>" name="appId">
		<input type="hidden" value="<%=confId %>" name="confId">
		<input type="hidden" value="<%=appName %>" name="appName">
		<input type="submit" value="更新实时配置关联"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>