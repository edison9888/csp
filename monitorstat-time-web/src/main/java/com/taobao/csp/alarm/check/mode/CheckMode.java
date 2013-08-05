
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check.mode;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.alarm.check.entry.RangeDefine;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;

/**
 * �����ж�key�Ƿ���Ҫ�澯��ģ�ͽӿ�
 * 
 * modeName$modeConfig@modeName$modeConfig@
 * 
 * @author xiaodu
 *
 * ����2:29:54
 */
public abstract class CheckMode {
	
	private String modeConfig;
	
	private CheckMode next;
	
	private String modeName;
	
	private int keyLevel;

	//ͨ�õķ�Χ
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
	 * @return����true ��ʾ��������Ҫ�澯
	 */
	abstract boolean check(DataMessage message);
	
	public boolean checkData(DataMessage message){
		
		List<DataEntry> list =  message.getDataList();
		
		if(list==null||list.size()==0){
			return false;
		}
		
		//ȡ�����һ�����ݣ����������ݺ͵�ǰʱ���ѯ2�����ϣ������ж�
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
