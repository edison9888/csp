/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.vo.MonitorVo;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-13 - 上午09:53:43
 * @version 1.0
 */
public class ReportJob implements Job {
	private static final Logger logger =  Logger.getLogger(ReportJob.class);
	/**
	 * @author 斩飞
	 * @param context
	 * @throws JobExecutionException
	 * 2011-5-13 - 上午09:53:43
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//获取触发器名称  
		String triggerName = context.getTrigger().getName();  
		//根据触发器名称得到对应的任务Id  
		int reportId = Integer.valueOf(triggerName.split("_")[1]);
		
		//获取任务  
		sendReport(reportId, Constants.REPORT_EMAIL_ACCEPTOR);

	}
	
	/**
	 * 发送报表
	 * @author 斩飞
	 * @return
	 * 2011-5-15 - 下午02:05:57
	 */
	private void sendReport(int reportId, String type){
		//当时日报表时判断是否有数据，没有数据则不发送
		if (reportId == 1) {
			String searchDate = Utlitites.getMonitorDate();
			Map<Integer, MonitorVo> map;
			try {
				map = MonitorDayAo.get().findMonitorCountMapByDate(searchDate);
			} catch (Exception e) {
				logger.error("发报表出错", e);
				sendWangWang(null, "抽取数据库的map时null，出错，请手动发送日报表");
				return;
			}
			if (map == null || map.size() == 0) {
				logger.error("发报表出错");
				sendWangWang(null, "数据库表中抽取出的map为空，请手动发送日报表");
				return;
			}
			List<Integer> appIdList = new ArrayList<Integer>();
			appIdList.add(2);
			appIdList.add(3);
			appIdList.add(1);
			appIdList.add(322);
			appIdList.add(330);
			appIdList.add(8);
			appIdList.add(4);
			appIdList.add(323);
			appIdList.add(32);
			appIdList.add(35);
			appIdList.add(341);
			appIdList.add(338);
			for(Integer appid:appIdList){
				AppInfoPo appPo = AppCache.get().getDayAppId(appid);
				if (appPo == null) {
					continue;
				}
				MonitorVo vo = map.get(appid);
				
				// c应用
				if(Utlitites.getLong(vo.getPv())<1&&Utlitites.getLong(vo.getApachePv())<1&&Utlitites.getLong(vo.getQpsNum())<1&&Utlitites.getLong(vo.getApacheQps())<1){ 
					long pv = vo.getAllHsfInterfacePv();
					if (pv == 0) {
						sendWangWang(appPo.getOpsName(), "HSFPV的数据为0");
						return;
					}
				} else {
					if (vo.getApachePv() == null || Long.parseLong(vo.getApachePv()) == 0) {
						sendWangWang(appPo.getOpsName(), "ApachePv的数据为0");
						return;
					}
				}
			}
			SendReportMessage.sendReport(reportId, type);
		} else {
			SendReportMessage.sendReport(reportId, type);
		}
	}
	
	private void sendWangWang(String appName, String str) {
		MessageSend wwSend = MessageSendFactory.create(MessageSendType.Email);
		wwSend.send("denghaichuan.pt@taobao.com", "日报表数据异常暂定发送", "应用：" + appName + "的" + str);
		
		MessageSend wwSend1 = MessageSendFactory.create(MessageSendType.WangWang);
		wwSend1.send("澳大军阀", "日报表数据异常暂定发送", "应用：" + appName + "的" + str);
	}
	
	public static void main(String[] args) {
		ReportJob job = new ReportJob();
		job.sendReport(1, Constants.REPORT_EMAIL_ACCEPTOR);
	}

}
