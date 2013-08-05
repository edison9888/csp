///*
// * Taobao.com Inc.
// * Copyright (c) 2010-2011 All Rights Reserved.
// */
//package com.taobao.monitor.selenium.controller;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.taobao.monitor.selenium.dao.model.SeleniumRc;
//import com.taobao.monitor.selenium.dao.model.UseCase;
//import com.taobao.monitor.selenium.dao.model.UseCaseTestStat;
//import com.taobao.monitor.selenium.schedule.SeleniumJob;
//import com.taobao.monitor.selenium.schedule.SeleniumQuartzManager;
//import com.taobao.monitor.selenium.service.BrowserCache;
//import com.taobao.monitor.selenium.service.SeleniumAlarmAcceptorService;
//import com.taobao.monitor.selenium.service.SeleniumRcService;
//import com.taobao.monitor.selenium.service.SeleniumUseCaseService;
//import com.taobao.monitor.selenium.service.UseCaseResultService;
//import com.taobao.monitor.selenium.util.DateUtil;
//import com.taobao.monitor.selenium.util.SeleniumConstant;
//import com.taobao.monitor.selenium.util.log.LogBean;
//import com.taobao.monitor.selenium.util.log.TestMetricsBean;
//import com.taobao.monitor.web.util.UserPermissionCheck;
//
///**
// * .
// * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
// * 2011-6-7 - 下午02:31:30
// * @version 1.0
// */
//@Controller
//public class UseCaseController {
//
//	private static final Logger logger = Logger.getLogger(UseCaseController.class);
//	
//	private static final String SELENIUM_POWER = "selenium";
//	
//	@Resource(name="seleniumUseCaseServiceImpl")
//	private SeleniumUseCaseService useCaseService;
//	
//	@Resource(name="seleniumRcServiceImpl")
//	private SeleniumRcService rcService;
//	
//	@Resource(name="useCaseResultServiceImpl")
//	private UseCaseResultService useCaseResultService;
//	
//	@Resource(name="seleniumAlarmAcceptorServiceImpl")
//	private SeleniumAlarmAcceptorService seleniumAlarmService;
//	
//	/**
//	 * selenium rc service 列表
//	 * 
//	 * @param req
//	 * @param res
//	 * @return
//	 * 2011-6-7 - 下午02:48:13
//	 */
//	@RequestMapping(value="/selenium/listRcService.do")
//	public ModelAndView listRcService(HttpServletRequest request, HttpServletResponse response){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/useCaseList"); 	
//		List<SeleniumRc> rsList = rcService.listRcServices();
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		return modelView;
//	}
//	
//	/**
//	 * 测试案例列表展示
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * 2011-6-7 - 下午04:35:26
//	 */
//	@RequestMapping(value="/selenium/useCaseList.do")
//	public ModelAndView listUseCase(HttpServletRequest request, HttpServletResponse response){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/useCaseList"); 	
//		List<UseCase> ucList = useCaseService.queryAllUseCase();
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		modelView.addObject("ucList", ucList);
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/goAddUseCase.do")
//	public ModelAndView goAddUseCase(HttpServletRequest request, HttpServletResponse response){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageUseCase"); 	
//		//获取rc server列表信息
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		modelView.addObject("rcList", rcList);
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/addUseCase.do")
//	public ModelAndView addUseCase(HttpServletRequest request, HttpServletResponse response,
//			UseCase useCase){
//		useCase.setUseCaseVersion(new Date().getTime());
//		int useCaseId = useCaseService.addUseCase(useCase);
//		useCase.setUseCaseId(useCaseId);
//		//获取rc server列表信息
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageUseCase"); 
//		modelView.addObject("rcList", rcList);
//		modelView.addObject("useCase", useCase);
//		modelView.addObject("operate", "update");
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/goUpdateUseCase.do")
//	public ModelAndView goUpdateUseCase(HttpServletRequest request, HttpServletResponse response,
//			int useCaseId){ 
//		//获取rc server列表信息
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageUseCase");
//		modelView.addObject("rcList", rcList);
//		UseCase useCase = useCaseService.getUseCaseByPrimaryKey(useCaseId);
//		modelView.addObject("useCase", useCase);
//		modelView.addObject("operate", "update");
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/viewUseCase.do")
//	public ModelAndView goViewUseCase(HttpServletRequest request, HttpServletResponse response,
//			int useCaseId){ 
//		//获取rc server列表信息
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageUseCase");
//		modelView.addObject("rcList", rcList);
//		UseCase useCase = useCaseService.getUseCaseByPrimaryKey(useCaseId);
//		modelView.addObject("useCase", useCase);
//		modelView.addObject("operate", "view");
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/updateUseCase.do")
//	public ModelAndView updateUseCase(HttpServletRequest request, HttpServletResponse response,
//			UseCase useCase){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageUseCase"); 
//		useCase.setUseCaseVersion(new Date().getTime());
//		useCaseService.updateUseCase(useCase);
//		//获取rc server列表信息
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		modelView.addObject("rcList", rcList);
//		modelView.addObject("useCase", useCase);
//		modelView.addObject("operate", "update");
//		return modelView;
//	}
//	
//	/**
//	 * 删除selenium server
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/deleteCase.do")
//	public void deleteCase(HttpServletResponse response, int useCaseId){
//		useCaseService.deleteUseCase(useCaseId);
//		outString(response, "true");
//	}
//	
//	@RequestMapping(value="/selenium/seleniumServerList.do")
//	public ModelAndView listSeleniumServer(HttpServletRequest request, HttpServletResponse response){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/seleniumServerList"); 	
//		List<SeleniumRc> rcList = rcService.listRcServices();
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		modelView.addObject("rcList", rcList);
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/goAddSeleniumServer.do")
//	public ModelAndView goAddSeleniumServer(HttpServletRequest request, HttpServletResponse response){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageSeleniumServer"); 	
//		//获取浏览器类型
//		modelView.addObject("browsers", BrowserCache.getAllBrowser());
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/addSeleniumServer.do")
//	public ModelAndView addSeleniumServer(HttpServletRequest request, HttpServletResponse response,
//			SeleniumRc seleniumRc){	
//		rcService.addSeleniumServer(seleniumRc);
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageSeleniumServer"); 	
//		//获取浏览器类型
//		modelView.addObject("browsers", BrowserCache.getAllBrowser());
//		modelView.addObject("seleniumServer", seleniumRc);
//		modelView.addObject("operate", "add");
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/goUpdateSeleniumServer.do")
//	public ModelAndView goUpdateSeleniumServer(HttpServletRequest request, HttpServletResponse response,
//			int rcId){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageSeleniumServer"); 
//		SeleniumRc seleniumRc = rcService.getSeleniumRcByPrimaryKey(rcId);
//		//获取浏览器类型
//		modelView.addObject("browsers", BrowserCache.getAllBrowser());
//		modelView.addObject("seleniumServer", seleniumRc);
//		modelView.addObject("operate", "update");
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/updateSeleniumServer.do")
//	public ModelAndView updateSeleniumServer(HttpServletRequest request, HttpServletResponse response,
//			SeleniumRc seleniumRc){	
//		rcService.updateSeleniumServer(seleniumRc);
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageSeleniumServer"); 	
//		//获取浏览器类型
//		modelView.addObject("browsers", BrowserCache.getAllBrowser());
//		modelView.addObject("operate", "update");
//		modelView.addObject("seleniumServer", seleniumRc);
//		return modelView;
//	}
//	
//	//测试结果汇总信息
//	@RequestMapping(value="/selenium/ucResultStatList.do")
//	public ModelAndView ucResultStatList(HttpServletRequest request, HttpServletResponse response,
//			UseCaseTestStat ucTestStat){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/ucResultStatList"); 
//		Map<Integer, UseCase> ucMap = useCaseService.getUseCaseMap();
//		if(ucTestStat.getStartDate() == null || ucTestStat.getEndDate() == null){
//			//ucTestStat = new UseCaseTestStat();
//			Calendar ca = Calendar.getInstance();
//			ucTestStat.setEndDate(DateUtil.getDateYMDHMSFormat().format(ca.getTime()));
//			ca.add(Calendar.HOUR_OF_DAY, -1);
//			ucTestStat.setStartDate(DateUtil.getDateYMDHMSFormat().format(ca.getTime()));	
//		}
//		if(ucTestStat.getUseCaseId() != 0){
//			List<Integer> ucIds = new ArrayList<Integer>();
//			ucIds.add(ucTestStat.getUseCaseId());
//			ucTestStat.setUcIds(ucIds);
//		}
//		Map<Integer, UseCaseTestStat> usStatMap = useCaseResultService.findUcResultStat(ucTestStat, ucMap);
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		modelView.addObject("usStatMap", usStatMap);
//		modelView.addObject("ucMap", ucMap);
//		modelView.addObject("ucTestStat", ucTestStat);
//		modelView.addObject("useCaseId", ucTestStat.getUseCaseId());
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/ucResultList.do")
//	public ModelAndView ucResultList(HttpServletRequest request, HttpServletResponse response,
//			UseCaseTestStat ucTestStat){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/ucResultList"); 
//		Map<Integer, UseCase> ucMap = useCaseService.getUseCaseMap();
//		if(ucTestStat.getStartDate() == null || ucTestStat.getEndDate() == null){
//			Calendar ca = Calendar.getInstance();
//			ucTestStat.setEndDate(DateUtil.getDateYMDHMSFormat().format(ca.getTime()));
//			ca.add(Calendar.HOUR_OF_DAY, -1);
//			ucTestStat.setStartDate(DateUtil.getDateYMDHMSFormat().format(ca.getTime()));	
//		}
//		if(ucTestStat.getUseCaseId() != 0){
//			List<Integer> ucIds = new ArrayList<Integer>();
//			ucIds.add(ucTestStat.getUseCaseId());
//			ucTestStat.setUcIds(ucIds);
//		}
//		List<TestMetricsBean> usResultList = useCaseResultService
//		.findUcResultByQuery(ucTestStat, ucMap);
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		modelView.addObject("usResultList", usResultList);
//		modelView.addObject("ucMap", ucMap);
//		modelView.addObject("ucTestStat", ucTestStat);
//		return modelView;
//	}
//	
//	@RequestMapping(value="/selenium/ucDetailResult.do")
//	public ModelAndView findUcDetailResultByResultId(HttpServletRequest request, 
//			HttpServletResponse response, long ucResultId){
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/ucResultDetailList"); 	
//		List<LogBean> usResultDetail = useCaseResultService.findUcDetailResultByResultId(ucResultId);
//		if(UserPermissionCheck.check(request, SELENIUM_POWER,"")){
//			modelView.addObject("selenium", "true");
//		}
//		modelView.addObject("usResultDetail", usResultDetail);
//		return modelView;
//	}
//	
//	/**	
//	 * 定位到告警接受者页面
//	 * @author 斩飞
//	 * @param reportId
//	 * @return
//	 * 2011-5-13 - 上午09:50:45
//	 */
//	@RequestMapping(value="/selenium/goSeleniumAlarmAcceptor.do")
//	public ModelAndView goSeleniumAliamAcceptor(String type){
//		Map<Integer, UseCase> ucMap = useCaseService.getUseCaseMap();
//		Map<String, String> alarmTypeMap = SeleniumConstant.getSeleniumAlarmType();
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageAlarmAcceptor");
//		if(type == null || type.equals(""))type = "ww";
//		List<String> acceptors = seleniumAlarmService.getAllAlarmAcceptor(type);
//		modelView.addObject("ucMap", ucMap);
//		modelView.addObject("alarmTypeMap", alarmTypeMap);
//		modelView.addObject("acceptors", acceptors);
//		modelView.addObject("type", type);
//		return modelView;
//	}
//	
//	/**
//	 * 配置报表应用以及对应接受人
//	 * @author 斩飞
//	 * @param request
//	 * @param hidEmailList
//	 * @param hidSelectIds
//	 * @return
//	 * 2011-5-12 - 下午03:07:11
//	 */
//	@RequestMapping(value="/selenium/addSeleniumAlarmAcceptor.do")
//	public ModelAndView addSeleniumAlarmAcceptor(HttpServletRequest request,
//			long[] useCaseIdArr, String addressList, String type){
//		String[] addressArray = null;
////		Set<String> acceptors = new HashSet<String>();
//		if(addressList != null && !addressList.equals("")){
//			addressArray = addressList.split(",");
//			if(addressArray == null || addressArray.length == 0)
//				addressArray = new String[]{};
//			seleniumAlarmService.addAlarmAcceptor(useCaseIdArr, addressArray, type);
//		}	
//		Map<Integer, UseCase> ucMap = useCaseService.getUseCaseMap();
//		Map<String, String> alarmTypeMap = SeleniumConstant.getSeleniumAlarmType();
//		ModelAndView modelView = new ModelAndView("/WEB-INF/selenium/manageAlarmAcceptor");
////		if(addressArray != null){
////			for(String address:addressArray){
////				acceptors.add(address);
////			}
////		}
//		List<String> acceptors = seleniumAlarmService.getAllAlarmAcceptor(type);
//		modelView.addObject("useCaseIdArr", useCaseIdArr);
//		modelView.addObject("acceptors", acceptors);
//		modelView.addObject("addressArray", addressArray);
//		modelView.addObject("ucMap", ucMap);
//		modelView.addObject("alarmTypeMap", alarmTypeMap);
//		return modelView;
//	}
//	
//	/**
//	 * 新启动任务
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/ajaxStartJob.do")
//	public void startJob(HttpServletResponse response, int rcId,
//			String quartzCron){
//		//String jobName = Constants.JOBDETAIL_PREFIX + rcId;
//		String triggerName = SeleniumConstant.TRIGGER_PREFIX + rcId;
//		//新启动任务
//		SeleniumQuartzManager.addJob(rcId+"", quartzCron, SeleniumJob.class);
//		outString(response, SeleniumQuartzManager.getTaskStateMsg(triggerName));
//	}
//			
//	/**
//	 * 停止任务
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/ajaxStopJob.do")
//	public void stopJob(HttpServletResponse response, int rcId){
//		String jobName = SeleniumConstant.JOBDETAIL_PREFIX + rcId;
//		String triggerName = SeleniumConstant.TRIGGER_PREFIX + rcId;
//		SeleniumQuartzManager.removeJob(jobName, triggerName);
//		outString(response, SeleniumQuartzManager.getTaskStateMsg(triggerName));
//	}
//	
//	/**
//	 * 恢复暂停任务
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/ajaxResumeJob.do")
//	public void resumeJob(HttpServletResponse response, int rcId){
//		String jobName = SeleniumConstant.JOBDETAIL_PREFIX + rcId;
//		String triggerName = SeleniumConstant.TRIGGER_PREFIX + rcId;
//		SeleniumQuartzManager.resumeJob(jobName, triggerName);
//		outString(response, SeleniumQuartzManager.getTaskStateMsg(triggerName));
//	}
//	
//	/**
//	 * 暂停任务
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/ajaxPauseJob.do")
//	public void pauseJob(HttpServletResponse response, int rcId){
//		String triggerName = SeleniumConstant.TRIGGER_PREFIX + rcId;
//		SeleniumQuartzManager.pauseJob(triggerName);
//		outString(response, SeleniumQuartzManager.getTaskStateMsg(triggerName));
//	}
//	
//	/**
//	 * 删除selenium server
//	 * @author 斩飞
//	 * @param request
//	 * @param reportId
//	 * @return
//	 * 2011-5-12 - 上午10:49:35
//	 */
//	@RequestMapping("/selenium/deleteSeleniumServer.do")
//	public void deleteSeleniumServer(HttpServletResponse response, int rcId){
//		//删除前需检查是否有用例依赖于该selenium server,存在依赖就不能删除
//		List<UseCase> list = useCaseService.getDependUcByRcId(rcId);
//		if(list.size() > 0){
//			outString(response, "存在："+list.size()+"个用例运行在当前Selenium server上不能删除！");
//		}else{
//			rcService.deleteSeleniumServer(rcId);
//			outString(response, "true");
//		}
//	}
//	
//	/**
//	 * 根据rcId获取支持的浏览器类型
//	 * 
//	 * @param response
//	 * @param rcId
//	 * 2011-6-9 - 下午04:04:50
//	 */
//	@RequestMapping("/selenium/ajaxRcBrowsers.do")
//	public void ajaxRcBrowsers(HttpServletResponse response, int rcId, String selectKey){
//		Map<String ,String> map = rcService.getBrowsersByPrimaryKey(rcId);
//		StringBuilder builder = new StringBuilder();
//		builder.append("document.getElementById('browser').options.length=0;");
//		for(Map.Entry<String, String> entity:map.entrySet()){
//			builder.append("$('#browser').append(\"<option value='").append(entity.getKey()).append("' ");
//			if(selectKey !=null && !selectKey.equals("") && selectKey.equals(entity.getKey())){
//				builder.append(" selected = 'true' ");	
//			}
//			builder.append(">").append(entity.getValue()).append("</option>\");");
//		}
//		outString(response, builder.toString());
//	}
//	
//	@RequestMapping("/selenium/ajaxCheckUCAdaptName.do")
//	public void ajaxCheckUCAdaptName(HttpServletResponse response, String useCaseName){
//		int ucId = useCaseService.getUseCaseIdByName(useCaseName);
//		outString(response, ucId+"");
//	}
//	
//	@RequestMapping("/selenium/ajaxCheckUseCaseAlarmByAddress")
//	public void ajaxCheckUseCaseAlarmByAddress(HttpServletResponse response, String addressList){
//		try {
//			addressList = new String(addressList.getBytes("GBK"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {}
//		String ucIds = seleniumAlarmService.getUseCaseIdsByAlarmAcceptor(addressList);
//		outString(response, ucIds);
//	}
//	
//	@RequestMapping("/selenium/ajaxRemoveAcceptorByAddress")
//	public void ajaxRemoveAcceptorByAddress(HttpServletResponse response, String addressList){
//		try {
//			addressList = new String(addressList.getBytes("GBK"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {}
//		seleniumAlarmService.deleteSeleniumAlarmAcceptor(addressList);
//		outString(response, "ok");
//	}
//	
//	
//	/**
//	 * ajax输出数据
//	 * @author 斩飞
//	 * @param response
//	 * @param json
//	 * 2011-5-13 - 上午09:51:25
//	 */
//	public void outString(HttpServletResponse response,String json) {  
//		PrintWriter out;
//		response.setContentType("text/html;charset=utf-8");
//		try {
//			out = response.getWriter();
//			out.write(json); 
//		} catch (IOException e) {
//			logger.error("ajax outString出错", e);
//		}  
//	}
//}
