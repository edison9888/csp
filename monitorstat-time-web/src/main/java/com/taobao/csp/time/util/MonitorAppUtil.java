
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.common.ZKClient;

/**
 * @author xiaodu
 *
 *操作和获取已经在实时监控内的应用信息
 *
 * 下午5:49:56
 */
public class MonitorAppUtil {
	
	public static final Logger log = Logger.getLogger(MonitorAppUtil.class);
	
	public static String ZK_MONITOR_APP_ROOT = "/csp/monitor/app";
	
	public static List<String> getMonitorApps(){
		List<String> monitorlist = null;
		try {
			monitorlist = ZKClient.get().list(ZK_MONITOR_APP_ROOT);
		} catch (Exception e) {
			log.error("获取监控应用失败", e);
		}
		
		return monitorlist;
	}
	
	
	public static void changeconfig(String action,String appName) {
		try {
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("action",action);
			map.put("appName", appName);
			ZKClient.get().setData(ZK_MONITOR_APP_ROOT,map);
		} catch (Exception e) {
		}
	}

}
