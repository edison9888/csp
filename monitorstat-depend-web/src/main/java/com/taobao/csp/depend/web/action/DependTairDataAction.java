package com.taobao.csp.depend.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspAppTairConsumeDao;
import com.taobao.csp.depend.po.hsf.ProvideSiteRating;
import com.taobao.csp.depend.po.tair.ActionSummaryPo;
import com.taobao.csp.depend.po.tair.CExceptionUnit;
import com.taobao.csp.depend.po.tair.CMachineDistributeUnit;
import com.taobao.csp.depend.po.tair.CTairActionTotalUnit;
import com.taobao.csp.depend.po.tair.CTairMachineRoomUnit;
import com.taobao.csp.depend.po.tair.CTairNameSpaceUnit;
import com.taobao.csp.depend.po.tair.TairConsumeDetailPo;
import com.taobao.csp.depend.po.tair.TairConsumeMachine;
import com.taobao.csp.depend.po.tair.TairConsumeSummaryPo;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.MonitorTairAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.TairInfoPo;
import com.taobao.monitor.common.po.TairNamespacePo;
import com.taobao.monitor.common.po.TairSumData;
import com.taobao.monitor.common.util.Arith;

/**
 * 这个类主要统计应用依赖的tair的信息，从consume的维度统计
 * @author zhongting.zy
 */
@Controller
@RequestMapping("/show/tairconsume.do")
public class DependTairDataAction {
	
	private static final Logger logger =  Logger.getLogger(DependTairDataAction.class);
	
	@Resource(name = "cspAppTairConsumeDao")
	private CspAppTairConsumeDao cspAppTairConsumeDao;
	private static int NUM_OF_SCALE = 4;	//小数点精确位数
	private static int NUM_OF_SCALE_SHORT = 2;	//小数点精确位数
	/**
	 * Tair调用信息，首页显示
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showTairConsumeMain")
	public ModelAndView showTairConsumeMain(String opsName,String selectDate ){
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		
		//Detail 调用状况
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		List<TairConsumeDetailPo> detailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectDate);
		
		//如果Detail中没有数据，则返回没有数据
		if(detailList.size() == 0) {
			ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/noMessage");
			view.addObject("opsName", opsName);
			view.addObject("selectDate", selectDate);
			return view;
		}
		List<TairConsumeDetailPo> preDetailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectPreDate);
		
		long allCallNum = 0;
		Map<String, ActionSummaryPo> actionSummaryMap = new HashMap<String, ActionSummaryPo>();	//key-value <actiontypename, ActionSummaryPo>
		Map<String, TairConsumeSummaryPo> tairConsumeSummaryMap = new HashMap<String, TairConsumeSummaryPo>();	//key-value  <tair-group-name, TairConsumeSummaryPo>
		Map<String,ProvideSiteRating> provideSiteRatingMap = new HashMap<String,ProvideSiteRating>();	//key-value <siteName,ProvideSiteRating> 

		long preAllCallNum = 0;
		Map<String, ActionSummaryPo> preActionSummaryMap = new HashMap<String, ActionSummaryPo>();	
		Map<String, TairConsumeSummaryPo> preTairConsumeSummaryMap = new HashMap<String, TairConsumeSummaryPo>();
		Map<String,ProvideSiteRating> preProvideSiteRatingMap = new HashMap<String,ProvideSiteRating>();
		
		for(TairConsumeDetailPo detailPo: detailList) {
			
			//组织Action部分的数据
			String actionTypeName = detailPo.getAction_type();
			
			//下面的操作在findConsumeTairDetailList方法中实现了
//			if(actionTypeName == null) {	//过滤掉异常和带/的情况
//				continue;
//			} else {
//				actionTypeName = actionTypeName.trim().toLowerCase();
//				if(actionTypeName.indexOf("/") > 0 || actionTypeName.indexOf(" ") > 0 && actionTypeName.indexOf("exception") > 0) {
//					continue;
//				}
//			}
			
			if(!actionSummaryMap.containsKey(actionTypeName)) {
				ActionSummaryPo actionPo = new ActionSummaryPo();
				actionPo.setActionName(actionTypeName);
				actionPo.setCallAllNum(detailPo.getCallNum());
				actionSummaryMap.put(actionTypeName, actionPo);
			} else {
				actionSummaryMap.get(actionTypeName).setCallAllNum(actionSummaryMap.get(actionTypeName).getCallAllNum() + detailPo.getCallNum());
			}
			
			//组织TariGroup分组部分的数据
			String tairGroupName = detailPo.getTair_group_name();
			if(tairGroupName == null || "".equals(tairGroupName)) {
				tairGroupName = ConstantParameters.EMPTY_TAIR_GROUPNAME;
			}
			if(!tairConsumeSummaryMap.containsKey(tairGroupName)) {
				TairConsumeSummaryPo summaryPo = new TairConsumeSummaryPo();
				summaryPo.setTair_group_name(tairGroupName);
				summaryPo.setApp_name(opsName);
				summaryPo.setCollect_time(MethodUtil.getDate(selectDate));
				summaryPo.setInvoking_all_num(detailPo.getCallNum());
				summaryPo.setNamespace(detailPo.getNamespace());
				tairConsumeSummaryMap.put(tairGroupName, summaryPo);
			} else {
				tairConsumeSummaryMap.get(tairGroupName).setInvoking_all_num(tairConsumeSummaryMap.get(tairGroupName).getInvoking_all_num() 
						+ detailPo.getCallNum());
			}
			
			//组织机房的数据
			String siteName = detailPo.getApp_site_name();
			if(!provideSiteRatingMap.containsKey(siteName)) {
				ProvideSiteRating siteRating = new ProvideSiteRating();
				siteRating.setSiteName(siteName);
				siteRating.setCallAllNum(detailPo.getCallNum());
				provideSiteRatingMap.put(siteName, siteRating);
			} else {
				provideSiteRatingMap.get(siteName).setCallAllNum(provideSiteRatingMap.get(siteName).getCallAllNum() + detailPo.getCallNum());
			}
			
			//记录调用的总数
			allCallNum += detailPo.getCallNum();
		}
		
		//统计历史的信息
		for(TairConsumeDetailPo detailPo: preDetailList) {
			
			String actionTypeName = detailPo.getAction_type();
			if(actionTypeName == null) {	//过滤掉异常和带/的情况
				continue;
			} else {
				actionTypeName = actionTypeName.trim().toLowerCase();
				if(actionTypeName.indexOf("/") > 0 || actionTypeName.indexOf(" ") > 0 && actionTypeName.indexOf("exception") > 0) {
					continue;
				}
			}
			
			if(!preActionSummaryMap.containsKey(actionTypeName)) {
				ActionSummaryPo actionPo = new ActionSummaryPo();
				actionPo.setActionName(actionTypeName);
				actionPo.setCallAllNum(detailPo.getInvoking_num());
				preActionSummaryMap.put(actionTypeName, actionPo);
			} else {
				preActionSummaryMap.get(actionTypeName).setCallAllNum(preActionSummaryMap.get(actionTypeName).getCallAllNum() + detailPo.getCallNum());
			}
			
			//组织TariGroup分组部分的数据
			String tairGroupName = detailPo.getTair_group_name();
			if(tairGroupName == null || "".equals(tairGroupName)) {
				tairGroupName = ConstantParameters.EMPTY_TAIR_GROUPNAME;
			}
			if(!preTairConsumeSummaryMap.containsKey(tairGroupName)) {
				TairConsumeSummaryPo summaryPo = new TairConsumeSummaryPo();
				summaryPo.setTair_group_name(tairGroupName);
				summaryPo.setApp_name(opsName);
				summaryPo.setCollect_time(MethodUtil.getDate(selectDate));
				summaryPo.setInvoking_all_num(detailPo.getCallNum());
				summaryPo.setNamespace(detailPo.getNamespace());
				preTairConsumeSummaryMap.put(tairGroupName, summaryPo);
			} else {
				preTairConsumeSummaryMap.get(tairGroupName).setInvoking_all_num(preTairConsumeSummaryMap.get(tairGroupName).getInvoking_all_num() 
						+ detailPo.getCallNum());
			}
			
			//组织机房的数据
			String siteName = detailPo.getApp_site_name();
			if(!preProvideSiteRatingMap.containsKey(siteName)) {
				ProvideSiteRating siteRating = new ProvideSiteRating();
				siteRating.setSiteName(siteName);
				siteRating.setCallAllNum(detailPo.getCallNum());
				preProvideSiteRatingMap.put(siteName, siteRating);
			} else {
        preProvideSiteRatingMap.get(siteName).setCallAllNum(
            preProvideSiteRatingMap.get(siteName).getCallAllNum()
                + detailPo.getCallNum());
			}
			
			//记录调用的总数
			preAllCallNum += detailPo.getCallNum();
		}		

		//新老机房数据对比
		for(String siteName: provideSiteRatingMap.keySet()) {
			if(preProvideSiteRatingMap.containsKey(siteName)) {
				provideSiteRatingMap.get(siteName).setPreCallAllNum(preProvideSiteRatingMap.get(siteName).getCallAllNum());
			} else {
				provideSiteRatingMap.get(siteName).setOption(ConstantParameters.CONTROST_ADD);
			}
		}

		//action 调用状况
		List<ActionSummaryPo> actionList;
		try {
			actionList = new ArrayList<ActionSummaryPo>(actionSummaryMap.values());
			List<ActionSummaryPo> preActionList = new ArrayList<ActionSummaryPo>(preActionSummaryMap.values());
			compareActionTypeList(actionList, preActionList);
			Collections.sort(actionList);
		} catch (Exception e) {
			logger.error("", e);
			actionList = new ArrayList<ActionSummaryPo>();
		}
		
		//summary调用
		List<TairConsumeSummaryPo> tairConsumeSummaryList;
		try {
			compareGroupMap(tairConsumeSummaryMap, preTairConsumeSummaryMap);
			tairConsumeSummaryList = new ArrayList<TairConsumeSummaryPo>(tairConsumeSummaryMap.values());
			Collections.sort(tairConsumeSummaryList);
		} catch (Exception e) {
			logger.error("", e);
			tairConsumeSummaryList = new ArrayList<TairConsumeSummaryPo>();
		}
		
		//合并机房的信息,精简过数据的detailList
		detailList = combineDetailList(detailList);
		Collections.sort(detailList);
		
		preDetailList = combineDetailList(preDetailList);
		
		compareDetailList(detailList, preDetailList);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/tairInfoMain");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("provideSiteRatingMap", provideSiteRatingMap);	
		view.addObject("allCallNum", allCallNum + "");
		view.addObject("preAllCallNum", preAllCallNum + "");
		
		view.addObject("actionList", actionList);
		view.addObject("tairConsumeSummaryList", tairConsumeSummaryList);
		view.addObject("detailList",detailList);				
		
		return view;
	}	
	
	/**
	 * 根据tairgroupname显示详细的信息
	 * @param opsName
	 * @param selectDate
	 * @param tairgroupname
	 * @return
	 */
	@RequestMapping(params = "method=showTairByTairGroupName")
	public ModelAndView showTairByTairGroupName(String opsName,String selectDate, String tairgroupname){
	
		//对tairgroupname做一下转换
		if(tairgroupname == null || ConstantParameters.EMPTY_TAIR_GROUPNAME.equals(tairgroupname)) {
			tairgroupname = "";
		}
		
		//group by actiontype,namespace,app_site_name
		List<TairConsumeDetailPo> detailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectDate, tairgroupname);
		Map<String, CTairActionTotalUnit> totalMap = getMapByList(detailList);
		computeMap(totalMap);	//计算map中的各项值
		
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		
		List<TairConsumeDetailPo> preDetailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectPreDate, tairgroupname);
		Map<String, CTairActionTotalUnit> preTotalMap = getMapByList(preDetailList);
		computeMap(preTotalMap);
		
		compareCTairActionTotalUnitMap(totalMap, preTotalMap);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/namespaceall");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("selectPreDate",selectPreDate);	
		view.addObject("tairgroupname",tairgroupname);				
		view.addObject("totalMap",totalMap);				
		return view;
	}
	
	/**
	 * 显示总计的全部信息
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showSummaryAll")
	public ModelAndView showSummaryAll(String opsName,String selectDate ){
		
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);		
		
		//summary调用，为null的tairgroupname在findConsumeTairSummary中已经做转换
		Map<String, TairConsumeSummaryPo> tairSummaryMap = cspAppTairConsumeDao.findConsumeTairSummary(opsName, selectDate);
		if(tairSummaryMap.values() == null || tairSummaryMap.values().size() == 0) {
			ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/noMessage");
			view.addObject("opsName", opsName);
			view.addObject("selectDate", selectDate);
			return view;
		}
				
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		
		Map<String, TairConsumeSummaryPo> preTairSummaryMap = cspAppTairConsumeDao.findConsumeTairSummary(opsName, selectPreDate);
		compareGroupMap(tairSummaryMap, preTairSummaryMap);
		List<TairConsumeSummaryPo> tairConsumeSummaryList = new ArrayList(tairSummaryMap.values());		
		Collections.sort(tairConsumeSummaryList);
		
		long allCallNum = 0;
		for(TairConsumeSummaryPo app:tairConsumeSummaryList){
			allCallNum += app.getInvoking_all_num();
		}
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/tairsummaryall");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("allCallNum", allCallNum  + "");
		view.addObject("tairConsumeSummaryList", tairConsumeSummaryList);
		
		return view;
	}
	
	/**
	 * 显示详细统计的全部
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showDetailAll")
	public ModelAndView showDetailAll(String opsName,String selectDate ){
		
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		
		List<TairConsumeDetailPo> detailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectDate);
		List<TairConsumeDetailPo> preDetailList = cspAppTairConsumeDao.findConsumeTairDetailList(opsName, selectPreDate);
		
		
		//合并机房的信息,精简过数据的detailList
		detailList = combineDetailList(detailList);
		preDetailList = combineDetailList(preDetailList);		
		compareDetailList(detailList, preDetailList);
		Collections.sort(detailList);
		long detailTotal = 0;
		for(TairConsumeDetailPo po: detailList) {
			detailTotal += po.getCallNum();
		}
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/tairdetailall");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("detailTotal", detailTotal  + "");
		view.addObject("detailList", detailList);
		
		return view;
	}
	
	/**
	 * 显示某一Action下面的机器分布
	 * @param opsName
	 * @param selectDate
	 * @param tairgroupname
	 * @param namespace
	 * @param actiontype
	 * @return
	 */
	@RequestMapping(params = "method=showMachine")
	public ModelAndView showMachine(String opsName,String selectDate,String tairgroupname,String namespace, String actiontype){
		
		List<TairConsumeMachine> list = cspAppTairConsumeDao.findMachineByGroupAndNameSpace(opsName, selectDate, 
				tairgroupname, namespace, actiontype);
		//key-value <machineIp,CMachineDistributeUnit>
		Map<String,CMachineDistributeUnit> machineDisMap = computeMachineList(list);
		
		double avgHit, avgLength, avgRate;
		long[] callNumberArray = new long[3], timeArray = new long[3];
		Map<String, Long> siteMap = new HashMap<String, Long>();
		
		for(CMachineDistributeUnit machine: machineDisMap.values()) {
			//统计机房的信息
			if(!siteMap.containsKey(machine.getSiteName())) {
				siteMap.put(machine.getSiteName(), machine.callNumberArray[ConstantParameters.INT_TYPE_NOR]);
			} else {
				siteMap.put(machine.getSiteName(), siteMap.get(machine.getSiteName()) + machine.callNumberArray[ConstantParameters.INT_TYPE_NOR]);
			}
			//统计总数 命中率 长度等信息
			callNumberArray[ConstantParameters.INT_TYPE_NOR] += machine.callNumberArray[ConstantParameters.INT_TYPE_NOR];
			callNumberArray[ConstantParameters.INT_TYPE_HIT] += machine.callNumberArray[ConstantParameters.INT_TYPE_HIT];
			callNumberArray[ConstantParameters.INT_TYPE_LEN] += machine.callNumberArray[ConstantParameters.INT_TYPE_LEN];
			
			timeArray[ConstantParameters.INT_TYPE_NOR] += machine.timeArray[ConstantParameters.INT_TYPE_NOR];
			timeArray[ConstantParameters.INT_TYPE_HIT] += machine.timeArray[ConstantParameters.INT_TYPE_HIT];
			timeArray[ConstantParameters.INT_TYPE_LEN] += machine.timeArray[ConstantParameters.INT_TYPE_LEN];
			
		}
		avgRate = Arith.div(timeArray[ConstantParameters.INT_TYPE_NOR], callNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT);
		avgHit = Arith.div(timeArray[ConstantParameters.INT_TYPE_HIT], callNumberArray[ConstantParameters.INT_TYPE_HIT], NUM_OF_SCALE);
		avgLength = Arith.div(timeArray[ConstantParameters.INT_TYPE_LEN], callNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT);
		
		//历史数据信息
		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);
		List<TairConsumeMachine> preList = cspAppTairConsumeDao.findMachineByGroupAndNameSpace(opsName, selectPreDate, 
				tairgroupname, namespace, actiontype);		
		Map<String,CMachineDistributeUnit> preMachineDisMap = computeMachineList(preList);
		
		double preAvgHit, preAvgLength, preAvgrate;
		long[] preCallNumberArray = new long[3], preTimeArray = new long[3];
		Map<String, Long> preSiteMap = new HashMap<String, Long>();
		
		for(CMachineDistributeUnit machine: preMachineDisMap.values()) {
			//统计机房的信息
			if(!preSiteMap.containsKey(machine.getSiteName())) {
				preSiteMap.put(machine.getSiteName(), machine.callNumberArray[ConstantParameters.INT_TYPE_NOR]);
			} else {
				preSiteMap.put(machine.getSiteName(), preSiteMap.get(machine.getSiteName()) + machine.callNumberArray[ConstantParameters.INT_TYPE_NOR]);
			}
			//统计总数 命中率 长度等信息
			preCallNumberArray[ConstantParameters.INT_TYPE_NOR] += machine.callNumberArray[ConstantParameters.INT_TYPE_NOR];
			preCallNumberArray[ConstantParameters.INT_TYPE_HIT] += machine.callNumberArray[ConstantParameters.INT_TYPE_HIT];
			preCallNumberArray[ConstantParameters.INT_TYPE_LEN] += machine.callNumberArray[ConstantParameters.INT_TYPE_LEN];
			
			preTimeArray[ConstantParameters.INT_TYPE_NOR] += machine.timeArray[ConstantParameters.INT_TYPE_NOR];
			preTimeArray[ConstantParameters.INT_TYPE_HIT] += machine.timeArray[ConstantParameters.INT_TYPE_HIT];
			preTimeArray[ConstantParameters.INT_TYPE_LEN] += machine.timeArray[ConstantParameters.INT_TYPE_LEN];
			
		}
		preAvgrate = Arith.div(preTimeArray[ConstantParameters.INT_TYPE_NOR], preCallNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT);
		preAvgHit = Arith.div(preTimeArray[ConstantParameters.INT_TYPE_HIT], preCallNumberArray[ConstantParameters.INT_TYPE_HIT], NUM_OF_SCALE);
		preAvgLength = Arith.div(preTimeArray[ConstantParameters.INT_TYPE_LEN], preCallNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT);		
		
		//对比历史数据
		for(String machineIp: machineDisMap.keySet()) {
			if(!preMachineDisMap.containsKey(machineIp)) {
				continue;
			}
			CMachineDistributeUnit preDisUnit = preMachineDisMap.get(machineIp);
			CMachineDistributeUnit curDisUnit = machineDisMap.get(machineIp);
			
			curDisUnit.setPreAvgHit(preDisUnit.getAvgHit());
			curDisUnit.setPreAvgLength(preDisUnit.getAvgLength());
			curDisUnit.setPreAvgrate(preDisUnit.getAvgRate());
			curDisUnit.setPreCallNumberArray(preDisUnit.getCallNumberArray());
			curDisUnit.setPreTimeArray(preDisUnit.getTimeArray());
		}
		
		List<CMachineDistributeUnit> machineList = null;
		if(machineDisMap.values() != null) {
			machineList = new ArrayList<CMachineDistributeUnit>(machineDisMap.values());
			Collections.sort(machineList);
		} else {
			machineList = new ArrayList<CMachineDistributeUnit>();
		}
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/machinedetail");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("selectPreDate", selectPreDate);
		view.addObject("tairgroupname", tairgroupname);
		view.addObject("namespace", namespace);
		view.addObject("actiontype", actiontype);
		
		view.addObject("machineList", machineList);
		view.addObject("siteMap", siteMap);
		view.addObject("totalNumber", callNumberArray[ConstantParameters.INT_TYPE_NOR] + "");
		view.addObject("avgRate", avgRate + "");
		view.addObject("avgHit", avgHit + "");
		view.addObject("avgLength", avgLength + "");

		view.addObject("preSiteMap", preSiteMap);
		view.addObject("preTotalNumber", preCallNumberArray[ConstantParameters.INT_TYPE_NOR] + "");
		view.addObject("preAvgrate", preAvgrate + "");
		view.addObject("preAvgHit", preAvgHit + "");
		view.addObject("preAvgLength", preAvgLength + "");
		
		return view;
	}	
	
	@RequestMapping(params = "method=showExceptionDetail")
	public ModelAndView showExceptionDetail(String opsName,String selectDate) {
		
		Map<String, CExceptionUnit> exceptionMap = cspAppTairConsumeDao.findExceptionDetail(opsName, selectDate);

		Date predate = MethodUtil.getPreDate(selectDate);
		final String selectPreDate = MethodUtil.getStringOfDate(predate);		
		
		Map<String, CExceptionUnit> preExceptionMap = cspAppTairConsumeDao.findExceptionDetail(opsName, selectPreDate);
		
		long bigTotal = 0;		//异常的总个数
		long preBigTotal = 0;	//历史异常个数
		//机房中数据的总数
		Map<String, Long> siteMap = new HashMap<String, Long>();
		for(CExceptionUnit unit: exceptionMap.values()) {
			long total = 0;
			//遍历机房,计算每一个机房异常的总数
			for(String machineName: unit.machineData.keySet()) {
				if(!siteMap.containsKey(machineName)) {
					siteMap.put(machineName, unit.machineData.get(machineName));
				} else {
					siteMap.put(machineName, siteMap.get(machineName) + unit.machineData.get(machineName));
				}
				total += unit.machineData.get(machineName);
				bigTotal += unit.machineData.get(machineName);
			}
			unit.setTotal(total);
		}
		
		//历史异常信息
		Map<String, Long> preSiteMap = new HashMap<String, Long>();
		for(CExceptionUnit unit: preExceptionMap.values()) {
			long total = 0;
			//遍历机房,计算每一个机房异常的总数
			for(String machineName: unit.machineData.keySet()) {
				if(!preSiteMap.containsKey(machineName)) {
					preSiteMap.put(machineName, unit.machineData.get(machineName));
				} else {
					preSiteMap.put(machineName, preSiteMap.get(machineName) + unit.machineData.get(machineName));
				}
				total += unit.machineData.get(machineName);
				preBigTotal += unit.machineData.get(machineName);
			}
			unit.setTotal(total);
		}		
		
		//对比Exception历史数据
		for(String exceptionName: exceptionMap.keySet()) {
			if(!preExceptionMap.containsKey(exceptionName)) {
				continue;
			}
			CExceptionUnit preUnit = preExceptionMap.get(exceptionName);
			Map<String, Long> preMapDetail = preUnit.machineData;
			for(String machineName: exceptionMap.get(exceptionName).machineData.keySet()) {
				if(!preMapDetail.containsKey(machineName)) {
					continue;
				}
				exceptionMap.get(exceptionName).preMachineData.put(machineName, preMapDetail.get(machineName));
			}
			exceptionMap.get(exceptionName).setPreTotal(preUnit.getTotal());
		}
		
		List<CExceptionUnit> exceptionList = null;	//所有异常的数据
		if(exceptionMap.values() != null)
			exceptionList = new ArrayList<CExceptionUnit>(exceptionMap.values());
		else 
			exceptionList = new ArrayList<CExceptionUnit>();
		Collections.sort(exceptionList);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tair/consume/exceptiondetail");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("selectPreDate", selectPreDate);
		view.addObject("exceptionList", exceptionList);
		view.addObject("siteMap", siteMap);
		view.addObject("preSiteMap", preSiteMap);
		view.addObject("bigTotal", bigTotal + "");
		view.addObject("preBigTotal", preBigTotal + "");
		
		return view;
	}
	
	/**
	 * 通过GroupName合并callnum和precallnum
	 * @param list
	 * @return
	 */
	private ArrayList<TairConsumeSummaryPo> getUniqueConsumeSummaryPoList(List<TairConsumeSummaryPo> list) {
		HashMap<String, TairConsumeSummaryPo> map = new HashMap();
		for(TairConsumeSummaryPo po: list) {
			String name = po.getTair_group_name(); 
			if(name == null || "".equals(name)) {
				name = ConstantParameters.EMPTY_TAIR_GROUPNAME;
				po.setTair_group_name(name);
			}
			TairConsumeSummaryPo temp = map.get(name); 
			if(temp == null) {
				map.put(name, po);
			} else {
				temp.setPrecall(temp.getPrecall() + po.getPrecall());
				temp.setInvoking_all_num(temp.getInvoking_all_num() + po.getInvoking_all_num());
			}
		}
		
		if(map.values() != null) {
			return  new ArrayList<TairConsumeSummaryPo>(map.values());
		} else {
			return  new ArrayList<TairConsumeSummaryPo>();
		}
	}
	
	/**
	 * 计算各个机房的占有率，并标记preCall属性
	 * @return
	 */
	private Map<String,ProvideSiteRating> comSiteRating(List<TairConsumeSummaryPo> appConsumerList,List<TairConsumeSummaryPo> preAppConsumerList){
		
		Map<String,ProvideSiteRating> map = new HashMap<String, ProvideSiteRating>();
		Map<String,ProvideSiteRating> preMap = new HashMap<String, ProvideSiteRating>();
		
		//根据数据库规则解析机房的信息
		translateRoomFeatureToMap(map, appConsumerList, true);
		translateRoomFeatureToMap(preMap, preAppConsumerList, false);
		
		for(Map.Entry<String,ProvideSiteRating> entry:map.entrySet()){
			if(preMap.get(entry.getKey())==null){
				entry.getValue().setOption(ConstantParameters.CONTROST_ADD);
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}	
		
		return map;
	}
	
	/**
	 * 对比TairGroup总计数据的信息，把历史调用数据写入
	 * @param appConsumerList
	 * @param preAppConsumerList
	 */
	public static void compareGroupMap(Map<String, TairConsumeSummaryPo> tairConsumeSummaryMap, Map<String, TairConsumeSummaryPo> preTairConsumeSummaryMap){
		for(String tairGroupname: tairConsumeSummaryMap.keySet()) {
			if(preTairConsumeSummaryMap.containsKey(tairGroupname)) {
				tairConsumeSummaryMap.get(tairGroupname).setPrecall(preTairConsumeSummaryMap.get(tairGroupname).getInvoking_all_num());
			}
		}
	}
	
	/**
	 * 根据csp_tair_provide_app_summary中,room_feature的规则来解析机房的信息
	 * @param map
	 * @param appConsumerList
	 * @param flag true表示现在的,false 表示过去的
	 */
	private void translateRoomFeatureToMap(final Map<String,ProvideSiteRating> map, List<TairConsumeSummaryPo> appConsumerList, boolean flag) {
		for(TairConsumeSummaryPo app:appConsumerList){
			String room_feature = app.getRoom_feature();
			if(room_feature == null) {
				continue;
			}
			String[] outestArray = room_feature.split("#");
			for(String outestString: outestArray) {
				String[] outerArray = outestString.split("\\$");	//java中$ 特殊字符
				String[] array = outerArray[0].split(":");
				ProvideSiteRating sum = map.get(array[0]);
				if(sum == null){
					sum = new ProvideSiteRating();
					sum.setSiteName(array[0]);
					map.put(array[0], sum);
				}
				if(flag) {
					sum.setCallAllNum(sum.getCallAllNum() + Long.parseLong(array[1]));
				} else {
					sum.setPreCallAllNum(sum.getPreCallAllNum() + Long.parseLong(array[1]));
				}
			}
		}
	}
	
	/**
	 * 为ActionType增加历史调用信息
	 * @param actionList
	 * @param preActionList
	 * @return
	 */
	private void compareActionTypeList(final List<ActionSummaryPo> actionList, final List<ActionSummaryPo> preActionList) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		for(ActionSummaryPo po: preActionList) {
			 map.put(po.getActionName(), po.getCallAllNum());
		}
		
		for(ActionSummaryPo po: actionList) {
			if(map.get(po.getActionName()) != null) {
				po.setPreCallAllNum(map.get(po.getActionName()));
			} else {
				po.setOption(ConstantParameters.CONTROST_ADD);
			}
		}
	}
	
	/**
	 * 对比Detail中tair调用前几名的数据
	 * @param detailList
	 * @param preDetailList
	 */
	private void compareDetailList(final List<TairConsumeDetailPo> detailList, final List<TairConsumeDetailPo> preDetailList) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		for(TairConsumeDetailPo po: preDetailList) {
			map.put(getKeyOfTairConsumeDetailPo(po), po.getCallNum());
		}
		
		for(TairConsumeDetailPo po: detailList) {
			
			if(map.get(getKeyOfTairConsumeDetailPo(po)) != null) {
				po.setPreCallNum(map.get(getKeyOfTairConsumeDetailPo(po)));
			} else {
				po.setOption(ConstantParameters.CONTROST_ADD);
			}
		}
	}
	
	/**
	 * 根据tair_groupname_namespace生成hashmap的主键,_actiontype-sitename暂时不用
	 * @param po
	 * @return
	 */
	private String getKeyOfTairConsumeDetailPo(TairConsumeDetailPo po) {
		return new StringBuilder(po.getTair_group_name()).append("-").append(po.getNamespace())
				.append("-").append(po.getAction_type()).toString();	//.append("-").append(po.getApp_site_name())
	}
	
	/**
	 * 遍历csp_app_consume_tair_detail的查询结果，把结果组织到需要CTairActionTotalUnit结构的map中
	 * @param detailList
	 * @return
	 */
	private Map<String, CTairActionTotalUnit> getMapByList(List<TairConsumeDetailPo> detailList) {
		
		Map<String, CTairActionTotalUnit> map = new HashMap<String, CTairActionTotalUnit>();
		
		for(TairConsumeDetailPo tcdPo : detailList) {
			int type = 0;
			String name = tcdPo.getAction_type();
			if(name == null)
				continue;
			
			String[] nameArray = name.split("/");
			if(nameArray.length > 1) {
				//记录当前的类型
				if(ConstantParameters.STRING_TYPE_HIT.equals(nameArray[1])) {
					type = ConstantParameters.INT_TYPE_HIT;	
				} else if(ConstantParameters.STRING_TYPE_LEN.equals(nameArray[1])) {
					type = ConstantParameters.INT_TYPE_LEN;
				} else {
					//非hit 和 len 类型均过滤掉
					continue;
				}
			} else {

				name = name.trim().toLowerCase();
				//过滤异常情况和以空格分割的action_type的情况
				if(name.indexOf(" ") > 0 || name.indexOf("exception") > 0) {
					continue;
				}
				type = ConstantParameters.INT_TYPE_NOR;
			}
			
			if(!map.containsKey(nameArray[0])) {
				map.put(nameArray[0], new CTairActionTotalUnit(nameArray[0]));
			}
			
			Map<String, CTairMachineRoomUnit> machineMap = map.get(nameArray[0]).machineMap;
			if(!machineMap.containsKey(tcdPo.getApp_site_name())) {
				machineMap.put(tcdPo.getApp_site_name(), new CTairMachineRoomUnit(tcdPo.getApp_site_name()));
			}
			
			Map<String, CTairNameSpaceUnit> namespaceMap = machineMap.get(tcdPo.getApp_site_name()).namespaceMap;
			if(!namespaceMap.containsKey(tcdPo.getNamespace())) {
				namespaceMap.put(tcdPo.getNamespace(), new CTairNameSpaceUnit(tcdPo.getNamespace()));
			}
			
			CTairNameSpaceUnit ctnsuPo = namespaceMap.get(tcdPo.getNamespace());
			switch(type) {
				case ConstantParameters.INT_TYPE_NOR:
					ctnsuPo.setAvgRate(Arith.div(tcdPo.getInvoking_time(), tcdPo.getCallNum(), NUM_OF_SCALE_SHORT));
					break;
				case ConstantParameters.INT_TYPE_HIT:
					ctnsuPo.setAvgHit(Arith.div(tcdPo.getInvoking_time(), tcdPo.getCallNum(), NUM_OF_SCALE));
					break;
				case ConstantParameters.INT_TYPE_LEN:
					ctnsuPo.setAvgLength(Arith.div(tcdPo.getInvoking_time(), tcdPo.getCallNum(), NUM_OF_SCALE_SHORT));
					break;
			}
			//记录总数，理论上这个地方不存在再加一次的可能，因为不会有重复的namespace
			ctnsuPo.callNumberArray[type] += tcdPo.getCallNum(); 
			ctnsuPo.timeArray[type] += tcdPo.getInvoking_time(); 
		}
		return map;
	}
	
	/**
	 * 统计map中的数据，统计总数和一些均值。对namespaceMap进行排序
	 * @param map
	 */
	private void computeMap(Map<String, CTairActionTotalUnit> map) {
		for(CTairActionTotalUnit ctatuPo: map.values()) {
			
			long[] callNumberArray = new long[3], callTimeArray = new long[3];	
			
			for(CTairMachineRoomUnit machinePo: ctatuPo.machineMap.values()) {
			
				long[] machineCallNumberArray = new long[3], machineCallTimeArray = new long[3];	
				for(CTairNameSpaceUnit spacePo: machinePo.namespaceMap.values()) {
					for(int i=0; i<machineCallNumberArray.length; i++) {
						machineCallNumberArray[i] += spacePo.callNumberArray[i];
						machineCallTimeArray[i] += spacePo.timeArray[i];
					}
				}
				
				machinePo.setCallNumberArray(machineCallNumberArray);
				machinePo.setTimeArray(machineCallTimeArray);
				
				for(int i=0; i<callNumberArray.length; i++) {
					callNumberArray[i] += machineCallNumberArray[i];
					callTimeArray[i] += machineCallTimeArray[i];
				}		
				//界面上没有显示，需要的话，可以做扩展
				machinePo.setAvgRate(Arith.div(machineCallTimeArray[ConstantParameters.INT_TYPE_NOR], machineCallNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT));
				machinePo.setAvgHit(Arith.div(machineCallTimeArray[ConstantParameters.INT_TYPE_HIT], machineCallNumberArray[ConstantParameters.INT_TYPE_HIT],NUM_OF_SCALE));
				machinePo.setAvgLength(Arith.div(machineCallTimeArray[ConstantParameters.INT_TYPE_LEN], machineCallNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT));
			}
			
			ctatuPo.setCallNumberArray(callNumberArray);
			ctatuPo.setTimeArray(callTimeArray);
			
			ctatuPo.setAvgRate(Arith.div(callTimeArray[ConstantParameters.INT_TYPE_NOR], callNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT));
			ctatuPo.setAvgHit(Arith.div(callTimeArray[ConstantParameters.INT_TYPE_HIT], callNumberArray[ConstantParameters.INT_TYPE_HIT], NUM_OF_SCALE));
			ctatuPo.setAvgLength(Arith.div(callTimeArray[ConstantParameters.INT_TYPE_LEN], callNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT));
		}
	}
	
	/**
	 * 比较两个CTairActionTotalUnit，并在curMap中做一些标记
	 * @param map
	 * @param preMap
	 */
	private void compareCTairActionTotalUnitMap(Map<String, CTairActionTotalUnit> map, Map<String, CTairActionTotalUnit> preMap) {
		for(CTairActionTotalUnit ctatuPo: map.values()) {
			
			long[] preCallNumberArray = new long[3], preCallTimeArray = new long[3];	
			
			if(!preMap.containsKey(ctatuPo.getActionShortName())) {
				continue;
			}
			Map<String, CTairMachineRoomUnit> preMachineMap = preMap.get(ctatuPo.getActionShortName()).machineMap;
			
			for(CTairMachineRoomUnit machinePo: ctatuPo.machineMap.values()) {
				
				if(!preMachineMap.containsKey(machinePo.getMachineRoomName())) {
					continue;
				}
				long[] preMachineCallNumberArray = new long[3], preMachineCallTimeArray = new long[3];
				Map<String, CTairNameSpaceUnit> preNameSpaceMap = preMachineMap.get(machinePo.getMachineRoomName()).namespaceMap;
				
				for(CTairNameSpaceUnit spacePo: machinePo.namespaceMap.values()) {
					if(!preNameSpaceMap.containsKey(spacePo.getNamespace())) {
						continue;
					}
					//填入同期对比的数据
					CTairNameSpaceUnit preSpacePo = preNameSpaceMap.get(spacePo.getNamespace());
					spacePo.setPreAvgHit(preSpacePo.getAvgHit());
					spacePo.setPreAvgLength(preSpacePo.getAvgLength());
					spacePo.setPreAvgrate(preSpacePo.getAvgRate());
					spacePo.preCallNumberArray = preSpacePo.callNumberArray;
					spacePo.preTimeArray = preSpacePo.timeArray;
					
					for(int i=0; i<preMachineCallNumberArray.length; i++) {
						preMachineCallNumberArray[i] += preSpacePo.callNumberArray[i];
						preMachineCallTimeArray[i] += preSpacePo.timeArray[i];
					}
				}
				
				machinePo.setPreCallNumberArray(preMachineCallNumberArray);
				machinePo.setPreTimeArray(preMachineCallTimeArray);
				
				for(int i=0; i<preCallNumberArray.length; i++) {
					preCallNumberArray[i] += preMachineCallNumberArray[i];
					preCallTimeArray[i] += preMachineCallTimeArray[i];
				}		
				machinePo.setPreAvgrate(Arith.div(preMachineCallTimeArray[ConstantParameters.INT_TYPE_NOR], preMachineCallNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT));
				machinePo.setPreAvgHit(Arith.div(preMachineCallTimeArray[ConstantParameters.INT_TYPE_HIT], preMachineCallNumberArray[ConstantParameters.INT_TYPE_HIT], NUM_OF_SCALE));
				machinePo.setPreAvgLength(Arith.div(preMachineCallTimeArray[ConstantParameters.INT_TYPE_LEN], preMachineCallNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT));
			}
			
			ctatuPo.setPreCallNumberArray(preCallNumberArray);
			ctatuPo.setPreTimeArray(preCallTimeArray);
			
			ctatuPo.setPreAvgrate((Arith.div(preCallTimeArray[ConstantParameters.INT_TYPE_NOR], preCallNumberArray[ConstantParameters.INT_TYPE_NOR], NUM_OF_SCALE_SHORT)));
			ctatuPo.setPreAvgHit(Arith.div(preCallTimeArray[ConstantParameters.INT_TYPE_HIT], preCallNumberArray[ConstantParameters.INT_TYPE_HIT], NUM_OF_SCALE));
			ctatuPo.setPreAvgLength(Arith.div(preCallTimeArray[ConstantParameters.INT_TYPE_LEN], preCallNumberArray[ConstantParameters.INT_TYPE_LEN], NUM_OF_SCALE_SHORT));
		}
	}
	
	/**
	 * 根据机器分布,计算出对应的信息
	 * @param list
	 * @return
	 */
	private Map<String, CMachineDistributeUnit> computeMachineList(List<TairConsumeMachine> list) {
		Map<String, CMachineDistributeUnit> map = new HashMap<String, CMachineDistributeUnit>();
		for(TairConsumeMachine machine: list) {
			if(!map.containsKey(machine.getConsumeMachineIp())) {
				map.put(machine.getConsumeMachineIp(), new CMachineDistributeUnit(machine.getConsumeMachineIp()));
			}
			CMachineDistributeUnit cmdUnit = map.get(machine.getConsumeMachineIp());
			if(machine.getActionType() == null)
				continue;
			String actionType = machine.getActionType();
			
			String[] nameArray = actionType.split("/");
			if(nameArray.length > 1) {
				//记录当前的类型
				if(ConstantParameters.STRING_TYPE_HIT.equals(nameArray[1])) {
					cmdUnit.setAvgHit(Arith.div(machine.getCalltime(), machine.getCallnum(), NUM_OF_SCALE));
					cmdUnit.callNumberArray[ConstantParameters.INT_TYPE_HIT] = machine.getCallnum();
					cmdUnit.timeArray[ConstantParameters.INT_TYPE_HIT] = machine.getCalltime();
					
				} else if(ConstantParameters.STRING_TYPE_LEN.equals(nameArray[1])) {
					cmdUnit.setAvgLength(Arith.div(machine.getCalltime(), machine.getCallnum(), NUM_OF_SCALE_SHORT));
					cmdUnit.callNumberArray[ConstantParameters.INT_TYPE_LEN] = machine.getCallnum();  
					cmdUnit.timeArray[ConstantParameters.INT_TYPE_LEN] = machine.getCalltime();
				}
			} else {
				actionType = actionType.trim().toLowerCase();
				
				/*
				 * 查询的时候，值查询hit、length、avage，没有exception的情况 
				 * 
				//过滤异常情况和以空格分割的action_type的情况
				if(actionType.indexOf(" ") > 0 || actionType.indexOf("exception") > 0) {
					continue;
				}
				*/
				cmdUnit.setAvgRate(Arith.div(machine.getCalltime(), machine.getCallnum(), NUM_OF_SCALE_SHORT));
				cmdUnit.callNumberArray[ConstantParameters.INT_TYPE_NOR] = machine.getCallnum();
				cmdUnit.timeArray[ConstantParameters.INT_TYPE_NOR] = machine.getCalltime();
			}
			cmdUnit.setSiteName(machine.getConsumeMachineCm());	//纪录机房名称
		}
		return map;
	}
	
	/**
	 * 把分散的machinelist合并成一条数据
	 * @param detailList
	 * @return
	 */
	private ArrayList<TairConsumeDetailPo> combineDetailList(List<TairConsumeDetailPo> detailList) {
		Map<String, TairConsumeDetailPo> detailMapNew = new HashMap<String, TairConsumeDetailPo>();
		TairConsumeDetailPo.siteName.clear();
		for(TairConsumeDetailPo po: detailList) {
			//记录机房的名字
			TairConsumeDetailPo.siteName.add(po.getApp_site_name());
			String key = getKeyOfTairConsumeDetailPo(po);
			if(!detailMapNew.containsKey(key)) {
				po.siteMap.put(po.getApp_site_name(), po.getCallNum());
				detailMapNew.put(key, po);
			} else {
				detailMapNew.get(key).siteMap.put(po.getApp_site_name(), po.getCallNum());
				detailMapNew.get(key).setCallNum(detailMapNew.get(key).getCallNum() + po.getCallNum());
			}
		}
		return new ArrayList<TairConsumeDetailPo>(detailMapNew.values());
	}
	
	 @RequestMapping(params = "method=showTopTair")
	 public ModelAndView showTopTair(String groupName, String collectTime, String top){
	   if(collectTime == null) {
	     Date date = MethodUtil.getDate(collectTime);
	     collectTime = MethodUtil.getStringOfDate(date);
	   }
	   if (top == null) {
	     top = "20";
	   }
	   
	   if (groupName == null) {
	     groupName = "";
	   }
	   
	   //下拉列表
	   List<String> groupList = MonitorTairAo.getInstance().findGroupList(collectTime);
	   groupList = MethodUtil.getSortedList(groupList); 

	   Map<String, TairSumData> appTairDataMap = MonitorTairAo.getInstance().findTairProviderTopApp(groupName, collectTime);
	   List<Map.Entry<String, TairSumData>> appTairDataMapList = new ArrayList<Map.Entry<String, TairSumData>>(appTairDataMap.entrySet());
	   Comparator<Map.Entry<String, TairSumData>> compare = new Comparator<Map.Entry<String, TairSumData>>(){
	     public int compare(Map.Entry<String, TairSumData> o1, Map.Entry<String, TairSumData> o2) {
	       if (o2.getValue().getAppCallSum() > o1.getValue().getAppCallSum()) {
	         return 1;
	       } else if (o2.getValue().getAppCallSum() == o1.getValue().getAppCallSum()) {
	         return 0;
	       } else {
	         return -1;
	       }
	     }
	   };
	   Collections.sort(appTairDataMapList, compare);
	   
     // 此处查询这个group被总共调用了多少次
     long sumCountInTairGroup = 0l; 
     for(Map.Entry<String, TairSumData> entry : appTairDataMap.entrySet()) {
       sumCountInTairGroup += entry.getValue().getAppCallSum();
     }
	   top = appTairDataMapList.size() + "";
	   int limit ;
	   if (appTairDataMapList.size() < Integer.parseInt(top)) {
	     limit = appTairDataMapList.size();
	   } else {
	     limit = Integer.parseInt(top);
	   }
	   
	   //如果Detail中没有数据，则返回没有数据
	   ModelAndView view = new ModelAndView("/depend/summarypages/tairtopsummary");
	   view.addObject("groupName", groupName);
	   view.addObject("collectTime", collectTime);
	   view.addObject("groupList", groupList);
	   view.addObject("appTairDataMapList", appTairDataMapList);
	   view.addObject("top", top);
	   view.addObject("limit", limit + "");
	   view.addObject("sumCountInTairGroup", sumCountInTairGroup + "");
	   return view;
	 }
	 
	 @RequestMapping(params = "method=gotoShowTairDetail")
   public ModelAndView gotoShowTairDetail(String groupName, String collectTime, String namespace, String appName){
	   String errorMsg = "";
	   AppInfoPo appInfopo = AppInfoAo.get().getAppInfoByAppName(appName);
	   Map<String, List<TairInfoPo>> ipTairMap = null;
	   if (appInfopo == null) {
	     logger.error("请求的应用不正确！");
	     errorMsg = "请求的应用不正确！";
	     ipTairMap = new HashMap<String, List<TairInfoPo>>();
	   } else {
	     ipTairMap = MonitorTairAo.getInstance().findTairInfoByNamespace(appInfopo.getOpsName(),groupName,namespace,collectTime);  
	   }
	   
     //如果Detail中没有数据，则返回没有数据
     ModelAndView view = new ModelAndView("/depend/summarypages/tair_detail");
     view.addObject("groupName", groupName);
     view.addObject("collectTime", collectTime);
     view.addObject("namespace", namespace);
     view.addObject("appName", appName);
     view.addObject("errorMsg", errorMsg);
     view.addObject("ipTairMap", ipTairMap);
     return view;
	 }
}
