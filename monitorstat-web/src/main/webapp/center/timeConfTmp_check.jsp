<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%><html>
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
<script type="text/javascript">
function goToCenter(){
	
	var url = "<%=request.getContextPath () %>/center/timeConfTmp_center.jsp";
	location.href=url;
}

</script>

</head>
<body>



<%
/*
request.setCharacterEncoding("gbk");

if(!UserPermissionCheck.check(request,"adduser","")){
	out.print("你没有权限操作!");
	return;
}
*/
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./dayConfTmp_update.jsp" method="post">
<%
	String id = request.getParameter("id");
	TimeConfTmpPo po = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(id));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">查看实时配置模板的基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">	
	<tr>
		<td>模版名称: </td>
		<td><%=po.getAliasLogName() %></td>
	</tr>	
	<tr>

		<td>数据行分隔符: </td>
		<td>
			<%=po.getSplitChar() %>
		</td>
	</tr>	
	<tr>
		<td>收集频率: </td>
		<td><%=po.getAnalyseFrequency() %>s</td>
	</tr>
	<tr>

		<td>tail模式: </td>
		<td>
			<%=po.getTailType() %>
		</td>
	</tr>
	<tr>
		<td>数据获取:  </td>
		<td>
			<%if(po.getAnalyseType()==1){out.print("日志文件");}
			if(po.getAnalyseType()==2){out.print("shell方式");} 
			if(po.getAnalyseType()==3){out.print("http方式");}
			%>
		</td>
	</tr>
	<tr>
		<td>文件路径: </td>
		<td><%=po.getFilePath() %></td>
	</tr>
	<tr>
		<td>分析方式: </td>
		<td>
			<%  if(po.getObtainType()==1){out.print("java-class");}
				if(po.getObtainType()==2){out.print("javscript");} 
			%>
		</td>
	</tr>
	<tr>
		<td>分析器: </td>
		<td><%=po.getClassName() %></td>
	</tr>	
	<tr>
		<td>分析器的附带参数: </td>
		<td><%=po.getAnalyseFuture() %></td>
	</tr>
	<tr>
		<td>描述说明: </td>
		<td>
			<%=po.getAnalyseDesc() %>
		</td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
	<td align="center"><input type="button" value="返回" onclick="goToCenter()"></td>
	</tr>
</table>
</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>