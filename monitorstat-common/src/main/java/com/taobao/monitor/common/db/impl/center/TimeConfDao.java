package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.TimeConfPo;

/**
 * ʵʱ������TimeConf��dao
 * @author wuhaiqian.pt
 *
 */
public class TimeConfDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(TimeConfDao.class);

	
	/**
	 * ���addTimeConfData
	 * @param timeConf
	 */
	public boolean addTimeConfData(TimeConfPo timeConf) {
		try {
			
			String sql = "insert into MS_MONITOR_APP_TIME_CONF "
					+ "(APP_ID,	analyse_class, analyse_future,FILE_PATH, ANALYSE_FREQUENCY, SPLIT_CHAR, ALIAS_LOG_NAME,tailType,analyse_type,obtain_type) values(?,?,?,?,?,?,?,?,?,?)";
			this.execute(sql, new Object[] {timeConf.getAppId(), timeConf.getClassName(), timeConf.getAnalyseFuture(),timeConf.getFilePath(), timeConf.getAnalyseFrequency(), timeConf.getSplitChar(), timeConf.getAliasLogName(), timeConf.getTailType(), timeConf.getAnalyseType(),timeConf.getObtainType()});
		} catch (Exception e) {
			logger.error("addTimeConfData ����,", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param appId
	 */
	public boolean deleteTimeConfByAppId(int appId) {
		String sql = "delete from MS_MONITOR_APP_TIME_CONF where APP_ID=?";
		try {
			this.execute(sql, new Object[] { appId });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			return false;
		}
		
		return true;
	}

	/**
	 * ɾ��TimeConfPo
	 * @param confId
	 */
	public boolean deleteTimeConfByConfId(int confId) {
		String sql = "delete from MS_MONITOR_APP_TIME_CONF where CONF_ID=?";
		try {
			this.execute(sql, new Object[] { confId });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(TimeConfPo timeConf) {
		String sql = "delete from MS_MONITOR_APP_TIME_CONF where conf_id=?";
		try {
			this.execute(sql, new Object[] { timeConf.getConfId() });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * ��ȡȫ��timeConf
	 * 
	 * @return
	 */
	public List<TimeConfPo> findAllAppTimeConf() {
		String sql = "select * from MS_MONITOR_APP_TIME_CONF";

		final List<TimeConfPo> timeConfPoList = new ArrayList<TimeConfPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					TimeConfPo po = new TimeConfPo();
					po.setAppId(rs.getInt("app_id"));
					po.setConfId(rs.getInt("conf_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setAnalyseFuture(rs.getString("analyse_future"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setAnalyseFrequency(rs.getInt("ANALYSE_FREQUENCY"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					po.setTailType(rs.getString("tailType"));
					po.setAnalyseType(rs.getInt("analyse_type"));
					po.setObtainType(rs.getInt("obtain_type"));
					timeConfPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppTimeConf����", e);
		}
		return timeConfPoList;
	}
	
	/**
	 * ����timeConfPo����
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConf(TimeConfPo timeConf){
		String sql = "update MS_MONITOR_APP_TIME_CONF set analyse_type=?, tailType=?,analyse_class=?,FILE_PATH=?,ANALYSE_FREQUENCY=?,SPLIT_CHAR=?, ALIAS_LOG_NAME=?,analyse_future=?,obtain_type=? where app_id=? and conf_id=? ";
		try {
			this.execute(sql, new Object[]{timeConf.getAnalyseType(),timeConf.getTailType(),timeConf.getClassName(), timeConf.getFilePath(), timeConf.getAnalyseFrequency(), timeConf.getSplitChar(), timeConf.getAliasLogName(),timeConf.getAnalyseFuture(),timeConf.getObtainType(),timeConf.getAppId(), timeConf.getConfId()});
		} catch (SQLException e) {
			logger.error("updateTimeConf����", e);
			return false;
		}
		return true;
	}
	/**
	 * ����ʵʱӦ��appid ��ѯ����������
	 * @param appId
	 * @return
	 */
	public List<TimeConfPo> findTimeConfByAppId(int appId){
		String sql = "select c.* from MS_MONITOR_APP_TIME_CONF c where c.app_id=?";

		final List<TimeConfPo> timeConfPoList = new ArrayList<TimeConfPo>();

		try {
			this.query(sql,new Object[]{appId}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					TimeConfPo po = new TimeConfPo();
					po.setAppId(rs.getInt("app_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setAnalyseFuture(rs.getString("analyse_future"));
					po.setAnalyseFrequency(rs.getInt("ANALYSE_FREQUENCY"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					po.setTailType(rs.getString("tailType"));
					po.setAnalyseType(rs.getInt("analyse_type"));
					po.setConfId(rs.getInt("conf_id"));
					po.setObtainType(rs.getInt("obtain_type"));
					timeConfPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppTimeConf����", e);
		}
		return timeConfPoList;		
	}
	
	/**
	 * ����confId��ѯ��Ӧ������
	 * @param confId
	 * @return
	 */
	public TimeConfPo findTimeConfByConfId(int confId){
		String sql = "select c.* from MS_MONITOR_APP_TIME_CONF c where c.conf_id=?";

		final TimeConfPo po = new TimeConfPo();

		try {
			this.query(sql,new Object[]{confId}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setAnalyseFrequency(rs.getInt("ANALYSE_FREQUENCY"));
					po.setAnalyseFuture(rs.getString("analyse_future"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					po.setTailType(rs.getString("tailType"));
					po.setAnalyseType(rs.getInt("analyse_type"));
					po.setObtainType(rs.getInt("obtain_type"));
					po.setConfId(rs.getInt("conf_id"));
					
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppTimeConf����", e);
		}
		return po;		
	}
	
}
