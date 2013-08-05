package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class SentinelDao extends MysqlRouteBase {
	private static final Logger logger =  Logger.getLogger(SentinelDao.class);
	
	public SentinelDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}
	
	public Set<String> getKeyListFromSentinel(String appName) {
		final Set<String> keySet = new HashSet<String>();
		String sql = "select interface_info from sentinel_white_interface where appName = ?";
		try {
			this.query(sql, new String[]{appName},  new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					keySet.add(rs.getString("interface_info"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		sql = "select interface_info from sentinel_black_interface where appName = ?";
		try {
			this.query(sql, new String[]{appName},  new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					keySet.add(rs.getString("interface_info"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keySet;
	}
	
	public Set<String> getKeyListFromSentinel() {
		final Set<String> keySet = new HashSet<String>();
		String sql = "select interface_info from sentinel_white_interface";
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					keySet.add(rs.getString("interface_info"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		sql = "select interface_info from sentinel_black_interface";
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					keySet.add(rs.getString("interface_info"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keySet;
	}
}
