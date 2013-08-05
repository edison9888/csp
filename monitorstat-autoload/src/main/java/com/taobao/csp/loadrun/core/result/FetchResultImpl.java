
package com.taobao.csp.loadrun.core.result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-6-28 ÏÂÎç03:42:06
 */
public class FetchResultImpl implements FetchResult{
	
	private static final Logger logger = Logger.getLogger(FetchResultImpl.class);
	
	public Map<ResultKey, List<ResultCell>> keyValueMap = new ConcurrentHashMap<ResultKey, List<ResultCell>>();
	
	
	private int MAX_SECOND = 25;
	

	public Map<ResultKey, Double> getResult() {
		
		Map<ResultKey, Double> map = new HashMap<ResultKey, Double>();
		try{
			for(Map.Entry<ResultKey, List<ResultCell>> entry:keyValueMap.entrySet()){
				ResultKey key = entry.getKey();
				List<ResultCell> list = entry.getValue();
				double tmp = 0;
				switch(key){
					case Apache_State_200:
						map.put(ResultKey.Apache_State_200,getAverage(ResultKey.Apache_State_200));
						break;
					case Apache_PageSize :
						tmp = getAverage(ResultKey.Apache_Pv, ResultKey.Apache_PageSize);
						map.put(ResultKey.Apache_PageSize,Arith.div(tmp, 1000,1));
						break;
					case Apache_Rest :
						tmp = getAverage(ResultKey.Apache_Pv, ResultKey.Apache_Rest);
						map.put(ResultKey.Apache_Rest,Arith.div(tmp, 1000,1));
						break;
						
					case Apache_Pv :
						map.put(ResultKey.Apache_Pv,getAverage(ResultKey.Apache_Pv));
						break;
					
					case Tomcat_PageSize :
						tmp =getAverage(ResultKey.Tomcat_Pv, ResultKey.Tomcat_PageSize);
						map.put(ResultKey.Tomcat_PageSize,Arith.div(tmp, 1000,1));
						break;
					case Tomcat_Rest :
						tmp = getAverage(ResultKey.Tomcat_Pv, ResultKey.Tomcat_Rest);
						map.put(ResultKey.Tomcat_Rest,Arith.div(tmp, 1000,1));
						break;
					case Tomcat_Pv :
						map.put(ResultKey.Tomcat_Pv,getAverage(ResultKey.Tomcat_Pv));
						break;
					case Tomcat_State_200:
						map.put(ResultKey.Tomcat_State_200,getAverage(ResultKey.Tomcat_State_200));
						break;
					case Hsf_pv :
						map.put(ResultKey.Hsf_pv,getHsfPv(ResultKey.Hsf_pv));
						break;
					case Hsf_Rest :
						tmp =getHsfRest(ResultKey.Hsf_pv, ResultKey.Hsf_Rest);
						map.put(ResultKey.Hsf_Rest,tmp);
						break;
					
					case GC_Memory :
						tmp = getAverage(ResultKey.GC_Memory);
						map.put(ResultKey.GC_Memory,getAverage(ResultKey.GC_Memory));
						break;
						
					case GC_CMS :
						map.put(ResultKey.GC_CMS,getCount(ResultKey.GC_CMS));
						break;
					
					case GC_Full :
						map.put(ResultKey.GC_Full,getCount(ResultKey.GC_Full));
						break;
						
					case GC_Full_Time :
						tmp = getAverage(ResultKey.GC_Full, ResultKey.GC_Full_Time);
						map.put(ResultKey.GC_Full_Time,tmp);
						break;
					
					case GC_Min :
						map.put(ResultKey.GC_Min,getCount(ResultKey.GC_Min));
						break;
					
					case GC_Min_Time :
						tmp = getAverage(ResultKey.GC_Min, ResultKey.GC_Min_Time);
						map.put(ResultKey.GC_Min_Time,tmp);
						break;
					
					case CPU :
						map.put(ResultKey.CPU,getAverage(ResultKey.CPU));
						break;
					case Load :
						map.put(ResultKey.Load,getAverage(ResultKey.Load));
						break;
					case Io :
						map.put(ResultKey.Io,getAverage(ResultKey.Io));
						break;
					
					case Jvm_Memeory :
						map.put(ResultKey.Jvm_Memeory,getAverage(ResultKey.Jvm_Memeory));
						break;
						
					case AJP_BLOCKED :
						map.put(ResultKey.AJP_BLOCKED, getAverage(ResultKey.AJP_BLOCKED));
						break;
						
					case AJP_RUNNABLE :
						map.put(ResultKey.AJP_RUNNABLE, getAverage(ResultKey.AJP_RUNNABLE));
						break;
						
					case AJP_WAITING :
						map.put(ResultKey.AJP_WAITING, getAverage(ResultKey.AJP_WAITING));
						break;
				}
			}
		}catch (Exception e) {
			logger.error("getResult ´íÎó",e);
		}
		return map;
	}
	
	
	
	
	private double getCount(ResultKey key1){
		
		List<ResultCell> s1  = keyValueMap.get(key1);
		double v = 0d;
		
		for(int i=0;i<s1.size();i++){
			ResultCell inner = s1.get(i);
			v=Arith.add(v, inner.getValue());
		}
		return v;
	}
	
	
	private double getHsfRest(ResultKey keyPv,ResultKey keyRest){ 
		
		try{
			if (isMonitorLog(keyRest)) {
				List<ResultCell> sPv  = keyValueMap.get(keyPv);
				List<ResultCell> sRest  = keyValueMap.get(keyRest);
				
				Map<Date,ResultCell> map = new HashMap<Date, ResultCell>();
				for(ResultCell rc:sPv){
					map.put(rc.getTime(), rc);
				}
				
				if(sRest.size() >0){
					ResultCell innerRest = sRest.get(sRest.size()-1);
					
					ResultCell pv = map.get(innerRest.getTime());
					if(pv !=null){
						return Arith.div(innerRest.getValue(), pv.getValue(), 2);
					}
					
					return 0;
				}
			} else {
				return getAverage(keyPv, keyRest);
			}
			
		}catch (Exception e) {
			logger.error("getHsfRest ´íÎó",e);
		}
		return 0;
		
		
	}
	
	private double getHsfPv(ResultKey key1){
		
		List<ResultCell> s1  = keyValueMap.get(key1);
		if (s1 == null || s1.isEmpty()) {
			return 0;
		}
		
		boolean isMonitorLog = isMonitorLog(key1);
		if (isMonitorLog) {
			ResultCell inner = s1.get(s1.size()-1);
			return Arith.div(inner.getValue(), 120, 2);
		} else {
			logger.info("collect from eagleeye:" + key1.getName());
			return getAverage(key1);
		}
	}
	
	private boolean isMonitorLog(ResultKey key) {
		List<ResultCell> list  = keyValueMap.get(key);
		if (list == null) {
			return false;
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("ss");
		for (ResultCell cell : list) {
			if (!sf.format(cell.getTime()).equals("00")) {
				return false;
			}
		}
		
		return true;
	}
	
	private double getAverage(ResultKey key1){
		
		long t = System.currentTimeMillis();
		
		List<ResultCell> s1  = keyValueMap.get(key1);
		
		Map<Date,Double> map1 = new HashMap<Date, Double>();
		
		Set<Date> lastTimeSet = new HashSet<Date>();
		
		for(int i=0;i<s1.size();i++){
			ResultCell inner = s1.get(i);
			//System.out.println(key1.name()+"t - inner.getTime().getTime():"+(t - inner.getTime().getTime()));
			if(t - inner.getTime().getTime() < MAX_SECOND*1000&& t - inner.getTime().getTime() > 5*1000){
				lastTimeSet.add(inner.getTime());
			}
			
			Double d = map1.get(inner.getTime());
			if(d == null){
				map1.put(inner.getTime(), inner.getValue());
			}else{
				map1.put(inner.getTime(), inner.getValue()+d);
			}
		}
		double v1 = 0d;
		int size =0;
		for(Date d:lastTimeSet){
			if(map1.get(d)!=null){
				v1 = Arith.add(v1, map1.get(d));
				size++;
			}
		}
		return size>0?Arith.div(v1, size,2):0;
	}
	
	
	private double getAverage(ResultKey key1, ResultKey key2){
		
		long t = System.currentTimeMillis();
		
		List<ResultCell> s1  = keyValueMap.get(key1);
		List<ResultCell> s2 = keyValueMap.get(key2);
		
		Map<Date,Double> map1 = new HashMap<Date, Double>();
		Map<Date,Double> map2 = new HashMap<Date, Double>();
		
		Set<Date> lastTimeSet = new HashSet<Date>();
		
		for(int i=0;i<s1.size();i++){
			ResultCell inner = s1.get(i);
			//System.out.println(key1.name()+"/"+key2.name()+"t - inner.getTime().getTime():"+(t - inner.getTime().getTime()));
			if(t - inner.getTime().getTime() < MAX_SECOND*1000&& t - inner.getTime().getTime() > 5*1000){
				lastTimeSet.add(inner.getTime());
			}
			Double d = map1.get(inner.getTime());
			if(d == null){
				map1.put(inner.getTime(), inner.getValue());
			}else{
				map1.put(inner.getTime(), inner.getValue()+d);
			}
		}
		
		for(int i=0;i<s2.size();i++){
			ResultCell inner = s2.get(i);
			Double d = map2.get(inner.getTime());
			if(d == null){
				map2.put(inner.getTime(), inner.getValue());
			}else{
				map2.put(inner.getTime(), inner.getValue()+d);
			}
		}
		
		double v1 = 0d;
		double v2 = 0d;
		for(Date d:lastTimeSet){
			if(map1.get(d)!=null&&map2.get(d)!=null){
				v1 = Arith.add(v1, map1.get(d));
				v2 = Arith.add(v2, map2.get(d));
			}
		}
		return v1>0?Arith.div(v2, v1,2):0;
	}
	
	
	

	public void put(ResultKey key,Double value,Date time) {
		
		List<ResultCell> q = keyValueMap.get(key);
		if(q == null){
			q = new Stack<ResultCell>();
			keyValueMap.put(key, q);
		}
		
		if(q.size() == 0){
			ResultCell inner = new ResultCell();
			inner.setValue(value) ;
			inner.setTime(time) ;
			q.add(inner);
		}else{
			ResultCell inner = q.get(q.size()-1);
			if(inner.getTime().equals(time)){
				inner.setValue(Arith.add(value, inner.getValue())) ;
			}else{
				inner = new ResultCell();
				inner.setValue(value) ;
				inner.setTime(time);
				q.add(inner);
			}
		}
	}

	public void clear() {
		keyValueMap.clear();
		
	}


	public Map<ResultKey, List<ResultCell>> getOriginalResult() {
		return keyValueMap;
	}

}
