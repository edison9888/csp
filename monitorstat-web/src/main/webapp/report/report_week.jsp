<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.HashMap"%>

<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.util.Collections"%>
<%@page import="java.text.DecimalFormat"%><html xmlns="http://www.w3.org/1999/xhtml">

<%!
	class InnerInterfaceMsg{
		private String type;
		private String calssName;
		private String classMethod;
		private Double  execute;
		private Double executeTime;
		private Double excption;
		private Double excptionTime;
		private Double bizexcption;
		private Double bizexcptionTime;
	
	}

	class InnerKeyValue implements Comparable<InnerKeyValue>{
		private String keyName;
		private Double value;
		
		public boolean equals(Object obj) {
			if(obj instanceof InnerKeyValue){
				InnerKeyValue key = (InnerKeyValue)obj;
				if(keyName.equals(key.keyName)){
					return true;
				}
			}
			return false;
		}
		
		
		public int compareTo(InnerKeyValue o) {			
			if(value<(o.value)){
				return 1;
			}else if(value==(o.value)){
				return 0;
			}else if(value>(o.value)){
				return 0;
			}			
			return 0;
		}
		
	}

public class InterfaceWaveManage {
	
		
	private String WEEK_RISING_5_10 = "������5%��10%֮��";
	private String WEEK_RISING_10_20 = "������10%��20%֮��";
	private String WEEK_RISING_20_30 = "������20%��30%֮��";
	private String WEEK_RISING_30_up = "������30%����";
	private String WEEK_RISING_negative_5 = "������-5%֮��";
	//Map<day,Map<appName,Map<keyName,KeyValuePo>>>
	private Map<String,Map<String,KeyValuePo>> cureentMap = new HashMap<String,Map<String,KeyValuePo>>();
	private Map<String,Map<String,KeyValuePo>> previousMap = new HashMap<String, Map<String,KeyValuePo>>();
					
	private Date currentDay = null;	
	private String appName = null;
	private Integer appId;
	
	public InterfaceWaveManage(Date currentDay,String appName,Integer appId){
		this.currentDay = currentDay;
		this.appName = appName;
		this.appId = appId;
		init();
	}
	
	private void init(){		
		findWeekMonitorData();
	}
	
	private void findWeekMonitorData(){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDay);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		List<Date> currentWeekDay = parseWeekDay(cal.getTime());
		for(Date day:currentWeekDay){
			try {
				cal.setTime(day);
				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
				Map<String, KeyValuePo> map = MonitorDayAo.get().findMonitorCountKeyMapByDate(appId, sdf.format(day));
				cureentMap.put(dayStr, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cal.setTime(currentDay);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		List<Date> previousWeekDay = parseWeekDay(cal.getTime());
		for(Date day:previousWeekDay){
			try {
				cal.setTime(day);
				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
				Map<String, KeyValuePo> map = MonitorDayAo.get().findMonitorCountKeyMapByDate(appId,  sdf.format(day));
				previousMap.put(dayStr, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * ��ѯ��һ������������һ��key value ֵ
	 * @param keyName
	 * @param appName
	 * @return
	 */
	public double findMaxKeyValue(String keyName){		
		double maxValue = 0d;		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
			KeyValuePo po = entry.getValue().get(keyName);
			if(po!=null&&po.getValueStr()!=null){
				Double value = Double.parseDouble(po.getValueStr());
				if(value>maxValue){
					maxValue = value;
				}
			}	
		}				
		return maxValue;
	}
	
	/**
	 * ����key ���������ƣ����㷽ʽΪ ������������ͬ�����ڵ�ƽ������
	 * @param keyName
	 * @param appName
	 * @return
	 */
	public Double keyRisingTrend(String keyName){
		try{
		Set<String> daySet = new HashSet<String>();
		daySet.addAll(cureentMap.keySet());
		daySet.addAll(previousMap.keySet());
		
		Map<String,Double> risingMap = new HashMap<String, Double>();
		
		for(String day:daySet){			
			Map<String,KeyValuePo> cuurentkeyMap = cureentMap.get(day);			
			Map<String,KeyValuePo> previouskeyMap = previousMap.get(day);			
			if(cuurentkeyMap!=null&&previouskeyMap!=null){				
				KeyValuePo currentPo = cuurentkeyMap.get(keyName);
				KeyValuePo previousPo = previouskeyMap.get(keyName);
				if(currentPo!=null&&previousPo!=null){						
					Double currentValue = Double.parseDouble(currentPo.getValueStr());
					Double previousValue = Double.parseDouble(previousPo.getValueStr());					
									
					if(previousValue!=0){
						double cp = Arith.sub(currentValue, previousValue);
						double rising = Arith.div(cp, previousValue);						
						risingMap.put(day, rising);
					}else{
						risingMap.put(day, 0d);
					}
					
				}					
			}
		}	
		double rising = 0d;
		if(risingMap.size()>0){			
			for(Map.Entry<String, Double> entry:risingMap.entrySet()){				
				rising=Arith.add(rising, entry.getValue());				
			}			
			return Arith.div(rising, risingMap.size(), 4);			
		}}catch(Exception e){
			
		}
		return -1d;
	}
	
	
	public String keyRisingTrendStr(String keyName){
		double scale = keyRisingTrend(keyName);
		scale = Arith.mul(scale,100);
		if (scale < 0) {
			return "<font style=\"color: green;\">�� " + scale+ "% </font>";
		} else if (scale == 0) {
			return "0.00%";
		} else {
			return "<font style=\"color: red;\">�� " + scale + "%</font>";
		}
	}
	
	
	private Map<String, Double> rushHourKeyMaxValue(int keyId,Date collectday){
		
		Map<String, Double> weekValue = new HashMap<String, Double>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(collectday);
		SimpleDateFormat sdf1 = new SimpleDateFormat("HHmm");	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		List<Date> currentWeekDay = parseWeekDay(cal.getTime());		
		for(Date day:currentWeekDay){
			try {
				cal.setTime(day);
				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
				
				Double aveValue = 0d;
				int size = 0;
				List<KeyValuePo> listPo = MonitorDayAo.get().findMonitorDataListByTime(appId, keyId, sdf.format(day));
				for(KeyValuePo po:listPo){
					Date date = po.getCollectTime();	
					Double value = Double.parseDouble(po.getValueStr());
					int time = Integer.parseInt(sdf1.format(date))	;
					if(time>=2030&&time<=2230){
						aveValue= Arith.add(aveValue, value);
						size++;
					}
				}				
				if(size>0){
					aveValue = Arith.div(aveValue, size,2);						
					weekValue.put(dayStr, aveValue);					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return weekValue;
	}
	
	public double rushHourKeyMaxValue(int keyId){		
		Map<String, Double> valueMap = rushHourKeyMaxValue(keyId,this.currentDay);
		double  maxvalue = 0d;
		for(Map.Entry<String, Double> entry:valueMap.entrySet()){
			if(maxvalue<entry.getValue()){
				maxvalue = entry.getValue();
			}
		}
		
		return maxvalue;
	}
	
	
	public double rushHourKeyrisingTrend(Integer keyId){
		
		Map<String, Double> currentvalueMap = rushHourKeyMaxValue(keyId,this.currentDay);		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDay);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		Map<String, Double> perviousvalueMap = rushHourKeyMaxValue(keyId,cal.getTime());		
		
		Set<String> daySet = new HashSet<String>();
		daySet.addAll(currentvalueMap.keySet());
		daySet.addAll(perviousvalueMap.keySet());
		
		Map<String,Double> risingMap = new HashMap<String, Double>();
		
		for(String day:daySet){			
			Double cuurentValue = currentvalueMap.get(day);			
			Double previousValue = perviousvalueMap.get(day);			
			if(cuurentValue!=null&&previousValue!=null){				
				if(previousValue!=0){
					double cp = Arith.sub(cuurentValue, previousValue);
					double rising = Arith.div(cp, previousValue);						
					risingMap.put(day, rising);
				}else{
					risingMap.put(day, 0d);
				}				
			}
		}	
		double rising = 0d;
		if(risingMap.size()>0){			
			for(Map.Entry<String, Double> entry:risingMap.entrySet()){				
				rising=Arith.add(rising, entry.getValue());				
			}			
			return Arith.div(rising, risingMap.size(), 2);			
		}
		
		return -1d;
	}
	
	
	
	/**
	 * �����ܶԱ� ���� �����Ľӿ�
	 * @return
	 */
	public List<String> findNewAddKey(){		
		List<String> keyList = new ArrayList<String>();		
		Set<String> currentKeySet = new HashSet<String>();		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
			Map<String,KeyValuePo> keyMap = entry.getValue();
			if(keyMap!=null){
				currentKeySet.addAll(keyMap.keySet());		
			}
		}
		
		Set<String> previousMapKeySet = new HashSet<String>();
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:previousMap.entrySet()){
			Map<String,KeyValuePo> keyMap = entry.getValue();
			if(keyMap!=null){
				previousMapKeySet.addAll(keyMap.keySet());		
			}
			
		}		
		currentKeySet.removeAll(previousMapKeySet);		
		keyList.addAll(currentKeySet);
		return keyList;	
	}
	
	/**
	 * �����ܶԱ� ���� ���ٵĽӿ�
	 * @return
	 */
	public List<String> findLoseKey(){		
		List<String> keyList = new ArrayList<String>();		
		Set<String> currentKeySet = new HashSet<String>();		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
			Map<String,KeyValuePo> keyMap = entry.getValue();
			if(keyMap!=null){
				currentKeySet.addAll(keyMap.keySet());		
			}
		}
		
		Set<String> previousMapKeySet = new HashSet<String>();
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:previousMap.entrySet()){
			Map<String,KeyValuePo> keyMap = entry.getValue();
			if(keyMap!=null){
				previousMapKeySet.addAll(keyMap.keySet());		
			}
			
		}		
		previousMapKeySet.removeAll(currentKeySet);		
		keyList.addAll(previousMapKeySet);
		return keyList;	
	}
	
	public long getAllKeyValueSum(String keyName){
		
		long maxvalue = 0l;
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
			long dayvalue = 0l;
			for(Map.Entry<String , KeyValuePo> poEntry:entry.getValue().entrySet()){
				String key = poEntry.getKey();
				KeyValuePo po = poEntry.getValue();				
				if(key.indexOf(keyName)>-1){
					if(key.indexOf(Constants.COUNT_TIMES_FLAG)>0)
						dayvalue+=Long.parseLong(po.getValueStr());
				}
			}
			
			if(maxvalue<dayvalue){
				maxvalue=dayvalue;
			}
			
		}
		
		
		return maxvalue;
	}
	
	
	
	
	public Map<String,Map<String,Double>> findAllKeyRising(){
		
		Map<String,Map<String,Double>> keyRisingMap = new HashMap<String, Map<String,Double>>();
		
		
		Set<String> currentKeySet = new HashSet<String>();		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
			Map<String,KeyValuePo> keyMap = entry.getValue();
			if(keyMap!=null){
				currentKeySet.addAll(keyMap.keySet());		
			}
		}
		
		for(String key:currentKeySet){
			Double rising = keyRisingTrend(key);
			if(rising==null){
				continue;
			}
			if(rising>0){
				if(rising<=-0.05){
					putRising(WEEK_RISING_negative_5,key,rising,keyRisingMap);
				}
				if(rising>=0.05&&rising<0.1){
					putRising(WEEK_RISING_5_10,key,rising,keyRisingMap);
				}
				if(rising>=0.1&&rising<0.2){
					
					putRising(WEEK_RISING_10_20,key,rising,keyRisingMap);
				}
				if(rising>=0.2&&rising<0.3){
					putRising(WEEK_RISING_20_30,key,rising,keyRisingMap);
				}
				if(rising>=0.3){
					putRising(WEEK_RISING_30_up,key,rising,keyRisingMap);
				}
			}			
		}
		
		return keyRisingMap;
	}
	
	private void putRising(String flag,String key,double value,Map<String,Map<String,Double>> keyRisingMap){
		Map<String,Double> keyMap = keyRisingMap.get(flag);
		if(keyMap==null){
			keyMap = new HashMap<String, Double>();
			keyRisingMap.put(flag, keyMap);
		}		
		keyMap.put(key, value);
		
	}
	
	
	
	
	/**
	 * ��������ʱ�� ��Ӧ�����ڵ�����
	 * @param day
	 * @return
	 */
	private List<Date> parseWeekDay(Date day){		
		List<Date> dayList = new ArrayList<Date>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);		
		int week = cal.get(Calendar.DAY_OF_WEEK);
		int offest = (week-1)==0?7:(week-1);		
		for(int i=0;i<offest;i++){	
			dayList.add(cal.getTime());	
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}		
		return dayList;
	}
	
	public Date parseWeekFirestDay(Date day){
		List<Date> list = parseWeekDay(day);
		Collections.sort(list);
		return list.get(0);
		
	}
	
	
	
	public Map<String,Map<String,List<InnerKeyValue>>> findInAndOutTop5(){		
		
		Map<String,List<InnerKeyValue>> outmap = new HashMap<String,List<InnerKeyValue>>();
		Map<String,List<InnerKeyValue>> inmap = new HashMap<String,List<InnerKeyValue>>();
		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
			String weekDay = entry.getKey();
			Map<String,KeyValuePo> map = entry.getValue();
			for(Map.Entry<String,KeyValuePo> keyEntry:map.entrySet()){			
				String keyName = keyEntry.getKey();
				String[] keys = keyName.split("_");				
				if(keys.length<5){
					continue;
				}
				if(!keys[keys.length-1].equals(Constants.COUNT_TIMES_FLAG)){
					continue;
				}
				String type = keys[1];					
				if(!type.equals("HSF-Consumer")&&!type.equals("HSF-ProviderDetail")&&!type.equals("SearchEngine")){
					continue;
				}				
				String classname =  keys[2];
				String methodName = keys[3];
				
				if(keyName.startsWith("IN")){					
					List<InnerKeyValue> innerKeyList = inmap.get(type);
					if(innerKeyList==null){
						innerKeyList = new ArrayList<InnerKeyValue>();
						inmap.put(type,innerKeyList);
					}					
					InnerKeyValue innerKey = new InnerKeyValue();
					innerKey.keyName = keyName;
					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
					innerKeyList.add(innerKey);
				}
				if(keyName.startsWith("OUT")){
					List<InnerKeyValue> innerKeyList = outmap.get(type);
					if(innerKeyList==null){
						innerKeyList = new ArrayList<InnerKeyValue>();
						outmap.put(type,innerKeyList);
					}
					
					InnerKeyValue innerKey = new InnerKeyValue();
					innerKey.keyName = keyName;
					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
					innerKeyList.add(innerKey);
				}
			}
		}
		
		Map<String,Map<String,List<InnerKeyValue>>> map = new HashMap<String,Map<String,List<InnerKeyValue>>>();		
		
		for(Map.Entry<String,List<InnerKeyValue>> entry:outmap.entrySet()){
			String type = entry.getKey();
			List<InnerKeyValue> list = entry.getValue();
			System.out.println(this.appName+"_"+type+":"+list.size());
			Collections.sort(list);			
			List<InnerKeyValue> top5 = new ArrayList<InnerKeyValue>();			
			for(int i=0;i<list.size();i++){
				InnerKeyValue key = list.get(i);
				if(top5.size()>5){
					break;
				}
				if(!top5.contains(key)){
					System.out.println(type+":"+key.keyName);
					top5.add(key);
				}
			}
			
			Map<String,List<InnerKeyValue>> outMap = map.get("OUT");
			if(outMap==null){
				outMap = new HashMap<String,List<InnerKeyValue>>();
				map.put("OUT",outMap);
			}
			outMap.put(type,top5);			
		}
		
		for(Map.Entry<String,List<InnerKeyValue>> entry:inmap.entrySet()){
			String type = entry.getKey();
			List<InnerKeyValue> list = entry.getValue();
			System.out.println(this.appName+"_"+type+":"+list.size());
			Collections.sort(list);			
			List<InnerKeyValue> top5 = new ArrayList<InnerKeyValue>();			
			for(int i=0;i<list.size();i++){
				InnerKeyValue key = list.get(i);
				if(top5.size()>5){
					break;
				}
				if(!top5.contains(key)){
					System.out.println(type+":"+key.keyName);
					top5.add(key);
				}
			}
			
			Map<String,List<InnerKeyValue>> outMap = map.get("IN");
			if(outMap==null){
				outMap = new HashMap<String,List<InnerKeyValue>>();
				map.put("IN",outMap);
			}
			outMap.put(type,top5);			
		}
		
		return map;
	}
}


%>



<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����ϵͳ����</title>
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
	font-family: "����";
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
</head>

<body>
<%
	
	Date dateTime = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String current = request.getParameter("dateTime");
	Calendar cal = Calendar.getInstance();
	if(current!=null){
		dateTime = sdf.parse(current);
		cal.setTime(dateTime);
	}else{		
		dateTime = cal.getTime();
	}
	cal.add(Calendar.DAY_OF_MONTH,-1);
	
	dateTime = cal.getTime();
	
	
	
	List<Date> dayList = new ArrayList<Date>();
	
	int week = cal.get(Calendar.DAY_OF_WEEK);
	int offest = (week-1)==0?7:(week-1);		
	for(int i=0;i<offest;i++){
		dayList.add(cal.getTime());	
		cal.add(Calendar.DAY_OF_MONTH, -1);
	}	
	
	Collections.sort(dayList);
	Date fristDate = dayList.get(0);
	
	
%>
<%
List<AppInfoPo> appList = MonitorDayAo.get().findAllApp();
%>
<table width="100%" border="1" width="100%" border="1" class="ui-widget ui-widget-content">
  <tr class="headcon ">
    <td colspan="15" align="center">����Ӧ�����[<%=sdf.format(fristDate)+"-"+sdf.format(dateTime) %>]</td>
  </tr> 
  <tr class="headcon ">
    <td>Ӧ��</td>
    <td colspan="2" align="center">PV</td>
    <td colspan="2" align="center">Qps</td>
    <td colspan="2" align="center">RT</td>
    <td colspan="2" align="center">LOAD</td>
    <td colspan="2" align="center">FullGC����</td>
    <td colspan="2" align="center">FullGCʱ��</td>
    <td colspan="2" align="center">СGC����</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="center">���ֵ</td>    
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
    <td align="center">���ֵ</td>    
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
     <td align="center">���ֵ</td>   
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
  </tr> 
  <%
  List<String> needReportApp = new ArrayList<String>();
  needReportApp.add("list");
  needReportApp.add("shopsystem");
  needReportApp.add("item");
  needReportApp.add("buy");
  needReportApp.add("trademgr");
  needReportApp.add("ic");
  needReportApp.add("tbuic");
  needReportApp.add("shopcenter");
  needReportApp.add("tc");
  
  for(AppInfoPo appPo:appList){
		if(!needReportApp.contains(appPo.getAppName())){
			continue;
		}
		InterfaceWaveManage manage = new InterfaceWaveManage(dateTime,appPo.getAppName(),appPo.getAppId());
  %>
  
  <tr>
    <td ><%=appPo.getAppName()%>���</td>
    <%
    String appName = appPo.getAppName();
	if(appName.equals("ic")){
	%>
		<td align="center"><%=Utlitites.fromatLong((long)manage.getAllKeyValueSum("IN_HSF-ProviderDetail")+"")  %></td>
    	<td align="center"><%=manage.keyRisingTrendStr("IN_HSF-ProviderDetail_com.taobao.item.service.ItemQueryService:1.0.0-L0_queryItemById_COUNTTIMES") %></td>
		<td align="center"><%=Utlitites.formatDotTwo(Arith.div(manage.rushHourKeyMaxValue(2328),60)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(2328),100) %>%</td>
    	<td align="center"><%=Utlitites.formatDotTwo(manage.rushHourKeyMaxValue(2342)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(2342),100) %>%</td>
	<%
	}else if(appPo.getAppName().equals("shopcenter")){
	%>
		<td align="center"><%=Utlitites.fromatLong((long)manage.getAllKeyValueSum("IN_HSF-ProviderDetail")+"")  %></td>
    	<td align="center"><%=manage.keyRisingTrendStr("IN_HSF-ProviderDetail_com.taobao.shopservice.core.client.ShopReadService:1.0.0_queryShop_COUNTTIMES") %></td>
		<td align="center"><%=Utlitites.formatDotTwo(Arith.div(manage.rushHourKeyMaxValue(2753),60)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(2753),100) %>%</td>
    	<td align="center"><%=Utlitites.formatDotTwo(manage.rushHourKeyMaxValue(2621)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(2621),100) %>%</td>
    	
	<%	
	}else if(appPo.getAppName().equals("tbuic")){
	%>
		<td align="center"><%=Utlitites.fromatLong((long)manage.getAllKeyValueSum("IN_HSF-ProviderDetail")+"")  %></td>
    	<td align="center"><%=manage.keyRisingTrendStr("IN_HSF-ProviderDetail_com.taobao.uic.common.service.userinfo.UserReadService:1.0.0_getUserAndUserExtraById_COUNTTIMES") %></td>
		<td align="center"><%=Utlitites.formatDotTwo(Arith.div(manage.rushHourKeyMaxValue(3242),60)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(3242),100) %>%</td>
    	<td align="center"><%=Utlitites.formatDotTwo(manage.rushHourKeyMaxValue(3192)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(3192),100) %>%</td>
    	
	<%
	}else if(appPo.getAppName().equals("tc")){
	%>
		<td align="center"><%=Utlitites.fromatLong((long)manage.getAllKeyValueSum("IN_HSF-ProviderDetail")+"")  %></td>
    	<td align="center"><%=manage.keyRisingTrendStr("IN_HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail_COUNTTIMES") %></td>
		<td align="center"><%=Utlitites.formatDotTwo(Arith.div(manage.rushHourKeyMaxValue(1526),60)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(1526),100) %>%</td>
    	<td align="center"><%=Utlitites.formatDotTwo(manage.rushHourKeyMaxValue(1525)+"")%></td>
    	<td align="center"><%=Arith.mul(manage.rushHourKeyrisingTrend(1525),100) %>%</td>
	<%	
	}else{
    %>    
    <td align="center"><%=Utlitites.fromatLong(((long)manage.findMaxKeyValue("PV"))+"")%></td>
    <td align="center"><%=manage.keyRisingTrendStr("PV") %></td>
    <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("IN_QPS")) %>
    <%if(appPo.getAppName().equals("list")){out.print("����:"+Utlitites.formatDotTwo(80-manage.findMaxKeyValue("IN_QPS")));} %>
    <%if(appPo.getAppName().equals("item")){out.print("����:"+Utlitites.formatDotTwo(120-manage.findMaxKeyValue("IN_QPS")));} %>
     <%if(appPo.getAppName().equals("shopsystem")){out.print("����:"+Utlitites.formatDotTwo(129-manage.findMaxKeyValue("IN_QPS")));} %>
    </td>
    <td align="center"><%=manage.keyRisingTrendStr("IN_QPS") %></td>
    <td align="center">
    <%=Utlitites.formatDotTwo(manage.findMaxKeyValue("IN_ResT"))  %>
    </td>
    <td align="center"><%=manage.keyRisingTrendStr("IN_ResT") %></td>
    <%} %>    
    
    <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("SELF_Load_AVERAGEUSERTIMES")) %></td>
    <td align="center"><%=manage.keyRisingTrendStr("SELF_Load_AVERAGEUSERTIMES") %></td>
     <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("SELF_GC_Full_AVERAGEMACHINEFLAG")) %></td>
    <td align="center"><%=manage.keyRisingTrendStr("SELF_GC_Full_AVERAGEMACHINEFLAG") %></td>
    <%if(appPo.getAppName().equals("tc")){ %>
    <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("SELF_GC_CMS_AVERAGEMACHINEFLAG")) %></td>
    <td align="center"><%=manage.keyRisingTrendStr("SELF_GC_CMS_AVERAGEMACHINEFLAG")%></td>
    <%}else{ %>
    <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("SELF_GC_Full_AVERAGEUSERTIMES")) %></td>
    <td align="center"><%=manage.keyRisingTrendStr("SELF_GC_Full_AVERAGEUSERTIMES")%></td>
    <%} %>
    <td align="center"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue("SELF_GC_GC_AVERAGEMACHINEFLAG")) %></td>
    <td align="center"><%=manage.keyRisingTrendStr("SELF_GC_GC_AVERAGEMACHINEFLAG")%></td>
  </tr>
  <%} %>
</table>
<table>
	<tr>
		<td>���ֵ��</td>
		<td>
			ǰ��Ӧ��pv��ʾÿ�����ֵ�ڱ������������ֵ��CӦ�ñ�ʾ ���нӿ�һ������ܼ���һ������������ֵ��<br/>
			ǰ��Ӧ��QPS ��RT ���㷽ʽΪ �Ƚ�һ���20:30 ��22:30 ƽ����Ȼ��ȡ�������������죬<br/>
			CӦ��QPS��RT�������漸����Ҫ�ӿڴ��� <br/>
			ic��HSF-ProviderDetail_com.taobao.item.service.ItemQueryService:1.0.0-L0_queryItemById<br/>
			shopcenter��HSF-ProviderDetail_com.taobao.shopservice.core.client.ShopReadService:1.0.0_queryShop<br/>
			tbuic��HSF-ProviderDetail_com.taobao.uic.common.service.userinfo.UserReadService:1.0.0_getUserAndUserExtraById<br/>
			tc��HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail		<br/>	
			������20:30 ��22:30  �е�ÿ�����ݵ����л������ۼ�ֵ�ٳ��Է��Ӻ� �ӿڵ���ʱ�� ��ƽ�� Ȼ����ȡȻ��ȡ�������������죬<br/>
		</td>
	</tr>	
	<tr>
		<td>���ƣ�</td>
		<td>���������ܵ�һ���Ա����������һ������һ���ܶ������ܶ������ƣ������ʵ�һ��ƽ�� </td>
	</tr>
	<tr>
		<td>������</td>
		<td>����ѹ��ֵ-��ǰ���ֵ </td>
	</tr>
	<tr>
		<td>IN��</td>
		<td>��ʾ�ⲿϵͳ���ñ�ϵͳ�����ģ�һ����CӦ�� </td>
	</tr>
	<tr>
		<td>OUT��</td>
		<td>��ʾ��ϵͳ�����ⲿӦ��һ����ǰ��Ӧ�� </td>
	</tr>
</table>

<p>ǰ��Ӧ����Ҫpv����</p>
<%
InterfaceWaveManage manageBuy = new InterfaceWaveManage(dateTime,"buy",5);
InterfaceWaveManage managetrademgr = new InterfaceWaveManage(dateTime,"trademgr",6);
%>
<table width="100%" border="1">  
  <tr>
    <td width="50" rowspan="3" align="center">BUY</td>
    <td colspan="4" align="center"><%=Utlitites.fromatLong(((long)manageBuy.findMaxKeyValue("PV"))+"")%></td>
  </tr>
  <tr>
    <td width="100" align="center">�ύ��������</td>
    <td width="100" align="center">�ύ��������Exception</td>
    <td width="100" align="center">��ӵ����ﳵ</td>
    <td width="100" align="center">�鿴���ﳵ</td>
  </tr>
  <tr>
    <td align="center"><%=Utlitites.fromatLong(((long)manageBuy.findMaxKeyValue("SUBMITBUY_COUNTTIMES"))+"")%></td>
    <td align="center"><%=Utlitites.fromatLong(((long)manageBuy.findMaxKeyValue("SUBMITBUY_Exception_COUNTTIMES"))+"")%></td>
    <td align="center"><%=Utlitites.fromatLong(((long)manageBuy.findMaxKeyValue("SHOPPINGCART_COUNTTIMES"))+"")%></td>
    <td align="center">&nbsp;</td>
  </tr>
</table>
<table width="100%" border="1"> 
  <tr>
    <td width="50" rowspan="4" align="center">TM</td>
    <td colspan="6" align="center"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("PV"))+"")%></td>
  </tr>
  <tr>
    <td colspan="2" align="center">�����б�</td>
    <td colspan="2" align="center">��������ҳ��</td>
    <td colspan="2" align="center">�������б�</td>
  </tr>
  <tr>
    <td align="center">�ɹ�</td>
    <td align="center">ʧ��</td>
    <td align="center">�ɹ�</td>
    <td align="center">ʧ��</td>
    <td align="center">�ɹ�</td>
    <td align="center">ʧ��</td>
  </tr>
  <tr>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_�����б�-ok_COUNTTIMES"))+"")%></td>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_�����б�-error_COUNTTIMES"))+"")%></td>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_��������ҳ��-ok_COUNTTIMES"))+"")%></td>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_��������ҳ��-error_COUNTTIMES"))+"")%></td>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_�������б�-ok_COUNTTIMES"))+"")%></td>
    <td width="100"><%=Utlitites.fromatLong(((long)managetrademgr.findMaxKeyValue("OTHER_��������Ҫҳ��ͳ��_�������б�-error_COUNTTIMES"))+"")%></td>
  </tr>
</table>


<table>
	 <%
  for(AppInfoPo appPo:appList){
	  if(!needReportApp.contains(appPo.getAppName())){
			continue;
		}
		InterfaceWaveManage manage = new InterfaceWaveManage(dateTime,appPo.getAppName(),appPo.getAppId());
	  	  
  %>
  <tr class="headcon ">
    <td align="center"><%=appPo.getAppName()%>�ӿ����</td>
  </tr>   
  <tr>  	
    <td align="left">
    
    	<table width="100%" border="1"> 
    	  <%
    	  	try{
    	  	Map<String,Map<String,List<InnerKeyValue>>> keyMap =  manage.findInAndOutTop5();
    	  	for(Map.Entry<String,Map<String,List<InnerKeyValue>>> entry:keyMap.entrySet()){
    	  %>  
	      <tr class="headcon ">
	        <td align="center" colspan="5"><%
	        if("IN".equals(entry.getKey())){
	        	out.print("���ⲿϵͳ����");
	        }else if("OUT".equals(entry.getKey())){
	        	out.print("�����ⲿϵͳ");
	        }else {
	        	out.print(entry.getKey());
	        }  
	        
	        %> ����ǰ5λ</td>       
	      </tr>
	      <%
	      Map<String,List<InnerKeyValue>> typeMap = entry.getValue();
	      for(Map.Entry<String,List<InnerKeyValue>> typeentry:typeMap.entrySet()){
	      %>
	      <tr >
	      	<td align="left" >
	      	<%
	      	 if("HSF-Consumer".equals(typeentry.getKey())){
		        	out.print("���õĽӿ�");
		        }else if("HSF-ProviderDetail".equals(typeentry.getKey())){
		        	out.print("�����õĽӿ�");
		        }else {
		        	out.print(typeentry.getKey());
		        }  
	      	
	      	%></td>
	      	<td align="center" >����</td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      	<td align="center" >����</td>
	      </tr>
	      <%
	      List<InnerKeyValue> list =  typeentry.getValue();
	      for(InnerKeyValue key:list){
	    	  String keyName = key.keyName;
	    	  String[] keys = keyName.split("_");
	    	  
	    	  
	    	  String aver = keyName.replaceAll(Constants.COUNT_TIMES_FLAG,Constants.AVERAGE_USERTIMES_FLAG);
	      %>
	      <tr>
	      	<td align="left"><%=keys[2]+"_"+keys[3] %></td>
	      	<td align="left"><%=Utlitites.fromatLong((key.value.longValue())+"") %></td>
	      	<td align="left"><%=manage.keyRisingTrendStr(key.keyName) %></td>
	      	<td align="left"><%=Utlitites.formatDotTwo(manage.findMaxKeyValue(aver)) %></td>
	      	<td align="left"><%=manage.keyRisingTrendStr(aver) %></td>	      
	      </tr>	      
	     <%}}} }catch(Exception e){e.printStackTrace();}%>
	    </table>
    
  	</td>
  </tr>  
    
    
<%} %>    
</table>
</body>
</html>
