
package com.taobao.wwnotify.biz;

import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2010-11-29 下午04:42:28
 */
public class WwNotify {
	
	private static final Logger logger =  Logger.getLogger(WwNotify.class);
	
	private static WwNotify wwNotify = new WwNotify();
	
	private WwNotifyMessageManager sender = new WwNotifyMessageManager();
	
	/**
	 * 旺旺服务器
	 */
	private String host = "110.75.160.5";// wap.im.alisoft.com | 10.2.226.101
	// String host = "wap.im.alisoft.com";
	// String host = "10.250.3.54";
	/**
	 * 旺旺端口号
	 */
	private int port = 33445;

	/**
	 * Read超时毫秒
	 */
	private int timeout = 1000;

	/**
	 * Connect超时毫秒
	 */
	private int connectionTimeout = 1000;

	/**
	 * 线程数
	 */
	private int threadSize = 2;

	/**
	 * 最大线程数
	 */
	private int maxThreadSize = 5;

	/**
	 * 失败最大失败次数后就停止调用ice接口
	 */
	private int maxFailedCount = 1;
	
	private WwNotify(){
		sender.setHost(host);
		sender.setPort(port);
		sender.setTimeout(timeout);
		sender.setConnectionTimeout(connectionTimeout);
		sender.setThreadSize(threadSize);
		sender.setMaxThreadSize(maxThreadSize);
		sender.setMaxFailedCount(maxFailedCount);
		sender.init();
	}
	
	public static WwNotify get(){
		return wwNotify;
	}
	
	
	public boolean sendWWMessage(String uid,String maintitle,String subject,String content){
		
		try {
			sender.sendNotifyMessage(uid, maintitle, subject, content);
		} catch (WwNotifyException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	
	
	

}
