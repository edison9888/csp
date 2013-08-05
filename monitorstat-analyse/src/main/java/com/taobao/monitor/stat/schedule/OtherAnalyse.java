package com.taobao.monitor.stat.schedule;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.analyse.ShareReportAnalyse;
import com.taobao.monitor.stat.content.ReportContent;

/**
 * 
 * @author xiaodu
 * @version 2010-5-23 ÉÏÎç10:29:00
 */
public class OtherAnalyse implements Job {

	private static Logger log = Logger.getLogger(OtherAnalyse.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		log.info("start OtherAnalyse:");
		String HOSTNAME = System.getenv("HOSTNAME");
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
		
		for(AppInfoPo po:appList){
			ShareReportAnalyse a1 = new ShareReportAnalyse(po.getOpsName());
			a1.analyseLogFile(new ReportContent());
		}
		log.info("end OtherAnalyse:");

//		try {
//			AnalyseDateConfig config = new AnalyseDateConfig();
//			String logFilePath = Config.getValue("LOG_PATH");
//			File file = new File(logFilePath);
//			if (!file.isDirectory()) {
//				return;
//			}
//			File[] logFiles = file.listFiles(new FileFilter() {
//				public boolean accept(File pathname) {
//					if (pathname.isDirectory()) {
//						return true;
//					} else {
//						return false;
//					}
//				}
//			});
//			log.info("log len:" + logFiles.length);
//			for (File appLog : logFiles) {
//				String appName = appLog.getName();
//				AppAnalyseInfo po = config.getAppMap().get(appName);
//				if (po == null) {
//					continue;
//
//				}
//				if (appName.equals("buy")) {
//					log.info(appName + "OtherAnalyse:");
//					ShareReportAnalyse a1 = new ShareReportAnalyse(appName);
//					a1.analyseLogFile(new ReportContent());
//					break;
//				}
//			}
//			log.info("end OtherAnalyse:");
//		} catch (DocumentException e1) {
//		}

	}

}
