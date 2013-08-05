package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * notify server端controller
 * @author denghaichuan.pt
 * @version 2012-4-27
 */
@Controller
@RequestMapping("/app/detail/notify/provider/show.do")
public class AppNotifyProviderController extends BaseController{
	private static Logger logger = Logger.getLogger(AppNotifyProviderController.class);
	
	@Resource(name="commonService")
	CommonServiceInterface commonService;
	
	
	@RequestMapping(params="method=showHistory")
	public void showHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String keyName){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		
		String keyNameFull = KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+keyName; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params="method=showTwoHistory")
	public void showTwoHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String key1Name,String key2Name){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		String keyNameFull = KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params="method=showThreeHistory")
	public void showThreeHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String key1Name,String key2Name, String key3Name){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		String keyNameFull = KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name+Constants.S_SEPERATOR+key3Name; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	@RequestMapping(params="method=showFourHistory")
	public void showFourHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String key1Name,String key2Name, String key3Name, String key4Name){
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		String keyNameFull = KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name+Constants.S_SEPERATOR+key3Name+Constants.S_SEPERATOR+key4Name; 
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory=&appName="+appName+"&keyName="+keyNameFull);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	
	
	/**
	 * notify sever端的首页
	 * @version 2012-4-20
	 * @param appId
	 * @return
	 */
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	} 
	
	@RequestMapping(params="method=showHostKey1Info")
	public ModelAndView showHostKey1Info(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+keyName, "E-times");
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider_key1_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", keyName);
		
		return view;
	}
	
	
	@RequestMapping(params="method=showKey2AppInfo")
	public ModelAndView showKey2AppInfo(int appId, String key1Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name, "E-times");
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider_key2");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		
		return view;
	}
	
	@RequestMapping(params="method=showKey2HostInfo")
	public ModelAndView showKey2HostInfo(int appId, String key1Name, String key2Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider_key2_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		view.addObject("key2Name", key2Name);
		
		return view;
	}
	
	@RequestMapping(params="method=showKey3AppInfo")
	public ModelAndView showKey3AppInfo(int appId, String key1Name, String key2Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider_key3");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		view.addObject("key2Name", key2Name);
		
		return view;
	}
	
	
	@RequestMapping(params="method=showKey3HostInfo")
	public ModelAndView showKey3HostInfo(int appId, String key1Name, String key2Name, String key3Name){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);	
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.NOTIFY_PROVIDER+Constants.S_SEPERATOR+key1Name+Constants.S_SEPERATOR+key2Name+Constants.S_SEPERATOR+key3Name, "E-times");
		
		List<String> timeList = getTimeMap();
		
		ModelAndView view = new ModelAndView("/time/notify/provider/app_notify_provider_key3_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("key1Name", key1Name);
		view.addObject("key2Name", key2Name);
		view.addObject("key3Name", key3Name);
		
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
