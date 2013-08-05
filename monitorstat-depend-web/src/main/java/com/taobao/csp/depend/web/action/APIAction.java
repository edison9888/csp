package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taobao.csp.depend.ao.UrlAo;
import com.taobao.csp.depend.dao.CspAppHsfDependConsumeDao;
import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.dao.CspAppTairConsumeDao;
import com.taobao.csp.depend.dao.CspCheckupDependDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.hsf.AppConsumerInterfaceSummary;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppProvderInterfaceSummary;
import com.taobao.csp.depend.po.hsf.HsfProvideMachine;
import com.taobao.csp.depend.po.hsf.InterfaceSummary;
import com.taobao.csp.depend.po.tair.TairConsumeSummaryPo;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.MonitorDayAo;
import com.taobao.monitor.common.db.impl.other.TcReportDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValuePo;

@Controller
@RequestMapping("/show/dependapi.do")
public class APIAction extends BaseAction {
	private static final Logger logger = Logger.getLogger(APIAction.class);

	@Resource(name = "cspAppTairConsumeDao")
	private CspAppTairConsumeDao cspAppTairConsumeDao;

	@Resource(name = "cspAppHsfDependConsumeDao")
	private CspAppHsfDependConsumeDao cspAppHsfDependConsumeDao;

	@Resource(name = "cspAppHsfDependProvideDao")
	private CspAppHsfDependProvideDao cspAppHsfDependProvideDao;

	@Resource(name = "cspCheckupDependDao")
	private CspCheckupDependDao cspCheckupDependDao;

	@Resource(name = "cspDependentDao")
	private CspDependentDao cspDependentDao;

	@Resource(name = "tcReportDao")
	private TcReportDao tcReportDao;	

	/*
	 * 根据interfaceName返回调用此接口的Appname
	 */
	@RequestMapping(params = "method=getConsumeAppByInterface")
	public void getConsumeAppByInterface(HttpServletResponse response,
			String opsName, String interfaceName, String selectDate) {

		Set<String> set = new HashSet<String>();
		if (opsName != null && interfaceName != null && selectDate != null) {
			List<AppConsumerSummary> list = cspAppHsfDependProvideDao
					.sumOneHsfInterfaceToAppConsumer(opsName, interfaceName, selectDate);
			for(AppConsumerSummary summary: list) {
				set.add(summary.getOpsName());
			}
		}
		JSONArray array = JSONArray.fromObject(set);
		this.writeResponse(array.toString(), response);
	}

	/**
	 * 获取某一天所有的App
	 * @param selectDate
	 */
	@RequestMapping(params = "method=getDistinctApp")
	public void getDistinctApp(HttpServletResponse response, String selectDate) {
		Set<String> set = cspDependentDao.findDistinctAppFromAppDependApp(selectDate);
		JSONArray array = JSONArray.fromObject(set);
		this.writeResponse(array.toString(), response);
	}

	/**
	 * 获取某一个App的所有依赖数据
	 * @param selectDate
	 */
	@RequestMapping(params = "method=getAppDependAppInfo")
	public void getAppDependAppInfo(HttpServletResponse response, String selectDate, String opsName) {
		List<AppDepApp> list = cspDependentDao.findAppDepend(opsName,  MethodUtil.getDate(selectDate));
		JSONArray array = JSONArray.fromObject(list);
		this.writeResponse(array.toString(), response);
	}

	/**
	 * 根据日期获取某个Provider应用的所有机器当前的与性能状况 
	 * @param selectDate
	 * @param opsName
	 */
	@RequestMapping(params = "method=getProviderAppMachineAll")
	public void getProviderAppMachineAll(HttpServletResponse response, String selectDate, String opsName) {
		List<HsfProvideMachine>  list = cspAppHsfDependProvideDao.getProviderAppMachineAll(opsName,selectDate);
		JSONArray array = JSONArray.fromObject(list);
		this.writeResponse(array.toString(), response);
	}  

	/**
	 * 根据keyid值获日报的数据，isCallBack==true时，callback参数有效
	 * @param keyid
	 * @param appName
	 * @param start
	 * @param end
	 * @param callback
	 * @param isCallBack
	 * @param response
	 */
	@RequestMapping(params = "method=queryTotalByKeyCallBack")
	public void queryTotalByKeyCallBack(int keyid, String appName, String start, String end, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		AppInfoPo info = AppInfoAo.get().getAppInfoByOpsName(appName);
		if(info != null) {
			List<KeyValuePo> keyValuePoList = MonitorDayAo.get().findMonitorCountByDate(info.getAppDayId(), keyid,
					MethodUtil.getBeginOfDay(start),MethodUtil.getEndOfDay(end));
			for(KeyValuePo po:keyValuePoList) {
				String timeStr = MethodUtil.getStringOfDate(po.getCollectTime());
				if(!map.containsKey(timeStr)) {
					map.put(timeStr, po.getValueStr());					
				} else {
					try {
						Long value = Long.parseLong(map.get(timeStr))
								+ Long.parseLong(po.getValueStr());
						map.put(timeStr, value + "");
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			}
		}
		JSONArray array = JSONArray.fromObject(map);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}

		//Map<String,TradeVo> mapCreate100 = haBoDao.findTcCreateSumNew(start,end,100);

		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping(params = "method=queryTotalByKeyWithoutApp")
	public void queryTotalByKeyWithoutApp(int keyid, String start, String end, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		List<KeyValuePo> keyValuePoList = MonitorDayAo.get().findMonitorCountAllByDate(keyid,
				MethodUtil.getBeginOfDay(start),MethodUtil.getEndOfDay(end));
		for(KeyValuePo po:keyValuePoList) {
			String timeStr = MethodUtil.getStringOfDate(po.getCollectTime());
			if(timeStr != null && timeStr.length()>0) {
				if(!map.containsKey(timeStr)) {
					map.put(timeStr, po.getValueStr());					
				} else {
					try {
						Long value = Long.parseLong(map.get(timeStr))
								+ Long.parseLong(po.getValueStr());
						map.put(timeStr, value + "");
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			}
		}
		JSONArray array = JSONArray.fromObject(map);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}

		//Map<String,TradeVo> mapCreate100 = haBoDao.findTcCreateSumNew(start,end,100);

		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}	

	@RequestMapping(params = "method=queryTradeDataCallBack")
	public void queryTradeDataCallBack(String start, String end, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, Long> tradeMap = tcReportDao.sumAllamount1(start, end);
		JSONArray array = JSONArray.fromObject(tradeMap);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}
		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}	

	/**
	 * 求总PV
	 * @param start
	 * @param end
	 * @param urlType null 表示 url， 可选值 url or domain
	 * @param callback
	 * @param isCallBack
	 * @param response
	 */
	@RequestMapping(params = "method=queryPVCallBack")
	public void queryPVCallBack(String start, String end, String urlType, String callback, 
			String isCallBack, HttpServletResponse response) {
		if(urlType == null || urlType.trim().equals(""))
			urlType = UrlAo.URL_TYPE_URL;
		Map<String, Long> pvMap = UrlAo.get().getPV(start, end, urlType);
		JSONArray array = JSONArray.fromObject(pvMap);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}
		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 求总PV
	 * @param start
	 * @param end
	 * @param urlType null 表示 url， 可选值 url or domain
	 * @param url
	 * @param callback
	 * @param isCallBack
	 * @param response
	 */
	@RequestMapping(params = "method=queryPVByUrlCallBack")
	public void queryPVByUrlCallBack(String start, String end, String urlType, String url, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, Long> pvMap = UrlAo.get().getPVByUrl(start, end, urlType, url);
		JSONArray array = JSONArray.fromObject(pvMap);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}
		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}	
	
	/**
	 * 返回UV总数
	 * @param start yyyy-MM-dd
	 * @param end	yyyy-MM-dd
	 * @param urlType null 表示 url， 可选值 url or domain
	 * @param callback	回调函数名称 
	 * @param isCallBack	是否回调
	 * @param response
	 */
	@RequestMapping(params = "method=queryUVCallBack")
	public void queryUVCallBack(String start, String end, String urlType, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, Long> uvMap = UrlAo.get().getUV(start, end, urlType);
		JSONArray array = JSONArray.fromObject(uvMap);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}
		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}	

	/**
	 * 返回UV总数,根据url查询,精确查询
	 * @param start yyyy-MM-dd
	 * @param end	yyyy-MM-dd
	 * @param urlType null 表示 url， 可选值 url or domain
	 * @param url 
	 * @param callback	回调函数名称 
	 * @param isCallBack	是否回调
	 * @param response
	 */
	@RequestMapping(params = "method=queryUVByUrlCallBack")
	public void queryUVByUrlCallBack(String start, String end, String urlType, String url, String callback, 
			String isCallBack, HttpServletResponse response) {
		Map<String, Long> uvMap = UrlAo.get().getUVByUrl(start, end, urlType, url);
		JSONArray array = JSONArray.fromObject(uvMap);
		String strReturn = "";
		boolean isCallBackBoolean = false;
		if(isCallBack != null) {
			isCallBackBoolean = Boolean.parseBoolean(isCallBack.toLowerCase());
		}
		if(isCallBackBoolean) {
			strReturn = addCallBackString(array.toString(), callback);	
		} else {
			strReturn = array.toString();
		}
		try {
			this.writeToResponse(response, strReturn);
		} catch (IOException e) {
			logger.error("", e);
		}
	}		
	
	/**
	 * 如果方法有回调，添加回调的方法名
	 * @param origin
	 * @param callback
	 * @return
	 */
	private String addCallBackString(String origin, String callback) {
		String strReturn = callback + "(";
		strReturn += origin;
		strReturn += ")";
		return strReturn;
	}
	
	/**
	 * 根据opsname返回所有被调用的接口,老接口，不带调用量，老接口
	 */
	@RequestMapping(params = "method=getProvideInterfaceByOpsName")
	public void getProvideInterfaceByOpsName(HttpServletResponse response,
			String opsName, String selectDate) {

		Set<String> set = new HashSet<String>();
		if (opsName != null && selectDate == null) {
			List<AppProvderInterfaceSummary> appProvderInterfaceList = cspAppHsfDependProvideDao
					.sumAppProviderInterfaceToCenterApp(opsName, selectDate);

			for(AppProvderInterfaceSummary sum: appProvderInterfaceList) {
				//String keyName = MethodUtil.simplifyHsfInterfaceName(sum.getInterfaceName());
				if(sum.getInterfaceName() != null && sum.getInterfaceName().indexOf("Exception_") >-1)
					continue;
				set.add(sum.getInterfaceName());
			}      
		}
		JSONArray array = JSONArray.fromObject(set);
		this.writeResponse(array.toString(), response);
	}	
	/***********************给博一的几个接口*******************************************/
	/**
	 * 根据type返回相应的应用。type: center, pv
	 * @param type
	 */
	@RequestMapping(params = "method=getCspAppListByType")
	public void getCspAppListByType(String type, HttpServletResponse response) {
		Set<String> set = new HashSet<String>();
		if(type != null) {
			type = type.trim().toLowerCase();
			if("pv".equals(type) || "center".equals(type)) {
				 List<AppInfoPo> appInfoList = AppInfoAo.get().findAllDayApp();
				 Iterator<AppInfoPo> iter = appInfoList.iterator();
				 while(iter.hasNext()) {
					 AppInfoPo app = iter.next();
					 if(app.getAppType() == null || !app.getAppType().trim().equals(type)) {
						 iter.remove();
					 } else {
						 set.add(app.getOpsName());
					 }
				 }
			}			
		}
		try {
			this.writeJSONToResponseJSONArray(response, set);
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 返回接口和当前的提供量
	 */
	@RequestMapping(params = "method=getProvideInterfaceAndNumberByOpsName")
	public void getProvideInterfaceAndNumberByOpsName(HttpServletResponse response,
			String opsName, String selectDate) {
		List<InterfaceSummary> appProvderInterfaceList = new ArrayList<InterfaceSummary>();
		if (opsName != null && selectDate != null) {
			List<AppProvderInterfaceSummary> appConsumerList = cspAppHsfDependProvideDao
					.sumAppProviderInterfaceToCenterApp(opsName, selectDate);
			//sumAppProviderInterfaceToCenterApp 接口做到了机器级别的汇总。还需要做一些接口级别的汇总
		    Map<String,InterfaceSummary> map = new HashMap<String, InterfaceSummary>();
		    for(AppProvderInterfaceSummary app : appConsumerList){
		      InterfaceSummary sum = map.get(app.getInterfaceName());
		      if(sum == null){
		        sum = new InterfaceSummary();
		        sum.setName(app.getInterfaceName());
		        sum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
		        map.put(app.getInterfaceName(), sum);
		      }
		      sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
		    }			
		    appProvderInterfaceList = new ArrayList<InterfaceSummary>(map.values());
		}
		try {
			this.writeJSONToResponseJSONArray(response, appProvderInterfaceList);
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 返回接口和当前的提供量
	 */
	@RequestMapping(params = "method=getConsumeInterfaceListByInterface")
	public void getConsumeInterfaceListByInterface(HttpServletResponse response,
			String opsName, String interfaceName, String selectDate) {
		List<AppConsumerSummary> list = new ArrayList<AppConsumerSummary>();
		Map<String, AppConsumerSummary> map = new HashMap<String, AppConsumerSummary>();
		if (opsName != null && selectDate != null && interfaceName != null) {
			if(!interfaceName.startsWith("IN_HSF-ProviderDetail_"))
				interfaceName = "IN_HSF-ProviderDetail_" + interfaceName;
			list = cspAppHsfDependProvideDao.sumOneHsfInterfaceToAppConsumer(opsName,interfaceName,selectDate);
			for(AppConsumerSummary summary : list) {
				if(!map.containsKey(summary.getOpsName())) {
					map.put(summary.getOpsName(), summary);
				} else {
					long callnum = summary.getCallAllNum() + map.get(summary.getOpsName()).getCallAllNum();
					summary.setCallAllNum(callnum);
					map.put(summary.getOpsName(), summary);
				}
			}
		}
		try {
			this.writeJSONToResponseJSONArray(response, map.values());
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	/**
	 * 需求 by 玄鸣
	 */
	@RequestMapping(params = "method=getMyHsfConsume")
	public void getMyHsfConsume(HttpServletResponse response, String opsName, String selectDate) {
		List<AppConsumerInterfaceSummary>appProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, selectDate);
		Date predate = MethodUtil.getPreDate(selectDate);
		List<AppConsumerInterfaceSummary> preAppProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, MethodUtil.getStringOfDate(predate));
		
		Map<String,InterfaceSummary> interfaceMap =  DependHsfConsumeDataAction.comInterface(appProvderInterfaceList,preAppProvderInterfaceList);
		
		ArrayList<InterfaceSummary> tmp = new ArrayList<InterfaceSummary>();
		tmp.addAll(interfaceMap.values());
		Collections.sort(tmp);
		String jsonString = com.alibaba.fastjson.JSONArray.toJSONString(tmp);
		try {
			this.writeJSONToResponse(response, jsonString);
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	@RequestMapping(params = "method=getMyTairConsume")
	public void getMyTairConsume(HttpServletResponse response, String opsName, String selectDate) {

		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);		
		
		//summary调用，为null的tairgroupname在findConsumeTairSummary中已经做转换
		Map<String, TairConsumeSummaryPo> tairSummaryMap = cspAppTairConsumeDao.findConsumeTairSummary(opsName, selectDate);
				
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		
		Map<String, TairConsumeSummaryPo> preTairSummaryMap = cspAppTairConsumeDao.findConsumeTairSummary(opsName, selectPreDate);
		DependTairDataAction.compareGroupMap(tairSummaryMap, preTairSummaryMap);
		List<TairConsumeSummaryPo> tairConsumeSummaryList = new ArrayList(tairSummaryMap.values());		
		Collections.sort(tairConsumeSummaryList);
		String jsonString = com.alibaba.fastjson.JSONArray.toJSONString(tairConsumeSummaryList);
		try {
			this.writeJSONToResponse(response, jsonString);
		} catch (IOException e) {
			logger.error("",e);
		}
	}
}
