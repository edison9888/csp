
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * @author xiaodu
 *
 * 下午1:43:01
 */
public class SameTime {
	
	private String time;
	
	private List<Double> values = new ArrayList<Double>();
	
	public SameTime(String time){
		this.time = time;
	}
	
	public void addValue(Double v){
		values.add(v);
	}
	
	public double getMean(){
		DescriptiveStatistics ds = new DescriptiveStatistics();
		Collections.sort(values);
		if(values.size()>10){
			for(int i=1;i<values.size()-1;i++){ //去掉最大与最小的值
				ds.addValue(values.get(i));
			}
		}else{
			for(int i=0;i<values.size();i++){ 
				ds.addValue(values.get(i));
			}
		}
		return ds.getMean();
		
	}

	public String getTime() {
		return time;
	}
	

	
	
	

}
