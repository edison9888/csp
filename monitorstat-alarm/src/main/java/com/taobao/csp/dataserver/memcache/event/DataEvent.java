
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.event;

/**
 * @author xiaodu
 *
 * ионГ10:51:50
 */
public interface DataEvent {
	
	public void onEvent(String appName,String propname,String fullName,long collectime,
			Object value);
	
	//public void onEvent(PropertyEntry entry,int index);

}
