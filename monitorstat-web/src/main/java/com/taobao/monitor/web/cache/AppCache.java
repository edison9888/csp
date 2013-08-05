package com.taobao.monitor.web.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 缓存所有app 信息
 * @author xiaodu
 * @version 2010-9-21 上午11:45:31
 */
public class AppCache {

	private static AppCache cache = new AppCache();

	private AppCache() {
	}

	public static AppCache get() {
		return cache;
	}

	private static Map<Integer, AppInfoPo> appIdMap = new ConcurrentHashMap<Integer, AppInfoPo>();
	private static Map<String, AppInfoPo> appNameMap = new ConcurrentHashMap<String, AppInfoPo>();
	private static Map<Integer, AppInfoPo> dayAppIdMap = new ConcurrentHashMap<Integer, AppInfoPo>();
	private static Map<String, AppInfoPo> opsnanmeAppIdMap = new ConcurrentHashMap<String, AppInfoPo>();

	public AppInfoPo getKey(int appId) {
		synchronized (AppCache.class) {
			AppInfoPo appPo = appIdMap.get(appId);
			if (appPo == null) {
				resetCache();
				appPo = appIdMap.get(appId);
			}
			if (appPo == null) {
				appPo = new AppInfoPo();
			}
			return appPo;
		}
	}
	
	public AppInfoPo getDayAppId(int appId) {
		synchronized (AppCache.class) {
			AppInfoPo appPo = dayAppIdMap.get(appId);
			if (appPo == null) {
				resetCache();
				appPo = dayAppIdMap.get(appId);
			}
			if (appPo == null) {
				appPo = new AppInfoPo();
			}
			return appPo;
		}
	}
	
	public AppInfoPo getOpsName(String opsnanme){
		synchronized (AppCache.class) {
			AppInfoPo appPo = opsnanmeAppIdMap.get(opsnanme);
			if (appPo == null) {
				resetCache();
				appPo = opsnanmeAppIdMap.get(opsnanme);
			}
			if (appPo == null) {
				appPo = new AppInfoPo();
			}
			return appPo;
		}
	}
	

	public AppInfoPo getKey(String appName) {
		synchronized (AppCache.class) {
			AppInfoPo appPo = appNameMap.get(appName);
			if (appPo == null) {
				resetCache();
				appPo = appNameMap.get(appName);
			}
			if (appPo == null) {
				appPo = new AppInfoPo();
			}
			return appPo;
		}
	}

	public List<AppInfoPo> findAllApp() {
		synchronized (AppCache.class) {
			List<AppInfoPo> list = new ArrayList<AppInfoPo>();
			list.addAll(appNameMap.values());
			return list;
		}
	}

	public void resetCache() {
		List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
		for (AppInfoPo po : list) {
			appIdMap.put(po.getAppId(), po);
			appNameMap.put(po.getAppName(), po);
			dayAppIdMap.put(po.getAppDayId(), po);
			opsnanmeAppIdMap.put(po.getOpsName(), po);
		}

	}

}
