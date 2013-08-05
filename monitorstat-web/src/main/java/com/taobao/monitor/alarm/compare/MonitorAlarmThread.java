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
 * �߳���InitServlet�����������м�����������ʱ���б���
 *
 */
public class MonitorAlarmThread extends Thread {

	private static final Logger logger = Logger.getLogger(MonitorAlarmThread.class);
	
	private final String[] ALARM_MEMBERS = { "����" };
	
	private final long WAIT = 1L *  60 * 1000;
	
	public MonitorAlarmThread() {

	}
	
	public static void startup() {
		new MonitorAlarmThread().start();
	}
	
	@Override
	public void run() {

		List<MonitorAlarmCase> alarmCaseList = new ArrayList<MonitorAlarmCase>();
		
		// ע��Ҫ������case
		MonitorAlarmCase testMonitorAlarmCase = new MonitorAlarmCase(330, 60873);
		// ע��case�ı����Ƚ��㷨
		testMonitorAlarmCase.getMonitorList().add(new MinituMonitorAlarm());
		testMonitorAlarmCase.getMonitorList().add(new WeekMonitorAlarm());
		
		alarmCaseList.add(testMonitorAlarmCase);
		
		while (true) {
			for (MonitorAlarmCase monitorAlarmCase : alarmCaseList) {
				// ȷ���õ�ǰʱ�䣬ȡһ����֮ǰ��ԭ�����ڵ�ǰ��һ���ӻ�ûͳ���꣬���ݻ�Ƚ���
				Calendar calendar =Calendar.getInstance();
				calendar.setTime(MonitorTimeAo.get().findLatestDateInLimit(monitorAlarmCase.getAppId(), monitorAlarmCase.getKeyId()));
				calendar.add(Calendar.MINUTE, -1);
				long nowInMillis = calendar.getTimeInMillis();
				Date nowEnd = calendar.getTime();
				Date nowStart = MonitorAlarmHelper.getPreviousDate(nowInMillis, 0, -2);
				
				// ȡ��׼ʱ�䣬ƽ��ÿ̨��������ƽ��ֵ��Ϊ��׼ֵ
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
				
				// ȫ����ͨ���ͱ���
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
		return "ͳ����Ϣ����";
	}
	
	private String getMessage(int appId, int keyId, String monitorMessage) {
		StringBuilder message = new StringBuilder();
		message.append("appId:").append(appId).append(", keyId:").append(keyId).append("�����쳣.");
		message.append("<br/>");
		message.append(monitorMessage);
		message.append("<br/>");
		message.append("<a  href=\"");
		message.append("http://cm.taobao.net:9999/monitorstat/time/key_detail_time.jsp?appId=").append(appId).append("&keyId=").append(keyId);
		message.append("\">");
		message.append("��Ӧ����ͼ");
		message.append("</a>");
		
		return message.toString();
	}
}
