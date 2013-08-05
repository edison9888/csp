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
 * 
 * @author denghaichuan.pt
 * @version 2012-5-8
 */
@Controller
@RequestMapping("/app/detail/uicfinal/consumer/show.do")
public class AppUicfinalTairController {
private static Logger logger = Logger.getLogger(AppUicfinalTairController.class);
	
	@Resource(name="commonService")
	CommonServiceInterface commonService;
	
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.UIC_TAIR_CLIENT, "Tair-Cnt");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("time/uicfinal/consumer/app_uicfinal_tair");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	@RequestMapping(params="method=showHostInfo")
	public ModelAndView showHostInfo(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.UIC_TAIR_CLIENT+Constants.S_SEPERATOR+keyName, "E-times");
		List<String> timeList = getTimeMap();
		ModelAndView view = new ModelAndView("time/uicfinal/consumer/app_uicfinal_tair_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", keyName);
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
