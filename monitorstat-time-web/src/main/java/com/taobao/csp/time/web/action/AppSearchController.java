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
@RequestMapping("/app/detail/search/show.do")
public class AppSearchController extends BaseController{
	private static Logger logger = Logger.getLogger(AppSearchController.class);
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
//	@Resource(name="appSearchService")
//	AppSearchServiceInterface appSearchServiceHelp;
	@RequestMapping(params="method=showIndex")
	public ModelAndView showHsfConsumerIndex(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);		
		Map<String, List<TimeDataInfo>> map= commonService.querykeyDataForChild(appInfo.getAppName(), KeyConstants.SEARCH_CONSUMER, PropConstants.E_TIMES);
		
		if(map==null||map.size()==0){
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName()
					+ " 不存在Search Consumer 信息");
			message.addObject("callbackurl", "/app/detail/search/show.do?method=showIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}
		ModelAndView view = new ModelAndView("/time/search/app_search");
		view.addObject("appInfo", appInfo);
		return view;
	} 

	@RequestMapping(params="method=showIndexHost")
	public ModelAndView showIndexHost(int appId){
		ModelAndView view = new ModelAndView("/time/search/app_search_host");
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		view.addObject("appInfo",appInfo);
		return view;
	}
	
	@RequestMapping(params="method=searchHost")
	public void searchHost(HttpServletResponse response,Integer appId,String key){
		try {
			String appName= AppInfoAo.get().findAppInfoById(appId).getAppName();
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appName, key, PropConstants.E_TIMES);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=search")
	public void appConsumer(HttpServletResponse response,Integer appId) {
		try {
			String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appName, KeyConstants.SEARCH_CONSUMER, PropConstants.E_TIMES);
			//Map<String,List<SearchInfo>>  map = appSearchServiceHelp.getAppSearchInfo(appName);
			//map = SortUtil.sortMapInfoListAccordingRecentMinites(map);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
//	@RequestMapping(params="method=showIndexHistory")
//	public void showIndexHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String url){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.HSF_CONSUMER+Constants.S_SEPERATOR+url; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistory&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
//	@RequestMapping(params="method=showIndexHostHistory")
//	public void showIndexHostHistory(HttpServletRequest request,HttpServletResponse response,Integer appId,String url,String ip){
//		String appNameMain = AppInfoAo.get().findAppInfoById(appId).getAppName();
//		String keyName = KeyConstants.HSF_CONSUMER+Constants.S_SEPERATOR+url+Constants.S_SEPERATOR+ip; 
//		try {
//			response.sendRedirect(request.getContextPath()+"/app/detail/history.do?method=showHistoryHost&appName="+appNameMain+"&keyName="+keyName);
//		} catch (IOException e) {
//			logger.info(e);
//		}
//	}
}
