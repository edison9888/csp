
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.check.mode;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.monitor.common.util.Arith;

/**
 * @author xiaodu
 *
 * 下午4:06:35
 */
public class RecentlyRateCheckMode extends CheckMode{

	/**
	 * @param name
	 * @param modeConfig
	 */
	private DataEntry alarmData;
	private String alarmCause;
	public RecentlyRateCheckMode(String name, String modeConfig) {
		super(name, modeConfig);
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.check.mode.CheckMode#getAlarmData()
	 */
	@Override
	public DataEntry getAlarmData() {
		return alarmData;
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.check.mode.CheckMode#getAlarmCause()
	 */
	@Override
	public String getAlarmCause() {
		return alarmCause;
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.check.mode.CheckMode#analyseConfig(java.lang.String)
	 */
	@Override
	void analyseConfig(String modeConfig) {
		try{
			parseRangeDefine(modeConfig);
		}catch (Exception e) {
		}
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.check.mode.CheckMode#check(java.util.List)
	 */
	@Override
	boolean check(DataMessage dataMessage) {
		List<DataEntry> list = dataMessage.getDataList();
		if(list.size()<3){
			return false;
		}
		DataEntry first = list.get(0);
		alarmData = first;
		int time = Integer.parseInt(TimeUtil.formatTime(first.getTime(), "HHmm"));
		double current = Util.converFloat(list.get(0).getValue());
		for(RangeDefine range:alarmRangeList){
			int startTime = range.getStartTime();
			int endTime = range.getEndTime();
			if(time>=startTime &&time<=endTime){
				DescriptiveStatistics ds = new DescriptiveStatistics();
				for(int i=1;i<list.size();i++){
					ds.addValue(Util.converFloat(list.get(i).getValue()));		
				}

				double mean  = ds.getMean();
				
				double d = Arith.mul(Arith.div(current, mean, 4), 100);
				
				
				
				if(range.getLessthan()!=-1&&d >range.getLessthan()){
					alarmCause = "最近均值为"+DataUtil.round(mean, 2,BigDecimal.ROUND_HALF_UP)+"，当前值为"+current+"与均值比值为"+d+"%超过"+range.getLessthan()+"%";
					return true;
				}
				
				current = Util.converFloat(list.get(0).getValue());
				 d = (current - mean)/mean;
				if(d <range.getLessthan()){
					alarmCause = "最近均值为"+DataUtil.round(mean, 2,BigDecimal.ROUND_HALF_UP)+"，当前值为"+current+"与均值比值为"+d+"%低于"+range.getLessthan()+"%";
					return true;
				}
			}
		}
		
		return false;
	}
	private void  parseRangeDefine(String d){
		
		try{
			String[] alarmArray =  d.split(";");	
			for(String alarm:alarmArray){
				String[] ranges = alarm.split("\\$");
				if(ranges.length!=2){
					continue;
				}
				String dataRange = ranges[0];
				String timeRange = ranges[1];
				
				String[] _dataRange = dataRange.split("\\#"); 
				String[] _timeRange = timeRange.split("\\#");
				if(_dataRange.length!=2||_timeRange.length!=2){
					continue;
				}
				
				RangeDefine object = new RangeDefine();
				object.setStartTime(Integer.parseInt(_timeRange[0].replaceAll(":", "")));
				object.setEndTime(Integer.parseInt(_timeRange[1].replaceAll(":", "")));
				object.setGreaterthan(Double.parseDouble(_dataRange[0]));
				object.setLessthan(Double.parseDouble(_dataRange[1]));				
				alarmRangeList.add(object);
			}
		}catch(Exception e){
		}
	}

}
