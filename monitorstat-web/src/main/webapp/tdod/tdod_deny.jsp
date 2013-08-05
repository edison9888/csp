<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.web.tdod.TdodVo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTdodAo"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.cache.IpRegionCache"%>
<%@page import="com.taobao.monitor.web.cache.IpRegion"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>TMD Deny 汇总</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<%
String searchDate = request.getParameter("searchDate");
String huanbiDate = "";
if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
	huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);
} else {
	huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);
}
Map<String, TdodVo> appTdodMap = new HashMap<String, TdodVo>();;
Map<String, TdodVo> huanbiAppTdodMap = new HashMap<String, TdodVo>();
String opsNameStr = request.getParameter("opsNameStr");
if (opsNameStr == null) {
	appTdodMap = MonitorTdodAo.getInstance().findAllTdodByDate(searchDate);
	huanbiAppTdodMap = MonitorTdodAo.getInstance().findAllTdodByDate(huanbiDate);
} else {
	String[] opsArray = opsNameStr.split("\\#");
	 Set<String> set = new HashSet<String>(Arrays.asList(opsArray));  
	 appTdodMap = MonitorTdodAo.getInstance().findAllTdodByAppList(set, searchDate);
}
if (appTdodMap == null) {
	out.print("获取的结果为空");
	return;
}

if (huanbiAppTdodMap == null) {
	out.print("昨天的结果为空");
	huanbiAppTdodMap = new HashMap<String, TdodVo>();
}

Comparator<Map.Entry<String, TdodVo>> outerCompare = new Comparator<Map.Entry<String, TdodVo>>(){
	public int compare(Map.Entry<String, TdodVo> e1,Map.Entry<String, TdodVo> e2){
		if (e2.getValue().getIpSumCount() > e1.getValue().getIpSumCount()) {
			return 1;
		} else if (e2.getValue().getIpSumCount() < e1.getValue().getIpSumCount()) {
			return -1;
		} else {
			return 0;
		}
	}
};

List<Map.Entry<String, TdodVo>> voMapList = new ArrayList<Map.Entry<String, TdodVo>>(appTdodMap.entrySet());
Collections.sort(voMapList, outerCompare);

Comparator<Map.Entry<String, Integer>> innerCompare = new Comparator<Map.Entry<String, Integer>>(){
	public int compare(Map.Entry<String, Integer> e1,Map.Entry<String, Integer> e2){
		return (int)(e2.getValue() - e1.getValue());
	}
};

long ipSumKind = 0l;
long ipSumCount = 0l;
for (Map.Entry<String, TdodVo> entry : appTdodMap.entrySet()) {
	ipSumKind += entry.getValue().getIpKindCount();
	ipSumCount += entry.getValue().getIpSumCount();
}
%>
<script type="text/javascript">
function goToAppDetail() {
	var searchDate = $("#datepicker").val();
	location.href="tdod_deny.jsp?searchDate="+searchDate;
	//window.open("tdod_deny.jsp?searchDate="+searchDate);
}
</script>
</head>
<body>
	<center>
		日期：
		<input type="text" id="datepicker" value="<%=searchDate%>"/>
		<button  onclick="goToAppDetail()">查看详细</button>
	<h2>TMD Deny 汇总（<%=searchDate %>）</h2>
	<table border="1">
		<tr class="ui-widget-header">
			<td rowspan="2" width="200" align="center">应用名</td>
			<td>拦截IP种数（UV）</td>
			<td>拦截IP次数（PV）</td>
			<td rowspan="2">汇总PV（CSP）</td>
			<td rowspan="2">拦截比例</td>
			<td colspan="2" width="400" align="center">被拦截前5位的IP</td>
		</tr>
		<tr class="ui-widget-header">
			<td>总共：<%=Utlitites.fromatLong(ipSumKind+"") %></td>
			<td>总共：<%=Utlitites.fromatLong(ipSumCount+"") %></td>
			<td align="center">IP</td>
			<td align="center">拦截次数</td>
		</tr>
		<%
		for (Map.Entry<String, TdodVo> entry : voMapList) {
			String appName = entry.getKey();
			AppInfoPo appPo = AppInfoAo.get().getDayAppByOpsName(appName);
			TdodVo vo = entry.getValue();
			
			TdodVo huanbiVo = huanbiAppTdodMap.get(appName);
			if (huanbiVo == null) {
				huanbiVo = new TdodVo();
			}
			
			Map<String, Integer> ipCountMap = vo.getIpCountMap();
			List<Map.Entry<String,Integer>> ipCountList = new ArrayList<Map.Entry<String, Integer>>(ipCountMap.entrySet());
			Collections.sort(ipCountList,innerCompare);
			int i = ipCountList.size();
			if (i > 5) {
				i = 5;
			}
			
			int appId = (Integer)appPo.getAppDayId() != null ? appPo.getAppDayId() : appPo.getAppId();
			int keyId = 16929;
			String cspPv = MonitorDayAo.get().findKeyValueFromCountByDate(16929, appId, searchDate).getValueStr();
			String huanbiCspPv = MonitorDayAo.get().findKeyValueFromCountByDate(16929, appId, huanbiDate).getValueStr();
		%>
		<tr>
			<td rowspan="<%=i %>" align="center"><%=appName %></td>
			<td rowspan="<%=i %>"><%=vo.getIpKindCount() %><%=Utlitites.scale(vo.getIpKindCount(), huanbiVo.getIpKindCount()) %></td>
			<td rowspan="<%=i %>"><%=Utlitites.fromatLong(vo.getIpSumCount()+"") %><%=Utlitites.scale(vo.getIpSumCount(), huanbiVo.getIpSumCount()) %></td>
		<%
		if (cspPv == null) {
			%>
			<td rowspan="<%=i %>">-</td>
			<td rowspan="<%=i %>">-</td>
			<%
		} else {
			%>
			<td rowspan="<%=i %>"><%=Utlitites.fromatLong(cspPv) %><%=Utlitites.scale(cspPv, huanbiCspPv) %></td>
			<td rowspan="<%=i %>"><%=Utlitites.formatDotTwo(vo.getIpSumCount()*100/Double.parseDouble(cspPv))%>%</td>
			<%
		}
		StringBuilder sb = new StringBuilder();
		IpRegion ipRegion = IpRegionCache.getIpRegion(ipCountList.get(0).getKey());
		if (ipRegion == null) {
			sb.append("\n(-)");
		} else {
			sb.append("\n(" + ipRegion.getProvince());
			if (!ipRegion.getProvince().equals(ipRegion.getCity())) {
				sb.append("-" + ipRegion.getCity());
			}
			sb.append(":" + ipRegion.getNetwork() + ")");
		}
		%>
			<td align="center"><%=ipCountList.get(0).getKey() %><%=sb.toString() %></td>
			<td align="center"><%=Utlitites.fromatLong(ipCountList.get(0).getValue()+"") %></td>
		</tr>
		<%
			for (int j = 1; j < i; j++) {
				StringBuilder sb1 = new StringBuilder();
				IpRegion ipRegion1 = IpRegionCache.getIpRegion(ipCountList.get(j).getKey());
				if (ipRegion1 == null) {
					sb1.append("\n(-)");
				} else {
					sb1.append("\n(" + ipRegion1.getProvince());
					if (!ipRegion1.getProvince().equals(ipRegion1.getCity())) {
						sb1.append("-" + ipRegion1.getCity());
					}
					sb1.append(":" + ipRegion1.getNetwork() + ")");
				}
			%>
			<tr>
				<td align="center"><%=ipCountList.get(j).getKey() %><%=sb1.toString() %></td>
				<td align="center"><%=Utlitites.fromatLong(ipCountList.get(j).getValue()+"") %></td>
			</tr>
			<%
			}
		}
		%>
	</table>
	</center>
</body>
</html>