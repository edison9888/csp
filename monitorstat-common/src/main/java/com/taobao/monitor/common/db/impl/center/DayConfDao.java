package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.DayConfPo;

/**
 * �ձ�������DayConf��dao
 * @author wuhaiqian.pt
 *
 */
public class DayConfDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(DayConfDao.class);

	/**
	 * ���addDayConfData
	 * @param dayConf
	 */
	public boolean addDayConfData(DayConfPo dayConf) {
		try {
			
			String sql = "insert into MS_MONITOR_APP_DAY_CONF "
					+ "(APP_ID,	analyse_class, FILE_PATH, SPLIT_CHAR, ALIAS_LOG_NAME) values(?,?,?,?,?)";
			this.execute(sql, new Object[] {dayConf.getAppId(), dayConf.getClassName(), dayConf.getFilePath(), dayConf.getSplitChar(), dayConf.getAliasLogName()});
		} catch (Exception e) {
			logger.error("addDayConfData ����,", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * ɾ��DayConfPo
	 * @param appId
	 */
	public void deleteDayConfByAppId(int appId) {
		String sql = "delete from MS_MONITOR_APP_DAY_CONF where APP_ID=?";
		try {
			this.execute(sql, new Object[] { appId });
		} catch (SQLException e) {
			logger.error("deleteDayConfData: ", e);
		}
	}

	/**
	 * ɾ��DayConfPo
	 * @param confId
	 */
	public void deleteDayConfByConfId(int confId) {
		String sql = "delete from MS_MONITOR_APP_DAY_CONF where CONF_ID=?";
		try {
			this.execute(sql, new Object[] { confId });
		} catch (SQLException e) {
			logger.error("deleteDayConfData: ", e);
		}
	}
	
	/**
	 * ɾ��DayConfPo
	 * @param dayConf
	 */
	public void deleteDayConfData(DayConfPo dayConf) {
		String sql = "delete from MS_MONITOR_APP_DAY_CONF where CONF_ID=?";
		try {
			this.execute(sql, new Object[] { dayConf.getAppId() });
		} catch (SQLException e) {
			logger.error("deleteDayConfData: ", e);
		}
	}
	
	/**
	 * ��ȡȫ��dayConf
	 * @return
	 */
	public List<DayConfPo> findAllAppDayConf() {
		String sql = "select * from MS_MONITOR_APP_DAY_CONF";

		final List<DayConfPo> dayConfPoList = new ArrayList<DayConfPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					DayConfPo po = new DayConfPo();
					po.setAppId(rs.getInt("app_id"));
					po.setConfId(rs.getInt("conf_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					dayConfPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppDayConf����", e);
		}
		return dayConfPoList;
	}
	
	
	/**
	 * ����confId��ȡdayConf
	 * @return
	 */
	public DayConfPo findAppDayConfByConfId(int confId) {
		String sql = "select * from MS_MONITOR_APP_DAY_CONF where conf_id = ?";

		final DayConfPo po = new DayConfPo();

		try {
			this.query(sql, new Object[]{confId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setConfId(rs.getInt("conf_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppDayConf����", e);
		}
		return po;
	}
	
	/**
	 * ����ʵʱӦ��appid ��ѯ������dayConf
	 * @return
	 */
	public List<DayConfPo> findAllAppDayConfById(int appId) {
		String sql = "select * from MS_MONITOR_APP_DAY_CONF where app_id=?";

		final List<DayConfPo> dayConfPoList = new ArrayList<DayConfPo>();

		try {
			this.query(sql, new Object[]{appId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					DayConfPo po = new DayConfPo();
					po.setAppId(rs.getInt("app_id"));
					po.setConfId(rs.getInt("conf_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					dayConfPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppDayConfById����", e);
		}
		return dayConfPoList;
	}
	
	
	/**
	 * ����dayConfPo����
	 * @param dayConf
	 * @return
	 */
	public boolean updateDayConf(DayConfPo dayConf){
		String sql = "update MS_MONITOR_APP_DAY_CONF set analyse_class=?, FILE_PATH=?,SPLIT_CHAR=?, ALIAS_LOG_NAME=? where app_id=? and conf_id=?";
		try {
			this.execute(sql, new Object[]{dayConf.getClassName(), dayConf.getFilePath(), dayConf.getSplitChar(), dayConf.getAliasLogName(),dayConf.getAppId(), dayConf.getConfId()});
		} catch (SQLException e) {
			logger.error("findAllAppDayConf", e);
			return false;
		}
		return true;
	}
	
}
