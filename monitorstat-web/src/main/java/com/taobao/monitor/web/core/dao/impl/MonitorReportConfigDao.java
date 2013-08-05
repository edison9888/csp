/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.schedule.QuartzManager;
import com.taobao.monitor.web.vo.ReportAcceptor;
import com.taobao.monitor.web.vo.ReportTemplate;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-11 - 下午08:35:11
 * @version 1.0
 */
public class MonitorReportConfigDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorReportConfigDao.class);

	/**
	 * 新增报表
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-11 - 下午09:03:45
	 */
	public void addReport(ReportTemplate reportTemplate) {
		String sql = "insert into ms_monitor_report(report_name, report_type) values(?,?)";
		try {
			this.execute(sql, new Object[] {
					reportTemplate.getReportName(), 
					reportTemplate.getReportType()
			});
		} catch (SQLException e) {
			logger.error("addReportTemplate[ms_monitor_report] 出错", e);
		}
		String select_sql = "select id from ms_monitor_report where report_name=? GROUP BY id DESC LIMIT 1";
		int reportId = 0;
		try {
			reportId = this.getIntValue(select_sql, new Object[] { reportTemplate.getReportName() });
		} catch (SQLException e) {
			logger.error("addReportTemplate[ms_monitor_report] 出错", e);
		}
		reportTemplate.setReportId(reportId);
		addReportTemplate(reportTemplate);
	}
	
	/**
	 * 添加报表模板配置
	 * @author 斩飞
	 * 2011-5-18 - 下午05:57:46
	 */
	public void addReportTemplate(ReportTemplate reportTemplate){
		String template_sql = "insert into ms_monitor_report_template(report_id, " +
				"type, path, quartz_cron) values(?,?,?,?)";
		try {
			this.execute(template_sql, new Object[] { reportTemplate.getReportId(), 
					reportTemplate.getType(), reportTemplate.getPath(),
					reportTemplate.getQuartzCron() });
		} catch (SQLException e) {
			logger.error("addReportTemplate[ms_monitor_report_template] 出错", e);
		}
	}
	
	/**
	 * 修改报表信息
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-13 - 上午11:30:27
	 */
	public void updateBaseReportInfo(ReportTemplate reportTemplate){
		String sql ="UPDATE ms_monitor_report SET report_name=?, report_type=? WHERE id=?";
		try {
			this.execute(sql, new Object[]{
					reportTemplate.getReportName(),
					reportTemplate.getReportType(),
					reportTemplate.getReportId()});
		} catch (SQLException e) {
			logger.error("updateBaseReportInfo出错", e);
		}
	}
	
	/**
	 * 修改报表信息
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-13 - 上午11:30:27
	 */
	public void updateReportTemplate(ReportTemplate reportTemplate){
		String sql ="UPDATE ms_monitor_report_template SET path=?,quartz_cron=? " +
				" WHERE report_id=?";
		try {
			this.execute(sql, new Object[]{
					reportTemplate.getPath(),
					reportTemplate.getQuartzCron(),
					reportTemplate.getReportId()
					});
		} catch (SQLException e) {
			logger.error("updateReportTemplate出错", e);
		}
	}
	
	/**
	 * 获取所有当前报表
	 * @author 斩飞
	 * @return
	 * 2011-5-11 - 下午09:19:50
	 */
	public List<ReportTemplate> findAllReport(){
		String sql = "SELECT r.id,r.report_name,t.path,t.type,t.quartz_cron" +
				" FROM ms_monitor_report r, ms_monitor_report_template t " +
				" WHERE r.id = t.report_id";
		
		final List<ReportTemplate> list = new ArrayList<ReportTemplate>();
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					ReportTemplate po = new ReportTemplate();
					po.setId(rs.getInt("id"));
					po.setReportId(rs.getInt("id"));
					po.setReportName(rs.getString("report_name"));
					po.setType(rs.getString("type"));
					po.setPath(rs.getString("path"));
					po.setJobState(QuartzManager.getTaskState(
							Constants.TRIGGER_PREFIX+po.getReportId()));
					po.setJobStateMsg(QuartzManager.getTaskStateMsg(
							Constants.TRIGGER_PREFIX+po.getReportId()));
					po.setQuartzCron(rs.getString("quartz_cron"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findAllReport出错", e);
		}
		
		return list;
	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
//	public int getReportIdByName(String reportName){
//		int reportId = 0;
//		String sql = "SELECT * FROM ms_monitor_report r WHERE r.report_name =?";
//		try {
//			reportId = this.getIntValue(sql, new Object[]{reportName});
//		} catch (Exception e) {
//			logger.error("getReportIdByName出错", e);
//		}
//		return reportId;
//	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
	public ReportTemplate getReportById(int reportId){
		String sql = "SELECT r.report_type, r.report_name,t.path,t.quartz_cron,t.TYPE " +
				" FROM ms_monitor_report r,ms_monitor_report_template t " +
				" WHERE r.id=t.report_id AND r.id=?";
		final ReportTemplate reportTemplate = new ReportTemplate();
		try {
			this.query(sql, new Object[]{reportId}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					reportTemplate.setReportName(rs.getString("report_name"));
					reportTemplate.setReportType(rs.getInt("report_type"));
					reportTemplate.setPath(rs.getString("path"));
					reportTemplate.setQuartzCron(rs.getString("quartz_cron"));
					reportTemplate.setType(rs.getString("type"));			
				}
			});
		} catch (Exception e) {
			logger.error("getReportById出错", e);
		}
		return reportTemplate;
	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
	public ReportTemplate getReportTemplateById(int reportId, String type){
		String sql = "SELECT t.report_id, t.path, t.quartz_cron, t.TYPE " +
				" FROM ms_monitor_report_template t " +
				" WHERE t.report_id = ? AND t.TYPE =?";
		final ReportTemplate reportTemplate = new ReportTemplate();
		try {
			this.query(sql, new Object[]{reportId, type}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					reportTemplate.setReportId(rs.getInt("report_id"));
					reportTemplate.setPath(rs.getString("path"));
					reportTemplate.setQuartzCron(rs.getString("quartz_cron"));
					reportTemplate.setType(rs.getString("type"));			
				}
			});
		} catch (Exception e) {
			logger.error("getReportTemplateById出错", e);
		}
		return reportTemplate;
	}
	
	/**
	 * 获取报表接收人列表
	 * @author 斩飞
	 * @param reportId
	 * @return
	 * 2011-5-15 - 下午02:23:00
	 */
	public List<ReportAcceptor> getReportAcceptorByReportId(int reportId, String type){
		String sql ="SELECT a.address, a.report_param, a.type FROM ms_monitor_report_acceptor a " +
				"WHERE a.report_id =? AND a.TYPE=? ";
		final List<ReportAcceptor> list = new ArrayList<ReportAcceptor>();
		try {
			this.query(sql, new Object[]{reportId, type}, new SqlCallBack() {	
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ReportAcceptor acceptor = new ReportAcceptor();
					acceptor.setAddress(rs.getString("address"));
					acceptor.setReportParam(rs.getString("report_param"));
					acceptor.setType(rs.getString("type"));
					list.add(acceptor);
				}
			});
		} catch (Exception e) {
			logger.error("getReportAcceptorByReportId出错", e);
		}
		return list;
	}
	
	/**
	 * 删除报表
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-12 - 上午10:33:34
	 */
	public void deleteReport(int reportId){
		String sql = "delete FROM ms_monitor_report WHERE id = ?";
		try {
			this.execute(sql, new Object[] { reportId });
		} catch (SQLException e) {
			logger.error("deleteReport[ms_monitor_report] 出错", e);
		}
	}
	
	/**
	 * 删除指定报表的模板信息
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-13 - 上午09:47:21
	 */
	public void deleteReportTemplate(int reportId){
		String dele_sql = "delete FROM ms_monitor_report_template  WHERE report_id = ?";
		try {
			this.execute(dele_sql, new Object[] { reportId });
		} catch (SQLException e) {
			logger.error("deleteReportTemplate[ms_monitor_report_template] 出错", e);
		}
	}
	
	/**
	 * 获取报表接收人
	 * @author 斩飞
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - 下午01:38:51
	 */
	public List<ReportAcceptor> getReportAppAndAcceptor(int reportId, String type){
		String sql ="SELECT distinct ac.report_param, ac.address FROM ms_monitor_report r ,ms_monitor_report_acceptor ac " +
				" WHERE r.id = ac.report_id AND ac.report_id ="+reportId+" and ac.type ='"+type+"'";
		final List<ReportAcceptor> list = new ArrayList<ReportAcceptor>();
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ReportAcceptor acceptor = new ReportAcceptor();
					acceptor.setReportParam(rs.getString("report_param"));
					acceptor.setAddress(rs.getString("address"));	
					list.add(acceptor);
				}			
			});
		} catch (Exception e) {
			logger.error("getReportAcceptor出错", e);
		}
		return list;
	}
	
	/**
	 * 获取报表接收人
	 * @author 斩飞
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - 下午01:38:51
	 */
	public List<ReportAcceptor> getReportParamsByAddress(int reportId, String type, String addressList){
		String sql ="SELECT ac.report_param, ac.address FROM ms_monitor_report r ,ms_monitor_report_acceptor ac " +
				" WHERE r.id = ac.report_id AND ac.report_id ="+reportId+" and ac.type ='"+type
				+"' AND address IN("+addressList+")";
		final List<ReportAcceptor> list = new ArrayList<ReportAcceptor>();
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ReportAcceptor acceptor = new ReportAcceptor();
					acceptor.setReportParam(rs.getString("report_param"));
					acceptor.setAddress(rs.getString("address"));	
					list.add(acceptor);
				}			
			});
		} catch (Exception e) {
			logger.error("getReportParamsByAddress出错", e);
		}
		return list;
	}
	
	/**
	 * 应用报表接收人批量入库
	 * @author 斩飞
	 * @param acceptors
	 * 2011-5-12 - 下午03:37:31
	 */
	public void addReportAcceptor(List<ReportAcceptor> acceptors){
		String sql = "INSERT INTO ms_monitor_report_acceptor(report_id,report_param,address,TYPE) " +
				" VALUES(?,?,?,?)";
		List<Object[]> tmp = new ArrayList<Object[]>();
		for(ReportAcceptor atpanelOne:acceptors){
			Object[] o = new Object[] {
					atpanelOne.getReportId(), 
					atpanelOne.getReportParam(),
					atpanelOne.getAddress(), 
					atpanelOne.getType()
					};
			tmp.add(o);
		}
		try {
			this.executeBatch(sql, tmp, DbRouteManage.get().getDbRouteByRouteId("Main"));
		} catch (Exception e) {
			logger.error("addReportAcceptor 出错",e);
		}
	}
	
	/**
	 * 删除指定的报表信息
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-13 - 上午09:46:33
	 */
	public void deleteReAcceptorByReportId(int reportId){
		String sql = "DELETE FROM ms_monitor_report_acceptor WHERE " +
				"report_id ="+reportId;
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error("deleteReAcceptorByReportId 出错",e);
		}
	}
	
	/**
	 * 删除应用报表对应接收人信息
	 * @author 斩飞
	 * @param appId
	 * @param addres
	 * 2011-5-12 - 下午03:39:19
	 */
	public void deleteReAcceptorByReIdAndAddress(int reportId, String addres){
		String sql = "DELETE FROM ms_monitor_report_acceptor WHERE " +
				"report_id ="+reportId+" AND address IN("+addres+")";
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error("deleteReAcceptorByAppAndAddress 出错",e);
		}
	}
	
}
