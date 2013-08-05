/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.taobao.monitor.selenium.dao.UseCaseResultDao;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.dao.model.UseCaseTestStat;
import com.taobao.monitor.selenium.service.UseCaseResultService;
import com.taobao.monitor.selenium.util.log.LogBean;
import com.taobao.monitor.selenium.util.log.TestMetricsBean;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-30 - 上午11:04:18
 * @version 1.0
 */
@Service
public class UseCaseResultServiceImpl implements UseCaseResultService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="useCaseResultDao")
	private UseCaseResultDao useCaseResultDao;
	
	/**
	 * 
	 * 2011-6-7 - 上午10:21:00
	 */
	@Override
	public void addUCResult(TestMetricsBean metricsBean) {
		useCaseResultDao.addUCResult(metricsBean);	
	}
	
	/**
	 * @param ucId
	 * @return
	 * 2011-6-9 - 下午11:00:39
	 */
	@Override
	public List<TestMetricsBean> findUcResultByQuery(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap) {
		return useCaseResultDao.findUcResultByQuery(ucTestStat, ucMap);
	}
	
	/**
	 * @param LogBean
	 * 2011-6-10 - 下午12:47:34
	 */
	@Override
	public void addUcResultDetail(LogBean LogBean) {
		useCaseResultDao.addUcResultDetail(LogBean);
	}
	
	/**
	 * @param ucResultId
	 * @return
	 * 2011-6-10 - 下午02:18:37
	 */
	@Override
	public List<LogBean> findUcDetailResultByResultId(long ucResultId) {
		return useCaseResultDao.findUcDetailResultByResultId(ucResultId);
	}

	/**
	 * @param useCaseTestStat
	 * @return
	 * 2011-6-29 - 上午11:32:23
	 */
	@Override
	public Map<Integer, UseCaseTestStat> findUcResultStat(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap){
		DecimalFormat df =new DecimalFormat("#.00");
		Map<Integer, UseCaseTestStat> map = new HashMap<Integer, UseCaseTestStat>(); 
		List<TestMetricsBean> list = useCaseResultDao.findUcResultByQuery(ucTestStat, ucMap);
		UseCaseTestStat tmp = null;
		for(TestMetricsBean tm:list){
			tmp = map.get(tm.getUsecaseId());
			if(tmp == null){
				tmp = new UseCaseTestStat();
				tmp.setCount(1);
				tmp.setCommandsProcessed(tm.getCommandsProcessed());
				tmp.setDuration(tm.getDuration());
				if(tm.getFinallyState() == 0)
					tmp.setSuccessTimes(1);
				tmp.setUseCaseId(tm.getUsecaseId());
				tmp.setUseCaseAlias(tm.getUseCaseAlias());
				map.put(tm.getUsecaseId(), tmp);
			}else{
				tmp.setCount(tmp.getCount()+1);
				tmp.setDuration(tmp.getDuration()+tm.getDuration());
				if(tm.getFinallyState() == 0)
					tmp.setSuccessTimes(tmp.getSuccessTimes()+1);
			}	
		}
		//统计数据
		for(Map.Entry<Integer, UseCaseTestStat> en:map.entrySet()){
			//计算成功率
			tmp = en.getValue();
			long successTimes = tmp.getSuccessTimes();
			long failedTimes = tmp.getCount() - successTimes;
			double rate = 0;
			double duration = 0;
			if(tmp.getCount() != 0){
				rate = (double)successTimes/tmp.getCount();
				duration = (double)tmp.getDuration()/tmp.getCount();
			}
			String rateStr = df.format(rate*100);
			String durationStr = df.format(duration);
			tmp.setSuccessRates(rateStr.equals(0)?"":(rateStr+"%"));
			tmp.setFailedTimes(failedTimes);
			tmp.setDurationStr(durationStr);
		}
		return map;
	}

	/**
	 * @param startTime
	 * @param endTime
	 * 2011-7-12 - 下午02:31:20
	 */
	@Override
	public void deleteUcResult(String startTime, String endTime) {
		useCaseResultDao.deleteUcResult(startTime, endTime);
		useCaseResultDao.deleteUcResultDetail(startTime, endTime);
		
	}
}
