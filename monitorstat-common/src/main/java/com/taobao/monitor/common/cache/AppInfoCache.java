
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * @author xiaodu
 *
 * 上午9:23:03
 */
public class AppInfoCache {
	
	private static Map<Integer, AppInfoPo> appIdMap = new ConcurrentHashMap<Integer, AppInfoPo>();
	private static Map<String, AppInfoPo> appNameMap = new ConcurrentHashMap<String, AppInfoPo>();
	private static final Logger logger = Logger.getLogger(AppInfoCache.class);
	
	
	public static AppInfoPo getAppInfoById(Integer appId){
		AppInfoPo app = appIdMap.get(appId);
		if(app==null){
			synchronized (appIdMap) {
				 app = appIdMap.get(appId);
				 if(app == null){
					 resetCache();
					return appIdMap.get(appId);
				}
			}
		}
		return app;
	}
	
	
	public static AppInfoPo getAppInfoByAppName(String appName){
		AppInfoPo app = appNameMap.get(appName);
		if(app==null){
			synchronized (appIdMap) {
				 app = appNameMap.get(appName);
				 if(app == null){
					 logger.info("****应用为Null****" + appName);
					 resetCache();
					return app = appNameMap.get(appName);
				}
			}
		}
		return app;
	}
	
	
	
	
	private static void resetCache() {
		appIdMap.clear();
		appNameMap.clear();
		List<AppInfoPo> list = AppInfoAo.get().findAllAppInfo();
		for (AppInfoPo po : list) {
			appIdMap.put(po.getAppId(), po);
			appNameMap.put(po.getAppName(), po);
		}

	}

}
