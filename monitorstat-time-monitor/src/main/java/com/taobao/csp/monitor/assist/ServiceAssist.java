
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.assist;

import java.util.List;

/**
 * @author xiaodu
 *
 * обнГ8:01:30
 */
public interface ServiceAssist {
	
	
	public void addCollectorListen(CollectorListen listen);
	
	public void addAppListen(AppListen appLisen);
	
	public List<String> findGroupCollectors();
	
	public List<String> findApps();
	
	public void registerCollector();
	
	public void logoutCollector();
	
	public void heartbeat(Object obj);
	
	
	

}
