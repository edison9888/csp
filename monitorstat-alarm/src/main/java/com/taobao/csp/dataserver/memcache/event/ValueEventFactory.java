
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.event;

/**
 * @author xiaodu
 *
 * 上午11:31:22
 */
public class ValueEventFactory {
	
	//HOST级别
	public final static DataEvent hbaseEvent = new HostHbaseValueEvent();
	
	//APP级别
	public final static DataEvent apphbaseEvent = new APPHbaseValueEvent();
	
	
}
