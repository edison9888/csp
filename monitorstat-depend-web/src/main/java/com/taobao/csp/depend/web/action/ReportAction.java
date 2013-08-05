package com.taobao.csp.depend.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.auto.DependCache;
import com.taobao.csp.depend.dao.CspAppHsfDependConsumeDao;
import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.dao.CspAppTairConsumeDao;
import com.taobao.csp.depend.dao.CspCheckupDependDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.po.report.ConsumeHSFReport;
import com.taobao.csp.depend.po.report.TairException;
import com.taobao.csp.depend.po.tair.TairConsumeSummaryPo;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 这个类用来生成依赖部分相关的报表。新增报表的时候新增一个方法就可以了。
 * @author zhongting.zy
 *
 */
@Controller
@RequestMapping("/show/reportaction.do")
public class ReportAction extends BaseAction {
	private static final Logger logger =  Logger.getLogger(ReportAction.class);

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

	/**
	 * 生成Tair统计部分的报表，按group统计
	 * @param selectDate
	 * @return
	 */

	public final List<AppInfoPo> centerList = getCenterlist();

	public List<AppInfoPo> getCenterlist() {
		return DependCache.getDependcache().getCenterList();
	}

	/**
	 * 消费Tair的报表
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=makeTairReport")
	public ModelAndView makeTairReport(String selectDate){
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		Date predate = MethodUtil.getPreDate(selectDate);
		String strPreDate = MethodUtil.getStringOfDate(predate);

		Map<String, TairConsumeSummaryPo> curMap = cspAppTairConsumeDao.getTairListByDate(selectDate);
		Map<String, TairConsumeSummaryPo> oldMap = cspAppTairConsumeDao.getTairListByDate(strPreDate);

		Map<String, TairException> exceptionMap = new HashMap<String, TairException>();
		cspAppTairConsumeDao.getTairExceptionListByDate(selectDate, exceptionMap, true);
		cspAppTairConsumeDao.getTairExceptionListByDate(strPreDate, exceptionMap, false);  

		ArrayList<TairConsumeSummaryPo> list = new ArrayList<TairConsumeSummaryPo>(curMap.values());
		Collections.sort(list);

		ModelAndView view = new ModelAndView("/depend/report/tair_report");
		view.addObject("selectDate", selectDate);
		view.addObject("predate", strPreDate);			
		view.addObject("list", list);			
		view.addObject("oldMap", oldMap);	
		view.addObject("exceptionMap", exceptionMap);	
		return view;
	}

	/**
	 * 消费HSF的报表
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=makeHSFConsumeReport")
	public ModelAndView makeHSFConsumeReport(String selectDate){
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		Date predate = MethodUtil.getPreDate(selectDate);
		String strPreDate = MethodUtil.getStringOfDate(predate);

		Map<String, ConsumeHSFReport> curMap = cspAppHsfDependConsumeDao.getExceptionReport(centerList, selectDate);
		Map<String, ConsumeHSFReport> oldMap = cspAppHsfDependConsumeDao.getExceptionReport(centerList, strPreDate);
		ArrayList<ConsumeHSFReport> list = new ArrayList<ConsumeHSFReport>(curMap.values());
		Collections.sort(list);

		ModelAndView view = new ModelAndView("/depend/report/hsf_report");
		view.addObject("selectDate", selectDate);
		view.addObject("predate", strPreDate);			
		view.addObject("list", list);			
		view.addObject("oldMap", oldMap);	
		return view;
	}	

	/**
	 * 提供HSF的报表
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=makeHSFProvideReport")
	public ModelAndView makeHSFProvideReport(String selectDate){
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		Date predate = MethodUtil.getPreDate(selectDate);
		String strPreDate = MethodUtil.getStringOfDate(predate);

		Map<String, ConsumeHSFReport> curMap = cspAppHsfDependProvideDao.getExceptionReport(centerList, selectDate);
		Map<String, ConsumeHSFReport> oldMap = cspAppHsfDependProvideDao.getExceptionReport(centerList, strPreDate);
		ArrayList<ConsumeHSFReport> list = new ArrayList<ConsumeHSFReport>(curMap.values());
		Collections.sort(list);

		ModelAndView view = new ModelAndView("/depend/report/hsf_provide_report");
		view.addObject("selectDate", selectDate);
		view.addObject("predate", strPreDate);			
		view.addObject("list", list);			
		view.addObject("oldMap", oldMap);	
		return view;
	}	
}
