package com.taobao.csp.alarm.check.mode;

/**
 * monitorstat-alarm
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.monitor.common.util.Arith;

/**
 * @author xiaodu
 *
 * 下午3:42:22
 */
public class BaselineCheckMode extends CheckMode {
	
	
	private String alarmCause="基线告警";

	private DataEntry alarmData;
	

	/**
	 * @param modeConfig
	 */
	public BaselineCheckMode(String name,String modeConfig){
		super(name,modeConfig);
	}

	@Override
	void analyseConfig(String modeConfig) {
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
	@Override
	boolean check(DataMessage dataMessage) {
		List<DataEntry> list = dataMessage.getDataList();
		if(list.size()==0)return false;
		alarmData = list.get(0);
		Date current = new Date(list.get(0).getTime());
		if((current.getTime()-(new Date().getTime()))>10*60*1000)return false;
		Map<String,Double> map = null;
		if(dataMessage.getIp()==null){
			 map = BaseLineCache.get().getBaseLine(dataMessage.getAppName(), dataMessage.getKeyName(), dataMessage.getPropertyName());
		}else{
			map = BaseLineCache.get().getBaseLine(dataMessage.getAppName(), dataMessage.getKeyName(), dataMessage.getPropertyName(),dataMessage.getIp());
		}
		if(map==null)return false;
		
		DataEntry second = null;
		if(list.size()>1){
			second = list.get(1);
		}
		
		
		
		Double baselineValue = map.get(TimeUtil.formatTime(current.getTime(), "HH:mm"));
		if(baselineValue==null)return false;
		
		Double currentValue = DataUtil.transformDouble(list.get(0).getValue());
		
		if(baselineValue == 0&&currentValue ==0){
			return false;
		}
		
		if(baselineValue == 0){//如果为0，设置为1作为假设值
			baselineValue=1d;
		}
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		String strTime = sdf.format(current);
		int time = Integer.parseInt(strTime);
		double portion = Arith.mul(Arith.div( (currentValue), baselineValue, 4), 100);
		
		for(RangeDefine range:alarmRangeList){
			int startTime = range.getStartTime();
			int endTime = range.getEndTime();
			if(time>=startTime &&time<=endTime){
				
				if(range.getLessthan()!=-1&&portion>range.getLessthan()){
					alarmCause = "上升到基线值的"+portion+"%,同期基线值:"+DataUtil.round(baselineValue, 2, BigDecimal.ROUND_HALF_UP)+"超过设置比例"+range.getLessthan()+"%";
					return true;
				}
				if(range.getGreaterthan()!=-1&&portion<range.getGreaterthan()){
					alarmCause = "下降到基线值的"+portion+"%,同期基线值:"+DataUtil.round(baselineValue, 2, BigDecimal.ROUND_HALF_UP)+"超过设置比例"+range.getGreaterthan()+"%";
					
					//pv 是一个特殊的key，如果设置了小于判断 需要利用上个时间值来做比较
					if(KeyConstants.PV.equals(dataMessage.getKeyName())){
						if(second!=null){
							Double secondValue = DataUtil.transformDouble(second.getValue());
							double secondportion = Arith.mul(Arith.div( (secondValue), baselineValue, 4), 100);
							if(secondportion<range.getGreaterthan()){
								return true;
							}
							
						}
					}
				}
			}
		}
		return false;
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
