
package com.taobao.monitor.alarm;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.ServiceFacade;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.vo.AlarmDataPo;


/**
 * 
 * @author xiaodu
 * @version 2011-2-28 下午07:49:02
 */
public class AlarmThread implements Runnable{
	
	private static final Logger logger =  Logger.getLogger(AlarmThread.class);
	
	
	private static AlarmThread alarmThread = new AlarmThread();
	
	private Thread thread = null;
	
	private AlarmThread(){
		thread = new Thread(this);
		thread.setDaemon(false);
		thread.setName("Monitor - AlarmThread");
		
	}
	
	public static AlarmThread get(){
		return alarmThread;
	}
	
	public void startup(){
		thread.start();
	}
	

	@Override
	public void run() {
	
		
//		logger.info("告警线程启动.....");
//		
//		while(true){
//			
//			logger.info("开始 检查.....");
//			
//		  try {
//				List<AppInfoPo> alarmApp = AppInfoAo.get().findAllTimeApp();
//				MonitorAlarmAo.get().clearConcurrentAlarmMap();
//				for(AppInfoPo app:alarmApp){
//					if(app.getAppStatus()==1){
//						continue;
//					}	
//					long t = System.currentTimeMillis();
//					List<AlarmDataPo> boList =MonitorAlarmAo.get().findAllAlarmWithAlarmTable(app.getAppId());	
//					
//					for(AlarmDataPo po:boList){
//						// 报警策略是自动的过滤掉单独处理,后续抽时间尝试重构把所有策略一并处理
//						if (po.getAlarmType() != null && (!po.getAlarmType().trim().equals("3"))) {
//							AlarmContext context = ServiceFacade.get().lookup(po);
//							if(context !=null){
//								MonitorAlarmAo.get().addConcurrentAlarmMap(app.getAppId(), context);
//							}
//						}
//					}
//				
//					
//					// 对自动策略进行处理
//					List<AlarmDataPo>  autoList = AlarmHelper.transferToUnionSite(boList);
//					for(AlarmDataPo po:autoList){
//						ServiceFacade.get().lookup(po);
//					}
//					
//					logger.info("应用:"+app.getAppName()+":"+boList.size()+" time="+(System.currentTimeMillis()-t));
//				}
//				Thread.sleep(60000);
//			} catch (Exception e) {
//				logger.error("", e);
//			}
//			
//			logger.info("结束检查.....");
//			
//		}
	}
	
	public static void main(String[] args){
		
		AlarmThread.get().startup();
		
		
	}

}
