package com.taobao.monitor.web.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.vo.SitePo;

/**
 * 缓存所有机器信息
 * @author xiaodu
 * @version 2010-9-21 上午11:45:31
 */
public class SiteCache {

	private static SiteCache cache = new SiteCache();

	private SiteCache() {
	}

	public static SiteCache get() {
		return cache;
	}

	private static Map<Integer, SitePo> siteMap = new ConcurrentHashMap<Integer, SitePo>();

	public SitePo getKey(int keyId) {
		synchronized (SiteCache.class) {
			SitePo sitePo = siteMap.get(keyId);
			if (sitePo == null) {
				resetCache();
				sitePo = siteMap.get(keyId);
			}
			if (sitePo == null) {
				sitePo = new SitePo();
			}
			return sitePo;
		}
	}

	public void resetCache() {
		Map<Integer, SitePo> map = MonitorTimeAo.get().findAllSite();
		siteMap.putAll(map);
	}

}
