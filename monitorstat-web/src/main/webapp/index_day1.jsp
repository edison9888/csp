<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.ProductLine"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>应用监控系统-日报首页</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

<%!

private String getBaseValue(Integer keyId,Map<Integer,KeyValueBaseLinePo> appBaseMap){
	KeyValueBaseLinePo po = appBaseMap.get(keyId);
	if(po==null){
		return null;
	}else{
		return po.getBaseLineValue()+"";
	}
}

%>

<script type="text/javascript">



function goToAppDetail(){	
	var searchDate = $("#datepicker").val();
	var appid = $("#appNameSelect").val();
	location.href="app_detail.jsp?searchDate="+searchDate+"&selectAppId="+appid;
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
</head>
<body>
<center>
<jsp:include page="head.jsp"></jsp:include>
</center>

<%

List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

%>

<%//yyyy-MM-dd
String searchDate = request.getParameter("searchDate");
if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}
String tongbiDate = Utlitites.getTongBiMonitorDate(searchDate);
String huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);
Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(searchDate);
Map<Integer, MonitorVo> tongqiMap = MonitorDayAo.get().findMonitorCountMapByDate(tongbiDate);
Map<Integer, MonitorVo> huanqiMap = MonitorDayAo.get().findMonitorCountMapByDate(huanbiDate);
Map<Integer,Map<Integer,KeyValueBaseLinePo>> baseMap = MonitorDayAo.get().findMonitorBaseLine(null,searchDate);


%>
<center>
<div >
<select id="companyGroupSelect" onChange="companyChange(this)">	
			</select>
<select id="parentGroupSelect" onchange="groupChange(this)">	
</select>
<select id="appNameSelect" name="selectAppId">	
</select>
日期: <input type="text" id="datepicker" value="<%=searchDate%>"/><button  onclick="goToAppDetail()">查看日报详细</button><button  onclick="goToMonitorTime()">进入实时监控首页</button>
</div>
</center>


<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
       <tr><td><table border="1" class="ui-widget ui-widget-content" width="1000">
        	<tr class="ui-widget-header ">        		
                <th align="center" >List(PV)</th>
                <th align="center">ShopSystem(PV)</th>
                <th align="center">Detail(PV)</th>
                <th align="center">Buy(PV)</th>
                <th align="center">Tm(PV)</th>
                <th align="center">临时添加购物车</th>
                <th align="center">会员添加购物车</th>
                <th align="center">立即购买次数</th>
                <%if(false){ %>
                <th align="center">创建订单次数</th>
                <th align="center">订单付款</th>
                <th align="center">支付总额</th>  
                <%} %>
            </tr>
            <tr>            	
                <td align="left" class="alt"><%=map.get(2)==null?" - ":Utlitites.fromatLong(map.get(2).getPv()) %></td>
                <td align="left" class="alt"><%=map.get(3)==null?" - ":Utlitites.fromatLong(map.get(3).getPv()) %></td>
                <td align="left" class="alt"><%=map.get(1)==null?" - ":Utlitites.fromatLong(map.get(1).getPv()) %></td>
                <td align="left" class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getPv()) %></td>
                <td align="left" class="alt"><%=map.get(323)==null?" - ":Utlitites.fromatLong(map.get(323).getPv()) %></td>
                <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(341).getShoppingCartTmp()) %></td>
                 <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(341).getShoppingCartMember()) %></td>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getSubmitBuy()) %></td>
                  <%if(false){ %>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getCreateOrderCount()) %></td>
                  <td align="left" class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getPayOrderCount()) %></td>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getAmountAll())%></td>
                  <%} %>
            </tr>                    
         </table></td></tr></table>


<%
List<Integer> initViewList = new ArrayList<Integer>();
initViewList.add(1);
initViewList.add(2);
initViewList.add(3);
initViewList.add(4);
initViewList.add(322);
initViewList.add(330);
initViewList.add(32);
initViewList.add(8);
initViewList.add(323);


SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date cDate = sdf.parse(searchDate);
Calendar cCal = Calendar.getInstance();
cCal.setTime(cDate);
cCal.set(Calendar.HOUR_OF_DAY,20);

Date startDate = cCal.getTime();
cCal.set(Calendar.HOUR_OF_DAY,22);
Date endDate = cCal.getTime();

	for(Integer appadyId:initViewList){
		AppInfoPo appPo = AppCache.get().getDayAppId(appadyId);
		
		if(!initViewList.contains(appPo.getAppDayId())) {
			
			continue;
		}
		String opsName = appPo.getOpsName();
		
		MonitorVo vo = map.get(appPo.getAppDayId());
		MonitorVo tongqiVo = tongqiMap.get(appPo.getAppDayId());
		MonitorVo huanbiVo = huanqiMap.get(appPo.getAppDayId());
		
		Map<Integer,KeyValueBaseLinePo> appBaseMap = baseMap.get(appPo.getAppId());
		
		if(appBaseMap==null){
			appBaseMap = new HashMap<Integer,KeyValueBaseLinePo>();
		}
		
		if(vo==null){
			vo = new MonitorVo();
		}
		if(tongqiVo==null){
			tongqiVo = new MonitorVo();
		}
		if(huanbiVo==null){
			huanbiVo = new MonitorVo();
		}
		
		Double cpu= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),3113,startDate,endDate);
		Double load= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),944,startDate,endDate);
		Double swap= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),32781,startDate,endDate);
		Double jvm= MonitorTimeAo.get().findKeyTimeRangeAverageValue(appPo.getAppId(),7621,startDate,endDate);
		if(cpu <0){
			cpu =null;
		}
		if(load <0){
			load=null;
		}
		if(swap <0){
			swap = null;
		}
		if(jvm <0){
			jvm = null;
		}
		
		

%>




<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<center>
	<div  class="tt2" style="font-size:13px" >= <%=opsName %> = [<%=vo.getMachines()==null?" - ":vo.getMachines()%>]&nbsp;&nbsp; <a href="app_detail.jsp?searchDate=<%=searchDate%>&selectAppId=<%=appPo.getAppDayId() %>">详细</a></div>
	</center>
	<tr class="headcon ">
		<td colspan="2" align="left"><font color="#000000" size="2">性能数据</font></td>
	</tr>
	<tr><td><table width="1000" >
		<td valign="top">
		<table width="480" align="center"  border="1" class="ui-widget ui-widget-content" >
			<tr class="ui-widget-header ">
				<td width="100" align="center">系统数据</td>
				<td width="90" align="center">当前</td>
				<td width="70" align="center">同比</td>
				<td width="70" align="center">环比</td>
				<td width="70" align="center">基线</td>
			</tr>
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
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getMenKeyId()%>','<%=searchDate%>')"><%=jvm!=null?jvm:vo.getMen() %></td>
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
		</table>
		</td>
		<td valign="top">
		<table width="480" align="center" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td width="100" align="center">访问信息</td>
				<td width="90" align="center">当前</td>
				<td width="70" align="center">同比</td>
				<td width="70" align="center">环比</td>
				<td width="70" align="center">基线</td>
			</tr>
			<%
				//如果是c应用的话是通过统计调用接口的总数为pv，qps为每秒调用的接口数，rt为没秒的接口调用数
				if(appPo.getFeature().indexOf("in") > -1){ 
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
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%="c_pv"%>','<%=searchDate%>')"><%=Utlitites.fromatLong(pv + "") %></td>
				<td><%=Utlitites.scale(pv,tongbipv) %></td>
				<td><%=Utlitites.scale(pv,huanbipv) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>总接口QPS</td>
				<td><%=Utlitites.formatNull(qps+"") %>(高峰期 平均到<%=mNumber %>机器)</td>
				<td><%=Utlitites.scale(qps,tongbiqps) %></td>
				<td><%=Utlitites.scale(qps,huanbiqps) %></td>
				<td>-</td>
			</tr>
			<tr>
				<td>总接口调用时间</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%="c_rt"%>','<%=searchDate%>')"><%=Utlitites.formatNull(rt_v+"") %></td>
				<td><%=Utlitites.scale(rt_v,tongbirt_v) %></td>
				<td><%=Utlitites.scale(rt_v,huanbirt_v) %></td>
				<td>-</td>
			</tr>
			<%}else { %>
			<tr>
				<td>PV</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getPvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),tongqiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),huanbiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),getBaseValue(vo.getPvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=qps&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appPo.getAppName() %>')"/><%=Utlitites.formatNull(vo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),tongqiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),huanbiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),getBaseValue(vo.getQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="window.open('http://cm.taobao.net:9999/relation/qps.jsp?type=rt&cm=cm3&time_type=day&hour=00&urlType=&app=<%=appPo.getAppName()  %>')"/><%=Utlitites.formatNull(vo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),tongqiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),huanbiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),getBaseValue(vo.getRtKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>PV(apache)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApachePvKeyId()%>','<%=searchDate%>')"><%=Utlitites.fromatLong(vo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),tongqiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),huanbiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),getBaseValue(vo.getApachePvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(apache)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheQpsKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),tongqiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),huanbiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),getBaseValue(vo.getApacheQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(apache)</td>
				<td><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=vo.getApacheRestKeyId()%>','<%=searchDate%>')"/><%=Utlitites.formatNull(vo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),tongqiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),huanbiVo.getApacheRest()) %></td>
				<td><%=Utlitites.scale(vo.getApacheRest(),getBaseValue(vo.getApacheRestKeyId(),appBaseMap)) %></td>
			</tr>
			<%} %>
			</table>
		</td></table></td>
	</tr>
	<tr>
		<td colspan="2">		
			<table width="1000" border="1" class="ui-widget ui-widget-content">
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
				String m = "SELF_GC_"+gcname+"_AVERAGEMACHINEFLAG";
				String a = "SELF_GC_"+gcname+"_AVERAGEUSERTIMES";
				
				if(vo.getGcMap().get(m)==null){
					continue;
				}
				try{
				String cV = Utlitites.formatNull(vo.getGcMap().get(m).getKeyValue());
				String aV = Utlitites.formatNull(vo.getGcMap().get(a).getKeyValue());
				
				
				Integer cId = vo.getGcMap().get(m).getKeyId();
				Integer aId = vo.getGcMap().get(a).getKeyId();
			%>
			<tr >
				<td width="160"><%="GC_"+gcname %>次数</td>
				<td width="80"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=cId%>','<%=searchDate%>')"><%=cV %></td>
				<td width="80"><%=Utlitites.scale(cV,tongqiVo.getGcMap().get(m)!=null?tongqiVo.getGcMap().get(m).getKeyValue():null)%></td>
				<td width="80"><%=Utlitites.scale(cV,huanbiVo.getGcMap().get(m)!=null?huanbiVo.getGcMap().get(m).getKeyValue():null) %></td>
				<td width="80"><%=Utlitites.scale(cV,getBaseValue(cId,appBaseMap)) %></td>
				<td width="160"><%="GC_"+gcname %>时间</td>
				<td width="80"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openKeyDetail('<%=vo.getAppId()%>','<%=aId%>','<%=searchDate%>')"><%=aV %></td>
				<td width="80"><%=Utlitites.scale(aV,tongqiVo.getGcMap().get(a)!=null?tongqiVo.getGcMap().get(a).getKeyValue():null)%></td>
				<td width="80"><%=Utlitites.scale(aV,huanbiVo.getGcMap().get(a)!=null?huanbiVo.getGcMap().get(a).getKeyValue():null) %></td>
				<td width="80"><%=Utlitites.scale(aV,getBaseValue(aId,appBaseMap)) %></td>
			</tr>			
			<%}catch(Exception e){}} %>		
			</table>
		</td>
	</tr>
</table>
<%
Map<String, String> alarmMap = vo.getAlarmMap();
if(alarmMap==null){
	alarmMap = new HashMap<String, String>();
}
Map<String, String> tongbiAlarmMap = tongqiVo.getAlarmMap();
if(tongbiAlarmMap==null){
	tongbiAlarmMap = new HashMap<String, String>();
}
Map<String, String> huanbiAlarmMap = huanbiVo.getAlarmMap();
if(huanbiAlarmMap==null){
	huanbiAlarmMap = new HashMap<String, String>();
}
Set<String> alarmNameSet = new HashSet<String>();
alarmNameSet.addAll(alarmMap.keySet());
alarmNameSet.addAll(tongbiAlarmMap.keySet());
alarmNameSet.addAll(huanbiAlarmMap.keySet());



%>
<%if(alarmNameSet.size()>0){ %>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr class="headcon ">
		<td align="left" colspan="4"><font color="#000000" size="2">告警信息</font></td>
	</tr>
	<tr class="ui-widget-header ">
		<td width="250" align="center">告警点</td>
		<td align="center">次数</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
	</tr>
	<%for(String alarmName:alarmNameSet){ %>
	<tr>
		<td align="center"><%=alarmName %></td>
		<td align="center"><%=alarmMap.get(alarmName)==null?" - ":alarmMap.get(alarmName) %></td>
		<td align="center"><%=Utlitites.scale(alarmMap.get(alarmName),tongbiAlarmMap.get(alarmName)) %></td>
		<td align="center"><%=Utlitites.scale(alarmMap.get(alarmName),huanbiAlarmMap.get(alarmName)) %></td>
	</tr>
	<%} %>
</table>
<%} %>
</div>
</div>
<%} %>

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
	
	ProductLine productLine = TBProductCache.getProductLineByAppName(appList.get(0).getOpsName());
	
	%>
	 initParentSelect("<%=productLine.getDevelopGroup()%>","<%=productLine.getProductline()%>","<%=appList.get(0).getAppDayId()%>");
</script>







<center>
    <tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
    </tr>
<jsp:include page="bottom.jsp"></jsp:include>
</center>
</body>
</html>