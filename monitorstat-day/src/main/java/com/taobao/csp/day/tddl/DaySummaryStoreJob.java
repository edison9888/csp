package com.taobao.csp.day.tddl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;
import com.taobao.csp.day.po.TddlSummaryPo;

/***
 * ���ܱ��job
 * @author youji.zj
 * 
 * @version 1.0 2012-09-17
 *
 */
public class DaySummaryStoreJob implements Job {
	
	public static Logger logger = Logger.getLogger(DaySummaryStoreJob.class);
	
	/*** ���ݴ洢 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	TddlSpecialStatics tddlSpecialStatics = TddlSpecialStatics.getInstance();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
		String dayKey = sf.format(date);
		
		// �����������
		reportContent.putReportDateOfDayTDDLSummaryNew(date);
		
		// ���»������������Ӧʱ��
		Map<TddlResponseKey, TddlResponseValue> dayMaxM = tddlSpecialStatics.getCacheMaxResponse().get(dayKey);
		if (dayMaxM == null || dayMaxM.isEmpty()) {
			logger.warn("DayStoreJob dayMaxM no data!!!");
			return;
		}
		
		if (dayMaxM != null) {
			logger.info("update max and min response...");
			for (Map.Entry<TddlResponseKey, TddlResponseValue> entry : dayMaxM.entrySet()) {
				TddlResponseKey key = entry.getKey();
				TddlResponseValue value = entry.getValue();
				
				// ��ʼ���ñ�����ֶ�
				TddlSummaryPo po = new TddlSummaryPo();
				po.setAppName(key.getAppName());
				po.setDbFeature(key.getDbFeature());
				po.setMaxResp(value.getResponse());
				po.setMaxRespDate(value.getResponseTime());
				po.setCollectDate(dayKey);
				
				reportContent.updateMaxResponseOfDayTDDLSummaryNew(po);
			}
		}
		
		tddlSpecialStatics.removeCacheMaxResponse(dayKey);

		
		// ���»���������С��Ӧʱ��
		Map<TddlResponseKey, TddlResponseValue> dayMinM = tddlSpecialStatics.getCacheMinResponse().get(dayKey);
		if (dayMinM == null || dayMinM.isEmpty()) {
			logger.warn("DayStoreJob dayMinM no data!!!");
		}
		
		if (dayMinM != null) {
			for (Map.Entry<TddlResponseKey, TddlResponseValue> entry : dayMinM.entrySet()) {
				TddlResponseKey key = entry.getKey();
				TddlResponseValue value = entry.getValue();
				
				// ��ʼ���ñ�����ֶ�
				TddlSummaryPo po = new TddlSummaryPo();
				po.setAppName(key.getAppName());
				po.setDbFeature(key.getDbFeature());
				po.setMinResp(value.getResponse());
				po.setMinRespDate(value.getResponseTime());
				po.setCollectDate(dayKey);
				
				reportContent.updateMinResponseOfDayTDDLSummaryNew(po);
			}
		}
		
		tddlSpecialStatics.removeCacheMinResponse(dayKey);
	}
	
}
