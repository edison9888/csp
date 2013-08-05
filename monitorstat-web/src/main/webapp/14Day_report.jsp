<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.util.AmLineFlash"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.web.cache.CacheKeyValue"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<title>Insert title here</title>
</head>
<body>
<%
int[] appArray = new int[]{1,2,3,5,6};
String id = request.getParameter("id");
String offset = request.getParameter("offset");
if(offset == null){
	offset = "14";
}
int ofs = Integer.parseInt(offset);
int dayPvKeyId = 982;
int dayAppId = Integer.parseInt(id);

int timeAppId = Integer.parseInt(id);
int timePvKeyId = 175;
int timeLoadKeyId = 944;
int timeCpuKeyId = 3113;
String timeAppName = AppCache.get().getKey(timeAppId).getAppName();


SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
Calendar calDay = Calendar.getInstance();
calDay.add(Calendar.DAY_OF_MONTH,-1);
Date  endDay= calDay.getTime();
calDay.add(Calendar.DAY_OF_MONTH,-ofs);
Date  startDay= calDay.getTime();
List<KeyValuePo> listdayPv = MonitorDayAo.get().findMonitorCountByDate(dayAppId,dayPvKeyId,startDay,endDay);
Map<String,Map<String, Double>> map = new HashMap<String,Map<String, Double>>();
Map<String, Double> dayPv = new HashMap<String, Double>();
for(KeyValuePo po:listdayPv){
	dayPv.put(sdf.format(po.getCollectTime()),Arith.div(Double.parseDouble(po.getValueStr()),10000));
}
map.put("pv流量(万)",dayPv);
String dayPvFlashStr = AmLineFlash.createCommonCharXml1(map);


SimpleDateFormat sdf1= new SimpleDateFormat("yyyyMMdd HH:mm:ss");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

Calendar cal = Calendar.getInstance();
Map<String,Double> pvMap = new HashMap<String,Double>();
Map<String,Double> loadMap = new HashMap<String,Double>();
Map<String,Double> cpuMap = new HashMap<String,Double>();
for(int i=0;i<ofs;i++){
	cal.add(Calendar.DAY_OF_MONTH,-1);
	String start = "20:30:00";
	String end = "22:30:00";
	
	Date startDate = sdf1.parse(sdf2.format(cal.getTime())+" "+start);
	Date endDate = sdf1.parse(sdf2.format(cal.getTime())+" "+end);
	
	double pv = CacheKeyValue.get().getKeyAverageCahceValue( timeAppId, timePvKeyId, startDate,endDate);
	double load = CacheKeyValue.get().getKeyAverageCahceValue( timeAppId, timeLoadKeyId, startDate,endDate);
	double cpu = CacheKeyValue.get().getKeyAverageCahceValue( timeAppId, timeCpuKeyId, startDate,endDate);
	
	pvMap.put(sdf2.format(cal.getTime()),Arith.div(pv,60,2));
	loadMap.put(sdf2.format(cal.getTime()),load);
	cpuMap.put(sdf2.format(cal.getTime()),cpu);
}

Map<String,Map<String, Double>> pvmap1 = new HashMap<String,Map<String, Double>>();
pvmap1.put("平均QPS",pvMap);
Map<String,Map<String, Double>> loadmap1 = new HashMap<String,Map<String, Double>>();
loadmap1.put("平均LOAD",loadMap);
Map<String,Map<String, Double>> cpumap1 = new HashMap<String,Map<String, Double>>();
cpumap1.put("平均CPU",cpuMap);


String dayQPSFlashStr = AmLineFlash.createCommonCharXml1(pvmap1);
String dayLoadFlashStr = AmLineFlash.createCommonCharXml1(loadmap1);
String dayCpuFlashStr = AmLineFlash.createCommonCharXml1(cpumap1);
%>
<form action="./14Day_report.jsp" method="get">
<table>
	<tr>
		<td><select name="id">
			<option value="1">item</option>
			<option value="2">list</option>
			<option value="3">shopsystem</option>
			<option value="5">buy</option>
			<option value="6">trademgr</option>
		</select> <input type="submit" value="查看"/></td>
	</tr>
	<tr>
		<td>当前应用:<%=timeAppName %></td>
	</tr>
</table>
</form>

<table>
	<tr>
		<td width="50%">
			<div id="chartdiv_pv" align="center"></div>		
		</td>
		<td width="50%">
			<div id="chartdiv_qps" align="center"></div>		
		</td>
	</tr>
	<tr>
		<td width="50%">
			<div id="chartdiv_cpu" align="center"></div>		
		</td>
		<td width="50%">
			<div id="chartdiv_load" align="center"></div>		
		</td>
	</tr>
</table>

<script type="text/javascript">
var so_pv = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_pv", "600", "400", "8", "#FFFFFF");
so_pv.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so_pv.addVariable("chart_id", "amline");   
so_pv.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so_pv.addVariable("chart_data", encodeURIComponent("<%=dayPvFlashStr%>"));
so_pv.write("chartdiv_pv");

var so_pqs = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_qps", "600", "400", "8", "#FFFFFF");
so_pqs.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so_pqs.addVariable("chart_id", "amline");   
so_pqs.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so_pqs.addVariable("chart_data", encodeURIComponent("<%=dayQPSFlashStr%>"));
so_pqs.write("chartdiv_qps");		


var so_cpu = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_cpu", "600", "400", "8", "#FFFFFF");
so_cpu.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so_cpu.addVariable("chart_id", "amline");   
so_cpu.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so_cpu.addVariable("chart_data", encodeURIComponent("<%=dayLoadFlashStr%>"));
so_cpu.write("chartdiv_cpu");		


var so_load = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "600", "400", "8", "#FFFFFF");
so_load.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so_load.addVariable("chart_id", "amline");   
so_load.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so_load.addVariable("chart_data", encodeURIComponent("<%=dayCpuFlashStr%>"));
so_load.write("chartdiv_load");		

</script>

</body>
</html>