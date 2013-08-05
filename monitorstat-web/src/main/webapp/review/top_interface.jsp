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
<%@page import="java.util.Arrays"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.web.vo.MonitorVo"%>
<%@page import="com.taobao.monitor.web.vo.HsfPo"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.vo.HsfClass"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>应用主机列表</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript">
function openKeyDetail(appId,keyId,collectTime){
	$("#iframe_report").attr("src","key_time_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);
	$("#dialog_report").dialog("open")
	//window.open("key_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);	
}
</script>
<%
String searchDate = request.getParameter("searchDate");

if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}

String opsNameStr = request.getParameter("opsNameStr");
List<String> appIdList = new ArrayList<String>();
if (opsNameStr == null) {
	appIdList.add("detail");
	appIdList.add("shopsystem");
	appIdList.add("tradeplatform");
	appIdList.add("tbuic");
	appIdList.add("itemcenter");
	appIdList.add("login");
	appIdList.add("tf_tm");
	appIdList.add("tf_buy");
	appIdList.add("ump");
	appIdList.add("servicecenter");
	appIdList.add("cart");
	
} else {
	String[] opsArray = opsNameStr.split("\\$");
	appIdList = Arrays.asList(opsArray);
}

%>

</head>
<body>
<center><h2>核心应用关键key报警情况（<%=searchDate %>）</h2></center>
<br></br>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
<font color="#FF0000"> = HSF= </font>
</div>
<%
for (String opsName : appIdList) {
	
	AppInfoPo appInfo = AppInfoAo.get().getDayAppByOpsName(opsName);
	Integer selectAppId = appInfo.getAppDayId();
	if (selectAppId == null) {
		selectAppId = appInfo.getAppId();
	}
	
	Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId,searchDate);
	
	List<AlarmDataPo> allalarmKeyList = MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(selectAppId,"");
	
	HashSet<String> alarmKeySet = new HashSet<String>();
	for (AlarmDataPo alarmPo : allalarmKeyList) {
		alarmKeySet.add(alarmPo.getKeyName());
	}
	MonitorVo vo = map.get(selectAppId);
	if(vo.getOutHsfList().size()>0) {
	%>
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon">
				<td align="left" colspan="19" width="100%"><font color="#000000" size="2">[ 应用：<%=opsName %> - OUT ]</font></td>
			</tr>	
			<tr class="ui-widget-header ">
			  <td width="120" align="center">类名</td>
			  <td width="80" align="center">方法名</td>
			  <td align="center">执行次数</td>
			  <td align="center">告警</td>
			  <td align="center">平均执行时间(ms)</td>
			  <td align="center">告警</td>
		  	</tr>
			<%
				List<HsfPo> currentHsfList = vo.getOutHsfList();
				
				Collections.sort(currentHsfList);
				
				int limit = currentHsfList.size();
				if (limit > 20) {
					limit = 20;
				}
				for (int i = 0; i < limit; i++) {
					try {
						boolean aa = false;
						boolean bb = false;
						HsfPo po = currentHsfList.get(i);
						if (po.getExeCountNum() < 100000) {
							continue;
						}
						String keyCount ="OUT_HSF-Consumer_" + po.getClassName()+"_"+po.getMethodName()+"_"+Constants.COUNT_TIMES_FLAG;
						String keyTime = "OUT_HSF-Consumer_" + po.getClassName()+"_"+po.getMethodName()+"_"+Constants.AVERAGE_USERTIMES_FLAG;
						KeyPo countKeyPo = KeyAo.get().getKeyByName(keyCount);
						KeyPo timeKeyPo = KeyAo.get().getKeyByName(keyTime);
						
						if (alarmKeySet.contains(keyCount)) {
							aa = true;
						}
						if (alarmKeySet.contains(keyTime)) {
							bb = true;
						}
					%>					
					<tr>
						<td align="left" style="font-size:12px"><%=po.getClassName() %></td>
						<td align="left" style="font-size:12px"><%=po.getHtmlMethodName() %></td>
						<td align="left"  style="font-size:10px"><a target="_blank" href="../key_time_detail.jsp?appId=<%=selectAppId %>&keyId=<%=countKeyPo.getKeyId() %>&collectTime=<%=searchDate %>"><img src="<%=request.getContextPath () %>/statics/images/report.png"></a><%=Utlitites.fromatLong(po.getExeCount()) %></td>
						<%
						if (aa) {
						%>
						<td align="left"  style="font-size:10px">是</td>
						<%
						} else {
						%>
						<td align="left"  style="font-size:10px"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/alarm/add_alarm_key.jsp?keyId=<%=countKeyPo.getKeyId() %>&appId=<%=appInfo.getAppId() %>"><font color="red">否</font></a></td>
						<%	
						}
						%>
						<td align="left"  style="font-size:10px"><a target="_blank" href="../key_time_detail.jsp?appId=<%=selectAppId %>&keyId=<%=timeKeyPo.getKeyId() %>&collectTime=<%=searchDate %>"><img src="<%=request.getContextPath () %>/statics/images/report.png"></a><%=po.getAverageExe() %></td>
						<%
						if (bb) {
						%>
						<td align="left"  style="font-size:10px">是</td>
						<%
						} else {
						%>
						<td align="left"  style="font-size:10px"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/alarm/add_alarm_key.jsp?keyId=<%=timeKeyPo.getKeyId() %>&appId=<%=appInfo.getAppId() %>"><font color="red">否</font></a></td>
						<%	
						}
						%>
						
					</tr>					
					<%
					} catch(Exception e) {
						e.printStackTrace();
						System.out.println("");
					}
				}
				%>
		</table>
		
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="headcon">
				<td align="left" colspan="18" width="100%"><font color="#000000" size="2">[ 应用：<%=opsName %> - IN ]</font></td>
			</tr>	
			<tr class="ui-widget-header ">	  
			  <td width="80" align="center">类名</td>
			  <td width="80" align="center">方法名</td>
			  <td align="center">执行次数</td>
			   <td align="center">告警</td>
			  <td align="center">平均执行时间(ms)</td>
			  <td align="center">告警</td>
		  	</tr>
			<%
				
				Map<String, List<HsfClass>> sortOutMap = vo.getSortInHsfMap1();		
				
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
							
						try{	
							boolean aa = false;
							boolean bb = false;
							if (Long.parseLong(po.getExeCount()) < 100000) {
								continue;
							}
							String keyCount ="IN_HSF-ProviderDetail_" + po.getClassName()+"_"+po.getMethodName()+"_"+Constants.COUNT_TIMES_FLAG;
							String keyTime = "IN_HSF-ProviderDetail_" + po.getClassName()+"_"+po.getMethodName()+"_"+Constants.AVERAGE_USERTIMES_FLAG;
							if (alarmKeySet.contains(keyCount)) {
								aa = true;
							}
							
							if (alarmKeySet.contains(keyTime)) {
								bb = true;
							}
							
							KeyPo countKeyPo = KeyAo.get().getKeyByName(keyCount);
							KeyPo timeKeyPo = KeyAo.get().getKeyByName(keyTime);
							
						%>					
						<tr>						
							<td align="left" style="font-size:12px"><%=classShow?po.getHtmlClassName():" - " %></td>
							<td align="left" style="font-size:12px"><%=po.getHtmlMethodName() %></td>
							<td align="left"  style="font-size:10px"><a target="_blank" href="../key_time_detail.jsp?appId=<%=selectAppId %>&keyId=<%=countKeyPo.getKeyId() %>&collectTime=<%=searchDate %>"><img src="<%=request.getContextPath () %>/statics/images/report.png"></a><%=Utlitites.fromatLong(po.getExeCount()) %></td>
							<%
							if (aa) {
							%>
							<td align="left"  style="font-size:10px">是</td>
							<%
							} else {
							%>
							<td align="left"  style="font-size:10px"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/alarm/add_alarm_key.jsp?keyId=<%=countKeyPo.getKeyId() %>&appId=<%=appInfo.getAppId() %>"><font color="red">否</font></a></td>
							<%	
							}
							%>
							<td align="left"  style="font-size:10px"><a target="_blank" href="../key_time_detail.jsp?appId=<%=selectAppId %>&keyId=<%=timeKeyPo.getKeyId() %>&collectTime=<%=searchDate %>"><img src="<%=request.getContextPath () %>/statics/images/report.png"></a><%=po.getAverageExe() %></td>
							<%
							if (bb) {
							%>
							<td align="left"  style="font-size:10px">是</td>
							<%
							} else {
							%>
							<td align="left"  style="font-size:10px"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/alarm/add_alarm_key.jsp?keyId=<%=timeKeyPo.getKeyId() %>&appId=<%=appInfo.getAppId() %>"><font color="red">否</font></a></td>
							<%	
							}
							%>
						</tr>					
						<%
							}catch(Exception e){
								e.printStackTrace();
								System.out.println("");
							}
						aimShow = false;
						classShow = false;	
						}
					classShow = true;
					}
					aimShow = true;
				}
				
				%>
		</table>
	<%
	}
}
%>
</div>


</body>
</html>