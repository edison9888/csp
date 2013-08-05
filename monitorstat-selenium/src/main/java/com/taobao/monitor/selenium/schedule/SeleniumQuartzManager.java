/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.schedule;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.selenium.util.SeleniumConstant;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-13 - 下午02:40:34
 * @version 1.0
 */
public class SeleniumQuartzManager {

	private static final Logger logger = Logger.getLogger(SeleniumQuartzManager.class);
	
	/**
	 * 添加任务
	 * @author 斩飞
	 * @param name
	 * @param trigger
	 * @param c
	 * 2011-5-13 - 下午02:44:13
	 */
	public static void addJob(String name, String quartzCron, Class jobClass){
		SeleniumSchedule.addJob(name, quartzCron, jobClass);
	}
	
	/**
	 * 修改任务的触发时间
	 * @author 斩飞
	 * @param jobName
	 * @param quartzCron Qutarz时间表达式
	 * 2011-5-13 - 下午02:45:48
	 */
	public static void modifyJobTime(String name, String jobName ,String triggerName, 
			String quartzCron, Class jobClass){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
			String[] jobGroups = sched.getJobNames(SeleniumConstant.JOB_GROUP_NAME);
			String[] triggers = sched.getTriggerNames(SeleniumConstant.TRIGGER_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,
					SeleniumConstant.TRIGGER_GROUP_NAME);  
			if(trigger == null) {  
				addJob(name, quartzCron, jobClass);
			    return;  
			} 
			String oldTime = trigger.getCronExpression();  
			if (!oldTime.equalsIgnoreCase(quartzCron)) {  
				CronTrigger ct = (CronTrigger) trigger; 
//				// 修改时间  
//				ct.setCronExpression(quartzCron);  
//				// 重启触发器  
//				sched.resumeTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);  
				
				JobDetail jobDetail = sched.getJobDetail(jobName, SeleniumConstant.JOB_GROUP_NAME); 
				removeJob(jobName, triggerName);  
				addJob(name, quartzCron, jobClass);
		    }  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager modifyJobTime出错", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * 删除任务
	 * @author 斩飞
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - 下午02:56:29
	 */
	public static void removeJob(String jobName, String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
		    sched.pauseTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// 停止触发器  
		    sched.unscheduleJob(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// 移除触发器  
		    sched.deleteJob(jobName, SeleniumConstant.JOB_GROUP_NAME);// 删除任务  
			String[] jobGroups = sched.getJobNames(SeleniumConstant.JOB_GROUP_NAME);
			String[] triggers = sched.getTriggerNames(SeleniumConstant.TRIGGER_GROUP_NAME);
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager removeJob出错", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * 暂停任务
	 * @author 斩飞
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - 下午02:56:29
	 */
	public static void pauseJob(String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
		    sched.pauseTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// 停止触发器  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager pauseJob出错", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * 暂停任务
	 * @author 斩飞
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - 下午02:56:29
	 */
	public static void resumeJob(String jobName, String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
			sched.resumeTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager pauseJob出错", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * 获取当前job运行状态
	 * @author 斩飞
	 * @param triggerName
	 * @return
	 * 2011-5-13 - 下午07:16:08
	 */
	public static int getTaskState(String triggerName){
		int state = -1;
		try {
			Scheduler scheduler =  SeleniumSchedule.getScheduler();
			state = scheduler.getTriggerState(triggerName,
					SeleniumConstant.TRIGGER_GROUP_NAME);
		} catch (SchedulerException e) {
			logger.error("SeleniumQuartzManager getJobStateMap出错", e);  
		}	
		return state;
	}
	
	/**
	 * 获取当前job运行状态
	 * @author 斩飞
	 * @param triggerName
	 * @return
	 * 2011-5-13 - 下午07:16:08
	 */
	public static String getTaskStateMsg(String triggerName){
		int state = getTaskState(triggerName);
		String stateStr = Constants.triggerStateMap().get(state);
		if(stateStr != null && !stateStr.equals("")){
			return stateStr;
		}else{
			return "未知状态";
		}
	}
	
}
