<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>



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
</style>
<%
int i=0;
String reslut = "";
//这个是一天为单位显示走势变化
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String collectTime1 = sdf.format(new Date());
String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";
String collectTime2 = request.getParameter("collectTime2");
String start2 = collectTime2+" 00:00:00";
String end2 = collectTime2+" 23:59:59";
String appId = request.getParameter("appId");
String keyId   = request.getParameter("keyId") ;
String appName = AppCache.get().getKey(Integer.parseInt(appId)).getAppName();
String tmpKeyId = null;
List<KeyPo> tmpKeyList = KeyCache.get().getAppKey(Integer.parseInt(appId));//.findAllKey("URLPV");
List<KeyPo> keyList = new ArrayList<KeyPo>();
for(KeyPo key:tmpKeyList){
	if(key.getKeyName().indexOf("URLPV")>-1){
		keyList.add(key);
	}
}
String urlname = "";
String siteName = "";
%>
<script type="text/javascript">
function goToDetailMonitor(){
	var keyid = $("#keySelect").val();
	location.href="./pv_url_detail.jsp?keyId="+keyid+"&appId=<%=appId%>";
}
function goToComparedDetailMonitor(){
	var keyid = $("#keySelect").val();
	var collectTime2 = $('#datepicker').val();	
	location.href="./pv_url_detail.jsp?keyId="+keyid+"&appId=<%=appId%>&collectTime1=<%=collectTime1%>&collectTime2="+collectTime2;
}
var collectTime2 = "<%=collectTime2%>";
if(collectTime2 ==null || collectTime2 ==""){
	goToDetailMonitor();
}
</script>


</head>
<body>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
	<table width="100%">
		<tr>
		   <td align="center">
			<span style="font-weight:bold ; size:34px;color:blue">应用【<%=appName%>】</span>：<select id="keySelect" onchange="goToDetailMonitor();">
			<%
			for(KeyPo keyPo:keyList){
				String url = keyPo.getKeyName();				
			%>
			<option value="<%=keyPo.getKeyId() %>" <%if(keyId!=null&&(keyPo.getKeyId()+"").equals(keyId)){out.print("selected");urlname=url;} %>><%=url %></option>
			<%} %>
			</select>
			日期: <input type="text" id="datepicker" value="<%=collectTime2==null?collectTime1:collectTime2 %>"/><button  onclick="goToComparedDetailMonitor()">对比</button>
		   </td>
		 </tr>
	</table>
</div>
<%

if(keyId==null){
	keyId = tmpKeyId;
}

if(keyId!=null){
	Map<String, List<KeyValuePo>>  mapPo = new HashMap<String,List<KeyValuePo>>();
	Map<String, List<KeyValuePo>>  mapPo2 = new HashMap<String,List<KeyValuePo>>();
	Map<String,List<PvUrlPo>> map = MonitorTimeAo.get().findPvUrlDetailByDate(keyId,appName,parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1));
	for(Map.Entry<String,List<PvUrlPo>> entry:map.entrySet()){
	    siteName = entry.getKey();
		mapPo = new HashMap<String,List<KeyValuePo>>();
		SimpleDateFormat parseFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		for(PvUrlPo po:entry.getValue()){
			{   
				List<KeyValuePo> polist = mapPo.get("pv流量["+parseFormatDate.format(po.getCollectTime())+"]");
				if(polist==null){
					polist = new ArrayList<KeyValuePo>();
					mapPo.put("pv流量["+parseFormatDate.format(po.getCollectTime())+"]",polist);
				}
				KeyValuePo keyValue = new KeyValuePo();
				keyValue.setCollectTime(po.getCollectTime());
				keyValue.setCollectTimeStr(po.getCollectTimeStr());
				keyValue.setValueStr(po.getPvCount()+"");
				polist.add(keyValue);
				
			}
			{
				List<KeyValuePo> polist = mapPo.get("响应时间(ms)["+parseFormatDate.format(po.getCollectTime())+"]");
				if(polist==null){
					polist = new ArrayList<KeyValuePo>();
					mapPo.put("响应时间(ms)["+parseFormatDate.format(po.getCollectTime())+"]",polist);
				}
				KeyValuePo keyValue = new KeyValuePo();
				keyValue.setCollectTime(po.getCollectTime());
				keyValue.setCollectTimeStr(po.getCollectTimeStr());
				keyValue.setValueStr(Utlitites.formatDotTwo((po.getRest()/1000)+""));
				polist.add(keyValue);
			}
			{
				List<KeyValuePo> polist = mapPo.get("页面大小(k)["+parseFormatDate.format(po.getCollectTime())+"]");
				if(polist==null){
					polist = new ArrayList<KeyValuePo>();
					mapPo.put("页面大小(k)["+parseFormatDate.format(po.getCollectTime())+"]",polist);
				}
				KeyValuePo keyValue = new KeyValuePo();
				keyValue.setCollectTime(po.getCollectTime());
				keyValue.setCollectTimeStr(po.getCollectTimeStr());
				keyValue.setValueStr(Utlitites.formatDotTwo(po.getPagesize()/1000+""));
				polist.add(keyValue);
			}
		}
	}
	if(collectTime2!=null){
		Map<String, List<PvUrlPo>> map2 = MonitorTimeAo.get().findPvUrlDetailByDate(keyId,appName,parseLogFormatDate.parse(start2), parseLogFormatDate.parse(end2));	
		for(Map.Entry<String,List<PvUrlPo>> entry:map2.entrySet()){
			siteName = entry.getKey();
			mapPo2 = new HashMap<String,List<KeyValuePo>>();
			SimpleDateFormat parseFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			for(PvUrlPo po:entry.getValue()){
				{   
					List<KeyValuePo> polist = mapPo2.get("pv流量["+parseFormatDate.format(po.getCollectTime())+"]");
					if(polist==null){
						polist = new ArrayList<KeyValuePo>();
						mapPo2.put("pv流量["+parseFormatDate.format(po.getCollectTime())+"]",polist);
					}
					KeyValuePo keyValue = new KeyValuePo();
					keyValue.setCollectTime(po.getCollectTime());
					keyValue.setCollectTimeStr(po.getCollectTimeStr());
					keyValue.setValueStr(po.getPvCount()+"");
					polist.add(keyValue);
					
				}
				{
					List<KeyValuePo> polist = mapPo2.get("响应时间(ms)["+parseFormatDate.format(po.getCollectTime())+"]");
					if(polist==null){
						polist = new ArrayList<KeyValuePo>();
						mapPo2.put("响应时间(ms)["+parseFormatDate.format(po.getCollectTime())+"]",polist);
					}
					KeyValuePo keyValue = new KeyValuePo();
					keyValue.setCollectTime(po.getCollectTime());
					keyValue.setCollectTimeStr(po.getCollectTimeStr());
					keyValue.setValueStr(Utlitites.formatDotTwo((po.getRest()/1000)+""));
					polist.add(keyValue);
				}
				{
					List<KeyValuePo> polist = mapPo2.get("页面大小(k)["+parseFormatDate.format(po.getCollectTime())+"]");
					if(polist==null){
						polist = new ArrayList<KeyValuePo>();
						mapPo2.put("页面大小(k)["+parseFormatDate.format(po.getCollectTime())+"]",polist);
					}
					KeyValuePo keyValue = new KeyValuePo();
					keyValue.setCollectTime(po.getCollectTime());
					keyValue.setCollectTimeStr(po.getCollectTimeStr());
					keyValue.setValueStr(Utlitites.formatDotTwo(po.getPagesize()/1000+""));
					polist.add(keyValue);
				}
			}
		}
	}
	mapPo.putAll(mapPo2);
	reslut = AmLineFlash.createCharXml(mapPo) ; 
%>
<%}%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=siteName %>:<%=urlname %></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div id="chartdiv_<%=i %>" align="center">


</div>
</div>
</div>
<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_<%=i %>", "800", "300", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline_<%=i %>");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv_<%=i %>");
</script>


</body>




</html>