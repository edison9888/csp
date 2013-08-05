/*
*//**
 * monitorstat-alarm
 *//*
package com.taobao.csp.alarm.check.mode;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.util.Util;

*//**
 * 
 * 简单的平均值检查方式
 * 
 * 当前值以外的 全部值做平均,并将 当前值/平均值
 * 
 * y = a[n]/Σa[n-1]
 * 
 * 
 * 
 * @author xiaodu
 *
 * 下午2:53:47
 *//*
public class SimpleRTCheckMode extends CheckMode{

	*//**
	 * @param modeConfig
	 *//*
	public SimpleRTCheckMode(String name ,String modeConfig) {
		super(name,modeConfig);
	}
	private DataEntry alarmData;
	
	private String alarmCause;


	public boolean check(List<DataEntry> list) {
		
		if(list.size()<3){
			return false;
		}
		alarmData = list.get(0);
		
		double current = Util.converFloat(list.get(0).getValue());
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for(int i=1;i<list.size();i++){
			ds.addValue(Util.converFloat(list.get(i).getValue()));
		}

		double mean  = ds.getMean();
		
		System.out.println("mean="+mean);
		System.out.println("current="+current);

		BigDecimal b1 = new BigDecimal(current);
		BigDecimal be = new BigDecimal(mean);
		
		double y = b1.subtract(be).divide(be, 10,BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println("y="+y);
		
		
		if(mean <5){
			if(y >3){
				return true;
			}
		}else if(mean <10){
			if(y >2){
				return true;
			}
		}else if(mean <50){
			if(y >1){
				return true;
			}
		}else{
			if(y >0.7){
				return true;
			}
		}
		return false;
	}
	
@Override
public void analyseConfig(String modeConfig) {
	
}



 (non-Javadoc)
 * @see com.taobao.csp.alarm.check.mode.CheckMode#getAlarmData()
 
@Override
public DataEntry getAlarmData() {
	// TODO Auto-generated method stub
	return null;
}



 (non-Javadoc)
 * @see com.taobao.csp.alarm.check.mode.CheckMode#getAlarmCause()
 
@Override
public String getAlarmCause() {
	// TODO Auto-generated method stub
	return null;
}

}
*/