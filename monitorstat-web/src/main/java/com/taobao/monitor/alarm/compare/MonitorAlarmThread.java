package com.taobao.monitor.alarm.compare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.transfer.WangwangTransfer;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/***
 * 
 * @author youji.zj
 * 
 * 线程在InitServlet中启动，进行监听，不正常时进行报警
 *
 */
public class MonitorAlarmThread extends Thread {

	private static final Logger logger = Logger.getLogger(MonitorAlarmThread.class);
	
	private final String[] ALARM_MEMBERS = { "游骥" };
	
	private final long WAIT = 1L *  60 * 1000;
	
	public MonitorAlarmThread() {

	}
	
	public static void startup() {
		new MonitorAlarmThread().start();
	}
	
	@Override
	public void run() {

		List<MonitorAlarmCase> alarmCaseList = new ArrayList<MonitorAlarmCase>();
		
		// 注册要监听的case
		MonitorAlarmCase testMonitorAlarmCase = new MonitorAlarmCase(330, 60873);
		// 注册case的报警比较算法
		testMonitorAlarmCase.getMonitorList().add(new MinituMonitorAlarm());
		testMonitorAlarmCase.getMonitorList().add(new WeekMonitorAlarm());
		
		alarmCaseList.add(testMonitorAlarmCase);
		
		while (true) {
			for (MonitorAlarmCase monitorAlarmCase : alarmCaseList) {
				// 确定好当前时间，取一分钟之前的原因在于当前这一分钟还没统计完，数据会比较少
				Calendar calendar =Calendar.getInstance();
				calendar.setTime(MonitorTimeAo.get().findLatestDateInLimit(monitorAlarmCase.getAppId(), monitorAlarmCase.getKeyId()));
				calendar.add(Calendar.MINUTE, -1);
				long nowInMillis = calendar.getTimeInMillis();
				Date nowEnd = calendar.getTime();
				Date nowStart = MonitorAlarmHelper.getPreviousDate(nowInMillis, 0, -2);
				
				// 取基准时间，平均每台服务器的平均值作为基准值
				Map<String, BigDecimal> nowStatisticsData = MonitorTimeAo.get().findToalInRangeDate(monitorAlarmCase.getAppId(), monitorAlarmCase.getKeyId(), nowStart, nowEnd, true);
				BigDecimal nowStatisticsAverage = MonitorAlarmHelper.getAverageData(nowStatisticsData);

				String alarmDetialMessage = "";
				boolean alarmFlag = true;
				for(MonitorAlarm monitorAlarm : monitorAlarmCase.getMonitorList()) {
					boolean needAlarm = monitorAlarm.needAlarm(nowInMillis, monitorAlarmCase.getAppId(), monitorAlarmCase.getKeyId(), nowStatisticsAverage);
					if (needAlarm) {
						alarmDetialMessage += monitorAlarm.getAlarmInfo() + "<br/>";
					}
					alarmFlag = alarmFlag && needAlarm;
				}
				
				// 全部不通过就报警
				if (alarmFlag) {
					alarm(getTile(), getMessage(monitorAlarmCase.getAppId(), monitorAlarmCase.getKeyId(), alarmDetialMessage));
				}
			}
		
			try {
				Thread.sleep(WAIT);
			} catch (InterruptedException e) {
				logger.error(e.getStackTrace());
			}

		}
	}
	
	private void alarm(String titel, String message) {
		for (String member : ALARM_MEMBERS) {
			WangwangTransfer.getInstance().sendExtraMessage(member, titel, message);
		}	
	}
	
	private String getTile() {
		return "统计信息报警";
	}
	
	private String getMessage(int appId, int keyId, String monitorMessage) {
		StringBuilder message = new StringBuilder();
		message.append("appId:").append(appId).append(", keyId:").append(keyId).append("出现异常.");
		message.append("<br/>");
		message.append(monitorMessage);
		message.append("<br/>");
		message.append("<a  href=\"");
		message.append("http://cm.taobao.net:9999/monitorstat/time/key_detail_time.jsp?appId=").append(appId).append("&keyId=").append(keyId);
		message.append("\">");
		message.append("对应性能图");
		message.append("</a>");
		
		return message.toString();
	}
}
