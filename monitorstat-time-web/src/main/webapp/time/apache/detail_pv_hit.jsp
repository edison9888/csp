<%@page import="com.taobao.csp.time.util.AmlineFlash"%>
<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>
<title>ÊµÊ±¼à¿Ø</title>
</head>
<script>
	$(function() {
		$( "#time1" ).datepicker();
		$( "#time1" ).datepicker("option", "dateFormat", "yy-mm-dd");
		$( "#time1" ).datepicker( "setDate" , "${time1 }");
		$( "#time2" ).datepicker();
		$( "#time2" ).datepicker("option", "dateFormat", "yy-mm-dd");
		$( "#time2" ).datepicker( "setDate" , "${time2 }");
	});
	</script>
<body>

<form action="<%=request.getContextPath()%>/app/detail/apache/show.do"  method="get">
<input  type="hidden"  value="${appName }"  name="appName">
<input  type="hidden"  value="hit"  name="method">
<table  width="100%" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<td align="center">
			µ±Ç°Ê±¼ä:<input id="time1" type="text"  value="${time1 }"  name="time1">
			¶Ô±ÈÊ±¼ä: <input  id="time2"  type="text"  value="${time2 }"  name="time2">
			<input type="submit"  value="²é¿´¶Ô±È">
			</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td width="100%"  id="hit_am"  style="height:250px" colspan="2">
					<script type="text/javascript">
						 var params = 
				            {
				                bgcolor:"#FFFFFF",
				                wmode:"transparent"
				            };
						
						var flashVars = 
			            {
			                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
			                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
			                chart_data: encodeURIComponent("${am}")
			            };
						
						swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
								"hit_am", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
								flashVars, params);
						
						</script>
					</td>							
				</tr>
			</table>
			</td>
		</tr>
	</tbody>
	
</table>
</form>



</body>
</html>