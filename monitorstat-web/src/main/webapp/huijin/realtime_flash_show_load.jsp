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
	String collectTime1 = request.getParameter("collectTime1");
	if(collectTime1 != null){
		current = sdf.parse(collectTime1);
	}else{
		collectTime1 = sdf.format(current);
	}
	
	//Date collectTime1Date = parseLogFormatDate.parse("2010-04-30 00:00:00") ; 
	//String collectTime1 = sdf.format(collectTime1Date) ; 
	String startDate = sdf.format(current)+" 00:00:00";
	String endDate = sdf.format(current)+" 23:59:59";
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(current);
	cal.add(Calendar.DAY_OF_MONTH,-7);
	
	String startDate2 = sdf.format(cal.getTime())+" 00:00:00";
	String endDate2 = sdf.format(cal.getTime())+" 23:59:59";
	
	String appName = request.getParameter("appName");
	String appIdStr = request.getParameter("appId");
	int appId = 0;
	if(appIdStr != null){
		appId = Integer.parseInt(appIdStr);
	}else{
		appId = AppCache.get().getKey(appName).getAppId();
	}
	if(appName==null){
		appName = "list";
	}
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
	
	//当前
	List<KeyValuePo> currentList = MonitorTimeAo.get().findKeyValueByRangeDate(appId,944,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
	//以前
	//List<KeyValuePo> beforeList = MonitorTimeAo.get().findKeyValueByRangeDate(appId,944,parseLogFormatDate.parse(startDate2),parseLogFormatDate.parse(endDate2));
	List<KeyValuePo> beforeList = new ArrayList<KeyValuePo>();
	Map<String,KeyValuePo> beforeMap = CacheTimeData.get().getAppKeyData(appName,"System_LOAD_AVERAGEUSERTIMES");
	if(beforeMap!=null)
		beforeList.addAll(beforeMap.values());
	
	Map<String, List<KeyValuePo>> mapLoad = new HashMap<String, List<KeyValuePo>>();
	List<KeyValuePo> baseListload = MonitorBaseLineAo.get().findKeyBaseValue(appId,944);
	mapLoad.put("基线",baseListload);
	
	if(currentList!=null){
		mapLoad.put("LOAD["+sdf.format(current)+"]",currentList);
	}
	if(beforeList!=null){
		mapLoad.put("LOAD["+sdf.format(cal.getTime())+"]",beforeList);
	}
	String loadReslut = AmLineFlash.createCharXml(mapLoad);	
	
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >机器性能</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
		
		<td width="20%" id="gcchartdiv" valign="top">		
		<% 
		
		SimpleDateFormat sdfGc1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfGc2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String statrt = sdfGc1.format(current)+" 00:00:00";		
		String _statrt = sdfGc1.format(cal.getTime())+" 00:00:00";
		
		long curFullNum = MonitorTimeAo.get().countKeyByDate(167,appId,sdfGc2.parse(statrt),current);
		//long _FullNum = MonitorTimeAo.get().countKeyByDate("SELF_GC_Full_AVERAGEMACHINEFLAG",appName,sdfGc2.parse(_statrt),cal.getTime());
		long _FullNum = CacheTimeData.get().getAppKeyCount(appName,"SELF_GC_Full_AVERAGEMACHINEFLAG",sdfGc2.parse(_statrt),cal.getTime());
		
		long curgcNum = MonitorTimeAo.get().countKeyByDate(3196,appId,sdfGc2.parse(statrt),current);
		//long _gcNum = MonitorTimeAo.get().countKeyByDate("SELF_GC_GC_AVERAGEMACHINEFLAG",appName,sdfGc2.parse(_statrt),cal.getTime());
		long _gcNum = CacheTimeData.get().getAppKeyCount(appName,"SELF_GC_GC_AVERAGEMACHINEFLAG",sdfGc2.parse(_statrt),cal.getTime());
		%>
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon ">
			<td colspan="5" align="left"><font color="#000000" size="2">GC</font></td>
			</tr>
			<tr class="ui-widget-header ">
			<td>现在总Full GC数</td>
			<td colspan="2">历史现在总Full GC数</td>
			</tr>
			<tr>
			<td align="center"><a target="_blank" href="<%=request.getContextPath () %>/time/key_detail_time_table.jsp?appName=<%=appName%>&keyId=167&aimName=FullGC&collectTime1=<%=collectTime1%>"><img src="<%=request.getContextPath () %>/statics/images/report.png"/></a><%=curFullNum %></td>
			<td align="center"><%=_FullNum %></td>
			<td align="center"><%=Utlitites.scale(curFullNum+"",_FullNum+"")%></td>
			</tr>
			<tr class="ui-widget-header ">
			<td>现在总GC数</td>
			<td colspan="2">历史现在总GC数</td>
			</tr>
			<tr>
			<td align="center"><a target="_blank" href="<%=request.getContextPath () %>/time/key_detail_time.jsp?appName=<%=appName%>&keyId=3196&aimName=GC&collectTime1=<%=collectTime1%>"><img src="<%=request.getContextPath () %>/statics/images/report.png"/></a><%=curgcNum %></td>
			<td align="center"><%=_gcNum %></td>
			<td align="center"><%=Utlitites.scale(curgcNum+"",_gcNum+"")%></td>
			</tr>
		</table>
		
		</td>
		
		<td width="20%" valign="top">
			<%
			List<KeyValueVo> memroyVoList = MonitorTimeAo.get().findLikeKeyByLimit("System_CPU_AVERAGEUSERTIMES",appName);
			if(memroyVoList!=null&&memroyVoList.size()>0){
		%>
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon ">
			<td colspan="2" align="left"><font color="#000000" size="2">CPU</font>&nbsp;&nbsp;<a title="具体每台CPU情况" target="_blank" href="./key_detail_time.jsp?keyId=3113&appName=<%=appName%>&aimName=Cpu">详细</a></td>
			</tr>
			<tr class="ui-widget-header ">
				<td width="40" align="center">时间</td>
				<td width="60"  align="center">使用率</td>	
			</tr>			
			<%			
			
			for(KeyValueVo vo:memroyVoList){ 
				KeyValuePo po=vo.getMap().get("System_CPU_AVERAGEUSERTIMES");
				
			%>
			<tr >
				<td width="40"><%=po.getCollectTimeStr() %></td>
				<td width="60"><%=po.getValueStr() %>%</td>
			</tr>
			<%} %>			
		</table>
		
		<%} %>		
		</td>		
		<td width="60%"  valign="top">
			<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon ">
			<td ><font color="#000000" size="2">Load</font>&nbsp;&nbsp;<a title="具体每台Load情况" target="_blank" href="./key_detail_time.jsp?keyId=944&appName=<%=appName%>&aimName=Load">详细</a></td>
			</tr>
			<tr >
			<td id="loadchartdiv"></td>
			</tr>
			</table>
		</td >				
	</tr>	
</table>

	
<script type="text/javascript">
var so3 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_load", "100%", "250", "6", "#FFFFFF");
so3.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so3.addVariable("chart_id", "amline3");   
so3.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so3.addVariable("chart_data", encodeURIComponent("<%=loadReslut%>"));
so3.write("loadchartdiv");
</script>
</div>
</div>

<%
	String currentHost = request.getParameter("currentHost");
%>
<iframe id="iframeC" name="iframeC" src="" width="0" height="0" style="display:none;" ></iframe>
<input id = "currentHost" type="hidden" value="<%=currentHost%>"/>
<script type="text/javascript"> 
function sethash(){
    hashH = document.documentElement.scrollHeight;
	urlC = document.getElementById("currentHost").value;
    document.getElementById("iframeC").src=urlC+"#"+hashH; 
}
window.onload=sethash;
</script>

</body>

</html>