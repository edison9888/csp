package com.taobao.monitor.common.db.impl.capacity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityLoadPo;

/***
 * µ¥»ú¸ººÉ
 * @author youji.zj
 * @version 2012-08-15
 *
 */
public class CapacityLoadDao extends MysqlRouteBase {
	
	public static Logger logger = Logger.getLogger(CapacityLoadDao.class);
	
	public CapacityLoadDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	public boolean addCapacityLoad(CapacityLoadPo po) {
		boolean success = true;

		String sql = "insert into csp_capacity_load (app_name,qps,data_type,data_source,collect_time)"
				+ "values(?,?,?,?,?)";

		try {
			this.execute(
					sql,
					new Object[] { po.getAppName(), po.getQps(), po.getDataType(), po.getDataSouce(), po.getCollectTime()
					});
		} catch (SQLException e) {
			logger.error("insert capacity load error", e);
			success = false;
		}
		
		return success;
	}
	
	public CapacityLoadPo findSingleCapacityLoadByAppNameDate(String appName, Date date) {
		final List<CapacityLoadPo> list = new ArrayList<CapacityLoadPo>();
		
		String sql = "select * from csp_capacity_load where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and app_name=?" +
				" and data_type='single'";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityLoadPo po = new CapacityLoadPo();
					po.setAppName(rs.getString("app_name"));
					po.setQps(rs.getDouble("qps"));
					po.setDataType(rs.getString("data_type"));
					po.setDataSouce(rs.getString("data_source"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.isEmpty() ? null : list.get(0);
	}
	
	public CapacityLoadPo findGroupCapacityLoadByAppNameDate(String appName, Date date) {
		final List<CapacityLoadPo> list = new ArrayList<CapacityLoadPo>();
		
		String sql = "select * from csp_capacity_load where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and app_name=?" +
				" and data_type='group'";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityLoadPo po = new CapacityLoadPo();
					po.setAppName(rs.getString("app_name"));
					po.setQps(rs.getDouble("qps"));
					po.setDataType(rs.getString("data_type"));
					po.setDataSouce(rs.getString("data_source"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.isEmpty() ? null : list.get(0);
	}

}
