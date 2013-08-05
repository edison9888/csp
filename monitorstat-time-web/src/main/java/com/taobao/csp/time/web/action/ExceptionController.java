/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.taobao.csp.time.util.MonitorAppUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.ExceptionReportPo;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;

/**
 * @author xiaodu
 * 
 *         上午11:40:49
 */
@Controller
@RequestMapping("/app/detail/exception/show.do")
public class ExceptionController extends BaseController {

	private static Logger logger = Logger.getLogger(ExceptionController.class);
	public static String ZK_MONITOR_APP_ROOT = "/csp/monitor/app";
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	@RequestMapping(params = "method=exceptionReport")
	public ModelAndView exceptionReport(HttpServletRequest request,
			HttpServletResponse response,String appName) {
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByAppName(appName);
		
		List<CspKeyInfo> childKeys = KeyAo.get().findKeyChildByApp(appInfo.getAppId(), KeyConstants.EXCEPTION);
		
		List<ExceptionReportPo> reportPoList = new ArrayList<ExceptionReportPo>();
		for (CspKeyInfo  key : childKeys) {
		
			List<TimeDataInfo> list = commonService.queryKeyDataHistory(
					appName, key.getKeyName(), PropConstants.E_TIMES,
					new Date());
			int total = 0;
			int max = 0;
			int average = 0;

			String maxPointTime = null;
			for (TimeDataInfo item : list) {
				int per = new Double(item.getMainValue()).intValue();
				total += per;
				if (per > max) {
					max = per;
					maxPointTime = item.getFtime();
				}
			}
			if (total != 0 && list.size() != 0)
				average = total / list.size();
			// 以应用为key，保存异常统计信息的Map
			ExceptionReportPo po = new ExceptionReportPo();
			po.setKeyName(key.getKeyName());
			po.setAverage(average);
			po.setMax(max);
			po.setTotal(total);
			po.setMaxPointTime(maxPointTime);
			reportPoList.add(po);
		}
		
		//按照某列排序
		Collections.sort(reportPoList, new Comparator<ExceptionReportPo>() {
			@Override
			public int compare(ExceptionReportPo o1, ExceptionReportPo o2) {
				return  o2.getTotal() -o1.getTotal();
			}
		});
		
		ModelAndView view = new ModelAndView("/time/report/exception");
		view.addObject("reportPoList", reportPoList);
		view.addObject("appInfo", appInfo);
		return view;
	}
	/**
	 * 
	 */
	@RequestMapping(params = "method=exceptionAppReport")
	public ModelAndView exceptionAppReport(HttpServletRequest request,
			HttpServletResponse response) {
		List<String> monitorlist = null;
		try {
			monitorlist = MonitorAppUtil.getMonitorApps();
		} catch (Exception e) {
			logger.error("获取监控应用失败", e);
		}
		List<ExceptionReportPo> reportPoList = new ArrayList<ExceptionReportPo>();

		for (String appName : monitorlist) {
			List<TimeDataInfo> list = commonService.queryKeyDataHistory(
					appName, KeyConstants.EXCEPTION, PropConstants.E_TIMES,
					new Date());
			int total = 0;
			int max = 0;
			int average = 0;

			String maxPointTime = null;
			for (TimeDataInfo item : list) {
				int per = new Double(item.getMainValue()).intValue();
				total += per;
				if (per > max) {
					max = per;
					maxPointTime = item.getFtime();
				}
			}
			if (total != 0 && list.size() != 0)
				average = total / list.size();
			// 以应用为key，保存异常统计信息的Map
			ExceptionReportPo po = new ExceptionReportPo();
			po.setAppName(appName);
			po.setAverage(average);
			po.setMax(max);
			po.setTotal(total);
			po.setMaxPointTime(maxPointTime);
			reportPoList.add(po);

		}
		
		//按照某列排序
		Collections.sort(reportPoList, new Comparator<ExceptionReportPo>() {
			@Override
			public int compare(ExceptionReportPo o1, ExceptionReportPo o2) {
				return  o2.getTotal() -o1.getTotal();
			}
		});
		
		ModelAndView view = new ModelAndView("/time/report/exception_app");
		view.addObject("reportPoList", reportPoList);
		return view;
	}

	/**
	 * 
	 */
	@RequestMapping(params = "method=showIndexHistory")
	public void showIndexHistory(HttpServletRequest request,
			HttpServletResponse response, Integer appId, String exception) {
		String appNameMain = AppInfoAo.get().findAppInfoById(appId)
				.getAppName();
		String keyName = KeyConstants.HSF_CONSUMER + Constants.S_SEPERATOR
				+ exception;
		try {
			response.sendRedirect(request.getContextPath()
					+ "/app/detail/history.do?method=showHistory&appName="
					+ appNameMain + "&keyName=" + keyName);
		} catch (IOException e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=showIndexHostHistory")
	public void showIndexHostHistory(HttpServletRequest request,
			HttpServletResponse response, Integer appId, String exception,
			String ip) {
		String appNameMain = AppInfoAo.get().findAppInfoById(appId)
				.getAppName();
		String keyName = KeyConstants.HSF_CONSUMER + Constants.S_SEPERATOR
				+ exception + Constants.S_SEPERATOR + ip;
		try {
			response.sendRedirect(request.getContextPath()
					+ "/app/detail/history.do?method=showHistoryHost&appName="
					+ appNameMain + "&keyName=" + keyName);
		} catch (IOException e) {
			logger.info(e);
		}
	}

	@RequestMapping(params = "method=showIndex")
	public ModelAndView gotoExceptionIndex(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		List<SortEntry<TimeDataInfo>> sortEntryList = commonService
				.querykeyDataForChildBySort(appInfo.getAppName(),
						KeyConstants.EXCEPTION, PropConstants.E_TIMES);

		ModelAndView view = new ModelAndView("/time/exception/exception_index");

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}

		view.addObject("sortEntryList", sortEntryList);
		view.addObject("appInfo", appInfo);
		view.addObject("timeList", timeList);
		return view;

	}

	@RequestMapping(params = "method=gotoHostException")
	public ModelAndView gotoHostException(int appId, String exception) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/exception/exception_host");

		List<SortEntry<TimeDataInfo>> sortEntryList = commonService
				.querykeyDataForHostBySort(appInfo.getAppName(), exception,
						PropConstants.E_TIMES);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}

		view.addObject("sortEntryList", sortEntryList);
		view.addObject("appInfo", appInfo);
		view.addObject("timeList", timeList);

		view.addObject("appInfo", appInfo);
		view.addObject("exception", exception);
		return view;
	}

	@RequestMapping(params = "method=getRecentlyExceptionTop")
	public void getRecentExceptionTop(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
		List<String> apps = new ArrayList<String>();
		for (AppInfoPo p : list) {
			String an = p.getAppName();
			apps.add(an);
		}

		Map<String, List<TimeDataInfo>> map = commonService
				.querykeyDataForApps(apps, KeyConstants.EXCEPTION,
						PropConstants.E_TIMES);

		Map<String, List<TimeDataInfo>> timeMap = new HashMap<String, List<TimeDataInfo>>();

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			for (TimeDataInfo e : entry.getValue()) {
				List<TimeDataInfo> appList = timeMap.get(e.getFtime());
				if (appList == null) {
					appList = new ArrayList<TimeDataInfo>();
					timeMap.put(e.getFtime(), appList);
				}

				AppInfoPo po = AppInfoCache.getAppInfoByAppName(e.getAppName());
				if (po != null) {
					e.setAppId(po.getAppId());
				}

				appList.add(e);
			}
		}

		List<String> timeList = new ArrayList<String>();

		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 6; i++) {
			timeList.add(TimeUtil.formatTime(cal.getTime(), "HH:mm"));
			cal.add(Calendar.MINUTE, -1);
		}

		Map<String, List<TimeDataInfo>> topMap = new HashMap<String, List<TimeDataInfo>>();

		for (String time : timeList) {
			List<TimeDataInfo> appList = timeMap.get(time);
			if (appList != null) {
				Collections.sort(appList, new Comparator<TimeDataInfo>() {
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

				if (appList.size() > 10) {
					topMap.put(time, appList.subList(0, 10));
				} else {
					topMap.put(time, appList);
				}
			}
		}

		Map jsonMap = new HashMap();
		jsonMap.put("topExceptionMap", topMap);
		jsonMap.put("recentlyTime", timeList);
		writeJSONToResponseJSONObject(response, jsonMap);

	}
	
	
	@RequestMapping(params = "method=showAppException")
	public ModelAndView showAppException(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
		List<String> apps = new ArrayList<String>();
		for (AppInfoPo p : list) {
			String an = p.getAppName();
			apps.add(an);
		}

		Map<String, List<TimeDataInfo>> map = commonService
				.querykeyDataForApps(apps, KeyConstants.EXCEPTION,
						PropConstants.E_TIMES);

		Map<String, List<TimeDataInfo>> timeMap = new HashMap<String, List<TimeDataInfo>>();

		for (Map.Entry<String, List<TimeDataInfo>> entry : map.entrySet()) {
			for (TimeDataInfo e : entry.getValue()) {
				List<TimeDataInfo> appList = timeMap.get(e.getFtime());
				if (appList == null) {
					appList = new ArrayList<TimeDataInfo>();
					timeMap.put(e.getFtime(), appList);
				}

				AppInfoPo po = AppInfoCache.getAppInfoByAppName(e.getAppName());
				if (po != null) {
					e.setAppId(po.getAppId());
				}

				appList.add(e);
			}
		}

		List<String> timeList = new ArrayList<String>();

		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 10; i++) {
			timeList.add(TimeUtil.formatTime(cal.getTime(), "HH:mm"));
			cal.add(Calendar.MINUTE, -1);
		}

		Map<String, List<TimeDataInfo>> topMap = new HashMap<String, List<TimeDataInfo>>();

		for (String time : timeList) {
			List<TimeDataInfo> appList = timeMap.get(time);
			if (appList != null) {
				Collections.sort(appList, new Comparator<TimeDataInfo>() {
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
				topMap.put(time, appList);
			}
		}

		ModelAndView view = new ModelAndView("/time/report/allException");
		view.addObject("topExceptionMap", topMap);
		view.addObject("recentlyTime", timeList);
		return view;
	}
	
	
	
	@RequestMapping(params = "method=desc")
	public ModelAndView showExceptionDesc(String appName,String keyName,String ip){
		List<TimeDataInfo> list = commonService.querySingleKeyData(appName, keyName, PropConstants.E_TIMES);
		ModelAndView view = new ModelAndView("/time/exception/exceptoin_desc");
		view.addObject("exceptionList", list);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("ip", ip);
		return view;
	}

}
