
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import org.apache.log4j.Logger;

import com.taobao.csp.monitor.DataAnalyse;
import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.DataCollector.CallBack;
import com.taobao.csp.monitor.Job;
import com.taobao.csp.monitor.JobInfo;
import com.taobao.csp.monitor.util.Constants;
import com.taobao.monitor.MonitorLog;

/**
 * @author xiaodu
 *
 * ÏÂÎç8:45:00
 */
public class DefaultJob implements Job{
	
	private static final Logger logger =  Logger.getLogger(DefaultJob.class);
	
	private JobInfo jobInfo;
	
	private DataAnalyse dataanalyse;
	
	private DataCollector datacollector;
	
	private long fireTime = System.currentTimeMillis()-1;
	
	private long interval = 60000;
	
	private boolean run = false;
	
	
	
	public DefaultJob(JobInfo info) throws Exception{
		this.jobInfo = info;
		
		this.jobInfo.setFilepath(this.jobInfo.getFilepath()); 
		
		this.dataanalyse = DataAnalyseFactory.createAnalyse(info);
		this.datacollector = DataCollectorFactory.createCollector(info);
		
		if(this.dataanalyse == null||this.datacollector ==null){
			throw new Exception("create DefaultJob dataanalyse ="+dataanalyse+" datacollector="+datacollector);
		}
		
		
		if(info.getAnalyseFrequency()>20){
			interval=info.getAnalyseFrequency()*1000;
		}
		
		
	}

	@Override
	public String getJobID() {
		return jobInfo.getJobID();
	}
	@Override
	public DataAnalyse getDataAnalyse() {
		return DataAnalyseFactory.createAnalyse(this.jobInfo);
	}

	@Override
	public DataCollector getDataCollector() {
		return DataCollectorFactory.createCollector(this.jobInfo);
	}

	@Override
	public void close() {
		datacollector.release();
		dataanalyse.submit();
		dataanalyse.release();
	}

	@Override
	public void execute() {
		
		long time = System.currentTimeMillis();
		
		logger.info("start job"+jobInfo.getAppName()+"="+
				jobInfo.getIp()+"="+jobInfo.getFilepath());
	
		try{
			DataCollector collector = getDataCollector();
			
			final DataAnalyse analyse=getDataAnalyse();
			
			collector.collect(new CallBack() {
				@Override
				public void readerLine(String line) {
					try{
						analyse.analyseOneLine(line);
					}catch (Exception e) {
						logger.info("analyseOneLine error:"+line+"&&"+jobInfo.getAppName()+"="+jobInfo.getIp()+"="+jobInfo.getFilepath(),e);
					}
				}
			});
			
			MonitorLog.addStat(Constants.CSP_DATA_ANALYSE, new String[]{"collectPv",collector.getName()}, 
					new Long[]{1l,System.currentTimeMillis()-time});
			
			analyse.submit();
			analyse.release();
			collector.release();
		}catch (Exception e) {
			logger.info("analyse job"+jobInfo.getAppName()+"="+jobInfo.getIp()+"="+jobInfo.getFilepath(),e);
			MonitorLog.addStat(Constants.CSP_DATA_ANALYSE, new String[]{"jobExceptionPv",jobInfo.getJobName()}, 
					new Long[]{1l,System.currentTimeMillis()-time});
		}
		MonitorLog.addStat(Constants.CSP_DATA_ANALYSE, new String[]{"jobPv",jobInfo.getJobName()}, new Long[]{1l,System.currentTimeMillis()-time});
		
		logger.info("end job"+jobInfo.getAppName()+"="+jobInfo.getIp()+"="+jobInfo.getFilepath());
		
		run = false;
		
	}

	@Override
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	


	@Override
	public boolean isFire() {
		long time = System.currentTimeMillis();
		if(time >fireTime){
			if(!run){
				fireTime = time+interval;
				run =true;
				return true;
			}else{
				MonitorLog.addStat(Constants.CSP_DATA_ANALYSE, new String[]{"jobBlock"}, new Long[]{1l});
			}
		}
		return false;
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		execute();		
		MonitorLog.addStat(Constants.CSP_DATA_ANALYSE, new String[]{"jobAll"}, new Long[]{1l,System.currentTimeMillis()-time});
	}

}
