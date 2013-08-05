package com.taobao.csp.time.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * tddl 应用端controller
 * @author denghaichuan.pt
 * @version 2012-4-27
 */
@Controller
@RequestMapping("/app/detail/tddl/consumer/show.do")
public class AppTddlConsumerController {
private static Logger logger = Logger.getLogger(AppTddlConsumerController.class);
	
	@Resource(name="commonService")
	CommonServiceInterface commonService;
	
	/**
	 * 进入tddl后首页的显示
	 * @version 2012-4-27
	 * @param appId
	 * @return
	 */
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.TDDL_CONSUMER, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("time/tddl/consumer/app_tddl_consumer");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	/**
	 * 查询db的host级数据
	 * @version 2012-4-27
	 * @param appId
	 * @param keyName
	 * @return
	 */
	@RequestMapping(params="method=showHostDBInfo")
	public ModelAndView showHostDBInfo(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.TDDL_CONSUMER+Constants.S_SEPERATOR+keyName, "E-times");
		List<String> timeList = getTimeMap();
		ModelAndView view = new ModelAndView("/time/tddl/consumer/app_tddl_consumer_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", keyName);
		return view;
	}
	
	@RequestMapping(params="method=showDBSQLInfo")
	public ModelAndView showDBSQLInfo(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.TDDL_CONSUMER+Constants.S_SEPERATOR+keyName, "E-times");
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/tddl/consumer/app_tddl_consumer_sql");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", keyName);
		
		return view;
	}
	
	@RequestMapping(params="method=showSqlHostInfo")
	public ModelAndView showSqlHostInfo(int appId, String key1Name, String key2Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		String key2NamePart;
		if (key2Name.length() > 50) {
			key2NamePart = key2Name.substring(0, 50);
		} else {
			key2NamePart = key2Name;
		}
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.TDDL_CONSUMER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name, "E-times");
		List<String> timeList = getTimeMap();
		ModelAndView view = new ModelAndView("/time/tddl/consumer/app_tddl_consumer_sql_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		view.addObject("key2NamePart", key2NamePart);
		view.addObject("key2Name", key2Name);
		return view;
	}
	
	
	/**
	 * 返回时间列表
	 * @version 2012-4-23
	 * @return
	 */
	private List<String> getTimeMap() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}
		
		return timeList;
	}
}
