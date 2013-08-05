<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataForPageViewPo"%>
<%@page import="com.taobao.monitor.alarm.source.AlarmSourceConclude"%>
<%@page import="com.taobao.monitor.web.ao.BeidouAlarmAO"%>
<%@page import="com.taobao.monitor.web.vo.beidou.BeidouAlarmDataPo"%>
<%@page import="com.taobao.monitor.alarm.network.lb.NetworkDeviceAlarmAo"%>
<%@page import="com.taobao.monitor.alarm.network.lb.NetworkDeviceAlarmPo"%>
<%@page import="com.taobao.monitor.alarm.network.lb.LBRelateApp"%>
<%@page import="com.taobao.monitor.alarm.opsfree.GetAppHostAmountRunnable"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.util.DateFormatUtil"%>
<%@page import="com.taobao.monitor.alarm.source.constants.CommonConstants"%>
<%@page import="com.taobao.monitor.time.constants.OptionConstants"%>
<%@page import="com.taobao.monitor.time.util.TairKeyValidator"%>
<%@page import="com.taobao.util.CollectionUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Arrays"%>
<%
    int appId = Integer.parseInt(request.getParameter("appId"));
    String startTimeString = (String)request.getParameter("startTime");
    String endTimeString = (String)request.getParameter("endTime");
	
    String alarmLevel = (String)request.getParameter("level");
    int level = OptionConstants.ALARM_OPTION_ALL;
    if(StringUtils.isNumeric(alarmLevel)){
    	level = Integer.parseInt(alarmLevel);
    }
    
    Date startTime, endTime;
    if(StringUtils.isBlank(startTimeString) || StringUtils.isBlank(endTimeString)){
    	endTime = new Date();
    	startTime = DateFormatUtil.getTime(endTime,CommonConstants.TIME_TYPE,CommonConstants.STARTTIME_BEFORE);
    } else {
    	startTime = DateFormatUtil.fomatPickTime(startTimeString);
    	endTime = DateFormatUtil.fomatPickTime(endTimeString);
    }
	SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	List<String> dbGroupNameList = new ArrayList<String>();
	Map<String, Integer> appName2IdMap = new HashMap<String, Integer>();
	String curAppName = null;
	if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("cart")) {
		dbGroupNameList = BeidouAlarmAO.get().getCartRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getCartRelateAppMap();
		curAppName = "cart";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_buy")) {
		dbGroupNameList = BeidouAlarmAO.get().getTfBuyRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTfBuyRelateAppMap();
		curAppName = "tf_buy";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_tm")) {
		dbGroupNameList = BeidouAlarmAO.get().getTfTmRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTfTmRelateAppMap();
		curAppName = "tf_tm";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tradeplatform")) {
		dbGroupNameList = BeidouAlarmAO.get().getTradeplatformRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTradeplatformRelateAppMap();
		curAppName = "tradeplatform";
	} else if ( appId == AppInfoAo.get().getAppInfoName2IdMap().get("tradeapi") ){
		appName2IdMap = AppInfoAo.get().getTradeapiRelateAppMap();
		curAppName = "tradeapi";
    } else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("auctionplatform")) {
        dbGroupNameList = BeidouAlarmAO.get().getAuctionplatformDBGroupList();
        appName2IdMap = AppInfoAo.get().getAuctionplatformRelateAppMap();
        curAppName = "auctionplatform";
    } else if (appId == 0){
    	dbGroupNameList = BeidouAlarmAO.get().getTradeRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTradeRelateAppMap();
		curAppName = "tradeplatform";
	}
	Set<String> appNameSet = appName2IdMap.keySet();
	HashMap<String, Integer> changeFreeIds = new HashMap<String, Integer>();
	changeFreeIds.putAll(appName2IdMap);
	changeFreeIds.put("mysql_taobao", 100000000);
	Set<String> alarmSources = AlarmSourceConclude.get().getAlarmSource(startTime, endTime,appNameSet);
 	
	boolean isWrapperInserted = false;
	List<Integer> keyLevelList = null;
	if(level == OptionConstants.ALARM_OPTION_ALL){
		keyLevelList = Arrays.asList(OptionConstants.ALARM_OPTION_P0, OptionConstants.ALARM_OPTION_P2);
	} else {
		keyLevelList = Arrays.asList(level);
	}

	HashMap<String, List<AlarmDataForPageViewPo>> appAlarmMap  = MonitorAlarmAo.get().getHistoryTradeRalateAlarmMapByTime(startTime,endTime,keyLevelList,appNameSet);
	List<String> alarmedAppNameList = new ArrayList<String>();
	for (Map.Entry<String, Integer> entry : appName2IdMap.entrySet()) {
		List<AlarmDataForPageViewPo> alarmDataForPageViewPoList = appAlarmMap.get(entry.getKey());
		if (CollectionUtil.isNotEmpty(alarmDataForPageViewPoList)) {
			alarmedAppNameList.add( entry.getKey() );
			if (isWrapperInserted == false) {
%>
<table cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="7"><h2>应用报警信息</h2></td>
	</tr>
	<tr>
		<td width='11%'><h3>应用名称</h3></td>
		<td width='44%'><h3>告警点</h3></td>
		<td width='7%'><h3>告警值</h3></td>
		<td width='11%'><h3>告警原因</h3></td>
		<td width='9%'><h3>机器ip</h3></td>
		<td width='12%'><h3>发生时间</h3></td>
		<td width='6%'><h3>判断模型</h3></td>
	</tr>
	<%
				isWrapperInserted = true;
			}
			for (AlarmDataForPageViewPo vpo : alarmDataForPageViewPoList) {
	%>
	<tr>
		<td><span class="redvalue"><%=vpo.getAppName()%></span></td>
		<%
			String keyName = vpo.getKeyName();
			if(TairKeyValidator.isTairKey(keyName)){
				%>
		<td>
			<span class="redvalue"><%=keyName%></span>
			<br/>
			<span class="tairmeaning">[<%=TairKeyValidator.getTairKeyDescByAppNameAndKeyName(curAppName, keyName)%>]</span>
		</td>
			<%
			} else {
				%>
		<td><span class="redvalue"><%=keyName%></span></td>
			<%
			}
		%>
		<td><span class="redvalue"><%=vpo.getAlarmValue()%></span></td>
		<td><span class="redvalue"><%=vpo.getAlarmCause()%></span></td>
		<td><span class="justice"><%=vpo.getIpString()%></span></td>
		<td><span class="redvalue"><%=standardSdf.format( vpo.getAlarmTime() )%></span></td>
		<td><span class="justice"><%=vpo.getModeName()%></span></td>
	</tr>
	<%
			}
		}
	}
    Map<String,List<BeidouAlarmDataPo>> dbAlarmMap = BeidouAlarmAO.get().getBeidouAlarmDataMapByTime(startTime,endTime);
	for (String dbGroupName : dbGroupNameList) {
		List<BeidouAlarmDataPo> alarmDataForPageViewPoList = dbAlarmMap.get(dbGroupName);
		if (CollectionUtil.isNotEmpty(alarmDataForPageViewPoList)) {
			if (isWrapperInserted == false) {
	%>
<table cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="7"><h2>应用报警信息</h2></td>
	</tr>
	<tr>
		<td width='11%'><h3>应用名称</h3></td>
		<td width='44%'><h3>告警点</h3></td>
		<td width='7%'><h3>告警值</h3></td>
		<td width='11%'><h3>告警原因</h3></td>
		<td width='9%'><h3>机器ip</h3></td>
		<td width='12%'><h3>发生时间</h3></td>
		<td width='6%'><h3>判断模型</h3></td>
	</tr>
		<%
				isWrapperInserted = true;
			}
			for (BeidouAlarmDataPo vpo : alarmDataForPageViewPoList) {
		%>
	<tr>
		<td><span class="redvalue"><%=dbGroupName%></span>报警</td>
		<td>---</td>
		<td>---</td>
		<td><span class="redvalue"><%=vpo.getAlertMsg()%></span></td>
		<td><span class="justice"><%=vpo.getAlertSource()%></span></td>
		<td><span class="redvalue"><%=standardSdf.format(vpo.getCheckTime())%></span></td>
		<td>---</td>
	</tr>
		<%
			}
		}
	}
	if( isWrapperInserted ){
		%>
</table>
	<% 
	} 
	boolean isLbTableStart = false;
	//if Apps alarm do
	//alarmedAppNameList.add("tradeplatform");
	if( CollectionUtil.isNotEmpty(alarmedAppNameList) ){
		ConcurrentHashMap<String,List<LBRelateApp>> lbAppRelate = GetAppHostAmountRunnable.lbAppRelateMap();
		Map<String, List<NetworkDeviceAlarmPo>> networkDeviceAlarmMap = NetworkDeviceAlarmAo.get().getLoadBalanceAlarmDataByTime(startTime, endTime);
		Set<String> alarmAppLBName = new HashSet<String>();
		for(String lbName:lbAppRelate.keySet()){
			List<LBRelateApp> lbRelateAppList = lbAppRelate.get( lbName );
			if( CollectionUtil.isNotEmpty(lbRelateAppList) ){
				for(LBRelateApp LBRApp:lbRelateAppList){
					if(alarmedAppNameList.contains( LBRApp.getAppName() )){
						//add alarmApp LbName
						alarmAppLBName.add( LBRApp.getLbName() );
					}
				}
			}
		}
		boolean isHostNamePrint = false;
		for(Map.Entry<String,List<NetworkDeviceAlarmPo>> entry:networkDeviceAlarmMap.entrySet()){
			List<NetworkDeviceAlarmPo> loadAlarmPoList = networkDeviceAlarmMap.get( entry.getKey() );
			if( CollectionUtil.isNotEmpty(loadAlarmPoList) && alarmAppLBName.contains(entry.getKey())){
				isHostNamePrint = false;
				for(NetworkDeviceAlarmPo po:loadAlarmPoList){
					StringBuffer sbAppName = new StringBuffer();
					for(LBRelateApp lBRelateApp:lbAppRelate.get( entry.getKey() )){
						sbAppName.append( lBRelateApp.getAppName()).append("<br />");
					}
					
					if( isLbTableStart == false ){
%>
<table cellpadding="0" cellspacing="0">
	<tr><td colspan="5"><h2>LB设备告警</h2></td></tr>
	<tr>
		<td><h3>主机名</h3></td>
		<td><h3>受影响应用</h3></td>
		<td><h3>告警指标</h3></td>
		<td><h3>报警内容</h3></td>
		<td><h3>报警时间</h3></td>
	</tr>
					<%
						isLbTableStart = true;
					}
				%>
	<tr>
			<%
					if( isHostNamePrint == false ){
			%>
		<td rowspan="<%=loadAlarmPoList.size()%>"><%=po.getHostName()%></td>
		<td rowspan="<%=loadAlarmPoList.size()%>" class="redvalue"><%=sbAppName.toString()%></td>
		<%
						isHostNamePrint = true;
					}
		%>
		<td><%=po.getServiceName()%></td>
		<td class="redvalue"><%=po.getOutput()%></td>
		<td class="redvalue"><%=po.getTime()%></td>
	</tr>
				<%
				}
			}
		}
	}
	if( isLbTableStart ){
		%>
</table>
	<% 
	} 
	if(isWrapperInserted == false && isLbTableStart == false) {
		%>
<div class="bigpadding"> 从<span class="resultTime"><%=standardSdf.format(startTime)%></span>到<span class="resultTime"> <%=standardSdf.format(endTime)%></span>时间段没有<%=(level == -1)?"所有级别":("P"+level+"级别")%>报警数据</div>
		<%
	}
	%>
<div id="content">
<%
	for(Map.Entry<String,Integer> entry:appName2IdMap.entrySet()){
			%>
	<div id="<%=entry.getKey()%>" appid="<%=entry.getValue()%>" alm="<%=alarmedAppNameList.contains(entry.getKey())%>" title="<%=alarmSources.contains(entry.getKey())%>"></div>
		<% 	
	}
	String relApp = "all";
	if(appId > 0){
		relApp = AppInfoAo.get().getAppInfoId2NameMap().get(appId);
	}
	for(String dbGroupName:dbGroupNameList){
		boolean isDbAlarm = CollectionUtil.isNotEmpty(dbAlarmMap.get(dbGroupName))&&BeidouAlarmAO.get().isAppRelatedDBAlarm(relApp,dbGroupName);
		%>
	<div id="<%=dbGroupName%>" appid="0" alm="<%=isDbAlarm%>" title="<%=alarmSources.contains(dbGroupName)%>"></div>
		<%
	}
	if(CollectionUtil.isNotEmpty(alarmedAppNameList)){
		%>
	<div id="hasalarm"></div>
		<%
	}
		%>
	<div id="starttime"><%=standardSdf.format(startTime)%></div>
	<div id="endtime"><%=standardSdf.format(endTime)%></div>
</div>