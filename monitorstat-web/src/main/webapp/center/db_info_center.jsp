<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>

<%@page import="com.taobao.monitor.common.ao.center.DataBaseInfoAo"%>
<%@page import="com.taobao.monitor.common.po.DataBaseInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>配置中心-数据库信息配置</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<style type="text/css">
div {
	font-size: 12px;
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
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#userInfoTableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#userInfoTableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
		$(".deleteAction").click(function(){
			return confirm("想清楚要删除了没有啊");
		});
	})	
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("你没有权限操作!");
	return;
}
%>
<%


String id = request.getParameter("id");

String action = request.getParameter("action");
if("delete".equals(action)){
	DataBaseInfoAo.get().deleteDataBaseInfo(Integer.parseInt(id));
}
%>

<input type="button" value="添加新的数据库" onclick="location.href='./db_info_add.jsp'">
<input type="button" value="返回" onclick="location.href='./manage_center.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">数据库列表</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
List<DataBaseInfoPo> dbInfo = DataBaseInfoAo.get().findAllDataBaseInfo();
%>
<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
		<td align="center" >数据库名</td>
		<td align="center" >昨天数据量</td>
		<td align="center" >URL</td>
		<td align="center" >类型</td>
		<td align="center">操作</td>
	</tr>
	<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH, -1);
	for(DataBaseInfoPo po: dbInfo){ 
		
		
	%>
	<tr>
	<td align="center"><%=po.getDbName()%></td>
	<td align="center">
		<%
		if(po.getDbType()==1){
			long num = DataBaseInfoAo.get().findDataBaseRecordNum(po.getDbId(),Integer.parseInt(sdf.format(cal.getTime())));
			out.print("("+num+")");
		}
		%>
	</td>
	<td align="left"><%=po.getDbUrl() %></td>
	<td align="center"><%=((po.getDbType() == 0) ? "核心库":((po.getDbType() == 1 )? "业务库":"外部库"))%></td>
	<td>
		<a href="./db_info_update.jsp?id=<%=po.getDbId()%>">修改</a>&nbsp;
		<a id="deleteAction" class="deleteAction" href="./db_info_center.jsp?id=<%=po.getDbId() %>&action=delete">删除</a> &nbsp;
		<a href="./db_info_check.jsp?id=<%=po.getDbId() %>">查看</a> &nbsp;
		<a href="./db_rel_app_time_center.jsp?databaseId=<%=po.getDbId() %>">设置实时应用</a> &nbsp;&nbsp;
		<a href="./db_rel_app_day_center.jsp?databaseId=<%=po.getDbId()%>">设置日报应用</a> &nbsp;&nbsp;
	</td>
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>