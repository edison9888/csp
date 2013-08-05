
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check.mode;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;

/**
 * 用来判断key是否需要告警的模型接口
 * 
 * modeName$modeConfig@modeName$modeConfig@
 * 
 * @author xiaodu
 *
 * 下午2:29:54
 */
public abstract class CheckMode {
	
	private String modeConfig;
	
	private CheckMode next;
	
	private String modeName;
	
	private int keyLevel;

	//通用的范围
	protected List<RangeDefine> alarmRangeList = new ArrayList<RangeDefine>();
	
	protected String getModeConfig() {
		return modeConfig;
	}

	protected CheckMode getNext() {
		return next;
	}

	protected void setNext(CheckMode next) {
		this.next = next;
	}

	public CheckMode(String name,String modeConfig){
		analyseConfig(modeConfig);
		this.modeName = name;
	}
	
	
	public abstract DataEntry getAlarmData();
	public abstract String getAlarmCause();
	
	/**
	 * 
	 * @param modeConfig
	 */
	abstract void analyseConfig(String modeConfig);
	
	/**
	 * 
	 * @param values
	 * @param idx
	 * @return返回true 表示有问题需要告警
	 */
	abstract boolean check(DataMessage message);
	
	public boolean checkData(DataMessage message){
		
		List<DataEntry> list =  message.getDataList();
		
		if(list==null||list.size()==0){
			return false;
		}
		
		//取得最近一个数据，如果这个数据和当前时间查询2分以上，不做判断
		DataEntry de = list.get(0);
		if(de.getTime()+60000<System.currentTimeMillis()){
			return false;
		}
		
		CheckMode mode = this;
		while(mode != null){
			boolean b = mode.check(message);
			if(b){
				return true;
			}else{
				mode = this.getNext();
			}
		}
		return false;
	}

	public String getModeName() {
		return modeName;
	}

	public int getKeyLevel() {
		return keyLevel;
	}

	public void setKeyLevel(int keyLevel) {
		this.keyLevel = keyLevel;
	}
	
}
