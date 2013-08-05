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
<title>�����滮-ģ�ͼ���</title>
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
					<td width="10%">Ԥ�㶩������:</td>
					<td width="20%"><input type="text" name="computeItems"
						id="computeItems" value="" style="width: 80%;"
						placeholder="�����������������ֶ���������"></td>
					<td width="10%">��ʼʱ��:</td>
					<td width="15%"><input type="text" name="startTime"
						id="startTime" value="<%=querydate%>" style="width: 80%;"
						class="Wdate" onClick="WdatePicker()"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
					<td width="10%">����ʱ��:</td>
					<td width="15%"><input type="text" name="endTime" id="endTime"
						value="<%=querydate%>" style="width: 80%;" class="Wdate"
						onClick="WdatePicker()"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
					<td width="10%"><input type="button" name="endTime"
						onclick="startCompute()" value="��ʼ����"></td>
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
			alert("Ԥ�㶩����������Ϊ��");
			return
		}
		if (!checkNum(computeItems)) {
			alert("Ԥ�㶩����������Ϊ����");
			return
		}
		var startTime = $("#startTime").attr("value");
		if (startTime == null || startTime == "") {
			alert("��ʼʱ�䲻��Ϊ��");
			return
		}
		var endTime = $("#endTime").attr("value");
		if (endTime == null || endTime == "") {
			alert("����ʱ�䲻��Ϊ��");
			return
		}
		if (!difDateDay(startTime, endTime)) {
			alert("����ʱ�������ܳ���30��");
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