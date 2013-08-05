<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.time.ao.AppHSFQueryAo"%>
<%@page import="com.taobao.monitor.time.po.AppPvPO"%>
<%@page import="com.taobao.monitor.time.po.SortEntry"%>
<%@page import="com.taobao.monitor.time.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.PropConstants"%>
<%@page import="com.taobao.util.CollectionUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%
	String hsfName = (String)request.getParameter("HSFName");
	String appName = "tradeplatform";
	String keyName;
	if("TpCreate".equals(hsfName)){
		keyName = AppHSFQueryAo.TAOBAO_CREATING_KEY;
	} else if("TmallCreate".equals(hsfName)){
		keyName = AppHSFQueryAo.TMALL_CREATING_KEY;
	} else {
		keyName = AppHSFQueryAo.TAOBAO_PAY_KEY;
	}
	Map<String, List<TimeDataInfo>> hsfInfoMap = AppHSFQueryAo.get().queryAppHSFInterface(appName,keyName);
%>
<table width="100%" class="gradient-style">
	<thead>
	<tr>
		<th>时间</th>
		<th>调用次数</th>
		<th width="40%">服务名称</th>
		<th>应用名称</th>
		<th>查看</th>
	</tr>
	</thead>
	<tbody>
	<%
	if(CollectionUtil.isNotEmpty(hsfInfoMap)){
		for(Map.Entry<String, List<TimeDataInfo>> entry:hsfInfoMap.entrySet()){
			List<TimeDataInfo> tDataInfoList = entry.getValue();
			String serviceName = entry.getKey();
			if(CollectionUtil.isNotEmpty(tDataInfoList)){
				for(TimeDataInfo td : tDataInfoList){
			%>
		<tr>
			<td><%=td.getFtime()%></td>
			<td><%=td.getMainValue()%></td>
			<td><%=td.getKeyName()%></td>
			<td><%=td.getAppName()%></td>
			<td><a href="http://time.csp.taobao.net:9999/time/app/detail/history.do?method=showHistory&appName=tradeplatform&keyName=<%=keyName%>" target="_blank">查看</a></td>
		</tr>
			<%
				}
			}
		}
	}
	%>
	</tbody>
</table>
