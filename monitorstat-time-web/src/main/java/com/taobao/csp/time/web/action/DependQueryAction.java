package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.other.beidou.BeiDouAlarmRecordCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;
import com.taobao.csp.time.web.po.IndexEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.capacity.CspLoadRunAo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.ao.center.DependRelationAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.po.TreeGridAlarmPo;
import com.taobao.monitor.common.po.TreeGridBasePo;
import com.taobao.monitor.common.po.TreeGridData;
import com.taobao.monitor.common.po.TreeGridHotInterfacePo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.common.util.page.Pagination;

@Controller
@RequestMapping("/app/depend/query/show.do")
public class DependQueryAction extends BaseController {


	@Resource(name = "commonService")
	private CommonServiceInterface commonService;


	CspDependInfoAo depAo = CspDependInfoAo.get();
	DependRelationAo  dependRelationAo = DependRelationAo.get();

	private static final Logger logger = Logger .getLogger(DependQueryAction.class);

	@RequestMapping(params="method=queryKeyDetailWithTimeData")
	public ModelAndView queryKeyDetailWithTimeData(String appName,String keyName){
		ModelAndView view = new ModelAndView("/time/depend/kerelationratetable");

		List<CspTimeKeyDependInfo> urlList = null;  
		if(keyName != null && keyName.startsWith("HSF-provider`")) {  //只查HSF provider的url信息
			urlList = CspDependInfoAo.get().getDistinctSourceUrlList(keyName);

			Collections.sort(urlList, new Comparator<CspTimeKeyDependInfo>() {
				@Override
				public int compare(CspTimeKeyDependInfo o1, CspTimeKeyDependInfo o2) {
					return o1.getSourceAppName().compareTo(o2.getSourceAppName());
				}
			});
		} else {
			urlList = new ArrayList<CspTimeKeyDependInfo>();
		}

		AppInfoPo AppInfoPo =  AppInfoCache.getAppInfoByAppName(appName);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("AppInfoPo", AppInfoPo);
		view.addObject("urlList", urlList);
		return view;
	}

	@RequestMapping(params="method=queryAlarmSingleJson")
	public void queryAlarmSingleJson(String appName,String keyName, HttpServletResponse response) {

		List<CspTimeKeyDependInfo> cspTimeKeyList = CspDependInfoAo.get().getSingleSourceKeyList(appName, keyName);
		TreeGridData treeGridData = DependRelationAo.get().formatCspTimeListToSingleTreeGridData(cspTimeKeyList);
		Map<String,Integer> alarmCountMap = new HashMap<String, Integer>();

		if(treeGridData.getAppName() == null || treeGridData.getAppName().trim().equals(""))
			treeGridData.setAppName(appName);
		if(treeGridData.getKeyName() == null || treeGridData.getKeyName().trim().equals(""))
			treeGridData.setKeyName(keyName);

		//按时间间隔取当前时间的前2分钟
		int minute = TimeUtil.getMinuteIndexForCache(new Date(), 2);
		addAlarmInfoToTreeGridData(treeGridData, alarmCountMap, minute, null);
		TreeGridData[] array = new TreeGridData[]{treeGridData};
		try {
			this.writeJSONToResponseJSONArray(response, array);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping(params="method=gotoAlarmDetail")
	public ModelAndView gotoAlarmDetail(String appName,String keyName, HttpServletRequest request) throws Exception{
		AppInfoPo appInfo =  AppInfoCache.getAppInfoByAppName(appName);
		String pageNoParam = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		ModelAndView view = new ModelAndView("/time/depend/dependalarmdetail");
		view.addObject("appInfo", appInfo);
		//第一次进入查询页面
		if(pageNoParam==null){
			pageNoParam = "1";
		}
		if(pageSizeStr==null){
			pageSizeStr = "15";
		}

		//form hidden传一定有值
		int pageNo = Integer.parseInt(pageNoParam);
		int pageSize = Integer.parseInt(pageSizeStr);

		Date to = new Date();
		Date from = Utlitites.getBeginOfTheDay(to);

		//默认查询到当前，查看详细引导到历史查询页面。

		if(keyName == null)
			keyName = "";
		List<TimeDataInfo> timeDataInfoList = commonService.querySingleKeyData(appName, keyName, PropConstants.E_TIMES);
		Pagination<CspTimeKeyAlarmRecordPo> page = CspTimeKeyAlarmRecordAo.get().findAlarmMsgListByOrder(appInfo.getAppName(), keyName, from, to, pageNo, pageSize, null, null);
		view.addObject("keyName",keyName);
		view.addObject("from", Utlitites.getStringOfDate(from, "yyyy-MM-dd HH:mm:ss"));
		view.addObject("to", Utlitites.getStringOfDate(to, "yyyy-MM-dd HH:mm:ss"));
		view.addObject("pagination", page);
		view.addObject("appInfo", appInfo);
		view.addObject("timeDataInfoList", timeDataInfoList);

		return view;
	}
	//  查询自动表生成表格树的版本
	//  @RequestMapping(params="method=queryAlarmMultiJson")
	//  public void queryAlarmMultiJson(String appName,String keyName, HttpServletResponse response) {
	//    String formatKey = dependRelationAo.changeTimeToEagleeyeKey(keyName);  //处理成能够被识别的key
	//    //List<CspTimeKeyDependInfo> keyList = depAo.getSingleSourceKeyList(appName, keyName);
	//    List<TreeGridData> treeList = DependRelationAo.get().getMultiRelationBySourceKey(formatKey);
	//    Map<String,Integer> alarmCountMap = new HashMap<String, Integer>();
	//    for(TreeGridData keyPo: treeList){
	//      if(keyPo.getAppName() == null || keyPo.getAppName().trim().equals(""))
	//        keyPo.setAppName(appName);      
	//      addAlarmInfoToTreeGridData(keyPo, alarmCountMap);
	//    }
	//    try {
	//      this.writeJSONToResponseJSONArray(response, treeList);
	//    } catch (IOException e) {
	//      logger.error("", e);
	//    }
	//  }
	//  
	//  @RequestMapping(params="method=queryAlarmSingleJson")
	//  public void queryAlarmSingleJson(String appName,String keyName, HttpServletResponse response) {
	//    String formatKey = dependRelationAo.changeTimeToEagleeyeKey(keyName);  //处理成能够被识别的key
	//    TreeGridData treeGridData = DependRelationAo.get().getSingleRelationBySourceKey(formatKey);
	//    Map<String,Integer> alarmCountMap = new HashMap<String, Integer>();
	//    if(treeGridData.getAppName() == null || treeGridData.getAppName().trim().equals(""))
	//      treeGridData.setAppName(appName);
	//    addAlarmInfoToTreeGridData(treeGridData, alarmCountMap);
	//    TreeGridData[] array = new TreeGridData[]{treeGridData};
	//    try {
	//      this.writeJSONToResponseJSONArray(response, array);
	//    } catch (IOException e) {
	//      logger.error("", e);
	//    }
	//  }

	//遍历每一棵关系树，把报警信息、当前调用信息都添加到树节点中去
	private void addAlarmInfoToTreeGridData(TreeGridBasePo data, Map<String,Integer> alarmCountMap, int minute, TreeGridBasePo parentNode) {
		Assert.notNull(alarmCountMap);
		if(data instanceof TreeGridData) {
			TreeGridData referData = (TreeGridData)data; 
			TreeGridBasePo[] childArray = referData.getChildren();
			for(TreeGridBasePo childpo: childArray) {
				addAlarmInfoToTreeGridData(childpo, alarmCountMap, minute, referData);        
			}
		} 
		TreeGridAlarmPo po = new TreeGridAlarmPo();
		final String queryKeyName = data.getKeyName(); 
		Integer acount = alarmCountMap.get(data.getAppName() + "_" + queryKeyName);
		if(acount == null){
			acount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(data.getAppName(), queryKeyName, 5);
			alarmCountMap.put(data.getAppName() + "_" + queryKeyName, acount);
		}
		po.setAlarmCount(acount);

		//设置服务提供量
		List<TimeDataInfo> timeDataInfoList = commonService.querySingleKeyData(data.getAppName(), data.getKeyName(), PropConstants.E_TIMES);
		if(timeDataInfoList.size() > 0) {
			if((timeDataInfoList.size() -1 ) > minute ) {
				po.setCallCount(timeDataInfoList.get(minute).getMainValue());        
			} else {
				po.setCallCount(timeDataInfoList.get(0).getMainValue());
			}
		} 

		//只对非根节点，非PV节点统计consumer的量
		//data.getKeyName() != null && !data.getKeyName().startsWith("PV`") 非根节点一般不是URL，所以注释掉 
		if(parentNode != null) {
			//HSF-provider`com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0`checkPrivilege
			//HSF-Consumer`未知`com.taobao.justice.hsf.IvmService:1.0.0`run
			String consumeQueryKey = "HSF-Consumer`" + data.getAppName() + "`" + queryKeyName.substring(queryKeyName.indexOf('`') + 1);      
			List<String> keyList = new ArrayList<String>();
			keyList.add(consumeQueryKey);
			Map<String, List<TimeDataInfo>> consumerMap = commonService.queryMutilKeyData(parentNode.getAppName(), keyList, PropConstants.E_TIMES);
			List<TimeDataInfo> consumerList = consumerMap.get(consumeQueryKey);
			if(consumerList != null && consumerList.size() > 0) {
				if((consumerList.size() -1 ) > minute ) {
					po.setConsumeCount(consumerList.get(minute).getMainValue());        
				} else {
					po.setConsumeCount(consumerList.get(0).getMainValue());
				}         
			}
		}
		//    double rate = Arith.mul(Arith.div(po.get,po.getCallCount(),4), 100);
		//    po.setRate(rate + "%");
		//设置上级服务调用量
		data.setAddedPo(po);  //把报警信息补充到po中去
	}



	/**
	 *@param total 总量 
	 * @return <关联的应用名,对应的refer流量> 1、如果没有数据返回null
	 */
	public Map<String, Long> getHSFReferInfo(String appName) throws Exception {

		Map<String, Long> result = new HashMap<String, Long>();


		Map<String,List<TimeDataInfo>> map = commonService.querykeyDataForChild(appName, KeyConstants.HSF_REFER, PropConstants.E_TIMES);

		for(Map.Entry<String,List<TimeDataInfo>> entry:map.entrySet()){
			if(entry.getValue().size()>0)
				result.put(entry.getKey(), (long)entry.getValue().get(0).getMainValue());
		}
		return result;
	}


	public Map<String, Long> getReferInfo(String appName,Map<String,String> urlMap) throws Exception {

		Map<String, Long> referAppResultMap = new HashMap<String, Long>();

		Map<String,List<TimeDataInfo>> map = commonService.querykeyDataForChild(appName, KeyConstants.PV_REFER, PropConstants.E_TIMES);

		for(Map.Entry<String,List<TimeDataInfo>> entry:map.entrySet()){
			String url = entry.getKey();
			List<TimeDataInfo> list = entry.getValue();

			long pv = 0;
			if(list.size() >0)
				pv = (long)list.get(0).getMainValue();

			String app = urlMap.get(url);
			if(app != null){
				Long num = referAppResultMap.get(app);
				if(num ==null){
					referAppResultMap.put(app, pv);
				}else{
					referAppResultMap.put(app, pv+num);
				}
			}
		}
		return referAppResultMap;
	}


	public IndexEntry getAppIndexEntry(String opsName,Map<String,String> urlMap){

		AppInfoPo po = null; 
		List<BeiDouAlarmRecordPo> beidouAlarmList = BeiDouAlarmRecordCache.get().get(opsName);	
		if(beidouAlarmList.size()!=0){
			IndexEntry index = new IndexEntry();	
			if(beidouAlarmList != null && beidouAlarmList.size()>0){
				index.setExceptionDBNum(beidouAlarmList.size());
			}
			index.setAppName(opsName);
			return index;
		}
		po = AppInfoCache.getAppInfoByAppName(opsName);

		try{
			IndexEntry index = new IndexEntry();
			index.setAppName(opsName);
			if(po!=null){
				index.setAppId(po.getAppId());
				index.setAppType(po.getAppType());
			}

			List<String> apps = new ArrayList<String>();
			apps.add(opsName);




			Map<String,List<TimeDataInfo>> exceptionMap = commonService.querykeyDataForApps(apps, KeyConstants.EXCEPTION, PropConstants.E_TIMES);

			List<TimeDataInfo> e = exceptionMap.get(opsName);
			if(e != null&&e.size()>0){
				index.setExceptionNum((int)e.get(0).getMainValue());
			}



			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(opsName);


			AppInfoPo appPo =  AppInfoCache.getAppInfoByAppName(opsName);
			String appType = appPo.getAppType();

			int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(opsName, 5);
			index.setAlarms(alarmcount);

			//流量信息
			if ("pv".equalsIgnoreCase(appType)) {


				TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.PV,PropConstants.E_TIMES);

				Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.PV, PropConstants.E_TIMES);

				double rate = getFailRate(opsName,(int)tdi.getMainValue());
				Map<String, Long> refer = getReferInfo(opsName, urlMap);

				double d = 0;
				try{
					d = CspLoadRunAo.get().findRecentlyAppLoadRunQps(appPo.getAppId());
				}catch (Exception e1) {
				}

				index.setCapcityRate((int)d);

				index.setFailurerate(rate);
				index.setFtime(tdi.getFtime());
				index.setMachines(hostlist.size());
				index.setPv((int)tdi.getMainValue());

				Calendar cal = Calendar.getInstance();
				for(int i=0;i<5;i++){
					cal.add(Calendar.MINUTE, -1);
					String t = TimeUtil.formatTime(cal.getTime(), "HH:mm");
					Float qps = mapqps.get(t);
					if(qps != null){
						index.setQps(qps.intValue()/60);
						break;
					}
				}

				index.setReferMap(refer);
			}
			if ("center".equalsIgnoreCase(appType)) {


				TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.HSF_PROVIDER,PropConstants.E_TIMES);

				Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);


				Map<String, Long> hsfRefer = getHSFReferInfo(opsName);


				double d = 0;
				try{
					d = CspLoadRunAo.get().findRecentlyAppLoadRunQps(appPo.getAppId());
				}catch (Exception e1) {
				}
				index.setCapcityRate((int)d);

				index.setFtime(tdi.getFtime());
				index.setMachines(hostlist.size());
				index.setPv((int)tdi.getMainValue());

				Calendar cal = Calendar.getInstance();
				for(int i=0;i<5;i++){
					cal.add(Calendar.MINUTE, -1);
					String t = TimeUtil.formatTime(cal.getTime(), "HH:mm");
					Float qps = mapqps.get(t);
					if(qps != null){
						index.setQps(qps.intValue()/60);
						break;
					}
				}

				index.setReferMap(hsfRefer);
			}


			if ("tair".equalsIgnoreCase(appType)) {

			}

			if ("notify".equalsIgnoreCase(appType)) {

			}

			return index;
		}catch (Exception e) {
		}
		return null;
	}

	public double getFailRate(String opsName, long pv) throws Exception {
		double fr = -1;
		// pv是被除数，所以不能为0
		if (pv != 0) {

			Map<String, DataEntry> blockmap = QueryUtil
					.queryRecentlySingleRealTime(opsName, "PV-Block");

			if (blockmap != null) {
				DataEntry d1 = blockmap.get("PV-SS");
				long pvss = 0;
				if (d1 != null) {
					Object pvsso = d1.getValue();
					pvss = pvsso == null ? 0 : DataUtil.transformLong(pvsso);
				}

				DataEntry d2 = blockmap.get("TDOD");
				long tdod = 0;
				if (d2 != null) {
					Object tdodo = d2.getValue();
					tdod = tdodo == null ? 0 : DataUtil.transformLong(tdodo);
				}

				fr = 1 - (pv - pvss) * (100 - tdod) / pv;
			}
		}
		return fr;
	}

	@RequestMapping(params="method=gotoHotInterfacePage")
	public ModelAndView gotoHotInterfacePage(String appNameStr, String company){
		ModelAndView view = new ModelAndView("/time/depend/app_hot_keys");
		view.addObject("appNameStr", appNameStr);
		view.addObject("company", company);
		view.addObject("timeinterval", Constants.CACHE_TIME_INTERVAL);
		return view;
	}
	/**
	 *  查询热点树形结构
	 *  TODO 
	 *  1.未来不排除按时间查询热点的可能性
	 *  2.按照company查询，现在还没有加进来 
	 */
	@RequestMapping(params="method=queryHotInterfaceMultiJson")
	public void queryHotInterfaceMultiJson(String appNameStr, String company, HttpServletResponse response) {
		String[] appNameArray = null;
		if(appNameStr != null && !appNameStr.trim().equals("")) {
			appNameArray = appNameStr.split(","); //参数逗号分隔
		}
		try {
			List<TreeGridData> list = CspDependInfoAo.get().getHotInterfaceGridData(appNameArray);
			Collections.sort(list, new Comparator<TreeGridData>() {
				@Override
				public int compare(TreeGridData o1, TreeGridData o2) {
					return o1.getAppName().compareTo(o2.getAppName());
				}
			});
			addDataToHotInterface(list);
			this.writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	//为热点接口添加实时数据。层数确定为2，所以不用递归
	private void addDataToHotInterface(List<TreeGridData> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for(TreeGridData data : list) {
			TreeGridHotInterfacePo po = new TreeGridHotInterfacePo();
			data.setAddedPo(po);
			String keyName = DependRelationAo.get().changeDependHSFProvideToTimeKey(data.getKeyName());
			data.setKeyName(keyName);
			Map<String, String> mapRoot = new HashMap<String, String>();
			po.setMap(mapRoot);

			TreeGridBasePo childArray[] = data.getChildren();
			if(childArray != null && childArray.length > 0) {
				for(TreeGridBasePo child: childArray) {
					int i=0;
					TreeGridHotInterfacePo poChild = new TreeGridHotInterfacePo();
					child.setAddedPo(poChild);
					String keyNameChild = DependRelationAo.get().changeDependHSFProvideToTimeKey(child.getKeyName());
					child.setKeyName(keyNameChild);
					Map<String, String> childMap = new HashMap<String, String>();
					poChild.setMap(childMap);


					List<TimeDataInfo> timeDataInfoListChild = commonService.querySingleKeyData(child.getAppName(), keyNameChild, PropConstants.E_TIMES);
					for(TimeDataInfo timeInfo: timeDataInfoListChild) {
						String formatTimeFull = sdfFull.format(new Date(timeInfo.getTime()));
						String start = formatTimeFull.substring(0, formatTimeFull.lastIndexOf(':')) + ":00";
						String end = formatTimeFull.substring(0, formatTimeFull.lastIndexOf(':')) + ":59";
						//Integer acount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(child.getAppName(), keyNameChild, Constants.CACHE_TIME_INTERVAL - i);//最近默认5分钟
						Integer acount = CspTimeKeyAlarmRecordAo.get().countAlarmByTime(child.getAppName(), keyNameChild, start, end);//查询每分钟的报警数据
						if(acount == null)
							acount = 0;
						mapRoot.put("time" + i, sdf.format(new Date(timeInfo.getTime())));
						childMap.put("time" + i, timeInfo.getMainValue() + "(<span style='color: red'>" + acount + "</span>)");
						i++;
					}    
				}
			}
		}
	}

	@RequestMapping(params="method=hotKeyGotoTimeReal")
	public String hotKeyGotoTimeReal(String appName, String keyName) {
		AppInfoPo appInfo =  AppInfoCache.getAppInfoByAppName(appName);
		if(keyName.startsWith("HSF-provider`")) {
			//跳到应用的接口页面
			//return "redirect:/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=" + appInfo.getAppId();
			//跳到应用的方法页面
			//HSF-provider`com.taobao.ump.core.service.PromotionReadService:1.0.0`findAllPromotionInfo4Cart
			keyName = keyName.substring(0, keyName.lastIndexOf("`")); //取得方法前的string
			return "redirect:/app/detail/hsf/provider/show.do?method=gotohsfProviderChlid&appId=" + appInfo.getAppId() + "&key=" + keyName;
		} else if(keyName.startsWith("PV`")) {  //url
			return "redirect:/app/detail/apache/show.do?method=gotoSourceDetail&appId=" + appInfo.getAppId();
		} else {
			return "redirect:/app/detail/show.do?method=showIndex&appId=" + appInfo.getAppId();
		}
	}

	@RequestMapping(params="method=queryAppDetailWithTimeData")
	public ModelAndView queryAppDetailWithTimeData(String appName) throws IOException {

		AppInfoPo po = AppInfoCache.getAppInfoByAppName(appName);

		List<CspTimeAppDependInfo> sourceAppList = CspDependInfoAo.get().getCspTimeAppDependInfoBySourceName(appName);

		Map<String,IndexEntry> indexMap = new HashMap<String,IndexEntry>();

		Map<String, String> appurlmap = AppInfoAo.get().findAllAppUrlRelationMap();
		Set<String> appNames = new HashSet<String>();
		for(CspTimeAppDependInfo a:sourceAppList){
			appNames.add(a.getAppName());
			appNames.add(a.getDepAppName());
		}

		for(String app:appNames){
			IndexEntry ie = getAppIndexEntry(app,appurlmap);
			if(ie != null){
				indexMap.put(app, ie);
			}
		}

		ModelAndView view = new ModelAndView("/time/depend/app_depend_manual");
		view.addObject("sourceAppList", sourceAppList);
		view.addObject("indexMap", indexMap);
		view.addObject("appNames", appNames);
		view.addObject("appinfo", po);
		return view;
	}   

	/**
	 * 热点接口展示更新版。展示几个主要应用当前的调用量。
	 * @param appNameStr
	 * @param company
	 * @return
	 */
	@RequestMapping(params="method=gotoHotInterfacePageNew")
	public ModelAndView gotoHotInterfacePageNew(String company){
		if(company == null)
			company = "taobao";
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] timeArray = new String[Constants.CACHE_TIME_INTERVAL];
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		for(int i=0; i<Constants.CACHE_TIME_INTERVAL; i++) {
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - 1);
			String time = TimeUtil.formatTime(cal.getTime(), "HH:mm");
			map.put(time, new Integer(i));
			timeArray[i] = time;
		}
		Map<String, String[]> appMap = new HashMap<String, String[]>();
		appMap.put("login", new String[]{"PV`http://login.taobao.com/member/login.jhtml"});
		appMap.put("hesper", new String[]{"PV`http://list.taobao.com/search_auction.htm",
										"PV`http://list.taobao.com/market/sp.htm"});
		appMap.put("search", new String[]{"PV`http://s.taobao.com/search"});
		appMap.put("detail", new String[]{"PV`http://item.taobao.com/item.htm"});
		appMap.put("buy", new String[]{"PV`http://buy.taobao.com/auction/buy_now.jhtml",
				"PV`http://buy.taobao.com/auction/buy_now.htm","PV`http://buy.taobao.com/auction/buy.htm"});
		appMap.put("consign", new String[]{"PV`http://wuliu.taobao.com/user/consign.htm"});
		
		Map<String, Map<String, TimeDataInfo[]>> valueMapMain = new HashMap<String, Map<String, TimeDataInfo[]>>();
		for(String appName: appMap.keySet()) {
			Map<String, TimeDataInfo[]> valueMap = new HashMap<String, TimeDataInfo[]>();			
			valueMapMain.put(appName, valueMap);
			String[] array = appMap.get(appName);
			for(String keyName:array) {
				//DependRelationAo.get().changeDependHSFProvideToTimeKey(keyName);// for hsf
				List<TimeDataInfo> timeDataInfoListChild = commonService.querySingleKeyData(appName, keyName, PropConstants.E_TIMES);
				
				TimeDataInfo[] valueArray = valueMap.get(keyName);
				if(valueArray == null) {
					valueArray = new TimeDataInfo[Constants.CACHE_TIME_INTERVAL];
					for(int i=0; i<valueArray.length; i++)
						valueArray[i] = new TimeDataInfo();
					valueMap.put(keyName, valueArray);	
				}
				for(TimeDataInfo po : timeDataInfoListChild) {
					Integer index = map.get(po.getFtime());
					if(index == null)
						continue;
					valueArray[index] = po;
				}
			}
		}
		List<String> nameList = new ArrayList<String>();
		nameList.add("login");
		nameList.add("hesper");
		nameList.add("search");
		nameList.add("detail");
		nameList.add("buy");
		nameList.add("consign");
		
		ModelAndView view = new ModelAndView("/time/depend/app_hot_keys_list");
		view.addObject("company", company);
		//view.addObject("appMap", appMap);
		view.addObject("valueMapMain", valueMapMain);
		view.addObject("nameList", nameList);
		view.addObject("timeArray", timeArray);
		return view;
	}
}
