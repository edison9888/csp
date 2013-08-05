package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.CheckupDependConfig;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CspCheckupDependDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(CspCheckupDependDao.class);

	public CspCheckupDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	public void addCheckupDependConfig(CheckupDependConfig config) {
		String sql = "insert into CSP_CHECKUP_DEPEND_CONFIG(codeversion,codepath,ops_name,self_ip,target_app_type,target_ops_name,target_ips,start_prevent_intensity,run_prevent_intensity,start_delay_intensity,run_delay_intensity,collect_time,uuid,costtime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql,
					new Object[] {config.getCodeVersion(),config.getCodePath(), config.getOpsName(), config.getSelfIp(), config.getTargetAppType(),config.getTargetOpsName(),config.getTargetIps(),config.getStartPreventIntensity(),config.getRunPreventIntensity(),config.getStartDelayIntensity(),config.getRunDelayIntensity(),config.getCollect_Time(),config.getUuid(),config.getCosttime()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public void deleteCheckupDependConfig(CheckupDependConfig config) {
		String sql = "delete from CSP_CHECKUP_DEPEND_CONFIG where ops_name =? and target_ops_name=? and codeversion=?";
		try {
			this.execute(sql,new Object[] {config.getOpsName(),config.getTargetOpsName(),config.getCodeVersion()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	private void fillDependConfig(ResultSet rs, CheckupDependConfig config) throws SQLException{
		config.setCodeVersion(rs.getInt("codeversion"));
		config.setCodePath(rs.getString("codepath"));
		config.setOpsName(rs.getString("ops_name"));
		config.setSelfIp(rs.getString("self_ip"));
		config.setTargetAppType(rs.getString("target_app_type"));
		config.setTargetOpsName(rs.getString("target_ops_name"));
		config.setStartDelayIntensity(rs.getString("start_delay_intensity")==null?"δ֪":rs.getString("start_delay_intensity"));
		config.setStartPreventIntensity(rs.getString("start_prevent_intensity")==null?"δ֪":rs.getString("start_prevent_intensity"));
		config.setRunDelayIntensity(rs.getString("run_delay_intensity")==null?"δ֪":rs.getString("run_delay_intensity"));
		config.setRunPreventIntensity(rs.getString("run_prevent_intensity")==null?"δ֪":rs.getString("run_prevent_intensity"));
		config.setTargetIps(rs.getString("target_ips"));
		config.setCollect_Time(rs.getDate("collect_time"));
		config.setCosttime(rs.getLong("costtime"));
		config.setUuid(rs.getString("uuid"));
	}


	public CheckupDependConfig getCheckupDependConfig(String opsName, String targetOpsName, int codeversion) {

		String sql = "select * from CSP_CHECKUP_DEPEND_CONFIG where ops_name = ? and target_ops_name=? and codeversion = ?";

		final CheckupDependConfig config = new CheckupDependConfig();

		try {
			this.query(sql, new Object[] { opsName, targetOpsName,codeversion}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					fillDependConfig(rs,config);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return config.getOpsName() == null ? null : config;
	}

	public List<CheckupDependConfig> getCheckupDependConfig(String opsName) {

		String sql = "select * from CSP_CHECKUP_DEPEND_CONFIG where ops_name = ?";

		final List<CheckupDependConfig> list = new ArrayList<CheckupDependConfig>();

		try {
			this.query(sql, new Object[] {opsName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CheckupDependConfig config = new CheckupDependConfig();
					fillDependConfig(rs,config);
					list.add(config);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

	public List<CheckupDependConfig> getCheckupDependConfigByTargetOpsName(String targetOpsName) {

		String sql = "select * from CSP_CHECKUP_DEPEND_CONFIG where target_ops_name = ? and codeversion = ?";

		final List<CheckupDependConfig> list = new ArrayList<CheckupDependConfig>();

		try {
			this.query(sql, new Object[] { targetOpsName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CheckupDependConfig config = new CheckupDependConfig();
					fillDependConfig(rs,config);
					list.add(config);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public Map<String, List<Integer>> getAllCodeVersion() {
		String sql = "select ops_name,codeversion from CSP_CHECKUP_DEPEND_CONFIG group by ops_name,codeversion order by codeversion desc";
		final Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		try {
			this.query(sql,new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer codeVersion = rs.getInt("codeversion");
					if(codeVersion == null)
						return;
					List<Integer> codeList = map.get(rs.getString("ops_name"));
					if(codeList == null) {
						codeList = new ArrayList<Integer>();
						map.put(rs.getString("ops_name"), codeList);
					}
					codeList.add(codeVersion);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
}
