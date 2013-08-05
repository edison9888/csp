package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.TimeConfTmpPo;

/**
 * 实时配置类TimeConf的dao
 * 
 * @author wuhaiqian.pt
 * 
 */
public class TimeConfTmpDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(TimeConfTmpDao.class);

	/**
	 * 添加addTimeConfTmp
	 * 
	 * @param timeConf
	 */
	public boolean addTimeConfTmp(TimeConfTmpPo tmpPo) {
		try {

			String sql = "insert into MS_MONITOR_APP_TIME_CONF_TMP "
					+ "(analyse_class, FILE_PATH, analyse_future,ANALYSE_FREQUENCY, SPLIT_CHAR, ALIAS_LOG_NAME,tailType,analyse_type,analyse_desc,obtain_type) values(?,?,?,?,?,?,?,?,?,?)";
			this.execute(sql, new Object[] { tmpPo.getClassName(), tmpPo.getFilePath(), tmpPo.getAnalyseFuture(), tmpPo.getAnalyseFrequency(),
					tmpPo.getSplitChar(), tmpPo.getAliasLogName(), tmpPo.getTailType(), tmpPo.getAnalyseType(), tmpPo.getAnalyseDesc(),tmpPo.getObtainType() });
		} catch (Exception e) {
			logger.error("addTimeConfData 出错,", e);

			return false;
		}

		return true;
	}

	/**
	 * 删除TimeConfPo
	 * 
	 * @param appId
	 */
	public boolean deleteTimeConfTmp(int tmpId) {
		String sql = "delete from MS_MONITOR_APP_TIME_CONF_TMP where TMP_ID=?";
		try {
			this.execute(sql, new Object[] { tmpId });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 删除TimeConfPo
	 * 
	 * @param timeConf
	 */
	public boolean deleteTimeConfTmp(TimeConfTmpPo tmpPo) {
		String sql = "delete from MS_MONITOR_APP_TIME_CONF where tmp_id=?";
		try {
			this.execute(sql, new Object[] { tmpPo.getTmpId() });
		} catch (SQLException e) {
			logger.error("deleteTimeConfTmp: ", e);
			return false;
		}

		return true;
	}

	/**
	 * 获取全部timeConf
	 * 
	 * @return
	 */
	public List<TimeConfTmpPo> findAllAppTimeConfTmp() {
		String sql = "select * from MS_MONITOR_APP_TIME_CONF_TMP";

		final List<TimeConfTmpPo> tmpList = new ArrayList<TimeConfTmpPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					TimeConfTmpPo po = new TimeConfTmpPo();
					po.setTmpId(rs.getInt("tmp_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setAnalyseFuture(rs.getString("analyse_future"));
					po.setAnalyseFrequency(rs.getInt("ANALYSE_FREQUENCY"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					po.setTailType(rs.getString("tailType"));
					po.setAnalyseType(rs.getInt("analyse_type"));
					po.setObtainType(rs.getInt("obtain_type"));
					tmpList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppTimeConf出错", e);
		}
		return tmpList;
	}

	/**
	 * 根据tmpId获得对应的tmpPo
	 * 
	 * @return
	 */
	public TimeConfTmpPo findTimeConfTmpById(int tmpId) {
		String sql = "select * from MS_MONITOR_APP_TIME_CONF_TMP where tmp_id=?";

		final TimeConfTmpPo po = new TimeConfTmpPo();

		try {
			this.query(sql, new Object[] { tmpId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setTmpId(rs.getInt("tmp_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setAnalyseFrequency(rs.getInt("ANALYSE_FREQUENCY"));
					po.setAnalyseFuture(rs.getString("analyse_future"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					po.setTailType(rs.getString("tailType"));
					po.setAnalyseType(rs.getInt("analyse_type"));
					po.setAnalyseDesc(rs.getString("analyse_desc"));
					po.setObtainType(rs.getInt("obtain_type"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppTimeConf出错", e);
		}
		return po;
	}

	/**
	 * 根据timeConfPo更新
	 * 
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConfTmp(TimeConfTmpPo tmpPo) {
		String sql = "update MS_MONITOR_APP_TIME_CONF_TMP set analyse_class=?,FILE_PATH=?,ANALYSE_FREQUENCY=?,SPLIT_CHAR=?, ALIAS_LOG_NAME=? ,tailType=?,analyse_type=?,analyse_desc=?,analyse_future=?,obtain_type=? where tmp_id=? ";
		try {
			this.execute(sql, new Object[] { tmpPo.getClassName(), tmpPo.getFilePath(), tmpPo.getAnalyseFrequency(), tmpPo.getSplitChar(),
					tmpPo.getAliasLogName(), tmpPo.getTailType(), tmpPo.getAnalyseType(), tmpPo.getAnalyseDesc(), tmpPo.getAnalyseFuture(),tmpPo.getObtainType(), tmpPo.getTmpId() });
		} catch (SQLException e) {
			logger.error("updateTimeConf出错", e);
			return false;
		}
		return true;
	}

}
