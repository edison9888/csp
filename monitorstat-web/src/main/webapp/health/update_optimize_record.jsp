<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.rating.RatingOptimizeRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorRatingAo"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<title>更新评分应用优化记录</title>
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

			String id = request.getParameter("id");
			RatingOptimizeRecord ror = MonitorRatingAo.get()
					.getRatingOptimizeRecordById(Integer.parseInt(id));

			String action = request.getParameter("action");
			if ("update".equals(action)) {
				RatingOptimizeRecord record = new RatingOptimizeRecord();
				record.setId(Integer.parseInt(request.getParameter("id")));
				record.setOwner(request.getParameter("owner"));
				record.setPriority(request.getParameter("priority"));
				record.setStatus(request.getParameter("status"));
				record.setSubject(request.getParameter("subject"));
				record.setSubmitter(request.getParameter("submitter"));
				record.setDescription(request.getParameter("description"));
				record.setComment(request.getParameter("comment"));
				MonitorRatingAo.get().updateRatingOptimizeRecord(record);
				String appid = request.getParameter("appid");
				response.sendRedirect("manage_optimize_record.jsp?appid=" + appid);
			} else {
		%>
		<form action="./update_optimize_record.jsp" method="post"
			onsubmit="return window.confirm('确认修改吗?')&&checkSubmit()">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
				style="width: 100%;">
				<div
					class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
					<font color="#FF0000">更新评分应用优化记录</font>&nbsp;&nbsp;
				</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<table width="100%" border="1" class="ui-widget ui-widget-content">

						<tr align="left">
							<td>
								主题：
								<input type="text" name="subject" id="subject" value="<%=ror.getSubject()%>"
									size="128" />
							</td>
						</tr>
						<tr align="left">
							<td>
								提交人：
								<input type="text" name="submitter" id="submitter"
									value="<%=ror.getSubmitter()%>" size="20" />
							</td>
						</tr>
						<tr align="left">
							<td>
								负责人：
								<input type="text" name="owner"
									value="<%=ror.getOwner()%>" size="20" />
							</td>
						</tr>
						<tr align="left">
							<td>
								问题状态：
								<select name="status" id="status">
									<option value="new">
										new
									</option>
									<option value="closed">
										closed
									</option>
									<option value="wontfix">
										wontfix
									</option>
									<option value="later">
										later
									</option>	
									<option value="fixed">
										fixed
									</option>										
								</select>
							</td>
						</tr>
						<tr align="left">
							<td>
								优先级：
								<select name="priority" id="priority">
									<option value="high">
										high
									</option>
									<option value="medium">
										medium
									</option>
									<option value="low">
										low
									</option>
								</select>
							</td>
						</tr>
						<tr align="left">
							<td>
								描述：
								<input type="text" name="description" value="<%=ror.getDescription()%>" size="150" />
							</td>
						</tr>
						<tr align="left">
							<td>
								备注：
								<input type="text" name="comment" value="<%=ror.getComment()%>" size="100" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="3">
								<input type="hidden" name="id"
									value="<%=request.getParameter("id")%>" size="40" />
								<input type="hidden" name="appid"
									value="<%=request.getParameter("appid")%>" size="40" />
								<input type="hidden" name="action" value="update" />
								<input type="submit" value="更新优化记录" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<%
			}
		%>
<script type="text/javascript">
            function checkSubmit(){
    			if(document.getElementById("subject").value == "") {
    			 	document.getElementById("subject").focus();
       				alert('主题不允许为空，请重新输入');  
       				return   false;  
    			}  
    			if(document.getElementById("submitter").value == "") {
    			 	document.getElementById("submitter").focus();
       				alert('提交人不允许为空，请重新输入');  
       				return   false;  
    			}   
    			return true;
			}
</script>		
	</body>

</html>