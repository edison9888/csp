package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.ServerInfoPo;


/**
 * 服务器的DAO
 * @author wuhaiqian.pt
 *
 */
public class ServerInfoDao  extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(ServerInfoDao.class);
	
	/**
	 * 添加addServerInfoData
	 * @param timeConf
	 */
	public boolean addServerInfoData(ServerInfoPo serverInfoPo) {
		try {
			
			String sql = "insert into MS_SERVER_INFO "
					+ "(SERVER_ID, SERVER_NAME, SERVER_IP, SERVER_DESC) values(?,?,?,?)";
			this.execute(sql, new Object[] {serverInfoPo.getServerId(),serverInfoPo.getServerName(),serverInfoPo.getServerIp(),serverInfoPo.getServerDesc()});
		} catch (Exception e) {
			logger.error("addServerInfoData 出错,", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除ServerInfoPo
	 * @param appId
	 */
	public boolean deleteHostData(int serverId) {
		String sql = "delete from MS_SERVER_INFO where SERVER_ID=?";
		try {
			this.execute(sql, new Object[] { serverId });
		} catch (SQLException e) {
			logger.error("ServerInfoPo: ", e);
			return false;
		}
		
		return true;
	}

	/**
	 * 删除ServerInfoPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(ServerInfoPo serverInfoPo) {
		String sql = "delete from MS_SERVER_INFO where SERVER_ID=?";
		try {
			this.execute(sql, new Object[] { serverInfoPo.getServerId() });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取全部ServerInfoPo
	 * 
	 * @return
	 */
	public List<ServerInfoPo> findAllServerInfo() {
		String sql = "select * from MS_SERVER_INFO";

		final List<ServerInfoPo> serverInfoPoList = new ArrayList<ServerInfoPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					ServerInfoPo po = new ServerInfoPo();
					po.setServerId(rs.getInt("server_id"));
					po.setServerName(rs.getString("server_name"));
					po.setServerIp(rs.getString("server_ip"));
					po.setServerDesc(rs.getString("server_desc"));
					serverInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllServerInfo出错", e);
		}
		return serverInfoPoList;
	}
	
	/**
	 * 根据id获取ServerInfoPo
	 * 
	 * @return
	 */
	public ServerInfoPo findAllServerInfoById(int id) {
		String sql = "select * from MS_SERVER_INFO where server_id = ?";

		final ServerInfoPo po = new ServerInfoPo();

		try {
			this.query(sql, new Object[]{id},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					
					po.setServerId(rs.getInt("server_id"));
					po.setServerName(rs.getString("server_name"));
					po.setServerIp(rs.getString("server_ip"));
					po.setServerDesc(rs.getString("server_desc"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllServerInfo出错", e);
		}
		return po;
	}
	
	/**
	 * 根据ServerInfoPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateServerInfo(ServerInfoPo serverInfoPo){
		String sql = "update MS_SERVER_INFO set server_name=?,server_ip=?,server_desc=? where server_id=? ";
		try {
			this.execute(sql, new Object[]{serverInfoPo.getServerName(), serverInfoPo.getServerIp(), serverInfoPo.getServerDesc(), serverInfoPo.getServerId()});
		} catch (SQLException e) {
			logger.error("updateServerInfo出错", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 根据应用id 和类型获取服务器对象
	 * @param appid
	 * @param type
	 * @return
	 */
	public ServerInfoPo findServerInfoByAppIdAndType(int appid,String type) {
		String sql = "select s.* from ms_server_app_rel r, ms_server_info s where r.app_id=? and r.app_type=? and s.server_id=r.server_id";

		final ServerInfoPo po = new ServerInfoPo();

		try {
			this.query(sql, new Object[]{appid,type},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					
					po.setServerId(rs.getInt("server_id"));
					po.setServerName(rs.getString("server_name"));
					po.setServerIp(rs.getString("server_ip"));
					po.setServerDesc(rs.getString("server_desc"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllServerInfo出错", e);
		}
		return po;
	}
	
}
