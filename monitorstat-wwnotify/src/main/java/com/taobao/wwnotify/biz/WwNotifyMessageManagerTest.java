package com.taobao.wwnotify.biz;

import org.apache.log4j.Logger;

public class WwNotifyMessageManagerTest {
	private static final Logger logger = Logger.getLogger(WwNotifyMessageManagerTest.class);

	public static void main(String[] args) throws Exception {
		/**
		 * 旺旺服务器
		 */
		String host = "110.75.160.5";// wap.im.alisoft.com | 10.2.226.101
		// String host = "wap.im.alisoft.com";
		// String host = "10.250.3.54";
		/**
		 * 旺旺端口号
		 */
		int port = 33445;

		/**
		 * Read超时毫秒
		 */
		int timeout = 1000;

		/**
		 * Connect超时毫秒
		 */
		int connectionTimeout = 1000;

		/**
		 * 线程数
		 */
		int threadSize = 2;

		/**
		 * 最大线程数
		 */
		int maxThreadSize = 5;

		/**
		 * 失败最大失败次数后就停止调用ice接口
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

		String uid = "小赌";
		String subject = "测试一下_标题_Title";
		String content = "测试一下_内容_Content<a href='f'>fff</>";

		sender.sendNotifyMessage(uid,"ff", subject, content);
		sender.destroy();
	}
}
