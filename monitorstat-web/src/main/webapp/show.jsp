<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.cache.CacheTimeData"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();
	
	String appIdStr = request.getParameter("appId");
	String keyIdStr = request.getParameter("keyId");
	String collectTime = request.getParameter("collectTime");
	String duibi = request.getParameter("duibi");
	if(collectTime!=null){
		cal.setTime(sdf.parse(collectTime));
	}
	
	
	
	Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(Integer.parseInt(appIdStr),Integer.parseInt(keyIdStr), cal.getTime());
	if(currentMap.size() == 0  || currentMap == null) {
		currentMap = MonitorTimeAo.get().findKeyValueInLimit(Integer.parseInt(appIdStr),Integer.parseInt(keyIdStr));
	}
	Map<String, KeyValueBaseLinePo> baseLineMap = MonitorBaseLineAo.get().findKeyBaseValueByDate(Integer.parseInt(appIdStr), Integer.parseInt(keyIdStr));
	
	
	Map<String,Double> currentValue = new HashMap<String,Double>();
	String keyName = null;
	String appName = request.getParameter("appName");
	
	if(appName == null){
		appName = AppCache.get().getKey(Integer.parseInt(appIdStr)).getAppName();
	}
	
	for(Map.Entry<String, KeyValuePo> entry:currentMap.entrySet()){
		String key = entry.getKey();
		KeyValuePo po = entry.getValue();
		keyName = po.getKeyName();
		currentValue.put(key,Double.parseDouble(po.getValueStr()));
	}
	Map<String,Double> baseValue = new HashMap<String,Double>();
	for(Map.Entry<String, KeyValueBaseLinePo> entry:baseLineMap.entrySet()){
		String key = entry.getKey();
		KeyValueBaseLinePo po = entry.getValue();
				
		baseValue.put(key,po.getBaseLineValue());
	}
	
	
	Map<String,Map<String,Double>> map = new HashMap<String,Map<String,Double>>();
	map.put(keyName,currentValue);
	map.put("基线",baseValue);
	
	
	if(duibi== null){
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DAY_OF_MONTH,-7);
		duibi=sdf.format(cal1.getTime());
	}
	if(duibi!=null){
		Map<String, KeyValuePo> duibiMap = MonitorTimeAo.get().findKeyValueByDate( Integer.parseInt(appIdStr),Integer.parseInt(keyIdStr), sdf.parse(duibi));
		Map<String,Double> duibiValue = new HashMap<String,Double>();
		for(Map.Entry<String, KeyValuePo> entry:duibiMap.entrySet()){
			String key = entry.getKey();
			KeyValuePo po = entry.getValue();			
			keyName = po.getKeyName();
			duibiValue.put(key,Double.parseDouble(po.getValueStr()));
		}
		map.put(duibi,duibiValue);
	}
	
	
	

%>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<%
	
	String loadReslut = AmLineFlash.createCommonCharXml(map);	
	
%>
<form action="show.jsp" method="get">
<table>
	<tr>
		<td>
		<input type="hidden" name="appId" value="<%=appIdStr %>"/>
		<input type="hidden" name="keyId" value="<%=keyIdStr %>"/>
		<input type="hidden" name="action" value="<%=keyIdStr %>"/>
		</td>
	</tr>
	<tr>
		<td>时间:<input type="text" name="duibi"  value="<%=duibi%>" class="Wdate" onfocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" /><input type="submit" value="对比"/></td>
	</tr>
</table>
</form>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=appName %>--<%=keyName %></font>&nbsp;&nbsp; <a href="<%=request.getContextPath() %>/time/key_detail_time.jsp?keyId=<%=keyIdStr %>&appId=<%=appIdStr %>&appName=<%=appName %>" target="_blank">查看每台情况</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		<td width="100%"  valign="top">
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
				<tr >
					<td id="loadchartdiv"></td>
				</tr>
			</table>
		</td >				
	</tr>	
</table>

	
<script type="text/javascript">
var so3 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "400", "6", "#FFFFFF");
so3.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so3.addVariable("chart_id", "amline3");   
so3.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so3.addVariable("chart_data", encodeURIComponent("<%=loadReslut%>"));
so3.write("loadchartdiv");
</script>
</div>
</div>

<jsp:include page="buttom.jsp"></jsp:include>
</body>

</html>