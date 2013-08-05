/*
*//**
 * monitorstat-alarm
 *//*
package com.taobao.csp.alarm.check.mode;

import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.util.Util;

*//**
 * @author xiaodu
 *
 * ÏÂÎç10:39:13
 *//*
public class SimpleFlowCheckMode extends CheckMode{

	*//**
	 * @param modeConfig
	 *//*
	public SimpleFlowCheckMode(String name ,String modeConfig) {
		super(name,modeConfig);
	}

	public boolean check(List<DataEntry> list) {
		
		if(list.size()<3){
			return false;
		}
		
		double current = Util.converFloat(list.get(0).getValue());
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for(int i=1;i<list.size();i++){
			ds.addValue(Util.converFloat(list.get(i).getValue()));		
		}

		double mean  = ds.getMean();
		
		double d = (current - mean)/mean;
		if(d >0.7){
			return true;
		}
		
		current = Util.converFloat(list.get(1).getValue());
		 d = (current - mean)/mean;
		if(d <-0.70){
			return true;
		}
		return false;
	}

	@Override
	public void analyseConfig(String modeConfig) {
		// TODO Auto-generated method stub
		
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