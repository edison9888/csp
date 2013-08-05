<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript"
	src="<%=request.getContextPath() %>/statics/My97DatePicker/WdatePicker.js"></script>
<title>容量规划-模型计算</title>
</head>
<body class="span20">
	<%@ include file="../top.jsp"%>
<div class="span20">
		<%
        request.setCharacterEncoding("gbk");
		String querydate = (String) request.getAttribute("querydate");
    %>
    <table class="bordered-table" id="mytable">
			<tbody>
				<tr>
					<td width="10%">预算订单笔数:</td>
					<td width="20%"><input type="text" name="computeItems"
						id="computeItems" value="" style="width: 80%;"
						placeholder="订单笔数必须是数字而且是整数"></td>
					<td width="10%">开始时间:</td>
					<td width="15%"><input type="text" name="startTime"
						id="startTime" value="<%=querydate%>" style="width: 80%;"
						class="Wdate" onClick="WdatePicker()"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
					<td width="10%">结束时间:</td>
					<td width="15%"><input type="text" name="endTime" id="endTime"
						value="<%=querydate%>" style="width: 80%;" class="Wdate"
						onClick="WdatePicker()"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
					<td width="10%"><input type="button" name="endTime"
						onclick="startCompute()" value="开始计算"></td>
				</tr>
			</tbody>
		</table>
</div>
<div class="span20">
	<iframe id="computeResilt" width="100%" height="1200" frameborder=0
		scrolling="no" src=""></iframe>
</div>
<script type="text/javascript">
	function startCompute() {
		var computeItems = $("#computeItems").attr("value");
		if (computeItems == null || computeItems == "") {
			alert("预算订单笔数不能为空");
			return
		}
		if (!checkNum(computeItems)) {
			alert("预算订单笔数必须为数字");
			return
		}
		var startTime = $("#startTime").attr("value");
		if (startTime == null || startTime == "") {
			alert("开始时间不能为空");
			return
		}
		var endTime = $("#endTime").attr("value");
		if (endTime == null || endTime == "") {
			alert("结束时间不能为空");
			return
		}
		if (!difDateDay(startTime, endTime)) {
			alert("计算时间间隔不能超出30天");
			return
		}
		var url = "./model.do?method=modelComputeResult&computeItems=" + computeItems + "&startTime=" + startTime + "&endTime=" + endTime;
		$("#computeResilt").attr("src", url)
	}
	function checkNum(relRatio) {
		if (relRatio.match(/^\+?(:?(:?\d+\.\d+)|(:?\d+))$/)) {
			return true
		} else {
			return false
		}
	}
	function difDateDay(strartDate, endDate) {
		var aDate, oDate1, oDate2, iDays;
		aDate = strartDate.split("-");
		oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
		aDate = endDate.split("-");
		oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
		var i = (oDate1 - oDate2) / 1000 / 60 / 60 / 24;
		iDays = i;
		if (iDays < -30) {
			return false
		} else {
			return true
		}
	}
</script>
</body>
</html>