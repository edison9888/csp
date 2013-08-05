package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.FlowControlDependencyPo;

public class FlowControlDependencyDao extends MysqlRouteBase {

	private static final Logger logger =  Logger.getLogger(FlowControlDependencyDao.class);
	
	public FlowControlDependencyDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<FlowControlDependencyPo> findAll() {
		String sql = "select * from sentinel_flow_dependency order by app_name";
		
		final List<FlowControlDependencyPo> list = new LinkedList<FlowControlDependencyPo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					FlowControlDependencyPo po = new FlowControlDependencyPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefAppName(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setLimitFlow(rs.getInt("limit_flow"));
					po.setUser(rs.getString("user"));
					po.setVersion(rs.getString("version"));
					po.setState(rs.getInt("state"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<FlowControlDependencyPo> find(final String appName, final boolean includeDisable) {
		String sql = "select * from sentinel_flow_dependency where app_name=?";
		
		final List<FlowControlDependencyPo> list = new LinkedList<FlowControlDependencyPo>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int state = rs.getInt("state");
					if (state == Constants.DISABLE && !includeDisable) {
						return;
					}
					
					FlowControlDependencyPo po = new FlowControlDependencyPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefAppName(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setLimitFlow(rs.getInt("limit_flow"));
					po.setUser(rs.getString("user"));
					po.setVersion(rs.getString("version"));
					po.setState(state);
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public void add(FlowControlDependencyPo po){
		String sql = "INSERT INTO sentinel_flow_dependency(id, app_name, ref_app, interface_info, limit_flow, user, version, state) VALUES(?,?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getRefAppName(),  po.getInterfaceInfo(), po.getLimitFlow(),
					po.getUser(), po.getVersion(), Constants.ENABLE });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public void add(String id, String appName, String controlApp, String interfaceInfo, String user, int limitFlow, String version) {
		String sql = "INSERT INTO sentinel_flow_dependency(id, app_name, ref_app, interface_info, limit_flow, user, version, state) VALUES(?,?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{ id, appName, controlApp, interfaceInfo, limitFlow, user, version, Constants.ENABLE });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean remove(String id) {
		String sql = "delete from sentinel_flow_dependency where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		String sql = "update sentinel_flow_dependency set limit_flow=? where id=?";

		try {
			this.execute(sql, new Object[] { limitFlow, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean checkExist(String appName, String controlApp, String interfaceInfo) {
		String sql = "select * from sentinel_flow_dependency where app_name=? and ref_app=? and interface_info=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, controlApp, interfaceInfo }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("appName:" + appName + " controlApp:" + controlApp + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean updateVersion(String id, String version) {
		String sql = "update sentinel_flow_interface set version=? where id=?";

		try {
			this.execute(sql, new Object[] { version, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersionByName(String appName, String version) {
		String sql = "update sentinel_flow_dependency set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateState(String id) {
		String sql = "update sentinel_flow_dependency set state = (state + 1) mod 2 where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
