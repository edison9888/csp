/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.ao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.schedule.QuartzManager;
import com.taobao.monitor.web.schedule.SendReportMessage;
import com.taobao.monitor.web.util.UserPermissionCheck;
import com.taobao.monitor.web.vo.ReportTemplate;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-10 - 上午09:55:58
 * @version 1.0
 */
@Controller
public class ReportConfigController {
	
	private final static Logger logger = Logger
	.getLogger(ReportConfigController.class);

	@Resource(name="monitorReportConfigAo")
	private MonitorReportConfigAo reportConfigAo;

	/**
	 * 定位到报表配置页面
	 * @author 斩飞
	 * @param request
	 * @param response
	 * @return
	 * 2011-5-13 - 上午09:51:48
	 */
	@RequestMapping(value="/reportConfig.do")	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response){	
		//不用登录也可以配置报表接收人
		ModelAndView modelView = null; 		
		List<ReportTemplate> listReportList = reportConfigAo.findAllReport();
		modelView = new ModelAndView("/report/reportConfig");
		modelView.addObject("listReportList", listReportList);
		if(UserPermissionCheck.check(request, Constants.REPORT_CONFIG_POWER,"")){
			modelView.addObject("reportconfig", "true");
		}
		return modelView;
//		if(!UserPermissionCheck.check(request,"adduser","")){
//			modelView = new ModelAndView("login");
//			modelView.addObject("powerMsg", "你没有权限操作,请登录!");
//			return modelView;
//		}else{
//			List<ReportTemplate> listReportList = reportConfigAo.findAllReport();
//			modelView = new ModelAndView("/report/reportConfig");
//			modelView.addObject("listReportList", listReportList);
//			if(UserPermissionCheck.check(request, Constants.REPORT_CONFIG_POWER,"")){
//				modelView.addObject("reportconfig", "true");
//			}
//			return modelView;
//		}
	}
	
	@RequestMapping("/goAddReport.do")
	public ModelAndView goAddReportTemplate(){
		ModelAndView modelView = new ModelAndView("/report/manageReport");
		return modelView;
	}
	
	/**
	 * 添加报表模板
	 * @author 斩飞
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-12 - 上午10:49:53
	 */
	@RequestMapping("/addReport.do")
	public ModelAndView addReport(HttpServletRequest request, 
			ReportTemplate reportTemplate){
		String quartzCron = reportTemplate.getQuartzCron().trim();
		quartzCron = quartzCron.replaceAll("\r\n", "");
		quartzCron = quartzCron.replaceAll("\r\n", "");
		quartzCron = quartzCron.replaceAll("\n", "");
		reportTemplate.setQuartzCron(quartzCron);
		reportConfigAo.addReport(reportTemplate);
		ModelAndView modelView = new ModelAndView("/report/manageReport");
		modelView.addObject("operate", "add");
		return modelView;
	}
	
	/**
	 * 修改报表
	 * @author 斩飞
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-13 - 上午11:36:42
	 */
	@RequestMapping("/forwardUpdateReport.do")
	public ModelAndView ReportTemplate(HttpServletRequest request, 
			int reportId){
		ReportTemplate report = reportConfigAo.getReportById(reportId);
		report.setReportId(reportId);
		report.setQcUpdate("update");
		ModelAndView modelView = new ModelAndView("/report/manageReport");
		modelView.addObject("report" ,report);
		return modelView;
	}
	
	/**
	 * 添加报表模板
	 * @author 斩飞
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-12 - 上午10:49:53
	 */
	@RequestMapping("/updateReport.do")
	public ModelAndView updateReport(HttpServletRequest request, 
			ReportTemplate reportTemplate){
		reportConfigAo.updateReport(reportTemplate);
		ModelAndView modelView = new ModelAndView("/report/manageReport");
		modelView.addObject("operate", "update");
		return modelView;
	}
	
	/**
	 * 删除报表配置
	 * @author 斩飞
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - 上午10:49:35
	 */
	@RequestMapping("/ajaxDeleteReport.do")
	public void deleteReport(HttpServletResponse response, int reportId){
		reportConfigAo.deleteReport(reportId);
		outString(response, "ok");
	}
	
	/**
	 * 暂停任务
	 * @author 斩飞
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - 上午10:49:35
	 */
	@RequestMapping("/ajaxPauseJob.do")
	public void pauseJob(HttpServletResponse response, int reportId){
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.pauseJob(triggerName);
		outString(response, QuartzManager.getTaskStateMsg(triggerName));
	}
	
	/**
	 * 启动任务
	 * @author 斩飞
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - 上午10:49:35
	 */
	@RequestMapping("/ajaxResumeJob.do")
	public void resumeJob(HttpServletResponse response, int reportId){
		String jobName = Constants.JOBDETAIL_PREFIX + reportId;
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.resumeJob(jobName, triggerName);
		outString(response, QuartzManager.getTaskStateMsg(triggerName));
	}
	
	/**
	 * 测试报表发送
	 * @author 斩飞
	 * @param response
	 * @param reportId
	 * 2011-5-17 - 下午03:44:06
	 */
	@RequestMapping("/ajaxTestSendJob.do")
	public void testSendJob(HttpServletResponse response, int reportId){
		SendReportMessage.sendReport(reportId, Constants.REPORT_EMAIL_ACCEPTOR);
		outString(response, "ok");
	}
	
	/**	
	 * 定位到添加报表接受者页面
	 * @author 斩飞
	 * @param reportId
	 * @return
	 * 2011-5-13 - 上午09:50:45
	 */
	@RequestMapping("/goAddReportAcceptor.do")
	public ModelAndView goAddReportAcceptor(String reportId){
		Set<String> acceptors = reportConfigAo.getReportAcceptor(Integer.parseInt(reportId), 
				Constants.REPORT_EMAIL_ACCEPTOR);
		List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
		Map<String, String> params = new HashMap<String, String>();
		ReportTemplate report = reportConfigAo.getReportById(Integer.parseInt(reportId));
		String path = report.getPath();
		int question_mark = path.indexOf("?");
		if(question_mark != -1){//?param1=&param=
			path = path.substring(question_mark+1);
			if(!path.equals("")){
				String[] paramArrs = path.split("=");
				if(paramArrs != null && paramArrs.length > 0){
					for(String param : paramArrs){
						int index = param.indexOf("&");
						if(index != -1){
							params.put(param.substring(index+1), "");
						}else{
							params.put(param, "");
						}
					}
				}
			}	
		}
		ModelAndView modelView = new ModelAndView("/report/manageReportAcceptor");
		modelView.addObject("listApp", listApp);
		modelView.addObject("acceptors", acceptors);
		modelView.addObject("reportId", reportId);
		modelView.addObject("report", report);
		modelView.addObject("params", params);
		return modelView;
	}
	
	@RequestMapping("/ajaxRepAppsByAddress.do")
	public void ajaxReportAppsByAddress(HttpServletResponse response,
			String reportId, String addressList){
		String appIds = reportConfigAo.getReportParamsByAddress(Integer.parseInt(reportId),
				Constants.REPORT_EMAIL_ACCEPTOR, addressList);
		outString(response, appIds);
	}
	
	@RequestMapping("/ajaxUserParamsByAddress.do")
	public void ajaxUserParamsByAddress(HttpServletResponse response,
			String reportId, String addressList){
		String reportParam = reportConfigAo.getReportParamsByAddress(Integer.parseInt(reportId),
				Constants.REPORT_EMAIL_ACCEPTOR, addressList);
		outString(response, reportParam);
	}
	
	/**
	 * 配置报表应用以及对应接受人
	 * @author 斩飞
	 * @param request
	 * @param hidEmailList
	 * @param hidSelectIds
	 * @return
	 * 2011-5-12 - 下午03:07:11
	 */
	@RequestMapping("/addReportAcceptor.do")
	public ModelAndView addReportAcceptor(HttpServletRequest request,
			int reportId, int reportType, String addressList,
			String selectId, String paramValue, String type){
		String[] addressArray = null;
		String reportParam = null;
		ReportTemplate report = reportConfigAo.getReportById(reportId);
		String path = report.getPath();
		int question_mark = path.indexOf("?");
		if(addressList != null && !addressList.equals("")){
			addressArray = addressList.split(",");
			if(addressArray == null || addressArray.length == 0)
				addressArray = new String[]{};
			if(reportType == Constants.REPORT_TYPE_APP){// 1:区分应用  
				reportParam = selectId;
			}else{//0:用户自定义
				reportParam = paramValue;
			}
			reportConfigAo.addReportAcceptor(reportId, addressArray, reportParam,
					type, report);
		}	
		ModelAndView modelView = new ModelAndView("/report/manageReportAcceptor");
		Set<String> acceptors = reportConfigAo.getReportAcceptor(reportId, 
				Constants.REPORT_EMAIL_ACCEPTOR);
		List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
		Map<String, String> params = new HashMap<String, String>();
		if(question_mark != -1){//?param1=&param=
			path = path.substring(question_mark+1);
			if(!path.equals("")){
				String[] paramArrs = path.split("=");
				String[] paramValues = null;
				if(paramValue != null && !paramValue.equals("")){
					paramValues = paramValue.split(",");
				}
				if(paramArrs != null && paramArrs.length > 0){
					int i = 0;
					for(String param : paramArrs){
						int index = param.indexOf("&");
						if(index != -1){
							if(paramValue != null && !paramValue.equals("")){
								params.put(param.substring(index+1), paramValues[i]);
							}else{
								params.put(param.substring(index+1), "");
							}			
						}else{
							if(paramValue != null && !paramValue.equals("")){
								params.put(param, paramValues[i]);
							}else{
								params.put(param, "");
							}	
						}
						i++;
					}
				}
			}	
		}
		modelView.addObject("listApp", listApp);
		modelView.addObject("acceptors", acceptors);
		modelView.addObject("reportId", reportId);
		modelView.addObject("report", report);
		modelView.addObject("params", params);
		modelView.addObject("addressArray", addressArray);
		return modelView;
	}
	
	/**
	 * 删除报表接收人信息
	 * @author 斩飞
	 * @param response
	 * @param reportId
	 * @param addressList
	 * 2011-5-19 - 上午11:19:40
	 */
	@RequestMapping("/ajaxRemoveAcceptorByAddress.do")
	public void removeReportAcceptorByAddress(HttpServletResponse response,
			String reportId, String addressList){
		String[] addressArray = null;
		if(addressList != null && !addressList.equals("")){
			addressArray = addressList.split(",");
			if(addressArray == null || addressArray.length == 0)
				addressArray = new String[]{};
		}
		reportConfigAo.removeAcceptorByAddress(Integer.parseInt(reportId),
				addressArray);
		outString(response, "ok");
	}
	
	/**
	 * ajax输出数据
	 * @author 斩飞
	 * @param response
	 * @param json
	 * 2011-5-13 - 上午09:51:25
	 */
	public void outString(HttpServletResponse response,String json) {  
		PrintWriter out;
		response.setContentType("text/html;charset=utf-8");
		try {
			out = response.getWriter();
			out.write(json); 
		} catch (IOException e) {
			logger.error("ajax outString出错", e);
		}  
	}

}
