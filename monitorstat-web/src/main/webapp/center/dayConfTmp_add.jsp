<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.DayConfTmpPo"%>
<%@page import="com.taobao.monitor.common.ao.center.DayConfAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-日报模板信息配置</title>
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
function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/dayConfTmp_center.jsp";
	location.href=url;
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

<form action="./dayConfTmp_add.jsp" method="post">
<%



String action = request.getParameter("action");
if("add".equals(action)){
	String aliasLogName = request.getParameter("aliasLogName");
	String analyseClass = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String filePath =request.getParameter("filePath");
	
	DayConfTmpPo po = new DayConfTmpPo();
	po.setAliasLogName(aliasLogName);
	po.setClassName(analyseClass);
	po.setSplitChar(split);
	po.setFilePath(filePath);
	boolean b = DayConfAo.get().addDayConfTmp(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("添加成功");}else{out.print("失败!出现异常");} %></font>	
	<a href="./dayConfTmp_center.jsp">返回</a>
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
		<td>日志文件别名: </td>
		<td><input type="text" name="aliasLogName" value=""></td>
	</tr>
	<tr>
		<td>行分隔符: </td>
		<td><input type="text" name="split" value=""></td>
	</tr>
	<tr>
		<td>分析器: </td>
		<td><input type="text" name="analyseClass" size="100" value=""></td>
	</tr>
	<tr>
		<td>文件路径: </td>
		<td><input type="text" name="filePath" size="100" value=""></td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="add" name="action">
		<input type="submit" value="添加新模板">
		<input type="button" value="返回" onclick="goToCenter()">
		</td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>