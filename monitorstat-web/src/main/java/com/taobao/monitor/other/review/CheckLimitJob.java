package com.taobao.monitor.other.review;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.ServerInfoAo;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ServerInfoPo;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * ���ÿ��app����ʱ����ձ��еļ�¼��
 */

public class CheckLimitJob implements Job {
	
	
	private MonitorTimeAo ao = MonitorTimeAo.get();
	
	private MessageSend wwSend = MessageSendFactory.create(MessageSendType.WangWang);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		get();

	}
	
	/**
	 * ����ʱ���л�ȡӦ�õļ�¼������¼Ϊ0ʱ������
	 */
	
	public void get() {	
		
		List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
		for (ServerInfoPo po : serverInfoList) {
			StringBuffer sb = new StringBuffer();
			List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerId(po.getServerId());
			for (AppInfoPo appPo : appList) {
				if (appPo.getAppStatus() == 0 && appPo.getAppType().equalsIgnoreCase("time") && appPo.getTimeDeploy() == 1) {
					
					int countInLimit = ao.findAppCountInLimit(appPo.getAppId());// ����ʱ���и�Ӧ�õļ�¼��
					if(countInLimit == 0) {
						sb.append("Ӧ��"+appPo.getAppName()+"("+appPo.getAppId()+")"+"��ʱ���еļ�¼��Ϊ0; <br>"+"<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appPo.getAppId()+"'>�鿴��ϸҳ��</a><br>");
						
					}	
					int countInData = ao.findAppCountInData(appPo.getAppId(), new Date());// ���ձ��и�Ӧ�ð��Сʱ�ڵļ�¼��
					if(countInData == 0) {
						sb.append("Ӧ��"+appPo.getAppName()+"("+appPo.getAppId()+")"+"�־ñ��� ���Сʱ�ڵļ�¼��Ϊ0; <br>"+"<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appPo.getAppId()+"'>�鿴��ϸҳ��</a><br>");
					}
				}
			}
			if (sb.length() >0) {
				//wwSend.send("С��", "���ݲɼ��쳣", "<font color=\"#0033FF\">SCP������ ��"+po.getServerName()+"("+po.getServerIp()+") �����쳣</font><br>"+sb.toString());
				//wwSend.send("�Ĵ����", "���ݲɼ��쳣", "<font color=\"#0033FF\">SCP������ ��"+po.getServerName()+"("+po.getServerIp()+") �����쳣</font><br>"+sb.toString());
			}
		}
	}

}
