package com.taobao.csp.day.tddl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;
import com.taobao.csp.day.po.TddlPo;

public class HourTempStoreJob implements Job {
	
	public static Logger logger = Logger.getLogger(HourTempStoreJob.class);
	
	/*** Êý¾Ý´æ´¢ ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	TddlStatistics tddlStatistics = TddlStatistics.getInstance();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String hourKey = sf.format(calendar.getTime());
		
		Map<TddlLogKey, TDDL> hourValueM = tddlStatistics.getCacheHour().get(hourKey);
		if (hourValueM == null || hourValueM.isEmpty()) {
			logger.warn("HourStoreJob no data!!!");
			return;
		}
		
		List<TddlPo> tempL = new ArrayList<TddlPo>();
		for (Map.Entry<TddlLogKey, TDDL> entry : hourValueM.entrySet()) {
			TddlLogKey key = entry.getKey();
			TDDL value = entry.getValue();
			
			TddlPo po = new TddlPo();
			po.setAppName(key.getAppName());
			po.setDbFeature(key.getDbFeature());
			po.setDbName(key.getDbName());
			po.setDbIp(key.getDbIp());
			po.setDbPort(key.getDbPort());
			po.setSqlText(key.getSql());
			
			po.setExeCount(value.getExeCount());
			po.setRespTime(value.getRespTime());
			po.setMaxResp(value.getMaxResp());
			po.setMaxRespDate(value.getMaxRespDate());
			po.setMinResp(value.getMinResp());
			po.setMinRespDate(value.getMinRespDate());	
			po.setCollectTime(hourKey);
			
			tempL.add(po);
			if (tempL.size() == 200) {
				reportContent.putReportDateOfTempTDDL(tempL);
				tempL.clear();
			}
		}
		
		if (tempL.size() > 0) {
			reportContent.putReportDateOfTempTDDL(tempL);
			tempL.clear();
		}
		
		tddlStatistics.removeCacheHour(hourKey);
	}

}
