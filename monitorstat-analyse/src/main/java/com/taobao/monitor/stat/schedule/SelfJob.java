
package com.taobao.monitor.stat.schedule;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.analyse.SelfAnalyse;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.Config;

/**
 * 
 * @author xiaodu
 * @version 2010-5-23 ÉÏÎç10:29:00
 */
public class SelfJob implements Job{
	
	 private static  Logger log = Logger.getLogger(SelfJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		log.info("start SelfJob");
		String HOSTNAME = System.getenv("HOSTNAME");
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
		
		for(AppInfoPo po:appList){
			SelfAnalyse self = new SelfAnalyse(po.getOpsName());
			self.analyseLogFile(new ReportContent());
		}
		
		log.info("end SelfAnalyse:");
		
//		try {
//			AnalyseDateConfig config = new AnalyseDateConfig();
//			String logFilePath = Config.getValue("LOG_PATH");
//			File file = new File(logFilePath);
//			if(!file.isDirectory()){
//				return ;
//			}
//			File[] logFiles = file.listFiles(new FileFilter(){
//				public boolean accept(File pathname) {
//					if(pathname.isDirectory()){
//						return true;
//					}else{
//						return false;
//					}					
//				}});
//			log.info("log len:"+logFiles.length);
//			for(File appLog:logFiles){					
//				String appName = appLog.getName();					
//				AppAnalyseInfo po = config.getAppMap().get(appName);
//				if(po==null){
//					continue;						
//				
//				}
//				log.info(appName+"SelfAnalyse:");
//				HostContents.get().load(appName, po.getOpsfreeName());
//				SelfAnalyse self = new SelfAnalyse(appName);
//				self.analyseLogFile(new ReportContent());
//				
//			}	
//			log.info("end SelfAnalyse:");
//		} catch (DocumentException e1) {
//		}	
	}

}
