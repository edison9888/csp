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
<%
Map<String,AmlineFlash> map = (Map<String,AmlineFlash>)request.getAttribute("flashMap");
String selectProperty = (String)request.getAttribute("selectProperty");
List<String> propertys = (List<String>)request.getAttribute("propertys");
if(propertys == null) {
	propertys = new ArrayList<String>();
}

String comparetype = (String)request.getAttribute("comparetype");


%>
<table  width="100%" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<td align="center">
			<div id="timeDiv">
			<form action="<%=request.getContextPath()%>/app/detail/history.do"  method="get"
			style="well form-inline;" id="timeForm">
				»ù´¡Ê±¼ä:<input id="time1" type="text"  value="${time1}"  name="time1">
				¶Ô±ÈÊ±¼ä:<input  id="time2"  type="text"  value="${time2}"  name="time2">			
					ÏÔÊ¾ÊôÐÔ:
					<select id="selectProperty" name="selectProperty">
						<%
							if(selectProperty == null || selectProperty.equals("allfield")) {
								out.println("<option value='allfield' selected='selected'>È«²¿ÊôÐÔ</option>");
							} else {
								out.println("<option value='allfield'>È«²¿ÊôÐÔ</option>");
							}
							for(String opt: propertys) {
								String selected = "";
								if(selectProperty != null && selectProperty.equals(opt)) {
									selected = " selected='selected'";
								}
								out.println("<option value='" + opt + "'" + selected + ">" + opt + "</option>");
							}
						%>
						</select>					
					<input  type="hidden"  value="${appName }"  name="appName">
					<input  type="hidden"  value="${keyName }"  name="keyName">
					<input  type="hidden"  value="${method }"  name="method">					
					<input type="text" name="ip" id="ip" value=${ip} title="¶ººÅ·Ö¸îIP£¬°´Ê±¼ä¶Ô±ÈÊ±Ö»¶ÔµÚÒ»¸öIPÓÐÐ§" style="display: none;">
					<input name="comparetype" value="time" style="display: none;">
			</form>
			</div>
				<div id="ipDiv">
				<form action="<%=request.getContextPath()%>/app/detail/history.do"  method="get"
				style="well form-inline;" id="ipForm">
					»ù´¡Ê±¼ä:<input id="time1" type="text"  value="${time1}"  name="time1">
						ÏÔÊ¾ÊôÐÔ:
						<select id="selectProperty" name="selectProperty">
							<%
								if(selectProperty == null || selectProperty.equals("allfield")) {
									out.println("<option value='allfield' selected='selected'>È«²¿ÊôÐÔ</option>");
								} else {
									out.println("<option value='allfield'>È«²¿ÊôÐÔ</option>");
								}
								for(String opt: propertys) {
									String selected = "";
									if(selectProperty != null && selectProperty.equals(opt)) {
										selected = " selected='selected'";
									}
									out.println("<option value='" + opt + "'" + selected + ">" + opt + "</option>");
								}
							%>
							</select>					
						<input  type="hidden"  value="${appName}"  name="appName">
						<input  type="hidden"  value="${keyName}"  name="keyName">
						<input  type="hidden"  value="${method}"  name="method">					
						IP:<input type="text" name="ip" id="ip" value=${ip} title="¶ººÅ·Ö¸îIP">
						<input name="comparetype" value="ip" style="display: none;">
				</form>
			</div>		
						<input type="radio" value="time" name="comparetype" id="radioTime" 
					<%
						if(comparetype == null || comparetype.equals("time"))
							out.println(" checked='checked'");
					%>
					>°´Ê±¼ä¶Ô±È
					<input type="radio" value="ip" name="comparetype" id="radioIp"
					<%
						if(comparetype == null || comparetype.equals("ip"))
							out.println(" checked='checked'");
					%>
					>°´IP¶Ô±È
					<input type="button"  value="²é¿´¶Ô±È" id="btnSubmit">			
			</td>
		</tr>
	</thead>
	<tbody>
		<%
		String path = request.getContextPath();
		int i=0;
			for(Map.Entry<String,AmlineFlash> entry:map.entrySet()){
				i++;
		%>
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td width="100%"  id="histo_<%=entry.getKey() %>"  style="height:250px" colspan="2">
					<script type="text/javascript">
						 var params = 
				            {
				                bgcolor:"#FFFFFF",
				                wmode:"transparent"
				            };
						
						var flashVars = 
			            {
			                path: "<%=path%>/statics/js/amcharts/flash/",
			                settings_file: "<%=path%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
			                chart_data: encodeURIComponent("<%=entry.getValue().getAmline()%>")
			            };
						
						swfobject.embedSWF("<%=path%>/statics/js/amcharts/flash/amline.swf", 
								"histo_<%=entry.getKey() %>", "100%", "350", "8.0.0", "<%=path%>/statics/js/amcharts/flash/expressInstall.swf",
								flashVars, params);
						
						</script>
					</td>							
				</tr>
			</table>
			</td>
		</tr>
		<%} %>
	</tbody>
	
</table>
<script type="text/javascript">
$(function() {
	$("#radioTime").click(function() {
		$("#ipDiv").hide();
		$("#timeDiv").show();
		return true;
	});
	$("#radioIp").click(function() {
		$("#ipDiv").show();
		$("#timeDiv").hide();
		return true;
	});	
	
	$("#btnSubmit").click(function() {
		
		if($("#radioTime")[0].checked) {
			$("#timeForm").submit();
			return true;
		}
		
		if($("#radioIp")[0].checked) {
			$("#ipForm").submit();
			return true;
		}		
		return false;
	});	
	
	<%
	if(comparetype == null || comparetype.equals("time")) {
		%>
		$("#ipDiv").hide();
		$("#timeDiv").show();
		<%
	}
	%>
	
	<%
	if(comparetype.equals("ip")) {
		%>
		$("#ipDiv").show();
		$("#timeDiv").hide();
		<%
	}
	%>	
});
</script>


</body>
</html>