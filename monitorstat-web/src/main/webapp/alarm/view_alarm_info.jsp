<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataForPageViewPo"%>
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
    	startTime = DateFormatUtil.getTime(endTime,CommonConstants.TIME_TYPE,CommonConstants.TIME_TYPE);
    } else {
    	startTime = DateFormatUtil.fomatPickTime(startTimeString);
    	endTime = DateFormatUtil.fomatPickTime(endTimeString);
    }
   
	boolean isAlarmed = false;
	SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String appName = "";
	List<String> dbGroupNameList = new ArrayList<String>();
	Map<String, Integer> appName2IdMap = new HashMap<String, Integer>();

	if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("cart")) {
		dbGroupNameList = BeidouAlarmAO.get().getCartRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getCartRelateAppMap();
		appName = "cart";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_buy")) {
		dbGroupNameList = BeidouAlarmAO.get().getTfBuyRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTfBuyRelateAppMap();
		appName = "tf_buy";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_tm")) {
		dbGroupNameList = BeidouAlarmAO.get().getTfTmRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTfTmRelateAppMap();
		appName = "tf_tm";
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tradeplatform")) {
		dbGroupNameList = BeidouAlarmAO.get().getTradeplatformRelateDBGroupList();
		appName2IdMap = AppInfoAo.get().getTradeplatformRelateAppMap();
		appName = "tradeplatform";
    } else if (appId == 0){
		appName2IdMap = AppInfoAo.get().getTradeRelateAppMap();
		appName = "tradeplatform";
	}
	Set<String> appNameSet = appName2IdMap.keySet();
	HashMap<String, Integer> changeFreeIds = new HashMap<String, Integer>();
	changeFreeIds.putAll(appName2IdMap);
	changeFreeIds.put("mysql_taobao", 100000000);
 	
	boolean isWrapperInserted = false;
	
	List<Integer> list = null;
	if(level == OptionConstants.ALARM_OPTION_ALL){
		list = Arrays.asList(OptionConstants.ALARM_OPTION_P0, OptionConstants.ALARM_OPTION_P2);
	} else {
		list = Arrays.asList(level);
	}
	
	HashMap<String, List<AlarmDataForPageViewPo>> appAlarmMap  = MonitorAlarmAo.get().getHistoryTradeRalateAlarmMapByTime(startTime,endTime,list, appNameSet);
	List<String> alarmedAppNameList = new ArrayList<String>();
%>
<!doctype html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title><%=appName%>报警信息</title>
	<link href="<%=request.getContextPath() %>/statics/css/alarm-index.css" rel="stylesheet" type="text/css" />
</head>
<body>
<!-- start containner -->
<div id="container">
	<!-- start header -->
	<div id="header">
		<div class="title"><a class="hidelink" href="" onClick="return false;" title="回到监控首页">交易平台总监系统</a></div>
		<div class="menulist">
               <ul>
                   <li><a href="./index.jsp" target="_blank">首页</a></li>
                   <li><a href="./trade_alarm_relate.jsp">关联监控</a></li>
                   <li><a href="./tradeplatform_relate.jsp">tp监控页</a></li>
                   <li><a href="./tf_tm_relate.jsp">tm监控页</a></li>
                   <li><a href="./tf_buy_relate.jsp">buy监控页</a></li>
                   <li><a href="./cart_relate.jsp">cart监控页</a></li>
                   <li><a href="./auctionplatform_relate.jsp">拍卖ap监控</a></li>
                   <li><a href="./tradeapi_relate.jsp">Tradeapi</a></li>
               </ul>
	  	</div>
	</div>
	<!-- end header -->
	<!-- start taobaoapp -->
	<div id="taobaoapp">
		<div class="table-space">
			<h4><%=appName%>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/time/app/detail/apache/show.do?method=queryPvHost&amp;appName=<%=appName%>">每台主机详情</a></h4>
<%
for (Map.Entry<String, Integer> entry : appName2IdMap.entrySet()) {
	List<AlarmDataForPageViewPo> alarmDataForPageViewPoList = appAlarmMap.get(entry.getKey());
	if (CollectionUtil.isNotEmpty(alarmDataForPageViewPoList)) {
		alarmedAppNameList.add( entry.getKey() );
		if (isWrapperInserted == false) {
			%>
			<table width="100%" class="gradient-style">
				<thead>
					<tr>
						<td colspan="7" align="center"><h2>从<%=standardSdf.format(startTime)%>时刻到<%=standardSdf.format(startTime) %>时刻，应用报警信息</h2></td>
					</tr>
					<tr>
						<th width='11%'><h3>应用名称</h3></th>
						<th width="44%"><h3>告警点</h3></th>
						<th width="7%"><h3>告警值</h3></th>
						<th width="11%"><h3>告警原因</h3></th>
						<th width="9%"><h3>机器ip</h3></th>
						<th width="12%"><h3>发生时间</h3></th>
						<th width="6%"><h3>判断模型</h3></th>
					</tr>
				</thead>
				<tbody>
				<%
				isWrapperInserted = true;
			}
			for (AlarmDataForPageViewPo vpo : alarmDataForPageViewPoList) {
		%>
					<tr>
						<td><%=vpo.getAppName()%>报警</td>
						<%
							String keyName = vpo.getKeyName();
							if(TairKeyValidator.isTairKey(keyName)){
								%>
						<td>
							<%=keyName%>
							<br/>
							<span class="tairmeaning">[<%=TairKeyValidator.getTairKeyDescByAppNameAndKeyName(appName, keyName)%>]</span>
						</td>
							<%
							} else {
						%>
						<td><%=keyName%></td>
							<%
							}
						%>
						<td><%=vpo.getAlarmValue()%></td>
						<td><%=vpo.getAlarmCause()%></td>
						<td><%=vpo.getIpString()%></td>
						<td><%=standardSdf.format( vpo.getAlarmTime() )%></td>
						<td><%=vpo.getModeName()%></td>
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
				<table width="100%" class="gradient-style">
					<thead>
						<tr>
							<td colspan="7" align="center"><h2>从<%=standardSdf.format(startTime)%>时刻到<%=standardSdf.format(startTime) %>时刻，应用报警信息</h2></td>
						</tr>
						<tr>
							<th width='11%'><h3>应用名称</h3></th>
							<th width="44%"><h3>告警点</h3></th>
							<th width="7%"><h3>告警值</h3></th>
							<th width="11%"><h3>告警原因</h3></th>
							<th width="9%"><h3>机器ip</h3></th>
							<th width="12%"><h3>发生时间</h3></th>
							<th width="6%"><h3>判断模型</h3></th>
						</tr>
					</thead>
					<tbody>
					<%
				isWrapperInserted = true;
			}
			for (BeidouAlarmDataPo vpo : alarmDataForPageViewPoList) {
				%>
					<tr>
						<td><%=dbGroupName%>报警</td>
						<td>---</td>
						<td>---</td>
						<td><%=vpo.getAlertMsg()%></td>
						<td><%=vpo.getAlertSource()%></td>
						<td><%=standardSdf.format(vpo.getCheckTime())%></td>
						<td>---</td>
					</tr>
				<%
					}
				}
			}
			if( isWrapperInserted ){
				%>
				</tbody>
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
			<table width="100%" class="gradient-style">
				<thead>
					<tr>
						<td colspan="5" align="center"><h2>LB设备告警</h2></td>
					</tr>
					<tr>
						<td><h3>主机名</h3></td>
						<td><h3>受影响应用</h3></td>
						<td><h3>告警指标</h3></td>
						<td><h3>报警内容</h3></td>
						<td><h3>报警时间</h3></td>
					</tr>
				</thead>
				<tbody>
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
						<td><%=po.getOutput()%></td>
						<td><%=po.getTime()%></td>
					</tr>
						<%
						}
					}
				}
			}
			if( isLbTableStart ){
				%>
				</tbody>
			</table>
			<% 
			} 
			if(isWrapperInserted == false && isLbTableStart == false) {
				%>
			<div class="bigpadding"> 从<span class="resultTime"><%=standardSdf.format(startTime)%></span>到<span class="resultTime"> <%=standardSdf.format(endTime)%></span>时间段没有报警数据</div>
				<%
			}
			%>
		</div>
	</div>
	<!-- end taobaoapp -->
</div>
<!-- end containner -->
</body>
</html>
