/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.schedule;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.adapter.CaseAdapterFactory;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.SeleniumBeanService;
import com.taobao.monitor.selenium.service.SeleniumUseCaseService;
import com.taobao.monitor.selenium.util.DateUtil;

/**
 * selenium ����ִ�е��ȳ���.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-9 - ����11:46:34
 * @version 1.0
 */
public class SeleniumJob implements Job {
	
	private final static Logger logger = Logger.getLogger(SeleniumJob.class);

	/**
	 * @param context
	 * @throws JobExecutionException
	 * 2011-6-9 - ����11:46:34
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//��ȡ����������  
		String triggerName = context.getTrigger().getName();  
		//���ݴ��������Ƶõ���Ӧ������Id  
		int rcId = Integer.valueOf(triggerName.split("_")[2]);  
		//��ȡ����
		executeUseCase(rcId);
		System.out.println(DateUtil.getDateYMDHMSFormat().format(new Date())+
				" Selenium RC��"+rcId+" ��ʼִ���������ȳ���");
	}

	/**
	 * ��ȡ����ִ������
	 * 
	 * @param seleniumServerId
	 * 2011-6-9 - ����08:11:24
	 */
	private void executeUseCase(int rcId){	
		SeleniumUseCaseService ucService = (SeleniumUseCaseService) 
		SeleniumBeanService.getBean("seleniumUseCaseServiceImpl");
		List<UseCase> ucList = ucService.getDependUcByRcId(rcId);
		for(UseCase uc : ucList){
			try{
				CaseAdapter logAdapter = CaseAdapterFactory.generate(uc.getUseCaseName(), 
						uc.getUseCaseVersion(), ucService, rcId);
				//ִ����������(selenium���󴴽�)
				logAdapter.execute(null);
			}catch(Exception e){
				logger.error("[Selenium Server:]"+rcId+"[Use Case:]"+uc.getUseCaseId()+" executeUseCase ����"+e.getMessage());
			}
		}
	}
}
