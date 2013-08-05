
package com.taobao.csp.loadrun.core.job;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.assign.job.BaseJob;
import com.taobao.csp.assign.job.JobReport;
import com.taobao.csp.assign.job.JobReportType;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.core.run.LoadrunFactory;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 下午03:53:06
 */
public class LoadrunJob extends BaseJob {
	
	private static final Logger logger = Logger.getLogger(LoadrunJob.class);
	
	private static final long serialVersionUID = -316909233820666580L;
	
	private LoadrunTarget target = null;
	
	private  transient BaseLoadrunTask task;
	
	
	public LoadrunJob(LoadrunTarget target){
		this.target = target;
	}
	
	@Override
	public void execute(final JobReport arg0) throws Exception {
		
		try {
			task = LoadrunFactory.createLoadrun(this.target);
			
			if(task != null){
				logger.info(target.toString()+" 构建loadrun 成功");
			} else{
				throw new Exception(target.toString()+" 构建loadrun 运行器，无法找到对应的类型");
			}
			
			task.addLoadrunListen(new LoadrunListen(){
				@Override
				public void complete(String loadrunId, LoadrunTarget target) {
					
					arg0.doJobReport(LoadrunJob.this,JobReportType.Complete,"完成"+target.toString());
				}

				@Override
				public void error(String loadrunId,LoadrunTarget target, Object e) {
					arg0.doJobReport(LoadrunJob.this,JobReportType.Exception,e);
				}

				@Override
				public void start(String loadrunId,LoadrunTarget target) {
					
					arg0.doJobReport(LoadrunJob.this,JobReportType.Start,"开始"+target.toString());
				}

				@Override
				public void reportLoad(String loadrunId,LoadrunTarget target,Map<ResultKey, Double> result) {
					arg0.doJobReport(LoadrunJob.this,JobReportType.Report, result);
				}

				@Override
				public void reportLoadDetail(String loadrunId, LoadrunTarget target, List<ResultDetailCell> result) {
					arg0.doJobReport(LoadrunJob.this,JobReportType.Report, result);	
				}

				@Override
				public void reportLoadThreshold(String loadrunId, LoadrunTarget target) {
					arg0.doJobReport(LoadrunJob.this,JobReportType.Report, getJobId());	
					
				}
				
			});
			
			task.recordThreashold();
			
			//执行操作
			executeUrl( target.getStartUrl());
			
			task.runFetchs();
			
			logger.info("run loadrun :"+this.target.getAppId()+" "+this.target.getAppName()+" "+this.target.getCronExpression()+" "+this.target.getTargetIp()+" "+this.target.getLoadrunType());
			
			task.runAutoControl();
			
			task.waitForStop();
			
			executeUrl(target.getEndUrl());
			
			logger.info("end loadrun :"+this.target.getAppId()+" "+this.target.getAppName()+" "+this.target.getCronExpression()+" "+this.target.getTargetIp()+" "+this.target.getLoadrunType());
			
		} catch (Exception e1) {
			logger.error("run loadrun :"+this.target.getAppId()+" "+this.target.getAppName()+" "+this.target.getCronExpression()+" "+this.target.getTargetIp()+" "+this.target.getLoadrunType(),e1);
		}
	}
	
	@Override
	public void stopJob() throws Exception {
		if(task != null){
			logger.info("接收停止job 指令");
			task.stopTask();
		}
		
	}

	public Object getDetail() {
		return this.target;
	}
	
	private void executeUrl(String urlAdress) {
		if (urlAdress != null && urlAdress.trim().length() > 0) {
			try {
				URL url = new URL(urlAdress);
				InputStream in = url.openStream();
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("execute url error", e);
			} 
		}
	}
}
