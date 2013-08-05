package com.taobao.monitor.alarm.compare;

import java.math.BigDecimal;
import java.util.Map;


public class MinituMonitorAlarm extends MonitorAlarm {

	@Override
	public boolean needAlarm(long nowMilliTime, int appId, int keyId, BigDecimal nowData) {
		// ��׼ʱ��Ϊ��ǰʱ���������֮ǰ
		Map<String, BigDecimal> mapData = getCompareData(nowMilliTime, appId, keyId, 0, -2, true);
		BigDecimal historyData = MonitorAlarmHelper.getAverageData(mapData);
		
		// ��ֹ��0�жϣ����ϻ���Ӧ�ò������
		if(historyData.intValue() == 0) {
			historyData = new BigDecimal("1");
		}
		
		boolean needAlarm = nowData.divide(historyData, 2, BigDecimal.ROUND_HALF_UP).compareTo(getAlarmRatio()) < 0 ? true : false;
		
		if (needAlarm) {
			setAlarmInfo("��ǰ��������ͳ������Ϊ:" + nowData + ",������ǰ��ͳ������Ϊ:" + historyData + ". ��ֵ����Ԥ������:" + getAlarmRatio());
		}
		
		return needAlarm;
	}


}
