
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.event;

/**
 * @author xiaodu
 *
 * ����11:31:22
 */
public class ValueEventFactory {
	
	//HOST����
	public final static DataEvent hbaseEvent = new HostHbaseValueEvent();
	
	//APP����
	public final static DataEvent apphbaseEvent = new APPHbaseValueEvent();
	
	
}
