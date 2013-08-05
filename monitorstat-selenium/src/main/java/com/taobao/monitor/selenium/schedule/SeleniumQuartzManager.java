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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-13 - ����02:40:34
 * @version 1.0
 */
public class SeleniumQuartzManager {

	private static final Logger logger = Logger.getLogger(SeleniumQuartzManager.class);
	
	/**
	 * �������
	 * @author ն��
	 * @param name
	 * @param trigger
	 * @param c
	 * 2011-5-13 - ����02:44:13
	 */
	public static void addJob(String name, String quartzCron, Class jobClass){
		SeleniumSchedule.addJob(name, quartzCron, jobClass);
	}
	
	/**
	 * �޸�����Ĵ���ʱ��
	 * @author ն��
	 * @param jobName
	 * @param quartzCron Qutarzʱ����ʽ
	 * 2011-5-13 - ����02:45:48
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
//				// �޸�ʱ��  
//				ct.setCronExpression(quartzCron);  
//				// ����������  
//				sched.resumeTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);  
				
				JobDetail jobDetail = sched.getJobDetail(jobName, SeleniumConstant.JOB_GROUP_NAME); 
				removeJob(jobName, triggerName);  
				addJob(name, quartzCron, jobClass);
		    }  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager modifyJobTime����", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * ɾ������
	 * @author ն��
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - ����02:56:29
	 */
	public static void removeJob(String jobName, String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
		    sched.pauseTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// ֹͣ������  
		    sched.unscheduleJob(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// �Ƴ�������  
		    sched.deleteJob(jobName, SeleniumConstant.JOB_GROUP_NAME);// ɾ������  
			String[] jobGroups = sched.getJobNames(SeleniumConstant.JOB_GROUP_NAME);
			String[] triggers = sched.getTriggerNames(SeleniumConstant.TRIGGER_GROUP_NAME);
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager removeJob����", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * ��ͣ����
	 * @author ն��
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - ����02:56:29
	 */
	public static void pauseJob(String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
		    sched.pauseTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);// ֹͣ������  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager pauseJob����", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * ��ͣ����
	 * @author ն��
	 * @param jobName
	 * @param triggerName
	 * 2011-5-13 - ����02:56:29
	 */
	public static void resumeJob(String jobName, String triggerName){
		try {  
			Scheduler sched = SeleniumSchedule.getScheduler();  
			sched.resumeTrigger(triggerName, SeleniumConstant.TRIGGER_GROUP_NAME);  
		} catch (Exception e) {  
			logger.error("SeleniumQuartzManager pauseJob����", e);  
		    //throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * ��ȡ��ǰjob����״̬
	 * @author ն��
	 * @param triggerName
	 * @return
	 * 2011-5-13 - ����07:16:08
	 */
	public static int getTaskState(String triggerName){
		int state = -1;
		try {
			Scheduler scheduler =  SeleniumSchedule.getScheduler();
			state = scheduler.getTriggerState(triggerName,
					SeleniumConstant.TRIGGER_GROUP_NAME);
		} catch (SchedulerException e) {
			logger.error("SeleniumQuartzManager getJobStateMap����", e);  
		}	
		return state;
	}
	
	/**
	 * ��ȡ��ǰjob����״̬
	 * @author ն��
	 * @param triggerName
	 * @return
	 * 2011-5-13 - ����07:16:08
	 */
	public static String getTaskStateMsg(String triggerName){
		int state = getTaskState(triggerName);
		String stateStr = Constants.triggerStateMap().get(state);
		if(stateStr != null && !stateStr.equals("")){
			return stateStr;
		}else{
			return "δ֪״̬";
		}
	}
	
}
