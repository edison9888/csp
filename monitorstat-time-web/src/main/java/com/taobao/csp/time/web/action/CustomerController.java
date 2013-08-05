package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

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
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;

@Controller
@RequestMapping("/app/detail/customer/show.do")
public class CustomerController extends BaseController{
	private static Logger logger = Logger.getLogger(CustomerController.class);
	
	@Resource(name="commonService")
	CommonServiceInterface commonService;
	
	
	@RequestMapping(params="method=showHistory")
	public void showHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String keyName){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		
		String keyNameFull = KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+keyName; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params="method=showTwoHistory")
	public void showTwoHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String key1Name,String key2Name){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		String keyNameFull = KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.CHONGZHIDE, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/customerapp/customermain");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	} 
	
	@RequestMapping(params="method=showHostKey1Info")
	public ModelAndView showHostKey1Info(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+keyName, "E-times");
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/customerapp/customer_key1_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", keyName);
		
		return view;
	}
	
	
	@RequestMapping(params="method=showKey2AppInfo")
	public ModelAndView showKey2AppInfo(int appId, String key1Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+key1Name, "E-times");
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/customerapp/customer_key2");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		
		return view;
	}
	
	@RequestMapping(params="method=showKey2HostInfo")
	public ModelAndView showKey2HostInfo(int appId, String key1Name, String key2Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/customerapp/customer_key2_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		view.addObject("key2Name", key2Name);
		
		return view;
	}
	
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
	
	/************************通用配置模板********************************************************/
	
//	@RequestMapping(params="method=queryRealTimeBussinessLogData")
//	public void queryRealTimeBussinessLogData(HttpServletRequest request,HttpServletResponse response,Integer appName,
//			String keyName, String parentKeys){
//		
//		String keyNameFull = KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+keyName; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
	
	@RequestMapping(params="method=queryRealTimeBussinessLogData")
	public ModelAndView queryRealTimeBussinessLogData(HttpServletRequest request,HttpServletResponse response,String appName,
			String keyName, String parentKeys, String property){
		ModelAndView view = new ModelAndView("/time/customerapp/bussiness_log");
		if(StringUtil.isBlank(appName)) {
			
		} else {
			AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(appName);
			List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), keyName, property);
			view.addObject("sortEntryList", sortEntryList);
			view.addObject("appInfo", appInfo);
			List<String> properties = KeyAo.get().findKeyPropertyNames(keyName);
			view.addObject("properties", properties);
		}
		List<String> timeList = getTimeMap();
		view.addObject("timeList", timeList);
		view.addObject("keyName", keyName);
		view.addObject("property", property);
		view.addObject("parentKeys", parentKeys);//@分割
		return view;
	} 
	
//	@RequestMapping(params="method=showHostKey1Info")
//	public ModelAndView showHostKey1Info(int appId, String keyName){
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
//		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.CHONGZHIDE+Constants.S_SEPERATOR+keyName, "E-times");
//		List<String> timeList = getTimeMap();
//		
//		ModelAndView view = new ModelAndView("/time/customerapp/customer_key1_host");
//		view.addObject("sortEntryList", sortEntryList);
//		view.addObject("timeList", timeList);
//		view.addObject("appInfo", appInfo);
//		view.addObject("keyName", keyName);
//		
//		return view;
//	}
	
}
