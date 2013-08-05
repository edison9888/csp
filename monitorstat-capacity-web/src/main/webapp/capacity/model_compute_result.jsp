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
	<!-- 	���ݼ��صȴ� -->
	<div class="span20" id="onloadinfo" style="display: block;">
		<p
			style="text-align: center; width: 100%; height: 200px; margin-top: 40px;">
			<br /> <br /> <br /> <img
				src="<%=request.getContextPath()%>/statics/images/loading.gif"
				height="18" width="25">&nbsp;&nbsp;&nbsp;&nbsp; <strong
				style="font-weight: bolder; font-size: 16px; height: 15px;">���ڼ�������
				.......</strong>
		</p>
	</div>
	<!-- 	����չʾ�� -->
	<div class="span20" id="onloadData" style="display: none;">
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>ҳ��Ӧ��</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>Ӧ����Ϣ1:</td>
					<td>Ӧ����Ϣ2:</td>
					<td>Ӧ����Ϣ3:</td>
					<td>Ӧ����Ϣ4:</td>
					<td>Ӧ����Ϣ5:</td>
					<td>Ӧ����Ϣ6:</td>
					<td>Ӧ����Ϣ7:</td>
				</tr>
			</thead>
			<tbody id="pageId">
			</tbody>
		</table>
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>HSFӦ��</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>HSFӦ����Ϣ1:</td>
					<td>HSFӦ����Ϣ2:</td>
					<td>HSFӦ����Ϣ3:</td>
					<td>HSFӦ����Ϣ4:</td>
					<td>HSFӦ����Ϣ5:</td>
					<td>HSFӦ����Ϣ6:</td>
				</tr>
			</thead>
			<tbody id="hsfId">
			</tbody>
		</table>
		<div class="span20">
			&nbsp;&nbsp;&nbsp;<strong>TairӦ��</strong>
		</div>
		<br />
		<table class='bordered-table' id='mytable' width='100%'>
			<thead>
				<tr>
					<td>TairӦ����Ϣ1:</td>
					<td>TairӦ����Ϣ2:</td>
					<td>TairӦ����Ϣ3:</td>
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