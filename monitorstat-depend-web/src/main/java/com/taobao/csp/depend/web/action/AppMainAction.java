package com.taobao.csp.depend.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspAppHsfDependConsumeDao;
import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.dao.CspAppTairConsumeDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.dao.UrlDao;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.csp.depend.po.tair.TairConsumeSummaryPo;
import com.taobao.csp.depend.po.url.RequestUrlSummary;
import com.taobao.csp.depend.po.url.UrlOriginSummary;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
@Controller
@RequestMapping("/main/appmain.do")  
public class AppMainAction extends BaseAction {

	@Resource(name = "cspDependentDao")
	private CspDependentDao cspDependentDao;

	@Resource(name = "cspAppTairConsumeDao")
	private CspAppTairConsumeDao cspAppTairConsumeDao;

	@Resource(name = "cspAppHsfDependProvideDao")
	private CspAppHsfDependProvideDao cspAppHsfDependProvideDao;

	@Resource(name = "cspAppHsfDependConsumeDao")
	private CspAppHsfDependConsumeDao cspAppHsfDependConsumeDao;

	@Resource(name = "urlDao")
	private UrlDao urlDao;  

	@RequestMapping(params = "method=showAppIndexMain")
	public ModelAndView showAppIndexMain(String opsName, String selectDate) {
		Date date = MethodUtil.getDate(selectDate);
		List<AppDepApp> meDependList = cspDependentDao.findAppDepend(opsName, date);
		List<AppDepApp> dependMeList = cspDependentDao.findDependApp(opsName, date);
		selectDate = MethodUtil.getStringOfDate(date);


		Map<String, TairConsumeSummaryPo> tairMap = cspAppTairConsumeDao.findConsumeTairSummary(opsName, selectDate);

		long hsfProviderTotal = 0;
		long hsfConsumerTotal = 0;
		long urlOriginTotal = 0;
		long urlRequestTotal = 0;
		long tairConsumerTotal = 0;

		List<AppProviderSummary> appProviderList = new ArrayList<AppProviderSummary>();
		List<AppProviderSummary> appConsumerList = new ArrayList<AppProviderSummary>();
		List<UrlOriginSummary> originList = new ArrayList<UrlOriginSummary>();
		List<RequestUrlSummary> requestList = new ArrayList<RequestUrlSummary>();
		List<TairConsumeSummaryPo> tairConsumerList = new ArrayList<TairConsumeSummaryPo>();

		Map<String,AppProviderSummary> appProviderMap = new HashMap<String,AppProviderSummary>();

		appConsumerList = cspAppHsfDependConsumeDao.getAppConsumeSummary(opsName,selectDate);
		for(AppProviderSummary summary: appConsumerList) {
			hsfConsumerTotal += summary.getCallAllNum();
		}    
		appConsumerList = MethodUtil.getSortedList(appConsumerList);

		AppInfoPo info = AppInfoAo.get().getAppInfoByAppName(opsName);
		if(info == null)
			info = new AppInfoPo();
		String type = info.getAppType();
		if(type != null)
			type = type.trim();
		else 
			type = "";
		if(type.equals("center")) { //后端 center
			//summary 表信息不全，暂时查询detail表
			List<AppConsumerSummary> tmpList = cspAppHsfDependProvideDao.sumCenterAppHsfToAppConsumer(opsName, selectDate);
			for(AppConsumerSummary summary: tmpList) {
				AppProviderSummary po = appProviderMap.get(summary.getOpsName());
				if(po == null) {
					po = new AppProviderSummary();
					po.setOpsName(opsName);
					appProviderMap.put(opsName, po);
				}
				po.setCallAllNum(summary.getCallAllNum() + po.getCallAllNum());
				hsfProviderTotal += summary.getCallAllNum();
			}
			//合并两个机房
			appProviderList = new ArrayList<AppProviderSummary>(appProviderMap.values());
			//appProviderList = cspAppHsfDependProvideDao.getAppHistoryCall(opsName, null, selectDate, selectDate);
			//      for(AppProviderSummary summary: appProviderList) {
			//        hsfProviderTotal += summary.getCallAllNum();
			//        hsfProviderTotal += summary.getCallAllNum();
			//      }
			appProviderList = MethodUtil.getSortedList(appProviderList);
		} else {  
			originList = urlDao.findOriginList(opsName, selectDate);
			requestList = urlDao.findRequestList(opsName, selectDate);
			Map<String, UrlOriginSummary> originMap = new HashMap<String, UrlOriginSummary>();
			for(UrlOriginSummary origin: originList) {
				if(origin.getOriginUrl() != null) { 
					urlOriginTotal += origin.getOriginUrlNum();
					if(originMap.containsKey(origin.getOriginUrl())) {
						origin.setOriginUrlNum(origin.getOriginUrlNum() + originMap.get(origin.getOriginUrl()).getOriginUrlNum());
					} 
					originMap.put(origin.getOriginUrl(), origin);
				}
			}
			Map<String, RequestUrlSummary> requestMap = new HashMap<String, RequestUrlSummary>();      
			for(RequestUrlSummary request: requestList) {
				if(request.getRequestUrl() != null) {
					urlRequestTotal += request.getRequestNum();
					if(requestMap.containsKey(request.getRequestUrl())) {
						request.setRequestNum(request.getRequestNum() + requestMap.get(request.getRequestUrl()).getRequestNum());
					} 
					requestMap.put(request.getRequestUrl(), request);
				}
			}

			originList = MethodUtil.getSortedList(originMap.values());
			requestList = MethodUtil.getSortedList(requestMap.values());
		}

		tairConsumerList = MethodUtil.getSortedList(tairMap.values());  //排序
		for(TairConsumeSummaryPo po: tairMap.values()) {
			tairConsumerTotal += po.getInvoking_all_num();
		}

		//处理端口
		Set<String> meDependSet = new HashSet<String>(); 
		Set<String> dependMeSet = new HashSet<String>();

		for(AppDepApp po: meDependList) {
			meDependSet.add(po.getPortInfo() + "");
		}
		for(AppDepApp po: dependMeList) {
			dependMeSet.add(po.getPortInfo() + "");
		}

		//    //测试用
		//    meDependSet.add("1230");
		//    meDependSet.add("1231");
		//    meDependSet.add("1232");
		//    
		//    dependMeSet.add("1230");
		//    dependMeSet.add("12344");
		//    dependMeSet.add("12305");

		ModelAndView view = new ModelAndView("/depend/appinfo/appindex");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", MethodUtil.getStringOfDate(date));
		view.addObject("meDependSet", meDependSet);
		view.addObject("dependMeSet", dependMeSet);

		view.addObject("hsfProviderTotal", hsfProviderTotal + "");
		view.addObject("hsfConsumerTotal", hsfConsumerTotal + "");
		view.addObject("appProviderList", appProviderList);
		view.addObject("appConsumerList", appConsumerList);
		view.addObject("tairConsumerList", tairConsumerList);

		view.addObject("originList", originList);
		view.addObject("requestList", requestList);
		view.addObject("urlOriginTotal", urlOriginTotal + "");
		view.addObject("urlRequestTotal", urlRequestTotal + "");
		view.addObject("tairConsumerTotal", tairConsumerTotal + "");

		return view;
	}
}
