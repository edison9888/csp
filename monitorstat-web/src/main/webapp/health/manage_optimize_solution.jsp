<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.rating.RatingOptimizeSolution"%>
<%@page import="com.taobao.monitor.web.rating.RatingOptimizeRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.monitor.web.ao.MonitorRatingAo"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<title>应用优化解决方案列表</title>
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
			String rid = request.getParameter("optimize_record_id");
			if ("delete".equals(action)) {
				RatingOptimizeSolution record = new RatingOptimizeSolution();
				record.setId(Integer.parseInt(request.getParameter("id")));
				MonitorRatingAo.get().deleteRatingOptimizeSolution(record);
			} else if ("add".equals(action)) {
				RatingOptimizeSolution solution = new RatingOptimizeSolution();
				solution.setOptimize_record_id(Integer.parseInt(rid));
				solution.setReason(request.getParameter("reason"));
				solution.setSolution(request.getParameter("solution"));
				solution.setSubmitter(request.getParameter("submitter"));
				MonitorRatingAo.get().addRatingOptimizeSolution(solution);
				response.sendRedirect("manage_optimize_solution.jsp?optimize_record_id=" + rid);
			}
						
			List<RatingOptimizeSolution> list = MonitorRatingAo.get()
					.getRatingOptimizeSolution(Integer.parseInt(rid));
			RatingOptimizeRecord ror = MonitorRatingAo.get()
					.getRatingOptimizeRecordById(Integer.parseInt(rid));		
		%>
		
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
			style="width: 100%;">
			<div
				class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
				<font color="#FF0000">优化记录</font>&nbsp;&nbsp;
			</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<table width="100%" border="1" class="ui-widget ui-widget-content">
					<tr align="left">
						<td >
							<b>标&nbsp;&nbsp;&nbsp;&nbsp;题</b> :&nbsp;&nbsp;<%=ror.getSubject()%>
						</td>
					</tr>
					<tr align="left">
						<td >
							<b>描&nbsp;&nbsp;&nbsp;&nbsp;述</b> :&nbsp;&nbsp;<%=ror.getDescription()%>
						</td>
					</tr>
					<tr align="left">
						<td >
							<b>提交人</b>&nbsp; :&nbsp;&nbsp;<%=ror.getSubmitter()%>
						</td>
					</tr>
					<tr align="left">
						<td >
							<b>负责人</b>&nbsp; :&nbsp;&nbsp;<%=ror.getOwner()%>
						</td>
					</tr>
					<tr align="left">
						<td >
							<b>时&nbsp;&nbsp;&nbsp;&nbsp;间</b> :&nbsp;&nbsp;<%=(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ror.getGmtCreate())%>
						</td>
					</tr>
					<tr align="left">
						<td >
							<b>备&nbsp;&nbsp;&nbsp;&nbsp;注</b> :&nbsp;&nbsp;<%=ror.getComment()%>
						</td>
					</tr>					
				</table>
			</div>
		</div>


		<form action="./manage_optimize_solution.jsp"  method="post"  onsubmit="return checkSubmit1()">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
				style="width: 100%;">
				<div
					class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
					<font color="#FF0000">新建优化解决方案：</font>&nbsp;&nbsp;
				</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<table width="100%" border="1" class="ui-widget ui-widget-content"
						align="center">
						<tr align="left">
							<td>
								<font color="#FF0000">*&nbsp;</font>原&nbsp;&nbsp;&nbsp;因：
								<input type="text" name="reason" id="reason" value="" size="128" />
							</td>
						</tr>
						<tr align="left">
							<td>
								<font color="#FF0000">*&nbsp;</font>方&nbsp;&nbsp;&nbsp;案：
								<input type="text" name="solution" id="solution" value="" size="128" />
							</td>
						</tr>
						<tr align="left">
							<td>
								<font color="#FF0000">*&nbsp;</font>提交人：
								<input type="text" name="submitter" id="submitter" value="" size="10" />
							</td>
						</tr>
						<tr align="center">
							<td>
								<input type="hidden" name="action" value="add" />
								<input type="hidden" name="optimize_record_id"
									value="<%=request.getParameter("optimize_record_id")%>" />
								<input type="submit" value="提交"  />
							</td>
						</tr>

					</table>
				</div>
			</div>
		</form>

		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all "
			style="width: 100%;">
			<div
				class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
				<font color="#FF0000">优化解决方案列表</font>&nbsp;&nbsp;
			</div>
			
			
			
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<table width="100%" border="1" class="ui-widget ui-widget-content">
					<tr class="headcon ">
						<td width="4%" align="center">
							方案ID
						</td>
						<td width="30%" align="center">
							问题原因
						</td>
						<td width="5%" align="center">
							提交人
						</td>
						<td width="50%" align="center">
							问题解决方案
						</td>
						<td width="7%" align="center">
							创建时间
						</td>
						<td width="4%" align="center">
							操作
						</td>
					</tr>
					<%
						for (RatingOptimizeSolution ros : list) {
					%>
					<tr>
						<td width="4%" align="center"><%=ros.getId()%></td>
						<td width="30%" align="center"><%=ros.getReason()%></td>
						<td width="5%" align="center"><%=ros.getSubmitter()%></td>
						<td width="50%" align="center"><%=ros.getSolution()%></td>
						<td width="7%" align="center"><%=ros.getGmtCreate()%></td>
						<td width="4%" align="center">
							<%--<a onclick="deleteapp('<%=ros.getId()%>','<%=rid%>')" href="#">删除</a>&nbsp;&nbsp;--%>
							<a
								href="./update_optimize_solution.jsp?optimize_record_id=<%=rid%>&id=<%=ros.getId()%>">编辑</a>
						</td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
		</div>
		
		
<script type="text/javascript">
  function deleteapp(id,optimize_record_id){
	if(window.confirm('确认删除该条应用优化解决方案?')){
		location.href="manage_optimize_solution.jsp?action=delete&id="+id+"&optimize_record_id="+optimize_record_id;
	}
  }	
  function checkSubmit1(){
    			if(document.getElementById("reason").value == "") {
    			 	document.getElementById("reason").focus();
       				alert('原因不允许为空，请重新输入');  
       				return   false;  
    			}  
    			if(document.getElementById("solution").value == "") {
    			 	document.getElementById("solution").focus();
       				alert('方案不允许为空，请重新输入');  
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