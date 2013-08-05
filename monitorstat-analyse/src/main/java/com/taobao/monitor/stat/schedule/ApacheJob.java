
package com.taobao.monitor.stat.schedule;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.stat.analyse.ApacheLogAnalyse;
import com.taobao.monitor.stat.content.ReportContent;

/**
 * 
 * @author xiaodu
 * @version 2011-7-28 ÉÏÎç09:45:33
 */
public class ApacheJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String HOSTNAME = System.getenv("HOSTNAME");
		
		ReportContent rc = new ReportContent();
		
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
		for(AppInfoPo app:appList){
			if(app.getAppStatus()==0&&app.getDayDeploy() == Constants.DEFINE_DATA_EFFECTIVE){
				ApacheLogAnalyse apache = new ApacheLogAnalyse(app.getOpsName());
				apache.analyseLogFile(rc);
			}
		}
		
	}

}
