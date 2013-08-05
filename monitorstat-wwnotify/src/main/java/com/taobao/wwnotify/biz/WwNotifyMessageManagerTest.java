package com.taobao.wwnotify.biz;

import org.apache.log4j.Logger;

public class WwNotifyMessageManagerTest {
	private static final Logger logger = Logger.getLogger(WwNotifyMessageManagerTest.class);

	public static void main(String[] args) throws Exception {
		/**
		 * ����������
		 */
		String host = "110.75.160.5";// wap.im.alisoft.com | 10.2.226.101
		// String host = "wap.im.alisoft.com";
		// String host = "10.250.3.54";
		/**
		 * �����˿ں�
		 */
		int port = 33445;

		/**
		 * Read��ʱ����
		 */
		int timeout = 1000;

		/**
		 * Connect��ʱ����
		 */
		int connectionTimeout = 1000;

		/**
		 * �߳���
		 */
		int threadSize = 2;

		/**
		 * ����߳���
		 */
		int maxThreadSize = 5;

		/**
		 * ʧ�����ʧ�ܴ������ֹͣ����ice�ӿ�
		 */
		int maxFailedCount = 1;

		WwNotifyMessageManager sender = new WwNotifyMessageManager();
		sender.setHost(host);
		sender.setPort(port);
		sender.setTimeout(timeout);
		sender.setConnectionTimeout(connectionTimeout);
		sender.setThreadSize(threadSize);
		sender.setMaxThreadSize(maxThreadSize);
		sender.setMaxFailedCount(maxFailedCount);
		sender.init();

		String uid = "С��";
		String subject = "����һ��_����_Title";
		String content = "����һ��_����_Content<a href='f'>fff</>";

		sender.sendNotifyMessage(uid,"ff", subject, content);
		sender.destroy();
	}
}
