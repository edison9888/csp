/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.dao.model.UseCaseTestStat;
import com.taobao.monitor.selenium.util.log.LogBean;
import com.taobao.monitor.selenium.util.log.TestMetricsBean;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午03:59:03
 * @version 1.0
 */
public interface UseCaseResultService {
	
	/**
	 * 添加用例测试结果信息
	 * 
	 * 2011-6-7 - 上午10:20:34
	 */
	public void addUCResult(TestMetricsBean metricsBean);

	/**
	 * 获取所有测试信息
	 * 
	 * @param ucId
	 * @param ucMap
	 * @return
	 * 2011-6-29 - 上午11:22:50
	 */
	public List<TestMetricsBean> findUcResultByQuery(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap);
	
	/**
	 * 保存详细信息
	 * 
	 * @param LogBean
	 * 2011-6-10 - 下午12:47:13
	 */
	public void addUcResultDetail(LogBean LogBean);
	
	/**
	 * 获取单次用例执行详细结果信息
	 * 
	 * @param ucId
	 * @return
	 * 2011-6-9 - 下午11:00:12
	 */
	public List<LogBean> findUcDetailResultByResultId(long ucResultId);
	
	/**
	 * 获取测试结果统计信息
	 * 
	 * @param useCaseTestStat
	 * @return
	 * 2011-6-29 - 上午11:31:38
	 */
	public Map<Integer, UseCaseTestStat> findUcResultStat(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap);
	
	/**
	 * 删除过期数据
	 * 
	 * @param startTime
	 * @param endTime
	 * 2011-7-12 - 下午02:31:02
	 */
	public void deleteUcResult(String startTime, String endTime);
}
