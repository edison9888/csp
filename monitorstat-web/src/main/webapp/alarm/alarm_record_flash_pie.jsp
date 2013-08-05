<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<style type="text/css">
div {
	font-size: 12px;
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

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
String appId = request.getParameter("appId");
String currentDay = request.getParameter("currentDay");
if(currentDay==null){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH, -1);
	currentDay = sdf.format(cal.getTime());
}



if(appId!=null&&currentDay!=null){
	List<AlarmRecordPo> keyvalueList = MonitorAlarmAo.get().getAppAlarmByDate(Integer.parseInt(appId),sdf.parse(currentDay));
	Map<String,Integer> allAlarmMap = new HashMap<String,Integer>();//所有告警分布
	Map<String,Integer> allAlarmTimeMap = new HashMap<String,Integer>();//一天24个小时分布
	
	for(AlarmRecordPo po:keyvalueList){
		String name = po.getAlarmKeyName();
		String time = sdf1.format(po.getCollectTime())+"点";
		
		Integer all = allAlarmMap.get(name);
		if(all == null){
			allAlarmMap.put(name,1);
		}else{
			allAlarmMap.put(name,1+all);
		}
		
		Integer hh = allAlarmTimeMap.get(time);
		if(hh == null){
			allAlarmTimeMap.put(time,1);
		}else{
			allAlarmTimeMap.put(time,1+hh);
		}		
	}
	
	
	
	String allAlarm = AmLineFlash.createPieXml(allAlarmMap);
	String allAlarmTime = AmLineFlash.createPieXml(allAlarmTimeMap);

%>

</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<form action="alarm_record_flash_pie.jsp">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<input type="hidden" value="<%=appId %>" name="appId">
<table width="100%">
<tr>
	<td width="100%" align="center">
		日期:<input type="text" name="currentDay" value="<%=currentDay %>"/> <input type="submit" value="查看">
	</td>
</tr>
</table>

</div>

<table>
	<tr>
		<td width="100%"><div id="chartdiv1" align="center"></div></td>
	</tr>
	<tr>
		<td width="100%"><div id="chartdiv2" align="center"></div></td>
	</tr>
</table>

</form>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "800", "300", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "ampie");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/pie_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=allAlarm%>"));
so.write("chartdiv1");;	


var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "800", "300", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/pie_settings2.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=allAlarmTime%>"));
so1.write("chartdiv2");		

</script>
<%}else{ %>
参数不正确！
<%} %>
</html>