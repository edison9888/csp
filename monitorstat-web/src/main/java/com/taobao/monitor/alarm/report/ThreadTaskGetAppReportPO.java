package com.taobao.monitor.alarm.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.key.DBMediaKeyCache;
import com.taobao.monitor.web.core.dao.impl.MonitorAlarmDao;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;

public class ThreadTaskGetAppReportPO implements Callable<AppReportPO> {
	private static final Logger logger = Logger.getLogger(ThreadTaskGetAppReportPO.class);
	private MonitorAlarmDao dao = new MonitorAlarmDao();
	private Date start;
	private Date end;
	private String appName;
	private CountDownLatch latch;
    
    public ThreadTaskGetAppReportPO(String appName, Date start, Date end, CountDownLatch latch) {
		super();
		this.start = start;
		this.end = end;
		this.appName = appName;
		this.latch = latch;
	}

	@Override  
    public AppReportPO call() throws Exception {  
		AppReportPO po = new AppReportPO();
		po.setAppName(appName);
		po.setTimeString(start);
		try{
			List<AlarmDataForPageViewPo> list = dao.findLimitRecordAlarmByAppNameAndTime(appName, start, end, 30);
			
			for(AlarmDataForPageViewPo viewPo:list){
				Integer keyLevel = DBMediaKeyCache.get().getKeyLevelByKeyName(viewPo.getKeyName());
				if(keyLevel != null){
					viewPo.setLevel(keyLevel.intValue());
				} else {
					viewPo.setLevel(4); //如果查不到就设为最低级4级报警
				}
			}
			
//			Collections.sort(list,new Comparator<AlarmDataForPageViewPo>() {
//				@Override
//				public int compare(AlarmDataForPageViewPo p1, AlarmDataForPageViewPo p2) {
//					if( p1.getAlarmTime().after(p2.getAlarmTime()) ){
//						return -1;
//					} else if( p1.getAlarmTime().before(p2.getAlarmTime()) ){
//						return 1;
//					}
//					return 0;
//				}
//			});	
			
			po.setAppAlarmDataList(list);
		} catch (Exception e){
			logger.warn("ThreadTaskGetAppReportPO call MonitorAlarmDao.findRecordAlarmByAppNameAndTime error", e);
		}
		latch.countDown();
		return po;
    }  
}  
