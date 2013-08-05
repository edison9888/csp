package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.vo.HostConfigPo;
import com.taobao.monitor.web.core.po.LoadRunHost;

public class LoadRunHostConfigDao extends MysqlRouteBase {
	public LoadRunHostConfigDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}

	private static final Logger logger = Logger.getLogger(LoadRunHostConfigDao.class);

	public HostConfigPo getHostConfig(int appId) {
		String sql = "SELECT * FROM ms_monitor_loadrun_hostconfig WHERE app_id = ?";

		final HostConfigPo po = new HostConfigPo();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setOsVersion(rs.getString("os_version"));
					po.setPlatform(rs.getString("platform"));
					po.setDomainIp(rs.getString("domain_ip"));
					po.setCpu(rs.getString("cpu"));
					po.setMemory(rs.getString("memory"));
					po.setJavaVersion(rs.getString("java_version"));
					po.setJbossVersion(rs.getString("jboss_version"));
					po.setApacheVersion(rs.getString("apache_version"));
					po.setDiskHome(rs.getString("disk_home"));
					po.setUptime(rs.getString("uptime"));
					po.setJvmParameters(rs.getString("jvm_parameters"));
					po.setUpdateTime(rs.getDate("update_time"));
				}
			});
		} catch (Exception e) {
			logger.error("getHostConfig 出错", e);
		}
		return po;
	}

	public boolean updateHostConfig(HostConfigPo po) {
		String sql = "UPDATE ms_monitor_loadrun_hostconfig SET os_version = ? , platform = ? , domain_ip = ? , cpu = ? , memory = ? , "
				+ "java_version = ? , jboss_version = ? , apache_version = ? , disk_home = ? , uptime = ? , jvm_parameters = ? , update_time = ? WHERE app_id = ?";
		try {
			this.execute(
					sql,
					new Object[] { po.getOsVersion(), po.getPlatform(), po.getDomainIp(), po.getCpu(), po.getMemory(),
							po.getJavaVersion(), po.getJbossVersion(), po.getApacheVersion(), po.getDiskHome(),
							po.getUptime(), po.getJvmParameters(), po.getUpdateTime(), po.getAppId() });
		} catch (SQLException e) {
			logger.error("updateHostConfig 出错", e);
			return false;
		}
		return true;
	}

	public boolean insertHostConfig(HostConfigPo po) {
		String sql = "INSERT INTO ms_monitor_loadrun_hostconfig (app_id, os_version, platform, domain_ip, cpu, memory, java_version, "
				+ "jboss_version, apache_version, disk_home, uptime, jvm_parameters, update_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(
					sql,
					new Object[] { po.getAppId(), po.getOsVersion(), po.getPlatform(), po.getDomainIp(), po.getCpu(),
							po.getMemory(), po.getJavaVersion(), po.getJbossVersion(), po.getApacheVersion(),
							po.getDiskHome(), po.getUptime(), po.getJvmParameters(), po.getUpdateTime() });
		} catch (Exception e) {
			logger.error("addAppInfoData 出错", e);
			return false;
		}
		return true;
	}

	/**
	 * 取得所有压测应用机器
	 * 
	 * @return
	 */
	public List<LoadRunHost> findAllLoadRunHost() {
		String sql = "select * from ms_monitor_loadrun_host ";

		final List<LoadRunHost> list = new ArrayList<LoadRunHost>();

		try {
			this.query(sql, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					LoadRunHost loadRunHost = new LoadRunHost();
					loadRunHost.setAppId(rs.getInt("app_id"));
					loadRunHost.setUserName(rs.getString("user_name"));
					loadRunHost.setPassword(rs.getString("user_password"));
					list.add(loadRunHost);
				}
			});
		} catch (Exception e) {
			logger.error("findAllLoadRunHost", e);
		}
		return list;

	}

	public LoadRunHost findLoadRunHostByAppId(int appId) {
		String sql = "select * from ms_monitor_loadrun_host where app_id=? ";
		final LoadRunHost loadRunHost = new LoadRunHost();
		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					loadRunHost.setAppId(rs.getInt("app_id"));
					loadRunHost.setHostIp(rs.getString("host_ip"));
					loadRunHost.setUserName(rs.getString("user_name"));
					loadRunHost.setPassword(rs.getString("user_password"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return loadRunHost;

	}
}
