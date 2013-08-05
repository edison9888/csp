<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorLoadRunAo"%>
<%@page import="com.taobao.monitor.capacity.ao.MaxValueAo"%>
<%@page import="com.taobao.monitor.capacity.po.MaxValueNameEnum"%>
<%@page import="com.taobao.monitor.capacity.po.MaxvaluePo"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.monitor.capacity.model.day.IndexMonthDayTrendModel"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.capacity.model.Coordinate"%>
<%@page import="com.taobao.monitor.capacity.ao.CapacityAo"%>
<%@page import="com.taobao.monitor.capacity.po.PvcountPo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.web.util.AmLineFlash"%>
<%@page import="com.taobao.monitor.capacity.model.day.BaseDayTrendModel"%>
<%@page import="com.taobao.monitor.capacity.model.equation.LineEquation"%>
<%@page import="com.taobao.monitor.capacity.po.CapacityAppPo"%>
<%@page import="com.taobao.monitor.capacity.model.equation.QuadraticEquation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.capacity.model.qps.QpsModel_N"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
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
.title_td{
 color:red;
 font-family:500;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<title>容量规划</title>
</head>
<body>

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String appIds = request.getParameter("appIds");
List<Integer> appIdList = new ArrayList<Integer>();
if(appIds != null && !appIds.equals("") && !appIds.equals(",")){
	String[] appIdArray = appIds.split(",");
	for(String appIdStr:appIdArray){
		appIdList.add(Integer.valueOf(appIdStr));
}}

Calendar cal = Calendar.getInstance();
int year = cal.get(Calendar.YEAR);

List<CapacityAppPo> capList = CapacityAo.get().findCapacityAppList();
for(CapacityAppPo capacityApp:capList){
	int appId = capacityApp.getAppId();
	AppInfoPo appPo = AppInfoAo.get().findAppInfoById(capacityApp.getAppId());
	cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	
	
	String pvkey = "PV";
	String qpsKey = "IN_QPS";
	String restKey = "IN_ResT";
	
	int qpsKeyId = 985;
	int pvkeyId = 982;
	int restKeyId = 3067;
	
	
	if("pv".equals(capacityApp.getAppType())){
		 qpsKeyId = 16931;
		 pvkeyId = 16929;
		 restKeyId = 16930;
	}
	if("hsf".equals(capacityApp.getAppType())){
		 qpsKeyId = 27734;
		 pvkeyId = 27733;
		 restKeyId = 27735;
	}
	
	
	
	KeyValuePo qpsKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(qpsKeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
	KeyValuePo pvKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(pvkeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
	KeyValuePo restKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(restKeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
	
	MaxvaluePo qpsKeyValuemax = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.PV_QPS_AVERAGEUSERTIMES.name());
	MaxvaluePo pvKeyValuemax = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.PV_VISIT_COUNTTIMES.name());
	MaxvaluePo restKeyValuemax = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.PV_REST_AVERAGEUSERTIMES.name());

%>
<table width="800" border="1">
  <tr class="headcon">
    <td colspan="2" align="center"><a title="点击查看详细数据" href="<%=request.getContextPath()%>/capacity/app_capacity.jsp?year=<%=year%>&appId=<%=appId%>"><%=appPo.getOpsName()%></a>&nbsp;&nbsp;[<%=sdf.format(cal.getTime()) %>]</td>
  </tr>
  <tr class="ui-widget-header ">
    <td colspan="2" align="left">运行情况</td>
  </tr>
  <tr>
    <td><table width="400" border="1">
      <tr>
        <td colspan="2">当前数据</td>
        </tr>
      <tr>
        <td width="118">QPS</td>
        <td width="266"><%if(qpsKeyValue != null){%><%=qpsKeyValue.getValueStr()%><%}%>
        </td>
      </tr>
      <tr>
        <td>平均Rest</td>
        <td><%if(restKeyValue != null){%><%=restKeyValue.getValueStr()%><%}%></td>
      </tr>
      <tr>
        <td>当前流量</td>
        <td><%if(pvKeyValue != null){%><%=pvKeyValue.getValueStr()%><%}%></td>
      </tr>
    </table></td>
    <td><table width="400" border="1">
      <tr>
        <td colspan="2">历史最高数据</td>
        </tr>
      <tr>
        <td>QPS</td>
        <td><%if(qpsKeyValuemax != null){%><%=qpsKeyValuemax.getMaxValue()%><%}%></td>
      </tr>
      <tr>
        <td>平均Rest</td>
        <td><%if(restKeyValuemax != null){%><%=restKeyValuemax.getMaxValue()%><%}%></td>
      </tr>
      <tr>
        <td>流量</td>
        <td><%if(pvKeyValuemax != null){%><%=pvKeyValuemax.getMaxValue()%><%}%></td>
      </tr>
    </table></td>
  </tr>
  <tr class="ui-widget-header ">
    <td colspan="2" align="left">容量情况</td>
  </tr>
  <%
  double qpsapp = MonitorLoadRunAo.get().findRecentlyAppLoadRunQps(appId);
  int[] machineNum = CspCacheTBHostInfos.get().getHostType(appPo.getOpsName());
  MaxvaluePo loadRunQps = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.LoadRunQps.name());
  MaxvaluePo virtualHost = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.App_Host_Virtual_Num.name());
  MaxvaluePo entityHost = MaxValueAo.get().getMaxvaluePo(appId,MaxValueNameEnum.App_Host_Entity_Num.name());
  if(loadRunQps == null)loadRunQps = new MaxvaluePo();
  if(virtualHost == null)virtualHost = new MaxvaluePo();
  if(entityHost == null)entityHost = new MaxvaluePo();
  %>
  <tr>
    <td >
    <table width="400" border="1">
	  <tr>
	    <td width="117">最新压测Qps</td>
	    <td width="267"><%=qpsapp%></td>
	  </tr>
	  <tr>
	    <td>最新机器数量</td>
	    <td>实体机:<%=machineNum[1] %>;虚拟机:<%=machineNum[0] %></td>
	  </tr>
	  <tr>
	    <td width="117">容量剩余率</td>
	    <td width="267">
	    <%
	      double surplusCapacity = 0;
	      try{
	    	  surplusCapacity = Arith.mul(Arith.div(qpsapp-Double.parseDouble(qpsKeyValue.getValueStr()),qpsapp,3) ,100);
	    %><%=surplusCapacity%><%
	      }catch(Exception e){}
	    %>%
	    </td>
	  </tr>
	  <tr>
	    <td width="117">机器使用率</td>
	    <td width="267">
	    <%
	      double machineUsageRates = 0;
	      try{
	    	  machineUsageRates = Arith.mul(Arith.div(Double.parseDouble(qpsKeyValue.getValueStr())*2*1.6,qpsapp,3) ,100);
	    %><%=machineUsageRates%><%
	      }catch(Exception e){}
	    %>%
	    </td>    
	  </tr>
	</table>
	</td>
	<td valign="top">
	<table width="400" border="1">
      <tr>
        <td width="294">压测Qps</td>
        <td width="90"><%=loadRunQps.getMaxValue()%></td>
      </tr>
      <tr>
        <td>机器数量</td>
        <td>实体机:<%=entityHost.getMaxValue() %>;虚拟机:<%=virtualHost.getMaxValue() %></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr class="ui-widget-header ">
    <td colspan="2" align="left">数据预测</td>
  </tr>
  <tr>
	<td colspan="2">
		<font  color="#FF3366" size="+2"  id="capactiy_message_id_<%=appId%>">在未来的三年内，以当前机器数量[<%=machineNum[0]+machineNum[1] %>]和应用的性能状态[<%=qpsapp %>] 在业务不发生变化的情况下系统将可以支撑</font>
	</td>
  </tr>
  </table>
  <%
  try{
  //日预测
  IndexMonthDayTrendModel m = new IndexMonthDayTrendModel(appId,year);
  
  BaseDayTrendModel dayModel = new BaseDayTrendModel(appId,year,new QuadraticEquation());
  
  List<Coordinate> dayFeatrueList = dayModel.getFutureDay();
  List<PvcountPo> curYearList = CapacityAo.get().findPvDataByYear(appId,year);
  
  Map<String,Map<String,Long>> dataMap = new HashMap<String,Map<String,Long>>();
  Map<String,Long> featureMap = new HashMap<String,Long>();
  
  for(Coordinate c:dayFeatrueList){
	  featureMap.put(c.getX()+"",(long)Arith.div(c.getY(),10000));
  }
  
  Map<String,Long> curentryMap = new HashMap<String,Long>();
  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
  for(PvcountPo c:curYearList){
	  curentryMap.put(sdf1.format(c.getCollectTime()),(long)Arith.div(c.getPvCount(),10000));
  }
  List<PvcountPo> pYearList = CapacityAo.get().findPvDataByYear(appId,year-1);
  Map<String,Long> pentryMap = new HashMap<String,Long>();
  for(PvcountPo c:pYearList){
	  pentryMap.put(sdf1.format(c.getCollectTime()),(long)Arith.div(c.getPvCount(),10000));
  }
  dataMap.put(year+"预测值",featureMap);
  dataMap.put(year+"实际值",curentryMap);
  
  String xml = AmLineFlash.createCommonCharXm2(dataMap);

  ////月预测
  
  List<Coordinate> monthFeatrueList = m.getFurureMonth();
  Map<String,Long> monthFeatureMap = new HashMap<String,Long>();
  Map<String,Map<String,Long>> monthDataMap = new HashMap<String,Map<String,Long>>();
  for(Coordinate c:monthFeatrueList){
	  monthFeatureMap.put((c.getX()+"").substring(4,6),(long)Arith.div(c.getY(),1000000));
  }
  
  
  Map<String,Long> monthEntryMap = new HashMap<String,Long>();
  SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
  for(PvcountPo c:curYearList){
	  String key = sdf2.format(c.getCollectTime());
	  Long k = monthEntryMap.get(key);
	  if(k==null){
		  monthEntryMap.put(key,(long)Arith.div(c.getPvCount(),1000000));
	  }else{
		  monthEntryMap.put(key,k+(long)Arith.div(c.getPvCount(),1000000));
	  }
	  
  }
  
  Map<String,Long> pMonthEntryMap = new HashMap<String,Long>();
  for(PvcountPo c:pYearList){
	  String key = sdf2.format(c.getCollectTime());
	  Long k = pMonthEntryMap.get(key);
	  if(k==null){
		  pMonthEntryMap.put(key,(long)Arith.div(c.getPvCount(),1000000));
	  }else{
		  pMonthEntryMap.put(key,k+(long)Arith.div(c.getPvCount(),1000000));
	  }
	  
  }

  monthDataMap.put(year+"预测值",monthFeatureMap);
  monthDataMap.put(year+"实际值",monthEntryMap);
  monthDataMap.put(year-1+"实际值",pMonthEntryMap);
  
  String xml2 = AmLineFlash.createCommonCharXm2(monthDataMap);

  QpsModel_N qpsmodel = new QpsModel_N(appId,cal.getTime());
  
  double curQps = Double.parseDouble(qpsKeyValue.getValueStr());
  double curPv =  Double.parseDouble(pvKeyValue.getValueStr());
  int curMachineNum = machineNum[0]+machineNum[1];
  
  Map<String,Double> featureQPSMap = new HashMap<String,Double>();
  for(Coordinate c:dayFeatrueList){
	  double featureQps = qpsmodel.getY(Double.valueOf(c.getY()).intValue());//预测的流量在高峰期可能达到的qps
	  featureQPSMap.put(c.getX()+"",featureQps);
  }
  
  Map<String,Double> entryQpsMap = new HashMap<String,Double>();
  List<KeyValuePo> poList = MonitorDayAo.get().findMonitorCountByDate(appPo.getAppDayId(),qpsKeyId,new Date(year-1900,0,1),new Date());
  
  Map<String,Double> mapMachine = MonitorDayAo.get().findAppMachineNumber(appPo.getAppDayId(),new Date(year-1900,0,1),new Date());
  
  for(KeyValuePo po:poList){
	  String key = sdf1.format(po.getCollectTime());
	  Double machines =  mapMachine.get(key);
		if(machines == null){
	  		machines =(double)curMachineNum;
	  	}
	  entryQpsMap.put(key,Double.parseDouble(po.getValueStr())*machines);
  }
  
  
  

  Map<String,Double> loadMap = MonitorLoadRunAo.get().findLoadrunQpsResult(appPo.getAppId(),new Date(year-1900,0,1),new Date());
  Map<String ,Double> yaceQpsMap = new HashMap<String,Double>();
  for(Map.Entry<String,Double> entry:loadMap.entrySet()){
  	String time = entry.getKey().replaceAll("-","");
  	Double machines =  mapMachine.get(time);
  	if(machines == null){
  		machines =(double)curMachineNum;
  	}
  	yaceQpsMap.put(time,entry.getValue()*machines);	
  }

  Map<String,Map<String,Double>> qpsDataMap = new HashMap<String,Map<String,Double>>();
  qpsDataMap.put(year+"压测QPS",yaceQpsMap);
  qpsDataMap.put(year+"实际QPS",entryQpsMap);
  qpsDataMap.put(year+"预测QPS",featureQPSMap);
  
  String xml3 = AmLineFlash.createCommonCharXmlExt(qpsDataMap);
  
  
  //机器预测
   
  
  Map<String,Double> featureMachineMap = new HashMap<String,Double>();
  Map<String,Double> savefeatureMachineMap = new HashMap<String,Double>();
  for(Coordinate c:dayFeatrueList){
	  double featureMachine =  qpsmodel.getY(Double.valueOf(c.getY()).intValue())/(qpsapp);  
	  featureMachineMap.put(c.getX()+"",featureMachine);
	  savefeatureMachineMap.put(c.getX()+"",featureMachine*3.2);
  }
  
  Map<Date,Integer> entryMachineMap = MonitorDayAo.get().findAppHostCount(appId,new Integer[]{992,993,14420},new Date(year-1900,0,1),new Date());
  
  Map<String,Double> entryMachineMapTmp = new HashMap<String,Double>();
  for(Map.Entry<Date,Integer> entry:entryMachineMap.entrySet()){
	  entryMachineMapTmp.put(sdf1.format(entry.getKey()),new Double(entry.getValue()));
  }
  
  
  Map<String,Map<String,Double>> machineDataMap = new HashMap<String,Map<String,Double>>();
  machineDataMap.put(year+"理论可支撑数",featureMachineMap);
  machineDataMap.put(year+"实际机器数",entryMachineMapTmp);
  machineDataMap.put(year+"安全机器数",savefeatureMachineMap);
  
  String xml4 = AmLineFlash.createCommonCharXmlExt(machineDataMap);
	String msg1 = null;
	String msg2 = null;
	for(int i=0;i<3;i++){
		for(int d=1;d<=365;d++){
			Coordinate cDay  = dayModel.getFutureDay(year+i,d);
			double featureQps = qpsmodel.getY(Double.valueOf(cDay.getY()).intValue());
			double featureMachine =  Math.round(Arith.div(featureQps,qpsapp));
			int machines = machineNum[0]+machineNum[1];
			double a = Arith.mul(featureMachine,3.2);
			double r = Arith.div((qpsapp-featureQps/machines),qpsapp,3);
			double u = Arith.div(a,machines,3);
			if(u >=0.8&&u <1){
				if(msg1 ==null)
					msg1 = cDay.getX()+"";
			}
			if(u>=1){
				if(msg2 ==null)
					msg2 = cDay.getX()+"";
			}
	}}
	String msg = "";
	if(msg1 != null){
		msg+="在["+msg1+"]可能出现应用容量不足的情况需要开始关注！！可以考虑增加机器";
	}
	if(msg2 != null){
		msg+="在["+msg2+"]应用开始处于危险的边缘!!需要马上增加机器部署";
	}
%>
<%if(msg!=null&&!"".equals(msg)){%>
<script type="text/javascript">
$("#capactiy_message_id_<%=appId%>").text("在未来的三年内，以当前机器数量[<%=machineNum[0]+machineNum[1] %>]和应用的性能状态[<%=qpsapp %>] 在业务不发生变化的情况下<%=msg%>");
</script>
<%}}catch(Exception e){}}%>
</body>
</html>