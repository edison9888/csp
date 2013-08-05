<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>

<%
//这个是一天为单位显示走势变化
String action = request.getParameter("action");

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

String collectTime1 = sdf.format(new Date());
String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";




SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String reslut = "";

String appName = request.getParameter("appName");
String keyId   = request.getParameter("keyId") ;
int appId = AppCache.get().getKey(appName).getAppId();

Map<String,List<KeyValuePo>> map = new HashMap<String,List<KeyValuePo>>();

//pv
List<KeyValuePo> listpv = MonitorTimeAo.get().findKeyValueByRangeDate(appId,175,  parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;
map.put("PV",listpv);
//rest
List<KeyValuePo> listResT = MonitorTimeAo.get().findKeyValueByRangeDate(appId,176,  parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;

for(KeyValuePo po:listResT){
	po.setValueStr((Double.parseDouble(po.getValueStr())/1000)+"");
}
map.put("ResT(ms)",listResT);
//load
List<KeyValuePo> listLoad = MonitorTimeAo.get().findKeyValueByRangeDate(appId,944,  parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;
for(KeyValuePo po:listLoad){
	po.setValueStr((Double.parseDouble(po.getValueStr())*1000)+"");
}

map.put("Load(*1000)",listLoad);

reslut = AmLineFlash.createCharXml(map) ; 

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript">


</script>
</head>
<body>
<div id="chartdiv" align="center">


</div>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "1000", "500", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");			

</script>

</html>