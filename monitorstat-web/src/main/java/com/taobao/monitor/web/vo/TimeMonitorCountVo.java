
package com.taobao.monitor.web.vo;

import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2010-4-28 обнГ02:56:12
 */
public class TimeMonitorCountVo {
	
	private long allCount;
	
	private Map<String,Map<String,Map<String,String>>> map;

	public long getAllCount() {
		return allCount;
	}

	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}

	public Map<String, Map<String, Map<String, String>>> getMap() {
		return map;
	}

	public void setMap(Map<String, Map<String, Map<String, String>>> map) {
		this.map = map;
	}
	
	

}
