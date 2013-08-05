/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.schedule;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.taobao.monitor.common.time.ScheduleContainer;
import com.taobao.monitor.common.util.Constants;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-13 - ����02:40:34
 * @version 1.0
 */
public class QuartzManager {

	private static final Logger logger = Logger.getLogger(QuartzManager.class);
	
	/**
	 * �������
	 * @author ն��
	 * @param name
	 * @param trigger
	 * @param c
	 * 2011-5-13 - ����02:44:13
	 */
	public static void addJob(String name, String quartzCron, Class jobClass){
		ScheduleContainer.addJob(name, quartzCron, jobClass);
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
			Scheduler sched = ScheduleContainer.getScheduler();  
			String[] jobGroups = sched.getJobNames(Constants.JOB_GROUP_NAME);
			String[] triggers = sched.getTriggerNames(Constants.TRIGGER_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,
					Constants.TRIGGER_GROUP_NAME);  
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
//				sched.resumeTrigger(triggerName, Constants.TRIGGER_GROUP_NAME);  
				
				JobDetail jobDetail = sched.getJobDetail(jobName, Constants.JOB_GROUP_NAME); 
				removeJob(jobName, triggerName);  
				addJob(name, quartzCron, jobClass);
		    }  
		} catch (Exception e) {  
			logger.error("modifyJobTime����", e);  
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
			Scheduler sched = ScheduleContainer.getScheduler();  
		    sched.pauseTrigger(triggerName, Constants.TRIGGER_GROUP_NAME);// ֹͣ������  
		    sched.unscheduleJob(triggerName, Constants.TRIGGER_GROUP_NAME);// �Ƴ�������  
		    sched.deleteJob(jobName, Constants.JOB_GROUP_NAME);// ɾ������  
			String[] jobGroups = sched.getJobNames(Constants.JOB_GROUP_NAME);
			String[] triggers = sched.getTriggerNames(Constants.TRIGGER_GROUP_NAME);
		} catch (Exception e) {  
			logger.error("removeJob����", e);  
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
			Scheduler sched = ScheduleContainer.getScheduler();  
		    sched.pauseTrigger(triggerName, Constants.TRIGGER_GROUP_NAME);// ֹͣ������  
		} catch (Exception e) {  
			logger.error("pauseJob����", e);  
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
			Scheduler sched = ScheduleContainer.getScheduler();  
			sched.resumeTrigger(triggerName, Constants.TRIGGER_GROUP_NAME);  
		} catch (Exception e) {  
			logger.error("pauseJob����", e);  
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
			Scheduler scheduler =  ScheduleContainer.getScheduler();
			state = scheduler.getTriggerState(triggerName,
					Constants.TRIGGER_GROUP_NAME);
		} catch (SchedulerException e) {
			logger.error("getJobStateMap����", e);  
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
