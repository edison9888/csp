package com.taobao.csp.day.base;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;


/***
 * 标准数据采集任务
 * @author youji.zj 2012-08-20 
 * 
 * @version 1.0
 */
public class StandardLogTask extends AbstractLogTask  {
	
	public static Logger logger = Logger.getLogger(StandardLogTask.class);
	
	private final static String FILE_BEGIN = "file-begin";
	
	
	/*** 限制position的位置 ***/
	private volatile long limitPosition = Long.MAX_VALUE;
	
	
	private final List<String> contentFilter;
	
	private Object runLock = new Object();
	
	
	public StandardLogTask(AbstractAnalyser analyser, String appName, HostInfo hostInfo, DataType dataType, 
			String realLogPath, long fetchSize, List<String> contentFilter) {
		super(analyser, appName, hostInfo, dataType, realLogPath, fetchSize);
		this.contentFilter = contentFilter;
	}
	
	public long getLimitPosition() {
		return limitPosition;
	}

	public void setLimitPosition(long limitPosition) {
		this.limitPosition = limitPosition;
	}

	public List<String> getContentFilter() {
		return contentFilter;
	}

	@Override
	public String identityKey() {
		return this.getAppName() + " " + this.getHostInfo().getIp() + " " + this.getDataType();
	}
	
	@Override
	public int filterIdentity() {
		return this.identityKey().hashCode() & 0x7FFFFFFF;
	}

	@Override
	public  void run() {
	
		if (!isValiable()) {
			String tipInfo = this.getAppName() + " " + this.getHostInfo().getIp() + " " + this.getDataType() + " " + this.getLogPath();
			logger.info(tipInfo + ", not available");
			return;
		}

		// 上一个任务没跑完直接返回
		if (this.isRun()) {
			logger.info(this.toString() + "wait for done...");
			return;
		}
		
		// 上一个任务跑完，如果此时有多个任务排队，则抢锁
		synchronized (runLock) {
			try {
				this.setRun(true);
				
				HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
				
				while (true) {
					String url = "http://" + this.getHostInfo().getIp() + ":8082/get"
							+ this.getLogPath() + "?" + "begin=" + this.getPosition()
							+ "&end=" + (this.getPosition() + this.getFetchSize())
							+ "&encode=text";
					
					GetMethod httpGet = new GetMethod(url);
					int httpStatus = httpClient.executeMethod(httpGet);
					Header header = httpGet.getResponseHeader(FILE_BEGIN);

					int dataSize = 0;
					if (httpStatus == HttpStatus.SC_OK) {
						// 当file-begin为0，position不为0，begin已经大于或者等于文件大小，文件已经分析完毕
						logger.debug("file begin: " + header.getValue().trim() + ", position:" + this.getPosition());
						if (Long.parseLong(header.getValue().trim()) == 0 && this.getPosition() > 0) {
							this.setValiable(false);
							this.setRun(false);
							httpGet.releaseConnection();
							return;
						}
						
						// log位置超出限制的大小
						if (!this.positionAvailable()) {
							logger.info(this.toString() + " out of limit:" + this.getLimitPosition());
							this.setValiable(false);
							this.setRun(false);
							httpGet.releaseConnection();
							return;
						} 
						
						StringBuffer contentBuffer = new StringBuffer();
						InputStream in = httpGet.getResponseBodyAsStream();
						byte[] byteA = new byte[1024 * 100];
						int size = 0;
						while ((size = in.read(byteA)) > 0) {
							this.setPosition(this.getPosition() + size);
							contentBuffer.append(new String(byteA, 0, size, "GBK"));
							dataSize += size;
						}
						
						logger.info(url + "get data size:" + dataSize);
						String content = contentBuffer.toString();
						
						// 内容过滤
						boolean contentAvaible = true;
						if (this.getContentFilter() != null && (!this.getContentFilter().isEmpty())) {
							for (String filter : this.getContentFilter()) {
								if (content.indexOf(filter)  == -1) {
									contentAvaible = false;
									break;
								} 
							}
						}
						
						// 立即进行下一次获取,否则本次分析拉取的数据，并跳出循环
						if (!contentAvaible) {
							httpGet.releaseConnection();
							continue;
						} else {
							List<Log> logPoL = this.getAnalyser().analyse(content);
							for (FetcherListener listener : this.getListeners()) {
								listener.onGenerateLogPo(logPoL);
							}
							break;
						}

					} else {
						this.setValiable(false);
					}
					httpGet.releaseConnection();
				}
			} catch (Exception e) {
				logger.error(e);
			} finally {
				this.setRun(false);
			}	
		}
	}
	
	private  boolean positionAvailable() {
		boolean available = true;
		if (this.getPosition() > this.getLimitPosition()) {
			logger.info(this.toString() + " position is limit at " + this.getLimitPosition());
			available = false;
		}
		
		return available;
	}
	
	/***
	 * toString 能唯一定位到一个日志
	 */
	@Override
	public String toString() {
		return this.getAppName() + " " + this.getHostInfo().getIp() + " " + this.getDataType() + " " + this.getLogPath();
	}
}
