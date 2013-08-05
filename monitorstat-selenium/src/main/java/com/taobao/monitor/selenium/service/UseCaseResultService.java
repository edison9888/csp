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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-26 - ����03:59:03
 * @version 1.0
 */
public interface UseCaseResultService {
	
	/**
	 * ����������Խ����Ϣ
	 * 
	 * 2011-6-7 - ����10:20:34
	 */
	public void addUCResult(TestMetricsBean metricsBean);

	/**
	 * ��ȡ���в�����Ϣ
	 * 
	 * @param ucId
	 * @param ucMap
	 * @return
	 * 2011-6-29 - ����11:22:50
	 */
	public List<TestMetricsBean> findUcResultByQuery(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap);
	
	/**
	 * ������ϸ��Ϣ
	 * 
	 * @param LogBean
	 * 2011-6-10 - ����12:47:13
	 */
	public void addUcResultDetail(LogBean LogBean);
	
	/**
	 * ��ȡ��������ִ����ϸ�����Ϣ
	 * 
	 * @param ucId
	 * @return
	 * 2011-6-9 - ����11:00:12
	 */
	public List<LogBean> findUcDetailResultByResultId(long ucResultId);
	
	/**
	 * ��ȡ���Խ��ͳ����Ϣ
	 * 
	 * @param useCaseTestStat
	 * @return
	 * 2011-6-29 - ����11:31:38
	 */
	public Map<Integer, UseCaseTestStat> findUcResultStat(UseCaseTestStat ucTestStat,
			Map<Integer, UseCase> ucMap);
	
	/**
	 * ɾ����������
	 * 
	 * @param startTime
	 * @param endTime
	 * 2011-7-12 - ����02:31:02
	 */
	public void deleteUcResult(String startTime, String endTime);
}
