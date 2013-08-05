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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-13 - ����09:53:43
 * @version 1.0
 */
public class ReportJob implements Job {
	private static final Logger logger =  Logger.getLogger(ReportJob.class);
	/**
	 * @author ն��
	 * @param context
	 * @throws JobExecutionException
	 * 2011-5-13 - ����09:53:43
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//��ȡ����������  
		String triggerName = context.getTrigger().getName();  
		//���ݴ��������Ƶõ���Ӧ������Id  
		int reportId = Integer.valueOf(triggerName.split("_")[1]);
		
		//��ȡ����  
		sendReport(reportId, Constants.REPORT_EMAIL_ACCEPTOR);

	}
	
	/**
	 * ���ͱ���
	 * @author ն��
	 * @return
	 * 2011-5-15 - ����02:05:57
	 */
	private void sendReport(int reportId, String type){
		//��ʱ�ձ���ʱ�ж��Ƿ������ݣ�û�������򲻷���
		if (reportId == 1) {
			String searchDate = Utlitites.getMonitorDate();
			Map<Integer, MonitorVo> map;
			try {
				map = MonitorDayAo.get().findMonitorCountMapByDate(searchDate);
			} catch (Exception e) {
				logger.error("���������", e);
				sendWangWang(null, "��ȡ���ݿ��mapʱnull���������ֶ������ձ���");
				return;
			}
			if (map == null || map.size() == 0) {
				logger.error("���������");
				sendWangWang(null, "���ݿ���г�ȡ����mapΪ�գ����ֶ������ձ���");
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
				
				// cӦ��
				if(Utlitites.getLong(vo.getPv())<1&&Utlitites.getLong(vo.getApachePv())<1&&Utlitites.getLong(vo.getQpsNum())<1&&Utlitites.getLong(vo.getApacheQps())<1){ 
					long pv = vo.getAllHsfInterfacePv();
					if (pv == 0) {
						sendWangWang(appPo.getOpsName(), "HSFPV������Ϊ0");
						return;
					}
				} else {
					if (vo.getApachePv() == null || Long.parseLong(vo.getApachePv()) == 0) {
						sendWangWang(appPo.getOpsName(), "ApachePv������Ϊ0");
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
		wwSend.send("denghaichuan.pt@taobao.com", "�ձ��������쳣�ݶ�����", "Ӧ�ã�" + appName + "��" + str);
		
		MessageSend wwSend1 = MessageSendFactory.create(MessageSendType.WangWang);
		wwSend1.send("�Ĵ����", "�ձ��������쳣�ݶ�����", "Ӧ�ã�" + appName + "��" + str);
	}
	
	public static void main(String[] args) {
		ReportJob job = new ReportJob();
		job.sendReport(1, Constants.REPORT_EMAIL_ACCEPTOR);
	}

}
