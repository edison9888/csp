package com.taobao.csp.time.web.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.cache.CapacityCache;
import com.taobao.csp.time.custom.ThreadTaskGetRealTimeTrade;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.IndexDependTable;
import com.taobao.csp.time.web.po.IndexEntry;
import com.taobao.csp.time.web.po.IndexHSfTable;
import com.taobao.csp.time.web.po.IndexPvTable;
import com.taobao.csp.time.web.po.RealTimeTradePo;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.db.impl.other.HaBoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
@Controller
@RequestMapping("/index.do")
/*
 * index_rel/apps.do", "/index_rel/app_pairs.do", "/index_rel/top_n.do"
 */
/* 首页大盘图 */
public class IndexController extends BaseController {

	private static final Logger logger = Logger.getLogger(IndexController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	@Resource(name = "haBoDao")
	private HaBoDao haBoDao;

	private static Map<String,List<String>> companyMap = new HashMap<String, List<String>>();

	static {
		companyMap.put("taobao", Arrays.asList("cart", "hesper",
				"shopcenter", "shopsystem", "tradeplatform", "uicfinal", "ump",
				"login", "tf_tm", "detail", "tf_buy", "itemcenter"));
		companyMap.put("tmall", Arrays.asList("tmallpromotion","malllist","wlb","sic","tmallsell","pointcenter","tmallbuy","malldetail","wlbexternal","memberprofile"));

		companyMap.put("tmall2012", Arrays.asList("promotion","shopsystem","shopcenter","tmallsearch","memberplatform","pointcenter","malldetail",
				"tmallcart","tmallbuy","tf_tm","tradeplatform","tmallpromotion","ump","inventoryplatform","logisticscenter",
				"itemcenter","uicfinal","malldetailskip","login"));
		//coronet->天猫无线后台服务。无线没人关注，去掉。
		//"top" 去掉，原来收集apache，造成load比较高，后来不继续收集。
		//malllist -> tmallsearch
	}

	public static final Logger log = Logger.getLogger(IndexController.class);


	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(String company) {
		List<String> appList = companyMap.get(company);

		ModelAndView view = new ModelAndView("index_b");

		List<AppInfoPo> list = new ArrayList<AppInfoPo>();
		for(String app:appList){
			AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
			list.add(po);
		}

		view.addObject("appList", list);
		view.addObject("company", company);
		return view;
	}

	/**
	 * 双11，Tmall监控大盘
	 */
	@RequestMapping(params = "method=showIndex2012")
	public ModelAndView showIndex2012() {
		List<String> appList = companyMap.get("tmall2012");
		Map<String, Integer[]> capacityMap = new HashMap<String, Integer[]>();//opsname, [machinenum, maxqps]

		ModelAndView view = new ModelAndView("tmall/index_2012");
		Map<String, AppInfoPo> appMap = new HashMap<String, AppInfoPo>();
		for(String app:appList){
			AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
			if (po == null) {
				po = new AppInfoPo();
				po.setAppId(1);
				po.setOpsName(app);
				po.setAppType("pv");
			}

			appMap.put(app, po);

			//不常变化的信息，一次性读取出来
			Integer[] array = capacityMap.get(app);
			if(array == null) {
				array = new Integer[]{0, 0};
				capacityMap.put(app, array);
			}

			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(po.getOpsName());
			if(hostlist != null)
				array[0] = hostlist.size();

			double d = CapacityCache.get().getAppCapacity(po.getAppId());
			array[1] = (int)d;
		}

		view.addObject("appMap", appMap);
		view.addObject("capacityMap", capacityMap);
		return view;
	}
	
	/**
	 * tmall用户路径
	 */
	@RequestMapping(params = "method=showUserPathMall")
	public ModelAndView showUserPathMall() {
		List<String> appList = companyMap.get("tmall2012");
		Map<String, Integer[]> capacityMap = new HashMap<String, Integer[]>();//opsname, [machinenum, maxqps]

		ModelAndView view = new ModelAndView("tmall/showUserPathMall");
		Map<String, AppInfoPo> appMap = new HashMap<String, AppInfoPo>();
		for(String app:appList){
			AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
			if (po == null) {
				po = new AppInfoPo();
				po.setAppId(1);
				po.setOpsName(app);
				po.setAppType("pv");
			}

			appMap.put(app, po);

			//不常变化的信息，一次性读取出来
			Integer[] array = capacityMap.get(app);
			if(array == null) {
				array = new Integer[]{0, 0};
				capacityMap.put(app, array);
			}

			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(po.getOpsName());
			if(hostlist != null)
				array[0] = hostlist.size();

			double d = CapacityCache.get().getAppCapacity(po.getAppId());
			array[1] = (int)d;
		}

		view.addObject("appMap", appMap);
		view.addObject("appTest", "test");
		view.addObject("capacityMap", capacityMap);
		return view;
	}
	
	/**
	 * TB用户路径
	 */
	@RequestMapping(params = "method=showUserPathTB")
	public ModelAndView showUserPathTB() {
		List<String> appList = companyMap.get("taobao");
		Map<String, Integer[]> capacityMap = new HashMap<String, Integer[]>();//opsname, [machinenum, maxqps]

		ModelAndView view = new ModelAndView("tmall/showUserPathTB");
		Map<String, AppInfoPo> appMap = new HashMap<String, AppInfoPo>();
		for(String app:appList){
			AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
			if (po == null) {
				po = new AppInfoPo();
				po.setAppId(1);
				po.setOpsName(app);
				po.setAppType("pv");
			}

			appMap.put(app, po);

			//不常变化的信息，一次性读取出来
			Integer[] array = capacityMap.get(app);
			if(array == null) {
				array = new Integer[]{0, 0};
				capacityMap.put(app, array);
			}

			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(po.getOpsName());
			if(hostlist != null)
				array[0] = hostlist.size();

			double d = CapacityCache.get().getAppCapacity(po.getAppId());
			array[1] = (int)d;
		}

		view.addObject("appMap", appMap);
		view.addObject("capacityMap", capacityMap);
		return view;
	}
	/**
	 *2012 tmall 大盘图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getAppInfo2012")
	public void getAppInfo2012(HttpServletResponse response,String company) throws Exception {

		//		List<String> appList = companyMap.get(company);
		//		
		//		if(appList == null){
		//			return ;
		//		}

		List<String> appList = companyMap.get("tmall2012");
		List<IndexEntry> indexList = new ArrayList<IndexEntry>();

		//获取异常信息
		Map<String,List<TimeDataInfo>> exceptionMap = commonService.querykeyDataForApps(appList, KeyConstants.EXCEPTION, PropConstants.E_TIMES);

		//遍历大盘页面
		for (String opsName : appList) {

			IndexEntry index = new IndexEntry();
			index.setAppName(opsName);
			List<TimeDataInfo> e = exceptionMap.get(opsName);
			if(e != null&&e.size()>0){
				index.setExceptionNum((int)e.get(0).getMainValue());
			}

			AppInfoPo appPo =  AppInfoCache.getAppInfoByAppName(opsName);
			if (appPo == null) {
				appPo = new AppInfoPo();
				appPo.setAppId(1);
				appPo.setOpsName(opsName);
				appPo.setAppType("pv");
			}			
			String appType = appPo.getAppType();

			try{
				//流量信息
				if ("pv".equalsIgnoreCase(appType)) {
					TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.PV,PropConstants.E_TIMES);

					Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.PV, PropConstants.E_TIMES);

					double rate = getFailRate(opsName,(int)tdi.getMainValue());	//失败率

					int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(opsName, 5);
					index.setAlarms(alarmcount);//目前没有

					index.setFailurerate(rate);
					index.setFtime(tdi.getFtime());
					index.setPv((int)tdi.getMainValue());

					try {
						//响应时间
						Object value = tdi.getOriginalPropertyMap().get(PropConstants.C_TIME);
						index.setRt(TimeUtil.getLongValueOfObj(value));
					} catch (Exception e2) {
						index.setRt(0);
						logger.error("rt exception", e2);
					}

					Calendar cal = Calendar.getInstance();
					for(int i=0;i<5;i++){
						cal.add(Calendar.MINUTE, -1);
						String t = TimeUtil.formatTime(cal.getTime(), "HH:mm");
						Float qps = mapqps.get(t);
						if(qps != null){
							index.setQps(qps.intValue());
							break;
						}
					}
					//基线对比
					String ftime = index.getFtime();

					index.setQps(index.getQps()/60);

					String pvRate = BaseLineCache.get().getScale(opsName,KeyConstants.PV, PropConstants.E_TIMES, ftime, index.getPv());

					String excpetionRate = BaseLineCache.get().getScale(opsName,KeyConstants.EXCEPTION, PropConstants.E_TIMES, ftime, index.getPv());
					index.setPvRate(pvRate);
					index.setExceptionRate(excpetionRate);
				}
				if ("center".equalsIgnoreCase(appType)) {
					TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.HSF_PROVIDER,PropConstants.E_TIMES);

					Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

					int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(opsName, 5);
					index.setAlarms(alarmcount);//目前没有

					index.setFtime(tdi.getFtime());
					index.setPv((int)tdi.getMainValue());

					double rate = getFailRate(opsName,(int)tdi.getMainValue());	//失败率

					index.setFailurerate(rate);

					try {
						//响应时间
						Object value = tdi.getOriginalPropertyMap().get(PropConstants.C_TIME);
						index.setRt(TimeUtil.getLongValueOfObj(value));
					} catch (Exception e2) {
						index.setRt(0);
						logger.error("rt exception", e2);
					}				

					Calendar cal = Calendar.getInstance();
					for(int i=0;i<5;i++){
						cal.add(Calendar.MINUTE, -1);
						String t = TimeUtil.formatTime(cal.getTime(), "HH:mm");
						Float qps = mapqps.get(t);
						if(qps != null){
							index.setQps(qps.intValue());
							break;
						}
					}

					index.setQps(index.getQps()/60);

					String ftime = index.getFtime();

					String pvRate = BaseLineCache.get().getScale(opsName,KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, ftime, index.getPv());

					String excpetionRate = BaseLineCache.get().getScale(opsName,KeyConstants.EXCEPTION, PropConstants.E_TIMES, ftime, index.getExceptionNum());
					index.setPvRate(pvRate);
					index.setExceptionRate(excpetionRate);

					//平均load信息
					Map<String,Float> topMap = commonService.queryAverageKeyDataByHost(opsName, KeyConstants.TOPINFO, PropConstants.LOAD);
					if(topMap != null) {
						Float load = topMap.get(ftime);
						if(load != null) {
							index.setLoad(DataUtil.round(load, 2, BigDecimal.ROUND_HALF_UP));
						}
					}
				}
			}catch (Exception e1) {
				log.error("出错"+opsName,e1);
			}
			indexList.add(index);
		}

		/*下面3个比较特别*/
		IndexEntry indexCdn = new IndexEntry();
		indexCdn.setAppName("cdn");

		IndexEntry indexAlipay = new IndexEntry();
		indexAlipay.setAppName("alipay");

		IndexEntry indexAlipayRed = new IndexEntry();
		indexAlipayRed.setAppName("alipay_red");

		indexList.add(indexCdn);
		indexList.add(indexAlipay);
		indexList.add(indexAlipayRed);

		writeJSONToResponseJSONArray(response, indexList);
	}	

	@RequestMapping(params = "method=queryIndexTableForDepend")
	public ModelAndView queryIndexTableForDepend(int appId, Integer showCount) {

		IndexDependTable table = new IndexDependTable();
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		int dAbility = 0;		//集群能力
		int hostNumber = 0;		//集群机器数
		if(showCount == null)	//每一种描述显示的应用的数量
			showCount = 5;
		int currentPv = 0;	//集群当前负荷
		int consumePv = 0;	//我消费的调用量

		if (appInfo == null) {
			appInfo = new AppInfoPo();
			appInfo.setAppId(1);
			appInfo.setOpsName("");
			appInfo.setAppType("pv");
		} else {
			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(appInfo.getOpsName());
			dAbility = (int)CapacityCache.get().getAppCapacity(appInfo.getAppId());
			hostNumber = hostlist.size();
			dAbility *= hostNumber*60;	//对比的是PV

			if(appInfo.getAppType().equalsIgnoreCase("pv")) {	//前端应用
				List<TimeDataInfo> sourceList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getOpsName(), KeyConstants.PV, PropConstants.E_TIMES);
				for(TimeDataInfo po : sourceList)
					currentPv += po.getMainValue();
				ratePv(sourceList);
				sourceList = subListByLength(sourceList, showCount);
				table.setSourceList(sourceList);				
			} else {	//后端应用
				List<TimeDataInfo> referList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getOpsName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES);
				for(TimeDataInfo po : referList)
					currentPv += po.getMainValue();
				ratePv(referList);
				referList = subListByLength(referList, showCount);
				table.setDependMeAppList(referList);
			}
		}
		//我消费部分，PV或Center都有
		List<TimeDataInfo> consumeList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getOpsName(), KeyConstants.HSF_CONSUMER, PropConstants.E_TIMES);
		for(TimeDataInfo po : consumeList)
			consumePv += po.getMainValue();

		ratePv(consumeList);
		consumeList = subListByLength(consumeList, showCount);
		table.setMeDependAppList(consumeList);

		ModelAndView view = new ModelAndView("/time/app_index_table_depend");
		view.addObject("table", table);
		view.addObject("appInfo", appInfo);
		view.addObject("showCount", showCount);
		view.addObject("dAbility", dAbility);
		view.addObject("hostNumber", hostNumber);
		view.addObject("currentPv", currentPv);
		view.addObject("consumePv", consumePv);
		return view;
	}	

	//	暂时注释掉
	//	@RequestMapping(params = "method=queryIndexTableForGroupApp")
	//	public ModelAndView queryIndexTableForGroupApp(Integer appId, Integer count) throws NumberFormatException, Exception {
	//		ModelAndView view = new ModelAndView("/time/app_index_table_minute");
	//
	//		if(count == null || count ==0)
	//			count = 1;
	//
	//		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
	//		if(appInfo == null) {
	//			view.addObject("error", "找不到应用 for appId->" + appId);
	//			return view;
	//		}
	//		//<groupname, list<ip>>
	//		Map<String, List<String>> groupMap = GroupManager.get().getGroupInfoByAppName(appInfo.getOpsName());
	//
	//		//groupname,IndexMinuteTableMain
	//		Map<String, IndexMinuteTableMain> resultMap = new HashMap<String, IndexMinuteTableMain>();
	//
	//		final Date now = new Date();
	//		Calendar cal = Calendar.getInstance();
	//		cal.setTime(now);
	//		cal.set(Calendar.SECOND, 0);
	//		cal.set(Calendar.MILLISECOND, 0);
	//
	//		final String fTime = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
	//		if(groupMap == null) {	//没有分组的情况
	//			//我分组信息
	//		} else {	//有分组的情况
	//			for(Entry<String, List<String>> entry : groupMap.entrySet()) {
	//				List<IndexMinuteTable> indexMinuteTableList = new ArrayList<IndexMinuteTable>();
	//
	//				//记录所有分钟的数据
	//				Map<String, IndexMinuteTable> indexMinuteMap = new HashMap<String, IndexMinuteTable>();
	//
	//				String groupName = entry.getKey();
	//				final int machineSize = groupMap.get(groupName).size();				
	//
	//				IndexMinuteTableMain indexMain = new IndexMinuteTableMain();
	//				indexMain.setList(indexMinuteTableList);
	//				indexMain.setAppId(appInfo.getAppId());
	//				indexMain.setAppName(appInfo.getOpsName());
	//				indexMain.setCapcityQps(111);	//FIXME 读取最大QPS*机器数。如何读取分组的QPS
	//				indexMain.setGroupName(groupName);
	//				indexMain.setMachines(machineSize);
	//				indexMain.setFtime(fTime);	//请求时间
	//				resultMap.put(groupName, indexMain);
	//				final String groupAppName = CommonUtil.combinAppNameAndGroupName(appInfo.getOpsName(), groupName); 
	//				List<String> ipList = entry.getValue();
	//				for(String ip : ipList) {
	//					//机器信息
	//					Map<Date,Map<String,String>> cur_top = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
	//							KeyConstants.TOPINFO, ip, new String[]{PropConstants.CPU, PropConstants.LOAD}, now);
	//					//JVM信息
	//					Map<Date,Map<String,String>> cur_jvm = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
	//							KeyConstants.JVMINFO, ip, new String[]{PropConstants.JVMGC, PropConstants.JVMMEMORY}, now);
	//
	//					//机器信息
	//					for(Entry<Date,Map<String,String>> tmpEntry : cur_top.entrySet()) {
	//						Date date = tmpEntry.getKey();
	//						Map<String,String> map = tmpEntry.getValue();
	//						IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
	//						if(index == null) {
	//							index = new IndexMinuteTable();
	//							indexMinuteMap.put(date.getTime() + "", index);
	//						}
	//						index.setCpu(Float.parseFloat(DataUtil.getValueOfMap(map, PropConstants.CPU)) + index.getCpu());
	//						index.setLoad(Double.parseDouble(DataUtil.getValueOfMap(map, PropConstants.LOAD)) + index.getLoad());
	//					}
	//
	//					//JVM信息
	//					for(Entry<Date,Map<String,String>> tmpEntry : cur_jvm.entrySet()) {
	//						Date date = tmpEntry.getKey();
	//						Map<String,String> map = tmpEntry.getValue();
	//						IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
	//						if(index == null) {
	//							index = new IndexMinuteTable();
	//							indexMinuteMap.put(date.getTime() + "", index);
	//						}
	//						index.setGc(Integer.parseInt(DataUtil.getValueOfMap(map, PropConstants.JVMGC)) + index.getGc());
	//						index.setMemory(Float.parseFloat(DataUtil.getValueOfMap(map, PropConstants.JVMMEMORY)) + index.getMemory());
	//					}
	//
	//					//PV相关
	//					if(appInfo.getAppType().equalsIgnoreCase("pv")) {
	//						//PV相关
	//						Map<Date,Map<String,String>> cur_pv = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
	//								KeyConstants.PV, ip, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME, PropConstants.C_200}, now);
	//						for(Entry<Date,Map<String,String>> tmpEntry : cur_pv.entrySet()) {
	//							Date date = tmpEntry.getKey();
	//							Map<String,String> map = tmpEntry.getValue();
	//							IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
	//							if(index == null) {
	//								index = new IndexMinuteTable();
	//								indexMinuteMap.put(date.getTime() + "", index);
	//							}
	//							index.setPv(Long.parseLong(DataUtil.getValueOfMap(map, PropConstants.E_TIMES)) + index.getPv());
	//							index.setRt(Long.parseLong(DataUtil.getValueOfMap(map, PropConstants.C_TIME)) + index.getRt());
	//							index.setPv200(Long.parseLong(DataUtil.getValueOfMap(map, PropConstants.C_200)) + index.getPv200());
	//						}
	//					} else {
	//						//HSF相关
	//						Map<Date,Map<String,String>> cur_hsf = QueryHistoryUtil.querySingleHostMultiProperty(appInfo.getOpsName(), 
	//								KeyConstants.HSF_PROVIDER, ip, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME}, now);
	//						for(Entry<Date,Map<String,String>> tmpEntry : cur_hsf.entrySet()) {
	//							Date date = tmpEntry.getKey();
	//							Map<String,String> map = tmpEntry.getValue();
	//							IndexMinuteTable index = indexMinuteMap.get(date.getTime() + "");
	//							if(index == null) {
	//								index = new IndexMinuteTable();
	//								indexMinuteMap.put(date.getTime() + "", index);
	//							}
	//							index.setPv(Long.parseLong(DataUtil.getValueOfMap(map, PropConstants.E_TIMES)) + index.getPv());
	//							index.setRt(Long.parseLong(DataUtil.getValueOfMap(map, PropConstants.C_TIME)) + index.getRt());
	//						}
	//					}
	//				}
	//
	//				//计算基线
	//				final Map<String, Set<String>> keyNameMap = new HashMap<String, Set<String>>();
	//				keyNameMap.put(KeyConstants.TOPINFO, new HashSet<String>(Arrays.asList(new String[]{PropConstants.CPU,PropConstants.LOAD})));
	//				keyNameMap.put(KeyConstants.JVMINFO, new HashSet<String>(Arrays.asList(new String[]{PropConstants.JVMGC, PropConstants.JVMMEMORY})));
	//				keyNameMap.put(KeyConstants.PV, new HashSet<String>(Arrays.asList(new String[]{PropConstants.E_TIMES, PropConstants.C_TIME, PropConstants.C_200})));
	//				keyNameMap.put(KeyConstants.HSF_PROVIDER, new HashSet<String>(Arrays.asList(new String[]{PropConstants.E_TIMES, PropConstants.C_TIME})));
	//
	//				//<key`property, map<ftime, value>>
	//				final Map<String, Map<String, Double>> baseLineMap = new HashMap<String, Map<String, Double>>();
	//
	//				for(Entry<String, Set<String>> keyEntry : keyNameMap.entrySet()) {
	//					String keyName = keyEntry.getKey();
	//					Set<String> properSet = keyEntry.getValue();
	//					for(String prop : properSet) {
	//						Map<String, Double> tmpMap = BaseLineCache.get().getBaseLineForGroup(groupAppName, keyName, prop);
	//						if(!baseLineMap.containsKey(Util.combinAppKeyName(keyName, prop))) {
	//							baseLineMap.put(Util.combinAppKeyName(keyName, prop), tmpMap);
	//						} else {
	//							logger.error(String.format("keyName=%s,prop=%s，有重复", keyName, prop));
	//						}
	//					}
	//				}
	//
	//				//把数据按时间间隔组织合并,恢复时间到请求的时间
	//				cal.setTime(now);
	//				final int showCount = cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE);
	//
	//				for(int j=0; j<showCount; j++) {
	//					IndexMinuteTable indexSum = new IndexMinuteTable();
	//					indexMinuteTableList.add(indexSum);
	//					String fTimeTmp = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
	//					indexSum.setFtime(fTimeTmp);
	//
	//					cal.add(Calendar.MINUTE, -1);
	//
	//					IndexMinuteTable tableTmp = indexMinuteMap.get(cal.getTimeInMillis() + "");
	//					if(tableTmp == null) {
	//						indexSum.setCpu(0);
	//						indexSum.setLoad(0);
	//						indexSum.setGc(0);
	//						indexSum.setMemory(0);
	//						indexSum.setPv(0);
	//						indexSum.setPv200(0);
	//						indexSum.setRt(0);
	//					} else {
	//						indexSum.setCpu(tableTmp.getCpu());
	//						indexSum.setLoad(tableTmp.getLoad());
	//						indexSum.setGc(tableTmp.getGc());
	//						indexSum.setMemory(tableTmp.getMemory());
	//						indexSum.setPv(tableTmp.getPv());
	//						indexSum.setPv200(tableTmp.getPv200());
	//						indexSum.setRt(tableTmp.getRt());	
	//					}
	//					//基线数据
	//					try {
	//						indexSum.setCpuPre(baseLineMap
	//								.get(Util.combinAppKeyName(KeyConstants.TOPINFO,PropConstants.CPU))
	//								.get(fTimeTmp).floatValue());
	//					} catch (Exception e) {
	//						indexSum.setCpuPre(0);
	//					}
	//					try {
	//						indexSum.setLoadPre(baseLineMap
	//								.get(Util.combinAppKeyName(KeyConstants.TOPINFO,PropConstants.LOAD))
	//								.get(fTimeTmp));
	//					} catch (Exception e) {
	//						indexSum.setLoadPre(0);
	//					}	
	//					try {
	//						indexSum.setGcPre(baseLineMap
	//								.get(Util.combinAppKeyName(KeyConstants.JVMINFO,PropConstants.JVMGC))
	//								.get(fTimeTmp).intValue());
	//					} catch (Exception e) {
	//						indexSum.setGcPre(0);
	//					}	
	//					try {
	//						indexSum.setMemoryPre(baseLineMap
	//								.get(Util.combinAppKeyName(KeyConstants.JVMINFO,PropConstants.JVMMEMORY))
	//								.get(fTimeTmp).floatValue());
	//					} catch (Exception e) {
	//						indexSum.setMemoryPre(0);
	//					}		
	//
	//					String pvCompineKeyTmp = null;
	//					String rtCompineKeyTmp = null;
	//
	//					if(appInfo.getAppType().equalsIgnoreCase("pv")) {
	//						pvCompineKeyTmp = Util.combinAppKeyName(KeyConstants.PV, PropConstants.E_TIMES);
	//						rtCompineKeyTmp = Util.combinAppKeyName(KeyConstants.PV, PropConstants.C_TIME);
	//					} else {
	//						//						try {
	//						//							indexSum.setPv200Pre(baseLineMap
	//						//									.get(Util.combinAppKeyName(KeyConstants.PV, PropConstants.C_200))
	//						//									.get(fTimeTmp).longValue());
	//						//						} catch (Exception e) {
	//						//							indexSum.setPv200Pre(0);
	//						//						}
	//						pvCompineKeyTmp = Util.combinAppKeyName(KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
	//						rtCompineKeyTmp = Util.combinAppKeyName(KeyConstants.HSF_PROVIDER, PropConstants.C_TIME);						
	//					}
	//
	//					indexSum.setCpu(indexSum.getCpu()/(count*machineSize));
	//					indexSum.setCpuPre(indexSum.getCpuPre()/(count*machineSize));
	//					indexSum.setLoad(indexSum.getLoad()/(count*machineSize));
	//					indexSum.setLoadPre(indexSum.getLoadPre()/(count*machineSize));
	//					indexSum.setGc(indexSum.getGc()/(count*machineSize));
	//					indexSum.setGcPre(indexSum.getGcPre()/(count*machineSize));
	//					indexSum.setMemory(indexSum.getMemory()/(count*machineSize));
	//					indexSum.setMemoryPre(indexSum.getMemoryPre()/(count*machineSize));
	//					//RT
	//					try {
	//						indexSum.setRtPre(baseLineMap.get(rtCompineKeyTmp).get(fTimeTmp).longValue());
	//					} catch (Exception e) {
	//						indexSum.setRtPre(0);
	//					}	
	//
	//					//					//集群QPS【集群QPS统计意义不大】
	//					//					indexSum.setQps(indexSum.getPv()/(count*60));
	//					//					indexSum.setQpsPre(indexSum.getPvPre()/(count*60));
	//
	//					//单主机QPS
	//					indexSum.setHostQps(indexSum.getPv()/(count*machineSize*60));
	//					try {
	//						indexSum.setHostQps(baseLineMap.get(pvCompineKeyTmp).get(fTimeTmp).longValue()/60);
	//					} catch (Exception e) {
	//						indexSum.setHostQps(0);
	//					}
	//
	//					//对比计算比例
	//					indexSum.setCpuRate(DataUtil.scale(indexSum.getCpu(), indexSum.getCpuPre()));
	//					indexSum.setLoadRate(DataUtil.scale(indexSum.getLoad(), indexSum.getLoadPre()));
	//					indexSum.setGcRate(DataUtil.scale(indexSum.getGc(), indexSum.getGcPre()));
	//					indexSum.setMemoryRate(DataUtil.scale(indexSum.getMemory(), indexSum.getMemoryPre()));
	//					//因为集群历史机器数没有记录，集群总PV的基线对比没有意义。
	//					//					indexSum.setPvRate(DataUtil.scale(indexSum.getPv(), indexSum.getPvPre()));
	//					//					indexSum.setPv200Rate(DataUtil.scale(indexSum.getPv200(), indexSum.getPv200Pre()));
	//					indexSum.setHostQpsRate(DataUtil.scale(indexSum.getHostQps(), indexSum.getHostQpsPre()));
	//					indexSum.setRtRate(DataUtil.scale(indexSum.getRt(), indexSum.getRtPre()));
	//				}
	//				//按时间降序
	//				Collections.sort(indexMinuteTableList, new Comparator<IndexMinuteTable>() {
	//					@Override
	//					public int compare(IndexMinuteTable o1, IndexMinuteTable o2) {
	//						return -o1.getFtime().compareTo(o2.getFtime());
	//					}
	//				});
	//			}
	//		}
	//		view.addObject("resultMap", resultMap);
	//		return view;
	//	}	
	private List<TimeDataInfo> subListByLength (List<TimeDataInfo> list, int length) {
		if (list.size() > length) {
			list = list.subList(0, length);
		}
		return list;
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
	//新的Tdod日志
	public double getFailRate(String opsName, long pv) throws Exception {
		double fr = -1;
		// pv是被除数，所以不能为0
		if (pv != 0) {

			Map<String, DataEntry> blockmap = QueryUtil
					.queryRecentlySingleRealTime(opsName, "PV-Block");

			if (blockmap != null) {
				DataEntry d1 = blockmap.get(PropConstants.PV_SS);
				long pvss = 0;
				if (d1 != null) {
					Object pvsso = d1.getValue();
					pvss = pvsso == null ? 0 : DataUtil.transformLong(pvsso);
				}

				DataEntry d2 = blockmap.get(PropConstants.TDOD);
				long tdod = 0;
				if (d2 != null) {
					Object tdodo = d2.getValue();
					tdod = tdodo == null ? 0 : DataUtil.transformLong(tdodo);
				}

				fr = getFailRate(pvss + tdod, pv);
			}
		}
		return fr;
	}

	public double getFailRate(long blockNumber, long pv){
		double fr = -1;
		try {
			// pv是被除数，所以不能为0
			if (pv != 0) {
				fr = blockNumber / pv;
			}
		} catch (Exception e) {
			logger.error("", e);
			fr = -1;
		}
		return fr;
	}
	/////////////////////////////////////////////////////////////////////

	/**
	 *大盘图
	 * 
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getApps")
	public void getAppInfps(HttpServletResponse response,String company) throws Exception {

		List<String> appList = companyMap.get(company);

		if(appList == null){
			return ;
		}


		//获取全网的应用URl关系
		Map<String,String> urlMap = AppInfoAo.get().findAllAppUrlRelationMap();

		List<IndexEntry> indexList = new ArrayList<IndexEntry>();

		//获取异常信息
		Map<String,List<TimeDataInfo>> exceptionMap = commonService.querykeyDataForApps(appList, KeyConstants.EXCEPTION, PropConstants.E_TIMES);


		//遍历大盘页面
		for (String opsName : appList) {

			IndexEntry index = new IndexEntry();
			index.setAppName(opsName);
			List<TimeDataInfo> e = exceptionMap.get(opsName);
			if(e != null&&e.size()>0){
				index.setExceptionNum((int)e.get(0).getMainValue());
			}

			List<String> hostlist = CspCacheTBHostInfos.get().getIpsListByOpsName(opsName);


			AppInfoPo appPo =  AppInfoCache.getAppInfoByAppName(opsName);
			String appType = appPo.getAppType();

			try{
				//流量信息
				 DecimalFormat dcmFmt = new DecimalFormat("0.0");
				if ("pv".equalsIgnoreCase(appType)) {
					TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.PV,PropConstants.E_TIMES);

					Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.PV, PropConstants.E_TIMES);

					double rate = getFailRate(opsName,(int)tdi.getMainValue());
					Map<String, Long> refer = getReferInfo(opsName, urlMap,company);


					int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(opsName, 5);
					index.setAlarms(alarmcount);//目前没有

					double d = CapacityCache.get().getAppCapacity(appPo.getAppId());

					index.setCapcityRate((int)d);

					index.setFailurerate(rate);
					index.setFtime(tdi.getFtime());
					index.setMachines(hostlist.size());
					index.setPv((int)tdi.getMainValue());
					Integer pageSize = (Integer)tdi.getOriginalPropertyMap().get(PropConstants.P_SIZE);
					if(pageSize != null) {
						float f = pageSize;
						f /= 1024;
						index.setPageSize(dcmFmt.format(f));
					}
					
					Calendar cal = Calendar.getInstance();
					for(int i=0;i<5;i++){
						cal.add(Calendar.MINUTE, -1);
						String t = TimeUtil.formatTime(cal.getTime(), "HH:mm");
						Float qps = mapqps.get(t);
						if(qps != null){
							index.setQps(qps.intValue());
							break;
						}
					}

					index.setReferMap(refer);

					//基线对比

					String ftime = index.getFtime();

					index.setQps(index.getQps()/60);

					String pvRate = BaseLineCache.get().getScale(opsName,KeyConstants.PV, PropConstants.E_TIMES, ftime, index.getPv());

					String excpetionRate = BaseLineCache.get().getScale(opsName,KeyConstants.EXCEPTION, PropConstants.E_TIMES, ftime, index.getPv());
					index.setPvRate(pvRate);

					index.setExceptionRate(excpetionRate);
				}
				if ("center".equalsIgnoreCase(appType)) {
					TimeDataInfo tdi = commonService.querySingleRecentlyKeyData(opsName,KeyConstants.HSF_PROVIDER,PropConstants.E_TIMES);

					Map<String,Float> mapqps = commonService.queryAverageKeyDataByHost(opsName,KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

					Map<String, Long> hsfRefer = getHSFReferInfo(opsName,company);

					int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(opsName, 5);
					index.setAlarms(alarmcount);//目前没有

					double d = CapacityCache.get().getAppCapacity(appPo.getAppId());
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
							index.setQps(qps.intValue());
							break;
						}
					}

					index.setReferMap(hsfRefer);


					index.setQps(index.getQps()/60);

					String ftime = index.getFtime();

					String pvRate = BaseLineCache.get().getScale(opsName,KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, ftime, index.getPv());

					String excpetionRate = BaseLineCache.get().getScale(opsName,KeyConstants.EXCEPTION, PropConstants.E_TIMES, ftime, index.getExceptionNum());
					index.setPvRate(pvRate);
					index.setExceptionRate(excpetionRate);
				}
			}catch (Exception e1) {
				log.error("出错"+opsName,e1);
			}



			indexList.add(index);
		}
		writeJSONToResponseJSONArray(response, indexList);
	}


	/**
	 *@param total 总量 
	 * @return <关联的应用名,对应的refer流量> 1、如果没有数据返回null
	 */
	public Map<String, Long> getHSFReferInfo(String appName,String company) throws Exception {

		List<String> appList = companyMap.get(company);

		Map<String, Long> result = new HashMap<String, Long>();
		Map<String,List<TimeDataInfo>> map = commonService.querykeyDataForChild(appName, KeyConstants.HSF_REFER, PropConstants.E_TIMES);

		for(Map.Entry<String,List<TimeDataInfo>> entry:map.entrySet()){
			String key = entry.getKey().substring(KeyConstants.HSF_REFER.length()+1, entry.getKey().length());
			if(appList.contains(key)){
				if(entry.getValue().size()==1){
					result.put(key, (long)entry.getValue().get(0).getMainValue());
				}else if(entry.getValue().size()>1){
					result.put(key, (long)entry.getValue().get(1).getMainValue());
				}
			}
		}
		return result;
	}


	public Map<String, Long> getReferInfo(String appName,Map<String,String> urlMap,String company) throws Exception {

		List<String> appList = companyMap.get(company);

		Map<String, Long> referAppResultMap = new HashMap<String, Long>();

		Map<String,List<TimeDataInfo>> map = commonService.querykeyDataForChild(appName, KeyConstants.PV_REFER, PropConstants.E_TIMES);

		for(Map.Entry<String,List<TimeDataInfo>> entry:map.entrySet()){
			List<TimeDataInfo> list = entry.getValue();

			long pv = 0;
			String key = "";
			if(list.size() ==1){
				pv = (long)list.get(0).getMainValue();
				key = list.get(0).getKeyName();
			}else if(list.size() >1){
				pv = (long)list.get(1).getMainValue();
				key = list.get(1).getKeyName();
			}

			String app = urlMap.get(key);
			if(app != null){

				if(!appList.contains(app)){
					continue;
				}

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

	/**
	 * 单独为替分组做的一个方法
	 * @param appId
	 * @return
	 */
	@RequestMapping(params = "method=queryIndexTableForGroup")
	public ModelAndView queryIndexTableForGroup(int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String,Float> topCpuMap = commonService.queryAverageKeyDataByHost(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.CPU);
		Map<String,Float> topLoadMap = commonService.queryAverageKeyDataByHost(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.LOAD);
		Map<String,Float> jvmGcMap = commonService.queryAverageKeyDataByHost(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMGC);
		Map<String,Float> jvmMemoryMap = commonService.queryAverageKeyDataByHost(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMMEMORY);

		Map<String, IndexHSfTable> timeMap = new HashMap<String, IndexHSfTable>();

		//暂时只有后端应用
		List<TimeDataInfo> pvList = commonService.querySingleKeyData(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
		List<TimeDataInfo> blockList = commonService.querySingleKeyData(appInfo.getAppName(), KeyConstants.PV_BLOCK, PropConstants.PV_SS);

		Map<String, TimeDataInfo> pvMap = new HashMap<String, TimeDataInfo>();
		for(TimeDataInfo info : pvList) {
			pvMap.put(info.getFtime(), info);
		}

		Map<String, TimeDataInfo> blockMap = new HashMap<String, TimeDataInfo>();
		for(TimeDataInfo info : blockList) {
			blockMap.put(info.getFtime(), info);
		}

		List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appInfo.getAppName());
		int machineCount = ipList.size();
		if(machineCount == 0) {
			machineCount = 1;
			logger.info(appInfo.getAppName() + "在Diamond中存储的机器数位0");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			String fTime = sdf.format(cal.getTime());
			timeList.add(fTime);
			IndexHSfTable hsf = null;
			if(timeMap.containsKey(fTime)) {
				hsf = timeMap.get(fTime);
			} else {
				hsf = new IndexHSfTable();
				timeMap.put(fTime, hsf);
			}
			TimeDataInfo blockHsf = blockMap.get(fTime);
			if(blockHsf != null) {
				//后端应用只有ss阻挡，和统一表格一致，平均到机器
				hsf.setSsBlock(DataUtil.transformLong(blockHsf.getOriginalPropertyMap().get(PropConstants.PV_SS))/machineCount);
			}

			hsf.setCpu(DataUtil.transformDouble(topCpuMap.get(fTime)));
			hsf.setLoad(DataUtil.transformDouble(topLoadMap.get(fTime)));
			hsf.setGc(DataUtil.transformInt(jvmGcMap.get(fTime)));
			hsf.setMem(DataUtil.transformDouble(jvmMemoryMap.get(fTime)));

			TimeDataInfo info = pvMap.get(fTime);
			if(info != null) {
				Object eTime = info.getOriginalPropertyMap().get(PropConstants.E_TIMES);
				Object cTime = info.getOriginalPropertyMap().get(PropConstants.C_TIME);
				//单机
				hsf.setPv(DataUtil.transformLong(eTime)/machineCount);
				hsf.setRt(DataUtil.transformLong(cTime)/machineCount);	//时间采集为平均情况

				//全部
				hsf.setPvForSite(DataUtil.transformLong(eTime));
				hsf.setQps(DataUtil.transformLong(eTime)/(machineCount*60));
			}
		}

		ModelAndView view = new ModelAndView("/time/app_index_table_hsf_group");
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("timeMap", timeMap);
		view.addObject("machineCount", machineCount);
		return view;
	}

	@RequestMapping(params = "method=queryIndexTableForHSf")
	public ModelAndView queryIndexTableForHSf(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		Map<String,Map<String,TimeDataInfo>> pvmap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> topMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> jvmMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> blockMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.PV_BLOCK, PropConstants.PV_SS);

		Map<String, Map<String, IndexHSfTable>> siteMap = new HashMap<String, Map<String, IndexHSfTable>>();
		//处理cpu
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : topMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexHSfTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexHSfTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexHSfTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexHSfTable();
					timeMap.put(h.getKey(), hsf);
				}


				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.CPU);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.LOAD);
				if(c!= null)
					hsf.setCpu(DataUtil.transformDouble(c));
				if(l!= null)
					hsf.setLoad(DataUtil.transformDouble(l));
			}
		}

		//处理jvm
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : jvmMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexHSfTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexHSfTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexHSfTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexHSfTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.JVMGC);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.JVMMEMORY);
				if(c!= null)
					hsf.setGc(DataUtil.transformInt(c));
				if(l!= null)
					hsf.setMem(DataUtil.transformDouble(l));
			}
		}

		Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom(appInfo.getAppName());
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : pvmap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexHSfTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexHSfTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexHSfTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexHSfTable();
					timeMap.put(h.getKey(), hsf);
				}


				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.E_TIMES);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.C_TIME);
				if(c!= null) {
					int pv = DataUtil.transformInt(c);
					hsf.setPv(pv);
					List<HostPo> hostList = hostMap.get(hostsite); 
					if(hostList != null) {
						hsf.setPvForSite(pv*hostList.size());	//算机房的PV					
					}
				}
				if(l!= null)
					hsf.setRt(DataUtil.transformDouble(l));
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}


		//基线对比计算
		for(Map.Entry<String, Map<String, IndexHSfTable>> entry:siteMap.entrySet()){
			String siteName = entry.getKey();

			for(Map.Entry<String, IndexHSfTable> timeEntry:entry.getValue().entrySet()){
				String ftime = timeEntry.getKey();
				IndexHSfTable table = timeEntry.getValue();
				table.setQps((long)Arith.div(table.getPv(), 60, 1))	;

				String pvRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, ftime, table.getPv(), siteName);
				String rtRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.C_TIME, ftime, table.getRt(), siteName);
				String cpuRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.CPU, ftime, table.getCpu(), siteName);
				String loadRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.LOAD, ftime, table.getLoad(), siteName);
				String memRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMMEMORY, ftime, table.getMem(), siteName);
				String gcRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMGC, ftime, table.getGc(), siteName);
				table.setCpuRate(cpuRate);
				table.setGcRate(gcRate);
				table.setLoadRate(loadRate);
				table.setMemRate(memRate);
				table.setPvRate(pvRate);
				table.setRtRate(rtRate);
			}

		}

		//处理Block相关
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : blockMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexHSfTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexHSfTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexHSfTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexHSfTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.TDOD);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.PV_SS);
				if(c!= null)
					hsf.setTdodBlock(DataUtil.transformLong(c));
				if(l!= null)
					hsf.setSsBlock(DataUtil.transformLong(l));
				//后端应用，只有ss 流控
				hsf.setBlockRate(getFailRate(hsf.getSsBlock(), hsf.getPvForSite()));
			}
		}

		ModelAndView view = new ModelAndView("/time/app_index_table_hsf");
		view.addObject("siteMap", siteMap);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryIndexTableForPv")
	public ModelAndView queryIndexTableForPv(HttpServletRequest request, int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		Map<String,Map<String,TimeDataInfo>> pvmap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> topMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> jvmMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.E_TIMES);

		Map<String,Map<String,TimeDataInfo>> blockMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.PV_BLOCK, PropConstants.PV_SS);

		//Map<siteName, Map<ftime, IndexPvTable>>
		Map<String, Map<String, IndexPvTable>> siteMap = new HashMap<String, Map<String, IndexPvTable>>();

		//处理cpu
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : topMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());

				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.CPU);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.LOAD);
				if(c!= null)
					hsf.setCpu(DataUtil.transformDouble(c));
				if(l!= null)
					hsf.setLoad(DataUtil.transformDouble(l));
			}
		}

		//处理jvm
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : jvmMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.JVMGC);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.JVMMEMORY);
				if(c!= null)
					hsf.setGc(DataUtil.transformInt(c));
				if(l!= null)
					hsf.setMem(DataUtil.transformDouble(l));
			}
		}

		Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom(appInfo.getAppName());
		//处理pv
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : pvmap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}


				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.E_TIMES);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.C_TIME);
				Object t= h.getValue().getOriginalPropertyMap().get(PropConstants.C_200);
				if(c!= null) {
					int pv = DataUtil.transformInt(c);
					hsf.setPv(pv);
					List<HostPo> hostList = hostMap.get(hostsite); 
					if(hostList != null) {
						hsf.setPvForSite(pv*hostList.size());	//算机房的PV					
					}
				}
				if(l!= null)
					hsf.setRt(DataUtil.transformDouble(l));
				if(t!= null)
					hsf.setPv200(DataUtil.transformInt(t));
			}
		}

		//处理Block相关
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : blockMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.TDOD);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.PV_SS);
				if(c!= null)
					hsf.setTdodBlock(DataUtil.transformLong(c));
				if(l!= null)
					hsf.setSsBlock(DataUtil.transformLong(l));

				hsf.setBlockRate(getFailRate(hsf.getTdodBlock() + hsf.getSsBlock(), hsf.getPvForSite()));
			}
		}	

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}


		//基线对比计算
		for(Map.Entry<String, Map<String, IndexPvTable>> entry:siteMap.entrySet()){
			String siteName = entry.getKey();

			for(Map.Entry<String, IndexPvTable> timeEntry:entry.getValue().entrySet()){
				String ftime = timeEntry.getKey();
				IndexPvTable table = timeEntry.getValue();

				table.setQps((long)Arith.div(table.getPv(), 60, 1))	;

				String pvRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.PV, PropConstants.E_TIMES, ftime, table.getPv(), siteName);
				String pv200Rate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.PV, PropConstants.C_200, ftime, table.getPv200(), siteName);
				String rtRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.PV, PropConstants.C_TIME, ftime, table.getRt(), siteName);
				String cpuRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.CPU, ftime, table.getCpu(), siteName);
				String loadRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.TOPINFO, PropConstants.LOAD, ftime, table.getLoad(), siteName);
				String memRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMMEMORY, ftime, table.getMem(), siteName);
				String gcRate = BaseLineCache.get().getScaleBySite(appInfo.getAppName(), KeyConstants.JVMINFO, PropConstants.JVMGC, ftime, table.getGc(), siteName);
				table.setCpuRate(cpuRate);
				table.setGcRate(gcRate);
				table.setLoadRate(loadRate);
				table.setMemRate(memRate);
				table.setPv200Rate(pv200Rate);
				table.setPvRate(pvRate);
				table.setRtRate(rtRate);
			}

		}


		ModelAndView view = new ModelAndView("/time/app_index_table_pv");
		view.addObject("siteMap", siteMap);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}

	/**
	 * 专门显示失败率
	 * @param appId
	 * @return
	 */
	@RequestMapping(params = "method=queryIndexTableForBlock")
	public ModelAndView queryIndexTableForBlock(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String,Map<String,TimeDataInfo>> blockMap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), KeyConstants.PV_BLOCK, PropConstants.PV_SS);

		String queryKey = null;
		if(appInfo.getAppType().equalsIgnoreCase("pv")){
			queryKey = KeyConstants.PV;
		} else {
			queryKey = KeyConstants.HSF_PROVIDER;
		}

		Map<String,Map<String,TimeDataInfo>> pvmap = commonService.queryAverageKeyDataByHostForSite(appInfo.getAppName(), queryKey, PropConstants.E_TIMES);

		//Map<siteName, Map<ftime, IndexPvTable>>
		Map<String, Map<String, IndexPvTable>> siteMap = new HashMap<String, Map<String, IndexPvTable>>();

		Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom(appInfo.getAppName());
		//处理pv
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : pvmap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}
				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.E_TIMES);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.C_TIME);
				Object t= h.getValue().getOriginalPropertyMap().get(PropConstants.C_200);
				if(c!= null) {
					int pv = DataUtil.transformInt(c);
					hsf.setPv(pv);
					hsf.setQps(pv/60);
					List<HostPo> hostList = hostMap.get(hostsite); 
					if(hostList != null) {
						hsf.setPvForSite(pv*hostList.size());	//算机房的PV					
					}
				}
				if(l!= null)
					hsf.setRt(DataUtil.transformDouble(l));
				if(t!= null)
					hsf.setPv200(DataUtil.transformInt(t));
			}
		}

		//处理Block相关
		for (Map.Entry<String,Map<String,TimeDataInfo>> entry : blockMap.entrySet()) {
			String hostsite = entry.getKey();
			Map<String, IndexPvTable> timeMap = siteMap.get(hostsite);
			if (timeMap == null) {
				timeMap = new HashMap<String, IndexPvTable>();
				siteMap.put(hostsite, timeMap);
			}

			Map<String,TimeDataInfo> tmp = entry.getValue();
			for(Map.Entry<String,TimeDataInfo> h:tmp.entrySet()){
				IndexPvTable hsf = timeMap.get(h.getKey());
				if(hsf == null){
					hsf = new IndexPvTable();
					timeMap.put(h.getKey(), hsf);
				}

				Object c = h.getValue().getOriginalPropertyMap().get(PropConstants.TDOD);
				Object l= h.getValue().getOriginalPropertyMap().get(PropConstants.PV_SS);
				if(c!= null)
					hsf.setTdodBlock(DataUtil.transformLong(c));
				if(l!= null)
					hsf.setSsBlock(DataUtil.transformLong(l));

				hsf.setBlockRate(getFailRate(hsf.getTdodBlock() + hsf.getSsBlock(), hsf.getPv()));
			}
		}	

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}

		ModelAndView view = new ModelAndView("/time/block/app_index_table_block");
		view.addObject("siteMap", siteMap);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=showBlockHost")
	public ModelAndView showBlockHost(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>>  blockList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), KeyConstants.PV_BLOCK, PropConstants.PV_SS);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}

		ModelAndView view = new ModelAndView("/time/block/block_host_detail");
		view.addObject("blockList", blockList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		return view;
	}	

//	@RequestMapping(params = "method=tradeTcCreateIndex")
//	public ModelAndView tradeTcCreateIndex() {
//
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MINUTE,-1);
//		Date end = cal.getTime();
//
//		cal.add(Calendar.MINUTE,-15);
//		Date start = cal.getTime();
//
//		cal.add(Calendar.DAY_OF_MONTH,-7);
//		Date pStart = cal.getTime();
//
//		cal.add(Calendar.MINUTE,15);
//
//		Date pEnd =  cal.getTime();
//
//		Map<String,TradeVo> mapCreate100 = haBoDao.findTcCreateSumNew(start,end,100);
//		Map<String,TradeVo> mapCreate100p = haBoDao.findTcCreateSumNew(pStart,pEnd,100);
//
//
//		Map<String,TradeVo> mapCreate1 = haBoDao.findTcCreateSumNew(start,end,1);
//		Map<String,TradeVo> mapCreate1p = haBoDao.findTcCreateSumNew(pStart,pEnd,1);
//
//		//老请求已经废弃，现在查tp那边的tair
//
//		ModelAndView view =new ModelAndView("/time/trade_time_show");
//		view.addObject("mapCreate100", mapCreate100);
//		view.addObject("mapCreate100p", mapCreate100p);
//		view.addObject("mapCreate1", mapCreate1);
//		view.addObject("mapCreate1p", mapCreate1p);
//		return view;
//	} 
	
	@RequestMapping(params = "method=tradeTcCreateIndex")
	public ModelAndView tradeTcCreateIndex() {
		List<RealTimeTradePo> poList = getMultiRealTimeTradeByMinute();
		ModelAndView view =new ModelAndView("/time/trade_time_show");
		view.addObject("poList", poList);
		return view;
	} 

	private List<RealTimeTradePo> getMultiRealTimeTradeByMinute(){
		List<Future<RealTimeTradePo>> rtPoList = new ArrayList<Future<RealTimeTradePo>>();
		List<RealTimeTradePo> poList = new ArrayList<RealTimeTradePo>();

		final int HTTP_THREAD_COUNT = 10;
		try {
			ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
					5, HTTP_THREAD_COUNT, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));
			CountDownLatch doneSignal = new CountDownLatch(HTTP_THREAD_COUNT*3);

			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			Calendar now = Calendar.getInstance();
			for(int i=0; i<HTTP_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(now.getTime(), doneSignal)));
				now.add(Calendar.MINUTE, -1);
			}
			doneSignal.await();

			for (Future<RealTimeTradePo> fr : rtPoList) {  
				if (fr.isDone()) {  
					poList.add(fr.get());  
				} else {  
					logger.info("getMultiRealTimeTradeNow not ends,fr=" + fr.toString());
				}  
			}
			//关闭线程池
			threadPool.shutdown(); 
		} catch (Exception e) {
			logger.warn("getMultiRealTimeTradeNow error:", e);
		} 
		return poList;
	}
}
