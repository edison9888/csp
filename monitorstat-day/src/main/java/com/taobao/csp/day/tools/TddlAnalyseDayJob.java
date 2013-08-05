package com.taobao.csp.day.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;
import com.taobao.csp.day.po.TddlPo;
import com.taobao.csp.day.util.AppUtil;
import com.taobao.monitor.common.po.AppInfoPo;

/***
 * 从日报移过来，必要的时候可以通过该类汇总temp表的数据插入detail表和summary表
 * @author youji.zj
 * 
 * @version 1.0 2012-10-22
 *
 */
public class TddlAnalyseDayJob implements Job {
	private static final Logger logger =  Logger.getLogger(TddlAnalyseDayJob.class);
	
	private ReportContentInterface reportContent = ReportContent.getInstance();
	
	private Calendar cal;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String collectTime;
	/**
	 * 缓冲中的对象对数
	 */
	private final int COUNT = 200;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		collectTime = sdf.format(cal.getTime());
		String startTime = collectTime + " 00:00:00";
		String endTime = collectTime + " 23:59:59";
		
		List<AppInfoPo> tddlAppL = AppUtil.getTddlApps();
		
		int oneTime = 100000;  // 一次取10万条，要不内存溢出
		for (AppInfoPo po : tddlAppL) {
			String appName = po.getOpsName();
			logger.info("summary tddl data of: " + appName);
			Map<String, Map<String, Map<String, TddlPo>>> resultMap = new HashMap<String, Map<String,Map<String,TddlPo>>>();
			
			int index = 0;
			List<TddlPo> tddlList0 = new ArrayList<TddlPo>();
			do {
				tddlList0 = reportContent.findReportDataOfTddlTemp("csp_app_consume_tddl_hour_temp0", startTime, endTime, appName, oneTime, index);
				combineResultMap(resultMap, tddlList0);
				index += tddlList0.size();
			} while (tddlList0.size() == oneTime);
			tddlList0.clear();
			tddlList0 = null;
			
			index = 0;
			List<TddlPo> tddlList1 = new ArrayList<TddlPo>();
			do {
				long begin = System.currentTimeMillis();
				tddlList1 = reportContent.findReportDataOfTddlTemp("csp_app_consume_tddl_hour_temp1", startTime, endTime, appName, oneTime, index);
				long end = System.currentTimeMillis();
				logger.info("use time " + (end - begin));
				combineResultMap(resultMap, tddlList1);
				index += tddlList1.size();
			} while (tddlList1.size() == oneTime);
			tddlList1.clear();
			tddlList1 = null;
			
			index = 0;
			List<TddlPo> tddlList2 = new ArrayList<TddlPo>();
			do {
				tddlList2 = reportContent.findReportDataOfTddlTemp("csp_app_consume_tddl_hour_temp2", startTime, endTime, appName, oneTime, index);
				combineResultMap(resultMap, tddlList2);
				index += tddlList2.size();
			} while (tddlList2.size() == oneTime);
			tddlList2.clear();
			tddlList2 = null;
		
			insertToDb(resultMap);
		}
		
	}
	
	/**
	 * 汇总从临时表抽取出的数据到汇总表
	 */
	private void combineResultMap(Map<String, Map<String, Map<String, TddlPo>>> resultMap, List<TddlPo> tddlList) {
		for (TddlPo po : tddlList) {
			String appHostId = po.getAppHostIp();
			String dbfeature = po.getDbFeature();
			String sqlText = po.getSqlText();
			
			if (appHostId == null || dbfeature == null || sqlText == null) {
				logger.info("数据库记录又为空项，有问题，查看");
				continue;
			}
			
			Map<String, Map<String, TddlPo>> map1 = resultMap.get(appHostId);
			if (map1 == null) {
				map1 = new HashMap<String, Map<String,TddlPo>>();
				resultMap.put(appHostId, map1);
			}
			Map<String, TddlPo> map2 = map1.get(dbfeature);
			if (map2 == null) {
				map2 = new HashMap<String, TddlPo>();
				map1.put(dbfeature, map2);
			}
			
			TddlPo tddl = map2.get(sqlText);
			if (tddl == null) {
				tddl  = new TddlPo();
				tddl.setAppName(po.getAppName());
				tddl.setDbFeature(dbfeature);
				tddl.setDbName(po.getDbName());
				tddl.setDbIp(po.getDbIp());
				tddl.setDbPort(po.getDbPort());
				tddl.setSqlText(sqlText);
				tddl.setType(po.getType());
				tddl.setAppHostIp(po.getAppHostIp());
				tddl.setAppHostSite(po.getAppHostSite());
				tddl.setCollectTime(po.getCollectTime());
				map2.put(sqlText, tddl);
			}
			
			combineTddlPo(tddl, po);
		}
	}
	
	/**
	 * 合并tddlpo内的数据
	 */
	private void combineTddlPo(TddlPo resultPo, TddlPo tempPo) {
		resultPo.setExeCount(tempPo.getExeCount() + resultPo.getExeCount());
		resultPo.setRespTime((tempPo.getRespTime() * tempPo.getExeCount() + resultPo.getRespTime() * resultPo.getExeCount()) / (tempPo.getExeCount() + resultPo.getExeCount()));
		if (tempPo.getMaxResp() > resultPo.getMaxResp()) {
			resultPo.setMaxResp(tempPo.getMaxResp());
			resultPo.setMaxRespDate(tempPo.getMaxRespDate());
		}
		
		if (tempPo.getMinResp() < resultPo.getMinResp()) {
			resultPo.setMinResp(tempPo.getMinResp());
			resultPo.setMinRespDate(tempPo.getMinRespDate());
		}
	}
	
	private void insertToDb(Map<String, Map<String, Map<String, TddlPo>>> resultMap) {
		logger.info(" tddl 开始入库");
		long start = System.currentTimeMillis();
		List<TddlPo> objectList = new ArrayList<TddlPo>();
		
		// 汇总数据统计的Map
		Map<String, TddlPo> cacheSumValue = new HashMap<String, TddlPo>();
		
		for (Map.Entry<String, Map<String, Map<String, TddlPo>>> entry : resultMap.entrySet()) {
			for (Map.Entry<String, Map<String, TddlPo>> entry1 : entry.getValue().entrySet()) {
				String dbfeature = entry1.getKey();
				for (Map.Entry<String, TddlPo> entry2 : entry1.getValue().entrySet()) {
					TddlPo tddl = entry2.getValue();
					
					
					TddlPo sumTddl = cacheSumValue.get(dbfeature);
					if (sumTddl == null) {
						sumTddl = new TddlPo();
						sumTddl.setAppName(tddl.getAppName());
						sumTddl.setDbFeature(tddl.getDbFeature());
						sumTddl.setDbName(tddl.getDbName());
						sumTddl.setDbIp(tddl.getDbIp());
						sumTddl.setDbPort(tddl.getDbPort());
						sumTddl.setCollectTime(tddl.getCollectTime());
						cacheSumValue.put(dbfeature, sumTddl);
					}
					combineTddlPo(sumTddl, tddl);	
					
					if (objectList.size() < COUNT) {
						objectList.add(tddl);
					} else {
						reportContent.putReportDateOfDayTDDL(objectList);
						objectList.clear();
					}
				}
			}
		}
		
		if (objectList.size() > 0) {
			reportContent.putReportDateOfDayTDDL(objectList);
			objectList.clear();
		}
		
		for (Map.Entry<String, TddlPo> entry : cacheSumValue.entrySet()) {
			TddlPo tddl = entry.getValue();
			
			if (objectList.size() < COUNT) {
				objectList.add(tddl);
			} else {
				reportContent.putReportDateOfDayTDDLSummaryNew(objectList);
				objectList.clear();
			}
		}
		
		if (objectList.size() > 0) {
			reportContent.putReportDateOfDayTDDLSummaryNew(objectList);
			objectList.clear();
		}
		
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp0", collectTime);
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp1", collectTime);
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp2", collectTime);
		
		long usedTime = (System.currentTimeMillis() - start)/1000;
		logger.info(" tddl 入库结束，用时：" + usedTime);
	}
	
	public static void main(String[] args) {
		TddlAnalyseDayJob job = new TddlAnalyseDayJob();
		try {
			job.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
