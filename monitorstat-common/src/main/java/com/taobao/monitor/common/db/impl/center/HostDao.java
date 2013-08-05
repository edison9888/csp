package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;

/**
 * 主机的DAO
 * 
 * @author wuhaiqian.pt
 * 
 */
public class HostDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(HostDao.class);

	/**
	 * 添加addHostData
	 * 
	 * @param hostPo
	 */
	public boolean addHostData(HostPo hostPo) {
		try {

			String sql = "insert into MS_MONITOR_HOST "
					+ "(APP_ID,	HOST_IP, HOST_SITE,HOST_NAME, SAVE_DATA,user_name,user_psw) values(?,?,?,?,?,?,?)";
			this.execute(sql, new Object[] { hostPo.getAppId(),
					hostPo.getHostIp(), hostPo.getHostSite(),
					hostPo.getHostName(), hostPo.getSavedata(),
					hostPo.getUserName(), hostPo.getUserPassword() });
		} catch (Exception e) {
			logger.error("addHostData 出错,", e);

			return false;
		}

		return true;
	}

	/**
	 * 添加hostList
	 * 
	 * @param hostList
	 */
	public boolean addHostList(List<HostPo> hostList) {
		try {
			for (HostPo hostPo : hostList) {

				if (!isExistHostByHostIpAndAppId(hostPo.getAppId(), hostPo
						.getHostIp())) {
					String sql = "insert into MS_MONITOR_HOST "
							+ "(APP_ID,	HOST_IP, HOST_SITE,HOST_NAME, SAVE_DATA,user_name,user_psw) values(?,?,?,?,?,?,?)";
					this.execute(sql, new Object[] { hostPo.getAppId(),
							hostPo.getHostIp(), hostPo.getHostSite(),
							hostPo.getHostName(), hostPo.getSavedata(),
							hostPo.getUserName(), hostPo.getUserPassword() });
				}
			}
		} catch (Exception e) {
			logger.error("addHostList 出错,", e);

			return false;
		}

		return true;
	}

	/**
	 *@author wb-lixing 2012-3-8 下午06:56:34
	 *@param hostList
	 *@return
	 */
	public boolean addHostListSync(List<HostPo> hostList) {
		try {
			String sql = "insert into csp_app_host_info_sync"
					+ "(ops_name, NODEGROUP, dns_ip, nodename, site,   rack, hdrs_chassis, state, model,  description,    vmparent, csp_version) values(?,?,?,?,?, ?,?,?,?,? ,?,?)";
			List<Object[]> params = new ArrayList<Object[]>();
			for (HostPo hostPo : hostList) {
				Object[] param = new Object[] { hostPo.getOpsName(),
						hostPo.getNodeGroup(), hostPo.getHostIp(),
						hostPo.getHostName(), hostPo.getHostSite(),
						hostPo.getRack(), hostPo.getHdrs_chassis(),
						hostPo.getState(), hostPo.getHostType(),
						hostPo.getDescription(), hostPo.getVmparent(),
						hostPo.getCpsVersion() };
				params.add(param);
			}
			this.executeBatch(sql, params);
		} catch (Exception e) {
			logger.error("addHostList 出错,", e);
			return false;
		}
		return true;
	}

	/**
	 * 删除HostPo
	 * 
	 * @param appId
	 */
	public boolean deleteHostData(int appId) {
		String sql = "delete from MS_MONITOR_HOST where APP_ID=?";
		try {
			this.execute(sql, new Object[] { appId });
		} catch (SQLException e) {
			logger.error("deleteHostData: ", e);

			return false;
		}

		return true;
	}

	/**
	 * 删除HostPo
	 * 
	 * @param hostId
	 */
	public boolean deleteHostbyHostId(int hostId) {
		String sql = "delete from MS_MONITOR_HOST where Host_ID=?";
		try {
			this.execute(sql, new Object[] { hostId });
		} catch (SQLException e) {
			logger.error("deleteHostData: ", e);

			return false;
		}

		return true;
	}

	/**
	 * 删除HostPo
	 * 
	 * @param hostIdList
	 */
	public boolean deleteHostList(String[] hostIdList) {

		String sql = "delete from MS_MONITOR_HOST where Host_ID=?";

		try {

			for (String hostId : hostIdList) {
				this.execute(sql, new Object[] { Integer.parseInt(hostId) });
			}
		} catch (SQLException e) {
			logger.error("deleteHostList: ", e);

			return false;
		}

		return true;
	}

	/**
	 *@author wb-lixing 2012-3-8 下午07:31:18
	 */
	public boolean deleteHostListSyncByOpsName(String opsName) {
		String sql = "delete from csp_app_host_info_sync where ops_name=?";
		try {
			this.execute(sql, new Object[] { opsName });
		} catch (SQLException e) {
			logger.error("deleteHostList: ", e);

			return false;
		}

		return true;
	}

	/**
	 *@author wb-lixing 2012-3-8 下午07:31:18
	 */
	public boolean deleteHostListSync(String[] ipList) {

		String sql = "delete from csp_app_host_info_sync where dns_ip=?";

		try {
			List<Object[]> params = new ArrayList<Object[]>();

			for (String ip : ipList) {
				Object[] param = new Object[] { ip };
				params.add(param);
			}

			this.executeBatch(sql, params);
		} catch (SQLException e) {
			logger.error("deleteHostList: ", e);

			return false;
		}

		return true;
	}

	public boolean deleteHostData(HostPo hostPo) {
		String sql = "delete from MS_MONITOR_HOST where APP_ID=?";
		try {
			this.execute(sql, new Object[] { hostPo.getAppId() });
		} catch (SQLException e) {
			logger.error("deleteHostData: ", e);

			return false;
		}

		return true;
	}

	/**
	 * 根据hostIp查询host是否存在
	 * 
	 * @return
	 */
	public boolean isExistHostByHostIp(String hostIp) {
		String sql = "select * from MS_MONITOR_HOST where host_ip=?";

		final HostPo po = new HostPo();
		try {
			this.query(sql, new Object[] { hostIp }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
				}
			});

		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}

		if (null == po.getHostIp() || po.getHostIp().equals("")) {

			return false;
		}
		return true;
	}

	/**
	 * 根据hostid返回hostPo
	 * 
	 * @param hostId
	 * @return
	 */
	public HostPo findHostByHostId(int hostId) {
		String sql = "select * from MS_MONITOR_HOST where host_id=?";

		final HostPo po = new HostPo();
		try {
			this.query(sql, new Object[] { hostId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPassword(rs.getString("user_psw"));
				}
			});

		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}
		return po;
	}

	/**
	 * 返回持久表的个数
	 * 
	 * @return
	 */
	public int sumOfSaveData(int appId) {
		String sql = "select * from MS_MONITOR_HOST where app_id=?";

		final List<HostPo> hostPoList = new ArrayList<HostPo>();
		int num = 0;
		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostPo po = new HostPo();
					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPassword(rs.getString("user_psw"));
					hostPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}

		for (HostPo p : hostPoList) {
			// saveData : 第一位 表示只保存 limit ，第二位表示保存data ,0为不保存，1为保存
			String saveData = p.getSavedata();
			char save = saveData.charAt(1);
			if (save == '1') {
				num++;
			}
		}
		return num;
	}

	/**
	 * 根据hostIp和AppId查询host是否存在
	 * 
	 * @return
	 */
	public boolean isExistHostByHostIpAndAppId(int appId, String hostIp) {
		String sql = "select * from MS_MONITOR_HOST where host_ip=? and app_id=?";

		final HostPo po = new HostPo();
		try {
			this.query(sql, new Object[] { hostIp, appId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
				}
			});

		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}

		if (null == po.getHostIp() || po.getHostIp().equals("")) {

			return false;
		}
		return true;
	}

	/**
	 * 获取全部HostPo
	 * 
	 * @return
	 */
	public List<HostPo> findAllHost() {
		String sql = "select * from MS_MONITOR_HOST";

		final List<HostPo> hostPoList = new ArrayList<HostPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostPo po = new HostPo();
					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPassword(rs.getString("user_psw"));
					hostPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}
		return hostPoList;
	}

	/**
	 * 根据appId获得全部相关的HostPo
	 * 
	 * @return
	 */
	public List<HostPo> findAllHostByAppId(int appId) {
		String sql = "select * from MS_MONITOR_HOST where app_id=?";

		final List<HostPo> hostPoList = new ArrayList<HostPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostPo po = new HostPo();
					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPassword(rs.getString("user_psw"));
					hostPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}
		return hostPoList;
	}

	/**
	 * 根据appId返回包含有对应包含HostList的AppInfoPo
	 * 
	 * @return
	 */
	public AppInfoPo findAppWithHostListByAppId(int appId) {

		String sql = "select a.*, h.* from MS_MONITOR_APP a, MS_MONITOR_HOST h where a.app_id = h.app_id and a.app_id =?";

		final List<HostPo> hostList = new ArrayList<HostPo>();
		final AppInfoPo appPo = new AppInfoPo();
		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostPo po = new HostPo();

					appPo.setAppId(rs.getInt("APP_ID"));
					appPo.setAppName(rs.getString("APP_NAME"));
					appPo.setFeature(rs.getString("feature"));
					appPo.setSortIndex(rs.getInt("SORT_INDEX"));
					appPo.setOpsName(rs.getString("OPS_NAME"));
					appPo.setGroupName(rs.getString("GROUP_NAME"));
					appPo.setDayDeploy(rs.getInt("day_deploy"));
					appPo.setTimeDeploy(rs.getInt("time_deploy"));
					appPo.setAppStatus(rs.getInt("app_status"));

					po.setAppId(rs.getInt("h.app_id"));
					po.setHostIp(rs.getString("h.host_ip"));
					po.setHostName(rs.getString("h.HOST_NAME"));
					po.setSavedata(rs.getString("h.SAVE_DATA"));
					po.setHostId(rs.getInt("h.host_id"));
					po.setHostSite(rs.getString("h.host_site"));
					po.setUserName(rs.getString("h.user_name"));
					po.setUserPassword(rs.getString("h.user_psw"));

					hostList.add(po);
				}
			});

			appPo.setHostList(hostList);
		} catch (Exception e) {
			logger.error("findHostById出错", e);
		}
		return appPo;
	}

	/**
	 * 获取全部HostPo
	 * 
	 * @return
	 */
	public List<HostPo> findAppAllHost(int appId) {
		String sql = "select * from MS_MONITOR_HOST where app_id = ?";

		final List<HostPo> hostPoList = new ArrayList<HostPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostPo po = new HostPo();
					po.setAppId(rs.getInt("app_id"));
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setHostSite(rs.getString("host_site"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPassword(rs.getString("user_psw"));
					hostPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}
		return hostPoList;
	}

	/**
	 * 根据HostPo更新
	 * 
	 * @param HostPo
	 * @return
	 */
	public boolean updateHostInfo(HostPo hostPo) {
		String sql = "update MS_MONITOR_HOST set app_id=?,host_ip=?,HOST_NAME=?,SAVE_DATA=?, host_site=? where host_id=? ";
		try {
			this.execute(sql, new Object[] { hostPo.getAppId(),
					hostPo.getHostIp(), hostPo.getHostName(),
					hostPo.getSavedata(), hostPo.getHostSite(),
					hostPo.getHostId() });
		} catch (SQLException e) {
			logger.error("updateHostInfo出错", e);
			return false;
		}
		return true;
	}

	/*****************************************************/

	public boolean addSyncPeSystemHostInfo(HostPo hostPo, int version) {
		String sql = "insert into CSP_APP_HOST_INFO_SYNC"
				+ "(ops_name,nodegroup,dns_ip,nodename,site,rack,hdrs_chassis,state,model,description,vmparent,manifest,csp_version) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		try {
			this.execute(sql, new Object[] { hostPo.getOpsName(),
					hostPo.getNodeGroup(), hostPo.getHostIp(),
					hostPo.getHostName(), hostPo.getHostSite().toUpperCase(),
					hostPo.getRack(), hostPo.getHdrs_chassis(),
					hostPo.getState(), hostPo.getHostType(),
					hostPo.getDescription(), hostPo.getVmparent(), hostPo.getManifest(),version });
		} catch (SQLException e) {
			logger.error("addSyncPeSystemHostInfo出错", e);
			return false;
		}
		return true;
	}

	public boolean deleteSyncPeSystemHostInfo(String appName, int version) {

		String sql = "delete from CSP_APP_HOST_INFO_SYNC where ops_name = ? and csp_version=?";

		try {
			this.execute(sql, new Object[] { appName, version });
		} catch (SQLException e) {
			logger.error("deleteSyncPeSystemHostInfo出错", e);
			return false;
		}
		return true;
	}

	public List<HostPo> findAllSyncHostInfos(int version) {

		final List<HostPo> list = new ArrayList<HostPo>();

		String sql = "select * from CSP_APP_HOST_INFO_SYNC where csp_version =? or csp_version = -99";

		try {
			this.query(sql, new Object[] { version }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HostPo hostPo = new HostPo();
					hostPo.setOpsName(rs.getString("ops_name"));
					hostPo.setNodeGroup(rs.getString("nodegroup"));
					hostPo.setHostIp(rs.getString("dns_ip"));
					hostPo.setHostName(rs.getString("nodename"));
					hostPo.setHostSite(rs.getString("site"));
					hostPo.setRack(rs.getString("rack"));
					hostPo.setHdrs_chassis(rs.getString("hdrs_chassis"));
					hostPo.setState(rs.getString("state"));
					hostPo.setHostType(rs.getString("model"));
					hostPo.setDescription(rs.getString("description"));
					hostPo.setVmparent(rs.getString("vmparent"));
					hostPo.setManifest(rs.getString("manifest"));
					hostPo.setCpsVersion(rs.getInt("csp_version"));
					list.add(hostPo);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}

		return list;
	}

	/**
	 *@author wb-lixing 2012-3-8 下午07:27:19
	 */
	public List<HostPo> findAllSyncHostInfos(String opsName) {

		final List<HostPo> list = new ArrayList<HostPo>();

		String sql = "select * from CSP_APP_HOST_INFO_SYNC where ops_name=?";

		try {
			this.query(sql, new Object[] { opsName }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HostPo hostPo = new HostPo();
					hostPo.setOpsName(rs.getString("ops_name"));
					hostPo.setNodeGroup(rs.getString("nodegroup"));
					hostPo.setHostIp(rs.getString("dns_ip"));
					hostPo.setHostName(rs.getString("nodename"));
					hostPo.setHostSite(rs.getString("site"));
					hostPo.setRack(rs.getString("rack"));
					hostPo.setHdrs_chassis(rs.getString("hdrs_chassis"));
					hostPo.setState(rs.getString("state"));
					hostPo.setHostType(rs.getString("model"));
					hostPo.setDescription(rs.getString("description"));
					hostPo.setVmparent(rs.getString("vmparent"));
					hostPo.setManifest(rs.getString("manifest"));
					hostPo.setCpsVersion(rs.getInt("csp_version"));
					list.add(hostPo);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}

		return list;
	}

	public List<HostPo> findAllSyncHostInfos(String opsName, int version) {

		final List<HostPo> list = new ArrayList<HostPo>();

		String sql = "select * from CSP_APP_HOST_INFO_SYNC where ops_name=? and csp_version=?";

		try {
			this.query(sql, new Object[] { opsName, version },
					new SqlCallBack() {
						@Override
						public void readerRows(ResultSet rs) throws Exception {
							HostPo hostPo = new HostPo();
							hostPo.setOpsName(rs.getString("ops_name"));
							hostPo.setNodeGroup(rs.getString("nodegroup"));
							hostPo.setHostIp(rs.getString("dns_ip"));
							hostPo.setHostName(rs.getString("nodename"));
							hostPo.setHostSite(rs.getString("site"));
							hostPo.setRack(rs.getString("rack"));
							hostPo
									.setHdrs_chassis(rs
											.getString("hdrs_chassis"));
							hostPo.setState(rs.getString("state"));
							hostPo.setHostType(rs.getString("model"));
							hostPo.setDescription(rs.getString("description"));
							hostPo.setVmparent(rs.getString("vmparent"));
							hostPo.setManifest(rs.getString("manifest"));
							hostPo.setCpsVersion(rs.getInt("csp_version"));
							list.add(hostPo);
						}
					});
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}

		return list;
	}

	public int getSyncVersion() {

		String sql = "select sync_version from csp_sync_flag_summary where sync_name=?";

		try {
			return this.getIntValue(sql, new String[] { "CSP-SYNC-OPS-FLAG" });
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}
		return -1;
	}

	public int isSync() {

		String sql = "select sync_stat from csp_sync_flag_summary where sync_name=?";

		try {
			return this.getIntValue(sql, new String[] { "CSP-SYNC-OPS-FLAG" });
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}
		return -1;
	}

	public boolean addSync(int flag) {

		String sql = "update csp_sync_flag_summary set sync_stat =? where sync_name=? ";

		try {
			this.execute(sql, new Object[] { flag, "CSP-SYNC-OPS-FLAG" });
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}
		return false;
	}

	public boolean updateSyncVersion(int version) {

		String sql = "update csp_sync_flag_summary set sync_version=?, update_time = now() where sync_name=? ";

		try {
			this.execute(sql, new Object[] { version, "CSP-SYNC-OPS-FLAG" });
			return true;
		} catch (Exception e) {
			logger.error("findAllHostInfos出错", e);
		}
		return false;
	}

	public boolean deleteSyncOldVersion(int version) {

		String sql = "delete from CSP_APP_HOST_INFO_SYNC where csp_version=?";

		try {
			this.execute(sql, new Object[] {version });
		} catch (SQLException e) {
			logger.error("deleteSyncPeSystemHostInfo出错", e);
			return false;
		}
		return true;
	}

}
