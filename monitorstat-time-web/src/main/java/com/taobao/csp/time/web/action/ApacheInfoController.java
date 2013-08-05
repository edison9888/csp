/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 * 
 *         下午1:59:00
 */
@Controller
@RequestMapping("/app/detail/apache/show.do")
public class ApacheInfoController extends BaseController {

	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	/**
	 * 查询应用pv流量信息
	 *@author xiaodu
	 * @param appName
	 *TODO
	 */
	@RequestMapping(params = "method=queryPvHost")
	public ModelAndView queryPvHost(String appName){
		
		AppInfoPo appInfo =AppInfoCache.getAppInfoByAppName(appName);
		
		List<SortEntry<TimeDataInfo>> sortEntryList = commonService.querykeyDataForHostBySort(appName, KeyConstants.PV, PropConstants.E_TIMES);
		ModelAndView view = new ModelAndView("/time/apache/pv_host");
		
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
	
	
	
	
	
	

	
	/**
	 */
	@RequestMapping(params = "method=queryHostDetail")
	public void queryHostDetail(HttpServletResponse response, int appId,String ip) {
		Map<String, List<TimeDataInfo>> map = new HashMap<String, List<TimeDataInfo>>();
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<TimeDataInfo>  referList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REFER, PropConstants.E_TIMES, ip);
		List<TimeDataInfo>  sourceList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES, ip);
		
		ratePv( referList);
		ratePv( sourceList);
		
		if (sourceList.size() > 10) {
			sourceList = sourceList.subList(0, 10);
		}
		if (referList.size() > 10) {
			referList = referList.subList(0, 10);
		}
		Map<String, String> urlMap = getReferUrl();
		//根据PV-refer url获取refer app
		for(TimeDataInfo po : referList){
			po.setReferAppName(urlMap.get(po.getKeyName()));
		}
		
		map.put("source", sourceList);
		map.put("refer", referList);
		try {
			writeJSONToResponseJSONObject(response, map);
		} catch (IOException e) {
		}

	}
	/**
	 * * 单机详情 - 页面
	 * http://localhost:8100/monitorstat-time-web/app/detail/apache/show.do?method=gotoIPDetail&appId=1&ip=172.23.172.121
	 */
	@RequestMapping(params = "method=gotoHostDetail")
	public ModelAndView gotoHostDetail(HttpServletResponse response, int appId,
			String ip) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/apache/ip_detail");
		view.addObject("appInfo", appInfo);
		view.addObject("ip", ip);
		return view;
	}
	
	
	
	
	
	/**
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(params = "method=queryReferHostDetail")
	public void queryReferHostDetail(HttpServletResponse response, int appId,
			String  key) throws UnsupportedEncodingException {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		//log.debug("url: " + url);
		List<SortEntry<TimeDataInfo>> l = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, l);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotoReferHostDetail")
	public ModelAndView gotoReferHostDetail(int appId, String key)
			throws UnsupportedEncodingException {
		log.debug(" key: " +  key);
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/apache/refer_host_detail");

		view.addObject("appInfo", appInfo);
	//	log.debug("解码后：" + URLDecoder.decode(url, "utf-8"));
		view.addObject("key",  key);
		return view;

	}

	@RequestMapping(params = "method=gotoSourceHostDetail")
	public ModelAndView gotoSourceHostDetail(int appId, String key)
			throws UnsupportedEncodingException {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/apache/source_host_detail");

		view.addObject("appInfo", appInfo);
		view.addObject("key", key);
		return view;

	}

	/**
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(params = "method=querySourceHostDetail")
	public void querySourceHostDetail(HttpServletResponse response, int appId,
			String key) throws UnsupportedEncodingException {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> l = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
//		Map<String, List<ApacheInfo>> map = apacheInfoService
//				.queryCacheAppSourceHostDetails(appInfo.getAppName(), url);
		//List<Entry<String, List<ApacheInfo>>> l = SortUtil.sortByCount(map);
		try {
			writeJSONToResponseJSONArray(response, l);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotoSourceDetail")
	public ModelAndView gotoSourceDetail(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/apache/source_detail");

		view.addObject("appInfo", appInfo);
		return view;

	}

	/**
	 */
	@RequestMapping(params = "method=querySourceDetail")
	public void querySourceDetail(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<AppUrlRelation> urllist = AppInfoAo.get().findAllAppUrlRelation(appInfo.getAppName());
		List<String> keys = new ArrayList<String>();
		for(AppUrlRelation url:urllist){
			keys.add(KeyConstants.PV+"`"+url.getAppUrl());
		}
		List<SortEntry<TimeDataInfo>>  list = commonService.queryMutilKeyDataBySort(appInfo.getAppName(),keys, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}

	/**
	 */
	@RequestMapping(params = "method=gotoReferDetail")
	public ModelAndView gotoReferDetail(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REFER, PropConstants.E_TIMES);
		
		
//		Map<String, List<ApacheInfo>> map = apacheInfoService
//		.queryCacheAppRefer(appInfo.getAppName());
		
		
		if (list ==null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName() + " 不存在 被调用URL-详细 ");
			message.addObject("callbackurl", "/app/detail/apache/show.do?method=gotoReferDetail");
			message.addObject("appInfo", appInfo);
			return message;
		}
		
		ModelAndView view = new ModelAndView("/time/apache/refer_detail");
		view.addObject("appInfo", appInfo);
		return view;
	}

	/**
	 */
	@RequestMapping(params = "method=queryReferDetail")
	public void queryReferDetail(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>>  list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REFER, PropConstants.E_TIMES);
		Map<String, String> urlMap = getReferUrl();
		//根据PV-refer url获取refer app
		for(SortEntry<TimeDataInfo> po : list){
			po.setReferAppName( urlMap.get(po.getKeyName()));
		}
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=queryNetworkDetail")
	public void queryNetworkDetail(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_NETWORK, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}

	@RequestMapping(params = "method=gotoNetworkDetail")
	public ModelAndView gotoNetworkDetail(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/apache/network_detail");

		view.addObject("appInfo", appInfo);
		return view;

	}

	@RequestMapping(params = "method=queryRegionDetail")
	public void queryRegionDetail(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REGION, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}

	@RequestMapping(params = "method=gotoRegionDetail")
	public ModelAndView gotoRegionDetail(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/apache/region_detail");

		view.addObject("appInfo", appInfo);
		return view;

	}

	


	/**
	 * 
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView gotoApacheIndex(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<TimeDataInfo>  list = commonService.querySingleKeyData(appInfo.getAppName(), KeyConstants.PV,PropConstants.E_TIMES);
		
	
		if (list == null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName() + " 不存在web 流量信息");
			message.addObject("callbackurl", "/app/detail/apache/show.do?method=showIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}
		
		
		

		List<HostPo> ipList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getAppName());

		ModelAndView view = new ModelAndView("/time/apache/apache_index");

		view.addObject("appInfo", appInfo);
		view.addObject("ipList", ipList);
		view.addObject("dataList", list);
		return view;
	}

	@RequestMapping(params = "method=showIPPv")
	public void showIpPv(HttpServletResponse response, Integer appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<TimeDataInfo> poList =  commonService.querykeyRecentlyDataForHostBySort(appInfo.getAppName(),KeyConstants.PV , PropConstants.E_TIMES);
		
	//	Map<String, ApacheInfo> ipApacheMap = apacheInfoService	.queryCacheRecentlyHostPv(appInfo.getOpsName(), ipList);
		for (TimeDataInfo po : poList) {
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(
					po.getIp());
			String site = host.getHostSite().toUpperCase();
			po.setSite(site);
			//apacheInfoList.add(apacheInfo);
		}
	//	sortBypv(apacheInfoList);
		try {
			writeJSONToResponseJSONArray(response,poList);
		} catch (IOException e) {
		}
	}

	/**
	 * 展示应用的来源URL 和refer URL ，访问top10区域和网络供应top10
	 * 
	 * @param response
	 * @param appId
	 */
	@RequestMapping(params = "method=showotherInfo")
	public void showotherInfo(HttpServletResponse response, Integer appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<TimeDataInfo> referList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REFER, PropConstants.E_TIMES);
		List<TimeDataInfo> sourceList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES);
		List<TimeDataInfo> regionList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_REGION, PropConstants.E_TIMES);
		List<TimeDataInfo> networkList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.PV_NETWORK, PropConstants.E_TIMES);

		Map<String, List<TimeDataInfo>> map = new HashMap<String, List<TimeDataInfo>>();
	
		ratePv( referList);
		if (referList.size() > 10) {
			referList = referList.subList(0, 10);
		}
		Map<String, String> urlMap = getReferUrl();
		//根据PV-refer url获取refer app
		for(TimeDataInfo po : referList){
			po.setReferAppName(urlMap.get(po.getKeyName()));
		}
		map.put("refer", referList);

		ratePv( sourceList);
		if (sourceList.size() > 10) {
			sourceList = sourceList.subList(0, 10);
		}
		map.put("source", sourceList);

		ratePv( regionList);
		if (regionList.size() > 10) {
			regionList = regionList.subList(0, 10);
		}
		map.put("region", regionList);

		ratePv( networkList);// 设置比例 放在site 这个字段
		if (networkList.size() > 10) {
			networkList = networkList.subList(0, 10);
		}
		map.put("network", networkList);

		try {
			writeJSONToResponseJSONObject(response, map);
		} catch (IOException e) {
		}

	}
	public Map<String, String> getReferUrl() {
		Map<String, String> urlMap = new HashMap<String, String>();
		Map<String, List<AppUrlRelation>> appurlmap = AppInfoAo.get()
				.findAllAppUrlRelation();
		for (Map.Entry<String, List<AppUrlRelation>> entry : appurlmap
				.entrySet()) {
			String appname = entry.getKey();
			List<AppUrlRelation> urlList = entry.getValue();
			for (AppUrlRelation u : urlList) {
				urlMap.put(u.getAppUrl(), appname);
			}
		}
		return urlMap;
	}

	private void ratePv( List<TimeDataInfo> poList) {
		
		int pv = 0;
		for (TimeDataInfo po : poList) {
			pv+=po.getMainValue();
		}
		
		if (pv != 0) {
			for (TimeDataInfo po : poList) {
				po.setMainRate(DataUtil.rate(DataUtil.transformLong(po.getMainValue()), pv) + "");
			}
		}

	}

	@RequestMapping(params = "method=showPvInfo")
	public void showPvInfo(HttpServletResponse response, Integer appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<TimeDataInfo>  list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES, new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		List<TimeDataInfo> prelist = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES, cal.getTime());
		
		AmlineFlash pvamline = new AmlineFlash();
		
		
		for(TimeDataInfo info: list){
			pvamline.addValue("pv", info.getTime(), info.getMainValue());
			
			Object v = info.getOriginalPropertyMap().get(PropConstants.C_200);
			if(v!=null)
				pvamline.addValue("pv_200", info.getTime(), DataUtil.transformDouble(v));
		}
		
		for(TimeDataInfo info: prelist){
			pvamline.addValue("pv_"+TimeUtil.formatTime(cal.getTime(), "yyyy-MM-dd"), info.getTime(), info.getMainValue());
			Object v =info.getOriginalPropertyMap().get(PropConstants.C_200);
			if(v!=null)
				pvamline.addValue("pv_200_"+TimeUtil.formatTime(cal.getTime(), "yyyy-MM-dd"), info.getTime(), DataUtil.transformDouble(v));
		}
		
		try {
			writeJSONToResponse(response, pvamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(params = "method=showRestInfo")
	public void showRestInfo(HttpServletResponse response, Integer appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<TimeDataInfo>  list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES, new Date());
		
		AmlineFlash restamline = new AmlineFlash();
		
		for(TimeDataInfo info:list){
			restamline.addValue("request-time", info.getTime(), DataUtil.transformDouble(info.getOriginalPropertyMap().get(PropConstants.C_TIME)));
		}
		try {
			writeJSONToResponse(response, restamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(params = "method=showHostPvInfo")
	public void showHostPvInfo(HttpServletResponse response, Integer appId, String ip) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		List<TimeDataInfo>  list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES,ip, new Date());
		
		AmlineFlash pvamline = new AmlineFlash();
		
		
		Map<String,Double> b = BaseLineCache.get().getBaseLine(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES,ip);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf1.format(new Date());
		for(Map.Entry<String,Double> h: b.entrySet() ){
			try {
				pvamline.addValue("pv基线", sdf.parse(time+" "+h.getKey()).getTime(), h.getValue());
			} catch (ParseException e) {
			}
		}
		
		for(TimeDataInfo info: list){
			pvamline.addValue("pv", info.getTime(), info.getMainValue());
			pvamline.addValue("pv_200", info.getTime(), DataUtil.transformDouble(info.getOriginalPropertyMap().get(PropConstants.C_200)));
			pvamline.addValue("pv_302", info.getTime(), DataUtil.transformDouble(info.getOriginalPropertyMap().get(PropConstants.C_200)));
		}
		try {
			writeJSONToResponse(response, pvamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writeJSONToResponseJSONArray(response,list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "method=showHostRestInfo")
	public void showHostRestInfo(HttpServletResponse response, Integer appId,String ip) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<TimeDataInfo>  list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES,ip, new Date());
		
		AmlineFlash restamline = new AmlineFlash();
		
		
		Map<String,Double> b = BaseLineCache.get().getBaseLine(appInfo.getAppName(), KeyConstants.PV, PropConstants.C_TIME,ip);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf1.format(new Date());
		for(Map.Entry<String,Double> h: b.entrySet() ){
			try {
				restamline.addValue("rest基线", sdf.parse(time+" "+h.getKey()).getTime(), h.getValue());
			} catch (ParseException e) {
			}
		}
		
		
		for(TimeDataInfo info:list){
			Object v = info.getOriginalPropertyMap().get(PropConstants.C_TIME);
			if(v!=null)
				restamline.addValue("RT", info.getTime(), DataUtil.transformDouble(v));
		}
		try {
			writeJSONToResponse(response, restamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(params = "method=hit")
	public ModelAndView showPvHit(String appName,String time1,String time2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(time1);
			time.setTime(date1);
		} catch (Exception e1) {
			
		}
		Date cur = time.getTime();
		
		time1 = sdf.format(cur);
		
		try {
			Date date2 = sdf.parse(time2);
			time.setTime(date2);
		} catch (Exception e1) {
			time.add(Calendar.DAY_OF_YEAR, -7);
		}
		Date old = time.getTime();
		
		time2 =  sdf.format(old);
		
		
		
		List<TimeDataInfo>  curlist = commonService.queryKeyDataHistory(appName, KeyConstants.PV, PropConstants.E_TIMES, cur);
		
		
		List<TimeDataInfo>  oldlist = commonService.queryKeyDataHistory(appName, KeyConstants.PV, PropConstants.E_TIMES, old);
		
		AmlineFlash am = new AmlineFlash();
		
		for(TimeDataInfo td:curlist){
			int pv = DataUtil.transformInt(td.getOriginalPropertyMap().get(PropConstants.E_TIMES));
			int pv_hit = DataUtil.transformInt(td.getOriginalPropertyMap().get("pv-hit"));
			am.addValue("pv-"+time1, td.getTime(), pv);
			am.addValue("pv-hit-"+time1, td.getTime(), pv_hit);
		}
		for(TimeDataInfo td:oldlist){
			int pv = DataUtil.transformInt(td.getOriginalPropertyMap().get(PropConstants.E_TIMES));
			int pv_hit = DataUtil.transformInt(td.getOriginalPropertyMap().get("pv-hit"));
			am.addValue("pv-"+time2, td.getTime(), pv);
			am.addValue("pv-hit-"+time2, td.getTime(), pv_hit);
		}
		
		ModelAndView view = new ModelAndView("/time/apache/detail_pv_hit");
		view.addObject("am", am.getAmline());
		view.addObject("time1", time1);
		view.addObject("time2", time2);
		view.addObject("appName", appName);
		return view;
	}
	
	

	

	private static final Logger log = Logger
			.getLogger(ApacheInfoController.class);

}
