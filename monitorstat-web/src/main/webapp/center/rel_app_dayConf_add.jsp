<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.common.ao.center.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-实时模板信息配置</title>
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
</head>
<%
request.setCharacterEncoding("gbk");
String appName = request.getParameter("appName");
String appId = request.getParameter("appId");	//从上一个check页面传递过来
String tmpId = request.getParameter("tmpId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_dayConf_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}
</script>
<body>



<%


List<DayConfTmpPo> dayConfTmpList = DayConfAo.get().findAllDayConfTmp();
if(tmpId == null || tmpId.equals("") ) {
	
	for(DayConfTmpPo tmpPo : dayConfTmpList){
		
		tmpId = tmpPo.getTmpId() + "";
		break;
	}
	
}
/*
String appId = request.getParameter("appId");
if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>
<jsp:include page="../head.jsp"></jsp:include>


<form action="./rel_app_dayConf_add.jsp" method="get">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<select name="tmpId">
	<%
	
	for(DayConfTmpPo tmpPo : dayConfTmpList){ 		
		
	%>
	<option value="<%=tmpPo.getTmpId() %>"<%if(tmpPo.getTmpId() == Integer.parseInt(tmpId)){out.print("selected");} %>><%=tmpPo.getAliasLogName() %></option>
	<%} %>
</select>
<input type="submit"  value="获取模板">
<input type="hidden" name="appId" value="<%=appId %>">
<input type="hidden" name="appName" value="<%=appName %>">
</div>
</form>


<form action="./rel_app_dayConf_add.jsp" method="get">
<%


String action = request.getParameter("action");
if("add".equals(action)){
	String appId1 = request.getParameter("appId");
	String aliasLogName = request.getParameter("aliasLogName");
	String className = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String filePath =request.getParameter("filePath");
//	String future = request.getParameter("future");
//	String frequency = request.getParameter("frequency");
//	String tailType = request.getParameter("tailType");
//	String analyseType = request.getParameter("analyseType");
	
	
	DayConfPo po = new DayConfPo();
	po.setAppId(Integer.parseInt(appId1));
	po.setAliasLogName(aliasLogName);
	po.setClassName(className);
	po.setSplitChar(split);
	po.setFilePath(filePath);
//	po.setAnalyseFuture(future);
//	po.setAnalyseFrequency(Integer.parseInt(frequency));
//	po.setTailType(tailType);
//	po.setAnalyseType(Integer.parseInt(analyseType));
	boolean b = DayConfAo.get().addDayConfData(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("添加成功");}else{out.print("失败!出现异常");} %></font>
	<input type="hidden" name="appId" value="<%=appId %>">
	<input type="hidden" name="appName" value="<%=appName %>">
	<input type="button" onclick="goToCheck()" name="返回" value="返回 ">
	<%
}else{

	DayConfTmpPo tmp = DayConfAo.get().findDayConfTmpById(Integer.parseInt(tmpId));
	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加日报配置的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>日志文件别名: </td>
		<td><%=tmp.getAliasLogName() %></td>
	</tr>
	<tr>
		<td>分析器: </td>
		<td><%=tmp.getClassName() %></td>
	</tr>
	<tr>
		<td>行分隔符: </td>
		<td><%=tmp.getSplitChar() %></td>
	</tr>
	<tr>
		<td>文件路径: </td>
		<td><input type="text" name="filePath" size="100" value="<%=tmp.getFilePath() %>"></td>
	</tr>
	<tr>
		<td>分析器的附带参数: </td>
		<td><input type="text" name="future" value="" size="100"></td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="add" name="action">
		<input type="hidden" value="<%=appId %>" name="appId">
		<input type="hidden" value="<%=appName %>" name="appName">
		<input type="submit" value="添加新日报配置关联"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>