package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

@Controller
@RequestMapping("/app/detail/hsf/consumer/show.do")
public class AppHsfConsumerController extends BaseController {
	private Logger logger = Logger.getLogger(AppHsfConsumerController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	/** HSF-Consumer 机房比例 
	 * @return */
	@RequestMapping(params = "method=gotoAreaRateDetail")
	public ModelAndView queryAreaRateDetail(HttpServletRequest request, int appId,
			String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		TimeDataInfo po = commonService.querySingleRecentlyKeyData(appInfo
				.getAppName(), key, PropConstants.E_TIMES);
		Map<String, Object> map = po.getPropertyMap();
		List<Entry<String,Object>> list = new ArrayList<Entry<String,Object>>();
		list.addAll(map.entrySet());
		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/area_rate_detail");
		request.setAttribute("list", list);
		view.addObject("appInfo", appInfo);
		return view;
	}

	/** HSF-Consumer 机房比例 */
	@RequestMapping(params = "method=queryAreaRate")
	public void queryConsumerAreaRate(HttpServletResponse response, int appId,
			String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<String> l1Keys = commonService.childKeyList(appInfo.getAppName(),
				KeyConstants.HSF_CONSUMER_RATE);
		List<Entry<String, List<TimeDataInfo>>> list = new ArrayList<Entry<String, List<TimeDataInfo>>>();
		Map<String, List<TimeDataInfo>> map = new HashMap<String, List<TimeDataInfo>>();
		// level 1 key
		for (String l1Key : l1Keys) {
			List l = commonService.querykeyRecentlyDataForChildBySort(appInfo
					.getAppName(), l1Key, PropConstants.E_TIMES);
			map.put(l1Key.substring(l1Key.indexOf(Constants.S_SEPERATOR) + 1),
					l);
		}
		list.addAll(map.entrySet());

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfConsumerClassHost")
	public ModelAndView gotohsfConsumerClassHost(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/hsf_consumer_class_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfConsumerClassHost")
	public void queryhsfConsumerClassHost(HttpServletResponse response,
			int appId, String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForHostBySort(appInfo.getAppName(), key,
						PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfConsumerMethodHost")
	public ModelAndView gotohsfConsumerMethodHost(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/hsf_consumer_method_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfConsumerMethodHost")
	public void queryhsfConsumerMethodHost(HttpServletResponse response,
			int appId, String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForHostBySort(appInfo.getAppName(), key,
						PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfConsumerClass")
	public ModelAndView Class(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/hsf_consumer_class");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfConsumerClass")
	public void queryhsfConsumerClass(HttpServletResponse response, int appId,
			String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForChildBySort(appInfo.getAppName(), key,
						PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfConsumerMethod")
	public ModelAndView gotohsfConsumerMethod(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/hsf_consumer_method");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfConsumerMethod")
	public void queryhsfConsumerMethod(HttpServletResponse response, int appId,
			String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForChildBySort(appInfo.getAppName(), key,
						PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfConsumerHost")
	public ModelAndView gotohsfConsumerHost(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView(
				"time/hsf/consumer/hsf_consumer_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfConsumerHost")
	public void queryhsfConsumerHost(HttpServletResponse response, int appId,
			String key) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForHostBySort(appInfo.getAppName(), key,
						PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	// findHsfInfosByHost
	@RequestMapping(params = "method=gotohsfConsumer")
	public ModelAndView gotohsfConsumer(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.HSF_CONSUMER, PropConstants.E_TIMES);
		if (list == null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName()
					+ " 不存在 HSF-我依赖的 信息");
			message
					.addObject("callbackurl",
							"/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}
		ModelAndView view = new ModelAndView("time/hsf/consumer/hsf_consumer");

		view.addObject("appInfo", appInfo);
		return view;

	}

	/**
	 */
	@RequestMapping(params = "method=queryhsfConsumer")
	public void queryhsfConsumer(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService
				.querykeyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.HSF_CONSUMER, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	
}
