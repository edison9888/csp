package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.FlowControlInterfacePo;

public class FlowControlInterfaceDao extends MysqlRouteBase {

	private static final Logger logger =  Logger.getLogger(FlowControlInterfaceDao.class);
	
	public FlowControlInterfaceDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<FlowControlInterfacePo> findAll(String strategy) {
		String sql = "select * from sentinel_flow_interface where strategy=? order by app_name, interface_info";
		
		final List<FlowControlInterfacePo> list = new LinkedList<FlowControlInterfacePo>();
		
		try {
			this.query(sql,  new Object[] { strategy }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					FlowControlInterfacePo po = new FlowControlInterfacePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setRefAppName(rs.getString("ref_app"));
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
	
	public List<FlowControlInterfacePo> find(final String appName, final boolean includeDisable, String strategy) {
		String sql = "select * from sentinel_flow_interface where app_name=? and strategy=? order by interface_info";
		
		final List<FlowControlInterfacePo> list = new LinkedList<FlowControlInterfacePo>();
		
		try {
			this.query(sql, new Object[] { appName, strategy }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int state = rs.getInt("state");
					if (state == Constants.DISABLE && !includeDisable) {
						return;
					}
					
					FlowControlInterfacePo po = new FlowControlInterfacePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setRefAppName(rs.getString("ref_app"));
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
	
	public void add(FlowControlInterfacePo po){
		String sql = "INSERT INTO sentinel_flow_interface(id, app_name, interface_info, ref_app, " +
				"limit_flow, user, version, state, strategy) VALUES(?,?,?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getInterfaceInfo(), po.getRefAppName(),
					po.getLimitFlow(), po.getUser(), po.getVersion(), Constants.ENABLE, po.getStrategy() });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean remove(String id) {
		String sql = "delete from sentinel_flow_interface where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		String sql = "update sentinel_flow_interface set limit_flow=? where id=?";

		try {
			this.execute(sql, new Object[] { limitFlow, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean checkExist(String appName, String flowApp, String interfaceInfo, String strategy) {
		String sql = "select * from sentinel_flow_interface where app_name=? and ref_app=? and interface_info=? and strategy=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, flowApp, interfaceInfo, strategy }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("interfaceName:" + appName + " flowApp:" + flowApp + " inteface:" + interfaceInfo + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		String sql = "update sentinel_flow_interface set version=? where app_name=? and ref_app=?";

		try {
			this.execute(sql, new Object[] { version, appName, refApp });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
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
		String sql = "update sentinel_flow_interface set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateState(String id) {
		String sql = "update sentinel_flow_interface set state = (state + 1) mod 2 where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
