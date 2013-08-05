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
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.cache.CacheTimeData"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
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
	SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date current = new Date();
	String collectTime1 = sdf.format(current); 
	String startDate = sdf.format(current)+" 00:00:00";
	String endDate = sdf.format(current)+" 23:59:59";
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(current);
	cal.add(Calendar.DAY_OF_MONTH,-7);
	
	String startDate2 = sdf.format(cal.getTime())+" 00:00:00";
	String endDate2 = sdf.format(cal.getTime())+" 23:59:59";
	
	String appId = request.getParameter("appId");
	
	if(appId == null){
		String opsName = request.getParameter("opsName");
		AppInfoPo appInfopo =AppInfoAo.get().getAppInfoByOpsName(opsName);
		appId = appInfopo.getAppId()+"";
	}
	
	AppInfoPo appInfopo = AppInfoAo.get().findAppInfoById(Integer.valueOf(appId));
	String appName = appInfopo.getAppName();
%>

<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
	function openTime(keyId){
		parent.openTime(keyId);	
	}
	
	
</script>
</head>
<body>
<%
if(appInfopo!=null&&appInfopo.getFeature()!=null&&appInfopo.getFeature().indexOf("pv")>-1){ 
	
	Map<String, List<KeyValuePo>> pvFlashMap = new HashMap<String, List<KeyValuePo>>();	
	Map<String, List<KeyValuePo>> resTFlashMap = new HashMap<String, List<KeyValuePo>>();
	
	//当前数据
	List<KeyValuePo> listCurrentPv = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),175,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
	List<KeyValuePo> resCurrentTPv = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),176,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
	
	//缓存上周的数据
	//List<KeyValuePo> pvCacheList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),175,parseLogFormatDate.parse(startDate2),parseLogFormatDate.parse(endDate2));
	//List<KeyValuePo> rtCacheList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),176,parseLogFormatDate.parse(startDate2),parseLogFormatDate.parse(endDate2));
	List<KeyValuePo> pvCacheList = new ArrayList<KeyValuePo>();
	List<KeyValuePo> rtCacheList = new ArrayList<KeyValuePo>();
	Map<String,KeyValuePo> pvmap = CacheTimeData.get().getAppKeyData(appName, "PV_VISIT_COUNTTIMES");
	Map<String,KeyValuePo> rtmap = CacheTimeData.get().getAppKeyData(appName, "PV_REST_AVERAGEUSERTIMES");
	if(pvmap != null)
		pvCacheList.addAll(pvmap.values());
	if(rtmap != null)
		rtCacheList.addAll(rtmap.values());
	//基线
	List<KeyValuePo> baseListpv = MonitorBaseLineAo.get().findKeyBaseValue(appInfopo.getAppId(),175);
	List<KeyValuePo> baseListrest = MonitorBaseLineAo.get().findKeyBaseValue(appInfopo.getAppId(),176);	
	
	for(KeyValuePo po:baseListrest){
		try{
			double ll = (Double.parseDouble(po.getValueStr())/1000);
			po.setValueStr((int)ll+"");			
		}catch(Exception e){
			
		}
	}
	
	//处理
	List<KeyValuePo> lastPv = new ArrayList<KeyValuePo>();
	List<KeyValuePo> lastRest= new ArrayList<KeyValuePo>();	
	
	
	Collections.sort(listCurrentPv);		
	for(int i=2;i<listCurrentPv.size()&&i<7;i++){
		lastPv.add(listCurrentPv.get(i));
	}
	
	for(int i=2;i<resCurrentTPv.size()&&i<7;i++){
		lastRest.add(resCurrentTPv.get(i));
	}
	
	
	
	pvFlashMap.put("流量["+sdf.format(current)+"]",listCurrentPv);
	pvFlashMap.put("流量["+sdf.format(cal.getTime())+"]",pvCacheList);
	pvFlashMap.put("基线",baseListpv);
	
	//
	for(KeyValuePo po:resCurrentTPv){
		try{
			double ll = (Double.parseDouble(po.getValueStr())/1000);
			po.setValueStr((int)ll+"");			
		}catch(Exception e){
			
		}
	}
	List<KeyValuePo> rtCacheListtmp = new ArrayList<KeyValuePo>();
	
	for(KeyValuePo po:rtCacheList){
		try{
			KeyValuePo potmp = new KeyValuePo();
			double ll = (Double.parseDouble(po.getValueStr())/1000);
			potmp.setValueStr((int)ll+"");	
			potmp.setCollectTime(po.getCollectTime());
			potmp.setCollectTimeStr(po.getCollectTimeStr());
			rtCacheListtmp.add(potmp);
		}catch(Exception e){
			
		}
	}
	
	resTFlashMap.put("ResT["+sdf.format(current)+"]",resCurrentTPv);
	resTFlashMap.put("ResT["+sdf.format(cal.getTime())+"]",rtCacheListtmp);	
	resTFlashMap.put("基线",baseListrest);
   	
	
	
	
	String pvreslut = AmLineFlash.createCharXml(pvFlashMap);
	String Restreslut = AmLineFlash.createCharXml(resTFlashMap);
		
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >访问性能数据</font>&nbsp;&nbsp; <a target="_blank" href="./app_relation_flash.jsp?keyId=175&appName=<%=appName%>">性能数据整合显示</a>&nbsp;&nbsp;<a target="_blank" href="./pv_url_detail.jsp?appId=<%=appId%>">展示各个url详细</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		<td width="50%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>时间</td>
			<%for(KeyValuePo po:lastPv){ %>
			<td><%=po.getCollectTimeStr() %></td>
			<%} %>						
			</tr>
			<tr >
			<td>流量</td>
			<%for(KeyValuePo po:lastPv){ %>
			<td><%=po.getValueStr() %></td>
			<%} %>			
			</tr>				
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr class="headcon ">
			<td ><font color="#000000" size="2">流量</font>&nbsp;&nbsp;<a title="具体每台流量" target="_blank" href="./key_detail_time.jsp?keyId=175&appName=<%=appName%>&aimName=pv">详细</a></td>
			</tr>
			<tr >
			<td id="pvchartdiv"></td>
			</tr>
			</table>
		</td>
		<td width="50%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>时间</td>
			<%for(KeyValuePo po:lastRest){ %>
			<td><%=po.getCollectTimeStr() %></td>
			<%} %>						
			</tr>
			<tr >
			<td>ResT</td>
			<%for(KeyValuePo po:lastRest){ %>
			<td><%=po.getValueStr() %></td>
			<%} %>			
			</tr>				
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon ">
			<td ><font color="#000000" size="2">ResT</font> <a title="具体每台Rest" target="_blank" href="./key_detail_time.jsp?keyId=176&appName=<%=appName%>&aimName=ResT">详细</a></td>
			</tr>
			<tr >
			<td id="Restchartdiv"></td>
			</tr>
			</table>
		</td>
	</tr>	
</table>

<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_pv", "100%", "250", "6", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline1");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=pvreslut%>"));
so1.write("pvchartdiv");

var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_rest", "100%", "250", "6", "#FFFFFF");
so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so2.addVariable("chart_id", "amline2");   
so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so2.addVariable("chart_data", encodeURIComponent("<%=Restreslut%>"));
so2.write("Restchartdiv");

</script>
</div>
</div>
<%}//这个 if 是用于 前端 %>


<%
if(appInfopo.getAppName().equals("ic")||appInfopo.getAppName().equals("shopcenter")||appInfopo.getAppName().equals("tbuic")||appInfopo.getAppName().equals("tc")){
	String c_pv_keyId = "";
	String c_rt_keyId = "";
	String className = "";
	
	if(appInfopo.getAppName().equals("ic")){
		className = "ItemQueryService:1.0.0-L0_queryItemById~lQ";
		c_pv_keyId = "30255";
		c_rt_keyId = "30256";
	}
	if(appInfopo.getAppName().equals("shopcenter")){
		className = "ShopReadService:1.0.0_queryShop";
		c_pv_keyId = "321";
		c_rt_keyId = "322";
	}
	if(appInfopo.getAppName().equals("tbuic")){
		className = "UserReadService:1.0.0_getUserAndUserExtraById";
		c_pv_keyId = "527";
		c_rt_keyId = "528";
	}
	if(appInfopo.getAppName().equals("tc")){
		className = "TcBaseService:1.0.0_queryMainAndDetail";
		c_pv_keyId = "6866";
		c_rt_keyId = "6867";
	}
	
	Map<String, List<KeyValuePo>> map = new HashMap<String, List<KeyValuePo>>();
	Map<String, List<KeyValuePo>> averMap = new HashMap<String, List<KeyValuePo>>();	
	
	
	List<KeyValuePo> currentKeyvalueList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(c_pv_keyId),parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
	List<KeyValuePo> perKeyvalueList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(c_pv_keyId),parseLogFormatDate.parse(startDate2),parseLogFormatDate.parse(endDate2));
	map.put(className+"["+sdf.format(current)+"]",currentKeyvalueList);
	map.put(className+"["+sdf.format(cal.getTime())+"]",perKeyvalueList);
	
	List<KeyValuePo> currentAverageList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(c_rt_keyId),parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
	List<KeyValuePo> perAverageList = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(c_rt_keyId),parseLogFormatDate.parse(startDate2),parseLogFormatDate.parse(endDate2));
	averMap.put(className+"["+sdf.format(current)+"]",currentAverageList);
	averMap.put(className+"["+sdf.format(cal.getTime())+"]",perAverageList);
	
	
	List<KeyValuePo> lastPv = new ArrayList<KeyValuePo>();
	List<KeyValuePo> lastRest= new ArrayList<KeyValuePo>();
	
	
	Collections.sort(currentKeyvalueList);		
	for(int i=2;i<currentKeyvalueList.size()&&i<7;i++){
		lastPv.add(currentKeyvalueList.get(i));
	}
	
	Collections.sort(currentAverageList);		
	for(int i=2;i<currentAverageList.size()&&i<7;i++){
		lastRest.add(currentAverageList.get(i));
	}
	
	
	String pvreslut = AmLineFlash.createCharXml(map);
	String Restreslut = AmLineFlash.createCharXml(averMap);
%>	
	
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >访问性能数据</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		<td width="50%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>时间</td>
			<%for(KeyValuePo po:lastPv){ %>
			<td><%=po.getCollectTimeStr() %></td>
			<%} %>						
			</tr>
			<tr >
			<td>流量</td>
			<%for(KeyValuePo po:lastPv){ %>
			<td><%=po.getValueStr() %></td>
			<%} %>			
			</tr>				
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr class="headcon ">
			<td ><font color="#000000" size="2">流量</font>&nbsp;&nbsp;<a title="具体每台流量" target="_blank" href="./key_detail_time.jsp?keyId=<%=c_pv_keyId %>&appName=<%=appName%>&aimName=pv">详细</a></td>
			</tr>
			<tr >
			<td id="C_pv_DEV"></td>
			</tr>
			</table>
		</td>
		<td width="50%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>时间</td>
			<%for(KeyValuePo po:lastRest){ %>
			<td><%=po.getCollectTimeStr() %></td>
			<%} %>						
			</tr>
			<tr >
			<td>ResT</td>
			<%for(KeyValuePo po:lastRest){ %>
			<td><%=po.getValueStr() %></td>
			<%} %>			
			</tr>				
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon ">
			<td ><font color="#000000" size="2">ResT</font> <a title="具体每台Rest" target="_blank" href="./key_detail_time.jsp?keyId=<%=c_rt_keyId %>&appName=<%=appName%>&aimName=ResT">详细</a></td>
			</tr>
			<tr >
			<td id="c_rt_div"></td>
			</tr>
			</table>
		</td>
	</tr>	
</table>
</div>
</div>
<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_pv11", "100%", "250", "6", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline111");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=pvreslut%>"));
so1.write("C_pv_DEV");

var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_rest11", "100%", "250", "6", "#FFFFFF");
so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so2.addVariable("chart_id", "amline211");   
so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so2.addVariable("chart_data", encodeURIComponent("<%=Restreslut%>"));
so2.write("c_rt_div");
</script>
<%	
}
%>
</body>

</html>