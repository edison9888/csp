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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-17 - ����03:33:32
 * @version 1.0
 */
public class SendReportMessage {
	
	private static final Logger logger = Logger.getLogger(SendReportMessage.class);
	
	private static EmailMessageSend emailSend = new EmailMessageSend();
	
	private static MonitorReportConfigDao reportDao = new MonitorReportConfigDao();
	
	/**
	 * ���ͱ�����Ϣ
	 * @author ն��
	 * @param reportId
	 * @param type
	 * 2011-5-17 - ����03:35:30
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
		//���������Ӧ�õı���ÿ���û�ѡ���Ӧ�ÿ��ܲ�һ��������Ҫ���û������ʼ�
		reportTile = "CSPƽ̨-" + report.getReportName();
		reportTile += searchDate;
		if(report.getReportType() == Constants.REPORT_TYPE_APP){// 1:����Ӧ��
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
								reportContent);// ��ȡ�澯��������Ϣ
						logger.info("��������Id="+reportId+" ���ͳɹ�...");
					}else{
						logger.info("��������Id="+reportId+" δ�����޽�������Ϣ...");
					}
				}catch (Exception e) {
					logger.info("",e);
				}
			}
		}else{//0:�û��Զ���
			if(acceptors.size() == 0){
				logger.info("��������Id="+reportId+" δ�����޽�������Ϣ...");
			}
			
			String reportUrl_tmp = report.getPath();
			
			
			
			
			if(reportUrl_tmp.indexOf("?") ==-1){
				try{
					//˵�����url �ǹ��õģ��������
					reportContent = RequestByUrl.getMessageByJsp(reportUrl_tmp);
					StringBuilder sb = new StringBuilder();
					for(ReportAcceptor addressAcceptor : acceptors){
						sb.append(addressAcceptor.getAddress()+";");
					}
					emailSend.send(sb.toString(), reportTile,
							reportContent);// ��ȡ�澯��������Ϣ
					logger.info("��������Id="+reportId+" ���ͳɹ�..."+sb.toString());
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
							logger.info("����"+urlBuffer.toString()+" parms:"+parms);
							reportContent = RequestByUrl.postMesageByJsp(urlBuffer.toString(), parms);
							reportContentMap.put(params, reportContent);
						}
						if(addressAcceptor.getAddress() != null && 
								!addressAcceptor.getAddress().equals("")){
							emailSend.send(addressAcceptor.getAddress() , reportTile,
									reportContent);// ��ȡ�澯��������Ϣ
							logger.info("��������Id="+reportId+" ���ͳɹ�..."+addressAcceptor.getAddress());
						}else{
							logger.info("��������Id="+reportId+" δ�����޽�������Ϣ...");
						}
					}catch (Exception e) {
						logger.info("",e);
					}}
			}
		}
	}
}
