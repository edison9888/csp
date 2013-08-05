<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>ʵʱ���ϵͳ</title>
<%@ include file="/time/common/base.jsp"%>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/swfobject.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
</head>
<script>
	$(function() {
		$( "#time" ).datepicker();
		$( "#time" ).datepicker("option", "dateFormat", "yy-mm-dd");
		$( "#time" ).datepicker( "setDate" , "${time }");
	});
	</script>
<body>
	<%@include file="/header.jsp"%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
							<div align="center" class="row" style="padding-bottom: 10px">
								<form action="<%=request.getContextPath() %>/data/show.do?method=baseline" id="form" method="POST">
									<input type="hidden" value="yes" name="flag">
									app����:<input type="text" name="appName" value="${appName }">
									key����:<input type="text" name="keyName" value="${keyName }">
									property����:<input type="text" name="propertyName" value="${propertyName }">
									ʱ��:<input id="time" type="text"  value="${time }"  name="time">								
									<input class="btn  primary" type="button" value="��ѯ" id="button">
									<input id="button2" class="btn btn-success" type="button"  value="���¼���">
									<input id="button3" class="btn btn-success" type="button"  value="�鿴Ӧ�û���״̬">
									<input id="button4" class="btn btn-success" type="button"  value="���¼���Ӧ�����л���">
									<input id="button5" class="btn btn-success" type="button"  value="���Ӧ�û���">
								</form>
							</div>
			</div>
		</div>
		<div class="row-fluid">
		<c:if test="${flag=='yes'}">
			<c:if test="${(cki.keyScope=='ALL') or ( cki.keyScope=='APP' )}">
			<div class="span6">
				<table width="100%">
					<caption>ȫ������</caption>
										<tr>
											<td width="100%" id="pvchartDivId" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
												var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
												so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
												so4.addVariable("chart_id", "amline4");  
												so4.addVariable("wmode", "transparent");
												so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
												so4.addVariable("data_file", escape("<%=request.getContextPath()%>/data/show.do?method=baselineData&appName=${appName}&keyId=${keyId}&propertyName=${propertyName}&time=${time}"));
												so4.write("pvchartDivId");
												</script>
											</td>
										</tr>
									</table>	
			</div>
			</c:if>
			<c:if test="${(cki.keyScope=='ALL') or ( cki.keyScope=='HOST' )}">
			<div class="span6">
				<table width="100%">
							<caption>�澯������������</caption>
										<tr>
											<td width="100%" id="pvchartDivId2" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
													var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
													so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
													so4.addVariable("chart_id", "amline4");  
													so4.addVariable("wmode", "transparent");
													so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
													so4.addVariable("data_file", escape("<%=request.getContextPath()%>/data/show.do?method=baselineDataCM&appName=${appName}&keyId=${keyId}&propertyName=${propertyName}&time=${time}"));
													so4.write("pvchartDivId2");
												</script>
											</td>
										</tr>
									</table>					
			</div>
			</c:if>
			</c:if>
		</div>
		<div class="row-fluid">
			<c:if test="${flag=='state'}">
				<table class="table table-striped table-bordered table-condensed">
				<caption>Ӧ�ø澯����:${all } �����������:${stored } �����쳣��${all-stored } </caption>
				<thead>
					<tr>
					<td>Ӧ����</td>
					<td>KEY��</td>
					<td>������</td>
					<td>����</td>
					<td>��Χ</td>
					<td>״̬</td>
					<td>ʱ��</td>
					<td>�鿴</td>
					</tr>
				</thead>	
				<c:forEach var="item" items="${baselinechecks}">
					<tr>
					<td>${item.appName}</td>
					<td>${item.keyName}</td>
					<td>${item.propertyName}</td>
					<td>${item.weekDay}</td>
					<td>${item.scope}</td>
					<td>${item.state}</td>
					<td>${item.processTime}</td>
					<td><a href="${item.url }">�鿴</a></td>
					</tr>
				</c:forEach>
				</table>
			</c:if>
		</div>
	</div>
</body>
<script>
	$(function(){
		$("#button").click(function(){ 
			var f = $("input[name='time']");
			var a = $("input[name='appName']")
			var k = $("input[name='keyName']");
			var p = $("input[name='propertyName']");
			/**����֤ */
			if ( $.trim(f.val()) == '') {
				//f.focus();
				alert("����������!");
				return;
			}
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("������Ӧ��!");
				return;
			}
			if ( $.trim(k.val()) == '') {
				//f.focus();
				alert("������KEY!");
				return;
			}
			if ( $.trim(p.val()) == '') {
				//f.focus();
				alert("����������!");
				return;
			}
			$("#form").submit();

		});
		$("#button2").click(function(){ 
			var f = $("input[name='time']");
			var a = $("input[name='appName']")
			var k = $("input[name='keyName']");
			var p = $("input[name='propertyName']");
			/**����֤ */
			if ( $.trim(f.val()) == '') {
				//f.focus();
				alert("����������!");
				return;
			}
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("������Ӧ��!");
				return;
			}
			if ( $.trim(k.val()) == '') {
				//f.focus();
				alert("������KEY!");
				return;
			}
			if ( $.trim(p.val()) == '') {
				//f.focus();
				alert("����������!");
				return;
			}
			$("#form").attr('action',base+'/data/show.do?method=reprocess');
			$("#form").submit();

		});
		$("#button3").click(function(){ 
			var a = $("input[name='appName']");
			/**����֤ */
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("������Ӧ��!");
				return;
			}
			$("#form").attr('action',base+'/data/show.do?method=baselineSateOfApp');
			$("#form").submit();

		});
		$("#button4").click(function(){ 
			var a = $("input[name='appName']")
			/**����֤ */
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("������Ӧ��!");
				return;
			}
			$("#form").attr('action',base+'/data/show.do?method=renewAllBaseline');
			$("#form").submit();

		});
		$("#button5").click(function(){ 
			var a = $("input[name='appName']")
			/**����֤ */
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("������Ӧ��!");
				return;
			}
			$("#form").attr('action',base+'/data/show.do?method=baselineCheck');
			$("#form").submit();

		});
	});
</script>
</html>