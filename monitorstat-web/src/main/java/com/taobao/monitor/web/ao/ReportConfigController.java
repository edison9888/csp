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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-10 - ����09:55:58
 * @version 1.0
 */
@Controller
public class ReportConfigController {
	
	private final static Logger logger = Logger
	.getLogger(ReportConfigController.class);

	@Resource(name="monitorReportConfigAo")
	private MonitorReportConfigAo reportConfigAo;

	/**
	 * ��λ����������ҳ��
	 * @author ն��
	 * @param request
	 * @param response
	 * @return
	 * 2011-5-13 - ����09:51:48
	 */
	@RequestMapping(value="/reportConfig.do")	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response){	
		//���õ�¼Ҳ�������ñ��������
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
//			modelView.addObject("powerMsg", "��û��Ȩ�޲���,���¼!");
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
	 * ��ӱ���ģ��
	 * @author ն��
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-12 - ����10:49:53
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
	 * �޸ı���
	 * @author ն��
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-13 - ����11:36:42
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
	 * ��ӱ���ģ��
	 * @author ն��
	 * @param request
	 * @param reportTemplate
	 * @return
	 * 2011-5-12 - ����10:49:53
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
	 * ɾ����������
	 * @author ն��
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - ����10:49:35
	 */
	@RequestMapping("/ajaxDeleteReport.do")
	public void deleteReport(HttpServletResponse response, int reportId){
		reportConfigAo.deleteReport(reportId);
		outString(response, "ok");
	}
	
	/**
	 * ��ͣ����
	 * @author ն��
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - ����10:49:35
	 */
	@RequestMapping("/ajaxPauseJob.do")
	public void pauseJob(HttpServletResponse response, int reportId){
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.pauseJob(triggerName);
		outString(response, QuartzManager.getTaskStateMsg(triggerName));
	}
	
	/**
	 * ��������
	 * @author ն��
	 * @param request
	 * @param reportId
	 * @return
	 * 2011-5-12 - ����10:49:35
	 */
	@RequestMapping("/ajaxResumeJob.do")
	public void resumeJob(HttpServletResponse response, int reportId){
		String jobName = Constants.JOBDETAIL_PREFIX + reportId;
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.resumeJob(jobName, triggerName);
		outString(response, QuartzManager.getTaskStateMsg(triggerName));
	}
	
	/**
	 * ���Ա�����
	 * @author ն��
	 * @param response
	 * @param reportId
	 * 2011-5-17 - ����03:44:06
	 */
	@RequestMapping("/ajaxTestSendJob.do")
	public void testSendJob(HttpServletResponse response, int reportId){
		SendReportMessage.sendReport(reportId, Constants.REPORT_EMAIL_ACCEPTOR);
		outString(response, "ok");
	}
	
	/**	
	 * ��λ����ӱ��������ҳ��
	 * @author ն��
	 * @param reportId
	 * @return
	 * 2011-5-13 - ����09:50:45
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
	 * ���ñ���Ӧ���Լ���Ӧ������
	 * @author ն��
	 * @param request
	 * @param hidEmailList
	 * @param hidSelectIds
	 * @return
	 * 2011-5-12 - ����03:07:11
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
			if(reportType == Constants.REPORT_TYPE_APP){// 1:����Ӧ��  
				reportParam = selectId;
			}else{//0:�û��Զ���
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
	 * ɾ�������������Ϣ
	 * @author ն��
	 * @param response
	 * @param reportId
	 * @param addressList
	 * 2011-5-19 - ����11:19:40
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
	 * ajax�������
	 * @author ն��
	 * @param response
	 * @param json
	 * 2011-5-13 - ����09:51:25
	 */
	public void outString(HttpServletResponse response,String json) {  
		PrintWriter out;
		response.setContentType("text/html;charset=utf-8");
		try {
			out = response.getWriter();
			out.write(json); 
		} catch (IOException e) {
			logger.error("ajax outString����", e);
		}  
	}

}
