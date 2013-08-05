package com.taobao.monitor.web.schedule;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 *
 * @author xiaodu
 * @version 2010-7-6 下午07:31:15
 */
public class WeekMailReportJob implements Job {
	private static  Logger log = Logger.getLogger(WeekMailReportJob.class);
	
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		sendWeekReport();
		
	}
	
	
	
	private void sendWeekReport(){
//		List<String> needReportApp = new ArrayList<String>();
//		  needReportApp.add("1");
//		  needReportApp.add("2");
//		  needReportApp.add("3");
//		  needReportApp.add("4");
//		  needReportApp.add("5");
//		  needReportApp.add("6");
//		  needReportApp.add("7");
//		  needReportApp.add("8");
//		  needReportApp.add("9");
//		try{
//			List<AlarmUserPo> alluserList = MonitorUserAo.get().findAllUser()
//			StringBuilder sb = new StringBuilder();
//			for(AlarmUserPo po:alluserList){
//				String appIdGroup = po.getGroup();
//				String[] apps = appIdGroup.split(",");			
//				for(String appid:apps){
//					if(needReportApp.contains(appid)){
//						sb.append(po.getMail()).append(";");
//						break;
//					}				
//				}
//			}
//			if(sb.length()>0){
//				String result = WeekReport.getWeekReportByJsp();					
//				boolean s = impl.sendEmail("核心监控-关键应用运行周报", result+"\n 接收者:"+sb.toString(),sb.toString());
//				log.info(sb.toString()+" 关键应用运行周报 :"+s);
//			}
//		}catch(Exception e){
//			log.error("",e);
//		}
	}


}
