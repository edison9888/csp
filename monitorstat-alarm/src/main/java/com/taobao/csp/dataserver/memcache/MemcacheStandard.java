package com.taobao.csp.dataserver.memcache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.item.KeyObject;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueObjectBox;
import com.taobao.csp.dataserver.key.ChildKeyCacheImpl;
import com.taobao.csp.dataserver.memcache.core.Memcache;
import com.taobao.csp.dataserver.memcache.entry.AppKeyEntry;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.memcache.entry.IPKeyEntry;
import com.taobao.csp.dataserver.memcache.entry.KeyEntry;
import com.taobao.csp.dataserver.memcache.entry.PropertyEntry;
import com.taobao.csp.dataserver.memcache.entry.SecondDataEntry;
import com.taobao.csp.dataserver.memcache.entry.ValueItem;
import com.taobao.csp.dataserver.packet.request.standard.QueryChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlySingleRequest;
import com.taobao.csp.dataserver.packet.request.standard.QuerySingleRequest;
import com.taobao.csp.dataserver.packet.request.standard.RequestStandard;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.monitor.MonitorLog;

public class MemcacheStandard {
	private static final Logger logger = Logger
			.getLogger(MemcacheStandard.class);

	private static MemcacheStandard cache = new MemcacheStandard();

	private long TIME = 60 * 20 * 1000;

	private MemcacheStandard() {

	}

	public static MemcacheStandard get() {
		return cache;
	}

	private Memcache memcache = new Memcache();

	public Memcache getCache() {
		return memcache;
	}
	
	/**
	 * 统一控制数据添加
	 * 
	 * @param item
	 * @param valueBox
	 * @param collectTime
	 */
	public void putData(RequestStandard r,boolean isSecond) {
		long time = System.currentTimeMillis();

		KeyObject item = r.getItem();

		MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "PV_APP", r.getAppName(),
				item.getScope().toString() }, new Long[] { 1l });

		long collectTime = r.getCollecttime();

		if (collectTime > time + Constants.DATA_FAIL_TIME_INTERVAL * 60 * 1000) {
			logger.warn(System.currentTimeMillis() + " ,收集到数据时间比当前时间还要大:"
					+ item.toString());
			MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "IgnoreOutOfTimeData", r.getAppName(),
					item.getScope().toString() }, new Long[] { 1l });
			return;
		}
		String fullName = Util.combinAppKeyName(r.getAppName(),
				item.getKeyName());
		String pfullName = Util.combinAppKeyName(r.getAppName(),
				item.getParentKeyName());
		if (item.getParentKeyName() != null)
			ChildKeyCacheImpl.putKeyChildren(pfullName, fullName);

		if (item.getScope() == KeyScope.ALL) {
			doHandleApp(r, KeyScope.APP,isSecond);
			doHandleHost(r, KeyScope.HOST,isSecond);
		} else if (item.getScope() == KeyScope.HOST) {
			doHandleHost(r, KeyScope.HOST,isSecond);
		} else if (item.getScope() == KeyScope.APP) {
			doHandleApp(r, KeyScope.APP,isSecond);
		}

		MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "PV" }, new Long[] { 1l,
				System.currentTimeMillis() - time });

	}

	public boolean rempoveKey(String keyName) {
		synchronized (memcache) {
			KeyEntry ake = (KeyEntry) MemcacheStandard.get().getCache()
					.get(keyName);
			if (ake == null) {
				return false;
			}
			long currentM = System.currentTimeMillis();
			if (currentM - ake.getRecentlyAcceptTim() > Constants.twelMinute) {
				ake.free();
				MemcacheStandard.get().getCache().remove(keyName);
				MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "RemoveKeyByTime" }, new Long[] { 1l,
						System.currentTimeMillis() - currentM });
				return true;
			}
		}
		return false;
	}

	private void doHandleApp(RequestStandard r, KeyScope scope,
			boolean isSecond) {
		long time = System.currentTimeMillis();

		ValueObjectBox valueBox = r.getValueBox();
		long collectTime = r.getCollecttime();
		String keyName = r.getItem().getKeyName();
		String appName = r.getAppName();
		String fullName = Util.combinAppKeyName(appName, keyName);

		KeyEntry array = (KeyEntry) memcache.get(fullName);

		if (array == null) {
			synchronized (memcache) {
				array = (KeyEntry) memcache.get(fullName);
				if (array == null) {
					array = new AppKeyEntry(appName, keyName, fullName,
							KeyScope.APP, memcache);
					memcache.put(fullName, array);
					MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleApp","NewKey" },
							new Long[] { 1l, System.currentTimeMillis() - time });
				}
			}
		} else {
			if (array.getKeyScope() != scope) {
				array.destroy();
				array = new AppKeyEntry(appName, keyName, fullName,
						KeyScope.APP, memcache);
				memcache.put(fullName, array);
				MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleApp","NewKeyAndScope" },
						new Long[] { 1l, System.currentTimeMillis() - time });
			}
		}
		array.addItem(collectTime, valueBox,isSecond);

		MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleApp" },
				new Long[] { 1l, System.currentTimeMillis() - time });
	}

	private void doHandleHost(RequestStandard r, KeyScope scope,
			boolean isSecond) {
		long time = System.currentTimeMillis();

		String ip = r.getIp();
		ValueObjectBox valueBox = r.getValueBox();
		long collectTime = r.getCollecttime();
		String appName = r.getAppName();
		String keyName = r.getItem().getKeyName();

		String fullName = Util.combinHostKeyName(appName, keyName, ip);

		KeyEntry array = (KeyEntry) memcache.get(fullName);

		if (array == null) {
			synchronized (memcache) {
				array = (KeyEntry) memcache.get(fullName);
				if (array == null) {
					array = new IPKeyEntry(appName, keyName, fullName, ip,
							KeyScope.HOST, memcache);
					memcache.put(fullName, array);
					MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleHost","NewKey" },
							new Long[] { 1l, System.currentTimeMillis() - time });
				}
			}
		} else {
			if (array.getKeyScope() != scope) {
				array.destroy();
				array = new IPKeyEntry(appName, keyName, fullName, ip,
						KeyScope.HOST, memcache);
				memcache.put(fullName, array);
				MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleHost","NewKeyAndScope" },
						new Long[] { 1l, System.currentTimeMillis() - time });
			}
		}

		array.addItem(collectTime, valueBox,isSecond);

		MonitorLog.addStat(Constants.MEMORY_DATA_STATUS_LOG, new String[] { "doHandleHost" },
				new Long[] { 1l, System.currentTimeMillis() - time });
	}

	/**
	 * 查询某一个key的下一级的所有key值，返回的map的格式：<childkey, <collecttime,
	 * <properties,value>>
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, Map<String, DataEntry>> getRecentlyChildData(
			QueryRecentlyChildRequest r) {
		Map<String, Map<String, DataEntry>> map = new HashMap<String, Map<String, DataEntry>>();
		String keyName = r.getKeyName();
		List<String> childlist = ChildKeyCacheImpl.getKeyChildren(keyName);
		for (String childKey : childlist) {
			Map<String, DataEntry> tmp = getRecentlySingleData(childKey);
			map.put(childKey, tmp);
		}
		return map;
	}

	/**
	 * 查询key所有机器的最近值
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, Map<String, DataEntry>> getRecentlyHostData(
			QueryRecentlyHostRequest r) {

		String keyName = r.getKeyName();
		List<String> ipList = r.getIpList();

		Map<String, Map<String, DataEntry>> map = new HashMap<String, Map<String, DataEntry>>();

		for (String ip : ipList) {
			String fullkey = Util.combinHostKeyName(keyName, ip);

			Map<String, DataEntry> tmp = getRecentlySingleData(fullkey);
			map.put(ip, tmp);
		}

		return map;

	}

	/**
	 * 同时查询多个key的最近值
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, Map<String, DataEntry>> getRecentlyMultiData(
			QueryRecentlyMultiRequest r) {

		Map<String, Map<String, DataEntry>> map = new HashMap<String, Map<String, DataEntry>>();

		List<String> keyList = r.getKeyList();
		for (String key : keyList) {

			Map<String, DataEntry> tmp = getRecentlySingleData(key);

			map.put(key, tmp);
		}
		return map;
	}

	/**
	 * 查询某个key 所有属性下的最近的一个值
	 * 
	 * @param keyName
	 * @return
	 */
	public Map<String, DataEntry> getRecentlySingleData(
			QueryRecentlySingleRequest request) {
		return getRecentlySingleData(request.getKeyName());
	}

	/**
	 * 查询某个key 所有属性下的最近的一个值
	 * 
	 * @param keyName
	 * @return
	 */
	private Map<String, DataEntry> getRecentlySingleData(String keyName) {

		long time = System.currentTimeMillis();

		Map<String, DataEntry> map = new HashMap<String, DataEntry>();

		try {
			KeyEntry array = (KeyEntry) memcache.get(keyName);
			if (array != null) {
				for (PropertyEntry item : array.getProperties()) {
					if (item != null && item.getValues() != null) {
						if (item.getRecentlyData() != null) {
							if (time - item.getRecentlyData().getTime() < TIME) {
								map.put(item.getPropertyName(),
										item.getRecentlyData());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("getRecentlySingleData查询数据出错:", e);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "getRecentlySingleDataException" },new Long[] { 1l, System.currentTimeMillis() - time});
		}
		return map;
	}

	/**
	 * 缓存中单key查询
	 * 
	 * @param QuerySingleRequest
	 */
	public Map<String, Map<String, Object>> getSingleData(QuerySingleRequest r) {
		return getSingleData(r.getKeyName());
	}

	public Map<String, Map<String, Map<String, Object>>> getMultiData(
			QueryMultiRequest r) {

		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String, Map<String, Object>>>();
		for (String key : r.getKeyList()) {
			Map<String, Map<String, Object>> valueMap = getSingleData(key);
			if (valueMap != null) {
				map.put(key, valueMap);
			}
		}

		return map;
	}

	private Map<String, Map<String, Object>> getSingleData(String keyName) {
		long time = System.currentTimeMillis();

		Map<String, Map<String, Object>> valueMap = new HashMap<String, Map<String, Object>>();
		try {
			KeyEntry array = (KeyEntry) memcache.get(keyName);
			if (array != null) {
				for (PropertyEntry item : array.getProperties()) {
					if (item != null && item.getValues() != null) {
						for (DataEntry obj : item.getValues()) {
							if (obj == null) {
								continue;
							}
							if(obj instanceof SecondDataEntry){
								SecondDataEntry minuteDataEntry=(SecondDataEntry)obj;
							
								
								for(ValueItem vi:minuteDataEntry.getValues()){
									putValueToMap(time,item.getPropertyName(),vi,valueMap);
								}
							}else{
								DataEntry minuteDataEntry=(DataEntry)obj;
								putValueToMap(time,item.getPropertyName(),minuteDataEntry.getVi(),valueMap);
							}
							
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("RealTimeQuerySingleRequest查询数据出错:", e);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "getSingleDataException" },new Long[] { 1l, System.currentTimeMillis() - time});
		}
		return valueMap;
	}

	private void putValueToMap(long currentTime,String propertyName,ValueItem vi,
			Map<String, Map<String, Object>> valueMap){
		if(vi==null){
			return;
		}
		if (currentTime - vi.getTime()< TIME) {
			Map<String, Object> tmp = valueMap.get(vi.getTime() + "");
			if (tmp == null) {
				tmp = new HashMap<String, Object>();
				valueMap.put(vi.getTime() + "", tmp);
			}
			tmp.put(propertyName, vi.getValue());
		}
	}
	
	/**
	 * 按主机查询实时数据,返回的map的格式：<ip, <collecttime, <properties,value>>
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, Map<String, Map<String, Object>>> getHostData(
			QueryHostRequest r) {
		String keyBase = r.getKeyName();
		List<String> ipList = r.getIpList();
		// 格式：<ip, <collecttime, <properties,value>>
		Map<String, Map<String, Map<String, Object>>> returnMap = new HashMap<String, Map<String, Map<String, Object>>>();
		for (String ip : ipList) {
			String key = Util.combinHostKeyName(keyBase, ip);
			// 格式<time, <propertyname,value>>
			Map<String, Map<String, Object>> map = getSingleData(key);

			returnMap.put(key, map);
		}
		return returnMap;
	}

	/**
	 * 查询某一个key的下一级的所有key值，返回的map的格式：<childkey, <collecttime,
	 * <properties,value>>
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, Map<String, Map<String, Object>>> getChildData(
			QueryChildRequest r) {
		String keyName = r.getKeyName();
		List<String> childlist = ChildKeyCacheImpl.getKeyChildren(keyName);
		Map<String, Map<String, Map<String, Object>>> returnMap = new HashMap<String, Map<String, Map<String, Object>>>();

		for (String childKey : childlist) {
			Map<String, Map<String, Object>> map = getSingleData(childKey);
			returnMap.put(childKey, map);
		}
		return returnMap;
	}

}
