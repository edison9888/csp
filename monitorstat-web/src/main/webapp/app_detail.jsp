<%@page import="com.taobao.monitor.common.po.ProductLine"%>
<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.web.vo.MonitorVo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.web.vo.GcPo"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.web.vo.HsfClass"%>
<%@page import="com.taobao.monitor.web.vo.HsfPo"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.web.vo.OtherKeyValueVo"%>
<%@page import="com.taobao.monitor.web.vo.OtherHaBoLogRecord"%>
<%@page import="com.taobao.monitor.web.vo.ForestPo"%>
<%@page import="com.taobao.monitor.web.vo.PageCachePo"%>
<%@page import="com.taobao.monitor.web.vo.TairClientPo"%>
<%@page import="com.taobao.monitor.web.vo.SearchEnginePo"%>
<%@page import="com.taobao.monitor.web.vo.ThreadPoolPo"%>
<%@page import="com.taobao.monitor.web.vo.ThreadPo"%>
<%@page import="com.taobao.monitor.web.vo.DataSourcePo"%>
<%@page import="com.taobao.monitor.web.vo.AppSqlInfo"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.util.Comparator" %>
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
	if(appBaseMap==null||keyId==null)return null;
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
	$("#iframe_report").attr("src","key_time_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);
	$("#dialog_report").dialog("open")
	//window.open("key_detail.jsp?appId="+appId+"&keyId="+keyId+"&collectTime="+collectTime);	
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
<jsp:include page="head.jsp"></jsp:include>
<jsp:include page="left.jsp"></jsp:include>
<div>
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

if (appPo == null) {
	out.print("<div align=\"center\"><font size=\"5\" color=\"red\">请求数据不存在</font></div>");
	return;
}

String appName = appPo.getOpsName();
int appDayId = appPo.getAppDayId();

Map<Integer,Map<Integer,KeyValueBaseLinePo>> baseMap = MonitorDayAo.get().findMonitorBaseLine(selectAppId,searchDate);


%>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
		<tr>
		   <td align="center">
		   <select id="companyGroupSelect" onChange="companyChange(this)">	
			</select>
			<select id="parentGroupSelect" onChange="groupChange(this)">	
			</select>
			<select id="appNameSelect" name="selectAppId">	
			</select>
日期: <input type="text" id="datepicker" value="<%=searchDate%>"/>
<button  onclick="goToAppDetail()">查看日报详细</button>
<button  onclick="goToIndex()">返回日报首页</button>
<button  onclick="goToMonitorTime()">进入实时监控首页</button>
<button  onclick="goToDetailBusi()">查看detail业务数据</button>
			</td>
		</tr>
</table>
</div>

<table width="100%">
	<tr>
	   <td align="center"><font style="color:red" size="5">【<%=appName%>】</font>	(<%=map.get(selectAppId)!=null?map.get(selectAppId).getMachines():"" %>)</td>
	</tr>
</table>
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
			
			//double cpu= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),3113,startDate,endDate);
			//double load= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),944,startDate,endDate);
			//double swap= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),32781,startDate,endDate);
			//double jvm= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),7621,startDate,endDate);
			
			%>
			<tr>
				<td>CPU&nbsp;(高峰期占比)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getCpuKeyId()%>','<%=searchDate%>')"><%=vo.getCpu()==null?" - ":vo.getCpu() %>%</td>
				<td><%=Utlitites.scale(vo.getCpu(),tongqiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),huanbiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),getBaseValue(vo.getCpuKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>IOWAIT&nbsp;(高峰期占比)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getIowaitKeyId()%>','<%=searchDate%>')"><%=vo.getIowait()==null?" - ":vo.getIowait() %>%</td>
				<td><%=Utlitites.scale(vo.getIowait(),tongqiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),huanbiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),getBaseValue(vo.getIowaitKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>LOAD</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getLoadKeyId()%>','<%=searchDate%>')"><%=vo.getLoad()==null?" - ":vo.getLoad() %></td>
				<td><%=Utlitites.scale(vo.getLoad(),tongqiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),huanbiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),getBaseValue(vo.getLoadKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>JVM Mem&nbsp;(高峰期占比)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getMenKeyId()%>','<%=searchDate%>')"><%=vo.getMen()==null?" - ":vo.getMen() %>%</td>
				<td><%=Utlitites.scale(vo.getMen(),tongqiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),huanbiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),getBaseValue(vo.getMenKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Swap&nbsp;(高峰期占比)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getSwapKeyId()%>','<%=searchDate%>')"><%=vo.getSwap()==null?" - ":vo.getSwap() %>%</td>
				<td><%=Utlitites.scale(vo.getSwap(),tongqiVo.getSwap()) %></td>
				<td><%=Utlitites.scale(vo.getSwap(),huanbiVo.getSwap()) %></td>
				<td><%=Utlitites.scale(vo.getSwap(),getBaseValue(vo.getSwapKeyId(),appBaseMap)) %></td>
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
				if("center".equals(appPo.getAppType())){ 

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
				<td>接口调用总量<br>(当天所有机器的汇总)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getAllHsfInterfacePvId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(pv + "") %></td>
				<td><%=Utlitites.scale(pv,tongbipv) %></td>
				<td><%=Utlitites.scale(pv,huanbipv) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>接口QPS<br>(高峰期每台机器的平均)</td>
				<td><%=Utlitites.formatNull(qps+"") %></td>
				<td><%=Utlitites.scale(qps,tongbiqps) %></td>
				<td><%=Utlitites.scale(qps,huanbiqps) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>接口RT(ms)<br>(高峰期每台机器的平均)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getAllHsfInterfaceRestId()%>','<%=searchDate%>')"><%=Utlitites.formatNull(rt_v+"") %></td>
				<td><%=Utlitites.scale(rt_v,tongbirt_v) %></td>
				<td><%=Utlitites.scale(rt_v,huanbirt_v) %></td>
				<td>-</td>
			</tr>
			<%}else { %>
			<tr>
				<td>PV(哈勃)</td>
				<td>
					<!--<img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getPvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getPv()) %> -->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getPv(),tongqiVo.getPv()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getPv(),huanbiVo.getPv()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getPv(),getBaseValue(vo.getPvKeyId(),appBaseMap)) %>-->
				</td>
			</tr>
			<tr>
				<td>Qps(哈勃)</td>
				<td>
					<!--<img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=qps&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appName %>')"/><%=Utlitites.formatNull(vo.getQpsNum()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getQpsNum(),tongqiVo.getQpsNum()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getQpsNum(),huanbiVo.getQpsNum()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getQpsNum(),getBaseValue(vo.getQpsKeyId(),appBaseMap)) %>-->
				</td>
			</tr>
			<tr>
				<td>Rt(哈勃)</td>
				<td>
				  <!--<img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=rt&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appName %>')"/><%=Utlitites.formatNull(vo.getRtNum()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getRtNum(),tongqiVo.getRtNum()) %>-->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getRtNum(),huanbiVo.getRtNum()) %> -->
				</td>
				<td>
					<!--<%=Utlitites.scale(vo.getRtNum(),getBaseValue(vo.getRtKeyId(),appBaseMap)) %>-->
				</td>
			</tr>
			<tr>
				<td>PV(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApachePvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),tongqiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),huanbiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),getBaseValue(vo.getApachePvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheQpsKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),tongqiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),huanbiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),getBaseValue(vo.getApacheQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(CSP)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheRestKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),tongqiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),huanbiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),getBaseValue(vo.getApacheRestKeyId(),appBaseMap)) %></td>
			</tr>
			<%} %>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">		
			<table width="800" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td width="100" align="center" colspan="10">GC信息</td>				
			</tr>					
			<%
			List<String> gcNameList = new ArrayList<String>();
			gcNameList.add("GC");
			gcNameList.add("Full");
			gcNameList.add("ParNew");
			gcNameList.add("CMS-concurrent-preclean");
			gcNameList.add("CMS-remark");
			gcNameList.add("CMS-concurrent-mark");
			gcNameList.add("CMS-concurrent-sweep");
			gcNameList.add("CMS-initial-mark");
			gcNameList.add("CMS-concurrent-reset");
			
			Map<String, GcPo> gcMap = vo.getGcMap();
			for(String gcname:gcNameList){
				try{
				String m = "SELF_GC_"+gcname+"_AVERAGEMACHINEFLAG";
				String a = "SELF_GC_"+gcname+"_AVERAGEUSERTIMES";
				
				if(vo.getGcMap().get(m)==null){
					continue;
				}
				
				String cV = Utlitites.formatNull(vo.getGcMap().get(m).getKeyValue());
				String aV = Utlitites.formatNull(vo.getGcMap().get(a).getKeyValue());
				
				
				Integer cId = vo.getGcMap().get(m).getKeyId();
				Integer aId = vo.getGcMap().get(a).getKeyId();
			%>
			<tr >
				<td width="160"><%="GC_"+gcname %>次数</td>
				<td width="80"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=cId%>','<%=searchDate%>')"><%=cV %></td>
				<td width="80"><%=Utlitites.scale(cV,tongqiVo.getGcMap().get(m)!=null?tongqiVo.getGcMap().get(m).getKeyValue():null)%></td>
				<td width="80"><%=Utlitites.scale(cV,huanbiVo.getGcMap().get(m)!=null?huanbiVo.getGcMap().get(m).getKeyValue():null) %></td>
				<td width="80"><%=Utlitites.scale(cV,getBaseValue(cId,appBaseMap)) %></td>
				<td width="160"><%="GC_"+gcname %>时间</td>
				<td width="80"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=aId%>','<%=searchDate%>')"><%=aV %></td>
				<td width="80"><%=Utlitites.scale(aV,tongqiVo.getGcMap().get(a)!=null?tongqiVo.getGcMap().get(a).getKeyValue():null)%></td>
				<td width="80"><%=Utlitites.scale(aV,huanbiVo.getGcMap().get(a)!=null?huanbiVo.getGcMap().get(a).getKeyValue():null) %></td>
				<td width="80"><%=Utlitites.scale(aV,getBaseValue(aId,appBaseMap)) %></td>
			</tr>			
			<%}catch(Exception e){}} %>		
			</table>
		</td>
	</tr>
    <tr>
    	<td colspan="2">
        	<table width="800" border="1" class="ui-widget ui-widget-content">
            <tr class="ui-widget-header ">
				<td width="100" align="center" colspan="10">Http Code</td>								
			</tr>
			<%
			Map<String, Integer> stateMap = new HashMap<String, Integer>();
			if(vo.getApacheStateMap() != null) {
				stateMap = vo.getApacheStateMap();
				Iterator<Map.Entry<String, Integer>> iter = stateMap.entrySet().iterator();
				for (int i = 0; i < stateMap.size(); i = i + 2) {
					Map.Entry<String, Integer> en1 = iter.next();
					String key1 = en1.getKey();
					String keyName = "PV_VISIT_STATE_"+key1+"_COUNTTIMES";
					String value1 = en1.getValue().toString();
					KeyPo keyPo = KeyAo.get().getKeyByName(keyName);
					Integer keyId1 = (keyPo==null?null:keyPo.getKeyId());// 通过keyname获取keyid
					Integer tongqiValue = tongqiVo.getApacheStateMap().get(keyName);
					Integer huanbiValue = huanbiVo.getApacheStateMap().get(keyName);
				%>
					<tr>
						<td width="160">状态码<%=key1%>出现次数</td>
						<td width="80"><img src="<%=request.getContextPath()%>/statics/images/report.png" onClick=""><%=Utlitites.fromatLong(value1)%></td>
						<td width="80"><%=Utlitites.scale(value1, tongqiValue != null ? tongqiValue.toString() : null)%></td>
						<td width="80"><%=Utlitites.scale(value1, huanbiValue != null ? huanbiValue.toString() : null)%></td>
						<td width="80"><%=Utlitites.scale(value1, getBaseValue(keyId1, appBaseMap))%></td>						
				<%
					if (iter.hasNext()) {
						Map.Entry<String, Integer> en2 = iter.next();
						String key2 = en2.getKey();
						String key2Name = "PV_VISIT_STATE_"+key2+"_COUNTTIMES";
						String value2 = en2.getValue().toString();
						KeyPo keyPo2 = KeyAo.get().getKeyByName(key2Name);
						Integer keyId2 = (keyPo2==null?null:keyPo2.getKeyId());// 通过keyname获取keyid
						Integer tongqiValue2 = tongqiVo.getApacheStateMap().get(key2Name);
						Integer huanbiValue2 = huanbiVo.getApacheStateMap().get(key2Name);
				%>
						<td width="160">状态码<%=key2%>出现次数</td>
						<td width="80"><img src="<%=request.getContextPath()%>/statics/images/report.png" onClick=""><%=Utlitites.fromatLong(value2)%></td>
						<td width="80"><%=Utlitites.scale(value1, tongqiValue2 != null ? tongqiValue2.toString() : null)%></td>
						<td width="80"><%=Utlitites.scale(value1, huanbiValue2 != null ? huanbiValue2.toString() : null)%></td>
						<td width="80"><%=Utlitites.scale(value1, getBaseValue(keyId2, appBaseMap))%></td>
					</tr>						
				<%
					} else {
				%>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					</tr>
				<%
						}
					}
				}			
			%>
            </table>
        </td>
    </tr>
 <%--z --%>
	<tr>
		<td valign="top" width="50%">
			<table width="100%" border="1" class="ui-widget ui-widget-content">
				<tr class="ui-widget-header ">
					<td colspan="3" align="center">
						来源URL统计
					</td>	
				</tr>
				<tr class="ui-widget-header ">
					<td width="70%" align="center">
						来源 URL
					</td>
					<td width="20%" align="center">
						PV量
					</td >
					<td width="10%" align="center">
						比例
					</td>
				</tr>
				<%
				Long allPv = -1L;
				DecimalFormat df = new DecimalFormat("0.00%"); ;
				Comparator<Map.Entry<String, Long>> compare = new Comparator<Map.Entry<String, Long>>(){
					public int compare(Map.Entry<String, Long> e1,Map.Entry<String, Long> e2){
						return (int)(e2.getValue() - e1.getValue());
					}
				};
				if(vo.getApachePv()!=null){
					allPv = Long.valueOf(vo.getApachePv());
					Map<String, Long> srcUrlPvMap = new HashMap<String, Long>();
					if(vo.getSrcUrlPvMap()!= null)
						srcUrlPvMap = vo.getSrcUrlPvMap();
					
					ArrayList<Map.Entry<String, Long>>srcUrlPvList = new ArrayList<Map.Entry<String, Long>>(srcUrlPvMap.entrySet());
					Collections.sort(srcUrlPvList,compare);
					
					for(Integer i=0; i<srcUrlPvList.size();i++){
							Map.Entry<String, Long> entry = srcUrlPvList.get(i);
							String key = entry.getKey();
							String value = entry.getValue().toString();
							String keyName = "PV_SOURCE_URL_"+key+"_COUNTTIMES";
							Double proportion = entry.getValue()*1.0/allPv;
							String proportionStr = df.format(proportion);
							%>
								<tr >
								<td align="left"><%= key%></td>
								<td align="left"><%= Utlitites.fromatLong(value) %></td>
								<td align="left"><%= proportionStr%></td>
								</tr>
							<% 
					}
				}
				%>
			</table>
		</td>
		<td valign="top" width="50%">
			<table width="100%" border="1" class="ui-widget ui-widget-content">
				<tr class="ui-widget-header ">
					<td colspan="3" align="center">
						目的URL统计
						<%
						if (vo.getUserRequestUrlNum()!=null && Long.parseLong(vo.getUserRequestUrlNum()) > 0) {
						%>
							(总计：<%=Utlitites.fromatLong(vo.getUserRequestUrlNum()) %>)
						<%
						}
						%>
					</td>	
				</tr>
				<tr class="ui-widget-header ">
					<td width="70%" align="center">目的 URL</td>
					<td width="20%" align="center">PV量</td >
					<td width="10%" align="center">比例</td>
				</tr>
					<%
					if(vo.getApachePv()!=null){
					Map<String, Long> reqUrlPvMap = new HashMap<String, Long>();
					if(vo.getReqUrlPvMap()!= null)
						reqUrlPvMap = vo.getReqUrlPvMap();
					
					ArrayList<Map.Entry<String, Long>>reqUrlPvList = new ArrayList<Map.Entry<String, Long>>(reqUrlPvMap.entrySet());
					Collections.sort(reqUrlPvList,compare);
					
					for(Integer i=0; i<reqUrlPvList.size();i++){
							Map.Entry<String, Long> entry = reqUrlPvList.get(i);
							String key = entry.getKey();
							String value = entry.getValue().toString();
							String keyName = "PV_REQUEST_URL_"+key+"_COUNTTIMES";
							Double proportion = entry.getValue()*1.0/allPv;
							String proportionStr = df.format(proportion);
							%>
								<tr>
								<td align="left"><%= key%></td>
								<td align="left"><%= Utlitites.fromatLong(value) %></td>
								<td align="left"><%= proportionStr%></td>
								</tr>
							<% 
					}
				}
				%>
			</table>	
		</td>
	</tr>
<%--z --%>
</table>
<%
Map<String, String> alarmMap = vo.getAlarmMap();
Map<String, String> tongbiAlarmMap = tongqiVo.getAlarmMap();
Map<String, String> huanbiAlarmMap = huanbiVo.getAlarmMap();

Set<String> alarmNameSet = new HashSet<String>();
alarmNameSet.addAll(alarmMap.keySet());
alarmNameSet.addAll(tongbiAlarmMap.keySet());
alarmNameSet.addAll(huanbiAlarmMap.keySet());
%>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="5"><font color="#000000" size="2">告警信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="250">告警点</td>
		<td align="center">次数</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
		<td align="center">基线</td>
	</tr>
	<%for(String alarmName:alarmNameSet){ %>
	<tr>
		<td><%=alarmName %></td>
		<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getAlarmKeyIdMap().get(alarmName)%>','<%=searchDate%>')"><%=alarmMap.get(alarmName)==null?" - ":alarmMap.get(alarmName) %></td>
		<td><%=Utlitites.scale(alarmMap.get(alarmName),tongbiAlarmMap.get(alarmName)) %></td>
		<td><%=Utlitites.scale(alarmMap.get(alarmName),huanbiAlarmMap.get(alarmName)) %></td>
		<td><%=Utlitites.scale(alarmMap.get(alarmName),getBaseValue(vo.getAlarmKeyIdMap().get(alarmName),appBaseMap)) %></td>
	</tr>
	<%} %>
</table>


<%
Map<String,AppSqlInfo> appSqlInfoMap = vo.getAppSqlInfoMap();
Map<String,AppSqlInfo> tongqiAppSqlInfoMap = tongqiVo.getAppSqlInfoMap();
Map<String,AppSqlInfo> huanbiAppSqlInfoMap = huanbiVo.getAppSqlInfoMap();
Set<String> dbNameSet = new HashSet<String>();
dbNameSet.addAll(appSqlInfoMap.keySet());
dbNameSet.addAll(tongqiAppSqlInfoMap.keySet());
dbNameSet.addAll(huanbiAppSqlInfoMap.keySet());

if(dbNameSet.size()>0){
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="16"><font color="#000000" size="2">北斗信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="80" rowspan="2" align="center">库名</td>
		<td width="80" rowspan="2" align="center">最大连接</td>
		<td width="80" rowspan="2" align="center">最小连接</td>
		<td width="80" rowspan="2" align="center">所有连接</td>
		<td colspan="4" align="center">平均执行次数</td>
		<td colspan="4" align="center">sql执行总数</td>
		<td colspan="3" align="center">总的sql数量</td>
	</tr>
	<tr class="ui-widget-header ">
		<td align="center">目前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
		<td align="center">基线</td>
		<td align="center">目前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
		<td align="center">基线</td>
		<td align="center">目前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
	</tr>
	<%
	
	for(String dbName:dbNameSet){ 
		AppSqlInfo info = appSqlInfoMap.get(dbName);
		AppSqlInfo tongbiInfo = tongqiAppSqlInfoMap.get(dbName);
		AppSqlInfo huanbiInfo = huanbiAppSqlInfoMap.get(dbName);
		if(tongbiInfo==null){
			tongbiInfo = new AppSqlInfo();
		}
		if(huanbiInfo==null){
			huanbiInfo = new AppSqlInfo();
		}
		if(info==null){
			info = new AppSqlInfo();
		}
	%>
	<tr>
		<td><%=dbName%></td>
		<td><%=info.getConnMax()%></td>
		<td><%=info.getConnMin()%></td>
		<td><%=info.getConnSum()%></td>
		<td><%=info.getExecAvg()%></td>
		<td><%=Utlitites.scale(info.getExecAvg(),tongbiInfo.getExecAvg()) %></td>
		<td><%=Utlitites.scale(info.getExecAvg(),huanbiInfo.getExecAvg()) %></td>
		<td><%=Utlitites.scale(info.getExecAvg(),getBaseValue(info.getExecAvgKeyId(),appBaseMap)) %></td>
		<td><%=info.getExecTotal()%></td>
		<td><%=Utlitites.scale(info.getExecTotal(),tongbiInfo.getExecTotal()) %></td>
		<td><%=Utlitites.scale(info.getExecTotal(),huanbiInfo.getExecTotal()) %></td>
		<td><%=Utlitites.scale(info.getExecTotal(),getBaseValue(info.getExecTotalKeyId(),appBaseMap)) %></td>
		<td><%=info.getSqlTotal()%></td>
		<td><%=Utlitites.scale(info.getSqlTotal(),tongbiInfo.getSqlTotal()) %></td>
		<td><%=Utlitites.scale(info.getSqlTotal(),huanbiInfo.getSqlTotal()) %></td>
	</tr>
	<%} %>
</table>
<%} %>

<%if(vo.getDataSourceMap().size()>0||vo.getThreadPo().size()>0||vo.getThreadPoolMap().size()>0){ %>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon ">
		<td align="left" colspan="7"><font color="#000000" size="2">MBean信息</font></td>
	</tr>
	<%if(vo.getDataSourceMap().size()>0){ %>
	<tr>
		<td align="left" colspan="7">连接池监控</td>
	</tr>
	<tr>
		<td align="left">数据源</td>
		<td align="left">MaxConnInUse</td>
		<td align="left">InUseConn</td>
		<td align="left">AvailableConn</td>
		<td align="left">ConnectionCount</td>
		<td align="left">MaxSize</td>
		<td align="left">MinSize</td>
	</tr>
	<%
		Iterator<Map.Entry<String,DataSourcePo>> it = vo.getDataSourceMap().entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,DataSourcePo> entry = it.next();
				DataSourcePo po = entry.getValue();
		%>
	<tr>
		<td align="left"><%=entry.getKey() %></td>
		<td align="left"><%=po.getMaxConnectionsInUseCount() %></td>
		<td align="left"><%=po.getInUseConnectionCount() %></td>
		<td align="left"><%=po.getAvailableConnectionCount() %></td>
		<td align="left"><%=po.getConnectionCount() %></td>
		<td align="left"><%=po.getMaxSize() %></td>
		<td align="left"><%=po.getMinSize() %></td>
	</tr>

	<%} }%>

	<%
		
		if(vo.getThreadPo().size()>0){
		%>
	<tr>
		<td align="left" colspan="7">线程监控</td>
	</tr>
	<tr>
		<td align="left">线程名</td>
		<td align="left">NEW</td>
		<td align="left">BLOCKED</td>
		<td align="left">RUNNABLE</td>
		<td align="left">WAITING</td>
		<td align="left">TERMINATED</td>
		<td align="left">TIMED_WAITING</td>
	</tr>
	<%
		
		Iterator<Map.Entry<String,ThreadPo>> it = vo.getThreadPo().entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,ThreadPo> entry = it.next();
				ThreadPo po = entry.getValue();
				
		%>
	<tr>
		<td align="left"><%=entry.getKey() %></td>
		<td align="left"><%=po.getNewThread() %></td>
		<td align="left"><%=po.getBlocked() %></td>
		<td align="left"><%=po.getRunnable() %></td>
		<td align="left"><%=po.getWaiting() %></td>
		<td align="left"><%=po.getTerminated() %></td>
		<td align="left"><%=po.getTimedWaiting() %></td>
	</tr>
	<%}} %>

	<%if(vo.getThreadPoolMap().size()>0){ %>

	<tr>
		<td align="left" colspan="7">线程池</td>
	</tr>

	<tr>
		<td align="left">线程池名</td>
		<td align="left">maxThreads</td>
		<td align="left">currentThreadCount</td>
		<td colspan="4" align="left">&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	<%
		
		Iterator<Map.Entry<String,ThreadPoolPo>> it = vo.getThreadPoolMap().entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,ThreadPoolPo> entry = it.next();
			ThreadPoolPo po = entry.getValue();
		%>
	<tr>
		<td align="left"><%=entry.getKey() %></td>
		<td align="left"><%=po.getMaxThreads() %></td>
		<td align="left"><%=po.getCurrentThreadCount() %></td>
		<td colspan="4" align="left">&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	<%}} %>
</table>
<%} %>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
<font color="#FF0000"> = OUT = </font>
</div>
<div class="ui-dialog-content ui-widget-content">
<%	
if(vo.getOutHsfList().size()>0){ 
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="19" width="100%"><font color="#000000" size="2">[ HSF ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td width="120" rowspan="2" align="center">类名</td>
	  <td width="80" rowspan="2" align="center">方法名</td>
	  <td colspan="4" align="center">执行次数</td>
	  <td colspan="4" align="center">平均执行时间(ms)</td>
	  <td colspan="4" align="center">Exception总数</td>
	  <td colspan="4" align="center">BizException总数</td>
  	</tr>
	<tr class="ui-widget-header ">
	  	<td width="220" align="center">当前</td>
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
		
		List<HsfPo> currentHsfList = vo.getOutHsfList();
		List<HsfPo> tongbiHsfList = tongqiVo.getOutHsfList();
		List<HsfPo> huanbiHsfList = huanbiVo.getOutHsfList();
		
		Collections.sort(currentHsfList);
		
		Map<String, HsfPo> tongbiMap = new HashMap<String, HsfPo>();
		for (HsfPo tongbiPo : tongbiHsfList) {
			String key = tongbiPo.getAim()+"_"+tongbiPo.getClassName()+"_"+tongbiPo.getMethodName();
			tongbiMap.put(key, tongbiPo);
		}
		
		Map<String, HsfPo> huanbiMap = new HashMap<String, HsfPo>();
		for (HsfPo huanbiPo : huanbiHsfList) {
			String key = huanbiPo.getAim()+"_"+huanbiPo.getClassName()+"_"+huanbiPo.getMethodName();
			tongbiMap.put(key, huanbiPo);
		}
		
		
		for (HsfPo po : currentHsfList) {
			
			String key = po.getAim()+"_"+po.getClassName()+"_"+po.getMethodName();
			%>					
			<tr>
				<td align="left" style="font-size:12px"><%=po.getClassName() %></td>
				<td align="left" style="font-size:12px"><%=po.getHtmlMethodName() %></td>
				
				<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),tongbiMap.get(key)==null?null:tongbiMap.get(key).getExeCount()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),huanbiMap.get(key)==null?null:huanbiMap.get(key).getExeCount()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>
				
				<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getAverageExe() %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),tongbiMap.get(key)==null?null:tongbiMap.get(key).getAverageExe()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),huanbiMap.get(key)==null?null:huanbiMap.get(key).getAverageExe()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>
				
				<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getExcCountkeyId()%>','<%=searchDate%>')"><%=po.getExecptionNum()==null?" - ":po.getExecptionNum() %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),tongbiMap.get(key)==null?null:tongbiMap.get(key).getExecptionNum()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),huanbiMap.get(key)==null?null:huanbiMap.get(key).getExecptionNum()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),getBaseValue(po.getExcCountkeyId(),appBaseMap)) %></td>
				
				<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getBizCountkeyId()%>','<%=searchDate%>')"><%=po.getBizExecptionNum()==null?" - ":po.getBizExecptionNum() %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),tongbiMap.get(key)==null?null:tongbiMap.get(key).getBizExecptionNum()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),huanbiMap.get(key)==null?null:huanbiMap.get(key).getBizExecptionNum()) %></td>
				<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getBizExecptionNum(),getBaseValue(po.getBizCountkeyId(),appBaseMap)) %></td>
			</tr>					
			<%
			
		}
		%>
</table>
<%} %>
<%if(vo.getOutSearchList().size()>0){ %>
<table width="100%" border="1" class="ui-widget ui-widget-content">

	<tr class="headcon">
		<td align="left" colspan="13" width="100%"><font color="#000000" size="2">[ SearchEngine ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center" width="300">URL</td>
	  <td rowspan="2" align="center">类型</td>
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">平均执行时间(ms)</td>
  </tr>
	<tr class="ui-widget-header ">
	  	<td align="center">当前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>					
		<td align="center">当前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>		
	</tr>
	<%
	
	Map<String, SearchEnginePo> tongbiOutSearchMap =tongqiVo.getOutSearchMap();	
	Map<String, SearchEnginePo> huanbiOutSearchMap =huanbiVo.getOutSearchMap()	;		
	List<SearchEnginePo> outSearchList =vo.getOutSearchList();
		Collections.sort(outSearchList);
		for(SearchEnginePo po:outSearchList){
			String key =po.getUrl()+"_"+po.getType();
			try{
	%>	
	<tr>
		<td align="left"><%=po.getUrl() %></td>
		<td align="center"><%=po.getType() %></td>
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeCount(),tongbiOutSearchMap.get(key)==null?null:tongbiOutSearchMap.get(key).getExeCount()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeCount(),huanbiOutSearchMap.get(key)==null?null:huanbiOutSearchMap.get(key).getExeCount()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>
						
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getExeAverage() %></td>
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),tongbiOutSearchMap.get(key)==null?null:tongbiOutSearchMap.get(key).getExeAverage()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),huanbiOutSearchMap.get(key)==null?null:huanbiOutSearchMap.get(key).getExeAverage()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>		
	</tr>
	<%}catch(Exception e){
		e.printStackTrace();
	}} %>
</table>
<%} %>
<%if(vo.getTairClientMap()!=null && vo.getTairClientMap().size()>0){ %>
<table width="100%" border="1" class="ui-widget ui-widget-content">	
	<tr>
		<td align="left" colspan="9" class="headcon"><font color="#000000" size="2"> [ TairClient ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">方法</td>
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">均值</td>
  	</tr>
  	<tr class="ui-widget-header ">
		<td align="center">当前</td>
		<td align="center">同比</td>				
		<td align="center">环比</td>	
		<td align="center">基线</td>	
		<td align="center">当前</td>
		<td align="center">同比</td>				
		<td align="center">环比</td>	
		<td align="center">基线</td>					
	</tr>
  	<%
	Map<String, TairClientPo> tairClientPoMap= vo.getTairClientMap();
  	
	for(Map.Entry<String, TairClientPo> entry:tairClientPoMap.entrySet()){
		String key = entry.getKey();
		TairClientPo po = entry.getValue(); 
		String aveDate = null;
	%>
	<tr>
	  	<td align="center"><%=po.getMethodName() %></td>
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeCount(),tongqiVo.getTairClientMap().get(key)==null?null:tongqiVo.getTairClientMap().get(key).getExeCount()) %></td>				
		<td align="center"><%=Utlitites.scale(po.getExeCount(),huanbiVo.getTairClientMap().get(key)==null?null:huanbiVo.getTairClientMap().get(key).getExeCount()) %></td>	
		<td align="center"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>	
	<%
	if (po.getAverageExe() != null && po.getMethodName().indexOf("hit") > -1) {
		aveDate = (Float.parseFloat(po.getAverageExe()) * 100) + "%(命中率)";
	} else if(po.getAverageExe() != null && po.getMethodName().indexOf("len") > -1) {
		aveDate = po.getAverageExe() + "(平均长度)";
	} else {
		aveDate = po.getAverageExe();
	}
	%>
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getAverageExe()==null?"-":aveDate %></td>
		<td align="center"><%=Utlitites.scale(po.getAverageExe(),tongqiVo.getTairClientMap().get(key)==null?null:tongqiVo.getTairClientMap().get(key).getAverageExe()) %></td>				
		<td align="center"><%=Utlitites.scale(po.getAverageExe(),huanbiVo.getTairClientMap().get(key)==null?null:huanbiVo.getTairClientMap().get(key).getAverageExe()) %></td>
		<td align="center"><%=Utlitites.scale(po.getAverageExe(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>	
	</tr>	
	<%} %>	
	
</table>
<%} %>

<%if(vo.getForestList().size()>0){ %>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="left" colspan="10" class="headcon"><font color="#000000" size="2"> [ forest ] </font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">类型</td>
	  <td rowspan="2" align="center">方法</td>
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">平均执行时间(ms)</td>
	</tr>
	<tr class="ui-widget-header ">
	    <td align="center">当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>						
		<td align="center">当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>			
	</tr>
	<%
	List<ForestPo> forestList =vo.getForestList();
	Collections.sort(forestList);
	for(ForestPo po:forestList){
		String key = po.getType()+"_"+po.getMethodName();
	%>
	<tr>				
		<td align="center"><%=po.getType() %></td>
		<td align="center"><%=po.getMethodName() %></td>
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
		<td align="center"><%=Utlitites.scale(po.getExeCount(),tongqiVo.getForestMap().get(key)==null?null:tongqiVo.getForestMap().get(key).getExeCount()) %></td>				
		<td align="center"><%=Utlitites.scale(po.getExeCount(),huanbiVo.getForestMap().get(key)==null?null:huanbiVo.getForestMap().get(key).getExeCount()) %></td>	
		<td align="center"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>	
					
		<td align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getExeAverage() %></td>
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),tongqiVo.getForestMap().get(key)==null?null:tongqiVo.getForestMap().get(key).getExeAverage()) %></td>				
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),huanbiVo.getForestMap().get(key)==null?null:huanbiVo.getForestMap().get(key).getExeAverage()) %></td>	
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>	
		
	</tr>
	<%} %>
</table>
<%} %>


<%if(vo.getPageCacheList().size()>0){ %>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="left" colspan="9" class="headcon"><font color="#000000" size="2"> [ PageCache ] </font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">类型</td>			 
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">平均执行时间</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center" >当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>						
		<td align="center">当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>			
	</tr>
	<%
	List<PageCachePo> pageCacheList =vo.getPageCacheList();
	Collections.sort(pageCacheList);
	for(PageCachePo po:pageCacheList){
		String key = po.getPageCacheName();
	%>
	<tr>				
		<td align="left"><%=po.getPageCacheName() %></td>				
		<td align="left"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>				
		<td align="left"><%=Utlitites.scale(po.getExeCount(),tongqiVo.getPageCacheMap().get(key)==null?null:tongqiVo.getPageCacheMap().get(key).getExeCount()) %></td>	
		<td align="left"><%=Utlitites.scale(po.getExeCount(),huanbiVo.getPageCacheMap().get(key)==null?null:huanbiVo.getPageCacheMap().get(key).getExeCount()) %></td>	
		<td align="center"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>	
					
		<td align="left"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getExeAverage() %></td>	
		<td align="left"><%=Utlitites.scale(po.getExeAverage(),tongqiVo.getPageCacheMap().get(key)==null?null:tongqiVo.getPageCacheMap().get(key).getExeAverage()) %></td>	
		<td align="left"><%=Utlitites.scale(po.getExeAverage(),huanbiVo.getPageCacheMap().get(key)==null?null:huanbiVo.getPageCacheMap().get(key).getExeAverage()) %></td>	
		<td align="center"><%=Utlitites.scale(po.getExeAverage(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>	
	</tr>
	<%} %>
</table>
<%} %>



<%
if(vo.getOtherKeyValueMap().size()>0){ 
for(Map.Entry<String, OtherKeyValueVo> entry:vo.getOtherKeyValueMap().entrySet()){
	String keyName = entry.getKey();
	OtherKeyValueVo keyValue = entry.getValue();	
	OtherKeyValueVo tongqikeyValue = tongqiVo.getOtherKeyValueMap().get(keyName);
	OtherKeyValueVo huanbikeyValue = huanbiVo.getOtherKeyValueMap().get(keyName);
	
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="left" colspan="9" class="headcon"><font color="#000000" size="2"> [ <%=keyName %> ] </font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">类型</td>			 
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">平均执行时间</td>
	</tr>
	<tr class="ui-widget-header ">
	  <td align="center" >当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>						
		<td align="center">当前</td>				
		<td align="center">同比</td>
		<td align="center">环比</td>	
		<td align="center">基线</td>			
	</tr>
	<%
	Map<String, OtherHaBoLogRecord> valueMap = keyValue.getKeyMap();
	
	List<Map.Entry<String, OtherHaBoLogRecord>> valueMapList = new ArrayList<Map.Entry<String, OtherHaBoLogRecord>>(valueMap.entrySet());
	
	Comparator<Map.Entry<String, OtherHaBoLogRecord>> compare1 = new Comparator<Map.Entry<String, OtherHaBoLogRecord>>(){
		public int compare(Map.Entry<String, OtherHaBoLogRecord> e1,Map.Entry<String, OtherHaBoLogRecord> e2){
			if (Long.parseLong(e2.getValue().getExeCount().getValueStr()) > Long.parseLong(e1.getValue().getExeCount().getValueStr())) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	

	try {
		Collections.sort(valueMapList, compare1);
	} catch (Exception e) {
		e.printStackTrace();
	}


	for(Map.Entry<String, OtherHaBoLogRecord> valueEntry : valueMapList){
		String valueName = valueEntry.getKey();
		OtherHaBoLogRecord record = valueEntry.getValue();
		if(record==null){
			continue;
		}
		
		Integer countId = record.getExeCount()==null?0:record.getExeCount().getKeyId();
		Integer averageId = record.getAverageExeTime()==null?0:record.getAverageExeTime().getKeyId();
		
		String count = record.getExeCount()==null?null:record.getExeCount().getValueStr();
		String average = record.getAverageExeTime()==null?null:record.getAverageExeTime().getValueStr();
		
		String tongqicount = null;
		String tongqiaverage = null;
		if(tongqikeyValue!=null){
			OtherHaBoLogRecord v = tongqikeyValue.getKeyMap().get(valueName);
			if(v!=null){
				tongqicount = v.getExeCount()==null?null:v.getExeCount().getValueStr();
				tongqiaverage = v.getAverageExeTime()==null?null:v.getAverageExeTime().getValueStr();
			}
			
		}
		
		String huanbicount = null;
		String huanbiaverage = null;
		if(huanbikeyValue!=null){
			OtherHaBoLogRecord v = huanbikeyValue.getKeyMap().get(valueName);
			if(v!=null){
				huanbicount = v.getExeCount()==null?null:v.getExeCount().getValueStr();
				huanbiaverage = v.getAverageExeTime()==null?null:v.getAverageExeTime().getValueStr();
			}
			
		}
		
	%>
	<tr>				
		<td align="left"><%=valueEntry.getKey() %></td>				
		<td align="left"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=countId%>','<%=searchDate%>')"><%=Utlitites.fromatLong(count) %></td>				
		<td align="left"><%=Utlitites.scale(count,tongqicount) %></td>	
		<td align="left"><%=Utlitites.scale(count,huanbicount) %></td>	
		<td align="center"><%=Utlitites.scale(count,getBaseValue(countId,appBaseMap)) %></td>	
					
		<td align="left"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=averageId%>','<%=searchDate%>')"><%=average %></td>	
		<td align="left"><%=Utlitites.scale(average,tongqiaverage) %></td>	
		<td align="left"><%=Utlitites.scale(average,huanbiaverage) %></td>	
		<td align="center"><%=Utlitites.scale(average,getBaseValue(averageId,appBaseMap)) %></td>	
	</tr>
	<%} %>
</table>
<%}} %>


</div>
</div>




<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
<font color="#FF0000">= IN = </font>
</div>
<div class="ui-dialog-content ui-widget-content">
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
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getCountkeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(po.getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getExeCount()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExeCount(),getBaseValue(po.getCountkeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getAverageKeyId()%>','<%=searchDate%>')"><%=po.getAverageExe() %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getAverageExe()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getAverageExe()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getAverageExe(),getBaseValue(po.getAverageKeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getExcCountkeyId()%>','<%=searchDate%>')"><%=po.getExecptionNum()==null?" - ":po.getExecptionNum() %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),tongbihsfPoMap.get(key)==null?null:tongbihsfPoMap.get(key).getExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),huanbihsfPoMap.get(key)==null?null:huanbihsfPoMap.get(key).getExecptionNum()) %></td>
					<td align="left"  style="font-size:10px"><%=Utlitites.scale(po.getExecptionNum(),getBaseValue(po.getExcCountkeyId(),appBaseMap)) %></td>
					
					<td align="left"  style="font-size:10px"><img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=vo.getAppId()%>','<%=po.getBizCountkeyId()%>','<%=searchDate%>')"><%=po.getBizExecptionNum()==null?" - ":po.getBizExecptionNum() %></td>
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
<%} %>

</div>


<!-- End demo --></div>



<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="key_detail.jsp" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
var groupMap ={};

function addAppGroup(company,groupName,appName,appId){
	
		if(!groupMap[company]){
			groupMap[company] = {};
		}
		
		if(!groupMap[company][groupName]){
			groupMap[company] [groupName]={};
		}
		if(!groupMap[company][groupName][appName]){
			groupMap[company][groupName][appName]=appId;
		}			
}



	function companyChange(selectObj){
		renderGroup("");
		renderApp("");
	}
	function renderCompany(name){
		
		clearSelect("companyGroupSelect");
		fillSelect("companyGroupSelect",groupMap,name);
		
	}
	
	
	function renderGroup(name){
		var companyObj = document.getElementById("companyGroupSelect");
		var company = companyObj.options[companyObj.selectedIndex].value;
		var group = groupMap[company];
		if(group){
			clearSelect("parentGroupSelect");
			fillSelect("parentGroupSelect",group,name);
		}
	}
	
	function renderApp(name){
		
		var companyObj = document.getElementById("companyGroupSelect");
		var company = companyObj.options[companyObj.selectedIndex].value;
		
		var groupObj = document.getElementById("parentGroupSelect");
		var groupName = groupObj.options[groupObj.selectedIndex].value;
		var apps = groupMap[company][groupName];
		
		
		if(apps){
			 clearSelect("appNameSelect")
			 fillSelect("appNameSelect",apps,name);
		}
	}
	
	function fillSelect(id,group,value){
		var ops = document.getElementById(id).options;
		var len = ops.length;
		for (name in group){
			if(typeof group[name]=== 'object'){
				document.getElementById(id).options[len++]=new Option(name,name);
				if(name == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}else{
				document.getElementById(id).options[len++]=new Option(name,group[name]);
				if(group[name] == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}
		}
	}
	
	
	
	
	function clearSelect(id){
		document.getElementById(id).options.length=0;		
	}
	
	
	
	function groupChange(selectObj){
		renderApp("");
	}
	
	function initParentSelect(com,gname,gvalue){
		renderCompany(com);
		renderGroup(gname);
		renderApp(gvalue);
	}
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
		ProductLine p = TBProductCache.getProductLineByAppName(app.getOpsName());
	%>
	addAppGroup("<%=p.getDevelopGroup()%>","<%=p.getProductline()%>","<%=app.getOpsName()%>","<%=app.getAppDayId()%>");
	<%				
	}
	
	ProductLine productLine = TBProductCache.getProductLineByAppName(appPo.getOpsName());
	
	%>
	 initParentSelect("<%=productLine.getDevelopGroup()%>","<%=productLine.getProductline()%>","<%=appPo.getAppDayId()%>");
</script>
<jsp:include page="buttom.jsp"></jsp:include>
</body>
</html>