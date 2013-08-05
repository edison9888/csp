
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
 * @version 2011-2-28 ����07:49:02
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
	
		
//		logger.info("�澯�߳�����.....");
//		
//		while(true){
//			
//			logger.info("��ʼ ���.....");
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
//						// �����������Զ��Ĺ��˵���������,������ʱ�䳢���ع������в���һ������
//						if (po.getAlarmType() != null && (!po.getAlarmType().trim().equals("3"))) {
//							AlarmContext context = ServiceFacade.get().lookup(po);
//							if(context !=null){
//								MonitorAlarmAo.get().addConcurrentAlarmMap(app.getAppId(), context);
//							}
//						}
//					}
//				
//					
//					// ���Զ����Խ��д���
//					List<AlarmDataPo>  autoList = AlarmHelper.transferToUnionSite(boList);
//					for(AlarmDataPo po:autoList){
//						ServiceFacade.get().lookup(po);
//					}
//					
//					logger.info("Ӧ��:"+app.getAppName()+":"+boList.size()+" time="+(System.currentTimeMillis()-t));
//				}
//				Thread.sleep(60000);
//			} catch (Exception e) {
//				logger.error("", e);
//			}
//			
//			logger.info("�������.....");
//			
//		}
	}
	
	public static void main(String[] args){
		
		AlarmThread.get().startup();
		
		
	}

}
