
package com.taobao.monitor.web.rating;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2011-1-17 ÏÂÎç04:22:33
 */
public class Conversion {
	
	public static  double conversion(List<KeyValuePo> rushHourData,RatingIndicator indicator){
		
		if(indicator.getKeyId() == 175){
			return averageQps(rushHourData);
		}else if(indicator.getKeyId() == 167){
			return averageHost(rushHourData);
		}else if(indicator.getKeyId() == 168){
			return averageGct(rushHourData);
		}else if(indicator.getKeyId() == 176){
			return averageRest(rushHourData);
		}else {
			return average(rushHourData);
		}
		
//		if("qps".equals(type.toLowerCase())){
//			return averageQps(rushHourData);
//		}else if("fgc".equals(type.toLowerCase())){
//			return averageHost(rushHourData);
//		}else if("fgct".equals(type.toLowerCase())){
//			return averageGct(rushHourData);
//		}else if("rt".equals(type.toLowerCase())){
//			return averageRest(rushHourData);
//		}else {
//			return average(rushHourData);
//		}
	}
	
	
	public static  double conversion(List<KeyValuePo> rushHourData,String type){
		if("qps".equals(type.toLowerCase())){
			return averageQps(rushHourData);
		}else if("fgc".equals(type.toLowerCase())){
			return averageHost(rushHourData);
		}else if("fgct".equals(type.toLowerCase())){
			return averageGct(rushHourData);
		}else if("rt".equals(type.toLowerCase())){
			return averageRest(rushHourData);
		}else {
			return average(rushHourData);
		}
	}
	
	public static double averageHost(List<KeyValuePo> rushHourData) {
		double num = 0;
		Set<Integer> set = new HashSet<Integer>();
		for(KeyValuePo po:rushHourData){
			Map<Integer, Double> map = po.getSiteValueMap();
			set.addAll(map.keySet());
			for(Map.Entry<Integer, Double> entry:map.entrySet()){
				num = Arith.add(num, entry.getValue());
			}
			
		}
		if(set.size()==0){
			return 0;
		}
		return Arith.div(num, set.size(), 2);
	}
	
	public static double averageQps(List<KeyValuePo> rushHourData) {
		return Arith.div(average(rushHourData), 60,2);
	}
	
	public static double averageRest(List<KeyValuePo> rushHourData) {
		return Arith.div(average(rushHourData), 1000);
	}
	public static double averageGct(List<KeyValuePo> rushHourData) {
		
		return Arith.div(average(rushHourData), 1000000);
	}
	
	
	public static double average(List<KeyValuePo> rushHourData){		
		double v = 0f;
		int size = 0;
		
		for(KeyValuePo po:rushHourData){			
			v=Arith.add(Double.parseDouble(po.getValueStr()), v);
			size++;
		}
		
		if(size>0){
			return Arith.div(v, size,2);
		}
		
		return 0f;
	}

}
