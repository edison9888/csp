package com.taobao.sentinel.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.ConfigVersionPo;

public class ConfigVersionDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(ConfigVersionDao.class);
	
	public ConfigVersionDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public Map<String, String> findApps() {
		String sql = "select app_name from sentinel_config_version";
		
		final Map<String, String> map = new HashMap<String, String>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					String version = rs.getString("version");
					
					map.put(appName, version);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return map;
	}
	
	public ConfigVersionPo findConfigVersion(final String appName) {
		String sql = "select * from sentinel_config_version where app_name=?";
		
		final List<ConfigVersionPo> list = new ArrayList<ConfigVersionPo>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ConfigVersionPo po = new ConfigVersionPo();
					po.setId(rs.getString("id"));
					po.setAppName(appName);
					po.setUser(rs.getString("user"));
					po.setVersion(rs.getString("version"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public ConfigVersionPo findLatestConfigVersion(final String appName) {
		String sql = "select * from sentinel_config_version where app_name=? order by version desc";
		
		final List<ConfigVersionPo> list = new ArrayList<ConfigVersionPo>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ConfigVersionPo po = new ConfigVersionPo();
					po.setId(rs.getString("id"));
					po.setAppName(appName);
					po.setUser(rs.getString("user"));
					po.setVersion(rs.getString("version"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<ConfigVersionPo> findRecentConfigVersion() {
		String sql = "select * from sentinel_config_version order by version desc";
		
		final List<ConfigVersionPo> list = new ArrayList<ConfigVersionPo>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -28);
		final BigInteger recentHold = new BigInteger(format.format(calendar.getTime()));
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ConfigVersionPo po = new ConfigVersionPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setUser(rs.getString("user"));
					po.setVersion(rs.getString("version"));
					po.setNumber(list.size() + 1);
					
					// just get the latest 4 weeks
					BigInteger record = new BigInteger(po.getVersion());
					if (recentHold.compareTo(record) == -1) {
						list.add(po);
					}
					
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}

		return list;
	}
	
	public void addConfigVersion(String id, String appName, String user, String version) {
		String sql = "INSERT INTO sentinel_config_version(id, app_name, user, version) VALUES(?,?,?,?)";
		try {

			this.execute(sql, new Object[]{ id, appName, user, version});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean updateConfigVersion(String appName, String version) {
		String sql = "update sentinel_config_version set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean removeConfgigVersion(String appName) {
		String sql = "delete from sentinel_config_version where app_name=?";

		try {
			this.execute(sql, new Object[] { appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
