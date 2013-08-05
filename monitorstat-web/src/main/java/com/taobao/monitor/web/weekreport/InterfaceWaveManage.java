
package com.taobao.monitor.web.weekreport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-14 下午01:24:04
 */
public class InterfaceWaveManage {

	private String WEEK_RISING_5_10 = "增长在5%和10%之间";
	private String WEEK_RISING_10_20 = "增长在10%和20%之间";
	private String WEEK_RISING_20_30 = "增长在20%和30%之间";
	private String WEEK_RISING_30_up = "增长在30%以上";
	private String WEEK_RISING_negative_5 = "增长在-5%之间";
	//Map<day,Map<appName,Map<keyName,KeyValuePo>>>
	private Map<String,Map<String,KeyValuePo>> cureentMap = new HashMap<String,Map<String,KeyValuePo>>();
	private Map<String,Map<String,KeyValuePo>> previousMap = new HashMap<String, Map<String,KeyValuePo>>();
					
	public  Date currentDay = null;	
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
			}
		}
		
	}
	/**
	 * 查询出一个星期内最大的一个key value 值
	 * @param keyName
	 * @param appName
	 * @return
	 */
	public double findMaxKeyValue(String keyName){		
		double maxValue = 0d;		
		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
			KeyValuePo po = entry.getValue().get(keyName);
			if(po!=null&&po.getValueStr()!=null){
				try{
				Double value = Double.parseDouble(po.getValueStr());
				if(value>maxValue){
					maxValue = value;
				}}catch (Exception e) {
				}
			}	
		}
		
		if("PV_REST_AVERAGEUSERTIMES".equals(keyName)){
			return Arith.div(maxValue, 1000, 2);
		}
		
		return maxValue;
	}
	
	/**
	 * 计算key 的增长趋势，计算方式为 本周与上周相同星期内的平均增长
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
		}
		}catch (Exception e) {
		}
		return -1d;
	}
	
	/**
	 * 当是c应用的时候调用这个
	 * 计算key 的增长趋势，计算方式为 本周与上周相同星期内的平均增长
	 * @param keyName
	 * @param appName
	 * @return
	 */
	public Double keyRisingTrend(){
		
		Set<String> daySet = new HashSet<String>();
		daySet.addAll(cureentMap.keySet());
		daySet.addAll(previousMap.keySet());
		
		Map<String,Double> risingMap = new HashMap<String, Double>();
		
		for(String day:daySet){			
			Map<String,KeyValuePo> cuurentkeyMap = cureentMap.get(day);			
			Map<String,KeyValuePo> previouskeyMap = previousMap.get(day);
			Double currentKeySum = 0d; 	//存储当天的总值
			Double previousKeySum = 0d; 	//存储当天的总值
			if(cuurentkeyMap!=null&&previouskeyMap!=null){			
				
				//求得这一周的c应用的总调用接口数（也就是以IN_HSF-ProviderDetail_开头，_COUNTTIMES结尾的key）
				for(Map.Entry<String, KeyValuePo> entry : cuurentkeyMap.entrySet()) {
					
					if(entry.getKey().toString().indexOf("IN_HSF-ProviderDetail_") > -1 
							&& entry.getKey().toString().indexOf("_COUNTTIMES") > -1){
						
						KeyValuePo currentPo = entry.getValue();
						currentKeySum += Double.parseDouble(currentPo.getValueStr());
					}
					
				}
				//求得上一周的c应用的总调用接口数（也就是以IN_HSF-ProviderDetail_开头，_COUNTTIMES结尾的key）
				for(Map.Entry<String, KeyValuePo> entry : previouskeyMap.entrySet()) {
					
					if(entry.getKey().toString().indexOf("IN_HSF-ProviderDetail_") > -1 
							&& entry.getKey().toString().indexOf("_COUNTTIMES") > -1){
						
						KeyValuePo currentPo = entry.getValue();
						previousKeySum += Double.parseDouble(currentPo.getValueStr());
					}
					
				}
				if(previousKeySum!=0){
					double cp = Arith.sub(currentKeySum, previousKeySum);
					double rising = Arith.div(cp, previousKeySum);						
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
			return Arith.div(rising, risingMap.size(), 4);			
		}
		return -1d;
	}
	
	public String keyRisingTrendStr(String keyName){
		double scale = 0d;
		if(keyName == null) {
			scale = keyRisingTrend();
		} else {
			scale = keyRisingTrend(keyName);
		}
		scale = Arith.mul(scale,100);
		if (scale < 0) {
			return "<font style=\"color: green;\">↓ " + scale+ "% </font>";
		} else if (scale == 0) {
			return "0.00%";
		} else {
			return "<font style=\"color: red;\">↑ " + scale + "%</font>";
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

				//为了不改变原来的代码，这里如果在前端传进来的keyId<0则代表是c应用
				if(keyId == -1) {
					aveValue = Double.parseDouble(MonitorDayAo.get().findCappQpsCountByDate(appId, sdf.format(day)));//返回的是20:30~22:30
					weekValue.put(dayStr, aveValue);	
				} else if(keyId == -2) {
					
					aveValue = Double.parseDouble(MonitorDayAo.get().findCappRtCountByDate(appId, sdf.format(day)));//返回的是20:30~22:30
					weekValue.put(dayStr, aveValue);	
				} else {
					
					//非c应用用以下代码处理
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
				}
				
			} catch (Exception e) {
			}
		}
		return weekValue;
	}
	
	/**
	 * 获取最大的qps值，若是c应用则传递一个负数作为执行c应用的一个标志
	 * @param keyId
	 * @return
	 */
	public double rushHourKeyMaxValue(int keyId){		
		Map<String, Double> valueMap = rushHourKeyMaxValue(keyId,this.currentDay);
		double  maxvalue = 0d;
		for(Map.Entry<String, Double> entry:valueMap.entrySet()){
			if(maxvalue < entry.getValue()){
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
	 * 与上周对比 本周 新增的接口
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
	 * 与上周对比 本周 减少的接口
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
	

	/**
	 * 返回count表的某一个key的m_data的值，如果是c应用keyName传进来的是null，则m_data为调用接口的总数
	 * @param keyName
	 * @return
	 */
	public long getAllKeyValueSum(String keyName){
		
		long maxvalue = 0l;
		//如果keyName为null，则是c应用的调用接口求和
		if(keyName == null) {
			
			for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
				for(Map.Entry<String , KeyValuePo> poEntry:entry.getValue().entrySet()){
	
					if(poEntry.getKey().toString().indexOf("IN_HSF-ProviderDetail_") > -1 
							&& entry.getKey().toString().indexOf("_COUNTTIMES") > -1){
						
						KeyValuePo currentPo = poEntry.getValue();
						maxvalue += Double.parseDouble(currentPo.getValueStr());
					}
				}
			}
		} else {
			
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
	 * 解析参数时间 对应本周内的天数
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
	
	
	
	public Map<String,Map<String,List<WeekReportInnerKeyValue>>> findInAndOutTop5(){		
		
		Map<String,List<WeekReportInnerKeyValue>> outmap = new HashMap<String,List<WeekReportInnerKeyValue>>();
		Map<String,List<WeekReportInnerKeyValue>> inmap = new HashMap<String,List<WeekReportInnerKeyValue>>();
		
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
					List<WeekReportInnerKeyValue> innerKeyList = inmap.get(type);
					if(innerKeyList==null){
						innerKeyList = new ArrayList<WeekReportInnerKeyValue>();
						inmap.put(type,innerKeyList);
					}					
					WeekReportInnerKeyValue innerKey = new WeekReportInnerKeyValue();
					innerKey.keyName = keyName;
					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
					innerKeyList.add(innerKey);
				}
				if(keyName.startsWith("OUT")){
					List<WeekReportInnerKeyValue> innerKeyList = outmap.get(type);
					if(innerKeyList==null){
						innerKeyList = new ArrayList<WeekReportInnerKeyValue>();
						outmap.put(type,innerKeyList);
					}
					
					WeekReportInnerKeyValue innerKey = new WeekReportInnerKeyValue();
					innerKey.keyName = keyName;
					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
					innerKeyList.add(innerKey);
				}
			}
		}
		
		Map<String,Map<String,List<WeekReportInnerKeyValue>>> map = new HashMap<String,Map<String,List<WeekReportInnerKeyValue>>>();		
		
		for(Map.Entry<String,List<WeekReportInnerKeyValue>> entry:outmap.entrySet()){
			String type = entry.getKey();
			List<WeekReportInnerKeyValue> list = entry.getValue();
			System.out.println(this.appName+"_"+type+":"+list.size());
			Collections.sort(list);			
			List<WeekReportInnerKeyValue> top5 = new ArrayList<WeekReportInnerKeyValue>();			
			for(int i=0;i<list.size();i++){
				WeekReportInnerKeyValue key = list.get(i);
				if(top5.size()>5){
					break;
				}
				if(!top5.contains(key)){
					System.out.println(type+":"+key.keyName);
					top5.add(key);
				}
			}
			
			Map<String,List<WeekReportInnerKeyValue>> outMap = map.get("OUT");
			if(outMap==null){
				outMap = new HashMap<String,List<WeekReportInnerKeyValue>>();
				map.put("OUT",outMap);
			}
			outMap.put(type,top5);			
		}
		
		for(Map.Entry<String,List<WeekReportInnerKeyValue>> entry:inmap.entrySet()){
			String type = entry.getKey();
			List<WeekReportInnerKeyValue> list = entry.getValue();
			System.out.println(this.appName+"_"+type+":"+list.size());
			Collections.sort(list);			
			List<WeekReportInnerKeyValue> top5 = new ArrayList<WeekReportInnerKeyValue>();			
			for(int i=0;i<list.size();i++){
				WeekReportInnerKeyValue key = list.get(i);
				if(top5.size()>5){
					break;
				}
				if(!top5.contains(key)){
					System.out.println(type+":"+key.keyName);
					top5.add(key);
				}
			}
			
			Map<String,List<WeekReportInnerKeyValue>> outMap = map.get("IN");
			if(outMap==null){
				outMap = new HashMap<String,List<WeekReportInnerKeyValue>>();
				map.put("IN",outMap);
			}
			outMap.put(type,top5);			
		}
		
		return map;
	}
	
	
	
	
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
}
