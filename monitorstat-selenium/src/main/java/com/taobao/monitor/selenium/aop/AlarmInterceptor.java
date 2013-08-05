/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.aop;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.adapter.CaseAdapterFactory;
import com.taobao.monitor.selenium.dao.model.SeleniumAlarmAcceptor;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.AlarmCache;
import com.taobao.monitor.selenium.service.SeleniumAlarmAcceptorService;
import com.taobao.monitor.selenium.service.SeleniumUseCaseService;
import com.taobao.monitor.selenium.util.DateUtil;
import com.taobao.monitor.selenium.util.log.TestMetricsBean;
import com.taobao.wwnotify.biz.WwNotify;

/**
 * �澯������.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-14 - ����03:49:38
 * @version 1.0
 */
@Aspect
@Component
public class AlarmInterceptor {

	public final static Logger logger = Logger.getLogger(AlarmInterceptor.class);
	
	@Resource(name="seleniumUseCaseServiceImpl")
	private SeleniumUseCaseService ucService;
	
	@Resource(name="seleniumAlarmAcceptorServiceImpl")
	private SeleniumAlarmAcceptorService seleniumAlarmService;
	
	/**
	 * ������Ϣ��ȡ�����
	 * 
	 * 2011-6-3 - ����04:47:53
	 */
	@Pointcut("execution(public * com.taobao.monitor.selenium.service.impl..*.addUCResult(..))")
	public void addPointCut(){};

	
	@After("addPointCut() && args(metricsBean)")
	public void beforeAddUCResult(TestMetricsBean metricsBean){
		UseCase uc = metricsBean.getUseCase();
		if(metricsBean.getFinallyState() == 1){//����ִ��ʧ��
			if(AlarmCache.whetherAlarm(metricsBean.getUsecaseId())){//����澯����
				 List<SeleniumAlarmAcceptor> acceptors = seleniumAlarmService
				 .getAlarmAcceptorByUcId(metricsBean.getUsecaseId());
				 for(SeleniumAlarmAcceptor acceptor:acceptors){
					 if(acceptor.getType().equalsIgnoreCase("ww")){
						 Calendar cal = Calendar.getInstance();
						 StringBuffer content = new StringBuffer();
						 content.append("������").append(uc.getUseCaseAlias()).append("</BR>");
						 content.append("���Խ��������ʧ��</BR>");
						 content.append("������������"+metricsBean.getCommandsProcessed()+"</BR>");
						 content.append("ʧ����������"+metricsBean.getFailedCommands()+"</BR>");
						 if(metricsBean.getLastFailedCommandMessage() != null &&
							 !metricsBean.getLastFailedCommandMessage().equals("")){
							 content.append("���ʧ����Ϣ��"+metricsBean.getLastFailedCommandMessage()+"</BR>");
						 }
						 content.append("<a href='http://10.13.42.83:8080/monitorstat/selenium/ucResultList.do");
						 content.append("?useCaseId=").append(metricsBean.getUsecaseId()).append("&endDate=")
						 .append(DateUtil.getDateYMDHMSFormat().format(cal.getTime()));
						 cal.add(Calendar.MINUTE, -5);
						 content.append("&startDate=").append(DateUtil.getDateYMDHMSFormat().format(cal.getTime()))
						 .append("'>�鿴����</a>");
						 WwNotify.get().sendWWMessage(acceptor.getAddress(), 
								 "CSPƽ̨", "����Ѳ���澯", content.toString());
					 }else if(acceptor.getType().equalsIgnoreCase("sms")){//���߸澯
						 
					 }else if(acceptor.getType().equalsIgnoreCase("email")){//�ʼ��澯
						 
					 }
				 }
				System.out.println("������"+metricsBean.getUsecaseId()
						+" ִ��ʧ�ܣ���ʼ�澯");
			}else{//�ٴ�ִ������
				try{
					CaseAdapter logAdapter = CaseAdapterFactory.generate(uc.getUseCaseName(), 
							uc.getUseCaseVersion(), ucService, uc.getRcId());
					//ִ����������(selenium���󴴽�)
					logAdapter.execute(null);
				}catch(Exception e){
					logger.error("[Selenium Server:]"+uc.getRcId()+"[Use Case:]"+uc.getUseCaseId()+" executeUseCase ����"+e.getMessage());
				}
			}	
		}
	}
}
