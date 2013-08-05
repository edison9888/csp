package com.taobao.csp.day.apache;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.base.FetcherListener;
import com.taobao.csp.day.base.Log;

/***
 * 数据采集任务
 * @author youji.zj 2012-08-20 
 * 
 * @version 1.0
 */
public class ApacheSpecialLogDataFetcherTask implements Runnable {
	
	public static Logger logger = Logger.getLogger(ApacheSpecialLogDataFetcherTask.class);
	
	private final static String FILE_BEGIN = "file-begin";
	
	/*** 分析器 ***/
	private AbstractAnalyser analyser;
	
	/*** 应用名 ***/
	private String appName;
	
	/*** 采集的机器ip ***/
	private final String ip;
	
	/*** 数据类型 ***/
	private final DataType dataType;
	
	/*** 当前采集的日志路径 ***/
	private volatile String logPath;

	/*** 一次采集的大小 ***/
	private volatile long fetchSize;
	
	/*** 当前取的位置 ***/
	private volatile long position;
	
	/*** 是否有效 ***/
	private volatile boolean valiable = true;
	
	/*** 任务的运行标志 ***/
	private volatile boolean run;
	
	/*** 限制position的位置 ***/
	private volatile long limitPosition = Long.MAX_VALUE;
	
	private volatile boolean collect = false;
	
	/*** 回调 ***/
	private List<FetcherListener> listeners = new ArrayList<FetcherListener>();
	
	public ApacheSpecialLogDataFetcherTask(AbstractAnalyser analyse, String appName, String ip, DataType dataType, String realLogPath, long fetchSize) {
		this.analyser = analyse;
		this.appName = appName;
		this.ip = ip;
		this.dataType = dataType;
		this.logPath = realLogPath;
		this.fetchSize = fetchSize;
	}

	public AbstractAnalyser getAnalyser() {
		return analyser;
	}

	public void setAnalyser(AbstractAnalyser analyser) {
		this.analyser = analyser;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIp() {
		return ip;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public long getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(long fetchSize) {
		this.fetchSize = fetchSize;
	}
	
	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public boolean isValiable() {
		return valiable;
	}

	public void setValiable(boolean valiable) {
		this.valiable = valiable;
	}

	public List<FetcherListener> getListeners() {
		return listeners;
	}
	
	public void setListeners(List<FetcherListener> listeners) {
		this.listeners = listeners;
	}
	
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	
	public long getLimitPosition() {
		return limitPosition;
	}

	public void setLimitPosition(long limitPosition) {
		this.limitPosition = limitPosition;
	}

	public void waitDone() {
		while (this.isRun()) {
			try {
				logger.info(this.toString() + " wait for done...");
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				logger.warn(e);
			}
		}
	}

	/***
	 * 具有唯一性
	 * @return
	 */
	public String identityKey() {
		return this.toString();
	}
	
	/***
	 * 任务提交时的identity，根据identity来对应spout
	 * @return
	 */
	public int filterIdentity() {
		return this.toString().hashCode() & 0x7FFFFFFF;
	}

	/***
	 * 同一个task不能多次执行，加了同步锁
	 */
	@Override
	public synchronized void run() {
		try {
			if (!isValiable()) {
				String tipInfo = this.getAppName() + " " + this.getIp() + " " + this.getDataType() + " " + this.getLogPath();
				logger.info(tipInfo + ", not available");
				return;
			}

			this.waitDone();
			this.setRun(true);
			
			while (true) {
				HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

				String url = "http://" + this.getIp() + ":8082/get"
						+ this.getLogPath() + "?" + "begin=" + this.getPosition()
						+ "&end=" + (this.getPosition() + this.getFetchSize())
						+ "&encode=text";
				
				GetMethod httpGet = new GetMethod(url);
				logger.debug(url + " begin to get data");

				int httpStatus = httpClient.executeMethod(httpGet);
				Header header = httpGet.getResponseHeader(FILE_BEGIN);

				int dataSize = 0;
				if (httpStatus == HttpStatus.SC_OK) {
					// 当file-begin为0，position不为0，begin已经大于或者等于文件大小，说明需要切换日志
					logger.debug("file begin: " + header.getValue().trim() + ", position:" + this.getPosition());

					StringBuffer contentBuffer = new StringBuffer();
					InputStream in = httpGet.getResponseBodyAsStream();
					byte[] byteA = new byte[1024];
					int size = 0;
					while ((size = in.read(byteA)) > 0) {
						this.setPosition(this.getPosition() + size);
						contentBuffer.append(new String(byteA, 0, size, "GBK"));
						dataSize += size;
					}

					this.checkPositionAvailable();

					logger.info(url + "get data size:" + dataSize);
					String content = contentBuffer.toString();
					if (!collect && content.indexOf("10/Nov/2012:23") > 0) {
						collect = true;
					}
						
					if (!collect) {
						continue;
					}

					List<Log> logPoL = this.getAnalyser().analyse(content);
					for (FetcherListener listener : this.getListeners()) {
						listener.onGenerateLogPo(logPoL);
					}
					break;

				} else {
					this.setValiable(false);
				}
			}

			this.setRun(false);
		} catch (Exception e) {
			logger.error(e);
			this.setRun(false);
		}
	}
	
	private  void checkPositionAvailable() {
		if (this.getPosition() > this.getLimitPosition()) {
			logger.info(this.toString() + " position is limit at " + this.getLimitPosition());
			this.setValiable(false);
		}
	}
	
	/***
	 * toString 能唯一定位到一个日志
	 */
	@Override
	public String toString() {
		return this.getAppName() + " " + this.getIp() + " " + this.getDataType() + " " + this.getLogPath();
	}
}
