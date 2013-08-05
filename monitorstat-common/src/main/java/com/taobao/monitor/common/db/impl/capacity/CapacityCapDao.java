package com.taobao.monitor.common.db.impl.capacity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityCapPo;

/***
 * µ¥»ú¸ººÉ
 * @author youji.zj
 * @version 2012-08-15
 *
 */
public class CapacityCapDao extends MysqlRouteBase {
	
	public static Logger logger = Logger.getLogger(CapacityCapDao.class);
	
	public CapacityCapDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	public boolean addCapacityCap(CapacityCapPo po) {
		boolean success = true;

		String sql = "insert into csp_capacity_cap (app_name,single_capacity,set_time,set_user)"
				+ "values(?,?,?,?)";

		try {
			this.execute(
					sql,
					new Object[] { po.getAppName(), po.getSingleCapacity(), po.getTime(), po.getUser()
					});
		} catch (SQLException e) {
			logger.error("insert capacity cap error", e);
			success = false;
		}
		
		return success;
	}
	
	public boolean updateCapacityCap(CapacityCapPo po) {
		boolean success = true;

		String sql = "update csp_capacity_cap set single_capacity=?,set_time=?,set_user=? where app_name=?";

		try {
			this.execute(
					sql,
					new Object[] { po.getSingleCapacity(), po.getTime(), po.getUser(), po.getAppName()
					});
		} catch (SQLException e) {
			logger.error("update capacity cap error", e);
			success = false;
		}
		
		return success;
	}
	
	public boolean deleteCapacityCap(String appName) {
		boolean success = true;

		String sql = "delete from csp_capacity_cap where app_name = ?";

		try {
			this.execute(
					sql,
					new Object[] { appName
					});
		} catch (SQLException e) {
			logger.error("delete capacity cap error", e);
			success = false;
		}
		
		return success;
	}
	
	public CapacityCapPo findCapacityCapByAppName(String appName) {
		final List<CapacityCapPo> list = new ArrayList<CapacityCapPo>();
		
		String sql = "select * from csp_capacity_cap where app_name=?";
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCapPo po = new CapacityCapPo();
					po.setAppName(rs.getString("app_name"));
					po.setSingleCapacity(rs.getDouble("single_capacity"));
					po.setTime(rs.getTimestamp("set_time"));
					po.setUser(rs.getString("set_user"));
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.isEmpty() ? null : list.get(0);
	}
	
	public List<CapacityCapPo> findAllCapacityCap() {
		final List<CapacityCapPo> list = new ArrayList<CapacityCapPo>();
		
		String sql = "select * from csp_capacity_cap ";
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCapPo po = new CapacityCapPo();
					po.setAppName(rs.getString("app_name"));
					po.setSingleCapacity(rs.getDouble("single_capacity"));
					po.setTime(rs.getTimestamp("set_time"));
					po.setUser(rs.getString("set_user"));
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

}
