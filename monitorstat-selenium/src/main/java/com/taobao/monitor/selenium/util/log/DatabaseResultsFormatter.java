/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import org.apache.log4j.Logger;

import com.taobao.monitor.selenium.service.SeleniumBeanService;
import com.taobao.monitor.selenium.service.UseCaseResultService;


/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-25 - 上午09:32:46
 * @version 1.0
 */
public class DatabaseResultsFormatter extends SeleniumBeanService 
    implements LogResultsFormatter {

	private static final Logger logger = Logger.getLogger(
			DatabaseResultsFormatter.class);
	
	private static final String UCResultServiceBean = "useCaseResultServiceImpl";
    
	/**
	 * @param LogBean
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void booleanCommandLogEvent(LogBean logBean) {
		if(logBean.getUsecaseId() ==0 || logBean.getUsecaseResultId() == 0)return;
        String[] loggingBeanArgs = LogUtils.getCorrectedArgsArray(logBean, 2, "");
        logBean.setArgs(loggingBeanArgs);
		saveLogBean(logBean);
	}

	/**
	 * @param LogBean
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void commandLogEvent(LogBean logBean) {
		//保存指令信息
        if (!logBean.isExcludeFromLogging() && logBean.getUsecaseId()!=0 
        		&& logBean.getUsecaseResultId() != 0) {
            String[] loggingBeanArgs = LogUtils.getCorrectedArgsArray(logBean, 2, "");
            logBean.setArgs(loggingBeanArgs);
        	saveLogBean(logBean);
        }
	}

	/**
	 * @param LogBean
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void commentLogEvent(LogBean logBean) {
		//保存指令信息
		if(logBean.getUsecaseId() ==0 || logBean.getUsecaseResultId() == 0)return;
        String[] loggingBeanArgs = LogUtils.getCorrectedArgsArray(logBean, 2, "");
        logBean.setArgs(loggingBeanArgs);
		saveLogBean(logBean);
	}

	/**
	 * 
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void footerLogEvent() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param baseName
	 * @return
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public String generateFilenameForAutomaticScreenshot(String baseName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public String getAutomaticScreenshotPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public String getScreenShotBaseUri() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 保存汇总信息到数据库
	 * @param metricsBean
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void headerLogEvent(TestMetricsBean metricsBean) {
		UseCaseResultService useCaseResultService = (UseCaseResultService) 
		this.getBean(UCResultServiceBean);
		useCaseResultService.addUCResult(metricsBean);	
	}

	/**
	 * @param LogBean
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void methodLogEvent(LogBean LogBean) {
		//saveLogBean(LogBean);

	}

	/**
	 * @param automaticScreenshotPath
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void setAutomaticScreenshotPath(String automaticScreenshotPath) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param screenShotBaseUri
	 * 2011-5-25 - 上午09:32:46
	 */
	@Override
	public void setScreenShotBaseUri(String screenShotBaseUri) {
		// TODO Auto-generated method stub

	}
	
	private void saveLogBean(LogBean LogBean){
		UseCaseResultService useCaseResultService = (UseCaseResultService) 
		this.getBean(UCResultServiceBean);
		useCaseResultService.addUcResultDetail(LogBean);
	}

}
