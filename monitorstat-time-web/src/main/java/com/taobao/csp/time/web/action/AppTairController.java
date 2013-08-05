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
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/detail/tair/show.do")
public class AppTairController extends BaseController {
	private static Logger logger = Logger.getLogger(AppTairController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;



	@RequestMapping(params = "method=showIndex")
	public ModelAndView showTairIndex(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/tair/appTair");
		
		List<HostPo> ipList = CspCacheTBHostInfos.get()
		.getHostInfoListByOpsName(appInfo.getAppName());

		view.addObject("ipList", ipList);
		view.addObject("appInfo", appInfo);
		return view;
	}
	/**
	 * 
	 * @param response
	 * @param appId
	 */
	@RequestMapping(params = "method=queryRecentlyHost")
	public void queryRecentlyHost(HttpServletResponse response, int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<TimeDataInfo> list = commonService.querykeyRecentlyDataForHostBySort(appInfo.getAppName(), KeyConstants.TAIR_CONSUMER,PropConstants.E_TIMES);
		
		for (TimeDataInfo hsfInfo : list) {
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(
					hsfInfo.getIp());
			hsfInfo.setSite(host.getHostSite().toUpperCase());
		}
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}
	
	
	@RequestMapping(params = "method=queryOtherInfo")
	public void queryOtherInfo(HttpServletResponse response, Integer appId) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<TimeDataInfo> ipList = commonService
					.querykeyRecentlyDataForHostBySort(appInfo.getAppName(),
							KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES);
			if (ipList.size() > 10)
				ipList = ipList.subList(0, 10);
		
			List<TimeDataInfo> groupList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(),KeyConstants.TAIR_CONSUMER , PropConstants.E_TIMES);
	
			Map map = new HashMap();
			map.put("ipList", ipList);
			map.put("groupList", groupList);
			writeJSONToResponseJSONObject(response, map);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=gotoHost")
	public ModelAndView gotoHost(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/host");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryHost")
	public void appHostConsumer(HttpServletResponse response, Integer appId) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(),KeyConstants.TAIR_CONSUMER , PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params = "method=gotoHostGroup")
	public ModelAndView showIndexHostConsumerGroup(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/host_group");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryHostGroup")
	public void queryHostGroup(HttpServletResponse response,
			Integer appId, String ip) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES, ip);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	
	
	
	@RequestMapping(params = "method=gotoGroup")
	public ModelAndView showTairIndexBase(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Map<String, List<TimeDataInfo>> map = commonService.querykeyDataForChild(appInfo.getAppName(), KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES);

		if (map == null || map.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName()
					+ " 不存在Tair Consumer 信息");
			message.addObject("callbackurl",
					"/app/detail/hsf/consumer/show.do?method=showIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}
		ModelAndView view = new ModelAndView("/time/tair/group");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryGroup")
	public void appConsumer(HttpServletResponse response, Integer appId) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.TAIR_CONSUMER, PropConstants.E_TIMES);
			
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=gotoGroupHost")
	public ModelAndView gotoGroupHost(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/group_host");
		view.addObject("appInfo", appInfo);
		return view;
	}
	@RequestMapping(params = "method=queryGroupHost")
	public void queryGroupHost(HttpServletResponse response, Integer appId,
			String key) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params = "method=gotoNamespace")
	public ModelAndView gotoNamespace(int appId ) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/namespace");
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	@RequestMapping(params = "method=gotoActions")
	public ModelAndView gotoActions(int appId ) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/actions");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryNamespace")
	public void appHostConsumerGroupNameSpace(HttpServletResponse response,
			Integer appId, String key) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params = "method=queryActions")
	public void queryActions(HttpServletResponse response,
			Integer appId, String key) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
	
	@RequestMapping(params = "method=gotoNamespaceHost")
	public ModelAndView showTairIndexNameSpaceHost(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/namespace_host");
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	
	@RequestMapping(params = "method=queryNamespaceHost")
	public void appHostConsumerGroupNameSpaceHost(HttpServletResponse response,
			Integer appId, String key) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	

	
	/**单个主机、单个分组对应的namespace。*/
	@RequestMapping(params = "method=gotoHostGroupNamespace")
	public ModelAndView gotoHostGroupNamespace(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/host_group_namespace");
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	
	/**单个分组、单个主机对应的namespace，和“单个主机、单个分组对应的namespace”相比，它们的导航文案不同。*/
	@RequestMapping(params = "method=gotoGroupHostNamespace")
	public ModelAndView gotoGroupHostNamespace(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/group_host_namespace");
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	
	@RequestMapping(params = "method=querySingleHostNamespace")
	public void querySingleHostNamespace(HttpServletResponse response,
			Integer appId, String key, String ip) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key,PropConstants.E_TIMES, ip);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}

//
//	@RequestMapping(params = "method=appTairConsumerHostGroup")
//	public void appConsumerHostGroup(HttpServletResponse response,
//			Integer appId, String group, String ip) {
//		try {
//			String appNameMain = AppInfoAo.get().findAppInfoById(appId)
//					.getAppName();
//			Map<String, List<TairInfo>> map = tairService
//					.getTairConsumerHostNameSpace(appNameMain, group, ip);
//			map = SortUtil.sortMapInfoListAccordingRecentMinites(map);
//			writeJSONToResponseJSONObject(response, map);
//		} catch (Exception e) {
//			logger.info(e);
//		}
//	}
//
//	@RequestMapping(params = "method=appTairConsumerGroup")
//	public void appConsumerType(HttpServletResponse response, Integer appId,
//			String group) {
//		try {
//			String appNameMain = AppInfoAo.get().findAppInfoById(appId)
//					.getAppName();
//			Map<String, List<TairInfo>> map = tairService
//					.getTairConsumerNameSpace(appNameMain, group);
//			map = SortUtil.sortMapInfoListAccordingRecentMinites(map);
//			writeJSONToResponseJSONObject(response, map);
//		} catch (Exception e) {
//			logger.info(e);
//		}
//	}
//
//	@RequestMapping(params = "method=appTairConsumerGroupHost")
//	public void appConsumerTypeHost(HttpServletResponse response,
//			Integer appId, String group, String namespaceAndMethod) {
//		try {
//			String appNameMain = AppInfoAo.get().findAppInfoById(appId)
//					.getAppName();
//			String[] keys = namespaceAndMethod.split("->");
//			String namespace = keys[0];
//			String method = keys[1];
//			String keyName = KeyConstants.TAIR_CONSUMER + Constants.S_SEPERATOR
//					+ group + Constants.S_SEPERATOR + namespace
//					+ Constants.S_SEPERATOR + method;
//			List<String> list = AppInfoHelp.getIpListByAppName(appNameMain);
//			Map<String, List<TairInfo>> map = tairService.getTairHostConsumer(
//					appNameMain, keyName, list);
//			map = SortUtil.sortMapInfoListAccordingRecentMinites(map);
//			writeJSONToResponseJSONObject(response, map);
//		} catch (Exception e) {
//			logger.info(e);
//		}
//	}
//
//	
//
//	@RequestMapping(params = "method=showIndexHostGroup")
//	public ModelAndView showTairIndexHostNameSpace(int appId, String ip,
//			String group) {
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
//		ModelAndView view = new ModelAndView(
//				"/time/tair/app_tair_consumer_host_group");
//		view.addObject("appInfo", appInfo);
//		view.addObject("ip", ip);
//		view.addObject("group", group);
//		return view;
//	}
//
//	@RequestMapping(params = "method=showIndexGroup")
//	public ModelAndView showTairIndexNameSpace(int appId, String group) {
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
//		ModelAndView view = new ModelAndView(
//				"/time/tair/app_tair_consumer_group");
//		view.addObject("appInfo", appInfo);
//		view.addObject("group", group);
//		return view;
//	}

	
	// @RequestMapping(params="method=showIndexHistory")
	// public void showIndexHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String group){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String keyName = KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+group;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
	// @RequestMapping(params="method=showIndexHostHistory")
	// public void showIndexHostHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String group,String
	// ip){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String keyName =
	// KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+ip;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
	// @RequestMapping(params="method=showIndexHostConsumerHistory")
	// public void showIndexHostConsumerHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String ip){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String keyName = KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+ip;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
	// @RequestMapping(params="method=showIndexHostGroupHistory")
	// public void showIndexHostGroupHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String group,String
	// ip,String namespaceAndMethod){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String namespace = namespaceAndMethod.split("->")[0];
	// String method = namespaceAndMethod.split("->")[1];
	// String keyName =
	// KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+namespace+Constants.S_SEPERATOR+method+Constants.S_SEPERATOR+ip;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
	// @RequestMapping(params="method=showIndexGroupHistory")
	// public void showIndexGroupHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String group,String
	// namespaceAndMethod){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String namespace = namespaceAndMethod.split("->")[0];
	// String method = namespaceAndMethod.split("->")[1];
	// String keyName =
	// KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+namespace+Constants.S_SEPERATOR+method;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
	// @RequestMapping(params="method=showIndexGroupHostHistory")
	// public void showIndexGroupHostHistory(HttpServletRequest
	// request,HttpServletResponse response,Integer appId,String group,String
	// namespaceAndMethod,String ip){
	// String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
	// String namespace = namespaceAndMethod.split("->")[0];
	// String method = namespaceAndMethod.split("->")[1];
	// String keyName =
	// KeyConstants.TAIR_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+namespace+Constants.S_SEPERATOR+method+Constants.S_SEPERATOR+ip;
	// try {
	// response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
	// } catch (IOException e) {
	// logger.info(e);
	// }
	// }
}
