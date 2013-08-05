package com.taobao.wwnotify.biz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.wwnotify.biz.wwv2.WWMessageInterfacePrx;
import com.taobao.wwnotify.biz.wwv2.WWMessageInterfacePrxHelper;


/**
 * 
 * @author wzm2162
 * 
 */
public class WwNotifyMessageManager {
	private static final Logger logger = Logger.getLogger(WwNotifyMessageManager.class);
	private static final String ICE_SERVICE_NAME = "AliWWMessageIAgentId";
	private static final int SAVE_MESSAGE_FLAG = 0x01;
	private static final String USER_ID_PREFIX_TAOBAO = "cntaobao";
	private static final String USER_ID_PREFIX_B2B = "cnalichn";
	private static final String DATE_FORMAT_PATTEN = "yyyyMMddHHmmss";
	private static final int DEFAULT_TIME_OUT = 1000;
	private static final int DEFAULT_CONNECTION_TIME_OUT = 1000;
	private static final int MAX_FAILD_COUNT = 5000;
	private String host;
	private int port;
	private int timeout = DEFAULT_TIME_OUT;
	private int connectionTimeout = DEFAULT_CONNECTION_TIME_OUT;
	private int threadSize = 1;
	private int maxThreadSize = 5;
	private int maxFailedCount = MAX_FAILD_COUNT;
	private Ice.Communicator ic = null;
	private WWMessageInterfacePrx prx = null;
	private int failedCount = 0;

	public void init() {
		try {
			Ice.InitializationData initData = new Ice.InitializationData();
			Ice.Properties properties = Ice.Util.createProperties();
			properties.setProperty("Ice.Override.ConnectTimeout", String.valueOf(this.connectionTimeout));
			properties.setProperty("Ice.Override.Timeout", String.valueOf(this.timeout));
			properties.setProperty("Ice.ThreadPool.Client.Size", String.valueOf(this.threadSize));
			properties.setProperty("Ice.ThreadPool.Client.SizeWarn", String.valueOf(this.maxThreadSize));
			properties.setProperty("Ice.ThreadPool.Client.SizeMax", String.valueOf(this.maxThreadSize));
			initData.properties = properties;
			this.ic = Ice.Util.initialize(initData);
			this.prx = this.openPrx();
		} catch (Exception ex) {
			logger.error(ex, ex);
			if (this.ic != null) {
				try {
					this.ic.destroy();
				} catch (Exception e) {
				}
			}
		}
	}

	public void destroy() {
		this.failedCount = 0;
		if (null != this.ic) {
			try {
				this.ic.destroy();
			} catch (Exception ex) {
				logger.warn(ex, ex);
			}
		}
	}

	private WWMessageInterfacePrx openPrx() throws WwNotifyException {
		StringBuffer connStr = new StringBuffer(ICE_SERVICE_NAME);
		connStr.append(" -t:tcp -p ");
		connStr.append(this.port);
		connStr.append(" -h ");
		connStr.append(this.host);
		connStr.append(" -t ");
		connStr.append(this.timeout);

		if (logger.isDebugEnabled()) {
			logger.debug(connStr.toString());
		}

		Ice.ObjectPrx objectPrx = this.ic.stringToProxy(connStr.toString());
		objectPrx = objectPrx.ice_timeout(timeout);
		if (null == objectPrx) {
			throw new WwNotifyException("I cant get Ice.ObjectPrx");
		}

		WWMessageInterfacePrx prx = WWMessageInterfacePrxHelper.checkedCast(objectPrx);
		if (null == prx) {
			throw new WwNotifyException("I cant get systemMsgInterfacePrx");
		}

		return prx;
	}

	private void onFailed() {
		this.failedCount++;

		if (this.failedCount > this.maxFailedCount) {
			logger.error("ice连接失败次数过多，自动断开!! failedCount=> " + failedCount);
		}

		if (this.failedCount >= this.maxFailedCount * 2) {
			this.failedCount = 0;
			logger.error("失败次数达到了双倍maxFailedCount, 则failedCount清零, 重新连接Ice!!");
		}
		this.prx = null;
	}

	private void onSuccess() {
		this.failedCount = 0;
	}

	public void finalize() throws Throwable {
		this.destroy();
		super.finalize();
	}

	public void sendNotifyMessage(String uid, String maintitle,String subject, String content) throws WwNotifyException {
		sendNotifyMessage("taobao", uid,maintitle, subject, content);
	}

	public void sendNotifyMessage(String from, String uid, String maintitle,String subject, String content) throws WwNotifyException {
		if (uid == null || subject == null || content == null) {
			throw new WwNotifyException("uid or subject or content is blank!!");
		}

		if (this.failedCount > this.maxFailedCount) {
			this.onFailed();
			return;
		}

		try {
			if (null == this.prx) {
				this.prx = this.openPrx();
			}

			uid = uid.trim();

			if ("taobao".equals(from)) {
				uid = USER_ID_PREFIX_TAOBAO + uid;
			} else if ("b2b".equals(from)) {
				uid = USER_ID_PREFIX_B2B + uid;
			}

			StringBuffer strBuffer = new StringBuffer();
			DateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTEN);
			String dateStr = df.format(new Date());
			strBuffer.append(dateStr);
			strBuffer.append("||");
			strBuffer.append(subject);
			strBuffer.append("||");
			strBuffer.append(content);
			if (logger.isDebugEnabled()) {
				logger.debug(uid + " : " + strBuffer.toString());
			}

			// add display mode
			Map<String, String> params = new HashMap<String, String>();
			params.put("maintitle", maintitle);
			params.put("subtitle", subject);
			params.put("sendtime", dateStr);
			params.put("displaymode", "1");
			params.put("message", content);

			Ice.StringHolder retHolder = new Ice.StringHolder();

			if ("taobao".equals(from)) {
				this.prx.SendNotifyMessageEx("cntaobaoInfoPlatForm", "cntaobaoInfoPlatFormType", uid, strBuffer.toString(), SAVE_MESSAGE_FLAG, params, retHolder);
				//this.prx.SendNotifyMessage("cntaobaoInfoPlatForm", "cntaobaoInfoPlatFormType", uid, strBuffer.toString(), SAVE_MESSAGE_FLAG, retHolder);
			} else if ("b2b".equals(from)) {
				this.prx.SendNotifyMessage("cntaobaoAdminSystem", "cntaobaoAdminSystemType", uid, strBuffer.toString(), SAVE_MESSAGE_FLAG, retHolder);
			}

			this.onSuccess();
		} catch (Exception ex) {
			this.onFailed();
			throw new WwNotifyException(ex);
		}
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getConnectionTimeout() {
		return this.connectionTimeout;
	}

	public int getThreadSize() {
		return this.threadSize;
	}

	public int getMaxThreadSize() {
		return this.maxThreadSize;
	}

	public int getMaxFailedCount() {
		return this.maxFailedCount;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
	}

	public void setMaxThreadSize(int maxThreadSize) {
		this.maxThreadSize = maxThreadSize;
	}

	public void setMaxFailedCount(int maxFailedCount) {
		this.maxFailedCount = maxFailedCount;
	}
}
