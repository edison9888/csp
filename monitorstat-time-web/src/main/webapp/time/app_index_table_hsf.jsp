<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.IndexHSfTable"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.csp.time.cache.CapacityCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>

<%
Map<String,Map<String,IndexHSfTable>> siteMap =(Map<String,Map<String,IndexHSfTable>>)request.getAttribute("siteMap");
List<String> timeList  = (List<String>)request.getAttribute("timeList");

List<String> siteList = new ArrayList<String>();
siteList.addAll(siteMap.keySet());
Collections.sort(siteList);
AppInfoPo appInfo = (AppInfoPo)request.getAttribute("appInfo");
Integer capacity = 0;
Map<String, List<HostPo>> machineMap = new HashMap<String, List<HostPo>>();
if(appInfo != null) {
	List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appInfo.getOpsName());
	machineMap = CspCacheTBHostInfos.get().getHostMapByRoom(appInfo.getOpsName());
	double d = CapacityCache.get().getAppCapacity(appInfo.getAppId());
	capacity = (int) d;	
}

%>
	<div class="row-fluid"  >
		<h5>${appInfo.appName}<a target="_blank" href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex&appId=${appInfo.appId}">应用首页</a>&nbsp;&nbsp;&nbsp; <a href="<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=HSF-provider" target="_blank">总流量历史详情</a>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
								<a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=${appInfo.appId}">每台机器详情</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	压测QPS:<%=capacity%>
	<%
	try {
		if(appInfo != null) {
			String appName = appInfo.getAppName();
			if(appName.equalsIgnoreCase("itemcenter")
					||appName.equalsIgnoreCase("tradeplatform")
					||appName.equalsIgnoreCase("ump")) {
				Map<String, String[]> map = CspCacheTBHostInfos.get().getCopyOfAppToGroupMap();
				if(map.containsKey(appName)) {
					out.println("&nbsp;&nbsp;&nbsp;应用的分组信息:");
					for(String appCombine : map.get(appName)) {
						AppInfoPo groupAppInfo = AppInfoCache.getAppInfoByAppName(appCombine);
						if(groupAppInfo != null) {
							%>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="<%=request.getContextPath()%>/index.do?method=queryIndexTableForGroup&appId=<%=groupAppInfo.getAppId()%>" target="_blank"><%=groupAppInfo.getAppName()%></a>							
							<%							
						}
					}				
				}
			}
		}
		} catch(Exception e) {
			//out.println("分组信息显示出错:" + e.toString());
		}
	%>
	</h5>
		<table width="100%"   class="table table-striped table-bordered table-condensed" >
			<tr>
				<td>&nbsp;</td>
				<%for(String site:siteList){ %>
				<td colspan="8" align="center"><%=site%>
				<% 
					if(machineMap.containsKey(site)) {
						out.println("机器数:" + machineMap.get(site).size());
					}
				%>
				</td>
				<%} %>
			</tr>
			<tr>
				<td>时间</td>
				<%for(String site:siteList){ %>
				<td>每台</td>
				<td>QPS</td>
				<td>PV</td>
				<td title="(SS_Block)"><a href="<%=request.getContextPath()%>/index.do?method=queryIndexTableForBlock&appId=${appInfo.appId}" target="_blank;">block</a></td>
				<td>RT</td>
				<td>Load</td>
				<td>CPU</td>
				<td>内存</td>
				<td>GC</td>
				<%} %>
			</tr>
			<%
			for(String time:timeList){
			%>
			<tr>
				<td><%=time %></td>
				<%
				for(String siteName:siteList){
					Map<String,IndexHSfTable> site = siteMap.get(siteName);
					if(site!= null){
						IndexHSfTable hsf = site.get(time);
						if(hsf != null){
					%>
					<td><%=hsf.getPv() %><%=hsf.getPvRate() %></td>
					<td><%=hsf.getQps()%></td>
					<td><%=hsf.getPvForSite()%></td>
					<td><%=hsf.getSsBlock()%></td>
					<td><%=hsf.getRt()%><%=hsf.getRtRate() %></td>
					<td><%=hsf.getLoad()%><%=hsf.getLoadRate() %></td>
					<td><%=hsf.getCpu()%>%<%=hsf.getCpuRate() %></td>
					<td><%=hsf.getMem()%>%<%=hsf.getMemRate() %></td>
					<td><%=hsf.getGc()%><%=hsf.getGcRate() %></td>					
					<%		
						}else{
							%>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>	
							<%				
						}
					}
				}
				%>
			</tr>
			<%} %>
		</table>
	</div>
