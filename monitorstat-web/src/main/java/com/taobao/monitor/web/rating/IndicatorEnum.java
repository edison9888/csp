
package com.taobao.monitor.web.rating;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-8-18 下午02:49:11
 */
public enum IndicatorEnum {
	CPU("cpu",3113,"%") {
		
		public double computeData(List<KeyValuePo> rushHourData) {	
			
			return average(rushHourData);
		}
	},JVM("jvm",7621,"%") {
		
		public double computeData(List<KeyValuePo> rushHourData) {
			
			return average(rushHourData);
		}
	},QPS("qps",175,"q/s") {//PV_VISIT_COUNTTIMES
		
		public double computeData(List<KeyValuePo> rushHourData) {
			
			return Arith.div(average(rushHourData), 60,2);
		}
	},RT("rt",176,"ms") {//PV_REST_AVERAGEUSERTIMES
		
		public double computeData(List<KeyValuePo> rushHourData) {
			return Arith.div(average(rushHourData), 1000);
		}
	},FGC("fgc",167,"次") {//SELF_GC_Full_AVERAGEMACHINEFLAG
		
		public double computeData(List<KeyValuePo> rushHourData) {
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
	},FGCT("fgct",168,"秒") {//SELF_GC_Full_AVERAGEUSERTIMES
		
		public double computeData(List<KeyValuePo> rushHourData) {
			
			return Arith.div(average(rushHourData), 1000000);
		}
	},IO("io",7620,"%") {//IoWait
		
		public double computeData(List<KeyValuePo> rushHourData) {
			return average(rushHourData);
		}
	},LOAD("load",944,"") {//load
		
		public double computeData(List<KeyValuePo> rushHourData) {
			return average(rushHourData);
		}
	};
	
	
	private IndicatorEnum(String name,int keyid,String unit){
		this.name = name;
		this.keyId = keyid;
		this.unit = unit;
	}
	
	private String name;
	
	private int keyId;//对应实时应用中的key
	
	private String unit ;//单位

	public int getKeyId() {
		return keyId;
	}

	public String getName() {
		return name;
	}
	
	
	
	public String getUnit() {
		return unit;
	}

	public abstract double computeData(List<KeyValuePo> rushHourData);
	
	
	private static double average(List<KeyValuePo> rushHourData){		
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
	
	
	
	public static IndicatorEnum getIndicatorEnum(String key){
		for(IndicatorEnum i:IndicatorEnum.values()){
			if(i.getName().equals(key)){
				return i;
			}
		}
		return null;
	}
	

}
