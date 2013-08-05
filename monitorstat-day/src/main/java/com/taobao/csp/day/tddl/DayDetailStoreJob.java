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

/***
 * 存储detail数据的job
 * @author youji.zj
 * 
 * @version 1.0 2012-09-17
 *
 */
public class DayDetailStoreJob implements Job {
	
	public static Logger logger = Logger.getLogger(DayDetailStoreJob.class);
	
	/*** 数据存储 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	TddlStatistics tddlStatistics = TddlStatistics.getInstance();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String dayKey = sf.format(calendar.getTime());
		
		Map<TddlLogKey, TDDL> dayValueM = tddlStatistics.getCacheDay().get(dayKey);
		if (dayValueM == null || dayValueM.isEmpty()) {
			logger.warn("DayStoreJob no data!!!");
			return;
		}

		List<TddlPo> tempL = new ArrayList<TddlPo>();
		for (Map.Entry<TddlLogKey, TDDL> entry : dayValueM.entrySet()) {
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
			po.setCollectTime(dayKey);
			
			tempL.add(po);
			if (tempL.size() == 200) {
				reportContent.putReportDateOfDayTDDL(tempL);
				tempL.clear();
			}
		}
		
		if (tempL.size() > 0) {
			reportContent.putReportDateOfDayTDDL(tempL);
			tempL.clear();
		}
		
		tddlStatistics.removeCacheDay(dayKey);
		
	}

}
