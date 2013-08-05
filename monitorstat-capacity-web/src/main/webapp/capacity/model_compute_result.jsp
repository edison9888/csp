<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/jquery-1.4.4.js"></script>
<link type="text/css"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
</head>
<body style="background-color: #fff;">
	<%
		request.setCharacterEncoding("gbk");
		String computeItems = (String) request.getAttribute("computeItems");
		String computeStartTime = (String) request
				.getAttribute("computeStartTime");
		String computeEndTime = (String) request
				.getAttribute("computeEndTime");
	%>
	<!-- 	数据加载等待 -->
	<div class="span20" id="onloadinfo" style="display: block;">
		<p
			style="text-align: center; width: 100%; height: 200px; margin-top: 40px;">
			<br /> <br /> <br /> <img
				src="<%=request.getContextPath()%>/statics/images/loading.gif"
				height="18" width="25">&nbsp;&nbsp;&nbsp;&nbsp; <strong
				style="font-weight: bolder; font-size: 16px; height: 15px;">正在加载数据
				.......</strong>
		</p>
	</div>
	<!-- 	数据展示区 -->
	<div class="span20" id="onloadData" style="display: none;">
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>页面应用</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>应用信息1:</td>
					<td>应用信息2:</td>
					<td>应用信息3:</td>
					<td>应用信息4:</td>
					<td>应用信息5:</td>
					<td>应用信息6:</td>
					<td>应用信息7:</td>
				</tr>
			</thead>
			<tbody id="pageId">
			</tbody>
		</table>
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>HSF应用</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>HSF应用信息1:</td>
					<td>HSF应用信息2:</td>
					<td>HSF应用信息3:</td>
					<td>HSF应用信息4:</td>
					<td>HSF应用信息5:</td>
					<td>HSF应用信息6:</td>
				</tr>
			</thead>
			<tbody id="hsfId">
			</tbody>
		</table>
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>Tair应用</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>Tair应用信息1:</td>
					<td>Tair应用信息2:</td>
					<td>Tair应用信息3:</td>
				</tr>
			</thead>
			<tbody id="tairId">
			</tbody>
		</table>
	</div>
	<br />
	<br />
	<script type="text/javascript">
	
	getModelInfo();
	
	function getModelInfo() {
		var urlDest = "./model.do?method=modelResultInfo";
	    var parameters = "computeItems=<%=computeItems%>&computeStartTime=<%=computeStartTime%>&computeEndTime=<%=computeEndTime%>
		";
			$.ajax({
				url : urlDest,
				async : true,
				type : "POST",
				dataType : "String",
				data : parameters,
				cache : false,
				success : function(data) {
					if (data.length > 0) {
						$('#onloadinfo').css({
							'display' : 'none'
						});
						$('#onloadData').css({
							'display' : 'block'
						});
						var dataStr = data.split(";");
						$('#pageId').append(dataStr[0]);
						$('#hsfId').append(dataStr[1]);
						$('#tairId').append(dataStr[2]);
					}
				}
			});
		}
	</script>
</body>
</html>