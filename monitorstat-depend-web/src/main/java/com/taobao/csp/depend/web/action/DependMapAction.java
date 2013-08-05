package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.taobao.csp.depend.ao.CspMapKeyInfoAo;
import com.taobao.csp.depend.ao.DependAo;
import com.taobao.csp.depend.ao.HsfConsumerAo;
import com.taobao.csp.depend.ao.HsfProviderAo;
import com.taobao.csp.depend.po.AjaxResult;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.EagleeyeTreeGridData;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.csp.depend.service.DependTreeService;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspMapKeyInfoPo;
import com.taobao.monitor.common.po.ReleaseInfo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;

/**
 * 依赖地图Action
 * @author zhongting.zy
 *
 */
@Controller
@RequestMapping("/show/dependmap.do")
public class DependMapAction extends BaseAction {

	/*依赖地图首页*/
	@RequestMapping(params = "method=index")
	public ModelAndView index(HttpServletResponse response, String appName, String opsName, String selectDate, String selectTabId){

		if("undefined".equals(appName)) {
			appName = "itemcenter";
		}
		if("undefined".equals(opsName)) {
			opsName = "itemcenter";
		}

		if(StringUtil.isBlank(appName)) {
			if(StringUtil.isNotBlank(opsName)) {
				appName = opsName;
			} else {
				appName = "itemcenter";				
			}
		}

		if(StringUtil.isBlank(selectDate)) {
			Date date = MethodUtil.getDate(selectDate);
			selectDate = MethodUtil.getStringOfDate(date);			
		}

		ModelAndView view = new ModelAndView("/depend/dependmap/map_index");
		view.addObject("appName", appName);
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("selectTabId", selectTabId);
		return view;
	}

	/*依赖地图首页-keylist页面*/
	@RequestMapping(params = "method=keyindex")
	public ModelAndView keyindex(HttpServletResponse response, String appName){
		if(StringUtil.isBlank(appName))
			appName = "itemcenter";
		List<CspMapKeyInfoPo> keyList = CspMapKeyInfoAo.get().getNormalKeyList(appName);
		ModelAndView view = new ModelAndView("/depend/dependmap/keylistpage");
		view.addObject("keyList", keyList);
		view.addObject("appName", appName);
		return view;
	}

	@RequestMapping(params = "method=gotoBlackKeyList")
	public ModelAndView gotoBlackKeyList(HttpServletResponse response, String appName){
		if(StringUtil.isBlank(appName))
			appName = "itemcenter";

		List<CspMapKeyInfoPo> keyList = CspMapKeyInfoAo.get().getBlackKeyList(appName);
		ModelAndView view = new ModelAndView("/depend/dependmap/blackkeylistpage");
		view.addObject("keyList", keyList);
		view.addObject("appName", appName);
		return view;
	}

	@RequestMapping(params = "method=gotoMeDependKeyPage")
	public ModelAndView gotoMeDependKeyPage(HttpServletResponse response, String appName, String keyName, String isShowBlackString){
		boolean isShowBlack = false;
		if(StringUtil.isBlank(isShowBlackString)) {
			isShowBlack = Boolean.valueOf(isShowBlackString);
		}

		CspEagleeyeApiRequestDay originContent = EagleeyeDataAo.get().searchRecentlyDayPo(appName, keyName, Constants.API_CHILD_KEY);
		EagleeyeTreeGridData data = DependTreeService.get().getDependTreeBlack(originContent, isShowBlack);
		String dependJsonData = JSONArray.toJSONString(new EagleeyeTreeGridData[]{data});

		ModelAndView view = new ModelAndView("/depend/dependmap/depend_tree_page");
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("dependJsonData",dependJsonData);
		return view;
	}	


	//FIXME
	@RequestMapping(params = "method=gotoDependMeKeyPage")
	public ModelAndView gotoDependMeKeyPage(HttpServletResponse response, String appName, String keyName, String isShowBlackString){
		boolean isShowBlack = false;
		if(StringUtil.isBlank(isShowBlackString)) {
			isShowBlack = Boolean.valueOf(isShowBlackString);
		}

		CspEagleeyeApiRequestDay originContent = EagleeyeDataAo.get().searchRecentlyDayPo(appName, keyName, Constants.API_CHILD_KEY);
		EagleeyeTreeGridData data = DependTreeService.get().getDependTreeBlack(originContent, isShowBlack);
		String dependJsonData = JSONArray.toJSONString(new EagleeyeTreeGridData[]{data});

		ModelAndView view = new ModelAndView("/depend/dependmap/depend_tree_page");
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("dependJsonData",dependJsonData);
		return view;
	}	

	/**
	 * 查看key详情
	 */
	@RequestMapping(params = "method=gotoKeyDetail")
	public ModelAndView gotoKeyDetail(HttpServletResponse response, String appName, String id, String keyName){
		CspMapKeyInfoPo info = null;
		if(StringUtil.isNumeric(id)) {
			try {
				info = CspMapKeyInfoAo.get().getMapKeyPoById(id);
			} catch (Exception e) {
				logger.error("id=" + id, e);
				info = null;
			}			
		}
		if(info == null) {
			try {
				info = CspMapKeyInfoAo.get().getMapKeyPoByKey(appName, keyName);
				if(info.getId() == 0)
					info = null;
			} catch (Exception e) {
				logger.error("appName=" + appName + ";keyName=" + keyName, e);
				info = null;
			}
		}

		//		List<CspMapKeyInfoPo> keyList = CspMapKeyInfoAo.get().getNormalKeyList(appName);
		ModelAndView view = new ModelAndView("/depend/dependmap/keydetail");
		view.addObject("info", info);
		view.addObject("keyName", keyName);
		view.addObject("appName", appName);
		view.addObject("id", id);
		return view;
	}

	@RequestMapping(params = "method=addKeyToBlack")
	public void addKeyToBlack(String id, String userName, HttpServletResponse response) {
		AjaxResult result = CspMapKeyInfoAo.get().changeKeyStatus(id, Constants.CSP_DEPEND_MAP_BLACK, userName);
		try {
			this.writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping(params = "method=removeKeyFromBlack")
	public void removeKeyFromBlack(String id, String userName, HttpServletResponse response) {
		AjaxResult result = CspMapKeyInfoAo.get().changeKeyStatus(id, Constants.CSP_DEPEND_MAP_NOT_BLACK, userName);
		try {
			this.writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/*我依赖的页面首页*/
	@RequestMapping(params = "method=appMeDependIndex")
	public ModelAndView appMeDependIndex(HttpServletResponse response, String appName,String selectDate){

		String dateBegin = selectDate + " 00:00";
		String endDate = selectDate + " 23:59";
		ReleaseInfo releaseInfo = null;
		try {
			releaseInfo = CspDependInfoAo.get().getReleaseInfoLatest(appName, dateBegin, endDate, "publish");
		} catch (Exception e) {
			logger.error("", e);
		}

		final List<AppDepApp> meDependList = DependAo.get().getMeDependList(appName, selectDate, "all");
		final List<AppConsumerSummary> summmaryList = HsfConsumerAo.get().getHsfSummaryInfoListMeDepend(selectDate, appName);

		Map<String, AppConsumerSummary> map = HsfConsumerAo.get().changeListToMap(summmaryList);
		long totalNum = 0;

		for(AppDepApp app : meDependList) {
			AppConsumerSummary summary = map.get(app.getDependOpsName());
			if(summary != null) {
				app.setCallnum(summary.getCallAllNum());
				map.remove(app.getDependOpsName());
				totalNum += summary.getCallAllNum();
			}
		}

		for(AppConsumerSummary summary : map.values()) {
			AppDepApp app = new AppDepApp();
			app.setOpsName(appName);
			app.setDependOpsName(summary.getOpsName());
			app.setDependAppType("hsf");
			app.setCollectTime(summary.getCollectDate());
			app.setSelfAppType("");
			app.setPortInfo(12200);
			app.setCallnum(summary.getCallAllNum());
			app.setExistStatus(ConstantParameters.CONTROST_ADD);
			totalNum += summary.getCallAllNum();
			meDependList.add(app);
		}

		if(totalNum > 0) {
			for(AppDepApp appInfo : meDependList) {
				appInfo.setRate(Arith.div(appInfo.getCallnum(), totalNum, 4));
			}			
		}
		//		DependAo.get().sortAppDepAppList(meDependList);


//		String oldDays = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(MethodUtil.getDate(selectDate), 1));
		Set<String> appSet = new HashSet<String>();
		for(AppDepApp depApp : meDependList) {
			appSet.add(depApp.getDependOpsName());
		}
		Map<String,ReleaseInfo> releaseMap = new HashMap<String,ReleaseInfo>();
		try {
			releaseMap = CspDependInfoAo.get().getReleaseInfoMap(appSet, dateBegin, endDate,"publish");
		} catch (Exception e) {
			logger.error("", e);
		}

		ModelAndView view = new ModelAndView("/depend/dependmap/app_medepend");
		view.addObject("meDependList", meDependList);
		view.addObject("opsName", appName);
		view.addObject("selectDate", selectDate);
		view.addObject("releaseMap", releaseMap);
		view.addObject("releaseInfo", releaseInfo);
		return view;
	}

	/*依赖我的页面首页*/
	@RequestMapping(params = "method=appDependMeIndex")
	public ModelAndView appDependMeIndex(HttpServletResponse response, String appName,String selectDate){
		
		String dateBegin = selectDate + " 00:00";
		String endDate = selectDate + " 23:59";
		ReleaseInfo releaseInfo = null;
		try {
			releaseInfo = CspDependInfoAo.get().getReleaseInfoLatest(appName, dateBegin, endDate, "publish");
		} catch (Exception e) {
			logger.error("", e);
		}
		
		final List<AppDepApp> dependMeList = DependAo.get().showDependMeList(appName, selectDate, "all");
		final List<AppConsumerSummary> summmaryList = HsfConsumerAo.get().getHsfSummaryInfoListDependMe(selectDate, appName);

		Map<String, AppConsumerSummary> map = HsfConsumerAo.get().changeListToMap(summmaryList);

		long totalNum = 0;
		AppProviderSummary providerSummary = HsfProviderAo.get().getHsfSummaryInfo(selectDate, appName);
		if(providerSummary != null) {
			totalNum = providerSummary.getCallAllNum();
		}

		for(AppDepApp app : dependMeList) {
			AppConsumerSummary summary = map.get(app.getOpsName());
			if(summary != null) {
				app.setCallnum(summary.getCallAllNum());
				map.remove(app.getOpsName());
				app.setRate(Arith.div(app.getCallnum(), totalNum, 4));
			}
		}

		for(AppConsumerSummary summary : map.values()) {
			AppDepApp app = new AppDepApp();
			app.setOpsName(summary.getOpsName());
			app.setDependOpsName(appName);
			app.setDependAppType("hsf");
			app.setCollectTime(summary.getCollectDate());
			app.setSelfAppType("");
			app.setPortInfo(12200);
			app.setCallnum(summary.getCallAllNum());
			app.setExistStatus(ConstantParameters.CONTROST_ADD);
			app.setRate(Arith.div(app.getCallnum(), totalNum, 4));
			dependMeList.add(app);
		}
		//		DependAo.get().sortAppDepAppList(meDependList);
		
		Set<String> appSet = new HashSet<String>();
		for(AppDepApp depApp : dependMeList) {
			appSet.add(depApp.getOpsName());
		}
		Map<String,ReleaseInfo> releaseMap = new HashMap<String,ReleaseInfo>();
		try {
			releaseMap = CspDependInfoAo.get().getReleaseInfoMap(appSet, dateBegin, endDate,"publish");
		} catch (Exception e) {
			logger.error("", e);
		}

		ModelAndView view = new ModelAndView("/depend/dependmap/app_dependme");
		view.addObject("dependMeList", dependMeList);
		view.addObject("opsName", appName);
		view.addObject("selectDate", selectDate);
		view.addObject("releaseMap", releaseMap);
		view.addObject("releaseInfo", releaseInfo);		
		return view;
	}
	
	@RequestMapping(params = "method=gotoReleaseListPage")
	public ModelAndView gotoReleaseListPage(HttpServletResponse response, String appName,String dateStart,String dateEnd, String pubLevel){
		if(dateEnd == null)
			dateEnd = dateStart;

		String beginDate = dateStart + " 00:00";
		String endDate = dateEnd + " 23:59";
		List<ReleaseInfo> list = null;
		try {
			if(StringUtils.isBlank(pubLevel))
				list = CspDependInfoAo.get().getReleaseInfoList(appName,beginDate, endDate);
			else
				list = CspDependInfoAo.get().getReleaseInfoList(appName,beginDate, endDate,pubLevel);
		} catch (Exception e) {
			logger.error("", e);
		}
		ModelAndView view = new ModelAndView("/depend/dependmap/releaselist");
		view.addObject("opsName", appName);
		view.addObject("beginDate", beginDate);
		view.addObject("endDate", endDate);
		view.addObject("list", list);		
		return view;
	}

	public static void main(String[] args) {
		//		new DependMapAction().appMeDependIndex(null, "itemcenter", "2012-12-23");
		String ss = "a";
		String ss2 = "b";
		System.out.println(ss.compareTo(ss2));
	}
}
