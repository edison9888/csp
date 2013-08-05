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
 * ��ѯ���ʽӿ�
 * 
 * @author zhongting.zy
 * 
 */
public class QueryUtil {

	private static final Logger logger = Logger.getLogger(QueryUtil.class);

	/**
	 * ����ĳ��key��ĳ̨�����ϵĵ�������key�Ĺ�ϵ�����صĽṹ�� <childkey, <collecttime,
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
	 * ����ĳ��key��ĳ̨�����ϵĵ�������key�Ĺ�ϵ�����صĽṹ�� <childkey, <collecttime,
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
	 * ����ĳ��key��������key�Ĺ�ϵ�����صĽṹ�� <childkey, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyChildRealTime(
			String appName, String key) throws Exception {
		long t = System.currentTimeMillis();

		if (key == null) {
			throw new Exception("����Ϊnull");
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

		// ������л������ص����ݣ����л���
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
				// ��idд���Ժ󣬲������keyidҪתΪstring
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
	 * ���Ҷ��key��������key�Ĺ�ϵ�����صĽṹ�� <childkey, <properties,value>>
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
	 * ��������ѯ��������,���������ֵ
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
			throw new Exception("����Ϊnull �� ipList.size()=0");
		}
		String keyName = "";

		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",idΪnull");
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
	 * �ṩ���ʻ������ݽӿ�ʵ�� ,���������һ��ֵ
	 * 
	 * @param keyName
	 *            ��key��ѯ��key������
	 * @return <propertyName,value>
	 * @throws Exception
	 */
	public static Map<String, DataEntry> queryRecentlySingleRealTime(
			String appName, String key) throws Exception {
		if (key == null) {
			throw new Exception("����Ϊnull");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",idΪnull");
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
	 * ͬʱ������Ӧ�õĶ��key ����ʱֻ���������һ��ֵ
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap�ṹ��<key,<propertyName,value>> key: ��Ҫ��ȡ��key������
	 *         propertyName: ���������� value: ��Ϥֵ������ʱ������
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(
			String appName, List<String> keys) throws Exception {

		List<String> apps = new ArrayList<String>();
		apps.add(appName);
		return queryRecentlyMultiRealTime(apps, keys);

	}

	/**
	 * ͬʱ������Ӧ�õĶ��key ����ʱֻ���������һ��ֵ
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap�ṹ��<key,<propertyName,value>> key: ��Ҫ��ȡ��key������
	 *         propertyName: ���������� value: ��Ϥֵ������ʱ������
	 * @throws Exception
	 */
	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(
			List<String> apps, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();
		if (keys == null || apps == null) {
			throw new Exception("����Ϊnull");
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
					// ��idд���Ժ󣬲������keyidҪתΪstring
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
	 * ͬʱ������Ӧ�õĶ��key
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap�ṹ��<key,<time,<propertyName,value>>> key: ��Ҫ��ȡ��key������
	 *         time: �ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm propertyName: ���������� value: ��Ϥֵ������ʱ������
	 * 
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryMultiRealTime(
			List<String> apps, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();

		if (keys == null || apps == null) {
			throw new Exception("����Ϊnull");
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
					// ��idд���Ժ󣬲������keyidҪתΪstring
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
		// ��idд���Ժ󣬲������keyidҪתΪstring
		String[] ssk = key.split(Constants.S_SEPERATOR);
		// �ڶ�λһ����id
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
	 * ͬʱ����һ��Ӧ�õĶ��key
	 * 
	 * @param apps
	 * @param keys
	 * @return valueMap�ṹ��<key,<time,<propertyName,value>>> key: ��Ҫ��ȡ��key������
	 *         time: �ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm propertyName: ���������� value: ��Ϥֵ������ʱ������
	 * 
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryMultiRealTime(
			String appName, List<String> keys) throws Exception {

		long t = System.currentTimeMillis();

		if (keys == null || appName == null) {
			throw new Exception("����Ϊnull");
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
					// ��idд���Ժ󣬲������keyidҪתΪstring
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
	 * �ṩ���ʻ������ݽӿ�ʵ��
	 * 
	 * @param keyName
	 *            ��key��ѯ��key������
	 * @return <time,<propertyName,value>>
	 * @throws Exception
	 */
	public static Map<String, Map<String, Object>> querySingleRealTime(
			String appName, String key) throws Exception {

		if (key == null) {
			throw new Exception("����Ϊnull");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",idΪnull");
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
	 * ��������ѯ��������
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
			throw new Exception("����Ϊnull �� ipList.size()=0");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",idΪnull");
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

			// ���ص���id����Ҫ�ҵ�name
			String childKey = entry.getKey();
			String tKey = "";

			String[] ssk = childKey.split(Constants.S_SEPERATOR);
			// �ڶ�λһ����id,��3λ��ip
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
	 * ����ĳ��key��������key�Ĺ�ϵ�����صĽṹ�� <childkey, <collecttime, <properties,value>>
	 * 
	 * @param keyName
	 * @throws Exception
	 */
	public static Map<String, Map<String, Map<String, Object>>> queryChildRealTime(
			String appName, String key) throws Exception {

		long t = System.currentTimeMillis();

		if (key == null) {
			throw new Exception("����Ϊnull");
		}

		String keyName = "";
		Integer id = DBMediaKeyCache.get().getKeyId(key);
		if (id == null) {
			// logger.warn(key+",idΪnull");
			return null;
		}
		keyName = Util.combinAppKeyName(appName, id);

		QueryChildRequest request = new QueryChildRequest(keyName);

		List<ResponsePacket> valueList = BaseQueryData.invokesAll(request);

		if (valueList == null)
			return null;

		Map<String, Map<String, Map<String, Object>>> valueMap = new HashMap<String, Map<String, Map<String, Object>>>();
		// ������л������ص����ݣ����л���
		for (ResponsePacket packacket : valueList) {
			QueryChildResponse childResponse = (QueryChildResponse) packacket;
			Map<String, Map<String, Map<String, Object>>> childMap = childResponse
					.getValueMap();
			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : childMap
					.entrySet()) {

				// ���ص���id����Ҫ�ҵ�name
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
	 * ���Ҷ��key��������key�Ĺ�ϵ�����صĽṹ�� <childkey, <collecttime, <properties,value>>
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
