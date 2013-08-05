
package com.taobao.monitor.alarm.po;
/**
 * 
 * @author xiaodu
 * @version 2011-2-28 上午10:38:39
 */
public class ExtraKeyAlarmDefine {
	
	private int appId;
	
	private int keyId;
	
	private int hostId;
	
	private String alarmDefine;// '格式为:   1000#20000$10:00#20:10;1000#20000$10:00#20:10;    -1 表示无限制',

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getAlarmDefine() {
		return alarmDefine;
	}

	public void setAlarmDefine(String alarmDefine) {
		this.alarmDefine = alarmDefine;
	}

	
	
	
	

}
