package com.taobao.csp.capacity.dao;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;


public class CapacityDao extends MysqlRouteBase {
	
	
	public CapacityDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	private static final Logger logger =  Logger.getLogger(CapacityDao.class);

	
	public List<CapacityAppPo> findCapacityAppList(){
		String sql = "select * from csp_capacity_app_datasource";
		
		final List<CapacityAppPo> list = new ArrayList<CapacityAppPo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityAppPo po = new CapacityAppPo();
					po.setAppId(rs.getInt("app_id"));
					po.setDatasourceName(rs.getString("datasoure_name"));
					po.setAppType(rs.getString("app_type"));
					po.setDataFeature(rs.getString("data_feature"));
					po.setGroupNames(rs.getString("group_names"));
					po.setGrowthRate(rs.getInt("growth_rate"));
					po.setItemName(rs.getString("item_name"));
					po.setDataName(rs.getString("data_name"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public CapacityAppPo getCapacityApp(int appId){
		String sql = "select * from csp_capacity_app_datasource where app_id=?";
		
		final CapacityAppPo po = new CapacityAppPo();
		
		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setDatasourceName(rs.getString("datasoure_name"));
					po.setAppType(rs.getString("app_type"));
					po.setDataFeature(rs.getString("data_feature"));
					po.setGroupNames(rs.getString("group_names"));
					po.setGrowthRate(rs.getInt("growth_rate"));
					po.setItemName(rs.getString("item_name"));
					po.setDataName(rs.getString("data_name"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId()>0?po:null;
	}
	
	
	public void insertToCapacityAppDataSource(CapacityAppPo po){
		String sql = "INSERT INTO csp_capacity_app_datasource(app_id, datasoure_name, app_type, data_feature, growth_rate, item_name, data_name) VALUES(?,?,?,?,?,?,?);";
		try {
			this.execute(sql, new Object[]{po.getAppId(), po.getDatasourceName(),po.getAppType(), po.getDataFeature(), po.getGrowthRate(), po.getItemName(), po.getDataName()});
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public boolean updateCapacityAppDataSource(CapacityAppPo po) {
		String sql = "update csp_capacity_app_datasource set datasoure_name=?,app_type=?,data_feature=?, growth_rate=?, item_name=?, data_name=? where app_id=?";

		try {
			this.execute(sql, new Object[] { po.getDatasourceName(), po.getAppType(), po.getDataFeature(), po.getGrowthRate(), po.getItemName(), po.getDataName(), po.getAppId() });
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	public boolean deleteCapacityAppDataSourceByAppId(int appId) {
		String sql = "delete from csp_capacity_app_datasource where app_id=?";

		try {
			this.execute(sql, new Object[] { appId });
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}	
	
}
