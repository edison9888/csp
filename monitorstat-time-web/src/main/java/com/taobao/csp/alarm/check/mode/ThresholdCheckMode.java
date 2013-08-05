
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check.mode;

import java.util.List;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.csp.time.util.TimeUtil;

/**
 * ��ֵ�ж�
 * 
 * 
 * Threshold$value;value@
 * 
 * @author xiaodu
 *
 * ����3:53:36
 */
public class ThresholdCheckMode extends CheckMode {
	
	
	private DataEntry alarmData;
	
	private String alarmCause;
	
	/**
	 * @param modeConfig
	 */
	public ThresholdCheckMode(String name ,String modeConfig) {
		super(name,modeConfig);
	}

	
	
	@Override
	public boolean check(DataMessage dataMessage) {
		List<DataEntry> list = dataMessage.getDataList();
		if(list==null||list.size()==0){
			return false;
		}
		
		DataEntry first = list.get(0);
		int time = Integer.parseInt(TimeUtil.formatTime(first.getTime(), "HHmm"));
		float firstdata = Util.converFloat(first.getValue());
		
		for(RangeDefine range:alarmRangeList){
			int startTime = range.getStartTime();
			int endTime = range.getEndTime();
			
			if(time>=startTime &&time<=endTime){
				
				if(firstdata>range.getLessthan()&&range.getLessthan()!=-1){
					
					alarmData = first;
					alarmCause = "����"+range.getLessthan()+"��ֵ����";
					return true;
				}
				//��Ҫ�������һ����¼�������ռ���ʱ�򣬲�����һ��ʱ�����ȫ��ȡ����������ʹ�õڶ���ʱ��������ܱ�֤��������
				if(range.getGreaterthan()!=-1){
					if(list.size()>1){
						DataEntry second = list.get(1);
						float seconddata = Util.converFloat(second.getValue());
						if(seconddata <range.getGreaterthan()){
							alarmData = second;
							alarmCause = "����"+range.getGreaterthan()+"��ֵ����";
							return true;
						}
					}
				}
			}
			
		}
		
		return false;
	}

	
	@Override
	/**
	 * -1#2500$00:10#23:59;-1#2500$10:10#23:59;
	 */
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


	@Override
	public DataEntry getAlarmData() {
		return alarmData;
	}

	@Override
	public String getAlarmCause() {
		return alarmCause;
	}
	
	
	


}
