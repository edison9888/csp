<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@ page import="com.taobao.monitor.web.util.*"%>

<%@page import="org.apache.velocity.runtime.directive.Foreach"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
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
body {
	font-size: 62.5%;
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
</style>
<script type="text/javascript">
$(function(){
	$("#tabs").tabs();
});
$(document).ready(function() {  
	 
         $('#datepicker').datepicker({dateFormat: 'yy-mm-dd' } );  
})

function goToAppDetail(){
	
	var searchDate = $("#datepicker").val();
	var appName = $("#appNameSelect").val();

	location.href="app_detail.jsp?searchDate="+searchDate+"&selectAppName="+appName;
}
function goToDetailBusi(){
	
	var searchDate = $("#datepicker").val();

	location.href="detail_busi.jsp?searchDate="+searchDate;
}
function goToIndex(){

	location.href="index.jsp";
}
$(function() {
	$("#dialog_report").dialog({
		bgiframe: true,
		height: 500,
		width:820,
		modal: true,
		draggable:true,
		resizable:false,
		autoOpen:false
	});
});

function openKeyDetail(appId,keyId,collectTime){
	$("#iframe_report").attr("src","key_time_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);
	$("#dialog_report").dialog("open")
	//window.open("key_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);	
}

function goToMonitorTime(){
	location.href="index_time.jsp";
}
</script>

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
	
	String appName = request.getParameter("appName");
	if(appName==null){
		appName = "item";
	}
%>

</head>
<body>

<%//yyyy-MM-dd
String searchDate = request.getParameter("searchDate");

String selectAppName = request.getParameter("selectAppName");

if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}
%>

<%
 
///访问数据
	Map<String, List<KeyValuePo>> map1 = DetailStatisticAO.get().findInfoByKeyDate("detail_ic_cache_success","item",parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));

	
	//String pvreslut = AmLineFlash.createCharXml(pvMap);
	String Restreslut = AmLineFlash.createCharXml(map1);
	
	Map<String, List<KeyValuePo>> uicMap = DetailStatisticAO.get().findInfoByKeyDate("detail_uic_cache_success","item",parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));

	
	//String pvreslut = AmLineFlash.createCharXml(pvMap);
	String UICRestreslut = AmLineFlash.createCharXml(uicMap);
	
%>




<div>

<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
		<tr>
		   <td align="center">

日期: <input type="text" id="datepicker" value="<%=searchDate%>"/>
<button  onclick="goToDetailBusi()">查看</button>
<button  onclick="goToAppDetail()">查看日报详细</button>
<button  onclick="goToIndex()">返回日报首页</button>
<button  onclick="goToMonitorTime()">进入实时监控</button>
			</td>
		</tr>
</table>
</div>

<table width="100%">
	<tr>
	   <td align="center"><font style="color:red" size="5">【<%=selectAppName%>】</font>	</td>
	</tr>
</table>







<div id="tabs" style="width: 100%px">
<%
List<DetailBusiData> listRef = DetailStatisticAO.get().queryStatistic("detail_refer","item",searchDate);
List<DetailBusiData> listPage = DetailStatisticAO.get().queryStatistic("detail_page","item",searchDate);
List<DetailBusiData> listResp = DetailStatisticAO.get().queryStatistic("detail_resp","item",searchDate);
%>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="4"><font color="#000000" size="2">来源统计信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="250">来源</td>
		<td>占比</td>
		<td></td>
		<td></td>
	</tr>
	
	<% 
	
	for(int i = 0; i < listRef.size() ; i++){
	    DetailBusiData data = listRef.get(i);%>
	   <tr  <% if(i %2 == 1 ) {%>class="ui-state-default" <% } %>>
		<td><%=data.getName() %></td>
		<td><%=data.getStrValue() %></td>
		<td></td>
		<td></td>
	</tr>
	<% } %>
	
	
	 
</table>
<table>
<br><br>
</table>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="4"><font color="#000000" size="2">页面大小统计信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="250">页面大小(单位:k)</td>
		<td>占比</td>
		<td></td>
		<td></td>
	</tr>
	
	<% 
	
	for(int i = 0; i < listPage.size() ; i++){
	    DetailBusiData data = listPage.get(i);%>
	   <tr  <% if(i %2 == 1 ) {%>class="ui-state-default" <% } %>>
		<td><%=data.getName() %></td>
		<td><%=data.getStrValue() %></td>
		<td></td>
		<td></td>
	</tr>
	<% } %>
	
	 
</table>
<table>
<br><br>
</table>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="4"><font color="#000000" size="2">响应时间信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="250">响应时间(单位:ms)</td>
		<td>占比</td>
		<td></td>
		<td></td>
	</tr>
	
	<% 
	
	for(int i = 0; i < listResp.size() ; i++){
	    DetailBusiData data = listResp.get(i);%>
	  <tr  <% if(i %2 == 1 ) {%>class="ui-state-default" <% } %>>
		<td><%=data.getName() %></td>
		<td><%=data.getStrValue() %></td>
		<td></td>
		<td></td>
	</tr>
	<% } %>
	
	 
</table>
</div>
</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
	
		<td width="100%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>IC命中率</td>								
			</tr>						
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td id="Restchartdiv"></td>
			</tr>
			</table>
		</td>
	</tr>	
</table>
</div>

<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1">
	<tr>
	
		<td width="100%" >
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td>UIC命中率</td>								
			</tr>						
			</table>
			<table width="100%" border="1" class="ui-widget ui-widget-content">			
			<tr >
			<td id="uicdiv"></td>
			</tr>
			</table>
		</td>
	</tr>	
</table>
</div>


<script type="text/javascript">	
	var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_rest", "100%", "250", "6", "#FFFFFF");
	
	so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
	so2.addVariable("chart_id", "amline2");   
	so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
	
	so2.addVariable("chart_data", encodeURIComponent("<%=Restreslut%>"));
	so2.write("Restchartdiv");


	var uicso = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_rest", "100%", "250", "6", "#FFFFFF");
	
	uicso.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
	uicso.addVariable("chart_id", "amline2");   
	uicso.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
	
	uicso.addVariable("chart_data", encodeURIComponent("<%=UICRestreslut%>"));
	uicso.write("uicdiv");

</script>
</body>
</html>