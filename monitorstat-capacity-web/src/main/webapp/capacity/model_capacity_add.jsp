<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-预测模型新增</title>
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
		request.setCharacterEncoding("gbk");
		String action = (String) request.getAttribute("action");
		String success = (String) request.getAttribute("success");
		if ("add".equals(action)) {
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
 			out.print("添加成功");
 		} else {
 			out.print("添加出现异常");
 		}
 %>
			</font><br />
			<br />
			<br /> <a href="../capacity/model.do?method=addCapacityModel">继续添加</a>
				<br />
			<br />
			<br />
			<br />
			<br /> <a href="javascript:winclose()">关闭窗口</a></td>
		</tr>
	</table>
	<%
		} else {
	%>
	<form id="myForm" name="myForm"
		action="./model.do?method=saveCapacityModel" method="post">
		<table id="mytable">

			<tr>
				<td>res_from:</td>
				<td><input type="text" name="resFrom" id="resFrom" value=""
					style="width: 80%;"></td>
			</tr>
			<tr>
				<td>res_to:</td>
				<td><input type="text" name="resTo" id="resTo" value=""
					style="width: 80%;"></td>
			</tr>
			<tr>
				<td>rel_ratio:</td>
				<td><input type="text" name="relRatio" id="relRatio" value=""
					style="width: 80%;"></td>
			</tr>
			<tr>
				<td align="right" colspan="2"><input type="hidden" value="add"
					name="action"> <input type="button" value="添    加"
					onclick="addApp()"> &nbsp;&nbsp; <input type="button"
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
	function addApp() {
		var resFrom = $("#resFrom").attr("value");
		if (resFrom == null || resFrom == "") {
			alert("res_from不能为空");
			return
		}
		var resTo = $("#resTo").attr("value");
		if (resTo == null || resTo == "") {
			alert("res_to不能为空");
			return
		}
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