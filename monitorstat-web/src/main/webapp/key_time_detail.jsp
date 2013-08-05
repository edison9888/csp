<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<style type="text/css">
body {
	font-size: 62.5%;
}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
</style>
<%



String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
String collectTime = request.getParameter("collectTime");
String result = "";
String collectTime2 = request.getParameter("collectTime2");
if(collectTime2 == null) {
	
	collectTime2 = collectTime;
}

if(appId!=null && keyId != null &&collectTime!=null){

	if("c_pv".equals(keyId)) {
		List<KeyValuePo> keyvalueList1 = MonitorDayAo.get().findCappPvDetailByTime(Integer.parseInt(appId),collectTime);
		List<KeyValuePo> keyvalueList2 = MonitorDayAo.get().findCappPvDetailByTime(Integer.parseInt(appId),collectTime2);
		result = AmLineFlash.createCharXml(collectTime,keyvalueList1,collectTime2,keyvalueList2);
	}else if("c_rt".equals(keyId)) {
		List<KeyValuePo> keyvalueList1 = MonitorDayAo.get().findCappRtDetailByTime(Integer.parseInt(appId),collectTime);
		List<KeyValuePo> keyvalueList2 = MonitorDayAo.get().findCappRtDetailByTime(Integer.parseInt(appId),collectTime2);
		result = AmLineFlash.createCharXml(collectTime,keyvalueList1,collectTime2,keyvalueList2);
		
	} else {

		List<KeyValuePo> keyvalueList1 = MonitorDayAo.get().findMonitorDataListByTime(Integer.parseInt(appId),Integer.parseInt(keyId),collectTime);
		List<KeyValuePo> keyvalueList2 = MonitorDayAo.get().findMonitorDataListByTime(Integer.parseInt(appId),Integer.parseInt(keyId),collectTime2);
		result = AmLineFlash.createCharXml(collectTime,keyvalueList1,collectTime2,keyvalueList2);
	}


%>
<script type="text/javascript">


function goToAppDateDetail(){
	
	var collectTime2 = $('#datepicker').val();	
	location.href="key_time_detail.jsp?appId=<%=appId%>&keyId=<%=keyId%>&collectTime=<%=collectTime%>&collectTime2="+collectTime2;
}

</script>
</head>
<body>

<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
<tr>
	<td width="100%" align="center">
		<a href="#">查看以分钟统计</a>
		<a href="key_date_detail.jsp?appId=<%=appId%>&keyId=<%=keyId%>&endCollectTime=<%=collectTime %>">查看以天统计</a>
	</td>
</tr>
<tr>
	<td width="100%" align="center">
		日期: <input type="text" id="datepicker" value="<%=collectTime %>"/><button  onclick="goToAppDateDetail()">对比</button>
	</td>
</tr>
</table>

</div>
<div id="chartdiv" align="center">


</div>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=result%>"));
so.write("chartdiv");			

</script>
<%}else{ %>
参数不正确！
<%} %>
</html>