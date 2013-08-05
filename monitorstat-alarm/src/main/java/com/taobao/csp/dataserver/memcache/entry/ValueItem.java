package com.taobao.csp.dataserver.memcache.entry;

/**
 * 时间和值
 * @author bishan.ct
 *
 */
public class ValueItem {

	private Object value;
	private long time;
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
