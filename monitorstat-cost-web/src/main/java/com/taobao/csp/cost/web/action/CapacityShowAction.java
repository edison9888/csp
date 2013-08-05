package com.taobao.csp.cost.web.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.cost.dao.CostDayDao;
import com.taobao.csp.cost.dao.CspAppDependDao;
import com.taobao.csp.cost.po.CapacityCostConfigPo;
import com.taobao.csp.cost.po.CapacityCostDepPo;
import com.taobao.csp.cost.po.CapacityCostInfoPo;
import com.taobao.csp.cost.po.CapacityCostRatioPo;
import com.taobao.csp.cost.util.OpsFreeUtil;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2011-9-19 下午01:46:31
 */
@Controller
@RequestMapping("/show.do")
public class CapacityShowAction {
	
	private static Log logger = LogFactory.getLog(CapacityShowAction.class);
	
	@Resource(name = "costDayDao")
	private CostDayDao costDayDao;
	
	@Resource(name = "cspAppDependDao")
	private CspAppDependDao cspAppDependDao;
	
	@RequestMapping(params = "method=showCapacityCostListNormal")
	public ModelAndView showCapacityCostListNormal() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = costDayDao.findCapacityCostListNormal();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=callHostSync")
	public void callHostSync(HttpServletResponse response) {

		logger.info("调用immediatelySync");
		try{
			CspSyncOpsHostInfos.immediatelySync();
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("调用CspSyncOpsHostInfos.immediatelySync()成功");
			response.flushBuffer(); 
		}catch (Exception e) {
		}
	}

	
	@RequestMapping(params = "method=showCapacityCostListTair")
	public ModelAndView showCapacityCostListTair() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = costDayDao.findCapacityCostListTair();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostListDB")
	public ModelAndView showCapacityCostListDB() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = costDayDao.findCapacityCostListDB();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=index")
	public ModelAndView showIndex() {
		ModelAndView view = new ModelAndView("/cost/cost_index");
		
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostDep")
	public ModelAndView showCapacityCostDep(String appName, String pv) {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_depend");
		List<CapacityCostDepPo> costDepList = costDayDao.findCapacityCostDepList(appName);
		
		long totalDepPv = 0;
		long totalDepCost = 0;
		double totolPerDepCost = 0;
		for (CapacityCostDepPo po :  costDepList) {
			 double perDepCost = (double)po.getDepCost() / Double.valueOf(pv).doubleValue();
			 po.setPerDepCost(perDepCost);
			 totalDepPv += po.getDepPv();
			 totalDepCost += po.getDepCost();
			 totolPerDepCost += perDepCost;
		}
		
		DecimalFormat dfDouble = new DecimalFormat("###,###.##");
		view.addObject("costDepList", costDepList);
		view.addObject("appName", appName);
		view.addObject("totalDepPv", Utlitites.fromatLong(String.valueOf(totalDepPv)));
		view.addObject("totalDepCost", Utlitites.fromatLong(String.valueOf(totalDepCost)));
		view.addObject("totalPerDepCost", dfDouble.format(totolPerDepCost));
		view.addObject("pv", Utlitites.fromatLong(String.valueOf(pv)));
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostSelf")
	public ModelAndView showCapacityCostDep(String appName, String machineNum, 
			String pv, String perCost, String selfCost) {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_depend_self");
		List<CapacityCostDepPo> costDepList = costDayDao.findCapacityCostDepSelfList(appName);
		
		view.addObject("costDepList", costDepList);
		view.addObject("appName", appName);
		view.addObject("machineNum", machineNum);
		view.addObject("pv", Utlitites.fromatLong(pv));
		view.addObject("perCost", perCost);
		view.addObject("selfCost", Utlitites.fromatLong(selfCost));
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostConfig")
	public ModelAndView showCapacityCostConfig() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_config");
		List<CapacityCostConfigPo> list = new ArrayList<CapacityCostConfigPo>();
		Map<String, List<CapacityCostConfigPo>> costMap = cspAppDependDao.getCapacityCostConfigPos();
		
		for (Map.Entry<String, List<CapacityCostConfigPo>> entry : costMap.entrySet()) {
			String groupName = entry.getKey();
			List<CapacityCostConfigPo> pos =  entry.getValue();
			int machineNum = OpsFreeUtil.findHostListInOpsfreeByOpsName(groupName).keySet().size();
			double perMachine = (double) machineNum / pos.size();
			
			for (CapacityCostConfigPo po : pos) {
				po.setGroupMachineNum(machineNum);
				po.setPerMchine(perMachine);
				list.add(po);
			}
		}
		
		view.addObject("costList", list);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostDiamond")
	public ModelAndView showCapacityCostDiamond() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_config");
		List<CapacityCostConfigPo> list = new ArrayList<CapacityCostConfigPo>();
		Map<String, List<CapacityCostConfigPo>> costMap = cspAppDependDao.getCapacityCostDiamondPos();
		
		for (Map.Entry<String, List<CapacityCostConfigPo>> entry : costMap.entrySet()) {
			String groupName = entry.getKey();
			List<CapacityCostConfigPo> pos =  entry.getValue();
			int machineNum = OpsFreeUtil.findHostListInOpsfreeByOpsName(groupName).keySet().size();
			double perMachine = (double) machineNum / pos.size();
			
			for (CapacityCostConfigPo po : pos) {
				po.setGroupMachineNum(machineNum);
				po.setPerMchine(perMachine);
				list.add(po);
			}
		}
		
		view.addObject("costList", list);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostRatio")
	public ModelAndView showCapacityCostRatio() {
		ModelAndView view = new ModelAndView("/cost/capacity_cost_ratio");
		List<CapacityCostRatioPo> list;
		
		list = costDayDao.findCapacityCostRatios();
		
		view.addObject("list", list);
		return view;
	} 
	
}
