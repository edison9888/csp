<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控-告警接收管理页面</title>
<style type="text/css">
div {
	font-size: 12px;
}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}

</style>
</head>
<body>
<%


String ids = request.getParameter("appIds");

String[] appIds = ids.split(",");

for(String appId:appIds){

String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");

Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");


if(startTime==null){
	startTime = sdf.format(cal.getTime());
};
cal.add(Calendar.DAY_OF_MONTH,1);
if(endTime==null){
	endTime = sdf.format(cal.getTime());
}


List<AlarmRecordPo> list = MonitorAlarmAo.get().findAllExceptionMonitorDataDesc(Integer.parseInt(appId),sdf.parse(startTime), sdf.parse(endTime));
if(list ==null){
	list = new ArrayList<AlarmRecordPo>();
}
List<AlarmRecordPo> filterList = list;

Set<String> siteNumSet = new HashSet<String>();
%>
<%
Map<String,Integer> exceptionMap = new HashMap<String,Integer>();
for(AlarmRecordPo po:filterList){
	String siteName = po.getSiteName();
	siteNumSet.add(siteName);
	
	Integer num = exceptionMap.get(po.getAlarmKeyName());
	if(num == null){
		exceptionMap.put(po.getAlarmKeyName(),Integer.parseInt(po.getAlarmValue()));
	}else{
		exceptionMap.put(po.getAlarmKeyName(),Integer.parseInt(po.getAlarmValue())+num);
	}
}

%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="center" colspan="2">监控应用：<%=AppCache.get().getKey(Integer.parseInt(appId)).getAppName() %>-->监控机器(<%=siteNumSet %>)</td>		
	</tr>
	<tr class="ui-widget-header ">
		<td align="center">异常名称</td>
		<td align="center">每台机器平均次数</td>
	</tr>
	<%for(Map.Entry<String,Integer> po:exceptionMap.entrySet()){
	%>
	<tr>
		<td align="center"><%=po.getKey() %></td>
		<td align="center"><%=po.getValue()/siteNumSet.size() %></td>
	</tr>
	<%} %>
</table>
<%} %>
</body>
</html>