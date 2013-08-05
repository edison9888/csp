<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>

<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>监控</title>
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
	
}
img {
cursor:pointer;
}
</style>

</head>
<body>
<jsp:include page="report_head.jsp"></jsp:include>
<div>
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
<%

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date cDate = sdf.parse(searchDate);
Calendar cCal = Calendar.getInstance();
cCal.setTime(cDate);
cCal.set(Calendar.HOUR_OF_DAY,20);

Date startDate = cCal.getTime();
cCal.set(Calendar.HOUR_OF_DAY,22);
Date endDate = cCal.getTime();
List<Integer> appIdList = new ArrayList<Integer>();
appIdList.add(2);
appIdList.add(3);
appIdList.add(1);
appIdList.add(322);
appIdList.add(330);
appIdList.add(8);
appIdList.add(4);
appIdList.add(323);
appIdList.add(32);
appIdList.add(35);
appIdList.add(341);
appIdList.add(338);
	%>
<div id="everyReportCountSummary" style="text-align:left">
	      <table border="1" width="100%" class="ui-widget ui-widget-content">
        	<tr class="ui-widget-header ">        		
                <th align="center" >List(PV)</th>
                <th align="center">ShopSystem(PV)</th>
                <th align="center">Detail(PV)</th>
                <th align="center">Buy(PV)</th>
                <th align="center">Tm(PV)</th>
               <th align="center">临时添加购物车</th>
                <th align="center">会员添加购物车</th>
                <th align="center">立即购买次数</th>
                <th align="center">创建订单次数</th>
                <th align="center">订单付款</th>
                <th align="center">支付总额</th>
            </tr>
            <tr>            	
                <td align="left" class="alt"><%=map.get(2)==null?" - ":Utlitites.fromatLong(map.get(2).getApachePv()) %></td>
                <td align="left" class="alt"><%=map.get(3)==null?" - ":Utlitites.fromatLong(map.get(3).getPv()) %></td>
                <td align="left" class="alt"><%=map.get(1)==null?" - ":Utlitites.fromatLong(map.get(1).getApachePv()) %></td>
                <td align="left" class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getApachePv()) %></td>
                <td align="left" class="alt"><%=map.get(323)==null?" - ":Utlitites.fromatLong(map.get(323).getPv()) %></td>
                <td align="left"  class="alt"><%=map.get(341)==null?" - ":Utlitites.fromatLong(map.get(341).getShoppingCartTmp()) %></td>
                 <td align="left"  class="alt"><%=map.get(341)==null?" - ":Utlitites.fromatLong(map.get(341).getShoppingCartMember()) %></td>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getSubmitBuy()) %></td>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getCreateOrderCount()) %></td>
                  <td align="left" class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getPayOrderCount()) %></td>
                  <td align="left"  class="alt"><%=map.get(330)==null?" - ":Utlitites.fromatLong(map.get(330).getAmountAll())%></td>
            </tr>                    
         </table>
    </div>

<%
			
	for(Integer appid:appIdList){
		String opsName = AppCache.get().getDayAppId(appid).getOpsName();
		if(!appIdList.contains(appid)){
			continue;
		}
		AppInfoPo appPo = AppCache.get().getDayAppId(appid);
		MonitorVo vo = map.get(appid);
		MonitorVo tongqiVo = tongqiMap.get(appid);
		MonitorVo huanbiVo = huanqiMap.get(appid);
		
		Map<Integer,KeyValueBaseLinePo> appBaseMap = baseMap.get(appid);
		
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



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" > = <%=opsName %> = [<%=vo.getMachines()==null?" - ":vo.getMachines()%>]</font>&nbsp;&nbsp; <a href="http://cm.taobao.net:9999/monitorstat/app_detail.jsp?searchDate=<%=searchDate%>&selectAppId=<%=appid %>">详细</a></div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%">
	<tr class="headcon ">
		<td colspan="2" align="left"><font color="#000000" size="2">性能数据</font></td>
	</tr>
	<tr>
		<td valign="top">
		<table width="400" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td width="100" align="center">系统数据</td>
				<td width="90" align="center">当前</td>
				<td width="70" align="center">同比</td>
				<td width="70" align="center">环比</td>
				<td width="70" align="center">基线</td>
			</tr>
			<tr>
				<td>CPU</td>
				<td><%=cpu!=null?cpu:vo.getCpu() %></td>
				<td><%=Utlitites.scale(vo.getCpu(),tongqiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),huanbiVo.getCpu()) %></td>
				<td><%=Utlitites.scale(vo.getCpu(),getBaseValue(vo.getCpuKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>IOWAIT</td>
				<td><%=vo.getIowait()==null?" - ":vo.getIowait() %></td>
				<td><%=Utlitites.scale(vo.getIowait(),tongqiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),huanbiVo.getIowait()) %></td>
				<td><%=Utlitites.scale(vo.getIowait(),getBaseValue(vo.getIowaitKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Load</td>
				<td><%=load!=null?load:vo.getLoad() %></td>
				<td><%=Utlitites.scale(vo.getLoad(),tongqiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),huanbiVo.getLoad()) %></td>
				<td><%=Utlitites.scale(vo.getLoad(),getBaseValue(vo.getLoadKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Mem</td>
				<td><%=jvm!=null?jvm:vo.getMen() %></td>
				<td><%=Utlitites.scale(vo.getMen(),tongqiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),huanbiVo.getMen()) %></td>
				<td><%=Utlitites.scale(vo.getMen(),getBaseValue(vo.getMenKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Swap</td>
				<td><%=swap!=null?swap:vo.getSwap() %></td>
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
				<td><%=Utlitites.fromatLong(pv + "") %></td>
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
				<td><%=Utlitites.formatNull(rt_v+"") %></td>
				<td><%=Utlitites.scale(rt_v,tongbirt_v) %></td>
				<td><%=Utlitites.scale(rt_v,huanbirt_v) %></td>
				<td>-</td>
			</tr>
			<%}else{ %>
			<tr>
				<td>PV(哈勃)</td>
				<td><%=Utlitites.fromatLong(vo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),tongqiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),huanbiVo.getPv()) %></td>
				<td><%=Utlitites.scale(vo.getPv(),getBaseValue(vo.getPvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(哈勃)</td>
				<td><%=Utlitites.formatNull(vo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),tongqiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),huanbiVo.getQpsNum()) %></td>
				<td><%=Utlitites.scale(vo.getQpsNum(),getBaseValue(vo.getQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(哈勃)</td>
				<td><%=Utlitites.formatNull(vo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),tongqiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),huanbiVo.getRtNum()) %></td>
				<td><%=Utlitites.scale(vo.getRtNum(),getBaseValue(vo.getRtKeyId(),appBaseMap)) %></td>
			</tr>
						<tr>
				<td>PV(CSP)</td>
				<td><%=Utlitites.fromatLong(vo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),tongqiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),huanbiVo.getApachePv()) %></td>
				<td><%=Utlitites.scale(vo.getApachePv(),getBaseValue(vo.getApachePvKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Qps(CSP)</td>
				<td><%=Utlitites.formatNull(vo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),tongqiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),huanbiVo.getApacheQps()) %></td>
				<td><%=Utlitites.scale(vo.getApacheQps(),getBaseValue(vo.getApacheQpsKeyId(),appBaseMap)) %></td>
			</tr>
			<tr>
				<td>Rt(CSP)</td>
				<td><%=Utlitites.formatNull(vo.getApacheRest()) %></td>
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
				String m = "SELF_GC_"+gcname+"_AVERAGEMACHINEFLAG";
				String a = "SELF_GC_"+gcname+"_AVERAGEUSERTIMES";
				try{
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
				<td width="80"><%=cV %></td>
				<td width="80"><%=Utlitites.scale(cV,tongqiVo.getGcMap().get(m)!=null?tongqiVo.getGcMap().get(m).getKeyValue():null)%></td>
				<td width="80"><%=Utlitites.scale(cV,huanbiVo.getGcMap().get(m)!=null?huanbiVo.getGcMap().get(m).getKeyValue():null) %></td>
				<td width="80"><%=Utlitites.scale(cV,getBaseValue(cId,appBaseMap)) %></td>
				<td width="160"><%="GC_"+gcname %>时间</td>
				<td width="80"><%=aV %></td>
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
Map<String, String> tongbiAlarmMap = tongqiVo.getAlarmMap();
Map<String, String> huanbiAlarmMap = huanbiVo.getAlarmMap();

Set<String> alarmNameSet = new HashSet<String>();
alarmNameSet.addAll(alarmMap.keySet());
alarmNameSet.addAll(tongbiAlarmMap.keySet());
alarmNameSet.addAll(huanbiAlarmMap.keySet());



%>
<%if(alarmNameSet.size()>0){ %>
<table width="100%" border="1" class="ui-widget ui-widget-content">
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
</div>
<jsp:include page="report_buttom.jsp"></jsp:include>
</body>
</html>