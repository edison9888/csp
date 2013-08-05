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
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
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
	String collectTime1 = sdf.format(current); 
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
		//parent.openTime(keyId);	
		if(keyId == null) {
			alert(keyId);
			keyId = "";
			}
		window.open("../time/key_detail_time.jsp?keyId="+keyId+"&appName=" + "<%=appName%>" + "&collectTime1=" + "<%=collectTime1%>");
	}
	
</script>
</head>
<body>

<%
List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
AppInfoPo appInfopo = null;
for(AppInfoPo appInfo:appList){
	String name = appInfo.getAppName();
	if(name.equals(appName)){
		appInfopo = appInfo;
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> = OUT = </font></div>
<div class="ui-dialog-content ui-widget-content">


<%{
List<KeyValueVo> listSearchEngine = MonitorTimeAo.get().findLikeKeyByLimit("OUT_SearchEngine",appName);
if(listSearchEngine!=null&&listSearchEngine.size()>0){
	
	List<KeyValueVo> list = new ArrayList<KeyValueVo>();
	list.add(listSearchEngine.get(0));
	Map<String,KeyValueVo> map = MonitorTimeAo.get().findTimeMonitorByTime("OUT_SearchEngine",appName,list);
	
//key OUT_SearchEngine_http://auction-search.config-vip.taobao.com:2088/bin/search?_auction_COUNTTIMES
Map<String,SearchEnginePo> searchEnginePoMap = new HashMap<String,SearchEnginePo>();
for(int i=0;i<1;i++){
	KeyValueVo searchVo = listSearchEngine.get(i);
	for(Map.Entry<String,KeyValuePo> entry:searchVo.getMap().entrySet()){		
		String key = entry.getKey();
		KeyValuePo po = entry.getValue();
		
		String[] _keys = key.split("_");
		
		String keyName = _keys[2]+"_"+_keys[3];
		
		SearchEnginePo searchEnginePo = searchEnginePoMap.get(keyName);
		if(searchEnginePo==null){
			searchEnginePo = new SearchEnginePo();
			searchEnginePoMap.put(keyName,searchEnginePo);
		}
		if(key.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
			searchEnginePo.setExeCount(po.getValueStr());
			searchEnginePo.setCountkeyId(po.getKeyId());
		}
		if(key.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
			searchEnginePo.setExeAverage(po.getValueStr());
			searchEnginePo.setAverageKeyId(po.getKeyId());
		}
	}
	
	break;
}

%>
<table id="OUT_SearchEngine" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="7" width="100%"><font color="#000000" size="2">[ SearchEngine ]</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center">URL</td>
	  <td align="center">类型</td>
	  <td align="center">时间</td>
	  <td align="center" colspan="2">执行数次</td>
	  <td  align="center" colspan="2">平均执行时间(ms)</td>	 
  	</tr>
  	<%
  	for(Map.Entry<String,SearchEnginePo> entry:searchEnginePoMap.entrySet()){ 
  		String[] _keys = entry.getKey().split("_");
  		String time = listSearchEngine.get(0).getCollectTime();
  		KeyValueVo vo = map.get(time);
  		if(vo==null)vo = new KeyValueVo();
  		
  		KeyValuePo po1 = vo.getMap().get("OUT_SearchEngine_"+entry.getKey()+"_"+Constants.COUNT_TIMES_FLAG);
  		KeyValuePo po2 = vo.getMap().get("OUT_SearchEngine_"+entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG);
  	%>
  	<tr>  	   	
	  <td align="left" width="300"><%=_keys[0] %></td>
	  <td align="center"><%=_keys[1] %></td>
	  <td align="center"><%=time %></td>	  
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getCountkeyId() %>)"/><%=entry.getValue().getExeCount() %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getExeCount(),po1==null?null:po1.getValueStr()) %></td>
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(entry.getValue().getExeAverage()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getExeAverage(),po2==null?null:po2.getValueStr()) %></td>	 
  	</tr>
  	<%} %>			
</table>
<%} }%>




<%
{
List<KeyValueVo> listTairClient = MonitorTimeAo.get().findLikeKeyByLimit("OUT_TairClient",appName);
if(listTairClient!=null&&listTairClient.size()>0){
	
	List<KeyValueVo> list = new ArrayList<KeyValueVo>();
	list.add(listTairClient.get(0));
	Map<String,KeyValueVo> map = MonitorTimeAo.get().findTimeMonitorByTime("OUT_TairClient",appName,list);
	
//key OUT_TairClient2.2.3_put_level-3_COUNTTIMES
Map<String,TairClientPo> tairClientPoMap = new HashMap<String,TairClientPo>();
for(int i=0;i<1;i++){
	KeyValueVo searchVo = listTairClient.get(i);
	for(Map.Entry<String,KeyValuePo> entry:searchVo.getMap().entrySet()){		
		String key = entry.getKey();
		KeyValuePo po = entry.getValue();
		
		String[] _keys = key.split("_");
		
		String keyName = _keys[1]+"_"+_keys[2]+"_"+_keys[3];
		
		TairClientPo tairClientPo = tairClientPoMap.get(keyName);
		if(tairClientPo==null){
			tairClientPo = new TairClientPo();
			tairClientPoMap.put(keyName,tairClientPo);
		}
		if(key.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
			tairClientPo.setExeCount(po.getValueStr());
			tairClientPo.setCountkeyId(po.getKeyId());
		}
		if(key.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
			tairClientPo.setAverageExe(po.getValueStr());
			tairClientPo.setAverageKeyId(po.getKeyId());
		}
	}
	
	break;
}

%>
<table id="OUT_TairClient" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="7" width="100%"><font color="#000000" size="2">[ TairClient ]</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center">URL</td>
	  <td align="center">类型</td>
	  <td align="center">时间</td>
	  <td align="center" colspan="2">执行数次</td>
	  <td  align="center" colspan="2">平均执行时间(ms)</td>	 
  	</tr>
  	<%
  	for(Map.Entry<String,TairClientPo> entry:tairClientPoMap.entrySet()){ 
  		String[] _keys = entry.getKey().split("_");
  		String time = listTairClient.get(0).getCollectTime();
  		KeyValueVo vo = map.get(time);
  		if(vo==null)vo = new KeyValueVo();
  		
  		KeyValuePo po1 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.COUNT_TIMES_FLAG);
  		KeyValuePo po2 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG);
  	%>
  	<tr>  	   	
	  <td align="left" width="300"><%=_keys[0] %></td>
	  <td align="center"><%=_keys[1] %></td>
	  <td align="center"><%=time %></td>	  
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getCountkeyId() %>)"/><%=entry.getValue().getExeCount() %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getExeCount(),po1==null?null:po1.getValueStr()) %></td>
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(entry.getValue().getAverageExe()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getAverageExe(),po2==null?null:po2.getValueStr()) %></td>	 
  	</tr>
  	<%} %>			
</table>
<%} }%>



<%
{
List<KeyValueVo> listTairClient = MonitorTimeAo.get().findLikeKeyByLimit("OUT_forest_",appName);
if(listTairClient!=null&&listTairClient.size()>0){
	
	List<KeyValueVo> list = new ArrayList<KeyValueVo>();
	list.add(listTairClient.get(0));
	Map<String,KeyValueVo> map = MonitorTimeAo.get().findTimeMonitorByTime("OUT_forest_",appName,list);
	
//key OUT_forest_client_getStdCategory_COUNTTIMES
Map<String,TairClientPo> tairClientPoMap = new HashMap<String,TairClientPo>();
for(int i=0;i<1;i++){
	KeyValueVo searchVo = listTairClient.get(i);
	for(Map.Entry<String,KeyValuePo> entry:searchVo.getMap().entrySet()){		
		String key = entry.getKey();
		KeyValuePo po = entry.getValue();
		
		String[] _keys = key.split("_");
		
		String keyName = _keys[1]+"_"+_keys[2]+"_"+_keys[3];
		
		TairClientPo tairClientPo = tairClientPoMap.get(keyName);
		if(tairClientPo==null){
			tairClientPo = new TairClientPo();
			tairClientPoMap.put(keyName,tairClientPo);
		}
		if(key.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
			tairClientPo.setExeCount(po.getValueStr());
			tairClientPo.setCountkeyId(po.getKeyId());
		}
		if(key.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
			tairClientPo.setAverageExe(po.getValueStr());
			tairClientPo.setAverageKeyId(po.getKeyId());
		}
	}
	
	break;
}

%>
<table id="OUT_forest" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="7" width="100%"><font color="#000000" size="2">[ Forest ]</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center">类型</td>
	  <td align="center">方法</td>
	  <td align="center">时间</td>
	  <td align="center" colspan="2">执行数次</td>
	  <td  align="center" colspan="2">平均执行时间(ms)</td>	 
  	</tr>
  	<%
  	for(Map.Entry<String,TairClientPo> entry:tairClientPoMap.entrySet()){ 
  		String[] _keys = entry.getKey().split("_");
  		String time = listTairClient.get(0).getCollectTime();
  		KeyValueVo vo = map.get(time);
  		if(vo==null)vo = new KeyValueVo();
  		
  		KeyValuePo po1 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.COUNT_TIMES_FLAG);
  		KeyValuePo po2 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG);
  	%>
  	<tr>  	   	
	  <td align="left" width="40"><%=_keys[1] %></td>
	  <td align="left"><%=_keys[2] %></td>
	  <td align="center"><%=time %></td>	  
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getCountkeyId() %>)"/><%=entry.getValue().getExeCount() %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getExeCount(),po1==null?null:po1.getValueStr()) %></td>
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(entry.getValue().getAverageExe()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getAverageExe(),po2==null?null:po2.getValueStr()) %></td>	 
  	</tr>
  	<%} %>			
</table>
<%} }%>





<%
{
List<KeyValueVo> listTairClient = MonitorTimeAo.get().findLikeKeyByLimit("OUT_PageCache",appName);
if(listTairClient!=null&&listTairClient.size()>0){
	
	List<KeyValueVo> list = new ArrayList<KeyValueVo>();
	list.add(listTairClient.get(0));
	Map<String,KeyValueVo> map = MonitorTimeAo.get().findTimeMonitorByTime("OUT_PageCache",appName,list);
	
//key OUT_PageCache-???_level-2_level-3_COUNTTIMES
Map<String,TairClientPo> tairClientPoMap = new HashMap<String,TairClientPo>();
for(int i=0;i<1;i++){
	KeyValueVo searchVo = listTairClient.get(i);
	for(Map.Entry<String,KeyValuePo> entry:searchVo.getMap().entrySet()){		
		String key = entry.getKey();
		KeyValuePo po = entry.getValue();
		
		String[] _keys = key.split("_");
		
		String keyName = _keys[1]+"_"+_keys[2]+"_"+_keys[3];
		
		TairClientPo tairClientPo = tairClientPoMap.get(keyName);
		if(tairClientPo==null){
			tairClientPo = new TairClientPo();
			tairClientPoMap.put(keyName,tairClientPo);
		}
		if(key.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
			tairClientPo.setExeCount(po.getValueStr());
			tairClientPo.setCountkeyId(po.getKeyId());
		}
		if(key.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
			tairClientPo.setAverageExe(po.getValueStr());
			tairClientPo.setAverageKeyId(po.getKeyId());
		}
	}
	
	break;
}

%>
<table id="OUT_PageCache" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="8" width="100%"><font color="#000000" size="2">[ PageCache ]</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center">类型</td>
	  <td align="center">级别</td>
	  <td align="center">级别</td>
	  <td align="center">时间</td>
	  <td align="center" colspan="2">执行数次</td>
	  <td  align="center" colspan="2">平均执行时间(ms)</td>	 
  	</tr>
  	<%
  	for(Map.Entry<String,TairClientPo> entry:tairClientPoMap.entrySet()){ 
  		String[] _keys = entry.getKey().split("_");
  		String time = listTairClient.get(0).getCollectTime();
  		KeyValueVo vo = map.get(time);
  		if(vo==null)vo = new KeyValueVo();
  		
  		KeyValuePo po1 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.COUNT_TIMES_FLAG);
  		KeyValuePo po2 = vo.getMap().get("OUT_"+entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG);
  	%>
  	<tr>  	   	
	  <td align="left" width="40"><%=_keys[0] %></td>
	  <td align="left"><%=_keys[1] %></td>
	   <td align="left"><%=_keys[2] %></td>
	  <td align="center"><%=time %></td>	  
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getCountkeyId() %>)"/><%=entry.getValue().getExeCount() %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getExeCount(),po1==null?null:po1.getValueStr()) %></td>
	  <td align="center" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=entry.getValue().getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(entry.getValue().getAverageExe()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=entry.getValue().getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td align="center" width="60"><%=Utlitites.scale(entry.getValue().getAverageExe(),po2==null?null:po2.getValueStr()) %></td>	 
  	</tr>
  	<%} %>			
</table>
<%} }%>





<%	
{
Map<String,List<KeyValuePo>> outHsfKeyMap = MonitorTimeAo.get().findLikeKeyAverageByLimit5("OUT_HSF-Consumer",appName);
//List<HsfDayLogDO> haBoList = null;
if(outHsfKeyMap.size()>0){
	//掉用哈勃接口
	//IHaBoDataDetailService haBoDataDetailService = HaBoDataDetailServiceImpl.get();
	//haBoList = haBoDataDetailService.getHsfConsumerListByAppName(HaBoAppNameMap.getHaBoAppName(appName));
	//if(haBoList == null)haBoList = new ArrayList<HsfDayLogDO>();
	//for(HsfDayLogDO hsfDayLogDO:haBoList){
	//	System.out.println(hsfDayLogDO.getLevel1Name()+"_"+hsfDayLogDO.getLevel2Name()+"_"+hsfDayLogDO.getLevel3Name());
	//}
%>
<table id="OUT_HSF-Consumer" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="10" width="100%"><font color="#000000" size="2">[ HSF ]</font>&nbsp;&nbsp;</td>
	</tr>	
	
	<%
	MonitorVo vo = new MonitorVo();
	
	for(Map.Entry<String,List<KeyValuePo>> entry:outHsfKeyMap.entrySet()){		
		String key = entry.getKey();
		
		if(key.indexOf("getReleasePagesBySiteIdUserIdSid")>-1){
			key = entry.getKey();
		}
		
		String keyName = entry.getKey();
		String[] _keys = key.split("_");
		String hsfType = _keys[1];
		String className =  _keys[2];
		String methodName = _keys[3];
		String flagName = _keys[4]; 
		
		List<KeyValuePo> list = entry.getValue();
		
		String dataValue = list.get(0).getValueStr();
		int keyId = list.get(0).getKeyId();
		//OUT_HSF-Consumer-BizException_com.taobao.tc.service.TcTradeService:1.0.0_sellerConfirmSendGoods_executes  
		String[] keys = key.split("_");		
		if("JVM".equals(keys[2])){continue;}		
		String name = keys[2]+"_"+keys[3];
		HsfPo po = vo.getOutHsfMap().get(name);
		//System.out.println("类型："+keys[1]);
		if(po==null){
			po = new HsfPo();
			System.out.println("name:"+name+" keys.length:"+keys.length+"_"+keys[1]);
			if(keys.length >=1 && keys[1].startsWith("HSF-Consumer")){
				//for(HsfDayLogDO hsfDayLogDO:haBoList){
				//	String level1AndLevel2Name = hsfDayLogDO.getLevel1Name()+"_"+hsfDayLogDO.getLevel2Name();
				//	if(level1AndLevel2Name.equals(name)){
				//		HsfDayLogDO totleHsfDayLogDO = haBoDataDetailService.getSingleHsfConsumer(hsfDayLogDO.getChildAppId() , hsfDayLogDO.getLevel1Id(), hsfDayLogDO.getLevel2Id(), hsfDayLogDO.getLevel3Id());
				//		if(totleHsfDayLogDO != null){
				//	    	po.setTotalRespNum((po.getTotalRespNum()==null?0D:po.getTotalRespNum())+totleHsfDayLogDO.getTotalValue1());
				//	    	po.setTotalRespTime((po.getTotalRespTime()==null?0D:po.getTotalRespTime())+totleHsfDayLogDO.getTotalValue2());    	
				//	    }
				//	}
				//}
			}										
			vo.getOutHsfMap().put(name, po);
			vo.getOutHsfList().add(po);
		}
		if(keys[2].indexOf("com.taobao.item")>-1){
			po.setAim("ic");
		}else if(keys[2].indexOf("com.taobao.tc")>-1){
			po.setAim("tc");
		}else
		if(keys[2].indexOf("com.taobao.uic")>-1){
			po.setAim("uic");
		}else
		if(keys[2].indexOf("com.taobao.ic")>-1){
			po.setAim("ic");
		}else
		if(keys[2].indexOf("com.taobao.sc")>-1){
			po.setAim("sc");
		}else if(keys[2].indexOf("com.taobao.designcenter")>-1){
			po.setAim("designcenter");
		}else if(keys[2].indexOf("com.taobao.db")>-1){
			po.setAim("db");
		}else if(keys[2].indexOf("com.taobao.logistics")>-1){
			po.setAim("logistics");
		}else if(keys[2].indexOf("com.taobao.upp")>-1){
			po.setAim("upp");
		}else if(keys[2].indexOf("com.taobao.clientSide")>-1){
			po.setAim("clientSide");
		}else if(keys[2].indexOf("com.taobao.messenger")>-1){
			po.setAim("messenger");
		}else if(keys[2].indexOf("com.taobao.shopservice")>-1){
			po.setAim("shopservice");
		}else if(keys[2].indexOf("com.taobao.misccenter")>-1){
			po.setAim("misccenter");
		}else if(keys[2].indexOf("com.taobao.mmp")>-1){
			po.setAim("mmp");
		}else if(keys[2].indexOf("com.taobao.promotioncenter")>-1){
			po.setAim("promotioncenter");
		}else if(keys[2].indexOf("com.taobao.vic")>-1){
			po.setAim("vic");
		}else if(keys[2].indexOf("com.taobao.forest")>-1){
			po.setAim("forest");
		}else if(keys[2].indexOf("com.taobao.top")>-1){
			po.setAim("top");
		}else if(keys[2].indexOf("com.taobao.kfc")>-1){
			po.setAim("kfc");
		}else if(keys[2].indexOf("com.taobao.serviceone")>-1){
			po.setAim("serviceone");
		}else{										
			po.setAim(" - ");
		}
		
		String _cn = keys[2];
		if(_cn.indexOf(":")>-1){
			_cn = _cn.substring(0,_cn.lastIndexOf(":"));
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length())+keys[2].substring(keys[2].lastIndexOf(":"),keys[2].length());
		}else{
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
		}
		po.setClassName(_cn);
		po.setMethodName(keys[3]);
											
		
		if(keys[1].equals("HSF-Consumer-BizException")){
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
				po.getBizExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
				po.setBizExecptionNum((dataValue));
				po.setBizCountkeyId(keyId);
			}
			
		}else if(keys[1].equals("HSF-Consumer-Exception")){
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
				po.getExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
				po.setExecptionNum(dataValue);
				po.setExcCountkeyId(keyId);
			}
			
		}else if(keys[1].equals("HSF-Consumer")){
			String collectTime = list.get(0).getCollectTimeStr();
			po.setCollectTime(collectTime);
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
				po.getExeCountNumMap().put(keys[2], Utlitites.getLong(dataValue));
				po.setExeCount((dataValue));											
				po.setExeCountNum(Utlitites.getLong(dataValue));
				po.setCountkeyId(keyId);
			}
			if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
				po.getAverageExeMap().put(keys[2], Utlitites.getDouble(dataValue));
				po.setAverageExe((dataValue));
				po.setAverageKeyId(keyId);
			}										
			
		}	
	}	
	%>
	<tr class="ui-widget-header ">
	  <td align="center" width="60px">目的</td>
	  <td align="center" >类名</td>
	  <td align="center" >方法名</td>
	  <td  align="center" >时间</td>
	  <td  align="center" >执行总次数</td>
	  <td  align="center" >平均执行时间(ms)</td>
	  <td  align="center" >执行次数</td>
	  <td  align="center" >平均执行时间(ms)</td>
	  <td  align="center" >Exception总数</td>
	  <td  align="center" >BizException总数</td>
  	</tr>	
  	
	<%
	Map<String, List<HsfClass>> sortOutMap = vo.getSortOutHsfMap1();		
	Iterator<Map.Entry<String,List<HsfClass>>> sortOutMapit = sortOutMap.entrySet().iterator();
	while(sortOutMapit.hasNext()){
		Map.Entry<String,List<HsfClass>> aimEntry = sortOutMapit.next();
		String aim = aimEntry.getKey();
		List<HsfClass> hsfClassList = aimEntry.getValue();				
		Collections.sort(hsfClassList);
		boolean aimShow = true;
		for(HsfClass hc:hsfClassList){
			List<HsfPo> list = hc.getHsfPo();
			Collections.sort(list);					
			boolean classShow = true;					
			for(HsfPo po:list){
				String key = aim+"_"+po.getClassName()+"_"+po.getMethodName();
			%>					
			<tr>
				<td align="left" ><%=aimShow?po.getAim():" - " %></td>			
				<td align="left" ><%=classShow?po.getHtmlClassName():" - " %></td>
				<td align="left" ><%=po.getHtmlMethodName() %></td>
				<td align="left"  ><%=po.getCollectTime() %></td>
				<td align="left"  ><%=po.getTotalRespNum() == null ? "" : Utlitites.fromatLong(Utlitites.formatDouble(po.getTotalRespNum()).toString()) %></td>
				<td align="left"  ><%=po.getAvgRespTime() == null ? "" : Utlitites.fromatLong(Utlitites.formatDouble(po.getAvgRespTime()).toString())  %></td>
				<td align="left"  ><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getCountkeyId() %>)"/><%=Utlitites.fromatLong(po.getExeCount()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=po.getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>					
				<td align="left"  ><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(po.getAverageExe()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=po.getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
				<td align="left"  ><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getExcCountkeyId() %>)"/><%=po.getExecptionNum()==null?" - ":po.getExecptionNum() %></td>				
				<td align="left"  ><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getBizCountkeyId() %>)"/><%=po.getBizExecptionNum()==null?" - ":po.getBizExecptionNum() %></td>
					
			</tr>					
			<%
			aimShow = false;
			classShow = false;	
			}
		classShow = true;
		}
		aimShow = true;
	}
	
	
	%>
	
</table>
<%} }  %>

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