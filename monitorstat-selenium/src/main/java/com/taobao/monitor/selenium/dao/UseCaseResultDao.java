/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.dao.model.UseCaseTestStat;
import com.taobao.monitor.selenium.util.DateUtil;
import com.taobao.monitor.selenium.util.log.LogBean;
import com.taobao.monitor.selenium.util.log.TestMetricsBean;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-7 - 上午10:21:54
 * @version 1.0
 */
@Repository
public class UseCaseResultDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(UseCaseResultDao.class);
	
	/**
	 * 
	 */
	public UseCaseResultDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
	}
	
	/**
	 * 添加测试用例汇总信息
	 * 
	 * @param metricsBean
	 * 2011-6-7 - 上午10:30:42
	 */
	public void addUCResult(TestMetricsBean metricsBean){
		String sql = "INSERT INTO csp_selenium_usecase_result(usecase_id, usecase_result_id, test_duration, " +
				"test_started,test_finished, commands_processed, verifications_processed, failed_commands, " +
				"last_failed_message, commands_notlogged, finally_state, gmt_create)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{
					metricsBean.getUsecaseId(),
					metricsBean.getUsecaseResultId(),
					metricsBean.getTestDuration(),
					DateUtil.getDateYMDHMSFormat().format(metricsBean.getStartTimeStamp()),
					DateUtil.getDateYMDHMSFormat().format(metricsBean.getEndTimeStamp()),
					metricsBean.getCommandsProcessed(),
					metricsBean.getVerificationsProcessed(),
					metricsBean.getFailedCommands(),
					metricsBean.getLastFailedCommandMessage(),
					ArrayUtils.toString(metricsBean.getCommandsExcludedFromLogging()),
					metricsBean.getFinallyState(),
					new Date()
			});
		} catch (SQLException e) {
			logger.error("addUCResult 出错！", e);
		}
	}
	
	/**
	 * 
	 * 
	 * @param ucId
	 * @return
	 * 2011-6-9 - 下午11:00:12
	 */
	public List<TestMetricsBean> findUcResultByQuery(UseCaseTestStat ucTestStat, final Map<Integer, UseCase> ucMap){
//		String sql = "SELECT r.commands_notlogged,r.commands_processed,r.failed_commands," +
//				" r.last_failed_message,r.test_duration,r.test_finished,r.test_started," +
//				" r.usecase_id, r.usecase_result_id FROM csp_selenium_usecase_result r";
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT r.commands_notlogged,r.commands_processed,r.failed_commands,r.finally_state,");
		sql.append(" r.last_failed_message,r.test_duration,r.test_finished,r.test_started,");
		sql.append(" r.usecase_id, r.usecase_result_id FROM csp_selenium_usecase_result r");
		sql.append(" WHERE r.test_started>='"+ucTestStat.getStartDate()+"' AND r.test_finished<='"+ucTestStat.getEndDate()+"'");
		if(ucTestStat.getUcIds() != null && ucTestStat.getUcIds().size() > 0){
			sql.append(" AND r.usecase_id IN(");
			for(int ucid:ucTestStat.getUcIds()){
				sql.append(ucid+",");
			}
			sql.append("0)");
		}
		
		final List<TestMetricsBean> list = new ArrayList<TestMetricsBean>();
		try {
			this.query(sql.toString(), new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					TestMetricsBean ucResult = new TestMetricsBean();
					ucResult.setUsecaseId(rs.getInt("usecase_id"));
					ucResult.setUsecaseResultId(rs.getLong("usecase_result_id"));
					ucResult.setCommandsProcessed(rs.getInt("commands_processed"));
					ucResult.setFailedCommands(rs.getInt("failed_commands"));
					ucResult.setLastFailedCommandMessage(rs.getString("last_failed_message"));
					ucResult.setDuration(rs.getLong("test_duration"));
					ucResult.setStartDate(rs.getString("test_started"));
					ucResult.setEndDate(rs.getString("test_finished"));
					ucResult.setUseCaseAlias(ucMap.get(ucResult.getUsecaseId())==null?"":
						ucMap.get(ucResult.getUsecaseId()).getUseCaseAlias());
					ucResult.setFinallyState(rs.getInt("finally_state"));
					list.add(ucResult);	
				}
			});
		} catch (Exception e) {
			logger.error("findUcResultByQuery 出错 "+e);
		}
		return list;
	}
	
	/**
	 * 保存详细信息
	 * 
	 * @param LogBean
	 * 2011-6-10 - 下午12:47:13
	 */
	public void addUcResultDetail(LogBean logBean){
		String sql = "INSERT INTO csp_selenium_usecase_result_detail(usecase_result_id, selenium_command," +
				" page_element, element_value, response_rc, response_selenium, cost_time," +
				" calling_class_Linenumber, gmt_create)VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{
					logBean.getUsecaseResultId(),
					logBean.getCommandName(),
					logBean.getArgs()[0],
					logBean.getArgs()[1],
					logBean.getSrcResult(),
					logBean.getSelResult(),
					logBean.getDeltaMillis(),
					logBean.getCallingClass(),
					logBean.getGmtCreate()
			});
		} catch (SQLException e) {
			logger.error("addUcResultDetail 出错！", e);
		}
	}
	
	/**
	 * 获取单次用例执行详细结果信息
	 * 
	 * @param ucId
	 * @return
	 * 2011-6-9 - 下午11:00:12
	 */
	public List<LogBean> findUcDetailResultByResultId(long ucResultId){
		String sql = "SELECT rd.calling_class_Linenumber, rd.cost_time, rd.element_value, rd.page_element, " +
				" rd.response_rc, rd.response_selenium, rd.selenium_command, rd.usecase_result_id " +
				" FROM csp_selenium_usecase_result_detail rd WHERE rd.usecase_result_id = ?";
		final List<LogBean> list = new ArrayList<LogBean>();
		try {
			this.query(sql, new Object[]{ucResultId}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					LogBean log = new LogBean();
					log.setUsecaseResultId(rs.getLong("usecase_result_id"));
					log.setCallingClass(rs.getString("calling_class_Linenumber"));
					log.setCostTime(rs.getLong("cost_time"));
					log.setPageElement(rs.getString("page_element"));
					log.setElementValue(rs.getString("element_value"));
					log.setResponseRc(rs.getString("response_rc"));
					log.setResponseSelenium(rs.getString("response_selenium"));
					log.setCommandName(rs.getString("selenium_command"));
					list.add(log);	
				}
			});
		} catch (Exception e) {
			logger.error("findUcDetailResultByResultId 出错 "+e);
		}
		return list;
	}
	
	/**
	 * 统计测试汇总信息
	 * 
	 * @param ucId
	 * @return
	 * 2011-6-29 - 上午11:42:04
	 */
	public List<UseCaseTestStat> findUcResultStat(UseCaseTestStat ucTestStat, final Map<Integer, UseCase> ucMap){
		final DecimalFormat df =new DecimalFormat("#.00");
		//final DecimalFormat rateDf =new DecimalFormat("#.0000");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(id) AS ids, AVG(R.test_duration) AS test_duration, SUM(R.failed_commands) as failed_commands ,R.commands_processed,");
		sql.append(" R.usecase_id,R.verifications_processed FROM csp_selenium_usecase_result R ");
		sql.append(" WHERE R.test_started>='"+ucTestStat.getStartDate()+"' AND R.test_finished<='"+ucTestStat.getEndDate()+"'");
		if(ucTestStat.getUcIds() != null && ucTestStat.getUcIds().size() > 0){
			sql.append(" AND R.usecase_id IN(");
			for(int ucid:ucTestStat.getUcIds()){
				sql.append(ucid+",");
			}
			sql.append("0)");
		}
		sql.append(" GROUP BY R.usecase_id");
		final List<UseCaseTestStat> list = new ArrayList<UseCaseTestStat>();
		try {
			this.query(sql.toString(), new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UseCaseTestStat ucStat = new UseCaseTestStat();
					ucStat.setCount(rs.getInt("ids"));
					ucStat.setUseCaseId(rs.getInt("usecase_id"));
					ucStat.setCommandsProcessed(rs.getInt("commands_processed"));
					ucStat.setVerificationsProcessed(rs.getInt("verifications_processed"));
					ucStat.setFailedCommands(rs.getInt("failed_commands"));
					//ucStat.setDuration(df.format(rs.getDouble("test_duration")));
					ucStat.setUseCaseAlias(ucMap.get(ucStat.getUseCaseId())==null?"":
						ucMap.get(ucStat.getUseCaseId()).getUseCaseAlias());
					//计算成功率
					long failedTimes = ucStat.getFailedCommands();
					long successTimes = ucStat.getCount() - failedTimes;
					double rate = 0;
					if(ucStat.getCount() != 0){
						rate = successTimes/ucStat.getCount();
					}
					String rateStr = df.format(rate*100);
					ucStat.setSuccessRates(rateStr.equals(0)?"":(rateStr+"%"));
					list.add(ucStat);	
				}
			});
		} catch (Exception e) {
			logger.error("findUcResultStat 出错 "+e);
		}
		return list;
	}

	/**
	 * 删除过期数据
	 * 
	 * @param startTime
	 * @param endTime
	 * 2011-7-12 - 下午02:32:15
	 */
	public void deleteUcResult(String startTime, String endTime){
		String sql = "DELETE FROM csp_selenium_usecase_result " +
				" WHERE gmt_create>=? AND gmt_create<=?";
		try {
			this.execute(sql, new Object[]{startTime, endTime});
		} catch (SQLException e) {
			logger.error("deleteUcResult 出错 "+e);
		}
	}
	
	/**
	 * 删除过期数据
	 * 
	 * @param startTime
	 * @param endTime
	 * 2011-7-12 - 下午02:32:15
	 */
	public void deleteUcResultDetail(String startTime, String endTime){
		String sql = "DELETE FROM csp_selenium_usecase_result_detail " +
				" WHERE gmt_create>=? AND gmt_create<=?";
		try {
			this.execute(sql, new Object[]{startTime, endTime});
		} catch (SQLException e) {
			logger.error("deleteUcResultDetail 出错 "+e);
		}
	}
}
