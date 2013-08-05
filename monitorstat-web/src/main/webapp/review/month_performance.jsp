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
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>CSP性能数据统计</title>

<%
// 年份，如2012、2011
String searchDate = request.getParameter("searchDate");

if(searchDate==null){
	searchDate = "2011";
}
Calendar cal = Calendar.getInstance();
cal.set(Calendar.YEAR, Integer.parseInt(searchDate));
cal.set(Calendar.HOUR_OF_DAY, 0);
cal.set(Calendar.MINUTE, 0);
cal.set(Calendar.SECOND, 0);

String opsNameStr = request.getParameter("opsNameStr");
List<AppInfoPo> appList = new ArrayList<AppInfoPo>();
if (opsNameStr == null) {
	appList.add(AppInfoAo.get().getAppInfoByOpsName("detail"));
} else {
	String[] opsArray = opsNameStr.split("\\$");
	for (String str : opsArray) {
		appList.add(AppInfoAo.get().getDayAppByOpsName(str));
	}
}

int keyId = 16929;
%>
</head>
<body>
<center><h2>PV数据月份汇总统计（CSP）（<%=searchDate %>）</h2></center>
<table width="100%" border="1">
	<tr>
		<td align="center">应用名</td>
		<td align="center">一月</td>
		<td align="center">二月</td>
		<td align="center">三月</td>
		<td align="center">四月</td>
		<td align="center">五月</td>
		<td align="center">六月</td>
		<td align="center">七月</td>
		<td align="center">八月</td>
		<td align="center">九月</td>
		<td align="center">十月</td>
		<td align="center">十一月</td>
		<td align="center">十二月</td>
	</tr>
	<%
	for (AppInfoPo app : appList) {
		if (app == null) {
			continue;
		}
		String opsName = app.getOpsName();
		%>
		<tr>
			<td><%=opsName %></td>
			<%
			int appId = (Integer)app.getAppDayId() != null ? app.getAppDayId() : app.getAppId();
			int maxDay;
			Date begin;
			Date end;
			List<KeyValuePo> keyList;
			long sumPv = 0;
			int size = 0;
			for (int i = 0; i < 12; i++) {
				size = 0;
				sumPv = 0;
				cal.set(Calendar.MONTH, i);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				begin = cal.getTime();
				maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				cal.set(Calendar.DAY_OF_MONTH, maxDay);
				end = cal.getTime();
				keyList = MonitorDayAo.get().findMonitorCountByDate(appId, keyId, begin, end);
				for (KeyValuePo po : keyList) {
					long ii = Long.parseLong(po.getValueStr());
					if (ii > 0) {
						sumPv += ii;
						size++;
					}
				}
				if (size == 0) {
					%>
					<td>无</td>
					<%	
				} else {
					%>
					<td><%=Utlitites.fromatLong((sumPv/size)*maxDay+"") %></td>
					<%
				}
			}
			%>
		</tr>
	<%
	}
	%>
</table>
</body>
</html>