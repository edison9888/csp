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
 * selenium 用例执行调度程序.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-9 - 上午11:46:34
 * @version 1.0
 */
public class SeleniumJob implements Job {
	
	private final static Logger logger = Logger.getLogger(SeleniumJob.class);

	/**
	 * @param context
	 * @throws JobExecutionException
	 * 2011-6-9 - 上午11:46:34
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//获取触发器名称  
		String triggerName = context.getTrigger().getName();  
		//根据触发器名称得到对应的任务Id  
		int rcId = Integer.valueOf(triggerName.split("_")[2]);  
		//获取任务
		executeUseCase(rcId);
		System.out.println(DateUtil.getDateYMDHMSFormat().format(new Date())+
				" Selenium RC："+rcId+" 开始执行用例调度程序！");
	}

	/**
	 * 获取用例执行用例
	 * 
	 * @param seleniumServerId
	 * 2011-6-9 - 下午08:11:24
	 */
	private void executeUseCase(int rcId){	
		SeleniumUseCaseService ucService = (SeleniumUseCaseService) 
		SeleniumBeanService.getBean("seleniumUseCaseServiceImpl");
		List<UseCase> ucList = ucService.getDependUcByRcId(rcId);
		for(UseCase uc : ucList){
			try{
				CaseAdapter logAdapter = CaseAdapterFactory.generate(uc.getUseCaseName(), 
						uc.getUseCaseVersion(), ucService, rcId);
				//执行用例方法(selenium对象创建)
				logAdapter.execute(null);
			}catch(Exception e){
				logger.error("[Selenium Server:]"+rcId+"[Use Case:]"+uc.getUseCaseId()+" executeUseCase 出错！"+e.getMessage());
			}
		}
	}
}
