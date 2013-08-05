package com.taobao.csp.dataserver.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.key.DBMediaKeyCache;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.packet.ResponsePacket;
import com.taobao.csp.dataserver.packet.request.standard.QueryChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlySingleRequest;
import com.taobao.csp.dataserver.packet.request.standard.QuerySingleRequest;
import com.taobao.csp.dataserver.packet.response.standard.QueryChildResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryHostResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryMultiResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyChildResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyHostResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyMultiResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlySingleResponse;
import com.taobao.csp.dataserver.packet.response.standard.QuerySingleResponse;
import com.taobao.csp.dataserver.util.Util;

/**
 * 查询访问接口
 * 
 * @author zhongting.zy
 * 
 */
public class QueryUtil {

	private static final Logger logger = Logger.getLogger(QueryUtil.class);

	/**
	 * 查找某个key在某台机器上的的所有子key的关系，返回的结构： <childkey, <collecttime,
	 * <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyChildHostRealTime(
			String appName, String key, String ip) throws Exception {

		Map<String, Map<String, DataEntry>> map = queryRecentlyChildRealTime(
				appName, key);

		List<String> keys = new ArrayList<String>();

		for (Map.Entry<String, Map<String, DataEntry>> entry : map.entrySet()) {

			String tmp = entry.getKey().substring(appName.length() + 1);
			keys.add(tmp + Constants.S_SEPERATOR + ip);
		}

		return queryRecentlyMultiRealTime(appName, keys);
	}

	/**
	 * 查找某个key在某台机器上的的所有子key的关系，返回的结构： <childkey, <collecttime,
	 * <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryChildHostRealTime(
			String appName, String key, String ip) throws Exception {

		Map<String, Map<String, DataEntry>> map = queryRecentlyChildRealTime(
				appName, key);

		List<String> keys = new ArrayList<String>();

		for (Map.Entry<String, Map<String, DataEntry>> entry : map.entrySet()) {

			String tmp = entry.getKey().substring(appName.length() + 1);
			keys.add(tmp + Constants.S_SEPERATOR + ip);
		}

		return queryMultiRealTime(appName, keys);
	}

	/**
	 * 查找某个key的所有子key的关系，返回的结构： <childkey, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyChildRealTime(
			String appName, String key) throws Exception {
		long t = System.currentTimeMillis();

		if (key == null) {
			throw new Exception("参数为null");
		}

		Map<String, Map<String, DataEntry>> valueMap = new HashMap<String, Map<String, DataEntry>>();

		String keyName = "";

		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			return valueMap;
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryRecentlyChildRequest request = new QueryRecentlyChildRequest(
				keyName);

		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);
		if (valueList == null)
			return valueMap;

		// 组合所有机器返回的数据，进行汇总
		for (ResponsePacket packacket : valueList) {
			QueryRecentlyChildResponse childResponse = (QueryRecentlyChildResponse) packacket;
			Map<String, Map<String, DataEntry>> childMap = childResponse
					.getRecentlyMap();
			// Map<String, Map<String, DataEntry>> childMap =
			// MemcacheStandard.get().getRecentlyChildData(request);

			for (Map.Entry<String, Map<String, DataEntry>> entry : childMap
					.entrySet()) {

				if (entry.getValue() == null || entry.getValue().size() == 0) {
					continue;
				}
				String tKey = entry.getKey();
				// 按id写入以后，查出来的keyid要转为string
				tKey = keyIdToName(tKey);

				Map<String, DataEntry> m = valueMap.get(tKey);
				if (m == null) {
					m = new HashMap<String, DataEntry>();
					valueMap.put(tKey, m);
				}
				Map<String, DataEntry> e = entry.getValue();
				if (e != null) {
					for (Map.Entry<String, DataEntry> p : e.entrySet()) {
						DataEntry tmp = m.get(p.getKey());
						if (tmp == null) {
							m.put(p.getKey(), p.getValue());
						} else {
							if (tmp.getTime() < p.getValue().getTime()) {
								m.put(p.getKey(), p.getValue());
							}
						}

					}
				}

			}

		}

		logger.info("queryRecentlyChildRealTime time:"
				+ (System.currentTimeMillis() - t));
		return valueMap;
	}

	/**
	 * 查找多个key的所有子key的关系，返回的结构： <childkey, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiChildRealTime(
			String appName, String[] keys) throws Exception {

		long t = System.currentTimeMillis();

		Map<String, Map<String, DataEntry>> map = new HashMap<String, Map<String, DataEntry>>();

		for (String k : keys) {
			Map<String, Map<String, DataEntry>> m = queryRecentlyChildRealTime(
					appName, k);

			for (Map.Entry<String, Map<String, DataEntry>> entry : m.entrySet()) {

				Map<String, DataEntry> tmp = map.get(entry.getKey());
				if (tmp == null) {
					map.put(entry.getKey(), entry.getValue());
				} else {
					tmp.putAll(entry.getValue());
				}

			}

		}

		logger.info("queryRecentlyMultiChildRealTime time:"
				+ (System.currentTimeMillis() - t));

		return map;
	}

	/**
	 * 按主机查询缓存数据,返回最近的值
	 * 
	 * @param keyName
	 * @param ipList
	 * @param columnNames
	 * @return <ip,<propertyName,value>>
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyHostRealTime(
			String appName, String key, List<String> ipList) throws Exception {

		if (key == null || ipList == null || ipList.size() == 0) {
			throw new Exception("参数为null 或 ipList.size()=0");
		}
		String keyName = "";

		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",id为null");
			return new HashMap<String, Map<String, DataEntry>>();
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryRecentlyHostRequest request = new QueryRecentlyHostRequest(
				keyName, ipList);

		QueryRecentlyHostResponse responsePacket = (QueryRecentlyHostResponse) BaseQueryData
				.invoke(keyName, request);
		if (responsePacket == null) {
			return new HashMap<String, Map<String, DataEntry>>();
		}

		return responsePacket.getRecentlyMap();
	}

	/**
	 * 提供访问缓存数据接口实现 ,返回最近的一个值
	 * 
	 * @param keyName
	 *            单key查询，key的名称
	 * @return <propertyName,value>
	 * @throws Exception
	 */
	public static Map<String, DataEntry> queryRecentlySingleRealTime(
			String appName, String key) throws Exception {
		if (key == null) {
			throw new Exception("参数为null");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",id为null");
			return new HashMap<String, DataEntry>();
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryRecentlySingleRequest request = new QueryRecentlySingleRequest(
				keyName);

		QueryRecentlySingleResponse responsePacket = (QueryRecentlySingleResponse) BaseQueryData
				.invoke(keyName, request);
		if (responsePacket == null)
			return new HashMap<String, DataEntry>();

		return responsePacket.getPropertyMap();
	}

	/**
	 * 同时请求多个应用的多个key ，当时只返回最近的一个值
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap结构：<key,<propertyName,value>> key: 需要获取的key的名称
	 *         propertyName: 数据属性名 value: 熟悉值，存入时的类型
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(
			String appName, List<String> keys) throws Exception {

		List<String> apps = new ArrayList<String>();
		apps.add(appName);
		return queryRecentlyMultiRealTime(apps, keys);

	}

	/**
	 * 同时请求多个应用的多个key ，当时只返回最近的一个值
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap结构：<key,<propertyName,value>> key: 需要获取的key的名称
	 *         propertyName: 数据属性名 value: 熟悉值，存入时的类型
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(
			List<String> apps, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();
		if (keys == null || apps == null) {
			throw new Exception("参数为null");
		}

		List<String> list = new ArrayList<String>();

		for (String key : keys) {
			for (String appName : apps) {
				Integer id = DBMediaKeyCache.get().getKeyId(key);
				if (id != null) {
					list.add(Util.combinAppKeyName(appName, id));
				}
			}
		}

		Map<String, Map<String, DataEntry>> valueMap = new HashMap<String, Map<String, DataEntry>>();
		QueryRecentlyMultiRequest request = new QueryRecentlyMultiRequest(list);

		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);

		for (ResponsePacket rp : valueList) {
			QueryRecentlyMultiResponse responsePacket = (QueryRecentlyMultiResponse) rp;
			Map<String, Map<String, DataEntry>> childMap = responsePacket
					.getRecentlyMap();
			if (childMap != null) {

				for (Map.Entry<String, Map<String, DataEntry>> entry : childMap
						.entrySet()) {

					if (entry.getValue() == null
							|| entry.getValue().size() == 0) {
						continue;
					}
					String key = entry.getKey();
					// 按id写入以后，查出来的keyid要转为string
					key = keyIdToName(key);

					Map<String, DataEntry> m = valueMap.get(key);
					if (m == null) {
						m = new HashMap<String, DataEntry>();
						valueMap.put(key, m);
					}
					Map<String, DataEntry> e = entry.getValue();
					if (e != null) {
						for (Map.Entry<String, DataEntry> p : e.entrySet()) {
							DataEntry tmp = m.get(p.getKey());
							if (tmp == null) {
								m.put(p.getKey(), p.getValue());
							} else {
								if (tmp.getTime() < p.getValue().getTime()) {
									m.put(p.getKey(), p.getValue());
								}
							}
						}
					}
				}
			}
		}

		logger.info("queryRecentlyMultiRealTime time:"
				+ (System.currentTimeMillis() - t));

		return valueMap;

	}

	/**
	 * 同时请求多个应用的多个key
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap结构：<key,<time,<propertyName,value>>> key: 需要获取的key的名称
	 *         time: 收集时间，格式为:yyyyMMddHHmm propertyName: 数据属性名 value: 熟悉值，存入时的类型
	 * 
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryMultiRealTime(
			List<String> apps, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();

		if (keys == null || apps == null) {
			throw new Exception("参数为null");
		}

		List<String> list = new ArrayList<String>();
		for (String key : keys) {
			for (String appName : apps) {
				Integer keyId = DBMediaKeyCache.get().getKeyId(key);
				if (keyId != null) {
					list.add(Util.combinAppKeyName(appName,
							String.valueOf(keyId)));
				}
			}
		}

		QueryMultiRequest request = new QueryMultiRequest(list);

		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String, Map<String, Object>>>();
		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);
		for (ResponsePacket rp : valueList) {
			QueryMultiResponse responsePacket = (QueryMultiResponse) rp;
			Map<String, Map<String, Map<String, Object>>> tmp = responsePacket
					.getValueMap();
			if (tmp != null) {
				for (Map.Entry<String, Map<String, Map<String, Object>>> entry : tmp
						.entrySet()) {
					String key = entry.getKey();
					// 按id写入以后，查出来的keyid要转为string
					key = keyIdToName(key);

					Map<String, Map<String, Object>> value = entry.getValue();

					Map<String, Map<String, Object>> m = map.get(key);
					if (m == null) {
						m = new HashMap<String, Map<String, Object>>();
						map.put(key, m);
					}
					m.putAll(value);
				}

			}
		}

		logger.info("queryMultiRealTime time:"
				+ (System.currentTimeMillis() - t));

		return map;

	}

	public static String keyIdToName(String key) {
		String tKey = "";
		// 按id写入以后，查出来的keyid要转为string
		String[] ssk = key.split(Constants.S_SEPERATOR);
		// 第二位一定是id
		if (ssk.length == 2) {
			String skeyId = ssk[1];
			try {
				String skeyName = DBMediaKeyCache.get().getKeyNameById(
						Integer.parseInt(skeyId));
				if (skeyName != null) {
					tKey = ssk[0] + Constants.S_SEPERATOR + skeyName;
				}

			} catch (Exception e) {
				logger.warn("keyid to keyname exception", e);
			}
		}

		if (StringUtils.isBlank(tKey)) {
			tKey = key;
		}
		return tKey;
	}

	/**
	 * 同时请求一个应用的多个key
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap结构：<key,<time,<propertyName,value>>> key: 需要获取的key的名称
	 *         time: 收集时间，格式为:yyyyMMddHHmm propertyName: 数据属性名 value: 熟悉值，存入时的类型
	 * 
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryMultiRealTime(
			String appName, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();

		if (keys == null || appName == null) {
			throw new Exception("参数为null");
		}

		List<String> list = new ArrayList<String>();
		for (String key : keys) {
			Integer keyId = DBMediaKeyCache.get().getKeyId(key);
			if (keyId != null) {
				// keyIdMap.put(Util.combinAppKeyName(appName, key),
				// Util.combinAppKeyName(appName, String.valueOf(keyId)));
				list.add(Util.combinAppKeyName(appName, String.valueOf(keyId)));
			}
		}

		QueryMultiRequest request = new QueryMultiRequest(list);

		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String, Map<String, Object>>>();
		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);
		for (ResponsePacket rp : valueList) {
			QueryMultiResponse responsePacket = (QueryMultiResponse) rp;
			Map<String, Map<String, Map<String, Object>>> tmp = responsePacket
					.getValueMap();
			if (tmp != null) {
				for (Map.Entry<String, Map<String, Map<String, Object>>> entry : tmp
						.entrySet()) {
					String key = entry.getKey();
					// 按id写入以后，查出来的keyid要转为string
					key = keyIdToName(key);

					Map<String, Map<String, Object>> n = map.get(key);
					if (n == null) {
						n = new HashMap<String, Map<String, Object>>();
						map.put(key, n);
					}
					n.putAll(entry.getValue());
				}

			}
		}

		logger.info("queryMultiRealTime time:"
				+ (System.currentTimeMillis() - t));
		return map;

	}

	/**
	 * 提供访问缓存数据接口实现
	 * 
	 * @param keyName
	 *            单key查询，key的名称
	 * @return <time,<propertyName,value>>
	 * @throws Exception
	 */
	public static Map<String, Map<String, Object>> querySingleRealTime(
			String appName, String key) throws Exception {

		if (key == null) {
			throw new Exception("参数为null");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",id为null");
			return new HashMap<String, Map<String, Object>>();
		}
		keyName = Util.combinAppKeyName(appName, id);

		QuerySingleRequest request = new QuerySingleRequest(keyName);

		QuerySingleResponse responsePacket = (QuerySingleResponse) BaseQueryData
				.invoke(keyName, request);
		if (responsePacket == null)
			return new HashMap<String, Map<String, Object>>();

		return responsePacket.getValueMap();
	}

	/**
	 * 按主机查询缓存数据
	 * 
	 * @param keyName
	 * @param ipList
	 * @param columnNames
	 * @return <ip,<time,<propertyName,value>>>
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryHostRealTime(
			String appName, String key, List<String> ipList) throws Exception {

		long t = System.currentTimeMillis();

		if (key == null || ipList == null || ipList.size() == 0) {
			throw new Exception("参数为null 或 ipList.size()=0");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",id为null");
			return new HashMap<String, Map<String, Map<String, Object>>>();
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryHostRequest request = new QueryHostRequest(keyName, ipList);

		QueryHostResponse responsePacket = (QueryHostResponse) BaseQueryData
				.invoke(keyName, request);
		if (responsePacket == null) {
			return new HashMap<String, Map<String, Map<String, Object>>>();
		}
		Map<String, Map<String, Map<String, Object>>> qmp = responsePacket
				.getValueMap();

		Map<String, Map<String, Map<String, Object>>> valueMap = new HashMap<String, Map<String, Map<String, Object>>>();

		for (Map.Entry<String, Map<String, Map<String, Object>>> entry : qmp
				.entrySet()) {

			// 返回的是id，需要找到name
			String childKey = entry.getKey();
			String tKey = "";

			String[] ssk = childKey.split(Constants.S_SEPERATOR);
			// 第二位一定是id,第3位是ip
			if (ssk.length == 3) {
				String skeyId = ssk[1];
				try {
					String skeyName = DBMediaKeyCache.get().getKeyNameById(
							Integer.parseInt(skeyId));
					if (skeyName != null) {
						tKey = ssk[0] + Constants.S_SEPERATOR + skeyName
								+ Constants.S_SEPERATOR + ssk[2];
					}

				} catch (Exception e) {
					logger.warn("keyid to keyname exception", e);
				}
			}
			if (StringUtils.isBlank(tKey)) {
				tKey = childKey;
			}

			Map<String, Map<String, Object>> n = valueMap.get(tKey);
			if (n == null) {
				n = new HashMap<String, Map<String, Object>>();
				valueMap.put(tKey, n);
			}

			n.putAll(entry.getValue());
		}
		return valueMap;
	}

	/**
	 * 查找某个key的所有子key的关系，返回的结构： <childkey, <collecttime, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryChildRealTime(
			String appName, String key) throws Exception {

		long t = System.currentTimeMillis();

		if (key == null) {
			throw new Exception("参数为null");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",id为null");
			return null;
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryChildRequest request = new QueryChildRequest(keyName);

		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);

		if (valueList == null)
			return null;

		Map<String, Map<String, Map<String, Object>>> valueMap = new HashMap<String, Map<String, Map<String, Object>>>();
		// 组合所有机器返回的数据，进行汇总
		for (ResponsePacket packacket : valueList) {
			QueryChildResponse childResponse = (QueryChildResponse) packacket;
			Map<String, Map<String, Map<String, Object>>> childMap = childResponse
					.getValueMap();
			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : childMap
					.entrySet()) {

				// 返回的是id，需要找到name
				String childKey = entry.getKey();
				logger.warn("queryHostRealTime result keyname" + childKey);

				childKey = keyIdToName(childKey);

				logger.warn("queryHostRealTime final keyname" + childKey);

				Map<String, Map<String, Object>> n = valueMap.get(childKey);
				if (n == null) {
					n = new HashMap<String, Map<String, Object>>();
					valueMap.put(childKey, n);
				}
				n.putAll(entry.getValue());
			}

		}

		logger.info("queryHostRealTime time:"
				+ (System.currentTimeMillis() - t));

		return valueMap;
	}

	/**
	 * 查找多个key的所有子key的关系，返回的结构： <childkey, <collecttime, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryMultiChildRealTime(
			String appName, String[] keys) throws Exception {

		long t = System.currentTimeMillis();

		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String, Map<String, Object>>>();

		for (String k : keys) {
			Map<String, Map<String, Map<String, Object>>> m = queryChildRealTime(
					appName, k);

			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : m
					.entrySet()) {
				String name = entry.getKey();
				Map<String, Map<String, Object>> n = map.get(name);
				if (n == null) {
					n = new HashMap<String, Map<String, Object>>();
					map.put(name, n);
				}
				n.putAll(entry.getValue());
			}
		}

		logger.info("queryMultiChildRealTime time:"
				+ (System.currentTimeMillis() - t));

		return map;
	}

}
