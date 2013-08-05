<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.alarm.source.artoo.ArtooPo"%>
<%@page import="com.taobao.monitor.alarm.source.artoo.ArtooAo"%>
<%@page import="com.taobao.monitor.alarm.source.change.ChangeFreePo"%>
<%@page import="com.taobao.monitor.alarm.source.change.ChangeFreeAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.alarm.source.constants.CommonConstants"%>
<%@page import="com.taobao.util.CollectionUtil"%>
<%@page import="com.taobao.monitor.web.util.DateFormatUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.util.DateFormatUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    int appId = Integer.parseInt(request.getParameter("appId"));
	String startTimeString = request.getParameter("startTime");
	String endTimeString = request.getParameter("endTime");
	Date startTime, endTime;
	if(StringUtils.isBlank(startTimeString) || StringUtils.isBlank(endTimeString)){
		endTime = new Date();
		startTime = DateFormatUtil.getTime(endTime,CommonConstants.TIME_TYPE, CommonConstants.STARTTIME_BEFORE);
	} else {
		startTime = DateFormatUtil.stringToDate(startTimeString);
		endTime = DateFormatUtil.stringToDate(endTimeString);
	}
	
	Map<String, Integer> appName2IdMap = new HashMap<String, Integer>();
	if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tradeplatform")) {
		appName2IdMap = AppInfoAo.get().getTradeplatformRelateAppMap();
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_buy")) {
		appName2IdMap = AppInfoAo.get().getTfBuyRelateAppMap();
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tf_tm")) {
		appName2IdMap = AppInfoAo.get().getTfTmRelateAppMap();
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("cart")) {
		appName2IdMap = AppInfoAo.get().getCartRelateAppMap();
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("tradeapi")) {
		appName2IdMap = AppInfoAo.get().getTradeapiRelateAppMap();
	} else if (appId == AppInfoAo.get().getAppInfoName2IdMap().get("auctionplatform")) {
		appName2IdMap = AppInfoAo.get().getAuctionplatformRelateAppMap();
	} else {
		appName2IdMap = AppInfoAo.get().getTradeRelateAppMap();
	}
	
	HashMap<String,Integer> changeFreeIds  = new HashMap<String,Integer>();
	changeFreeIds.putAll(appName2IdMap);
	changeFreeIds.put("mysql_taobao",100000000);
	boolean hasArtoo = false;
	boolean hasChangeFree = false;
	
	for(Map.Entry<String,Integer> entry:appName2IdMap.entrySet()){
		List<ArtooPo> artooList = ArtooAo.get().findArtooPoListByAppNameAndTime(entry.getKey(), startTime, endTime);
		if(CollectionUtil.isNotEmpty(artooList)){
				if(!hasArtoo){
				%>
<div id="artoo">
	<table id="attable" cellpadding="0" cellspacing="0">
		<tr><td colspan="8" align="center">Ӧ�÷�����</td></tr>
		<tr>
			<td>Ӧ������</td>
			<td>����ʱ��</td>
			<td>��������</td>
			<td>״̬</td>
			<td>��������</td>
			<td>������</td>
			<td>������Դ</td>
			<td>���ʱ��</td>
		</tr>
				<%
					hasArtoo = true;
				}
				for(ArtooPo po:artooList){
					%>
		<tr class="redvalue">
			<td><a href="http://artoo2.taobao.net:9999/artoo/deployPlanDetail.htm?deployPlanId=<%=po.getId()%>&showType=unfinish" target="_blank"><%=po.getAppName() %></a></td>
			<td><%=po.getDeployTime()%></td>
			<td><%=po.getPlanType()%></td>
			<td><%=po.getState()%></td>
			<td><%=po.getCompleteServerNum()%>/<%=po.getTotalServerNum()%></td>
			<td><%=po.getCreator()%></td>
			<td><%=po.getCallSystem()%></td>
			<td><%=po.getFinishTime()%></td>
		</tr>
		<%
				}
				
		}
	}
	if(hasArtoo){
%>
	</table>
</div>
<%
	}
	for(Map.Entry<String,Integer> entry:changeFreeIds.entrySet()){
		List<ChangeFreePo> changeFreeList = ChangeFreeAo.get().getChangeFreeInfo(endTime, entry.getKey());
		if(changeFreeList != null && changeFreeList.size() >0 ){
			if(!hasChangeFree){
				%>
<div id="changefree">
	<table id="cftable" cellpadding="0" cellspacing="0">
	<tr><td>Ӧ������</td><td>����</td><td>�������</td><td>���ִ����</td><td>�����ʼʱ��</td><td>�������ʱ��</td></tr>
				<%
				hasChangeFree = true;
			}
			for(ChangeFreePo po:changeFreeList){
				%>
				<tr>
					<td><%=entry.getKey() %></td>
					<td><a href="http://changefree.corp.taobao.com/v2_changefree/app/index.php/showOrderPage/<%=po.getId()%>" target="_blank"><%=po.getTitle() %></a></td>
					<td><%=po.getChangeType() %></td>
					<td><%=po.getUserName()%></td>
					<td><%=po.getStartTime() %></td>
					<td><%=po.getEndTime() %></td>
				</tr>
				<%
			}
		}
	}
	if(hasChangeFree){
%>
	</table>
</div>
<%
	}
	if(hasArtoo || hasChangeFree){
		%>
<div id="datahide" onClick="hideArtooAndChangefree(this); return false;"></div>
<div id="datashow" onClick="showArtooAndChangefree(this); return false;"></div>
		<%
	}
%>