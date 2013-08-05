<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
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

</script>
</head>
<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
	return;
}
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./timeConfTmp_add.jsp" method="post">
<%



String action = request.getParameter("action");
if("add".equals(action)){
	String aliasLogName = request.getParameter("aliasLogName");
	String className = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String filePath =request.getParameter("filePath");
	String future = request.getParameter("future");
	String frequency = request.getParameter("frequency");
	String tailType = request.getParameter("tailType");
	String analyseType = request.getParameter("analyseType");
	String analyseDesc = request.getParameter("analyseDesc");
	String obtainType = request.getParameter("obtainType");
	
	TimeConfTmpPo po = new TimeConfTmpPo();
	po.setAliasLogName(aliasLogName);
	po.setClassName(className);
	po.setSplitChar(split);
	po.setFilePath(filePath);
	po.setAnalyseFuture(future);
	po.setAnalyseFrequency(Integer.parseInt(frequency));
	po.setTailType(tailType);
	po.setAnalyseType(Integer.parseInt(analyseType));
	po.setAnalyseDesc(analyseDesc);
	po.setObtainType(Integer.parseInt(obtainType));
	boolean b = TimeConfAo.get().addTimeConfTmp(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("添加成功");}else{out.print("失败!出现异常");} %></font>	
	<a href="./timeConfTmp_center.jsp">返回</a>
	<%
}else{

	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加日报配置的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>模版名称: </td>
		<td><input type="text" name="aliasLogName" value=""></td>
	</tr>	
	<tr>
		<td>数据行分隔符: </td>
		<td>
			<input type="text"  name="split" value="\n" >
		</td>
	</tr>	
	<tr>
		<td>收集频率: </td>
		<td><input type="text" name="frequency" value="60">s</td>
	</tr>	
	<tr>
		<td>数据获取: </td>
		<td>
			<select name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
				<option value="1">日志文件</option>
				<option value="2">shell方式</option>
				<option value="3">http get方式</option>
				<option value="41">http post方式</option>
				<option value="4">jmx方式</option>
				<option value="5">configserver推送</option>
				<option value="44">json推送</option>
			</select>
		</td>
	</tr>
	<tr>
		<td id="obtainId">文件路径: </td>
		<td><input type="text" name="filePath" size="100" value="">如果分析器类型为执行ssh后执行分析器，请将ssh指令写这里</td>
	</tr>
	<tr id="tailId">
		<td>tail模式: </td>
		<td>
			<select name="tailType">
				<option value="line">行</option>
				<option value="char">字节</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>分析方式: </td>
		<td>
			<select name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1">java-class</option>
				<option value="2">javscript</option>
				<option value="11" >dynamic-java</option>
				<!--  <option value="3">Ruby</option>-->
				<!-- <option value="4">Python</option>-->
			</select>
		</td>
	</tr>
	<tr id="classId">
		<td>java分析器: </td>
		<td><input type="text" name="analyseClass" value="" size="100"></td>
	</tr>	
	<tr id="scriptId">
		<td id="scriptContentId">分析器的附带参数: </td>
		<td><textarea rows="10" cols="100" name="future"></textarea>如果是读取日志后执行javascript分析器,请将javascript写这里</td>
	</tr>
	<tr>
		<td>描述说明: </td>
		<td>
			<textarea rows="5" cols="80" name="analyseDesc"></textarea>
		</td>
	</tr>
</table>
</div>
</div>
<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="add" name="action"><input type="submit" value="添加新模板"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
说明：javascrip 脚本需要实现function analyseOneLine(line){}方法。<br/>
利用function putCountData(time,title,key,value){	}<br/>
function putAverageData(time,title,key,value){}<br/>
function putTextData(time,title,key,value){}保存analyseOneLine出来信息<br/>
例子:<br/>
function analyseOneLine(line){<br/>	
&nbsp;&nbsp;&nbsp;var _tmpLines = line.split(',');<br/>	
&nbsp;&nbsp;&nbsp;var calls = _tmpLines[0];	<br/>
&nbsp;&nbsp;&nbsp;var responseTime = _tmpLines[1];<br/>	
&nbsp;&nbsp;&nbsp;var dateTime = _tmpLines[2];<br/>
&nbsp;&nbsp;&nbsp;putCountData(dateTime,'IC接口访问','getItemById',calls);<br/>
&nbsp;&nbsp;&nbsp;putAverageData(dateTime,'IC接口访问','getItemById',responseTime);<br/>
};其中dateTime的格式必须是yyyy-MM-dd HH:mm<br/>



<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>