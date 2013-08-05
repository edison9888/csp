<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.capacity.po.CapacityModelPo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-预测模型修改</title>
<link type="text/css"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/jquery.min.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap/bootstrap.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/capacity.js"></script>

</head>
<body class="span15">
	<%
		CapacityModelPo capacityModelPo = (CapacityModelPo) request
				.getAttribute("capacityModelPo");
		String action = (String) request.getAttribute("action");
		String success = (String) request.getAttribute("success");
		if ("edit".equals(action)) {
			boolean finished = "true".equals(success);
	%>
	<script type="text/javascript">
	function winclose() { 
	window.opener.location.reload(); 
	window.close(); 
} 
</script>
	<table id="mytable" align="center" width="100%">
		<tr>
			<td align="center" colspan="3" height="40"><font size="+3"
				color="red"> <%
 	if (finished) {
 			out.print("更新成功");
 		} else {
 			out.print("更新出现异常");
 		}
 %>
			</font> <br />
			<br />
			<br />
			<br />
			<br /> <a href="javascript:winclose()">关闭窗口</a></td>
		</tr>
	</table>
	<%
		} else {
			String modelId = capacityModelPo == null ? "" : capacityModelPo
					.getId();
			String resFrom = capacityModelPo == null ? "" : capacityModelPo
					.getResFrom();
			String resTo = capacityModelPo == null ? "" : capacityModelPo
					.getResTo();
			double relRation = capacityModelPo == null
					? 0.0
					: capacityModelPo.getRelRatio();
	%>
	<form id="myForm" name="myForm"
		action="./model.do?method=updateCapacityModel" method="post">
		<table id="mytable">
			<tr>
				<td>res_from:</td>
				<td><input type="text" name="resFrom" id="resFrom"
					value="<%=resFrom%>" readonly="readonly" style="width: 60%;">
					<input type="hidden" name="modelId" id="modelId"
					value="<%=modelId%>" readonly="readonly" style="width: 60%;">
				</td>
			</tr>
			<tr>
				<td>res_to:</td>
				<td><input type="text" name="resTo" id="resTo"
					value="<%=resTo%>" readonly="readonly" style="width: 60%;"></td>
			</tr>
			<tr>
				<td>rel_ratio:</td>
				<td><input type="text" name="relRatio" id="relRatio"
					value="<%=relRation%>" style="width: 60%;"></td>
			</tr>
			<tr>
				<td align="right" colspan="2"><input type="hidden" value="add"
					name="action"> <input type="button" value="修    改"
					onclick="updateModel()"> &nbsp;&nbsp; <input type="button"
					value="关    闭" onclick="winclose()"></td>
			</tr>
		</table>
		</div>
		</div>
	</form>
	<%
		}
	%>
<script type="text/javascript">
	function updateModel() {

		var relRatio = $("#relRatio").attr("value");
		if (relRatio == null || relRatio == "") {
			alert("rel_ratio不能为空");
			return
		}
		if (!checkNum(relRatio)) {
			alert("rel_ratio必须为数字或者小数");
			return
		}
		document.myForm.submit()
	}
	function checkNum(relRatio) {
		if (relRatio.match(/^\+?(:?(:?\d+\.\d+)|(:?\d+))$/)) {
			return true
		} else {
			return false
		}
	}

	function winclose() {
		window.opener.location.reload();
		window.close();
	}
</script>
</body>
</html>