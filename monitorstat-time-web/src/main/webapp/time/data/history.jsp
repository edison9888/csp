<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>实时监控系统</title>
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
<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
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
							<div class="row" style="padding-bottom: 10px">
								<form action="<%=request.getContextPath() %>/data/show.do" id="form" method="POST">
									<input  type="hidden"  value="history"  name="method">
									<input type="hidden" value="yes" name="flag">
									<span style="margin-left:100px">app:</span><input type="text" name="appName" value="${appName }">
									key:<input type="text" name="keyName" value="${keyName }">
									property:<input type="text" name="propertyName" value="${propertyName }">									
									时间:<input id="time" type="text"  value="${time }"  name="time"><br>
									<span style="margin-left:100px">IP:</span><input type="text" name="ip" value="${ip }">								
									<input class="btn  primary" type="button" value="查询" id="button"><span style="margin-left:50px">IP为空则查询全网的历史数据</span>
								</form>
							</div>
			</div>
		</div>
		<div class="row-fluid">
		<c:if test="${flag=='yes'}">
		<c:if test="${ipflag=='no'}">
			<div class="span12">
				<table width="100%">
					<caption>全网历史</caption>
										<tr>
											<td width="100%" id="pvchartDivId" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
												var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
												so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
												so4.addVariable("chart_id", "amline4");  
												so4.addVariable("reload_data_interval", "60"); 
												so4.addVariable("wmode", "transparent");
												so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
												so4.addVariable("data_file", escape("<%=request.getContextPath()%>/data/show.do?method=historyData&appName=${appName}&keyId=${keyId}&propertyName=${propertyName}&time=${time}"));
												so4.write("pvchartDivId");
												</script>
											</td>
										</tr>
									</table>	
			</div>
			</c:if>
			<c:if test="${ipflag=='yes'}">
			<div class="span12">
				<table width="100%">
							<caption>告警主机历史数据</caption>
										<tr>
											<td width="100%" id="pvchartDivId2" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
													var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
													so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
													so4.addVariable("chart_id", "amline4");  
													so4.addVariable("reload_data_interval", "60"); 
													so4.addVariable("wmode", "transparent");
													so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
													so4.addVariable("data_file", escape("<%=request.getContextPath()%>/data/show.do?method=historyDataHost&appName=${appName}&keyId=${keyId}&propertyName=${propertyName}&time=${time}&ip=${ip}"));
													so4.write("pvchartDivId2");
												</script>
											</td>
										</tr>
									</table>					
				</div>
			</c:if>
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
			/**表单验证 */
			if ( $.trim(f.val()) == '') {
				//f.focus();
				alert("请输入日期!");
				return;
			}
			if ( $.trim(a.val()) == '') {
				//f.focus();
				alert("请输入应用!");
				return;
			}
			if ( $.trim(k.val()) == '') {
				//f.focus();
				alert("请输入KEY!");
				return;
			}
			if ( $.trim(p.val()) == '') {
				//f.focus();
				alert("请输入属性!");
				return;
			}
			$("#form").submit();

		});
	});
</script>
</html>