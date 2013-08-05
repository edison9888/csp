package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.HostTmpPo;

/**
 * 主机的DAO
 * @author wuhaiqian.pt
 *
 */
public class HostTmpDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(HostTmpDao.class);

	/**
	 * 添加HostTmpPo
	 * @param hostPo
	 */
	public boolean addHostTmp(HostTmpPo tmpPo) {
		try {
			
			String sql = "insert into MS_MONITOR_HOST_TMP "
					+ "(HOST_IP, HOST_NAME, SAVE_DATA, host_status) values(?,?,?,?)";
			this.execute(sql, new Object[] {tmpPo.getHostIp(), tmpPo.getHostName(), tmpPo.getSavedata(), tmpPo.getStatus()});
		} catch (Exception e) {
			logger.error("addHostData 出错,", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除HostPo
	 * @param appId
	 */
	public boolean deleteHostTmp(int hostId) {
		String sql = "delete from MS_MONITOR_HOST_TMP where HOST_ID=?";
		try {
			this.execute(sql, new Object[] { hostId });
		} catch (SQLException e) {
			logger.error("deleteHostTmp: ", e);
			
			return false;
		}
		return true;
	}

	/**
	 * 删除HostPo
	 * @param hostPo
	 */
	public boolean deleteHostTmp(HostTmpPo tmpPo) {
		String sql = "delete from MS_MONITOR_HOST_TMP where HOST_ID=?";
		try {
			this.execute(sql, new Object[] { tmpPo.getHostId() });
		} catch (SQLException e) {
			logger.error("deleteHostData: ", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取全部HostTmpPo
	 * 
	 * @return
	 */
	public List<HostTmpPo> findAllHostTmp() {
		String sql = "select * from MS_MONITOR_HOST_TMP";

		final List<HostTmpPo> hostPoList = new ArrayList<HostTmpPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					HostTmpPo po = new HostTmpPo();
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setStatus(rs.getInt("host_status"));
					hostPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllHost出错", e);
		}
		return hostPoList;
	}
	
	/**
	 * 根据appId获取全部HostPo
	 * 
	 * @return
	 */
	public HostTmpPo findHostTmpById(int hostId) {
		String sql = "select * from MS_MONITOR_HOST_TMP where host_id = ?";

		final HostTmpPo po = new HostTmpPo();

		try {
			this.query(sql, new Object[]{hostId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
	
					po.setHostIp(rs.getString("host_ip"));
					po.setHostName(rs.getString("HOST_NAME"));
					po.setSavedata(rs.getString("SAVE_DATA"));
					po.setHostId(rs.getInt("host_id"));
					po.setStatus(rs.getInt("host_status"));
				}
			});
		} catch (Exception e) {
			logger.error("findHostById出错", e);
		}
		return po;
	}
	
	/**
	 * 根据HostPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateHostTmpInfo(HostTmpPo tmpPo){
		String sql = "update MS_MONITOR_HOST_TMP set host_ip=?,HOST_NAME=?,SAVE_DATA=?,host_status=? where host_id=? ";
		try {
			this.execute(sql, new Object[]{tmpPo.getHostIp(), tmpPo.getHostName(), tmpPo.getSavedata(),tmpPo.getStatus(), tmpPo.getHostId()});
		} catch (SQLException e) {
			logger.error("updateHostInfo出错", e);
			return false;
		}
		return true;
	}
	
}
