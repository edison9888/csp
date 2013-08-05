package com.taobao.csp.time.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.taobao.csp.time.cache.CapacityCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.IndexEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
@Controller
@RequestMapping("/indexother.do")
/*首页大盘图 */
public class IndexOtherController extends BaseController {

	private static final Logger logger = Logger.getLogger(IndexOtherController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	private static Map<String,List<String>> companyMap = new HashMap<String, List<String>>();

	static {
		companyMap.put("data_company", Arrays.asList("tcif","buyerstory","taobaoindex","cubeweb","cubesearch", "itier",
				"nodefox","hbaserest","andes","galaxyfirsttime","cubeorder"));
	}

	public static final Logger log = Logger.getLogger(IndexOtherController.class);

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
	 * 监控大盘
	 */
	@RequestMapping(params = "method=showDataIndex")
	public ModelAndView showDataIndex() {
		List<String> appList = companyMap.get("data_company");
		Map<String, Integer[]> capacityMap = new HashMap<String, Integer[]>();//opsname, [machinenum, maxqps]

		ModelAndView view = new ModelAndView("indexother/data_index_graph");
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
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getDataAppInfo")
	public void getDataAppInfo(HttpServletResponse response,String company) throws Exception {

		//		List<String> appList = companyMap.get(company);
		//		
		//		if(appList == null){
		//			return ;
		//		}

		List<String> appList = companyMap.get("data_company");
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

					double rate = 0;

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

					double rate = 0;

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
		writeJSONToResponseJSONArray(response, indexList);
	}	
}
