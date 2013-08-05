<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.Map"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>CSP性能数据统计</title>

<%
String searchDate = request.getParameter("searchDate");

if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}

String opsNameStr = request.getParameter("opsNameStr");
List<AppInfoPo> appList = new ArrayList<AppInfoPo>();
if (opsNameStr == null) {
	appList = AppInfoAo.get().findAllDayApp();
} else {
	String[] opsArray = opsNameStr.split("\\$");
	for (String str : opsArray) {
		appList.add(AppInfoAo.get().getDayAppByOpsName(str));
	}
}

String returnType = request.getParameter("returnType");
if(returnType == null){
	returnType = "html";
}

long[] keyArray = {50832, 50831, 124165, 124166, 139574, 139575, 4510, 124222, 124250, 124223, 124251, 27735, 986, 989, 16930, 16929};
%>
</head>
<body>
<%
if (returnType.equalsIgnoreCase("html")) {
%>
<center><h2>CSP监控应用性能数据汇总（<%=searchDate %>）</h2></center>
<table width="100%" border="1">
	<tr>
		<td rowspan="2" align="center">应用名</td>
		<td colspan="5" align="center">CM3</td>
		<td colspan="5" align="center">CM4</td>
		<td colspan="6" align="center">全网</td>
	</tr>
	<tr>
		<td align="center">PV</td>
		<td align="center">QPS</td>
		<td align="center">RT(mm)</td>
		<td align="center">HSF-PV</td>
		<td align="center">HSF-QPS</td>
		<td align="center">PV</td>
		<td align="center">QPS</td>
		<td align="center">RT(mm)</td>
		<td align="center">HSF-PV</td>
		<td align="center">HSF-QPS</td>
		<td align="center">PV</td>
		<td align="center">RT(mm)</td>
		<td align="center">HSF-RT(mm)</td>
		<td align="center">CPU</td>
		<td align="center">LOAD</td>
		<td align="center">YGC</td>
	</tr>
	<%
	for (AppInfoPo app : appList) {
		if (app == null) {
			continue;
		}
		String opsName = app.getOpsName();
		int appId = (Integer)app.getAppDayId() != null ? app.getAppDayId() : app.getAppId();
		Map<Long, String> dataMap = MonitorDayAo.get().findMonitorCountStrMapAsValueByDate(appId, searchDate, keyArray);
	%>
	<tr>
		<td><%=opsName %></td>
		<td><%=Utlitites.fromatLong(updateData(dataMap.get(50832L))) %></td>
		<td><%=updateData(dataMap.get(124166L)) %></td>
		<td><%=updateData3(dataMap.get(139575L)) %></td>
		<td><%=Utlitites.fromatLong(updateData(dataMap.get(124222L))) %></td>
		<td><%=updateData(dataMap.get(124223L)) %></td>
		<td><%=Utlitites.fromatLong(updateData(dataMap.get(50831L))) %></td>
		<td><%=updateData(dataMap.get(124165L)) %></td>
		<td><%=updateData3(dataMap.get(139574L)) %></td>
		<td><%=Utlitites.fromatLong(updateData(dataMap.get(124250L))) %></td>
		<td><%=updateData(dataMap.get(124251L)) %></td>
		<td><%=Utlitites.fromatLong(updateData(dataMap.get(16929L))) %></td>
		<td><%=updateData3(dataMap.get(16930L)) %></td>
		<td><%=updateData(dataMap.get(27735L)) %></td>
		<td><%=updateData2(dataMap.get(986L)) %></td>
		<td><%=updateData2(dataMap.get(989L)) %></td>
		<td><%=updateData(dataMap.get(4510L)) %></td>
	</tr>
	<%
	}
	%>
</table>
<%
} else if (returnType.equalsIgnoreCase("json")) {
	List<PerfData> resultList = new ArrayList<PerfData>();
	for (AppInfoPo app : appList) {
		if (app == null) {
			continue;
		}
		String opsName = app.getOpsName();
		int appId = (Integer)app.getAppDayId() != null ? app.getAppDayId() : app.getAppId();
		Map<Long, String> dataMap = MonitorDayAo.get().findMonitorCountStrMapAsValueByDate(appId, searchDate, keyArray);
		PerfData perfData = new PerfData();
		perfData.setAppName(opsName);
		perfData.setCm3Pv(Utlitites.fromatLong(updateData(dataMap.get(50832L))));
		perfData.setCm3Qps(updateData(dataMap.get(124166L)));
		perfData.setCm3RT(updateData3(dataMap.get(139575L)));
		perfData.setCm3HsfPv(Utlitites.fromatLong(updateData(dataMap.get(124222L))));
		perfData.setCm3HsfQps(updateData(dataMap.get(124223L)));
		perfData.setCm4Pv(Utlitites.fromatLong(updateData(dataMap.get(50831L))));
		perfData.setCm4Qps(updateData(dataMap.get(124165L)));
		perfData.setCm4RT(updateData3(dataMap.get(139574L)));
		perfData.setCm4HsfPv(Utlitites.fromatLong(updateData(dataMap.get(124250L))));
		perfData.setCm4HsfQps(updateData(dataMap.get(124251L)));
		perfData.setAllPv(Utlitites.fromatLong(updateData(dataMap.get(16929L))));
		perfData.setAllRT(updateData3(dataMap.get(16930L)));
		perfData.setAllHsfRT(updateData(dataMap.get(27735L)));
		perfData.setAllCpu(updateData2(dataMap.get(986L)));
		perfData.setAllLoad(updateData2(dataMap.get(989L)));
		perfData.setAllYgc(updateData(dataMap.get(4510L)));
		resultList.add(perfData);
	}
	JSONArray json = JSONArray.fromObject(resultList);
	response.setContentType("text/html;charset=gbk"); 
	try {
		response.getWriter().write(json.toString());
		response.flushBuffer();
	} catch (Exception e) {
	}	
} else {
%>
请求类型不正确
<%	
}
%>

<%!
public String updateData(String str) {
	if(str==null || str.equalsIgnoreCase("null")) {
		return "0";
	} else {
		return str;
	}
}

public String updateData2(String str) {
	if(str==null || str.equalsIgnoreCase("null")) {
		return "0";
	} else {
		return Utlitites.formatDotTwo(str);
	}
}

public String updateData3(String str) {
	if(str==null || str.equalsIgnoreCase("null") || str.indexOf("-") > -1) {
		return "0";
	} else {
		return 	Utlitites.formatDotTwo(Double.parseDouble(str)/1000);
	}
}

public class PerfData {
	String appName;
	String cm3Pv;
	String cm3Qps;
	String cm3RT;
	String cm3HsfPv;
	String cm3HsfQps;
	String cm4Pv;
	String cm4Qps;
	String cm4RT;
	String cm4HsfPv;
	String cm4HsfQps;
	String allPv;
	String allRT;
	String allHsfRT;
	String allCpu;
	String allLoad;
	String allYgc;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCm3Pv() {
		return cm3Pv;
	}
	public void setCm3Pv(String cm3Pv) {
		this.cm3Pv = cm3Pv;
	}
	public String getCm3Qps() {
		return cm3Qps;
	}
	public void setCm3Qps(String cm3Qps) {
		this.cm3Qps = cm3Qps;
	}
	public String getCm3RT() {
		return cm3RT;
	}
	public void setCm3RT(String cm3rt) {
		cm3RT = cm3rt;
	}
	public String getCm3HsfPv() {
		return cm3HsfPv;
	}
	public void setCm3HsfPv(String cm3HsfPv) {
		this.cm3HsfPv = cm3HsfPv;
	}
	public String getCm3HsfQps() {
		return cm3HsfQps;
	}
	public void setCm3HsfQps(String cm3HsfQps) {
		this.cm3HsfQps = cm3HsfQps;
	}
	public String getCm4Pv() {
		return cm4Pv;
	}
	public void setCm4Pv(String cm4Pv) {
		this.cm4Pv = cm4Pv;
	}
	public String getCm4Qps() {
		return cm4Qps;
	}

	public void setCm4Qps(String cm4Qps) {
		this.cm4Qps = cm4Qps;
	}
	public String getCm4RT() {
		return cm4RT;
	}
	public void setCm4RT(String cm4rt) {
		cm4RT = cm4rt;
	}
	public String getCm4HsfPv() {
		return cm4HsfPv;
	}
	public void setCm4HsfPv(String cm4HsfPv) {
		this.cm4HsfPv = cm4HsfPv;
	}
	public String getCm4HsfQps() {
		return cm4HsfQps;
	}
	public void setCm4HsfQps(String cm4HsfQps) {
		this.cm4HsfQps = cm4HsfQps;
	}
	public String getAllPv() {
		return allPv;
	}
	public void setAllPv(String allPv) {
		this.allPv = allPv;
	}
	public String getAllRT() {
		return allRT;
	}
	public void setAllRT(String allRT) {
		this.allRT = allRT;
	}
	public String getAllHsfRT() {
		return allHsfRT;
	}
	public void setAllHsfRT(String allHsfRT) {
		this.allHsfRT = allHsfRT;
	}
	public String getAllCpu() {
		return allCpu;
	}
	public void setAllCpu(String allCpu) {
		this.allCpu = allCpu;
	}
	public String getAllLoad() {
		return allLoad;
	}
	public void setAllLoad(String allLoad) {
		this.allLoad = allLoad;
	}
	public String getAllYgc() {
		return allYgc;
	}
	public void setAllYgc(String allYgc) {
		this.allYgc = allYgc;
	}
}
%>
</body>
</html>