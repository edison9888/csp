package com.taobao.csp.time.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.AppLeftMenuAo;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.web.po.LeftMenuPo;

public class AppLeftMenuCache {
	private static AppLeftMenuCache cache = new AppLeftMenuCache();
	private static Logger logger = Logger.getLogger(AppLeftMenuCache.class);
	private Map<String, LeftMenuPo> map = new ConcurrentHashMap<String,LeftMenuPo>();

	private AppLeftMenuCache() {
	}
	
	public void clear(){
		map.clear();
	}

	public static AppLeftMenuCache get() {
		return cache;
	}
	/**多台机器导致不一致，所以本地缓存废弃*/
	@Deprecated
	public boolean insert(String appName,LeftMenuPo po){
		boolean flag = true;
		map.put(appName, po);
		return flag;
	}
	public LeftMenuPo query(String appName) {
		LeftMenuPo lmp =null;
		
		List<LeftMenuPo> list =  AppLeftMenuAo.get().find(appName);
		if(list.size()>0){
			lmp = list.get(0);
			//map.put(appName, lmp);
		}
		if (lmp == null) {
			lmp = new LeftMenuPo();
			lmp.setPerformence(true);
			try {
				Map<String, DataEntry> valueMap = QueryUtil
						.queryRecentlySingleRealTime(appName, KeyConstants.PV);
				if (valueMap.size() == 0)
					lmp.setPv(false);
				else
					lmp.setPv(true);
			} catch (Exception e) {
				lmp.setPv(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.HSF_CONSUMER);
				if (valueMap.size() == 0)
					lmp.setHsfConsumer(false);
				else
					lmp.setHsfConsumer(true);
			} catch (Exception e) {
				lmp.setHsfConsumer(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.HSF_PROVIDER);
				if (valueMap.size() == 0)
					lmp.setHsfProvider(false);
				else
					lmp.setHsfProvider(true);
			} catch (Exception e) {
				lmp.setHsfProvider(false);
			}
			lmp.setHsfRefer(lmp.isHsfProvider());
			if (lmp.isHsfConsumer() || lmp.isHsfProvider())
				lmp.setHsf(true);
			else
				lmp.setHsf(false);
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.TAIR_CONSUMER);
				if (valueMap.size() == 0)
					lmp.setTairConsumer(false);
				else
					lmp.setTairConsumer(true);
			} catch (Exception e) {
				lmp.setTairConsumer(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.TDDL_CONSUMER);
				if (valueMap.size() == 0)
					lmp.setTddl(false);
				else
					lmp.setTddl(true);
			} catch (Exception e) {
				lmp.setTddl(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName,
								KeyConstants.NOTIFY_CONSUMER);
				if (valueMap.size() == 0)
					lmp.setNotifyConsumer(false);
				else
					lmp.setNotifyConsumer(true);
			} catch (Exception e) {
				lmp.setNotifyConsumer(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName,
								KeyConstants.NOTIFY_PROVIDER);
				if (valueMap.size() == 0)
					lmp.setNotifyProvider(false);
				else
					lmp.setNotifyProvider(true);
			} catch (Exception e) {
				lmp.setNotifyProvider(false);
			}

			if (lmp.isNotifyConsumer() || lmp.isNotifyProvider()) {
				lmp.setNotify(true);
			} else
				lmp.setNotify(false);

			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName,
								KeyConstants.SEARCH_CONSUMER);
				if (valueMap.size() == 0)
					lmp.setSearch(false);
				else
					lmp.setSearch(true);
			} catch (Exception e) {
				lmp.setSearch(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.EXCEPTION);
				if (valueMap.size() == 0)
					lmp.setException(false);
				else
					lmp.setException(true);
			} catch (Exception e) {
				lmp.setException(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.MBEAN);
				if (valueMap.size() == 0)
					lmp.setMbean(false);
				else
					lmp.setMbean(true);
			} catch (Exception e) {
				lmp.setMbean(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName,
								KeyConstants.UIC_TAIR_CLIENT);
				if (valueMap.size() == 0)
					lmp.setUicfinal(false);
				else
					lmp.setUicfinal(true);
			} catch (Exception e) {
				lmp.setUicfinal(false);
			}
			try {
				Map<String, Map<String, Map<String, Object>>> valueMap = QueryUtil
						.queryChildRealTime(appName, KeyConstants.TB_SESSION);
				if (valueMap.size() == 0)
					lmp.setTbsession(false);
				else
					lmp.setTbsession(true);
			} catch (Exception e) {
				lmp.setTbsession(false);
			}
			try {
				Map<String, Map<String, Object>>  valueMap = QueryUtil.querySingleRealTime(appName, KeyConstants.TAIR_PROVIDER);
				if (valueMap.size() == 0)
					lmp.setTairProvider(false);
				else
					lmp.setTairProvider(true);
			} catch (Exception e) {
				lmp.setTbsession(false);
			}
			lmp.setAppName(appName);
			AppLeftMenuAo.get().insert(lmp);
			map.put(appName, lmp);
		}
		return lmp;
	}
}
