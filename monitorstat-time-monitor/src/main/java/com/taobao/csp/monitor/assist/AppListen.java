
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.assist;

import java.util.List;

/**
 * @author xiaodu
 *
 * обнГ12:38:52
 */
public interface AppListen {
	
	public void appChange(List<String> apps);
	
	public void appAdd(String app);
	
	public void appDelete(String app);

}
