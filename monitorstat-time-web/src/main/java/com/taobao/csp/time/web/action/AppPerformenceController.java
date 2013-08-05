package com.taobao.csp.time.web.action;

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

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/detail/perf/show.do")
public class AppPerformenceController extends BaseController {

	private static Logger log = Logger.getLogger(AppPerformenceController.class);
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
//	@Resource(name="appPerformenceService")
//	AppPerformenceServiceInterface appPerformenceHelp;
	
	
	@RequestMapping(params="method=showIndex")
	public ModelAndView showPerfIndex(int appId, HttpServletRequest request){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/perf/appPerformence");
		
		
		
		List<HostPo> ipList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getAppName());
		view.addObject("ipList", ipList);
		view.addObject("appInfo", appInfo);
		return view;
	} 


	@RequestMapping(params="method=ipDetail")
	public void performenceInfo(HttpServletResponse response, Integer appId) throws Exception {
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		
		List<TimeDataInfo> list = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.TOPINFO, PropConstants.CPU);
		for (TimeDataInfo po : list) {
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(
					po.getIp());
			String site = host.getHostSite().toUpperCase();
			po.setSite(site);
		}
		writeJSONToResponseJSONArray(response, list);
	}

	@RequestMapping(params="method=top10")
	public void timeInfo(HttpServletResponse response, Integer appId) throws Exception {
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		Map jsonMap = new HashMap();
		List<TimeDataInfo> cpu = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.TOPINFO, PropConstants.CPU);
		if (cpu.size() > 10) {
			cpu = cpu.subList(0, 10);
		}
		List<TimeDataInfo> swap = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.TOPINFO, PropConstants.SWAP);
		if (swap.size() > 10) {
			swap = swap.subList(0, 10);
		}
		List<TimeDataInfo> load = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.TOPINFO, PropConstants.LOAD);
		if (load.size() > 10) {
			load = load.subList(0, 10);
		}
		List<TimeDataInfo> gc = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.JVMINFO, PropConstants.JVMGC);
		if (gc.size() > 10) {
			gc = gc.subList(0, 10);
		}
		List<TimeDataInfo> fullgc = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.JVMINFO, PropConstants.JVMFULLGC);
		if (fullgc.size() > 10) {
			fullgc =fullgc.subList(0, 10);
		}
		List<TimeDataInfo> memory = commonService.querykeyRecentlyDataForHostBySort(appName, KeyConstants.JVMINFO, PropConstants.JVMMEMORY);
		if (memory.size() > 10) {
			memory = memory.subList(0, 10);
		}
	
		
		jsonMap.put("cpu",cpu);
		jsonMap.put("swap",swap);
		jsonMap.put("load",load);
		jsonMap.put("fullgc",fullgc);
		jsonMap.put("gc",gc);
		jsonMap.put("memory",memory);
		writeJSONToResponseJSONObject(response, jsonMap);
	}
	
	@RequestMapping(params="method=gotoDetail")
	public ModelAndView gotoDetail(int appId){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/perf/detail");
		view.addObject("appInfo", appInfo);
		return view;
	} 
	
	@RequestMapping(params="method=queryDetail")
	public void queryDetail(HttpServletResponse response, Integer appId, String key, String mainProp) throws Exception {
		String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appName, key, mainProp);
		writeJSONToResponseJSONArray(response, list);
	}
	
//	@RequestMapping(params="method=showIndexIpPerformence")
//	public ModelAndView showIndexIpPerformence(int appId){
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
//		ModelAndView view = new ModelAndView("/time/perf/appIpPerformence");
//		view.addObject("appInfo", appInfo);
//		return view;
//	} 
//	@RequestMapping(params="method=ipPerformence")
//	public void ipPerformence(HttpServletResponse response,Integer appId,String ip){
//		try {
//			String appName = AppInfoAo.get().findAppInfoById(appId).getAppName();
//			
//			
//			Map<String,List<PerformenceInfo>> map = appPerformenceHelp.getPerformencePerIp(appName, ip);
//			Map jsonMap = map;
//			writeJSONToResponseJSONObject(response, jsonMap);
//		} catch (IOException e) {
//			log.info(e);
//		}
//	}
}
