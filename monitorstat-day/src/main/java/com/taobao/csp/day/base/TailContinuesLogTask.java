package com.taobao.csp.day.base;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.day.util.DayUtil;

/***
 * 数据采集任务
 * @author youji.zj 2012-08-20 
 * 
 * @version 1.0
 */
public class TailContinuesLogTask extends AbstractLogTask {
	
	public static Logger logger = Logger.getLogger(TailContinuesLogTask.class);
	
	private static String TASK_ID;
	
	private HttpClient httpClient;
	private GetMethod httpGet;
	
	static {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		TASK_ID = sf.format(calendar.getTime());
	}
	
	private Object runLock = new Object();
	
	public TailContinuesLogTask(AbstractAnalyser analyser, String appName, HostInfo hostInfo, DataType dataType, 
			String logPath) {
		super(analyser, appName, hostInfo, dataType, logPath, 0);
		
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(2000);
		httpGet = new GetMethod();
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

				String url = DayUtil.appendTailUrl(this.getHostInfo().getIp(), this.getLogPath(), TASK_ID);
					
				String charset = httpClient.getParams().getUriCharset();
				httpGet.setURI(new URI(url, true, charset));
				
				logger.debug(url + " begin to get data");
				int status = httpClient.executeMethod(httpGet);
				
				if (status == HttpStatus.SC_OK) {
					int dataSize = 0;
					InputStream in = httpGet.getResponseBodyAsStream();
					byte[] byteA = new byte[1024 * 1024];
					int size = 0;
					while (in != null && (size = in.read(byteA)) > 0) {
						String content = new String(byteA, 0, size, "GBK");
						dataSize += size;
						
						List<Log> logPoL = this.getAnalyser().analyse(content);
						for (FetcherListener listener : this.getListeners()) {
							listener.onGenerateLogPo(logPoL);
						}
					}
					
					if (dataSize > 0)  {
						logger.debug(url + "get data size:" + dataSize);
					}
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage() + " " +  this.getLogPath());
			} finally {
				this.setRun(false);
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
			}
		}
	}
	
	
	/***
	 * toString 能唯一定位到一个日志
	 */
	@Override
	public String toString() {
		return this.getAppName() + " " + this.getHostInfo().getIp() + " " + this.getDataType() + " " + this.getLogPath();
	}
}
