<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-实时模板信息配置</title>
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
	

	function goToManagerCenter(){
		
		var url = "<%=request.getContextPath () %>/center/manage_center.jsp";
		location.href=url;
	}
	
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

	TimeConfAo.get().deleteTimeConfTmp(Integer.parseInt(id));
}
%>

<input type="button" value="添加新的实时配置模板" onclick="location.href='./timeConfTmp_add.jsp'">
<input type="button" value="返回" onclick="location.href='./manage_center.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">实时配置模板列表</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
List<TimeConfTmpPo> tmpList = TimeConfAo.get().findAllAppTimeConfTmp();
%>
<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
	
		<td align="center">日志文件别名</td>
		<td align="center">分析器</td>
		<td align="center">行分隔符</td>
		<td align="center">收集频率</td>
		<td align="center">tail类型</td>
		<td align="center">数据获取</td>
		<td align="center">分析方式</td>
		<td align="left">  操作</td>
		
	</tr>
	<%for(TimeConfTmpPo po: tmpList){ %>
	<tr >
		<td align="center"><%=po.getAliasLogName() %></td>
		<td align="center"><%=po.getClassName()%></td>
		<!-- <td align="center"><%=po.getFilePath()%></td> -->
		<td align="center"><%=po.getSplitChar() %></td>
		<!--<td align="center"><%=po.getAnalyseFuture()%></td> -->
		<td align="center"><%=po.getAnalyseFrequency()%></td>
		<td align="center"><%=po.getTailType()%></td>
		<td align="center">
			<%if(po.getObtainType()==1){out.print("日志文件");}
			if(po.getObtainType()==2){out.print("shell方式");} 
			if(po.getObtainType()==3){out.print("http方式");}
			%>
		</td>
		<td align="center">
			<%  if(po.getAnalyseType()==1){out.print("java-class");}
				if(po.getAnalyseType()==2){out.print("javscript");} 
			%>
		</td>
		<td>
			<a href="./timeConfTmp_update.jsp?id=<%=po.getTmpId()%>">修改</a>&nbsp;&nbsp;
			<a id="deleteAction" class="deleteAction" href="./timeConfTmp_center.jsp?id=<%=po.getTmpId() %>&action=delete">删除</a> &nbsp;&nbsp;
			<a href="./timeConfTmp_check.jsp?id=<%=po.getTmpId() %>">查看</a> &nbsp;&nbsp;
		</td>
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>