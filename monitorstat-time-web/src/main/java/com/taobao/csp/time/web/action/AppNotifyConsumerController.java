package com.taobao.csp.time.web.action;

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
@Controller
@RequestMapping("/app/detail/notify/consumer/show.do")
public class AppNotifyConsumerController extends BaseController{
	private static Logger logger = Logger.getLogger(AppNotifyConsumerController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
//	@Resource(name="appNotifyService")
//	AppNotifyServiceInterface appNotifyServiceHelp;
	
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);		
		Map<String, List<TimeDataInfo>>  map = commonService.querykeyDataForChild(appInfo.getAppName(),KeyConstants.NOTIFY_CONSUMER , PropConstants.E_TIMES);
		if(map==null||map.size()==0){
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName()
					+ " 不存在Notify Consumer 信息");
			message.addObject("callbackurl", "/app/detail/notify/consumer/show.do?method=showIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}
		ModelAndView view = new ModelAndView("/time/notify/consumer/app_notify_consumer");
		view.addObject("appInfo", appInfo);
		return view;
	} 

	@RequestMapping(params="method=gotoHostGroup")
	public ModelAndView gotoHostGroup(int appId){
		ModelAndView view = new ModelAndView("/time/notify/consumer/app_notify_consumer_host_group");
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=gotoHost")
	public ModelAndView gotoHost(int appId){
		ModelAndView view = new ModelAndView("/time/notify/consumer/app_notify_consumer_host");
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=gotoGroup")
	public ModelAndView gotoGroup(int appId){
		ModelAndView view = new ModelAndView("/time/notify/consumer/app_notify_consumer_group");
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=queryIndex")
	public void queryIndex(HttpServletResponse response,int appId){
		try {
			String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
				
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appName, KeyConstants.NOTIFY_CONSUMER, PropConstants.NOTIFY_C_S);
			
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryHost")
	public void queryHost(HttpServletResponse response,int appId,String key){
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.NOTIFY_C_S);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryHostGroup")
	public void queryHostGroup(HttpServletResponse response,int appId,String key,String ip){
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.NOTIFY_C_S, ip);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryGroup")
	public void queryGroup(HttpServletResponse response,Integer appId,String key){
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.NOTIFY_C_S);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
//	@RequestMapping(params="method=showIndexHistory")
//	public void showIndexHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String group){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.NOTIFY_CONSUMER+Constants.S_SEPERATOR+group; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
//	@RequestMapping(params="method=showIndexGroupHistory")
//	public void showIndexGroupHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String group,String type){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.NOTIFY_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+type; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
//	@RequestMapping(params="method=showIndexHostHistory")
//	public void showIndexHostHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String group,String ip){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.NOTIFY_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+ip; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
//	@RequestMapping(params="method=showIndexHostGroupHistory")
//	public void showIndexHostGroupHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String group,String ip,String type){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.NOTIFY_CONSUMER+Constants.S_SEPERATOR+group+Constants.S_SEPERATOR+type+Constants.S_SEPERATOR+ip; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
}
