package com.taobao.csp.depend.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspTddlConsumeDao;
import com.taobao.csp.depend.po.hsf.ProvideSiteRating;
import com.taobao.csp.depend.po.tddl.ConsumeDbDetail;
import com.taobao.csp.depend.po.tddl.MainResultPo;
import com.taobao.csp.depend.service.impl.TDDLServiceImpl;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.csp.depend.util.SQLPreParser;

@Controller
@RequestMapping("/show/tddlconsume.do")
public class DependTddlDataAction {
	
	private static final Logger logger =  Logger.getLogger(DependTddlDataAction.class);
	
	@Resource(name = "cspTddlConsumeDao")
	private CspTddlConsumeDao cspTddlConsumeDao;
	
	@RequestMapping(params = "method=showTddlConsumeMain")
	public ModelAndView showTddlConsumeMain(String opsName,String selectDate ){
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		
		//Detail 调用状况
		Date predate = MethodUtil.getPreDate(selectDate);
		//final String selectPreDate = MethodUtil.getStringOfDate(predate);
		List<ConsumeDbDetail> detailListRow = cspTddlConsumeDao.findTddlConsumeDetail(opsName, selectDate);
		
		HashMap<String, ProvideSiteRating> machineMap = new HashMap<String, ProvideSiteRating>();
		long totalNum = 0;
		long sqlSuccessNum = 0;
		long timeOutNum = 0;
		
		HashMap<String, ConsumeDbDetail> detailMap = new HashMap<String, ConsumeDbDetail>();
		for(ConsumeDbDetail detail: detailListRow) {
			String executeType = detail.getExecuteType();
			if(executeType.equals("EXECUTE_A_SQL_SUCCESS")) {
				sqlSuccessNum += detail.getExecuteSum(); 
			} else if(executeType.equals("EXECUTE_A_SQL_TIMEOUT")) {
				timeOutNum += detail.getExecuteSum(); 
			} else {
				continue;
			}
			
			String sqlText = detail.getSqlText();
			if(!detailMap.containsKey(sqlText)) {
				detailMap.put(sqlText, detail);
			} else {
				ConsumeDbDetail po =  detailMap.get(sqlText);
				po.setExecuteSum(detail.getExecuteSum() + po.getExecuteSum());
			}
			
			String appSiteName = detail.getAppSiteName();
			ConsumeDbDetail po =  detailMap.get(sqlText);
			
			if(!po.getSiteMap().containsKey(appSiteName)) {
				po.getSiteMap().put(appSiteName, detail.getExecuteSum());
			} else {
				po.getSiteMap().put(appSiteName, detail.getExecuteSum() + po.getExecuteSum());
			}
			
			if(!machineMap.containsKey(appSiteName)) {
				ProvideSiteRating machinePo = new ProvideSiteRating();
				machinePo.setSiteName(appSiteName);
				machinePo.setCallAllNum(detail.getExecuteSum());
				machineMap.put(appSiteName, machinePo);
			} else {
				ProvideSiteRating machinePo = machineMap.get(appSiteName);
				machinePo.setCallAllNum(detail.getExecuteSum() + machinePo.getCallAllNum());
				machineMap.put(appSiteName, machinePo);
			}
		}
		
		totalNum = sqlSuccessNum + timeOutNum;
		ArrayList<ConsumeDbDetail> detailList = null;
		if(detailMap.values().size() > 0) {
			detailList = new ArrayList<ConsumeDbDetail> (detailMap.values());	
		} else {
			detailList = new ArrayList<ConsumeDbDetail>();
		}
		Collections.sort(detailList);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tddl/consume/tddlmainpage");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("detailList", detailList);
		view.addObject("totalNum", totalNum + "");
		view.addObject("sqlSuccessNum", sqlSuccessNum + "");
		view.addObject("timeOutNum", timeOutNum + "");
		view.addObject("machineMap", machineMap);
		return view;		
	}
	@RequestMapping(params = "method=showTddlAll")
	public ModelAndView showTddlAll(String opsName,String selectDate ){
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		
		//Detail 调用状况
		Date predate = MethodUtil.getPreDate(selectDate);
		//final String selectPreDate = MethodUtil.getStringOfDate(predate);
		List<ConsumeDbDetail> detailListRow = cspTddlConsumeDao.findTddlConsumeDetail(opsName, selectDate);
		
		HashMap<String, ProvideSiteRating> machineMap = new HashMap<String, ProvideSiteRating>();
		long totalNum = 0;
		long sqlSuccessNum = 0;
		long timeOutNum = 0;
		
		HashMap<String, ConsumeDbDetail> detailMap = new HashMap<String, ConsumeDbDetail>();
		for(ConsumeDbDetail detail: detailListRow) {
			String executeType = detail.getExecuteType();
			if(executeType.equals("EXECUTE_A_SQL_SUCCESS")) {
				sqlSuccessNum += detail.getExecuteSum(); 
			} else if(executeType.equals("EXECUTE_A_SQL_TIMEOUT")) {
				timeOutNum += detail.getExecuteSum(); 
			} else {
				continue;
			}
			
			String sqlText = detail.getSqlText();
			if(!detailMap.containsKey(sqlText)) {
				detailMap.put(sqlText, detail);
			} else {
				ConsumeDbDetail po =  detailMap.get(sqlText);
				po.setExecuteSum(detail.getExecuteSum() + po.getExecuteSum());
			}
			
			String appSiteName = detail.getAppSiteName();
			ConsumeDbDetail po =  detailMap.get(sqlText);
			
			if(!po.getSiteMap().containsKey(appSiteName)) {
				po.getSiteMap().put(appSiteName, detail.getExecuteSum());
			} else {
				po.getSiteMap().put(appSiteName, detail.getExecuteSum() + po.getExecuteSum());
			}
			
			if(!machineMap.containsKey(appSiteName)) {
				ProvideSiteRating machinePo = new ProvideSiteRating();
				machinePo.setSiteName(appSiteName);
				machinePo.setCallAllNum(detail.getExecuteSum());
				machineMap.put(appSiteName, machinePo);
			} else {
				ProvideSiteRating machinePo = machineMap.get(appSiteName);
				machinePo.setCallAllNum(detail.getExecuteSum() + machinePo.getCallAllNum());
				machineMap.put(appSiteName, machinePo);
			}
		}
		
		totalNum = sqlSuccessNum + timeOutNum;
		ArrayList<ConsumeDbDetail> detailList = null;
		if(detailMap.values().size() > 0) {
			detailList = new ArrayList<ConsumeDbDetail> (detailMap.values());	
		} else {
			detailList = new ArrayList<ConsumeDbDetail>();
		}
		Collections.sort(detailList);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tddl/consume/tddldetailall");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("detailList", detailList);
		view.addObject("totalNum", totalNum + "");
		view.addObject("sqlSuccessNum", sqlSuccessNum + "");
		view.addObject("timeOutNum", timeOutNum + "");
		view.addObject("machineMap", machineMap);
		return view;		
	}	
	
	@RequestMapping(params = "method=showTddlByMachine")
	public ModelAndView showTddlByMachine(String opsName,String selectDate, String executeType, String sqlText) {
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		
		//Detail 调用状况
		Date predate = MethodUtil.getPreDate(selectDate);
		//final String selectPreDate = MethodUtil.getStringOfDate(predate);
		List<ConsumeDbDetail> detailListRow = cspTddlConsumeDao.findTddlConsumeDetailMachine(opsName, selectDate, executeType, sqlText);
		
		long totalNum = 0;
		long sqlSuccessNum = 0;
		long timeOutNum = 0;
		
		HashMap<String, ConsumeDbDetail> detailMap = new HashMap<String, ConsumeDbDetail>();
		for(ConsumeDbDetail detail: detailListRow) {
			String executeTypeTmp = detail.getExecuteType();
			if(executeTypeTmp.equals("EXECUTE_A_SQL_SUCCESS")) {
				sqlSuccessNum += detail.getExecuteSum(); 
			} else if(executeTypeTmp.equals("EXECUTE_A_SQL_TIMEOUT")) {
				timeOutNum += detail.getExecuteSum(); 
			} else {
				continue;
			}
			
			String sqlTextTmp = detail.getSqlText();
			if(!detailMap.containsKey(sqlTextTmp)) {
				detailMap.put(sqlTextTmp, detail);
			} else {
				ConsumeDbDetail po =  detailMap.get(sqlTextTmp);
				po.setExecuteSum(detail.getExecuteSum() + po.getExecuteSum());
			}
		}
		
		totalNum = sqlSuccessNum + timeOutNum;
		ArrayList<ConsumeDbDetail> detailList = null;
		if(detailMap.values().size() > 0) {
			detailList = new ArrayList<ConsumeDbDetail> (detailMap.values());	
		} else {
			detailList = new ArrayList<ConsumeDbDetail>();
		}
		Collections.sort(detailList);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/tddl/consume/machinedetail");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("predate", predate);
		view.addObject("detailList", detailList);
		view.addObject("totalNum", totalNum + "");
		view.addObject("sqlSuccessNum", sqlSuccessNum + "");
		view.addObject("timeOutNum", timeOutNum + "");
		view.addObject("sqlText", sqlText);
		view.addObject("executeType", executeType);
		
		return view;				
	}
}
