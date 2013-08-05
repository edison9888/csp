/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.schedule;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.selenium.service.SeleniumBeanService;
import com.taobao.monitor.selenium.service.UseCaseResultService;
import com.taobao.monitor.selenium.util.DateUtil;

/**
 * �������selenium��������.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-7-12 - ����02:25:28
 * @version 1.0
 */
public class DeleteSeleniumTestData implements Job {

	/**
	 * @param context
	 * @throws JobExecutionException
	 * 2011-7-12 - ����02:25:28
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("��ʼ���������ʷ���ݣ�");
		UseCaseResultService ucResultService = (UseCaseResultService) 
		SeleniumBeanService.getBean("useCaseResultServiceImpl");
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -5);
		String endTime = DateUtil.getDateYMDHMSFormat().format(ca.getTime());
		ca.add(Calendar.DATE, -35);
		String startTime = DateUtil.getDateYMDHMSFormat().format(ca.getTime());
		ucResultService.deleteUcResult(startTime, endTime);
	}

}
