<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.capacity.po.CapacityModelPo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-预测模型修改</title>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/jquery-1.4.4.js"></script>
<link type="text/css"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="span15">
	<%
		CapacityModelPo capacityModelPo = (CapacityModelPo) request
				.getAttribute("capacityModelPo");
		String querydate = (String) request.getAttribute("querydate");
		String modelId = capacityModelPo == null ? "" : capacityModelPo
				.getId();
		String resFrom = capacityModelPo == null ? "" : capacityModelPo
				.getResFrom();
		String resTo = capacityModelPo == null ? "" : capacityModelPo
				.getResTo();
		double relRation = capacityModelPo == null ? 0.0 : capacityModelPo
				.getRelRatio();
	%>
	<div class="span15" id="autoComId" style="display: block;">
		<table id="mytable" class="span15">
			<tr>
				<td width="300">res_from:</td>
				<td><input type="text" name="resFrom" id="resFrom"
					value="<%=resFrom%>" readonly="readonly" style="width: 60%;">
					<input type="hidden" name="modelId" id="modelId"
					value="<%=modelId%>" readonly="readonly" style="width: 60%;">
				</td>
			</tr>
			<tr>
				<td width="300">res_to:</td>
				<td><input type="text" name="resTo" id="resTo"
					value="<%=resTo%>" readonly="readonly" style="width: 60%;"></td>
			</tr>
			<tr>
				<td width="300">rel_ratio原始值:</td>
				<td><input type="text" name="relRatio" id="relRatio"
					value="<%=relRation%>" readonly="readonly" style="width: 60%;"></td>
			</tr>
			<tr>
				<td width="300">计算参照时间:</td>
				<td><input type="text" name="startTime" id="startTime"
					value="<%=querydate%>" style="width: 60%;" class="Wdate"
					onClick="WdatePicker()"
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
			</tr>
			<tr>
				<td align="right" colspan="2">  <input type="button" value="开始计算"
					onclick="startComputer()"> &nbsp;&nbsp; <input type="button"
					value="关    闭" onclick="winclose()"></td>
			</tr>
		</table>
	</div>
	
	<div class="span15" id="onloadinfo" style="display:none;">
		<p
			style="text-align: center; width: 100%; height: 200px; margin-top: 40px;">
			<br /> <br /> <br /> <img
				src="<%=request.getContextPath()%>/statics/images/loading.gif"
				height="18" width="25">&nbsp;&nbsp;&nbsp;&nbsp; <strong
				style="font-weight: bolder; font-size: 16px; height: 15px;">正在计算
				.......</strong>
		</p>
	</div>

	<script type="text/javascript">
	
// 	computerRateInfo("2012-05-26",4);
	function computerRateInfo(startTime,modelId) {
		var urlDest = "./model.do?method=autoComputerModelRelRatio";
	    var parameters = "computeStartTime="+startTime+"&modelId="+modelId;
			$.ajax({
				url : urlDest,
				async : false,
				type : "POST",
				dataType : "String",
				data : parameters,
				cache : false,
				success : function(data) {
					if (data ==  1 ) {
						alert("重新计算成功!");
						winclose();
					}else{
						alert("重新计算失败!");
						winclose();
					}
				}
			});
		}
	
	function startComputer() {
		var startTime = $("#startTime").attr("value");
		var modelId = $("#modelId").attr("value");
		if (startTime == null || startTime == "") {
			alert("计算时间不能为空");
			return
		}
		$('#autoComId').css({
			'display' : 'none'
		});
		$('#onloadinfo').css({
			'display' : 'block'
		});
		computerRateInfo(startTime,modelId);
		
	}
 
	function winclose() {
		window.opener.location.reload();
		window.close();
	}
</script>
</body>
</html>