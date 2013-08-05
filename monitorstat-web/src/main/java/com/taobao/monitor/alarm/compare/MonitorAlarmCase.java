package com.taobao.monitor.alarm.compare;

import java.util.ArrayList;
import java.util.List;


public class MonitorAlarmCase {
	
	private int appId;
	
	private int keyId;
	
	List<MonitorAlarm> monitorList = new ArrayList<MonitorAlarm>();
	
	public MonitorAlarmCase(int appId, int keyId) {
		this.appId = appId;
		this.keyId = keyId;
	}

	public int getAppId() {
		return appId;
	}


	public int getKeyId() {
		return keyId;
	}

	public List<MonitorAlarm> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<MonitorAlarm> monitorList) {
		this.monitorList = monitorList;
	}
	
	
	

}
