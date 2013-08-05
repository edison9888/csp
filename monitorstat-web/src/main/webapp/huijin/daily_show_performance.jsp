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

<div id="<%=appName %>_app_id" class="ui-widget">
<table width="100%" >
	<tr class="headcon ">
		<td colspan="2" align="left"><font color="#000000" size="2">性能数据</font></td>
	</tr>
	<tr>
		<td>
		<table width="400" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td width="100" align="center">系统数据</td>
				<td width="90" align="center">当前</td>
				<td width="70" align="center">同比</td>
				<td width="70" align="center">环比</td>
				<td width="70" align="center">基线</td>
			</tr>
			<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date cDate = sdf.parse(searchDate);
			Calendar cCal = Calendar.getInstance();
			cCal.setTime(cDate);
			cCal.set(Calendar.HOUR_OF_DAY,20);
			
			Date startDate = cCal.getTime();
			cCal.set(Calendar.HOUR_OF_DAY,22);
			Date endDate = cCal.getTime();
			
			double cpu= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),3113,startDate,endDate);
			double load= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),944,startDate,endDate);
			double swap= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),32781,startDate,endDate);
			double jvm= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),7621,startDate,endDate);
			
			%>
			<tr>
				<td>CPU</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getCpuKeyId()%>','<%=searchDate%>')"><%=vo.getCpu()==null?cpu:vo.getCpu() %></td>
				<td><%=Utlitites.scale(vo.getCpu(),tongqiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),huanbiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),getBaseValue(vo.getCpuKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>IOWAIT</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getIowaitKeyId()%>','<%=searchDate%>')"><%=vo.getIowait()==null?" - ":vo.getIowait() %></td>
				<td><%=Utlitites.scale(vo.getIowait(),tongqiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),huanbiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),getBaseValue(vo.getIowaitKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Load</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getLoadKeyId()%>','<%=searchDate%>')"><%=vo.getLoad()==null?load:vo.getLoad() %></td>
				<td><%=Utlitites.scale(vo.getLoad(),tongqiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),huanbiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),getBaseValue(vo.getLoadKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Mem</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getMenKeyId()%>','<%=searchDate%>')"><%=vo.getMen()==null?jvm:vo.getMen() %></td>
				<td><%=Utlitites.scale(vo.getMen(),tongqiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),huanbiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),getBaseValue(vo.getMenKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Swap</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getSwapKeyId()%>','<%=searchDate%>')"><%=vo.getSwap()==null?swap:vo.getSwap() %></td>
				<td><%=Utlitites.scale(vo.getSwap(),tongqiVo.getSwap()) %></td>
				<td><%=Utlitites.scale(vo.getSwap(),huanbiVo.getSwap()) %></td>
				<td><%=Utlitites.scale(vo.getSwap(),getBaseValue(vo.getSwapKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>GC次数</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getGcpo().getAveMachinekeyId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(vo.getGcpo().getGcCount()) %></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcCount(),tongqiVo.getGcpo().getGcCount())%></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcCount(),huanbiVo.getGcpo().getGcCount()) %></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcCount(),getBaseValue(vo.getGcpo().getAveMachinekeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>GC时间</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getGcpo().getAveuserTimeKeyId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(vo.getGcpo().getGcAverage()) %></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcAverage(),tongqiVo.getGcpo().getGcAverage())%></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcAverage(),huanbiVo.getGcpo().getGcAverage()) %></td>
				<td><%=Utlitites.scale(vo.getGcpo().getGcAverage(),getBaseValue(vo.getGcpo().getAveuserTimeKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>FullGc次数</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getFullGcpo().getAveMachinekeyId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(vo.getFullGcpo().getGcCount()) %></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcCount(),tongqiVo.getFullGcpo().getGcCount())%></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcCount(),huanbiVo.getFullGcpo().getGcCount()) %></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcCount(),getBaseValue(vo.getFullGcpo().getAveMachinekeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>FullGC时间</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getFullGcpo().getAveuserTimeKeyId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(vo.getFullGcpo().getGcAverage()) %></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcAverage(),tongqiVo.getFullGcpo().getGcAverage())%></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcAverage(),huanbiVo.getFullGcpo().getGcAverage()) %></td>
				<td><%=Utlitites.scale(vo.getFullGcpo().getGcAverage(),getBaseValue(vo.getFullGcpo().getAveuserTimeKeyId(),appBaseMap)) %></td>
			</tr>
		</table>
		</td>
		<td valign="top">
			<table width="400" border="1" class="ui-widget ui-widget-content">
				<tr class="ui-widget-header ">
					<td width="100" align="center">访问信息</td>
					<td width="90" align="center">当前</td>
					<td width="70" align="center">同比</td>
					<td width="70" align="center">环比</td>
					<td width="70" align="center">基线</td>
				</tr>
				
				<%
				//如果是c应用的话是通过统计调用接口的总数为pv，qps为每秒调用的接口数，rt为没秒的接口调用数
				if(Utlitites.getLong(vo.getPv())<1&&Utlitites.getLong(vo.getApachePv())<1&&Utlitites.getLong(vo.getQpsNum())<1&&Utlitites.getLong(vo.getApacheQps())<1){ 

					String machines = vo.getMachines();
					String[] ms =  machines.split("/");
					int mNumber = 0;
					for(String m:ms){
						String[] h = m.split(":");
						if(h.length==2){
							mNumber+=Integer.parseInt(h[1]);
						}
					}

					long qps =vo.getAllHsfInterfaceQps();
					long rt_v = vo.getAllHsfInterfaceRest();
					long pv = vo.getAllHsfInterfacePv();


					
					long tongbiqps = tongqiVo.getAllHsfInterfaceQps();										
					long tongbirt_v = tongqiVo.getAllHsfInterfaceRest();
					long tongbipv = tongqiVo.getAllHsfInterfacePv();

					
					long huanbiqps = huanbiVo.getAllHsfInterfaceQps();
					long huanbirt_v = huanbiVo.getAllHsfInterfaceRest();
					long huanbipv = huanbiVo.getAllHsfInterfacePv();

				
			%>
			<tr>
				<td>接口总调用量</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getAllHsfInterfacePvId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(pv + "") %></td>
				<td><%=Utlitites.scale(pv,tongbipv) %></td>
				<td><%=Utlitites.scale(pv,huanbipv) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>总接口qps</td>
				<td><%=Utlitites.formatNull(qps+"") %>(高峰期 平均到<%=mNumber %>机器)</td>
				<td><%=Utlitites.scale(qps,tongbiqps) %></td>
				<td><%=Utlitites.scale(qps,huanbiqps) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>接口平均耗时(ms)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getAllHsfInterfaceRestId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(rt_v+"") %></td>
				<td><%=Utlitites.scale(rt_v,tongbirt_v) %></td>
				<td><%=Utlitites.scale(rt_v,huanbirt_v) %></td>
				<td>-</td>
			</tr>
			<%}else { %>
			<tr>
				<td>PV(哈勃)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getPvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),tongqiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),huanbiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),getBaseValue(vo.getPvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(哈勃)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=qps&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appName %>')"/><%=Utlitites.formatNull(vo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),tongqiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),huanbiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),getBaseValue(vo.getQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(哈勃)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=rt&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appName %>')"/><%=Utlitites.formatNull(vo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),tongqiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),huanbiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),getBaseValue(vo.getRtKeyId(),appBaseMap)) %></td>
			</tr>
						<tr>
				<td>PV(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApachePvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),tongqiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),huanbiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),getBaseValue(vo.getApachePvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheQpsKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),tongqiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),huanbiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),getBaseValue(vo.getApacheQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheRestKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),tongqiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),huanbiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),getBaseValue(vo.getApacheRestKeyId(),appBaseMap)) %></td>
			</tr>
			<%} %>
			</table>
		</td>
	</tr>
</table>

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


