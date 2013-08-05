package com.taobao.monitor.web.ao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.taobao.monitor.web.core.dao.impl.MonitorAlarmDao;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;

public class TaskGetHistoryTradeRalateAlarmMapByTimeInterval  implements Callable<List<AlarmDataForPageViewPo>> {  
	private MonitorAlarmDao dao = new MonitorAlarmDao();
	private Date start;
	private Date end;
	private String appName;
	private CountDownLatch latch;
    
    public TaskGetHistoryTradeRalateAlarmMapByTimeInterval(String appName,Date start,
			Date end,CountDownLatch latch) {
		super();
		this.start = start;
		this.end = end;
		this.appName = appName;
		this.latch = latch;
	}


	@Override  
    public List<AlarmDataForPageViewPo> call() throws Exception {  
		 List<AlarmDataForPageViewPo> list = dao.findRecordAlarmByAppNameAndTime(appName, start, end);
		 latch.countDown();
		return list;
    }  
}  
