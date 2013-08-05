
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check.mode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.time.cache.HistoryCache;
import com.taobao.csp.time.util.TimeUtil;

/**
 * @author xiaodu
 *
 * 下午12:33:52
 */
public class HistoryCheckMode extends CheckMode{
	
	
	
	private String alarmCause;
	
	private DataEntry alarmData;
	

	/**
	 * @param modeConfig
	 */
	public HistoryCheckMode(String name ,String modeConfig) {
		super(name,modeConfig);
	}

	public boolean check(DataMessage dataMessage) {
		List<DataEntry> list = dataMessage.getDataList();
		if(list.size()==0)return false;
		Date current = new Date(list.get(0).getTime());
		if((current.getTime()-(new Date().getTime()))>10*60*1000)return false;
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DATE, -1); 
		Date date = calendar.getTime();
		Map<Date, String> map = HistoryCache.get().getHistoryCache(dataMessage.getAppName(), dataMessage.getKeyName(), dataMessage.getPropertyName(), date);
		if(map == null) return false;
		Double historyValue = Double.parseDouble(map.get(current));
		Double currentValue = (Double)(list.get(0).getValue());
		Double portion = (currentValue-historyValue)/historyValue;
		int time = Integer.parseInt(TimeUtil.formatTime(current.getTime(), "HH:mm"));
		for(RangeDefine range:alarmRangeList){
			int startTime = range.getStartTime();
			int endTime = range.getEndTime();
			if(time>=startTime &&time<=endTime){
				if(portion>range.getGreaterthan()){
					alarmCause = "超过历史同期"+portion;
					return true;
				}
				if(portion<range.getLessthan()){
					alarmCause = "小于历史同期"+portion;
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void analyseConfig(String modeConfig) {
		try{
			parseRangeDefine(modeConfig);
		}catch (Exception e) {
		}
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


}
