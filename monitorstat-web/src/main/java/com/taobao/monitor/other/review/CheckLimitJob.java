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
 * 检查每个app在临时表和日表中的记录数
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
	 * 在临时表中获取应用的记录数，记录为0时，报警
	 */
	
	public void get() {	
		
		List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
		for (ServerInfoPo po : serverInfoList) {
			StringBuffer sb = new StringBuffer();
			List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerId(po.getServerId());
			for (AppInfoPo appPo : appList) {
				if (appPo.getAppStatus() == 0 && appPo.getAppType().equalsIgnoreCase("time") && appPo.getTimeDeploy() == 1) {
					
					int countInLimit = ao.findAppCountInLimit(appPo.getAppId());// 在临时表中该应用的记录数
					if(countInLimit == 0) {
						sb.append("应用"+appPo.getAppName()+"("+appPo.getAppId()+")"+"临时表中的记录数为0; <br>"+"<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appPo.getAppId()+"'>查看详细页面</a><br>");
						
					}	
					int countInData = ao.findAppCountInData(appPo.getAppId(), new Date());// 在日表中该应用半个小时内的记录数
					if(countInData == 0) {
						sb.append("应用"+appPo.getAppName()+"("+appPo.getAppId()+")"+"持久表中 半个小时内的记录数为0; <br>"+"<a href='http://cm.taobao.net:9999/monitorstat/time/app_time.jsp?appId="+appPo.getAppId()+"'>查看详细页面</a><br>");
					}
				}
			}
			if (sb.length() >0) {
				//wwSend.send("小赌", "数据采集异常", "<font color=\"#0033FF\">SCP服务器 ："+po.getServerName()+"("+po.getServerIp()+") 数据异常</font><br>"+sb.toString());
				//wwSend.send("澳大军阀", "数据采集异常", "<font color=\"#0033FF\">SCP服务器 ："+po.getServerName()+"("+po.getServerIp()+") 数据异常</font><br>"+sb.toString());
			}
		}
	}

}
