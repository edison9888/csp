<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<%!

private String getBaseValue(Integer keyId,Map<Integer,KeyValueBaseLinePo> appBaseMap){
	if(appBaseMap==null)return null;
	KeyValueBaseLinePo po = appBaseMap.get(keyId);
	if(po==null){
		return null;
	}else{
		return po.getBaseLineValue()+"";
	}
}

%>
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
</style>
<script type="text/javascript">
$(function(){
	$("#tabs").tabs();
});

function goToAppDetail(){
	
	var searchDate = $("#datepicker").val();
	var appName = $("#appNameSelect").val();

	location.href="app_detail.jsp?searchDate="+searchDate+"&selectAppId="+appName;
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
	$("#iframe_report").attr("src","daily_key_time_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);
	$("#dialog_report").dialog("open")
}

function goToMonitorTime(){
	location.href="index_time.jsp";
}

function goToDetailBusi(){
	location.href="detail_busi.jsp";
}
</script>

</head>
<body>
<%//yyyy-MM-dd
String searchDate = request.getParameter("searchDate");

Integer selectAppId = Integer.parseInt(request.getParameter("selectAppId"));

if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}
String tongbiDate = Utlitites.getTongBiMonitorDate(searchDate);
String huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);

Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId,searchDate);
Map<Integer, MonitorVo> tongqiMap = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId,tongbiDate);
Map<Integer, MonitorVo> huanqiMap = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId,huanbiDate);

List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();
AppInfoPo appPo = null;
for(AppInfoPo po:appList){
	if(po.getAppDayId() == selectAppId){
		appPo = po;
		break;
	}
}

String appName = appPo.getOpsName();
int appDayId = appPo.getAppDayId();

Map<Integer,Map<Integer,KeyValueBaseLinePo>> baseMap = MonitorDayAo.get().findMonitorBaseLine(selectAppId,searchDate);

%>

<div id="tabs" style="width: 100%px">

<%
			
	if(selectAppId !=null){
		MonitorVo vo = map.get(selectAppId);
		MonitorVo tongqiVo = tongqiMap.get(selectAppId);
		MonitorVo huanbiVo = huanqiMap.get(selectAppId);
		
		Map<Integer,KeyValueBaseLinePo> appBaseMap = baseMap.get(selectAppId);
		
		if(vo==null){
			vo = new MonitorVo();
		}
		if(tongqiVo==null){
			tongqiVo = new MonitorVo();
		}
		if(huanbiVo==null){
			huanbiVo = new MonitorVo();
		}

%>

<%	
if(vo.getInHsfList().size()>0){ 
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="18" width="100%"><font color="#000000" size="2">[ HSF ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">	  
	  <td width="80" rowspan="2" align="center">类名</td>
	  <td width="80" rowspan="2" align="center">方法名</td>
	  <td colspan="4" align="center">执行次数</td>
	  <td colspan="4" align="center">平均执行时间(ms)</td>
	  <td colspan="4" align="center">Exception总数</td>
	  <td colspan="4" align="center">BizException总数</td>
  	</tr>
	<tr class="ui-widget-header ">
	  	<td width="200" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>		
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>		
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>		
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>	
		<td width="100" align="center">基线</td>			
	</tr>
	<%
		
		Map<String, List<HsfClass>> sortOutMap = vo.getSortInHsfMap1();		
		Map<String, List<HsfClass>> tongbiSortOutMap = tongqiVo.getSortInHsfMap1();
		Map<String, List<HsfClass>> huanbiSortOutMap = huanbiVo.getSortInHsfMap1();
		
		
		Map<String,HsfPo> huanbihsfPoMap = new HashMap<String,HsfPo>();
		
		Iterator<Map.Entry<String,List<HsfClass>>> huanbiOutMapit = huanbiSortOutMap.entrySet().iterator();
		while(huanbiOutMapit.hasNext()){
			Map.Entry<String,List<HsfClass>> aimEntry = huanbiOutMapit.next();
			String aim = aimEntry.getKey();
			List<HsfClass> hsfClassList = aimEntry.getValue();
			for(HsfClass hc:hsfClassList){
				List<HsfPo> list = hc.getHsfPo();
				for(HsfPo po:list){	
					String key = aim+"_"+po.getClassName()+"_"+po.getMethodName();
					huanbihsfPoMap.put(key,po);
				}
			}
		}
		
		Map<String,HsfPo> tongbihsfPoMap = new HashMap<String,HsfPo>();
		Iterator<Map.Entry<String,List<HsfClass>>> tongbiOutMapit = tongbiSortOutMap.entrySet().iterator();
		while(tongbiOutMapit.hasNext()){
			Map.Entry<String,List<HsfClass>> aimEntry = tongbiOutMapit.next();
			String aim = aimEntry.getKey();
			List<HsfClass> hsfClassList = aimEntry.getValue();
			for(HsfClass hc:hsfClassList){
				List<HsfPo> list = hc.getHsfPo();
				for(HsfPo po:list){	
					String key = aim+"_"+po.getClassName()+"_"+po.getMethodName();
					tongbihsfPoMap.put(key,po);
				}
			}
		}
		
	
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
					String key = aim+"_"+po.getClassName()+"_"+po.getMethodName();					
					try{					
				%>					
				<tr>						
					<td align="left" style="font-size:12px"><%=classShow?po.getHtmlClassName():" - " %></td>
					<td align="left" style="font-size:12px"><%=po.getHtmlMethodName() %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getAverageExe() %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getAverageExe()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getAverageExe()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getExcCountkeyId()%>','<%=searchDate%>')"><%=po.getExecptionNum()==null?" - ":po.getExecptionNum() %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),getBaseValue(po.getExcCountkeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getBizCountkeyId()%>','<%=searchDate%>')"><%=po.getBizExecptionNum()==null?" - ":po.getBizExecptionNum() %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getBizExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getBizExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),getBaseValue(po.getBizCountkeyId(),appBaseMap)) %></td>
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
<%} %>
</div>
</div>
</div>
</div>
<%} %>
<%
	String currentHost = request.getParameter("currentHost");
%>
<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="daily_key_detail.jsp" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>

<iframe id="iframeC" name="iframeC" src="" width="0" height="0" style="display:none;" ></iframe>
<input id = "currentHost" type="hidden" value="<%=currentHost%>"/>
<script type="text/javascript"> 
function sethash(){
    hashH = document.documentElement.scrollHeight;
	urlC = document.getElementById("currentHost").value;
    document.getElementById("iframeC").src=urlC+"#"+hashH; 
}
window.onload=sethash;
</script>

</body>

</html>


