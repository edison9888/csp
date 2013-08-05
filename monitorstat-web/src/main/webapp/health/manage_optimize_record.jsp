<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.rating.RatingOptimizeRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorRatingAo"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<title>应用优化记录列表</title>
		<style>
.report_on {
	background: #bce774;
}
</style>
		<link type="text/css"
			href="<%=request.getContextPath()%>/statics/css/themes/base/ui.all.css"
			rel="stylesheet" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/ui/ui.core.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/ui/ui.tabs.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/ui/ui.draggable.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/ui/ui.resizable.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/ui/ui.dialog.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery.bgiframe.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
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
	background: url(<%=request.getContextPath()%>/ statics/ images/ 4_17
		. gif );
}

img {
	cursor: pointer;
	border: 0;
}

.body_summary {
	margin: 5px 0;
	padding: 2px;
	border-top: 2px solid #ccc;
	border-bottom: 1px solid #ccc;
}
</style>

	</head>
	<body>
		<jsp:include page="../head.jsp"></jsp:include>	
		<jsp:include page="../left.jsp"></jsp:include>
		<%
			request.setCharacterEncoding("gbk");
			String action = request.getParameter("action");
			String appid = request.getParameter("appid");
			if ("delete".equals(action)) {
				RatingOptimizeRecord record = new RatingOptimizeRecord();
				record.setId(Integer.parseInt(request.getParameter("id")));
				MonitorRatingAo.get().deleteRatingOptimizeRecord(record);
			}
			List<RatingOptimizeRecord> list = MonitorRatingAo.get()
					.getRatingOptimizeRecord(Integer.parseInt(appid));
		%>
		<input type="button" value="添加新优化记录"
			onclick="location.href='./add_optimize_record.jsp?appid=<%=appid%>'">
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
			style="width: 100%;">
			<div
				class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
				<font color="#FF0000">评分应用优化记录列表</font>&nbsp;&nbsp;
			</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<table width="100%" border="1" class="ui-widget ui-widget-content">
					<tr class="headcon ">
						<td width="5%" align="center">
							记录ID
						</td>
						<td width="35%" align="center">
							主题
						</td>
						<td width="5%" align="center">
							提交人
						</td>
						<td width="5%" align="center">
							负责人
						</td>
						<td width="5%" align="center">
							记录状态
						</td>
						<td width="5%" align="center">
							优先级
						</td>
						<td width="5%" align="center">
							应用得分
						</td>
						<td width="7%" align="center">
							收集日
						</td>
						<td width="12%" align="center">
							最近修改时间
						</td>
						<td width="12%" align="center">
							创建时间
						</td>
						<td width="4%" align="center">
							操作
						</td>
					</tr>
					<%
						for (RatingOptimizeRecord ror : list) {
					%>
					<tr>
						<td width="5%" align="center"><%=ror.getId()%></td>
						<td width="35%" align="center">
							<a
								href='manage_optimize_solution.jsp?optimize_record_id=<%=ror.getId()%>'  style='color:#0000FF'><%=ror.getSubject()%></a>
						</td>
						<td width="5%" align="center"><%=ror.getSubmitter()%></td>
						<td width="5%" align="center"><%=ror.getOwner()%></td>
						<td width="5%" align="center"><%=ror.getStatus()%></td>
						<td width="5%" align="center"><%=ror.getPriority()%></td>
						<td width="5%" align="center"><%=ror.getRating_value()%></td>
						<td width="7%" align="center"><%=ror.getCollect_day()%></td>
						<td width="12%" align="center"><%= (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ror.getGmtModified())%></td>
						<td width="12%" align="center"><%= (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ror.getGmtCreate())%></td>
						<td width="4%" align="center">
							<%--<a onclick="deleteapp('<%=ror.getId()%>','<%=appid%>')" href="#">删除</a>&nbsp;&nbsp;--%>
							<a
								href="./update_optimize_record.jsp?id=<%=ror.getId()%>&appid=<%=appid%>">编辑</a>
						</td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
		</div>

<script type="text/javascript">
function deleteapp(id,appid){
	if(window.confirm('确认删除该条应用优化记录?')){
		location.href="manage_optimize_record.jsp?action=delete&id="+id+"&appid="+appid;
	}
}
</script>
	</body>

</html>