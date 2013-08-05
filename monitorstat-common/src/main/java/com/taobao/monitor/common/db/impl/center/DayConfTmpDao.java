package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.DayConfTmpPo;

/**
 * 日报模板配置类DayConf的dao
 * @author wuhaiqian.pt
 *
 */
public class DayConfTmpDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(DayConfTmpDao.class);

	/**
	 * 添加addDayConfTmp
	 * @param DayConfTmpPo
	 */
	public boolean addDayConfTmp(DayConfTmpPo tmpPo) {
		try {
			
			String sql = "insert into MS_MONITOR_APP_DAY_CONF_TMP "
					+ "(analyse_class, FILE_PATH, SPLIT_CHAR, ALIAS_LOG_NAME) values(?,?,?,?)";
			this.execute(sql, new Object[] {tmpPo.getClassName(), tmpPo.getFilePath(), tmpPo.getSplitChar(), tmpPo.getAliasLogName()});
		} catch (Exception e) {
			logger.error("addDayConfTmp 出错,", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 删除deleteDayConfTmp
	 * @param tmpId
	 */
	public boolean deleteDayConfTmp(int tmpId) {
		String sql = "delete from MS_MONITOR_APP_DAY_CONF_TMP where TMP_ID=?";
		try {
			this.execute(sql, new Object[] { tmpId });
		} catch (SQLException e) {
			logger.error("deleteDayConfTmp: ", e);
			
			return false;
		}
		
		return true;
	}

	/**
	 * 删除deleteDayConfTmp
	 * @param dayConf
	 */
	public boolean deleteDayConfTmp(DayConfTmpPo tmpPo) {
		String sql = "delete from MS_MONITOR_APP_DAY_CONF_TMP where TMP_ID=? and analyse_class=?, file_path=?, split_char=?,alias_log_name=? ";
		try {
			this.execute(sql, new Object[] { tmpPo.getTmpId(), tmpPo.getClassName(), tmpPo.getFilePath(), tmpPo.getSplitChar(), tmpPo.getAliasLogName() });
		} catch (SQLException e) {
			logger.error("deleteDayConfTmp: ", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 根据tmpId获取DayConfTmpPo
	 * @return
	 */
	public DayConfTmpPo findDayConfTmpById(int tmpId) {
		String sql = "select * from MS_MONITOR_APP_DAY_CONF_TMP where tmp_id=? ";

		final DayConfTmpPo po = new DayConfTmpPo();
		try {
			this.query(sql, new Object[]{tmpId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setTmpId(rs.getInt("tmp_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppDayConf出错", e);
		}
		return po;
	}
	
	/**
	 * 获取全部DayConfTmpPo
	 * @return
	 */
	public List<DayConfTmpPo> findAllDayConfTmp() {
		String sql = "select * from MS_MONITOR_APP_DAY_CONF_TMP";

		final List<DayConfTmpPo> dayConfPoList = new ArrayList<DayConfTmpPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					DayConfTmpPo po = new DayConfTmpPo();
					po.setTmpId(rs.getInt("tmp_id"));
					po.setClassName(rs.getString("analyse_class"));
					po.setFilePath(rs.getString("FILE_PATH"));
					po.setSplitChar(rs.getString("SPLIT_CHAR"));
					po.setAliasLogName(rs.getString("ALIAS_LOG_NAME"));
					dayConfPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppDayConf出错", e);
		}
		return dayConfPoList;
	}
	
	/**
	 * 根据DayConfTmpPo更新
	 * @param tmpPo
	 * @return
	 */
	public boolean updateDayConfTmp(DayConfTmpPo tmpPo){
		String sql = "update MS_MONITOR_APP_DAY_CONF_TMP set analyse_class=?, FILE_PATH=?,SPLIT_CHAR=?, ALIAS_LOG_NAME=? where tmp_id=?";
		try {
			this.execute(sql, new Object[]{tmpPo.getClassName(), tmpPo.getFilePath(), tmpPo.getSplitChar(), tmpPo.getAliasLogName(),tmpPo.getTmpId()});
		} catch (SQLException e) {
			logger.error("updateDayConfTmp", e);
			return false;
		}
		return true;
	}
	
}
