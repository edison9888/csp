package com.taobao.monitor.web.schedule;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.baseline.DateBaseLine;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.baseline.BaseLineManage;
import com.taobao.monitor.web.cache.CacheTimeData;
import com.taobao.monitor.web.cache.WeekReportCache;
import com.taobao.monitor.web.jprof.AutoJprofManage;
import com.taobao.monitor.web.rating.RatingManage;

/**
 * 
 * @author xiaodu
 * @version 2010-5-26 ÏÂÎç02:39:05
 */
public class BaseLineJob implements Job {
	private static final Logger logger = Logger.getLogger(BaseLineJob.class);

	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try {
			RatingManage m = new RatingManage();
			m.rating();
		} catch (Exception e) {
			logger.error("", e);
		}
		try {
			logger.info("start DateBaseLine");
			DateBaseLine baseLine = new DateBaseLine();
			baseLine.startup();
			logger.info("end DateBaseLine");
		} catch (Exception e) {
			logger.error("", e);
		}
		
		try {
			logger.info("start CacheTimeData");
			CacheTimeData.get().cacheData();
			logger.info("end CacheTimeData");
		} catch (Exception e) {
			logger.error("", e);
		}
//		try {
//			logger.info("start ScpManage");
//			AutoJprofManage.get().doAutoCollectJprofile();
//			logger.info("end ScpManage");
//		} catch (Exception e) {
//			logger.error("", e);
//		}
		try {
			
			List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
			for(AppInfoPo app:appList){
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);
				MonitorAlarmAo.get().deleteAlarmDataDesc(app.getAppId(),cal.getTime());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
		
		try {
			DataBaseInfoAo.get().saveAppMysqlInfo();			
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}

}
