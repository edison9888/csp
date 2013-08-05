<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-告警历史记录</title>
<style type="text/css">
div {
	font-size: 12px;
}

table {
	margin: 1em 1em 1em 1em;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

img {
cursor:pointer;
}


</style>
</head>
<body>
<jsp:include page="report_head.jsp"></jsp:include>


<%

String appIdStr = request.getParameter("appIds");
String[] appIdArray = null;
if(appIdStr != null && !appIdStr.equals("")){
	appIdArray = appIdStr.split(",");
}else{
	appIdArray = new String[]{};
}
List<AppInfoPo> appIds = new ArrayList<AppInfoPo>();

List<AppInfoPo> listApp = AppInfoAo.get().findAllAppInfo();


for(AppInfoPo appInfoPo:listApp){
	String appName = appInfoPo.getAppName();
	String appId = appInfoPo.getAppId()+"";
	for(String app_id:appIdArray){
		if(app_id.equals(appId)){
			appIds.add(appInfoPo);
			break;
		}
	}
}


SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String currentDay = request.getParameter("currentDay");
if(currentDay==null){
	java.util.Calendar cal = java.util.Calendar.getInstance();
	cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
	currentDay = sdf.format(cal.getTime());
}

%>

<%
for(AppInfoPo appid:appIds) {
	long num = MonitorAlarmAo.get().countAlarmKeyNum(appid.getAppName());
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" > <%=appid.getAppName() %>(设置告警key数量:<%=num %>)</font></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" width="100%" class="ui-widget ui-widget-content" style="font-size: 12px;">   
  <%if(num>0){ %> 
  <tr class="ui-widget-header ">
    <td width="200" align="center">出现告警key名称</td>
    <td width="400" align="center">告警分布</td>
    <td width="200" align="center">次数汇总</td>    
    <td width="200" align="center">查看历史告警</td>
  </tr>
  <%
  List<AlarmRecordPo> list = MonitorAlarmAo.get().getAppAlarmCountByDate(appid.getAppId(),sdf.parse(currentDay));
  if(list.size()>0){
	  for(AlarmRecordPo po:list){
		  int sum = 0;
	  %>
		<tr>
	    	<td align="center"><%=po.getAlarmKeyName() %></td>
	    	<td align="center">
	    		<%
	    		Map<String,Integer> siteMap = po.getSiteMap();
	    		for(Map.Entry<String,Integer> siteEntry:siteMap.entrySet()){
	    			sum+=siteEntry.getValue();
	    			%>
	    			<%=siteEntry.getKey() %>:<font color="red"><%=siteEntry.getValue().toString() %></font>次<br/>
	    			<%
	    		}
	    		%>
			</td>
			<td align="center">
	    		<%=sum+"" %>
			</td>
	   	 	<td align="center"><a href="http://cm.taobao.net:9999/monitorstat/alarm/alarm_record_flash.jsp?appId=<%=appid.getAppId()+""%>&keyId=<%=po.getAlarmkeyId()+""%>" target="_blank">查看历史告警</a>&nbsp;&nbsp;<a href="http://cm.taobao.net:9999/monitorstat/alarm/alarm_record_flash_pie.jsp?appId=<%=appid.getAppId()+""%>" target="_blank">查看告警分布</a></td>
	  	</tr>	
	  <%
	  }
   }else{
	%>
   <tr>
    <td colspan="4" align="center">相安无事。<a href="http://cm.taobao.net:9999/monitorstat/alarm/manage_key.jsp?appId=<%=appid.getAppId()+""%>" target="_blank" style="color:green">点击这里添加更多的告警点</a></td>
   </tr> 
	
  <%	  
  }}else{
  %>
     <tr>
    <td colspan="4" align="center">应用还没有设置告警,<a href="http://cm.taobao.net:9999/monitorstat/alarm/manage_key.jsp?appId=<%=appid.getAppId()+""%>" target="_blank" style="color:green">点击这里添加更多的告警点</a></td>
   </tr> 
  <%
  }
  %>  
</table>
</div>
</div>
<%} %>
<jsp:include page="report_buttom.jsp"></jsp:include>
</body>
</html>