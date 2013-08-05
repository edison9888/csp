<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.rating.RatingOptimizeSolution"%>

<%@page import="com.taobao.monitor.web.ao.MonitorRatingAo"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<title>添加应用优化解决方案</title>
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
			if ("add".equals(action)) {
				RatingOptimizeSolution solution = new RatingOptimizeSolution();
				String optimize_record_id = request.getParameter("optimize_record_id");
				solution.setOptimize_record_id(Integer.parseInt(optimize_record_id));
				solution.setReason(request.getParameter("reason"));
				solution.setSolution(request.getParameter("solution"));
				solution.setSubmitter(request.getParameter("submitter"));
				MonitorRatingAo.get().addRatingOptimizeSolution(solution);
				response.sendRedirect("manage_optimize_solution.jsp?optimize_record_id=" + optimize_record_id);
			} else {
		%>
		<form action="./add_optimize_solution.jsp" method="post">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
				style="width: 100%;">
				<div
					class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
					<font color="#FF0000">请输入优化解决方案：</font>&nbsp;&nbsp;
				</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<table width="100%" border="1" class="ui-widget ui-widget-content"
						align="center">
						<tr align="left">
							<td>
								原因：
								<input type="text" name="reason" value="" size="128" />
							</td>
						</tr>
						<tr align="left">
							<td>
								解决方案：
								<input type="text" name="solution" value="" size="128" />
							</td>
						</tr>
						<tr align="left">
							<td>
								提交人：
								<input type="text" name="submitter" value="" size="10" />
							</td>
						</tr>
						<tr align="center">
							<td>
								<input type="hidden" name="action" value="add" />
								<input type="hidden" name="optimize_record_id"
									value="<%=request.getParameter("optimize_record_id")%>" />
								<input type="submit" value="提交" />
							</td>
						</tr>

					</table>
				</div>
			</div>
		</form>
		<%
			}
		%>
	</body>

</html>