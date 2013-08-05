<%@page import="java.io.IOException"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.web.vo.SitePo"%>
<%@page import="com.taobao.monitor.web.cache.SiteCache"%>
<%@page import="com.taobao.monitor.common.util.OpsFreeHostCache"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.cache.CacheTimeData"%>
<%@page import="com.taobao.monitor.web.vo.KeyValueVo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta http-equiv="refresh" content="60">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
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
</style>

<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
String collectTime1 = parseLogFormatDate.format(new Date());

%>
<script type="text/javascript">

	function openTrade(tradeType){
			parent.openTrade(tradeType);

	}



	
	function openDetail(keyId,appName){
		//parent.openTime(keyId);	
		if(keyId == null) {
			alert(keyId);
			keyId = "";
			}
		window.open("./time/key_detail_time.jsp?keyId="+keyId+"&appName="+appName );
	}
	
</script>

</head>
<body>
<%

Map<String,Float> qps = new HashMap<String,Float>();
qps.put("list", 85.7f);				//122.467*0.7=85.73
qps.put("shopsystem", 113.1f);		//161.53*0.7=113.071
qps.put("item", 213.4f);			//304.88*0.7=213.4
qps.put("tradeface-tm", 138.2f);	//197.47*0.7=138.229
qps.put("tradeface-buy", 99.4f);	//142.07*0.7=99.449
qps.put("cart", 156.1f);			//222.94*0.7=156.058
qps.put("login", 275.7f);			//393.85*0.7=275.695
qps.put("uicfinal", 1181.8f);		//1688.35*0.7=1181.845
qps.put("shopcenter", 961.8f);		//1373.95*0.7=961.765
qps.put("ump", 257.3f);				//367.52*0.7=257.264
qps.put("ic", 560.4f);				//800.6*0.7=560.42
qps.put("tradeplatform", 1298.5f);	//1854.98*0.7=1298.486
qps.put("at", 314.8f);	//449.733*0.7=314.8
qps.put("et", 304.0f);	//434.35 * 0.7
qps.put("iesearch", 28.0f);	//39.9657 * 0.7
qps.put("hotel", 112.4f);	//160.63 * 0.7

String appName = request.getParameter("appName");

int appId = Integer.parseInt(request.getParameter("appId"));

AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

Float f = qps.get(appName);

int tmpSiteId = 0;
Map<Integer,SitePo> tmpSiteMap = new HashMap<Integer,SitePo>();

%>

<div id="tabs" style="width: 100%px">

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" > = <%=appName %> =</font>&nbsp;&nbsp; <a href="./time/app_time.jsp?appId=<%=appId %>" target="_blank">详细</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" >	
	<tr>
		<td valign="top">	
			<%
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			List<KeyValueVo> pvVoList = MonitorTimeAo.get().findLikeKeyByLimit("PV",appName);	
			
			if(!"tradeplatform".equals(appName)&&pvVoList.size()>0){		
			%>	
		<table width="280" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
	<td colspan="23" align="left">
	<font color="#000000" size="2">响应数据</font>
	<img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openDetail(175,'<%=appName%>')"/>
	&nbsp;&nbsp;
	<font color="#000000" size="2">性能数据</font><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openDetail(944,'<%=appName%>')"/>
	&nbsp;&nbsp; 对比的数据为过去六周基线同时刻平均数据
	</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td>&nbsp;</td>
	  <td colspan="7" align="center">CM3</td>
	  <td colspan="7" align="center">CM4</td>
  	</tr>
	<tr class="ui-widget-header ">
		<td width="40" title="红色时间表示数据正在收集中，只做参考">时间</td>
		<td width="320" colspan="1" align="center">总流量(PV)
		<%
			if(appName != null && "shopsystem".equals(appName.trim())) {
				out.print( "（<font color='red'>商城流量</font>）");
			}
		%>
		</td>
		<td width="80" colspan="1" align="center">200流量</td>
		<td width="80" colspan="1" align="center" title="成功率 = (1-ApacheBlock拦截数/总流量)*(1-Tdod抽样率)">成功率</td>
		<td width="180" colspan="1" align="center" title="单台极限QPS*0.7">QPS（安全qps:<font color="background-color: #00CC00;font-weight:bold"><%= (f==null?"-":f) %></font>）</td>
		<td width="200" colspan="1" align="center">RT</td>
		<td width="180" colspan="1" align="center">Load</td>
		<td width="180" colspan="1" align="center">CPU</td>
		
		<td width="320" colspan="1" align="center">总流量(PV)
		<%
			if(appName != null && "shopsystem".equals(appName.trim())) {
				out.print( "（<font color='red'>商城流量</font>）");
			}
		%>
		</td>
		<td width="80" colspan="1" align="center">200流量</td>
		<td width="80" colspan="1" align="center" title="成功率 = (1-ApacheBlock拦截数/总流量)*(1-Tdod抽样率)">成功率</td>
		<td width="180" colspan="1" align="center" title="单台极限QPS*0.7">QPS（安全qps:<font color="background-color: #00CC00;font-weight:bold"><%= (f==null?"-":f) %></font>）</td>
		<td width="200" colspan="1" align="center">RT</td>
		<td width="180" colspan="1" align="center">Load</td>
		<td width="180" colspan="1" align="center">CPU</td>			
	</tr>
	<%
	List<KeyValueVo> tmallVoList = null;
	if("shopsystem".equals(appName)){
		 tmallVoList = MonitorTimeAo.get().findLikeKeyByLimit("URLPV_VISIT_",appName);
	}
	
	//性能数据
	List<KeyValueVo> loadVoList = MonitorTimeAo.get().findLikeKeyByLimit("System_",appName);	
	Map<String, KeyValueVo> loadMap = new HashMap<String, KeyValueVo>();	//时间做主键
	
	try {
		for(KeyValueVo vo: loadVoList) {
			KeyValuePo po = vo.getMap().get("System_LOAD_AVERAGEUSERTIMES");
			if(po != null) {
				loadMap.put(po.getCollectTimeStr(), vo);	
			}
		}		
	} catch(Exception e) {
		//e.printStackTrace();
	}

	for(int i=0;i<pvVoList.size();i++){
		
		Map<String,Double> dataMap1 = new HashMap<String,Double>();		//响应时间
		Map<String,Double> tmallMap = new HashMap<String,Double>();		//Tmall流量
		Map<String,Double> pv200Map = new HashMap<String,Double>();		//200访问数据

		KeyValueVo vo = pvVoList.get(i);
		
		try{
			KeyValueVo voTmall = new KeyValueVo();
			try{
				if(tmallVoList!=null){
					voTmall =tmallVoList.get(i);
				}
			 }catch(Exception e){}
		KeyValuePo po=vo.getMap().get("PV_VISIT_COUNTTIMES");
		KeyValuePo po1=vo.getMap().get("PV_REST_AVERAGEUSERTIMES");
		KeyValuePo tmall=voTmall.getMap().get("URLPV_VISIT_http://[\\\\\\w]+.tmall.com/_COUNTTIMES");
		KeyValuePo blockedPo = vo.getMap().get("PV_MainRequestBlocked_COUNTTIMES");	//命中率
		KeyValuePo tdodPo = vo.getMap().get("PV_TDodRate_AVERAGEUSERTIMES");	//tdod数据
		KeyValuePo pv200Po = vo.getMap().get("PV_VISIT_STATUS_200_COUNTTIMES");	//200的访问数据
		
		if(po == null){
			po = new KeyValuePo();
		}
		if(po1 == null){
			po1 = new KeyValuePo();
		}
		if(tmall == null){
			tmall = new KeyValuePo();
		}
		if(blockedPo == null) {
			blockedPo =  new KeyValuePo();
		}
		if(tdodPo == null) {
			tdodPo =  new KeyValuePo();
		}
		if(pv200Po == null) {
			pv200Po =  new KeyValuePo();
		}
				
		Map<String,Double> dataMap = new HashMap<String,Double>();		//存储每一个机房的PV的平均值
		Map<String,Double> singlePassPvCm3 = new HashMap<String,Double>();	//ip-value		
		Map<String,Double> singlePassPvCm4 = new HashMap<String,Double>();
		
		
		for(Map.Entry<Integer, Double> entry:po.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			
			if(site.getSiteId() == 0){
				site = tmpSiteMap.get(entry.getKey());
			}
			
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase();
			Double kv = dataMap.get(roomName);
			
			if(kv == null){
				dataMap.put(roomName, entry.getValue());
			}else {
				dataMap.put(roomName, entry.getValue() + kv );	
			}
			if(roomName.equals("cm3")) {
				singlePassPvCm3.put(siteName, entry.getValue());
			} else {	
				singlePassPvCm4.put(siteName, entry.getValue());
			}
		}
		//dataMap 先记录总数，最后做平均，记录显示有有流量的机器的总数
		final int cm3MachineNum = singlePassPvCm3.size() ==0?1:singlePassPvCm3.size();
		final int cm4MachineNum = singlePassPvCm4.size()==0?1:singlePassPvCm4.size();
		//响应时间
		for(Map.Entry<Integer, Double> entry:po1.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			if(site.getSiteId() == 0){
				site = tmpSiteMap.get(entry.getKey());
			}
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase();
			Double kv = dataMap1.get(roomName);
			if(kv == null){
				dataMap1.put(roomName, entry.getValue());
			}else{
				dataMap1.put(roomName,entry.getValue() + kv);
			}
		}
		for(Map.Entry<String,Double> entry: dataMap1.entrySet()) {
			if("cm3".equals(entry.getKey())) {
				dataMap1.put(entry.getKey(), entry.getValue()/cm3MachineNum);	
			} else {
				dataMap1.put(entry.getKey(), entry.getValue()/cm4MachineNum);
			}
		}		
		
		//Tmall的PV
		for(Map.Entry<Integer, Double> entry:tmall.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase();
			Double kv = tmallMap.get(_tmp[0].toLowerCase());
			if(kv == null){
				tmallMap.put(roomName, entry.getValue());
			}else{
				tmallMap.put(roomName, entry.getValue() + kv);
			}
		}
		for(Map.Entry<String,Double> entry: tmallMap.entrySet()) {
			if("cm3".equals(entry.getKey())) {
				tmallMap.put(entry.getKey(), entry.getValue()/cm3MachineNum);	
			} else {
				tmallMap.put(entry.getKey(), entry.getValue()/cm4MachineNum);
			}			
		}
		
		Map<String,Double> blockedMap = new HashMap<String,Double>();	//阻塞数据		
		for(Map.Entry<Integer, Double> entry:blockedPo.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase().trim();
			Double kv = blockedMap.get(roomName);
			if(kv == null){
				blockedMap.put(roomName, entry.getValue());
			}else{
				blockedMap.put(roomName, entry.getValue() + kv);
			}
		}	
		
		Map<String,Double> singleTdodMapCm3 = new HashMap<String,Double>();		//机房CM3每一台机器解析的tdod,根据ip进行记录
		Map<String,Double> singleTdodMapCm4 = new HashMap<String,Double>();		
		
		for(Map.Entry<Integer, Double> entry:tdodPo.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase().trim();
			if(roomName.equals("cm3")) {
				Double kv = singleTdodMapCm3.get(roomName);
				if(kv == null){
					singleTdodMapCm3.put(siteName, entry.getValue());
				}else{
					singleTdodMapCm3.put(siteName, entry.getValue()+kv);
				}				
			} else if(roomName.equals("cm4")){
				Double kv = singleTdodMapCm4.get(roomName);
				if(kv == null){
					singleTdodMapCm4.put(siteName, entry.getValue());
				}else{
					singleTdodMapCm4.put(siteName, entry.getValue() + kv);
				}				
			}
		}
		
		//200 Visit
		for(Map.Entry<Integer, Double> entry:pv200Po.getSiteValueMap().entrySet()){
			SitePo site = SiteCache.get().getKey(entry.getKey());
			String siteName = site.getSiteName();
			String[] _tmp = siteName.split("_");
			String roomName = _tmp[0].toLowerCase();
			Double kv = pv200Map.get(roomName);
			if(kv == null){
				pv200Map.put(roomName, entry.getValue());
			}else{
				pv200Map.put(roomName, entry.getValue() + kv);
			}
		}	
		for(Map.Entry<String,Double> entry: pv200Map.entrySet()) {
			if("cm3".equals(entry.getKey())) {
				pv200Map.put(entry.getKey(), entry.getValue()/cm3MachineNum);	
			} else {
				pv200Map.put(entry.getKey(), entry.getValue()/cm4MachineNum);
			}			
		}		
		
		KeyValuePo oldpo=CacheTimeData.get().getAppKeyDataBase(appName, "PV_VISIT_COUNTTIMES", vo.getCollectTime());
		if(oldpo == null) {	//如果基线数据为null，则使用上周数据
			oldpo=CacheTimeData.get().getAppKeyData(appName, "PV_VISIT_COUNTTIMES", vo.getCollectTime());	
		}
		
		KeyValuePo oldpo1=CacheTimeData.get().getAppKeyDataBase(appName, "PV_REST_AVERAGEUSERTIMES", vo.getCollectTime());
		if(oldpo1 == null) {
			oldpo1=CacheTimeData.get().getAppKeyData(appName, "PV_REST_AVERAGEUSERTIMES", vo.getCollectTime());	
		}
		
		
		//负载相关数据
		KeyValueVo loadVo = loadMap.get(vo.getCollectTime());
		String loadValue = "";
		String loadValuePre = "";
		String cpuValue = "";
		String cupValuePre = "";
		
		Map<String,Double> loadDataMap = new HashMap<String,Double>();
		Map<String,Double> cpuDataMap = new HashMap<String,Double>();		
		
		if(loadVo != null) {
			KeyValuePo loadPo = loadVo.getMap().get("System_LOAD_AVERAGEUSERTIMES");
			KeyValuePo cpuPo = loadVo.getMap().get("System_CPU_AVERAGEUSERTIMES");
			
			KeyValuePo loadOldPo = CacheTimeData.get().getAppKeyDataBase(appName, "System_LOAD_AVERAGEUSERTIMES", vo.getCollectTime());
			if(loadOldPo == null) {
				loadOldPo = CacheTimeData.get().getAppKeyData(appName, "System_LOAD_AVERAGEUSERTIMES", vo.getCollectTime());
			}			
			
			KeyValuePo cpuOldPo = CacheTimeData.get().getAppKeyDataBase(appName, "System_LOAD_AVERAGEUSERTIMES", vo.getCollectTime());
			if(cpuOldPo == null) {
				cpuOldPo = CacheTimeData.get().getAppKeyData(appName, "System_LOAD_AVERAGEUSERTIMES", vo.getCollectTime());	
			}
			
			loadValuePre = loadOldPo==null? null:loadOldPo.getValueStr();
			cupValuePre = cpuOldPo==null ? null:cpuOldPo.getValueStr();
			
			if(loadPo != null) {
				//load的信息
				for(Map.Entry<Integer, Double> entry: loadPo.getSiteValueMap().entrySet()){
					SitePo site = SiteCache.get().getKey(entry.getKey());
					
					String siteName = site.getSiteName();
					String[] _tmp = siteName.split("_");
					String roomName = _tmp[0].toLowerCase();
					Double kv = loadDataMap.get(roomName);
					if(kv == null){
						loadDataMap.put(roomName, entry.getValue());
					}else{
						loadDataMap.put(roomName, entry.getValue() + kv);
					}
				}
				for(Map.Entry<String,Double> entry: loadDataMap.entrySet()) {
					if("cm3".equals(entry.getKey())) {
						loadDataMap.put(entry.getKey(), Arith.div(entry.getValue()/cm3MachineNum,1,2));	
					} else {
						loadDataMap.put(entry.getKey(), Arith.div(entry.getValue()/cm4MachineNum,1,2));
					}			
				}					
			}
						
			if(cpuPo != null) {
				//CPU的信息
				for(Map.Entry<Integer, Double> entry:cpuPo.getSiteValueMap().entrySet()){
					SitePo site = SiteCache.get().getKey(entry.getKey());
					String siteName = site.getSiteName();
					String[] _tmp = siteName.split("_");
					String roomName = _tmp[0].toLowerCase();
					Double kv = cpuDataMap.get(roomName);
					if(kv == null){
						cpuDataMap.put(roomName, entry.getValue());
					}else{
						cpuDataMap.put(roomName, entry.getValue() + kv);
					}
				}					
				for(Map.Entry<String,Double> entry: cpuDataMap.entrySet()) {
					if("cm3".equals(entry.getKey())) {
						cpuDataMap.put(entry.getKey(), Arith.div(entry.getValue()/cm3MachineNum,1,2));	
					} else {
						cpuDataMap.put(entry.getKey(), Arith.div(entry.getValue()/cm4MachineNum,1,2));
					}			
				}					
			}
		}
	
		
		boolean isRowsShow = (i == 0 || i == 1);
		String strFlatMsg = "<font color='red'>收集</font>";
		
		//因为老代码中机房写死，所以使用变量代替
		String hitRateCM3 = "";
		String hitRateCM4 = "";
	
		if(dataMap.get("cm3") != null) {
			final double pvCM3PassTotal = dataMap.get("cm3");
			final double blockCm3 = blockedMap.get("cm3") == null ? 0: blockedMap.get("cm3");
			//平均，dataMap.get("cm3") != null，故不需要做除数校验
			dataMap.put("cm3", dataMap.get("cm3") / cm3MachineNum);
			
			double pvTotalCm3 = 0;		
			for(String ip: singlePassPvCm3.keySet()) {
				double tdod = singleTdodMapCm3.get(ip) == null ? 0 : singleTdodMapCm3.get(ip);
				if(tdod >= 0 && tdod < 100) {	//只记录合法数据
					pvTotalCm3 += singlePassPvCm3.get(ip)*100 /(100 - tdod);	
				}
			}
			if(pvTotalCm3 == 0) {
				hitRateCM3 = strFlatMsg;
			} else {
				double successRateCm3 = (pvCM3PassTotal  - blockCm3) / pvTotalCm3;
				double result = Arith.div(successRateCm3, 1, 4);
				if(result != 1) {	//非100%，标记
					hitRateCM3 = "<font color='red'>" + Arith.mul(result, 100) + "%" + "</font>";
				} else {
					hitRateCM3 = "100%";	
				}					
			}
		} else {
			hitRateCM3 = strFlatMsg;
		}
		
		if(dataMap.get("cm4") != null) {
			final double pvCM4PassTotal = dataMap.get("cm4");
			final double blockCm4 = blockedMap.get("cm4") == null ? 0: blockedMap.get("cm4");
			dataMap.put("cm4", dataMap.get("cm4") / cm4MachineNum);
			
			double pvTotalCm4 = 0;		
			for(String ip: singlePassPvCm4.keySet()) {
				double tdod = singleTdodMapCm4.get(ip) == null ? 0 : singleTdodMapCm4.get(ip);
				if(tdod >= 0 && tdod < 100) {
					pvTotalCm4 += singlePassPvCm4.get(ip)*100 /(100 - tdod);	
				}
			}
			if(pvTotalCm4 == 0) {
				hitRateCM4 = strFlatMsg;
			} else {
				double successRateCm4 = (pvCM4PassTotal  - blockCm4) / pvTotalCm4;
				double result = Arith.div(successRateCm4, 1, 4);
				if(result != 1) {	//非100%，标记
					hitRateCM4 = "<font color='red'>" + Arith.mul(result, 100) + "%" + "</font>";
				} else {
					hitRateCM4 = "100%";	
				}					
			}
		} else {
			hitRateCM4 = strFlatMsg;
		}		
		
		if(isRowsShow) {
			hitRateCM3 = strFlatMsg;	
			hitRateCM4 = strFlatMsg;
		}
	%>
	<tr >
		<td width="40">
		<%
			//前两条颜色加粗，红色处理
			if(isRowsShow) {
				out.print("<font color='red' style='font-weight: bold;'>" + vo.getCollectTime() + "</font>");
			} else {
				out.println(vo.getCollectTime());
			}
		%>
		</td>
		<td width="320">
		<%
			//流量
			if(dataMap.get("cm3") == null) {
				out.print(strFlatMsg);	
			} else {
				out.print(Utlitites.formatDotTwo(dataMap.get("cm3"), 0));
				if(tmallMap.get("cm3")!=null) {
					%>
					(<font color="red"><%=Utlitites.formatDotTwo(tmallMap.get("cm3"), 0)%></font>)
					<%
				}
				//百分比
				if(isRowsShow) {	
					out.print("&nbsp;" + strFlatMsg);
				} else {
					out.print(Utlitites.scale(dataMap.get("cm3") == null?null:dataMap.get("cm3").toString(),oldpo==null?null:oldpo.getValueStr()));
				}				
			}
		%>
		</td>
		<td width="80">
		<%
		if(pv200Map.get("cm3") == null) {
			out.print(strFlatMsg);
		} else {
			out.print(Utlitites.formatDotTwo(pv200Map.get("cm3"),0));
		}
		%>
		</td>				
		<td width="80"><%=hitRateCM3%></td>
		<td width="180">
		<%
			//QPS
			if(dataMap.get("cm3") == null) {	
				out.print(strFlatMsg);
			} else {
				out.print(Utlitites.formatDotTwo((dataMap.get("cm3")/60.0),0));
				//QPS Rate
				if(isRowsShow) {	
					out.print("&nbsp;" + strFlatMsg);
				} else {
					out.print(Utlitites.scale(dataMap.get("cm3") + "",oldpo==null?null:oldpo.getValueStr()));
				}					
			}			
		%>		
		</td>
		<td width="200">
		<%
			if(dataMap1.get("cm3") != null) {
				out.println(Utlitites.formatDotTwo((dataMap1.get("cm3")/1000),0) + Utlitites.scale(dataMap1.get("cm3") + "",oldpo1==null?null:oldpo1.getValueStr()));
			} else {
				out.print(strFlatMsg);
			}
		%>
		</td>	
		<td width="180">
		<%
			if(loadDataMap.get("cm3")!= null) {
				out.print(loadDataMap.get("cm3") + "" + Utlitites.scale(loadDataMap.get("cm3") + "", loadValuePre));
			} else {
				out.print(strFlatMsg);
			}
		%>
		</td>		
		<td width="180">
		<%
			if(cpuDataMap.get("cm3") != null) {
				out.print(cpuDataMap.get("cm3") + Utlitites.scale(cpuDataMap.get("cm3") + "", cupValuePre));
			} else {
				out.print(strFlatMsg);
			}
		%>
		</td>
		<!-- CM4 -->
		<td width="320">
		<%
			//流量
			if(dataMap.get("cm4") == null) {
				out.print(strFlatMsg);	
			} else {
				out.print(Utlitites.formatDotTwo(dataMap.get("cm4"),0));
				if(tmallMap.get("cm4")!=null) {
					%>
					(<font color="red"><%=Utlitites.formatDotTwo(tmallMap.get("cm4"), 0)%></font>)
					<%
					}
					//百分比
					if(isRowsShow) {	
						out.print("&nbsp;" + strFlatMsg);
					} else {
						out.print(Utlitites.scale(dataMap.get("cm4") == null?null:dataMap.get("cm4").toString(),oldpo==null?null:oldpo.getValueStr()));
					}				
			}
		%>
		</td>
		<td width="80">
		<%
		if(pv200Map.get("cm4") == null) {
			out.print(strFlatMsg);
		} else {
			out.print(Utlitites.formatDotTwo(pv200Map.get("cm4"),0));
		}		
		%>
		</td>		
		<td width="80"><%=hitRateCM4%></td>		
		<td width="180">
		<%
			//QPS
			if(dataMap.get("cm4") == null) {	
				out.print(strFlatMsg);
			} else {
				out.print(Utlitites.formatDotTwo((dataMap.get("cm4")/60.0),0));
				//QPS Rate
				if(isRowsShow) {	
					out.print("&nbsp;" + strFlatMsg);
				} else {
					out.print(Utlitites.scale(dataMap.get("cm4") + "",oldpo==null?null:oldpo.getValueStr()));
				}					
			}			
		%>		
		</td>		
		<td width="200">
		<%
			if(dataMap1.get("cm4") != null) {
				out.println(Utlitites.formatDotTwo((dataMap1.get("cm4")/1000),0) + Utlitites.scale(dataMap1.get("cm4") + "",oldpo1==null?null:oldpo1.getValueStr()));
			} else {
				out.print(strFlatMsg);
			}
		%>
		</td>
		
		<td width="180">
		<%
			if(loadDataMap.get("cm4")!= null) {
				out.print(loadDataMap.get("cm4") + "" + Utlitites.scale(loadDataMap.get("cm4") + "", loadValuePre));
			} else {
				out.print(strFlatMsg);
			}
		%>		
		</td>		
		<td width="180">
		<%
			if(cpuDataMap.get("cm4") != null) {
				out.print(cpuDataMap.get("cm4") + Utlitites.scale(cpuDataMap.get("cm4") + "", cupValuePre));
			} else {
				out.print(strFlatMsg);
			}
		%>		
		</td>
					
	</tr>
	<%}catch(Exception e){
		e.printStackTrace();	
		//out.print(e.toString());
	}} %>
	</table>
			<%
			}else{} 
			%>	
		</td>		
	</tr>
</table>

</div>
</div>


</div>

</body>
</html>