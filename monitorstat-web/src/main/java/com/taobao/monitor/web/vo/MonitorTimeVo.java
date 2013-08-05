
package com.taobao.monitor.web.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2010-4-27 下午05:52:51
 */
public class MonitorTimeVo {
	
	private String appName;	
	//key 为时间
	private Map<String,MonitorVo> monitorVoMap = new HashMap<String, MonitorVo>();
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Map<String, MonitorVo> getMonitorVoMap() {
		return monitorVoMap;
	}
	public void setMonitorVoMap(Map<String, MonitorVo> monitorVoMap) {
		this.monitorVoMap = monitorVoMap;
	}
	
	
	
	
	
	
	

}
