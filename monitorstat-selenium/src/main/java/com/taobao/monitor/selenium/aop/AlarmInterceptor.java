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
 * 告警拦截器.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-14 - 下午03:49:38
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
	 * 用例信息获取切入点
	 * 
	 * 2011-6-3 - 下午04:47:53
	 */
	@Pointcut("execution(public * com.taobao.monitor.selenium.service.impl..*.addUCResult(..))")
	public void addPointCut(){};

	
	@After("addPointCut() && args(metricsBean)")
	public void beforeAddUCResult(TestMetricsBean metricsBean){
		UseCase uc = metricsBean.getUseCase();
		if(metricsBean.getFinallyState() == 1){//用例执行失败
			if(AlarmCache.whetherAlarm(metricsBean.getUsecaseId())){//满足告警条件
				 List<SeleniumAlarmAcceptor> acceptors = seleniumAlarmService
				 .getAlarmAcceptorByUcId(metricsBean.getUsecaseId());
				 for(SeleniumAlarmAcceptor acceptor:acceptors){
					 if(acceptor.getType().equalsIgnoreCase("ww")){
						 Calendar cal = Calendar.getInstance();
						 StringBuffer content = new StringBuffer();
						 content.append("用例：").append(uc.getUseCaseAlias()).append("</BR>");
						 content.append("测试结果：运行失败</BR>");
						 content.append("测试命令数："+metricsBean.getCommandsProcessed()+"</BR>");
						 content.append("失败命令数："+metricsBean.getFailedCommands()+"</BR>");
						 if(metricsBean.getLastFailedCommandMessage() != null &&
							 !metricsBean.getLastFailedCommandMessage().equals("")){
							 content.append("最后失败信息："+metricsBean.getLastFailedCommandMessage()+"</BR>");
						 }
						 content.append("<a href='http://10.13.42.83:8080/monitorstat/selenium/ucResultList.do");
						 content.append("?useCaseId=").append(metricsBean.getUsecaseId()).append("&endDate=")
						 .append(DateUtil.getDateYMDHMSFormat().format(cal.getTime()));
						 cal.add(Calendar.MINUTE, -5);
						 content.append("&startDate=").append(DateUtil.getDateYMDHMSFormat().format(cal.getTime()))
						 .append("'>查看详情</a>");
						 WwNotify.get().sendWWMessage(acceptor.getAddress(), 
								 "CSP平台", "交易巡警告警", content.toString());
					 }else if(acceptor.getType().equalsIgnoreCase("sms")){//短线告警
						 
					 }else if(acceptor.getType().equalsIgnoreCase("email")){//邮件告警
						 
					 }
				 }
				System.out.println("用例："+metricsBean.getUsecaseId()
						+" 执行失败，开始告警");
			}else{//再次执行用例
				try{
					CaseAdapter logAdapter = CaseAdapterFactory.generate(uc.getUseCaseName(), 
							uc.getUseCaseVersion(), ucService, uc.getRcId());
					//执行用例方法(selenium对象创建)
					logAdapter.execute(null);
				}catch(Exception e){
					logger.error("[Selenium Server:]"+uc.getRcId()+"[Use Case:]"+uc.getUseCaseId()+" executeUseCase 出错！"+e.getMessage());
				}
			}	
		}
	}
}
