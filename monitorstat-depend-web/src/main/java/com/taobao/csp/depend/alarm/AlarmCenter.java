package com.taobao.csp.depend.alarm;

import java.util.Date;

import org.apache.log4j.Logger;

public class AlarmCenter {
	private static final Logger logger = Logger.getLogger(AlarmCenter.class);
	//���򴥷�
	public void startAlarmCheck() {
		logger.info("��ʱ���񴥷���ʱ��->" + new Date());
		new HSFProviderAlarm().alarm(null);
	}
	
	//�ֶ�����
	public void startAlarmCheckMannual(String appName) {
		logger.info("�ֶ�������ʱ��->" + new Date() + ";appName=" + appName);
		new HSFProviderAlarm().alarm(appName);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AlarmCenter().startAlarmCheck();
	}

}
