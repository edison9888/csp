package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ServerAppRelPo;
import com.taobao.monitor.common.po.ServerInfoPo;
/**
 * Server和App关联实体的dao
 * @author wuhaiqian.pt
 *
 */
public class ServerAppRelDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(ServerAppRelDao.class);

	/**
	 * 添加rel
	 * @param ServerAppRelPo
	 */
	public boolean addRel(ServerAppRelPo po) {
		try {
			
			String sql = "insert into ms_server_app_rel "
				+ "(server_id, app_id, app_type) values(?,?,?)";
			this.execute(sql, new Object[] {po.getServerId(), po.getAppId(), po.getAppType()});
		} catch (Exception e) {
			logger.error("addRel 出错,", e);
			return false;
		}
		
		return true;
	}
	/**
	 * 添加server-app-rel的list
	 * @param List<ServerAppRelPo>
	 */
	public boolean addRel(List<ServerAppRelPo> list) {
		
	
		try {
			for(ServerAppRelPo po : list) {
				
				String sql = "insert into ms_server_app_rel "
					+ "(server_id, app_id, app_type) values(?,?,?)";			
				this.execute(sql, new Object[] {po.getServerId(), po.getAppId(), po.getAppType()});
			}
		} catch (Exception e) {
			logger.error("addRel 出错,", e);
			return false;
		}
		
		return true;
	}

	/**
	 * 删除rel
	 * @param ServerAppRelPo
	 */
	public boolean deleteRel(ServerAppRelPo po) {
		String sql = "delete from ms_server_app_rel where server_id=? and app_id=? and app_type=? ";
		try {
			this.execute(sql, new Object[] {po.getServerId(), po.getAppId(), po.getAppType()});
		} catch (SQLException e) {
			logger.error("deleteRel: ", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 查找rel
	 * @param ServerAppRelPo
	 */
	public ServerAppRelPo findRel(ServerAppRelPo relPo) {
		String sql = "select * from ms_server_app_rel where server_id= ? and app_id=? and app_type = ? ";
		
		final ServerAppRelPo po = new ServerAppRelPo();
		try {
			this.query(sql, new Object[]{relPo.getServerId(),relPo.getAppId(), relPo.getAppType()},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

				
					po.setAppId(rs.getInt("app_id"));
					po.setAppType(rs.getString("app_type"));
					po.setServerId(rs.getInt("server_id"));
				}
			});
		} catch (Exception e) {
			logger.error("findRel: ", e);
		}
		
		return po;
	}
	
	/**
	 * app_id,和app_type决定rel的唯一性，故根据app_id,app_type来判断是否已经存在rel
	 * @param DatabaseAppRelPo
	 */
	public boolean isExistedRel(ServerAppRelPo relPo) {
		String sql = "select * from ms_server_app_rel where app_id=? and app_type = ? ";
		
		final ServerAppRelPo po = new ServerAppRelPo();
		try {
			this.query(sql, new Object[]{relPo.getAppId(), relPo.getAppType()},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setAppType(rs.getString("app_type"));
				}
			});
		} catch (Exception e) {
			logger.error("findRel: ", e);
		}
		
		if(po.getAppId() != 0 && po.getAppType() != null) {
			
			return true;
		} else {
			
			return false;
		}
	}
	
	/**
	 * 更新rel
	 * @param ServerAppRelPo
	 * @return
	 */
	public boolean updateRel(ServerAppRelPo relPo){
		String sql = "update ms_server_app_rel set server_id= ?,app_type = ? where app_id=?";
		try {
			this.execute(sql, new Object[]{relPo.getServerId(), relPo.getAppType(), relPo.getAppId()});
		} catch (SQLException e) {
			logger.error("updateRel出错：", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 根据appId查询所有相关的AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findServerRel(int appId) {

		String sql = "SELECT a.*, " +
		"s.server_id, s.server_name, s.server_ip, s.server_desc, r.app_type FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " + 
		"WHERE a.app_id = r.app_id and s.server_id = r.server_id and a.app_id = ?";

		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{appId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerId: ", e);
		}
		return appInfoPoList;
	}
	
	/**
	 * 根据serverId查询所有相关的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerId(int serverId) {

		String sql = "SELECT a.*, " +
		"s.server_id, s.server_name, s.server_ip, s.server_desc, r.app_type FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " + 
		"WHERE s.server_id=r.server_id and a.app_id = r.app_id and r.server_id = ?";

		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{serverId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerId: ", e);
		}
		return appInfoPoList;
	}
	
	
	
	/**
	 * 根据serverId查询所有相关的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerId(int serverId,String appType) {

		String sql = "SELECT a.*, " +
		"s.server_id, s.server_name, s.server_ip, s.server_desc, r.app_type FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " + 
		"WHERE s.server_id=r.server_id and a.app_id = r.app_id and r.server_id = ? and r.app_type=?";

		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{serverId,appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerId: ", e);
		}
		return appInfoPoList;
	}
	
	
	/**
	 * 根据appType查找所有还没有关联服务器的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppWithoutRel(String appType) {
		
		final String type = appType;
		String sql = "SELECT a.* " +
		" FROM ms_monitor_app a " + 
		"WHERE a.app_id NOT IN ( " +
		"select r.app_id from ms_server_app_rel r where app_type = ? )";
		
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql,new Object[]{appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(type);
					//po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppWithoutRel: ", e);
		}
		return appInfoPoList;
	}
	
	/**
	 * 根据serverName查询所有相关的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerName(String serverName) {
	
		String sql = "SELECT a.*, r.app_type," +
				"s.server_id, s.server_name, s.server_ip, s.server_desc " +
				"FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " +
				" WHERE a.app_id = r.app_id and r.server_id = s.server_id and s.server_name = ?";
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{serverName},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerName出错", e);
		}
		return appInfoPoList;
	}

	/**
	 * 根据serverName和应用的类型查询所有相关的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerNameAndAppType(String serverName, String appType) {
	
		String sql = "SELECT a.*, r.app_type," +
				"s.server_id, s.server_name, s.server_ip, s.server_desc " +
				"FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " +
				" WHERE a.app_id = r.app_id and r.server_id = s.server_id and s.server_name = ? and r.app_type = ?";
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{serverName,appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerName出错", e);
		}
		return appInfoPoList;
	}
	/**
	 * 根据serverId和应用的类型查询所有相关的AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerIdAndAppType(int serverId, String appType) {
		
		String sql = "SELECT a.*, r.app_type," +
		"s.server_id, s.server_name, s.server_ip, s.server_desc " +
		"FROM ms_monitor_app a, ms_server_app_rel r, ms_server_info s " +
		" WHERE a.app_id = r.app_id and r.server_id = s.server_id and s.server_id = ? and r.app_type = ?";
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql, new Object[]{serverId,appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setServerInfoPo(setServerInfo("s",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByServerName出错", e);
		}
		return appInfoPoList;
	}
	
	public AppInfoPo setAppInfoPo(String alias,ResultSet rs) throws Exception {
		
		AppInfoPo po = new AppInfoPo();
		po.setAppId(rs.getInt(alias + ".APP_ID"));
		po.setAppName(rs.getString(alias + ".APP_NAME"));
		po.setFeature(rs.getString(alias + ".feature"));
		po.setSortIndex(rs.getInt(alias + ".SORT_INDEX"));
		po.setOpsName(rs.getString(alias + ".OPS_NAME"));
		po.setGroupName(rs.getString(alias + ".GROUP_NAME"));
		po.setDayDeploy(rs.getInt(alias + ".day_deploy"));
		po.setTimeDeploy(rs.getInt(alias + ".time_deploy"));
		po.setAppStatus(rs.getInt(alias + ".app_status"));
		po.setOpsField(rs.getString(alias + ".ops_field"));
		po.setLoginName(rs.getString(alias + ".login_name"));
		po.setLoginPassword(rs.getString(alias + ".login_password"));
		int app_day_id = rs.getInt(alias + ".app_day_id");
		String app_day_featrue = rs.getString(alias + ".app_day_featrue");
		String app_type = rs.getString(alias + ".app_type");
				
		if(app_day_id != 0){
			po.setAppDayFeature(app_day_featrue);
			po.setDefineType(app_type);
			po.setAppDayId(app_day_id);
		}else{
			po.setAppDayFeature(app_day_featrue);
			po.setDefineType("app");
			po.setAppDayId(po.getAppId());
		}
		
		return po;
	}
	
	public ServerInfoPo setServerInfo(String alias,ResultSet rs) throws Exception {
		
		ServerInfoPo po = new ServerInfoPo();
		po.setServerId(rs.getInt(alias + ".server_id"));
		po.setServerName(rs.getString(alias + ".server_name"));
		po.setServerIp(rs.getString(alias + ".server_ip"));
		po.setServerDesc(rs.getString(alias + ".server_desc"));
		return po;
	}
	
}
