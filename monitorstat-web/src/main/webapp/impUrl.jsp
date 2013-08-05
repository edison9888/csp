<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.cache.CacheTimeData"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@ page import="com.taobao.monitor.common.util.Arith" %>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.web.po.MonitorStatisticsDataPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
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
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>

<%
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		Map<String, Map<String, Double>> mapBuyNow = new LinkedHashMap<String, Map<String,Double>>();
		Map<String, Map<String, Double>> mapConOrder = new LinkedHashMap<String, Map<String,Double>>();
		Map<String, Map<String, Double>> mapAddCart = new LinkedHashMap<String, Map<String,Double>>();
		Map<String, Map<String, Double>> mapMyCart = new LinkedHashMap<String, Map<String,Double>>();
		Map<String, Map<String, Double>> mapUnionOrder = new LinkedHashMap<String, Map<String,Double>>();
		
		String [] urlFlag = { "330:60968", "330:60873", "341:65398", "341:65401", "330:62185" };
		String splitFlag = ":";
		long nowTimeMillis = Calendar.getInstance().getTimeInMillis();
		String BUY = "立即购买:";
		String ORDER = "订单确认:";
		String CART = "加入购物车:";
		String MYCART = "我的购物车:";
		String UNIONORDER = "统一订单确认:";
		
		Map<Integer, Integer> siteCount = new HashMap<Integer, Integer>();
		siteCount.put(60968, 0);
		siteCount.put(60873, 0);
		siteCount.put(65398, 0);
		siteCount.put(65401, 0);
		siteCount.put(62185, 0);
		
		for (String url : urlFlag) {
			Calendar calendar = Calendar.getInstance();
			int appId = Integer.valueOf(url.split(splitFlag)[0]);
			int keyId = Integer.valueOf(url.split(splitFlag)[1]);
			
			
			// 今天的数据
			calendar.setTime(new Date(nowTimeMillis));
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date start_1 = calendar.getTime();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date end_1 = calendar.getTime();
			List<MonitorStatisticsDataPo> totol_list_1 = MonitorStatisticsDataAo.get().findStatisticsData(appId, keyId, start_1, end_1);
			Map<String, Double> total_1 = new HashMap<String, Double>();
			for (MonitorStatisticsDataPo po : totol_list_1) {
				String time = timeFormat.format(po.getCollectTime());
				int siteCounts = po.getSiteCount() == 0 ? 1 : po.getSiteCount();
				int existSiteCount = siteCount.get(keyId);
				if (siteCounts > existSiteCount) {
					siteCount.put(keyId, siteCounts);
				}
				double averageValue = po.getTotalData() / siteCounts;
				total_1.put(time, averageValue);
			}
			switch (keyId) {
				case 60968: mapBuyNow.put(BUY + format.format(start_1), total_1); break;
				case 60873: mapConOrder.put(ORDER + format.format(start_1), total_1); break;
				case 65398: mapAddCart.put(CART + format.format(start_1), total_1); break;
				case 65401: mapMyCart.put(MYCART + format.format(start_1), total_1); break;
				case 62185: mapUnionOrder.put(UNIONORDER + format.format(start_1), total_1); break;
				default: break;
			}
			
			// 昨天的数据
			calendar.setTime(new Date(nowTimeMillis));
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date start_2 = calendar.getTime();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date end_2 = calendar.getTime();
			List<MonitorStatisticsDataPo> totol_list_2 = MonitorStatisticsDataAo.get().findStatisticsData(appId, keyId, start_2, end_2);
			Map<String, Double> total_2 = new HashMap<String, Double>();
			for (MonitorStatisticsDataPo po : totol_list_2) {
				String time = timeFormat.format(po.getCollectTime());
				int siteCounts = po.getSiteCount() == 0 ? 1 : po.getSiteCount();
				double averageValue = po.getTotalData() / siteCounts;
				total_2.put(time, averageValue);
			}
			switch (keyId) {
			 	case 60968: mapBuyNow.put(BUY + format.format(start_2), total_2); break;
			 	case 60873: mapConOrder.put(ORDER + format.format(start_2), total_2); break;
			 	case 65398: mapAddCart.put(CART + format.format(start_2), total_2); break;
			 	case 65401: mapMyCart.put(MYCART + format.format(start_2), total_2); break;
				case 62185: mapUnionOrder.put(UNIONORDER + format.format(start_2), total_2); break;
			 	default: break;
			}
			
			// 一个星期前的数据
			calendar.setTime(new Date(nowTimeMillis));
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date start_3 = calendar.getTime();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date end_3 = calendar.getTime();
			List<MonitorStatisticsDataPo> totol_list_3 = MonitorStatisticsDataAo.get().findStatisticsData(appId, keyId, start_3, end_3);
			Map<String, Double> total_3 = new HashMap<String, Double>();
			for (MonitorStatisticsDataPo po : totol_list_3) {
				String time = timeFormat.format(po.getCollectTime());
				int siteCounts = po.getSiteCount() == 0 ? 1 : po.getSiteCount();
				double averageValue = po.getTotalData() / siteCounts;
				total_3.put(time, averageValue);
			}
			switch (keyId) {
		 		case 60968: mapBuyNow.put(BUY + format.format(start_3), total_3); break;
		 		case 60873: mapConOrder.put(ORDER + format.format(start_3), total_3); break;
		 		case 65398: mapAddCart.put(CART + format.format(start_3), total_3); break;
		 		case 65401: mapMyCart.put(MYCART + format.format(start_3), total_3); break;
				case 62185: mapUnionOrder.put(UNIONORDER + format.format(start_3), total_3); break;
		 		default: break;
			}	
		}

%>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<%
	
	String loadReslutBuyNow = AmLineFlash.createCommonCharXml1(mapBuyNow);	
	String loadReslutConOrder = AmLineFlash.createCommonCharXml1(mapConOrder);	
	String loadReslutAddCart = AmLineFlash.createCommonCharXml1(mapAddCart);	
	String loadReslutMyCart = AmLineFlash.createCommonCharXml1(mapMyCart);	
	String loadReslutUnionOrder = AmLineFlash.createCommonCharXml1(mapUnionOrder);	
	String info = "重要URL";
	
%>
<form action="" method="get">

</form>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=info %></font></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		<td width="100%"  valign="top">
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
				
				<tr >
					<td id="loadBuyNowdiv"></td>
				</tr>
			
				<tr >
					<td id="loadConOrderdiv"></td>
				</tr>
				<tr> <td align="center"><font color="blue"> </font></td></tr>
				
				<tr >
					<td id="loadAddCartdiv"></td>
				</tr>
				
				<tr >
					<td id="loadMyCartdiv"></td>
				</tr>
				
				<tr >
					<td id="loadUnionOrderdiv"></td>
				</tr>
			</table>
		</td >				
	</tr>
	<tr> <td align="center"><font color="blue">
		立即购买：<%=siteCount.get(60968)%>台服务器均值  &nbsp; &nbsp;
		订单确认：<%=siteCount.get(60873)%>台服务器均值&nbsp; &nbsp; 
		加入购物车：<%=siteCount.get(65398)%>台服务器均值&nbsp; &nbsp;
		我的购物车：<%=siteCount.get(65401)%>台服务器均值&nbsp; &nbsp;
		统一订单确认：<%=siteCount.get(62185)%>台服务器均值&nbsp; &nbsp;
		(新添加的url需要收集一段时间数据)&nbsp; &nbsp;
	</font></td></tr>	
</table>

	
<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline3");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=loadReslutBuyNow%>"));
so1.write("loadBuyNowdiv");

var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so2.addVariable("chart_id", "amline3");   
so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so2.addVariable("chart_data", encodeURIComponent("<%=loadReslutConOrder%>"));
so2.write("loadConOrderdiv");

var so3 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so3.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so3.addVariable("chart_id", "amline3");   
so3.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so3.addVariable("chart_data", encodeURIComponent("<%=loadReslutAddCart%>"));
so3.write("loadAddCartdiv");

var so4 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so4.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so4.addVariable("chart_id", "amline3");   
so4.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so4.addVariable("chart_data", encodeURIComponent("<%=loadReslutMyCart%>"));
so4.write("loadMyCartdiv");

var so5 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so5.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so5.addVariable("chart_id", "amline3");   
so5.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so5.addVariable("chart_data", encodeURIComponent("<%=loadReslutUnionOrder%>"));
so5.write("loadUnionOrderdiv");


</script>
</div>
</div>

<jsp:include page="buttom.jsp"></jsp:include>
</body>

</html>