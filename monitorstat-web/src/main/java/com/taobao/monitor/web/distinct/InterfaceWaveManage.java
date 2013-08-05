//package com.taobao.monitor.web.distinct;
//
//
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.monitor.common.util.Arith;
//import com.taobao.monitor.common.util.Constants;
//import com.taobao.monitor.web.ao.MonitorDayAo;
//import com.taobao.monitor.common.po.KeyValuePo;
//
///**
// * 
// * @author xiaodu
// * @version 2010-6-9 下午04:26:29
// */
//public class InterfaceWaveManage {
//	
//	private String WEEK_RISING_5_10 = "增长在5%和10%之间";
//	private String WEEK_RISING_10_20 = "增长在10%和20%之间";
//	private String WEEK_RISING_20_30 = "增长在20%和30%之间";
//	private String WEEK_RISING_30_up = "增长在30%以上";
//	private String WEEK_RISING_negative_5 = "增长在-5%之间";
//	private Map<String,Map<String,KeyValuePo>> cureentMap = new HashMap<String,Map<String,KeyValuePo>>();
//	private Map<String,Map<String,KeyValuePo>> previousMap = new HashMap<String, Map<String,KeyValuePo>>();
//					
//	private Date currentDay = null;	
//	private String appName = null;
//	private Integer appId;
//	
//	public InterfaceWaveManage(Date currentDay,String appName,Integer appId){
//		this.currentDay = currentDay;
//		this.appName = appName;
//		this.appId = appId;
//		init();
//	}
//	
//	private void init(){		
//		findWeekMonitorData();
//	}
//	/**
//	 * 查询出一个一个应用一周内的
//	 */
//	private void findWeekMonitorData(){
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(currentDay);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
//		List<Date> currentWeekDay = parseWeekDay(cal.getTime());
//		for(Date day:currentWeekDay){
//			try {
//				cal.setTime(day);
//				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
//				Map<String, KeyValuePo> map = MonitorDayAo.get().findMonitorCountKeyMapByDate(appName, sdf.format(day));
//				cureentMap.put(dayStr, map);
//			} catch (Exception e) {
//			}
//		}
//		cal.setTime(currentDay);
//		cal.add(Calendar.DAY_OF_MONTH, -7);
//		
//		List<Date> previousWeekDay = parseWeekDay(cal.getTime());
//		for(Date day:previousWeekDay){
//			try {
//				cal.setTime(day);
//				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
//				Map<String, KeyValuePo> map = MonitorDayAo.get().findMonitorCountKeyMapByDate(appName,  sdf.format(day));
//				previousMap.put(dayStr, map);
//			} catch (Exception e) {
//			}
//		}
//		
//	}
//	/**
//	 * 查询出一个星期内最大的一个key value 值
//	 * @param keyName
//	 * @param appName
//	 * @return
//	 */
//	public double findMaxKeyValue(String keyName){		
//		double maxValue = 0d;		
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
//			KeyValuePo po = entry.getValue().get(keyName);
//			if(po!=null){
//				Double value = Double.parseDouble(po.getValueStr());
//				if(value>maxValue){
//					maxValue = value;
//				}
//			}	
//		}				
//		return maxValue;
//	}
//	
//	/**
//	 * 计算key 的增长趋势，计算方式为 本周与上周相同星期内的平均增长
//	 * @param keyName
//	 * @param appName
//	 * @return
//	 */
//	public Double keyRisingTrend(String keyName){
//		
//		Set<String> daySet = new HashSet<String>();
//		daySet.addAll(cureentMap.keySet());
//		daySet.addAll(previousMap.keySet());
//		
//		Map<String,Double> risingMap = new HashMap<String, Double>();
//		
//		for(String day:daySet){			
//			Map<String,KeyValuePo> cuurentkeyMap = cureentMap.get(day);			
//			Map<String,KeyValuePo> previouskeyMap = previousMap.get(day);			
//			if(cuurentkeyMap!=null&&previouskeyMap!=null){				
//				KeyValuePo currentPo = cuurentkeyMap.get(keyName);
//				KeyValuePo previousPo = previouskeyMap.get(keyName);
//				if(currentPo!=null&&previousPo!=null){						
//					Double currentValue = Double.parseDouble(currentPo.getValueStr());
//					Double previousValue = Double.parseDouble(previousPo.getValueStr());					
//									
//					if(previousValue!=0){
//						double cp = Arith.sub(currentValue, previousValue);
//						double rising = Arith.div(cp, previousValue);						
//						risingMap.put(day, rising);
//					}else{
//						risingMap.put(day, 0d);
//					}
//					
//				}					
//			}
//		}	
//		double rising = 0d;
//		if(risingMap.size()>0){			
//			for(Map.Entry<String, Double> entry:risingMap.entrySet()){				
//				rising=Arith.add(rising, entry.getValue());				
//			}			
//			return Arith.div(rising, risingMap.size(), 2);			
//		}
//		return -1d;
//	}
//	
//	
//	private Map<String, Double> rushHourKeyMaxValue(int keyId,Date collectday){
//		
//		Map<String, Double> weekValue = new HashMap<String, Double>();
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(collectday);
//		SimpleDateFormat sdf1 = new SimpleDateFormat("HHmm");	
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
//		List<Date> currentWeekDay = parseWeekDay(cal.getTime());		
//		for(Date day:currentWeekDay){
//			try {
//				cal.setTime(day);
//				String dayStr = cal.get(Calendar.DAY_OF_WEEK)+"";
//				
//				Double aveValue = 0d;
//				int size = 0;
//				List<KeyValuePo> listPo = MonitorDayAo.get().findMonitorDataListByTime(appId, keyId, sdf.format(day));
//				for(KeyValuePo po:listPo){
//					Date date = po.getCollectTime();	
//					Double value = Double.parseDouble(po.getValueStr());
//					int time = Integer.parseInt(sdf1.format(date))	;
//					if(time>=2030&&time<=2230){
//						aveValue= Arith.add(aveValue, value);
//						size++;
//					}
//				}				
//				if(size>0){
//					aveValue = Arith.div(aveValue, size,2);						
//					weekValue.put(dayStr, aveValue);					
//				}
//				
//			} catch (Exception e) {
//			}
//		}
//		return weekValue;
//	}
//	
//	public double rushHourKeyMaxValue(int keyId){		
//		Map<String, Double> valueMap = rushHourKeyMaxValue(keyId,this.currentDay);
//		double  maxvalue = 0d;
//		for(Map.Entry<String, Double> entry:valueMap.entrySet()){
//			if(maxvalue<entry.getValue()){
//				maxvalue = entry.getValue();
//			}
//		}
//		
//		return maxvalue;
//	}
//	
//	
//	public double rushHourKeyrisingTrend(Integer keyId){
//		
//		Map<String, Double> currentvalueMap = rushHourKeyMaxValue(keyId,this.currentDay);		
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(currentDay);
//		cal.add(Calendar.DAY_OF_MONTH, -7);
//		
//		Map<String, Double> perviousvalueMap = rushHourKeyMaxValue(keyId,cal.getTime());		
//		
//		Set<String> daySet = new HashSet<String>();
//		daySet.addAll(currentvalueMap.keySet());
//		daySet.addAll(perviousvalueMap.keySet());
//		
//		Map<String,Double> risingMap = new HashMap<String, Double>();
//		
//		for(String day:daySet){			
//			Double cuurentValue = currentvalueMap.get(day);			
//			Double previousValue = perviousvalueMap.get(day);			
//			if(cuurentValue!=null&&previousValue!=null){				
//				if(previousValue!=0){
//					double cp = Arith.sub(cuurentValue, previousValue);
//					double rising = Arith.div(cp, previousValue);						
//					risingMap.put(day, rising);
//				}else{
//					risingMap.put(day, 0d);
//				}				
//			}
//		}	
//		double rising = 0d;
//		if(risingMap.size()>0){			
//			for(Map.Entry<String, Double> entry:risingMap.entrySet()){				
//				rising=Arith.add(rising, entry.getValue());				
//			}			
//			return Arith.div(rising, risingMap.size(), 2);			
//		}
//		
//		return -1d;
//	}
//	
//	
//	
//	/**
//	 * 与上周对比 本周 新增的接口
//	 * @return
//	 */
//	public List<String> findNewAddKey(){		
//		List<String> keyList = new ArrayList<String>();		
//		Set<String> currentKeySet = new HashSet<String>();		
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
//			Map<String,KeyValuePo> keyMap = entry.getValue();
//			if(keyMap!=null){
//				currentKeySet.addAll(keyMap.keySet());		
//			}
//		}
//		
//		Set<String> previousMapKeySet = new HashSet<String>();
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:previousMap.entrySet()){
//			Map<String,KeyValuePo> keyMap = entry.getValue();
//			if(keyMap!=null){
//				previousMapKeySet.addAll(keyMap.keySet());		
//			}
//			
//		}		
//		currentKeySet.removeAll(previousMapKeySet);		
//		keyList.addAll(currentKeySet);
//		return keyList;	
//	}
//	
//	/**
//	 * 与上周对比 本周 减少的接口
//	 * @return
//	 */
//	public List<String> findLoseKey(){		
//		List<String> keyList = new ArrayList<String>();		
//		Set<String> currentKeySet = new HashSet<String>();		
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
//			Map<String,KeyValuePo> keyMap = entry.getValue();
//			if(keyMap!=null){
//				currentKeySet.addAll(keyMap.keySet());		
//			}
//		}
//		
//		Set<String> previousMapKeySet = new HashSet<String>();
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:previousMap.entrySet()){
//			Map<String,KeyValuePo> keyMap = entry.getValue();
//			if(keyMap!=null){
//				previousMapKeySet.addAll(keyMap.keySet());		
//			}
//			
//		}		
//		previousMapKeySet.removeAll(currentKeySet);		
//		keyList.addAll(previousMapKeySet);
//		return keyList;	
//	}
//	
//	public long getAllKeyValueSum(String keyName){
//		
//		long maxvalue = 0l;
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
//			long dayvalue = 0l;
//			for(Map.Entry<String , KeyValuePo> poEntry:entry.getValue().entrySet()){
//				String key = poEntry.getKey();
//				KeyValuePo po = poEntry.getValue();				
//				if(key.indexOf(keyName)>-1){
//					if(key.indexOf(Constants.COUNT_TIMES_FLAG)>0)
//						dayvalue+=Long.parseLong(po.getValueStr());
//				}
//			}
//			
//			if(maxvalue<dayvalue){
//				maxvalue=dayvalue;
//			}
//			
//		}
//		
//		
//		return maxvalue;
//	}
//	
//	
//	
//	
//	public Map<String,Map<String,Double>> findAllKeyRising(){
//		
//		Map<String,Map<String,Double>> keyRisingMap = new HashMap<String, Map<String,Double>>();
//		
//		
//		Set<String> currentKeySet = new HashSet<String>();		
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){			
//			Map<String,KeyValuePo> keyMap = entry.getValue();
//			if(keyMap!=null){
//				currentKeySet.addAll(keyMap.keySet());		
//			}
//		}
//		
//		for(String key:currentKeySet){
//			Double rising = keyRisingTrend(key);
//			if(rising==null){
//				continue;
//			}
//			if(rising>0){
//				if(rising<=-0.05){
//					putRising(WEEK_RISING_negative_5,key,rising,keyRisingMap);
//				}
//				if(rising>=0.05&&rising<0.1){
//					putRising(WEEK_RISING_5_10,key,rising,keyRisingMap);
//				}
//				if(rising>=0.1&&rising<0.2){
//					
//					putRising(WEEK_RISING_10_20,key,rising,keyRisingMap);
//				}
//				if(rising>=0.2&&rising<0.3){
//					putRising(WEEK_RISING_20_30,key,rising,keyRisingMap);
//				}
//				if(rising>=0.3){
//					putRising(WEEK_RISING_30_up,key,rising,keyRisingMap);
//				}
//			}			
//		}
//		
//		return keyRisingMap;
//	}
//	
//	private void putRising(String flag,String key,double value,Map<String,Map<String,Double>> keyRisingMap){
//		Map<String,Double> keyMap = keyRisingMap.get(flag);
//		if(keyMap==null){
//			keyMap = new HashMap<String, Double>();
//			keyRisingMap.put(flag, keyMap);
//		}		
//		keyMap.put(key, value);
//		
//	}
//	
//	
//	
//	
//	/**
//	 * 解析参数时间 对应本周内的天数
//	 * @param day
//	 * @return
//	 */
//	private List<Date> parseWeekDay(Date day){		
//		List<Date> dayList = new ArrayList<Date>();
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(day);		
//		int week = cal.get(Calendar.DAY_OF_WEEK);
//		int offest = (week-1)==0?7:(week-1);		
//		for(int i=0;i<offest;i++){	
//			dayList.add(cal.getTime());	
//			cal.add(Calendar.DAY_OF_MONTH, -1);
//		}		
//		return dayList;
//	}
//	
//	public Date parseWeekFirestDay(Date day){
//		List<Date> list = parseWeekDay(day);
//		Collections.sort(list);
//		return list.get(0);
//		
//	}
//	
//	
//	
//	public Map<String,Map<String,List<InnerKeyValue>>> findInAndOutTop5(){		
//		
//		Map<String,List<InnerKeyValue>> outmap = new HashMap<String,List<InnerKeyValue>>();
//		Map<String,List<InnerKeyValue>> inmap = new HashMap<String,List<InnerKeyValue>>();
//		
//		for(Map.Entry<String,Map<String,KeyValuePo>> entry:cureentMap.entrySet()){
//			String weekDay = entry.getKey();
//			Map<String,KeyValuePo> map = entry.getValue();
//			for(Map.Entry<String,KeyValuePo> keyEntry:map.entrySet()){			
//				String keyName = keyEntry.getKey();
//				String[] keys = keyName.split("_");				
//				if(keys.length<5){
//					continue;
//				}
//				if(!keys[keys.length-1].equals(Constants.COUNT_TIMES_FLAG)){
//					continue;
//				}
//				String type = keys[1];					
//				if(!type.equals("HSF-Consumer")&&!type.equals("HSF-ProviderDetail")&&!type.equals("SearchEngine")){
//					continue;
//				}				
//				String classname =  keys[2];
//				String methodName = keys[3];
//				
//				if(keyName.startsWith("IN")){					
//					List<InnerKeyValue> innerKeyList = inmap.get(type);
//					if(innerKeyList==null){
//						innerKeyList = new ArrayList<InnerKeyValue>();
//						inmap.put(type,innerKeyList);
//					}					
//					InnerKeyValue innerKey = new InnerKeyValue();
//					innerKey.keyName = keyName;
//					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
//					innerKeyList.add(innerKey);
//				}
//				if(keyName.startsWith("OUT")){
//					List<InnerKeyValue> innerKeyList = outmap.get(type);
//					if(innerKeyList==null){
//						innerKeyList = new ArrayList<InnerKeyValue>();
//						outmap.put(type,innerKeyList);
//					}
//					
//					InnerKeyValue innerKey = new InnerKeyValue();
//					innerKey.keyName = keyName;
//					innerKey.value = Double.parseDouble(keyEntry.getValue().getValueStr()) ;
//					innerKeyList.add(innerKey);
//				}
//			}
//		}
//		
//		Map<String,Map<String,List<InnerKeyValue>>> map = new HashMap<String,Map<String,List<InnerKeyValue>>>();		
//		
//		for(Map.Entry<String,List<InnerKeyValue>> entry:outmap.entrySet()){
//			String type = entry.getKey();
//			List<InnerKeyValue> list = entry.getValue();
//			System.out.println(this.appName+"_"+type+":"+list.size());
//			Collections.sort(list);			
//			List<InnerKeyValue> top5 = new ArrayList<InnerKeyValue>();			
//			for(int i=0;i<list.size();i++){
//				InnerKeyValue key = list.get(i);
//				if(top5.size()>5){
//					break;
//				}
//				if(!top5.contains(key)){
//					System.out.println(type+":"+key.keyName);
//					top5.add(key);
//				}
//			}
//			
//			Map<String,List<InnerKeyValue>> outMap = map.get("OUT");
//			if(outMap==null){
//				outMap = new HashMap<String,List<InnerKeyValue>>();
//				map.put("OUT",outMap);
//			}
//			outMap.put(type,top5);			
//		}
//		
//		for(Map.Entry<String,List<InnerKeyValue>> entry:inmap.entrySet()){
//			String type = entry.getKey();
//			List<InnerKeyValue> list = entry.getValue();
//			System.out.println(this.appName+"_"+type+":"+list.size());
//			Collections.sort(list);			
//			List<InnerKeyValue> top5 = new ArrayList<InnerKeyValue>();			
//			for(int i=0;i<list.size();i++){
//				InnerKeyValue key = list.get(i);
//				if(top5.size()>5){
//					break;
//				}
//				if(!top5.contains(key)){
//					System.out.println(type+":"+key.keyName);
//					top5.add(key);
//				}
//			}
//			
//			Map<String,List<InnerKeyValue>> outMap = map.get("IN");
//			if(outMap==null){
//				outMap = new HashMap<String,List<InnerKeyValue>>();
//				map.put("IN",outMap);
//			}
//			outMap.put(type,top5);			
//		}
//		
//		return map;
//	}
//	class InnerKeyValue implements Comparable<InnerKeyValue>{
//		private String keyName;
//		private Double value;
//		
//		public boolean equals(Object obj) {
//			if(obj instanceof InnerKeyValue){
//				InnerKeyValue key = (InnerKeyValue)obj;
//				if(keyName.equals(key.keyName)){
//					return true;
//				}
//			}
//			return false;
//		}
//		
//		
//		public int compareTo(InnerKeyValue o) {			
//			if(value<(o.value)){
//				return 1;
//			}else if(value==(o.value)){
//				return 0;
//			}else if(value>(o.value)){
//				return 0;
//			}			
//			return 0;
//		}
//		
//	}
//	
//
//}
