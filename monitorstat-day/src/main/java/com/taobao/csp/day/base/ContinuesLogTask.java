package com.taobao.csp.day.base;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.day.ao.RecordAo;
import com.taobao.csp.day.config.AppLogPathConfig;
import com.taobao.csp.day.config.DayConfig;
import com.taobao.csp.day.po.RecordPo;
import com.taobao.csp.day.util.DayUtil;
import com.taobao.csp.day.util.PinkieUtil;

/***
 * 数据采集任务
 * @author youji.zj 2012-08-20 
 * 
 * @version 1.0
 */
public class ContinuesLogTask extends AbstractLogTask {
	
	public static Logger logger = Logger.getLogger(ContinuesLogTask.class);
	
	private final static String FILE_BEGIN = "file-begin";
	
	
	/*** 备份日志路径 ***/
	private volatile String backLogPath;
	
	
	/*** 是否在run 备份日志 ***/
	private volatile boolean runBack;
	
	/*** 最新的时间 ***/
	private volatile Date latestDate;
	
	
	/*** 等待的周期数，根据取到的实际数据大小与预想取的数据大小动态调整 ***/
	private volatile int waitCount;
	
	private volatile int logGenerateCount;
	
	private Object runLock = new Object();
	
	private HttpClient httpClient;
	private GetMethod httpGet;
	
	
	public ContinuesLogTask(AbstractAnalyser analyser, String appName, HostInfo hostInfo, DataType dataType, 
			String logPath, long fetchSize) {
		super(analyser, appName, hostInfo, dataType, logPath, fetchSize);
		
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(2000);
//		httpClient.getParams().setConnectionManagerTimeout(1000);
		httpGet = new GetMethod();
		
		this.latestDate = Calendar.getInstance().getTime();
		
		this.setPosition(PinkieUtil.getCurrentSize(hostInfo.getIp(), logPath));
		
		// 装载任务的状态
		recoverState(dataType);
	}

	public String getBackLogPath() {
		return backLogPath;
	}

	public void setBackLogPath(String backLogPath) {
		this.backLogPath = backLogPath;
	}

	public boolean isRunBack() {
		return runBack;
	}

	public void setRunBack(boolean runBack) {
		this.runBack = runBack;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}

	public int getWaitCount() {
		return waitCount;
	}

	public void setWaitCount(int waitCount) {
		this.waitCount = waitCount;
	}

	public int getLogGenerateCount() {
		return logGenerateCount;
	}

	public void setLogGenerateCount(int logGenerateCount) {
		this.logGenerateCount = logGenerateCount;
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

		synchronized (runLock) {

			try {
				this.setRun(true);
				
				// 等待一定的次数后再取
				if (this.getWaitCount() > 0) {
					logger.debug(this.toString() + " wait, and wait count is " + this.getWaitCount());
					this.setWaitCount(this.getWaitCount() - 1);
					this.setRun(false);
					return;
				}

				int dataSize = 0;
				String detectUrl = DayUtil.appendSizeUrl(this.getHostInfo().getIp(), this.getLogPath(), this.getPosition(), this.getPosition() + 1);
				String charset = httpClient.getParams().getUriCharset();
				httpGet.setURI(new URI(detectUrl, true, charset));
				
				int detectStatus = httpClient.executeMethod(httpGet);
				Header header = httpGet.getResponseHeader(FILE_BEGIN);
				if (detectStatus == HttpStatus.SC_OK) {
					this.setLogGenerateCount(0);  // 文件已存在
					
					if (Long.parseLong(header.getValue().trim()) == 0 && this.getPosition() > 0) {
						if (isRunBack()) {
							changeModeFromBackToNormal(detectUrl);
						} else {
							if (logChange(this.getDataType())) {
								changeModeFromNormalToBack(detectUrl);
							} else {
								// 说明该周期内没打日志
								logger.debug("fetch no update log...");
								this.setRun(false);
								return;
							}
						}
					} else {
						httpGet.releaseConnection();  //每次访问需要releaseConnection
						String url = DayUtil.appendSizeUrl(this.getHostInfo().getIp(), this.getLogPath(), this.getPosition(), this.getPosition() + this.getFetchSize());
						httpGet.setURI(new URI(url, true, charset));	
				
						logger.info(url + " begin to get data");
						httpClient.executeMethod(httpGet);
						
						InputStream in = httpGet.getResponseBodyAsStream();
						byte[] byteA = new byte[1024 * 1024];
						int size = 0;
						while ((size = in.read(byteA)) > 0) {
							this.setPosition(this.getPosition() + size);
							dataSize += size;
							
							String content = new String(byteA, 0, size, "GBK");
							List<Log> logPoL = this.getAnalyser().analyse(content);
							
							for (FetcherListener listener : this.getListeners()) {
								listener.onGenerateLogPo(logPoL);
							}
						}
							
						// 调整等待的周期数
						this.adjustWaitCount(this.getFetchSize(), dataSize);

						logger.info(url + "get data size:" + dataSize);

						if (!isRunBack()) {
							RecordPo po = combineRecord();
							RecordAo.get().addOrUpdateRecord(po);
						}
					}
				} else  {
					if (this.isRunBack()) {
						logger.info(detectUrl +  "wrong status:" + detectStatus);
						this.setLogGenerateCount(this.getLogGenerateCount() + 1);
							
						// 超时则设置为不可用
						if (this.getLogGenerateCount() > DayConfig.getBackLogGeneratePeriod(this.getDataType())) {
							changeModeFromBackToNormal(detectUrl);
							this.setLogGenerateCount(0);
						}
					} else {
						logger.info(detectUrl + "wrong status:" + detectStatus);
						this.setLogGenerateCount(this.getLogGenerateCount() + 1);
							
						// 超时则设置为不可用则设置为不可用
						if (this.getLogGenerateCount() > DayConfig.getCurrentLogGeneratePeriod(this.getDataType())) {
							this.setValiable(false);
						}
					}
				}
			} catch (Throwable e) {
				logger.error("run task exception" + this.toString(), e);
			} finally {
				this.setRun(false);
				
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
			}
		}
	}
	
	private RecordPo combineRecord() {
		RecordPo recordPo = new RecordPo();
		recordPo.setAppName(this.getAppName());
		recordPo.setIp(this.getHostInfo().getIp());
		recordPo.setDataType(this.getDataType());
		recordPo.setLogPath(this.getLogPath());
		recordPo.setPosition(this.getPosition());
		recordPo.setUpdateTime(new Date());
		
		return recordPo;
	}
	
	private void recoverState(DataType dataType) {
		RecordPo recordPo = combineRecord();
		
		SimpleDateFormat sf;
		if (dataType == DataType.TDDL) {
			sf = new SimpleDateFormat("yyyy-MM-dd HH");
		} else {
			sf = new SimpleDateFormat("yyyy-MM-dd");
		}
		
		RecordPo existRecordPo = RecordAo.get().existRecordPo(recordPo);
		
		// 如果存在记录，且为同一天(tddl为同一个小时)，还原现场
		if (existRecordPo != null && (sf.format(existRecordPo.getUpdateTime()).equals(sf.format(recordPo.getUpdateTime())))) {
			this.setPosition(existRecordPo.getPosition());
			this.setLatestDate(existRecordPo.getUpdateTime());
		}
	}
	
	private boolean logChange(DataType dataType) {
		boolean changed = false;
		SimpleDateFormat sf;
		if (dataType == DataType.TDDL) {
			sf = new SimpleDateFormat("yyyy-MM-dd HH");
		} else {
			sf = new SimpleDateFormat("yyyy-MM-dd");
		}
		Calendar calendar = Calendar.getInstance();
		Date currentTime = calendar.getTime();
		Date lastLogTime = this.getLatestDate();
		
		if (!sf.format(currentTime).equals(sf.format(lastLogTime))) {
			changed = true;
		}
		
		return changed;
	}
	
	/***
	 * 调整等待周期数
	 * @param expectSize 期望取到的数据大小
	 * @param actualSize 实际取到的数据大小
	 */
	private void adjustWaitCount(long expectSize, long actualSize) {
		int waitCount;
		if (actualSize == 0) {
			waitCount = 10;
		} else if (expectSize / actualSize < 4) {
			waitCount = 0;
		} else if ((expectSize / actualSize > 4) && (expectSize / actualSize < 6)) {
			waitCount = 3;
		} else if ((expectSize / actualSize > 6) && (expectSize / actualSize < 10)) {
			waitCount = 5;
		} else {
			waitCount = 8;
		}
		
		this.setWaitCount(waitCount);
	}
	
	private void changeModeFromBackToNormal(String currentUrl) {
		logger.info(currentUrl + "change log from back to normal");
		this.setLogPath(AppLogPathConfig.getLogPath(this.getAppName(), this.getDataType()));
		this.setPosition(0);
		this.setRunBack(false);
		this.setLatestDate(new Date());
	}
	
	private void changeModeFromNormalToBack(String currentUrl) {
		logger.info(currentUrl + "change log from normal to back");
		this.setLogPath(AppLogPathConfig.getBackLogPath(this.getAppName(), this.getDataType()));
		this.setRunBack(true);
	}
	
	/***
	 * toString 能唯一定位到一个日志
	 */
	@Override
	public String toString() {
		return this.getAppName() + " " + this.getHostInfo().getIp() + " " + this.getDataType() + " " + this.getLogPath();
	}
}
