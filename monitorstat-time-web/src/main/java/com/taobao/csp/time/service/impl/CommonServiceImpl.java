/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.Arith;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 * 
 *         下午3:27:57
 */
public class CommonServiceImpl implements CommonServiceInterface {

	private static final Logger logger = Logger.getLogger(CommonServiceImpl.class);
	
//	private static Map<String, String> groupMap = new HashMap<String, String>();
	private static Map<String, String> groupToAppMap = new HashMap<String, String>();
	static {
		//暂时写死，以后统一数据库配置和diamond的配置
//		groupMap.put("itemcenter`G1", "ic-L0-d");
//		groupMap.put("itemcenter`G2", "ic-L0-c");
//		groupMap.put("itemcenter`G3", "ic-L0-o");
//		groupMap.put("itemcenter`G4", "ic-L1");
//		groupMap.put("itemcenter`G5", "ic-L2");
//		groupMap.put("itemcenter`G6", "ic-bid");
//
//		groupMap.put("sell`G1", "sell");
//		groupMap.put("sell`G2", "sell-upload");
//		groupMap.put("sell`G3", "sell_top");
//
//		groupMap.put("tradeplatform`G1", "tp-g1");
//		groupMap.put("tradeplatform`G2", "tp-g2");
//		groupMap.put("tradeplatform`G3", "tp-g3");
//
//		groupMap.put("ump`G1", "Cart_ump");
//		groupMap.put("ump`G2", "detail_ump");
//		groupMap.put("ump`G3", "Noraml_ump");
//		groupMap.put("ump`G4", "Tmall_ump");
		
		groupToAppMap.put("ic-L0-d", "itemcenter");
		groupToAppMap.put("ic-L0-c", "itemcenter");
		groupToAppMap.put("ic-L0-o", "itemcenter");
		groupToAppMap.put("ic-L1", "itemcenter");
		groupToAppMap.put("ic-L2", "itemcenter");
		groupToAppMap.put("ic-bid", "itemcenter");

		groupToAppMap.put("sell-upload", "sell");
		groupToAppMap.put("sell_top", "sell");

		groupToAppMap.put("tp-g1", "tradeplatform");
		groupToAppMap.put("tp-g2", "tradeplatform");
		groupToAppMap.put("tp-g3", "tradeplatform");

		groupToAppMap.put("Cart_ump", "ump");
		groupToAppMap.put("detail_ump", "ump");
		groupToAppMap.put("Noraml_ump", "ump");
		groupToAppMap.put("Tmall_ump", "ump");
	}
	
	/**
	 * 获取应用某个key的全网平均值
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp 
	 * @return Map<site, Map<ftime, TimeDataInfo>> 最近几分钟的所有平均值
	 *TODO
	 */
	public Map<String,Map<String,TimeDataInfo>>  queryAverageKeyDataByHostForSite(String appName,
			String keyName,String mainProp){
		
		Map<String,List<TimeDataInfo>> pvmap =querykeyDataForHost(appName,keyName,mainProp,false);
		
		Map<String, Map<String, TimeDataInfo>> tmpsiteMap = new HashMap<String, Map<String, TimeDataInfo>>();
		
		Map<String, Map<String, Map<String,double[]>>> siteMap = new HashMap<String, Map<String, Map<String,double[]>>>();
		
		
		for (Map.Entry<String, List<TimeDataInfo>> entry : pvmap.entrySet()) {
			String ip = entry.getKey();
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(ip);
			if (host != null) {
				String hostsite = host.getHostSite();
				Map<String, Map<String,double[]>> timeMap = siteMap.get(hostsite);
				if (timeMap == null) {
					timeMap = new HashMap<String, Map<String,double[]>>();
					siteMap.put(hostsite, timeMap);
				}
				for (TimeDataInfo h : entry.getValue()) {
					
					if (h.getOriginalPropertyMap().size()<1) {
						continue;
					}
					Map<String,double[]> dataMap = timeMap.get(h.getFtime());
					if (dataMap == null) {
						dataMap = new HashMap<String,double[]>();
						timeMap.put(h.getFtime(), dataMap);
					}
					for(Map.Entry<String, Object> k:h.getOriginalPropertyMap().entrySet()){
						String n = k.getKey();
						Object v = k.getValue();
						try{
							double g = DataUtil.transformDouble(v);
							double[] vtmp = dataMap.get(n);
							if(vtmp == null){
								vtmp = new double[2];
								dataMap.put(n, vtmp);
							}
							vtmp[0] = Arith.add(g, vtmp[0] );
							vtmp[1] = Arith.add(1, vtmp[1] );
						}catch (Exception e) {
						}
						
					}
				}
			}
		}
		
		
		for(Map.Entry<String, Map<String, Map<String,double[]>>> entry:siteMap.entrySet()){
			String site = entry.getKey();
			Map<String, TimeDataInfo> map = tmpsiteMap.get(site);
			if(map == null){
				map = new HashMap<String, TimeDataInfo>();
				tmpsiteMap.put(site, map);
			}
			
			for(Map.Entry<String, Map<String,double[]>> timeEntry:entry.getValue().entrySet()){
				String time = timeEntry.getKey();
				Map<String,double[]> vMap = timeEntry.getValue();
				
				TimeDataInfo td = map.get(time);
				if(td ==null){
					td = new TimeDataInfo();
					map.put(time, td);
				}
				
				for(Map.Entry<String,double[]> d:vMap.entrySet()){
					td.getOriginalPropertyMap().put(d.getKey(), Arith.div(d.getValue()[0], d.getValue()[1], 1));
				}
			}
		} 
		
		return tmpsiteMap;
		
	}
	
	/**
	 * 获取应用的key 的所有子key
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param parentKey
	 * @return TODO
	 */
	public List<String> childKeyList(String appName, String parentKey) {
		AppInfoPo app = AppInfoCache.getAppInfoByAppName(appName);
		if(app == null)
			return new ArrayList<String>();
		
		List<CspKeyInfo> keyList = KeyAo.get().findKeyChildByApp(app.getAppId(), parentKey);
		List<String> keys = new ArrayList<String>();
		for (CspKeyInfo keyInfo : keyList) {
			keys.add(keyInfo.getKeyName());
		}
		return keys;
	}

	/**
	 * 获取key的所有机器的平均值
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp
	 * @return Map<String, Float> 最近几分钟的所有平均值 TODO
	 */
	public Map<String, Float> queryAverageKeyDataByHost(String appName, String keyName, String mainProp) {

		List<String> ips = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);

		String originApp = appName;
		if(groupToAppMap.containsKey(appName))
			originApp = groupToAppMap.get(appName);
		
		Map<String, Map<String, Float>> timeIpPv = new HashMap<String, Map<String, Float>>();
		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryHostRealTime(originApp, keyName, ips);

			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {
				String ip = entry.getKey().substring((originApp + Constants.S_SEPERATOR + keyName).length() + 1);
				Map<String, Map<String, Object>> timeMap = entry.getValue();

				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {
					String time = p.getKey();
					Map<String, Float> ipMap = timeIpPv.get(time);
					if (ipMap == null) {
						ipMap = new HashMap<String, Float>();
						timeIpPv.put(time, ipMap);
					}

					Map<String, Object> m = p.getValue();
					if (m.get(mainProp) != null)
						ipMap.put(ip, DataUtil.transformFloat(m.get(mainProp)));

				}

			}
		} catch (Exception e) {
			logger.error("queryAverageKeyDataByHost appName:" + appName + " keyName:" + keyName + " mainProp:" + mainProp, e);
		}

		Map<String, Float> m = new HashMap<String, Float>();

		for (Map.Entry<String, Map<String, Float>> entry : timeIpPv.entrySet()) {
			String time = TimeUtil.formatTime(Long.parseLong(entry.getKey()), "HH:mm");

			Map<String, Float> tmp = entry.getValue();
			float c = 0;
			for (Map.Entry<String, Float> e : tmp.entrySet()) {
				c += e.getValue();
			}
			if (tmp.size() > 0)
				m.put(time, c / tmp.size());
		}
		return m;
	}

	/**
	 * 查询某个key的历史数据
	 * 
	 * @author xiaodu
	 * @param appName
	 *            应用名称
	 * @param key
	 *            key的名称
	 * @param mainProp
	 *            主属性名称
	 * @param date
	 * @return TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String keyName, String mainProp, Date date) {
		List<TimeDataInfo> list = new ArrayList<TimeDataInfo>();
		try {
			// 获取key的子属性
			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);

			// 获取历史数据
			Map<Date, Map<String, String>> map = QueryHistoryUtil.queryMultiProperty(
					appName, keyName,"", propertys.toArray(new String[] {}), date);

			// 遍历历史数据
			for (Map.Entry<Date, Map<String, String>> entry : map.entrySet()) {
				Date d = entry.getKey();
				Map<String, String> m = entry.getValue();

				String mainValue = m.get(mainProp);
				if (mainValue == null) {
					continue;
				}

				TimeDataInfo info = new TimeDataInfo();
				info.setTime(d.getTime());
				info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
				info.setMainProp(mainProp);
				for (Map.Entry<String, String> e : m.entrySet())
					info.getOriginalPropertyMap().put(e.getKey(), e.getValue());
				if (m.get(mainProp) != null)
					info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
				list.add(info);

			}
		} catch (Exception e) {
			logger.error("queryKeyDataHistory appName:" + appName + " keyName:" + keyName + " mainProp:" + mainProp, e);
		}

		return list;
	}

	/**
	 * 查询某个ip的key的历史数据
	 * 
	 * @author xiaodu
	 * @param appName
	 *            应用名称
	 * @param key
	 *            key的名称
	 * @param mainProp
	 *            主属性名称
	 * @param ip
	 *            查询目标机器的ip
	 * @param date
	 * @return TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String keyName, String mainProp, String ip, Date date) {
		List<TimeDataInfo> list = new ArrayList<TimeDataInfo>();
		try {
			// 获取key的子属性
			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);

			// 获取历史数据
			Map<Date, Map<String, String>> map = QueryHistoryUtil.queryMultiProperty(
					appName, keyName,ip,
					propertys.toArray(new String[] {}), date);

			// 遍历历史数据
			for (Map.Entry<Date, Map<String, String>> entry : map.entrySet()) {
				Date d = entry.getKey();
				Map<String, String> m = entry.getValue();

				String mainValue = m.get(mainProp);
				if (mainValue == null) {
					continue;
				}

				TimeDataInfo info = new TimeDataInfo();
				info.setTime(d.getTime());
				info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
				info.setMainProp(mainProp);
				for (Map.Entry<String, String> e : m.entrySet())
					info.getOriginalPropertyMap().put(e.getKey(), e.getValue());

				if (m.get(mainProp) != null)
					info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
				list.add(info);

			}
		} catch (Exception e) {
			logger.error("queryKeyDataHistory appName:" + appName + " keyName:" + keyName + " ip:" + ip + " mainProp:" + mainProp, e);
		}

		return list;
	}

	/**
	 * 查询某个key 在所有机器上的数据信息
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return Map<ip,List<TimeDataInfo>> list 主属性排序采用降序 TODO
	 */
	public Map<String, List<TimeDataInfo>> querykeyDataForHost(String appName,
			String key, String mainProp,boolean isSecond) {

		Map<String, List<TimeDataInfo>> ipMap = new HashMap<String, List<TimeDataInfo>>();

		List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);

		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryHostRealTime(appName, key, ipList);
			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

				String ip = entry.getKey().substring((appName + Constants.S_SEPERATOR + key).length() + 1);

				Map<String, Map<String, Object>> timeMap = entry.getValue();

				List<TimeDataInfo> timeList = ipMap.get(ip);
				if (timeList == null) {
					timeList = new ArrayList<TimeDataInfo>();
					ipMap.put(ip, timeList);
				}

				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

					TimeDataInfo info = new TimeDataInfo();
					info.setAppName(appName);
					info.setKeyName(key);
					info.setFullKeyName(entry.getKey().substring((appName).length() + 1));
					info.setMainProp(mainProp);
					info.setIp(ip);

					String time = p.getKey();

					Map<String, Object> m = p.getValue();

					info.setTime(Long.parseLong(time));
					if(isSecond){
						info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm:ss"));
					}else{
						info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
					}
					info.getOriginalPropertyMap().putAll(m);

					if (m.get(mainProp) != null)
						info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));

					timeList.add(info);
				}
				
				
				Collections.sort(timeList,new Comparator<TimeDataInfo>() {
					@Override
					public int compare(TimeDataInfo o1, TimeDataInfo o2) {
						if(o1.getTime()>o2.getTime()){
							return -1;
						}else if(o1.getTime()<o2.getTime()){
							return 1;
						}
						return 0;
					}
				});
				
			}

		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appName + " key:" + key, e);
		}
		return ipMap;
	}

	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, 
			String key, String mainProp) {
		return querykeyDataForHostBySort( appName,  key,  mainProp,false);
	}
	
	/**
	 * 查询某个key 在所有机器上的数据信息
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return List<SortEntry<TimeDataInfo>> 主属性排序采用降序 TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, 
			String key, String mainProp,boolean isSecond) {

		List<SortEntry<TimeDataInfo>> list = new ArrayList<SortEntry<TimeDataInfo>>();

		Map<String, List<TimeDataInfo>> map = querykeyDataForHost(appName, 
				key, mainProp,isSecond);

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			String ip = entry.getKey();
			List<TimeDataInfo> ts = entry.getValue();

			SortEntry<TimeDataInfo> sort = new SortEntry<TimeDataInfo>();
			sort.setKeyName(key);
			sort.setAppName(appName);
			sort.setIp(ip);
			sort.setFullKeyName(key + Constants.S_SEPERATOR + ip);

			for (TimeDataInfo data : ts)
				sort.getObjectMap().put(data.getFtime(), data);
			if (ts.size() > 0)
				sort.setSortValue(DataUtil.transformDouble(ts.get(0).getMainValue()));

			list.add(sort);

		}

		Collections.sort(list);

		return list;
	}

	/**
	 * 取得某个父key 下面的所有子key的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return Map<子key名称,List<TimeDataInfo>> List<TimeDataInfo> 表示最近的几个值 TODO
	 */
	public Map<String, List<TimeDataInfo>> querykeyDataForChild(String appName, String key, String mainProp) {

		Map<String, List<TimeDataInfo>> childMap = new HashMap<String, List<TimeDataInfo>>();

		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, key);

			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

				try {
					String fullName = entry.getKey().substring(
							(appName).length() + 1);
					String childName = entry.getKey()
							.substring((appName + Constants.S_SEPERATOR + key).length() + 1);
					Map<String, Map<String, Object>> timeMap = entry.getValue();
					List<TimeDataInfo> timeList = childMap.get(fullName);
					if (timeList == null) {
						timeList = new ArrayList<TimeDataInfo>();
						childMap.put(fullName, timeList);
					}
					for (Map.Entry<String, Map<String, Object>> p : timeMap
							.entrySet()) {
						TimeDataInfo info = new TimeDataInfo();
						info.setAppName(appName);
						info.setKeyName(childName);
						info.setFullKeyName(fullName);
						info.setMainProp(mainProp);

						String time = p.getKey();

						Map<String, Object> m = p.getValue();

						info.setTime(Long.parseLong(time));
						info.setFtime(TimeUtil.formatTime(info.getTime(),
								"HH:mm"));
						info.getOriginalPropertyMap().putAll(m);

						if (m.get(mainProp) != null)
							info.setMainValue(DataUtil.transformDouble(m
									.get(mainProp)));
						timeList.add(info);
					}
					Collections.sort(timeList, new Comparator<TimeDataInfo>() {
						@Override
						public int compare(TimeDataInfo o1, TimeDataInfo o2) {
							if (o1.getTime() > o2.getTime()) {
								return -1;
							} else if (o1.getTime() < o2.getTime()) {
								return 1;
							}
							return 0;
						}
					});
				} catch (Exception e) {
					logger.error("querykeyDataForChild key异常，key=" + entry.getKey(), e);
				}
				
			}

		} catch (Exception e) {
			logger.error("querykeyDataForChild 查询 appName：" + appName + " key:" + key, e);
		}
		return childMap;
	}

	/**
	 * 取得某个父key 下面的所有子key的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key, String mainProp) {

		List<SortEntry<TimeDataInfo>> list = new ArrayList<SortEntry<TimeDataInfo>>();

		Map<String, List<TimeDataInfo>> map = querykeyDataForChild(appName, key, mainProp);

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			String fullName = entry.getKey();
			List<TimeDataInfo> ts = entry.getValue();

			SortEntry<TimeDataInfo> sort = new SortEntry<TimeDataInfo>();
			sort.setKeyName(fullName.substring(key.length() + 1));
			sort.setAppName(appName);
			sort.setFullKeyName(fullName);

			for (TimeDataInfo data : ts)
				sort.getObjectMap().put(data.getFtime(), data);
			if (ts.size() > 0)
				sort.setSortValue(DataUtil.transformDouble(ts.get(0).getMainValue()));

			list.add(sort);
		}

		Collections.sort(list);

		return list;
	}

	/**
	 * 取得所有key所有主机最近一次的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return List<TimeDataInfo> TODO
	 */
	public List<TimeDataInfo> querykeyRecentlyDataForHostBySort(String appName, String key, 
			String mainProp) {

		Map<String, List<TimeDataInfo>> map = querykeyDataForHost(appName, key, mainProp,false);

		List<TimeDataInfo> list = new ArrayList<TimeDataInfo>();

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			if (entry.getValue().size() > 0)
				list.add(entry.getValue().get(0));
		}

		Collections.sort(list, new Comparator<TimeDataInfo>() {
			@Override
			public int compare(TimeDataInfo o1, TimeDataInfo o2) {

				if (o1.getMainValue() > o2.getMainValue()) {
					return -1;
				} else if (o1.getMainValue() < o2.getMainValue()) {
					return 1;
				}
				return 0;
			}
		});

		return list;
	}

	/**
	 * 取得父key的所有子key最近一次的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return List<TimeDataInfo> TODO
	 */
	public List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key, String mainProp) {
		Map<String, List<TimeDataInfo>> map = querykeyDataForChild(appName, key, mainProp);

		List<TimeDataInfo> list = new ArrayList<TimeDataInfo>();

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			if (entry.getValue().size() > 0)
				list.add(entry.getValue().get(0));
		}
		
		Collections.sort(list,new Comparator<TimeDataInfo>() {
			@Override
			public int compare(TimeDataInfo o1, TimeDataInfo o2) {
				if(o1.getMainValue()>o2.getMainValue()){
					return -1;
				}else if(o1.getMainValue()<o2.getMainValue()){
					return 1;
				}
				return 0;
			}
		});
		
		
		
		return list;
	}

	/**
	 * 查询应用类表获取到这些应用对应的key的数据 ，这个级别是应用级别
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return Map<appName,List<TimeDataInfo>> list 主属性排序采用降序 TODO
	 */
	public Map<String, List<TimeDataInfo>> querykeyDataForApps(List<String> appNames, String key, String mainProp) {

		Map<String, List<TimeDataInfo>> appMap = new HashMap<String, List<TimeDataInfo>>();

		List<String> keys = new ArrayList<String>();
		keys.add(key);

		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryMultiRealTime(appNames, keys);
			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

				String[] tmp = entry.getKey().split(Constants.S_SEPERATOR);

				String appName = tmp[0];

				Map<String, Map<String, Object>> timeMap = entry.getValue();

				List<TimeDataInfo> timeList = appMap.get(appName);
				if (timeList == null) {
					timeList = new ArrayList<TimeDataInfo>();
					appMap.put(appName, timeList);
				}

				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

					TimeDataInfo info = new TimeDataInfo();
					info.setAppName(appName);
					info.setKeyName(key);
					info.setFullKeyName(key);
					info.setMainProp(mainProp);

					String time = p.getKey();

					Map<String, Object> m = p.getValue();

					info.setTime(Long.parseLong(time));
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
					info.getOriginalPropertyMap().putAll(m);

					if (m.get(mainProp) != null)
						info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));

					timeList.add(info);
				}
				
				Collections.sort(timeList,new Comparator<TimeDataInfo>() {
					@Override
					public int compare(TimeDataInfo o1, TimeDataInfo o2) {
						if(o1.getTime()>o2.getTime()){
							return -1;
						}else if(o1.getTime()<o2.getTime()){
							return 1;
						}
						return 0;
					}
				});
			}

		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appNames + " key:" + key, e);
		}
		return appMap;
	}

	/**
	 * 查询某台机器下 取得父key的所有子key最近一次的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return List<TimeDataInfo> TODO
	 */
	public List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key, String mainProp, String ip) {
		Map<String, List<TimeDataInfo>> map = querykeyDataForChild(appName, key, mainProp, ip);

		List<TimeDataInfo> list = new ArrayList<TimeDataInfo>();

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			if (entry.getValue().size() > 0)
				list.add(entry.getValue().get(0));
		}
		return list;
	}

	/**
	 * 查询某台机器下取得某个父key 下面的所有子key的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key, String mainProp, String ip) {

		List<SortEntry<TimeDataInfo>> list = new ArrayList<SortEntry<TimeDataInfo>>();

		Map<String, List<TimeDataInfo>> map = querykeyDataForChild(appName, key, mainProp, ip);

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			String fullName = entry.getKey();
			List<TimeDataInfo> ts = entry.getValue();

			SortEntry<TimeDataInfo> sort = new SortEntry<TimeDataInfo>();
			sort.setKeyName(fullName.substring(key.length() + 1));
			sort.setAppName(appName);
			sort.setFullKeyName(fullName);

			for (TimeDataInfo data : ts)
				sort.getObjectMap().put(data.getFtime(), data);
			if (ts.size() > 0)
				sort.setSortValue(DataUtil.transformDouble(ts.get(0).getMainValue()));

			list.add(sort);
		}

		Collections.sort(list);

		return list;
	}

	/**
	 * 查询某台机器下取得某个父key 下面的所有子key的数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @param ip
	 *            主机IP
	 * @return Map<子key名称,List<TimeDataInfo>> List<TimeDataInfo> 表示最近的几个值 TODO
	 */
	public Map<String, List<TimeDataInfo>> querykeyDataForChild(String appName, String key, String mainProp, String ip) {

		List<String> childKeys = childKeyList(appName, key);

		List<String> keys = new ArrayList<String>();
		for (String k : childKeys)
			keys.add(k + Constants.S_SEPERATOR + ip);

		Map<String, List<TimeDataInfo>> childMap = new HashMap<String, List<TimeDataInfo>>();

		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryMultiRealTime(appName, keys);

			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {
				int last = entry.getKey().lastIndexOf(Constants.S_SEPERATOR);
				int first = entry.getKey().indexOf(Constants.S_SEPERATOR);

				String fullName = entry.getKey().substring(first + 1, last);

				int second = fullName.lastIndexOf(Constants.S_SEPERATOR);
				String childName = fullName.substring(second + 1);

				Map<String, Map<String, Object>> timeMap = entry.getValue();

				List<TimeDataInfo> timeList = childMap.get(fullName);
				if (timeList == null) {
					timeList = new ArrayList<TimeDataInfo>();
					childMap.put(fullName, timeList);
				}

				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

					TimeDataInfo info = new TimeDataInfo();
					info.setAppName(appName);
					info.setKeyName(childName);
					info.setFullKeyName(fullName);
					info.setMainProp(mainProp);

					String time = p.getKey();

					Map<String, Object> m = p.getValue();

					info.setTime(Long.parseLong(time));
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
					info.getOriginalPropertyMap().putAll(m);

					if (m.get(mainProp) != null)
						info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
					timeList.add(info);

				}
				
				
				Collections.sort(timeList,new Comparator<TimeDataInfo>() {
					@Override
					public int compare(TimeDataInfo o1, TimeDataInfo o2) {
						if(o1.getTime()>o2.getTime()){
							return -1;
						}else if(o1.getTime()<o2.getTime()){
							return 1;
						}
						return 0;
					}
				});
			}

		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appName + " key:" + key + "ip:" + ip + " mainProp:" + mainProp, e);
		}
		return childMap;

	}

	public List<TimeDataInfo> querySingleKeyData(String appName, String key, String mainProp) {
		return querySingleKeyData(appName,key,mainProp,false);
	}
	
	/**
	 * 查询某个key的流量信息
	 * 
	 * @author xiaodu
	 * @param appName
	 *            应用名称
	 * @param key
	 *            key的名称
	 * @param mainProp
	 *            主属性名称
	 * @param date
	 * @return TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key, String mainProp,
			boolean isSecond) {
		List<TimeDataInfo> timeList = new ArrayList<TimeDataInfo>();
		
		try {
			Map<String, Map<String, Object>> timeMap = null;
			if(groupToAppMap.containsKey(appName)) {
				timeMap = new HashMap<String, Map<String,Object>>();
				//查询分组
				final String originApp = groupToAppMap.get(appName);
				List<String> ips = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
				Map<String,Map<String,Map<String,Object>>> hostValueList = QueryUtil.
					queryHostRealTime(originApp, key, ips);
				
				for(String ip : hostValueList.keySet()) {
					Map<String,Map<String,Object>> tmpMap = hostValueList.get(ip);
					for(String time : tmpMap.keySet()) {
						Map<String, Object> valueMap = timeMap.get(time);
						if(valueMap == null) {
							valueMap = new HashMap<String, Object>();
							timeMap.put(time, valueMap);
						}
						
						Map<String,Object> properMap = tmpMap.get(time);
						for(Map.Entry<String,Object> entry : properMap.entrySet()) {
							String property = entry.getKey();
							Object value = entry.getValue();
							Object lastValue = valueMap.get(property);
							if(lastValue == null) {
								valueMap.put(property, value);
							} else {
								valueMap.put(property, Util.add(lastValue, value));//暂时全部
							}
						}
					}
				}
				
			} else {
				timeMap = QueryUtil.querySingleRealTime(appName, key);//查询应用	
			}
			
			
			for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

				TimeDataInfo info = new TimeDataInfo();
				info.setAppName(appName);
				info.setKeyName(key);
				info.setFullKeyName(key);
				info.setMainProp(mainProp);

				String time = p.getKey();

				Map<String, Object> m = p.getValue();
				info.setOriginalPropertyMap(m);	//把原来的值放进来

				info.setTime(Long.parseLong(time));
				if(isSecond){
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm:ss"));
				}else{
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
				}
				info.getOriginalPropertyMap().putAll(m);

				if (m.get(mainProp) != null)
					info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));

				timeList.add(info);
			}
			
			Collections.sort(timeList,new Comparator<TimeDataInfo>() {
				@Override
				public int compare(TimeDataInfo o1, TimeDataInfo o2) {
					if(o1.getTime()>o2.getTime()){
						return -1;
					}else if(o1.getTime()<o2.getTime()){
						return 1;
					}
					return 0;
				}
			});

		} catch (Exception e) {
			logger.error("querySingleKeyData 查询 appName：" + appName + " key:" + key, e);
			//e.printStackTrace();
		}

		return timeList;
	}

	/**
	 * 查询某个key的流量信息
	 * 
	 * @author xiaodu
	 * @param appName
	 *            应用名称
	 * @param key
	 *            key的名称
	 * @param mainProp
	 *            主属性名称
	 * @param ip
	 *            机器ip
	 * @param date
	 * @return TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key, String mainProp, String ip) {
		return querySingleKeyDataDetail(appName,  key,  mainProp,  ip,false);
	}

	private List<TimeDataInfo> querySingleKeyDataDetail(
			String appName, String key, String mainProp, String ip,boolean isSecond) {

		List<TimeDataInfo> timeList = new ArrayList<TimeDataInfo>();

		try {
			Map<String, Map<String, Object>> timeMap = QueryUtil.querySingleRealTime(appName, key + Constants.S_SEPERATOR + ip);
			for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

				TimeDataInfo info = new TimeDataInfo();
				info.setAppName(appName);
				info.setKeyName(key);
				info.setFullKeyName(key);
				info.setMainProp(mainProp);
				info.setIp(ip);
				String time = p.getKey();

				Map<String, Object> m = p.getValue();

				info.setTime(Long.parseLong(time));
				if(isSecond){
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm:ss"));
				}else{

					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
				}
				info.getOriginalPropertyMap().putAll(m);

				if (m.get(mainProp) != null)
					info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));

				timeList.add(info);
			}
			
			Collections.sort(timeList,new Comparator<TimeDataInfo>() {
				@Override
				public int compare(TimeDataInfo o1, TimeDataInfo o2) {
					if(o1.getTime()>o2.getTime()){
						return -1;
					}else if(o1.getTime()<o2.getTime()){
						return 1;
					}
					return 0;
				}
			});

		} catch (Exception e) {
			logger.error("querySingleKeyData 查询 appName：" + appName + " key:" + key + "ip:" + ip, e);
		}

		return timeList;
	}
	
	
	/**
	 * 查询多个key的 最近数据
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param keys
	 * @param mainProp
	 * @param ip
	 * @return TODO
	 */
	public Map<String, List<TimeDataInfo>> queryMutilKeyData(String appName, List<String> keys, String mainProp) {
		Map<String, List<TimeDataInfo>> mutilMap = new HashMap<String, List<TimeDataInfo>>();

		try {

			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryMultiRealTime(appName, keys);

			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {
				int first = entry.getKey().indexOf(Constants.S_SEPERATOR);
				String fullName = entry.getKey().substring(first + 1);

				Map<String, Map<String, Object>> timeMap = entry.getValue();

				List<TimeDataInfo> timeList = mutilMap.get(fullName);
				if (timeList == null) {
					timeList = new ArrayList<TimeDataInfo>();
					mutilMap.put(fullName, timeList);
				}

				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

					TimeDataInfo info = new TimeDataInfo();
					info.setAppName(appName);
					info.setKeyName(fullName);
					info.setFullKeyName(fullName);
					info.setMainProp(mainProp);

					String time = p.getKey();

					Map<String, Object> m = p.getValue();

					info.setTime(Long.parseLong(time));
					info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
					info.getOriginalPropertyMap().putAll(m);

					if (m.get(mainProp) != null)
						info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
					timeList.add(info);

				}
				
				Collections.sort(timeList,new Comparator<TimeDataInfo>() {
					@Override
					public int compare(TimeDataInfo o1, TimeDataInfo o2) {
						if(o1.getTime()>o2.getTime()){
							return -1;
						}else if(o1.getTime()<o2.getTime()){
							return 1;
						}
						return 0;
					}
				});
			}

		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appName + " key:" + keys + " mainProp:" + mainProp, e);
		}
		return mutilMap;

	}

	/**
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param key
	 *            父key名称
	 * @param mainProp
	 *            主属性，用来排序展示的属性
	 * @return TODO
	 */
	public List<SortEntry<TimeDataInfo>> queryMutilKeyDataBySort(String appName, List<String> keys, String mainProp) {

		List<SortEntry<TimeDataInfo>> list = new ArrayList<SortEntry<TimeDataInfo>>();

		Map<String, List<TimeDataInfo>> map = queryMutilKeyData(appName, keys, mainProp);

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			String fullName = entry.getKey();
			List<TimeDataInfo> ts = entry.getValue();

			SortEntry<TimeDataInfo> sort = new SortEntry<TimeDataInfo>();
			// 取`分割后的最后一部分为key name
			int lastIdx = fullName.lastIndexOf(Constants.S_SEPERATOR);
			String keyName = lastIdx != -1 ? fullName.substring(lastIdx + 1) : fullName;
			sort.setKeyName(keyName);
			sort.setAppName(appName);
			sort.setFullKeyName(fullName);

			for (TimeDataInfo data : ts)
				sort.getObjectMap().put(data.getFtime(), data);
			if (ts.size() > 0)
				sort.setSortValue(DataUtil.transformDouble(ts.get(0).getMainValue()));

			list.add(sort);
		}

		Collections.sort(list);

		return list;
	}

	/**
	 * 查询某个key的流量信息
	 * 
	 * @author xiaodu
	 * @param appName
	 *            应用名称
	 * @param key
	 *            key的名称
	 * @param mainProp
	 *            主属性名称
	 * @param date
	 * @return TODO
	 */
	public TimeDataInfo querySingleRecentlyKeyData(String appName, String key, String mainProp) {
		List<TimeDataInfo> list = querySingleKeyData(appName, key, mainProp);
		if(list.size() ==1){
			return list.get(0);
		}else if (list.size() > 1) {
			return list.get(1);
		} else {
			TimeDataInfo d = new TimeDataInfo();
			return d;
		}
	}
}
