package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.AppIndexInfo;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 进入应用详情，首先看得一个应用概括页面
 * 
 * 
 * @author xiaodu
 * 
 *         下午2:16:08
 */
@Controller
@RequestMapping("/app/detail/show.do")
public class AppIndexController extends BaseController {
	private static Logger logger = Logger.getLogger(AppIndexController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
//
//	@Resource(name = "appIndexServiceBean")
//	private AppIndexServiceInterface appIndexHelp;
//
//	@Resource(name = "hsfService")
//	private HSFService hsfHelp;
//
//	@Resource(name = "tairService")
//	private AppTairService tairService;

	/**
	 * @throws Exception
	 * 
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView graphAppRelation(int appId) throws Exception {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/app_index");
		view.addObject("appInfo", appInfo);
		AppIndexInfo appIndexInfo = new AppIndexInfo();
		String appName = appInfo.getAppName();

//		Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(
//				appName, KeyConstants.PV);
//		if (map == null || map.size() == 0) {
//			appIndexInfo.setPv(false);
//		} else {
//			appIndexInfo.setPv(true);
//		}
//
//		Map<String, Map<String, Object>> map2 = QueryUtil.querySingleRealTime(
//				appName, KeyConstants.HSF_PROVIDER);
//		if (map2 == null || map2.size() == 0) {
//			appIndexInfo.setHsfProvider(false);
//		} else {
//			appIndexInfo.setHsfProvider(true);
//		}
//		Map<String, Map<String, Object>> map3 = QueryUtil.querySingleRealTime(
//				appName, "Tair-Consumer");
//		if (map3 == null || map3.size() == 0) {
//			appIndexInfo.setTair(false);
//		} else {
//			appIndexInfo.setTair(true);
//		}
//		Map<String, Map<String, Object>> map4 = QueryUtil.querySingleRealTime(
//				appName, KeyConstants.HSF_CONSUMER);
//		if (map4 == null || map4.size() == 0) {
//			appIndexInfo.setHsfConsumer(false);
//		} else {
//			appIndexInfo.setHsfConsumer(true);
//		}
//		view.addObject("appIndexInfo", appIndexInfo);
		return view;
	}

	@RequestMapping(params = "method=appIndexPv")
	public void appIndexPv(HttpServletResponse response, Integer appId) {
		try {
			AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
			List<TimeDataInfo> list = commonService.querySingleKeyData(appInfo
					.getAppName(), KeyConstants.PV, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=appIndexPvTop10")
	public void appIndexPvRefer(HttpServletResponse response, Integer appId) {

		try {
			AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
			List<TimeDataInfo> referList = commonService
					.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
							KeyConstants.PV_REFER, PropConstants.E_TIMES);
			List<TimeDataInfo> sourceList = commonService
					.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
							KeyConstants.PV, PropConstants.E_TIMES);
			Map<String, List<TimeDataInfo>> map = new HashMap<String, List<TimeDataInfo>>();

			if (referList.size() > 10) {
				referList = referList.subList(0, 10);
			}
			map.put("refer", referList);

			if (sourceList.size() > 10) {
				sourceList = sourceList.subList(0, 10);
			}
			map.put("source", sourceList);
			writeJSONToResponseJSONObject(response, map);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	// @RequestMapping(params="method=appIndexPvSource")
	// public void appIndexPvSource(HttpServletResponse response,Integer appId)
	// throws Exception{
	// try {
	// AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
	// List<TimeDataInfo> list =
	// commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
	// KeyConstants.PV_SOURCE, PropConstants.E_TIMES);
	// writeJSONToResponseJSONArray(response, list);
	// } catch (Exception e) {
	// logger.info(e);
	// }
	// }
	@RequestMapping(params = "method=appIndexHSFProvider")
	public void appIndexHSFProvider(HttpServletResponse response, Integer appId)
			throws Exception {
		try {
			AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
			List<TimeDataInfo> list = commonService.querySingleKeyData(appInfo
					.getAppName(), KeyConstants.HSF_PROVIDER,
					PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=appIndexHSFProviderTop10")
	public void findHsfReferAppAndProviderInterface(
			HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String, List<TimeDataInfo>> result = new HashMap<String, List<TimeDataInfo>>();
		List<TimeDataInfo> providerList = commonService
				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
		if (providerList.size() > 10) {
			providerList = providerList.subList(0, 10);
		}
		result.put("providerList", providerList);

		List<TimeDataInfo> referList = commonService
				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.HSF_REFER, PropConstants.E_TIMES);
		if (referList.size() > 10) {
			referList = referList.subList(0, 10);
		}
		result.put("referList", referList);

		try {
			writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=appIndexTair")
	public void appIndexTair(HttpServletResponse response, Integer appId)
			throws Exception {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<TimeDataInfo> list = commonService.querySingleKeyData(appInfo
				.getAppName(), KeyConstants.TAIR_CONSUMER,
				PropConstants.E_TIMES);
		writeJSONToResponseJSONArray(response, list);
	}

	@RequestMapping(params = "method=appIndexTairTop10")
	public void appIndexTairTop10(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String, List<TimeDataInfo>> result = new HashMap<String, List<TimeDataInfo>>();
		List<TimeDataInfo> groupList = commonService
				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES);
		if (groupList.size() > 10) {
			groupList = groupList.subList(0, 10);
		}
		result.put("group", groupList);

		List<TimeDataInfo> hostList = commonService
				.querykeyRecentlyDataForHostBySort(appInfo.getAppName(),
						KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES);
		if (hostList.size() > 10) {
			hostList = hostList.subList(0, 10);
		}
		result.put("host", hostList);

		try {
			writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
		}

	}


	@RequestMapping(params = "method=appIndexHsfConsumer")
	public void appIndexHSFConsumer(HttpServletResponse response, Integer appId)
			throws Exception {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<TimeDataInfo> list = commonService.querySingleKeyData(appInfo
				.getAppName(), KeyConstants.HSF_CONSUMER,
				PropConstants.E_TIMES);
		writeJSONToResponseJSONArray(response, list);
	}

	@RequestMapping(params = "method=appIndexHsfConsumerTop10")
	public void appIndexHSFConsumerTop10(HttpServletResponse response,
			Integer appId) throws Exception {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String, List<TimeDataInfo>> result = new HashMap<String, List<TimeDataInfo>>();

		List<TimeDataInfo> applist = commonService
				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.HSF_CONSUMER, PropConstants.E_TIMES);
		
		if (applist.size() > 10) {
			applist = applist.subList(0, 10);
		}
		result.put("app", applist);
//		List<String> keys = new ArrayList<String>();
//		for(TimeDataInfo po : applist){
//			String key = po.getFullKeyName();
//			key = key.substring(key.indexOf(Constants.S_SEPERATOR)+1);
//			keys.add(key);
//		}
//		
//		List<TimeDataInfo> classlist = commonService
//				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),
//						KeyConstants.HSF_CONSUMER, PropConstants.E_TIMES);
//	
//		
//		
//		if (classlist.size() > 10) {
//			classlist = classlist.subList(0, 10);
//		}
//		result.put("class", applist);

		try {
			writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
		}
	}

	// @RequestMapping(params="method=appIndexHSFConsumerInterface")
	// public void appIndexHSFConsumerInterface(HttpServletResponse response,
	// Integer appId) throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// String key = KeyConstants.HSF_CONSUMER;
	// List<HsfInfo> list = appIndexHelp.getHsfConsumerInterfaceByApp(appName,
	// key);
	// if(list.size()>10)list = list.subList(0, 10);
	// Map jsonMap = new HashMap();
	// jsonMap.put("list", list);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
	// @RequestMapping(params="method=appIndexHSFConsumerTargetApp")
	// public void appIndexHSFConsumerTargetApp(HttpServletResponse response,
	// Integer appId) throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// String keyName = KeyConstants.HSF_CONSUMER;
	// Map<String,HsfInfo> jsonMap = hsfHelp.findHsfChildRecentlyByApp(appName,
	// keyName);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
	public static void main(String args[]) throws Exception {
		Map<String, Map<String, Map<String, Object>>> map = QueryUtil
				.queryChildRealTime("detail", KeyConstants.PV);
	}
	// @RequestMapping(params="method=appIndexTairGroup")
	// public void appIndexTairGroup(HttpServletResponse response, Integer
	// appId) throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// List<TairInfo> list = tairService.findTairGroupConsumer(appName,
	// KeyConstants.TAIR_CONSUMER);
	// Collections.sort(list);
	// Map jsonMap = new HashMap();
	// jsonMap.put("list", list);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
	// @RequestMapping(params="method=appIndexTairHost")
	// public void appIndexTairHost(HttpServletResponse response, Integer appId)
	// throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// String keyname = KeyConstants.TAIR_CONSUMER;
	// List<TairInfo> list = tairService.findTairConsumerRecentlyByHost(appName,
	// keyname);
	// Map jsonMap = new HashMap();
	// jsonMap.put("list", list);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
	// @RequestMapping(params="method=appIndexHSFProviderInterface")
	// public void appIndexHSFProviderInterface(HttpServletResponse response,
	// Integer appId) throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// Map jsonMap = new HashMap();
	// String key = KeyConstants.HSF_PROVIDER;
	// Map<String, HsfInfo> hsfInfoMap =
	// hsfHelp.findHsfChildRecentlyByApp(appName, key);
	// List<Map.Entry<String, HsfInfo>> list = sortMapByValue(hsfInfoMap);
	// List<HsfInfo> listRet = new ArrayList();
	// for(Map.Entry<String, HsfInfo> entry : list){
	// entry.getValue().setMethodName(entry.getKey());
	// listRet.add(entry.getValue());
	// }
	// jsonMap.put("list", listRet);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
	// private List<Map.Entry<String, HsfInfo>> sortMapByValue(Map<String,
	// HsfInfo> map) {
	// if(map == null)return null;
	// List<Map.Entry<String, HsfInfo>> info = new ArrayList<Map.Entry<String,
	// HsfInfo>>(map.entrySet());
	// Collections.sort(info, new Comparator<Map.Entry<String, HsfInfo>>() {
	// public int compare(Map.Entry<String, HsfInfo> obj1, Map.Entry<String,
	// HsfInfo> obj2) {
	// return obj2.getValue().getNum() - obj1.getValue().getNum();
	// }
	// });
	// return info;
	// }
	// @RequestMapping(params="method=appIndexHSFProviderReferApp")
	// public void appIndexHSFProviderReferApp(HttpServletResponse response,
	// Integer appId) throws Exception {
	// String appName = AppInfoCache.getAppInfoById(appId).getAppName();
	// Map jsonMap = new HashMap();
	// String key = KeyConstants.HSF_REFER;
	// Map<String, HsfInfo> hsfInfoMap =
	// hsfHelp.findHsfChildRecentlyByApp(appName, key);
	// List<Map.Entry<String, HsfInfo>> list = sortMapByValue(hsfInfoMap);
	// List<HsfInfo> listRet = new ArrayList();
	// for(Map.Entry<String, HsfInfo> entry : list){
	// entry.getValue().setReferApp(entry.getKey());
	// listRet.add(entry.getValue());
	// }
	// jsonMap.put("list", listRet);
	// writeJSONToResponseJSONObject(response, jsonMap);
	// }
}
