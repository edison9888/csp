package com.taobao.monitor.web.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.web.ao.MonitorUserAo;
import com.taobao.monitor.web.cache.CacheJsp;
import com.taobao.monitor.web.cache.CacheProviderCustomer;
import com.taobao.monitor.web.report.DayReport;
import com.taobao.monitor.web.report.PhoneOut;
import com.taobao.monitor.web.report.WebWWReport;
import com.taobao.monitor.web.util.Config;
import com.taobao.monitor.web.vo.LoginUserPo;
/**
 *
 * @author xiaodu
 * @version 2010-7-6 下午07:31:15
 */
public class MailReportJob implements Job {
	private static  Logger log = Logger.getLogger(MailReportJob.class);
	
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
        SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		PhoneOut.sendPhone(parseLogFormatDate.format(cal.getTime()),Config.getValue("monitor.phone"));
		cache();		
		sendDayReport(parseLogFormatDate.format(cal.getTime()));
		sendWebWangWangReport(); //启动一个线程用来发送WEB旺旺的报表。
		//sendLoadRunReport(parseLogFormatDate.format(cal.getTime()));
		//sendAlarmReport(parseLogFormatDate.format(cal.getTime()));
		//sendDistinct(parseLogFormatDate.format(cal.getTime()));
		//sendJprof(parseLogFormatDate.format(cal.getTime()));
		//sendException(parseLogFormatDate.format(cal.getTime()));
		
	}
	
	
	
//	private Map<String, String> getSendAppMapUser(){
//		List<AppInfoPo> listApp = AppInfoAo.get().findAllAppInfo();
//		Map<String, String> map = new HashMap<String, String>();
//		List<AlarmUserPo> alluserList = MonitorUserAo.get().findAllAlarmUser();
//		for(AppInfoPo app:listApp){
//			try{
//				int id = app.getAppId();
//				StringBuilder sb = new StringBuilder();
//				for(AlarmUserPo po:alluserList){
//					String appIdGroup = po.getGroup();
//					String[] apps = appIdGroup.split(",");
//					for(String appid:apps){
//						if(appid.equals(id+"")){
//							sb.append(po.getMail()).append(";");
//						}				
//					}
//				}
//				if(sb.length()>0){
//					map.put(app.getAppId()+"",app.getAppName()+" 接收者:" +sb.toString());
//				}
//			}catch(Exception e){
//				log.error("",e);
//			}
//		}
//		return map;
//	}
	
//	public void sendDistrib(String time){
//		
//		Set<String> providerSet = CacheProviderCustomer.get().getProviderAppName();
//		
//		
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();	
//				
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}			
//			String combin = combin(po,"6");	
//			if(!combin.equals("")){
//				String appNames = "";
//				
//				for(String appid:combin.split(",")){
//					String appName = AppCache.get().getKey(Integer.parseInt(appid)).getAppName();
//					if(providerSet.contains(appName)){
//						appNames+=appName+",";
//					}
//				}
//				
//				if(!appNames.equals("")){
//					String report = DistribeReport.getDistribeReportByJsp(appNames, time);
//					boolean r = impl.sendEmail("CSP平台-C应用依赖对比["+time+"]", report, po.getMail());
//					log.info(po.getMail()+"C应用依赖对比  send "+appNames+" :"+r);
//				}
//			}			
//		}
//		
//	}
	
	
//	public void sendException(String time){
//		
//		
//		
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();	
//				
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}			
//			String report = RequestByUrl.getMessageByJsp("http://127.0.0.1:8080/monitorstat/report/report_exception.jsp?appIds="+po.getGroup());
//			boolean r = impl.sendEmail("CSP平台-应用异常数量["+time+"]", report, po.getMail());
//			log.info(po.getMail()+"应用异常数量 send "+time+" :"+r);
//		}
//		
//	}
//	
//	
	
//	public void sendJprof(String time){		//5
//		
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();		
//		List<JprofHost> hostList = MonitorJprofAo.get().findAllJprofHosts();
//		Map<String,JprofHost> jpropMap = new HashMap<String, JprofHost>();
//		for(JprofHost jprof:hostList){
//			jpropMap.put(jprof.getAppName(), jprof);
//		}
//		
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}			
//			String combin = combin(po,"5");	
//			if(!combin.equals("")){
//				String appNames = "";
//				
//				for(String appid:combin.split(",")){
//					String appName = AppCache.get().getKey(Integer.parseInt(appid)).getAppName();
//					if(jpropMap.get(appName)!=null){
//						appNames+=appName+",";
//					}
//				}
//				
//				if(!appNames.equals("")){
//					String report = JprofReport.getJprofReportByJsp(appNames, time);
//					boolean r = impl.sendEmail("CSP平台-关键java类执行时间["+time+"]", report, po.getMail());
//					log.info(po.getMail()+"jprof send "+appNames+" :"+r);
//				}
//			}			
//		}
//		
//	}
	
	
	public void sendDayReport(String time){//1
		
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();
//		String result = DayReport.getDayReportByJsp(time);
//		StringBuilder sb = new StringBuilder();
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}			
//			String reportDesc = po.getReportDesc();
//			if(reportDesc!= null && reportDesc.indexOf("1;")>-1){
//				sb.append(po.getMail()+";");
//			}			
//		}
//		boolean s = impl.sendEmail("CSP平台-交易系统数据报表["+time+"]", result,sb.toString());
//		log.info(sb.toString()+" sendDayReport:"+s);
	}
	
	
//	public void sendLoadRunReport(String time){//2
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();
//		List<LoadRunHost> listLoadHost = MonitorLoadRunAo.get().findAllLoadRunHost();
//		
//		Map<Integer,LoadRunHost> loadMap = new HashMap<Integer, LoadRunHost>();
//		for(LoadRunHost load:listLoadHost){
//			loadMap.put(load.getAppId(), load);
//		}
//		
//		
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}			
//			String combin = combin(po,"2");	
//			if(!combin.equals("")){
//				String appIds = "";				
//				for(String appid:combin.split(",")){
//					int appId = Integer.parseInt(appid);
//					if(loadMap.get(appId)!=null){
//						appIds+=appId+",";
//					}
//				}
//				
//				if(!appIds.equals("")){					
//					String result = LoadRunReport.getLoadREportByJsp(appIds);
//					boolean s = impl.sendEmail("CSP平台-线上自动压测-["+time+"]", result,po.getMail());
//					log.info(po.getMail()+" appIds:"+appIds+" sendLoadRunReport:"+s);
//				}
//			}
//		}
//	}
	
	
//	/**
//	 * 发送报警发送报表
//	 * @param time
//	 */
//	public void sendAlarmReport(String time){//3
//		
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();
//		
//		for(LoginUserPo po:alluserList){
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}		
//			
//			String combin = combin(po,"3");
//			
//			if(!combin.equals("")){				
//				log.info(po.getName()+":"+combin);
//				String result = AlarmReport.getAlarmReportByJsp(combin,time);
//				boolean s = impl.sendEmail("CSP平台-应用告警报表["+time+"]", result,po.getMail());
//				log.info(po.getMail()+" appIds:"+combin+" sendAlarmReport:"+s);	
//			}
//		}
//	}
	
//	/**
//	 * 发送应用执行对比报表
//	 * @param time
//	 */
//	public void sendDistinct(String time){//代码 ： 4
//		List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();
//		
//		for(LoginUserPo po:alluserList){			
//			if(po.getMail()==null||po.getMail().equals("")){
//				continue;
//			}
//			String combin = combin(po,"4");
//			if(!combin.equals("")){				
//				String result = DistinctReport.getAppDistinct(combin);
//				boolean s = impl.sendEmail("CSP平台-运行对比数据-["+time+"]", result,po.getMail());
//				log.info(po.getMail()+" appIds:"+combin+" sendDistinct :"+s);
//			}
//		}
//		
//	}
	
	
//	private String combin(LoginUserPo po,String t){
//		
//		String reportDesc = po.getReportDesc();			
//		String reportApps = "";
//		
//		if(reportDesc != null&&!reportDesc.trim().equals("")){
//			for(String reportInfo:reportDesc.split(";")){					
//				int index = reportInfo.indexOf(":");
//				if(index >0){
//					String type = reportInfo.substring(0,index);
//					if(t.equals(type)){
//						reportApps = reportInfo.substring(index+1,reportInfo.length());	
//						break;
//					}
//				}
//			}
//		}
//		
//		String s1 = po.getGroup();
//		String s2 = reportApps;
//		
//		Set<String> set = new HashSet<String>();
//		
//		String[] appids = s1.split(",");
//		for(String appId:appids){
//			set.add(appId);
//		}
//		appids = s2.split(",");
//		for(String appId:appids){
//			set.add(appId);
//		}
//		
//		String g = "";
//		for(String str:set){
//			if(!"".equals(str))
//				g+=str+",";
//		}		
//		return g;			
//	}

    public void sendWebWangWangReport() {
    	
    	MessageSend messageSender = MessageSendFactory.create(MessageSendType.Email);
    	
    	try{
	        Calendar now = Calendar.getInstance();
	        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 1);
	        SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
	        String date = parseLogFormatDate.format(now.getTime());
	
	        String contents = WebWWReport.getReportContents(date);
	
	        log.info(date + "，内容:" + contents);
	        messageSender.send(Config.getValue("webwangwang.report.mail.to"),"【" + date + "】WEB旺旺业务数据报表", contents);
	
        }catch (Exception e) {
        	log.error("",e);
		}
    }
    
    
    
    public void cache(){
    	
    	CacheProviderCustomer.get().reset();
    	
    	CacheJsp.get().getDayHtml("http://127.0.0.1:9999/monitorstat/week_report1.jsp",Utlitites.getMonitorDate(),true);
    	CacheJsp.get().getDayHtml("http://127.0.0.1:9999/monitorstat/index_day1.jsp",Utlitites.getMonitorDate(),true);
    	CacheJsp.get().getDayHtml("http://127.0.0.1:9999/monitorstat/load/load_capacity1.jsp",Utlitites.getMonitorDate(),true);
    }
    
    public static void main(String [] args) {
    	SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
  		Calendar cal = Calendar.getInstance();
  		cal.add(Calendar.DAY_OF_MONTH, -1);
    	PhoneOut.sendPhone(parseLogFormatDate.format(cal.getTime()),"15867123026");
    }

}
