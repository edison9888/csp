/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.schedule;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.messagesend.EmailMessageSend;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.core.dao.impl.MonitorReportConfigDao;
import com.taobao.monitor.web.util.DateUtil;
import com.taobao.monitor.web.util.RequestByUrl;
import com.taobao.monitor.web.vo.ReportAcceptor;
import com.taobao.monitor.web.vo.ReportTemplate;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-17 - 下午03:33:32
 * @version 1.0
 */
public class SendReportMessage {
	
	private static final Logger logger = Logger.getLogger(SendReportMessage.class);
	
	private static EmailMessageSend emailSend = new EmailMessageSend();
	
	private static MonitorReportConfigDao reportDao = new MonitorReportConfigDao();
	
	/**
	 * 发送报表信息
	 * @author 斩飞
	 * @param reportId
	 * @param type
	 * 2011-5-17 - 下午03:35:30
	 */
	public static void sendReport(int reportId, String type){
		
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		ReportTemplate report = reportDao.getReportById(reportId);
		List<ReportAcceptor> acceptors = reportDao.getReportAcceptorByReportId(
				reportId, type);
		String reportTile = null;
		String reportContent = null;
		//StringBuffer address = new StringBuffer();
		String searchDate = DateUtil.getDateYMDFormat().format(cal.getTime());
		//如果是区分应用的报表，每个用户选择的应用可能不一致所以需要按用户发送邮件
		reportTile = "CSP平台-" + report.getReportName();
		reportTile += searchDate;
		if(report.getReportType() == Constants.REPORT_TYPE_APP){// 1:区分应用
			for(ReportAcceptor addressAcceptor : acceptors){
				try{
					String reportUrl = report.getPath()+"?appIds="+addressAcceptor.getReportParam()+"&searchDate="+searchDate;
					if(report.getPath().indexOf("?") >-1){
						reportUrl = report.getPath()+"&appIds="+addressAcceptor.getReportParam()+"&searchDate="+searchDate;
					}
					reportContent = RequestByUrl.getMessageByJsp(reportUrl);
					if(addressAcceptor.getAddress() != null && 
							!addressAcceptor.getAddress().equals("")){
						emailSend.send(addressAcceptor.getAddress() , reportTile,
								reportContent);// 获取告警接收人信息
						logger.info("报表任务Id="+reportId+" 发送成功...");
					}else{
						logger.info("报表任务Id="+reportId+" 未配置无接收人信息...");
					}
				}catch (Exception e) {
					logger.info("",e);
				}
			}
		}else{//0:用户自定义
			if(acceptors.size() == 0){
				logger.info("报表任务Id="+reportId+" 未配置无接收人信息...");
			}
			
			String reportUrl_tmp = report.getPath();
			
			
			
			
			if(reportUrl_tmp.indexOf("?") ==-1){
				try{
					//说明这个url 是公用的，无需参数
					reportContent = RequestByUrl.getMessageByJsp(reportUrl_tmp);
					StringBuilder sb = new StringBuilder();
					for(ReportAcceptor addressAcceptor : acceptors){
						sb.append(addressAcceptor.getAddress()+";");
					}
					emailSend.send(sb.toString(), reportTile,
							reportContent);// 获取告警接收人信息
					logger.info("报表任务Id="+reportId+" 发送成功..."+sb.toString());
				}catch (Exception e) {
					logger.info("",e);
				}
			}else{
				
				Map<String,String> reportContentMap =  new HashMap<String, String>();
				
				for(ReportAcceptor addressAcceptor : acceptors){
					
					
					
					
					String reportUrl = reportUrl_tmp;
					
					try{
						String[] paramValues = null;
						String params = addressAcceptor.getReportParam();
						if(params != null && !params.equals("")){
							paramValues = params.split(",");
						}
						
						
						reportContent = reportContentMap.get(params);
						if(reportContent == null){
							StringBuffer urlBuffer = new StringBuffer();
							int question_mark = reportUrl.indexOf("?");
							if(question_mark == -1){
								urlBuffer.append(reportUrl);
							}else{
								urlBuffer.append(reportUrl.substring(0, question_mark))
								.append("?");
							}
							Map parms = new HashMap();
							if(paramValues != null && paramValues.length != 0){
								if(question_mark != -1){//?param1=&param=
									reportUrl = reportUrl.substring(question_mark+1);
									if(!reportUrl.equals("")){
										String[] paramArrs = reportUrl.split("=");
										if(paramArrs != null && paramArrs.length > 0){
											for(int i =0;i<paramArrs.length;i++){
												if(!paramValues[i].equals("null")){
													parms.put(paramArrs[i], paramValues[i]);
												}else{
													parms.put(paramArrs[i], "");
												}
											}
										}
									}
								}
							}
							logger.info("报表"+urlBuffer.toString()+" parms:"+parms);
							reportContent = RequestByUrl.postMesageByJsp(urlBuffer.toString(), parms);
							reportContentMap.put(params, reportContent);
						}
						if(addressAcceptor.getAddress() != null && 
								!addressAcceptor.getAddress().equals("")){
							emailSend.send(addressAcceptor.getAddress() , reportTile,
									reportContent);// 获取告警接收人信息
							logger.info("报表任务Id="+reportId+" 发送成功..."+addressAcceptor.getAddress());
						}else{
							logger.info("报表任务Id="+reportId+" 未配置无接收人信息...");
						}
					}catch (Exception e) {
						logger.info("",e);
					}}
			}
		}
	}
}
