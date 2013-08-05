package com.taobao.csp.loadrun.core.constant;

/***
 * 压测相信信息key的类型
 * @author youji.zj
 * @version 2012-06-22
 */
public enum ResultDetailType {
	
	URL("url统计信息", 1),
	
	TOMCAT_URL("tomcat url统计", 2),
	
	HSF_PROVIDER("HSF提供信息", 3),
	
	HSF_CONSUMER("HSF调用信息", 4),
	
	TAIR("Tair调用信息", 5),
	
	TDDL("Tddl调用信息", 6),
	
	PERFORMANCE_INDEX("性能指标", 7),
	
	HSF_PV_RT("HSF调用信息", 8),
	
	IO_DATA("IO信息", 9);
	
	
	private String key;
	
	private int sort;
	
	private ResultDetailType(String key, int sort) {
		this.key = key;
		this.sort = sort;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
