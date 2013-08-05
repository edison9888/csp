<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
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
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>

<%
	SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date current = new Date();
	String collectTime1 = sdf.format(current); 
	//Date collectTime1Date = parseLogFormatDate.parse("2010-04-30 00:00:00") ; 
	//String collectTime1 = sdf.format(collectTime1Date) ; 
	String startDate = sdf.format(current)+" 00:00:00";
	String endDate = sdf.format(current)+" 23:59:59";
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(current);
	cal.add(Calendar.DAY_OF_MONTH,-7);
	
	String startDate2 = sdf.format(cal.getTime())+" 00:00:00";
	String endDate2 = sdf.format(cal.getTime())+" 23:59:59";
	String appId = request.getParameter("appId");
	
	if(appId == null){
		String opsName = request.getParameter("opsName");
		AppInfoPo appInfopo =AppInfoAo.get().getAppInfoByOpsName(opsName);
		appId = appInfopo.getAppId()+"";
	}
	
	AppInfoPo appInfopo = AppInfoAo.get().findAppInfoById(Integer.valueOf(appId));
	String appName = appInfopo.getAppName();
%>

<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
	function openTime(keyId){
		parent.openTime(keyId);	
	
	}
	
	
</script>
</head>
<body>


<%
Map<String,List<KeyValuePo>> maidianMap = MonitorTimeAo.get().findLikeKeyAverageByLimit5("NAGIOSMAIDIAN",appName);	
if(maidianMap.size()>0){
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >埋点信息</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="400" border="1" class="ui-widget ui-widget-content">
	<%
	try{
	int size = 0;
	for(Map.Entry<String,List<KeyValuePo>> entry:maidianMap.entrySet()){
		size+=2;
	} 
	%>
	<tr class="headcon ">
	<td colspan="<%=size %>" align="left"><font color="#000000" size="2">告警数据</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	<%
	for(Map.Entry<String,List<KeyValuePo>> entry:maidianMap.entrySet()){
		String[] name = entry.getKey().split("_");
		List<KeyValuePo> polist = entry.getValue();
		int id=0;
		if(polist.size()>0){
			id = polist.get(0).getKeyId();
		}
	%>
    <td colspan="2"><%=name[1] %><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=id %>)"/></td>
    <%} %>
  	</tr>
  	  	
  	<tr class="ui-widget-header ">
  	<%
	for(Map.Entry<String,List<KeyValuePo>> entry:maidianMap.entrySet()){	
	%>
    <td align="center">时间</td>
    <td align="center">数据</td>
    <%} %>
  	</tr>		  	
  	
  	<%
  	for(int i=0;i<6;i++){
  	%>
    <tr>
    <% 
    for(Map.Entry<String,List<KeyValuePo>> entry:maidianMap.entrySet()){
    	
    	if(entry.getValue().size()>i){
    %>
    <td><%=entry.getValue().get(i).getCollectTimeStr() %></td>
    <td><%=entry.getValue().get(i).getValueStr() %></td>
    <%}else{
    %>
    <td> - </td>
    <td> - </td>	
    <%	
    	} } 
    %>
    </tr>
	<%}}catch(Exception e){
		e.printStackTrace();
	} %>		
</table>
</div>
</div>
<%} %>

<%

Map<String,List<KeyValuePo>> exceptionMap = MonitorTimeAo.get().findLikeKeyAverageByLimit5("EXCEPTION",appName);
if(exceptionMap.size()>0){
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >异常信息</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table id="EXCEPTION" width="400" border="1" class="ui-widget ui-widget-content">
	<%
	try{
	int size = 0;
	for(Map.Entry<String,List<KeyValuePo>> entry:exceptionMap.entrySet()){
		size+=2;
	} 
	%>
	<tr class="headcon ">
	<td colspan="<%=size %>" align="left"><font color="#000000" size="2">异常信息</font>&nbsp;&nbsp;</td>
	</tr>
	<tr class="ui-widget-header ">
	<%
	for(Map.Entry<String,List<KeyValuePo>> entry:exceptionMap.entrySet()){
		String[] name = entry.getKey().split("_");
		List<KeyValuePo> polist = entry.getValue();
		int id=0;
		if(polist.size()>0){
			id = polist.get(0).getKeyId();
		}
	%>
    <td colspan="2"><%=name[1] %><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=id %>)"/></td>
    <%} %>
  	</tr>
  	  	
  	<tr class="ui-widget-header ">
  	<%
	for(Map.Entry<String,List<KeyValuePo>> entry:exceptionMap.entrySet()){	
	%>
    <td align="center">时间</td>
    <td align="center">异常数</td>
    <%} %>
  	</tr>		  	
  	
  	<%
  	for(int i=0;i<6;i++){
  	%>
    <tr>
    <% 
    for(Map.Entry<String,List<KeyValuePo>> entry:exceptionMap.entrySet()){
    	
    	if(entry.getValue().size()>i){
    %>
    <td><%=sdf2.format(entry.getValue().get(i).getCollectTime()) %></td>
    <td><%=entry.getValue().get(i).getValueStr() %></td>
    <%}else{
    %>
    <td> - </td>
    <td> - </td>	
    <%	
    	} } 
    %>
    </tr>
	<%}}catch(Exception e){
		e.printStackTrace();
	} %>		
</table>
</div>
</div>
<%} %>
</body>

</html>