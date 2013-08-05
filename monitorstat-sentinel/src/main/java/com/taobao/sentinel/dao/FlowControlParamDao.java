package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.FlowControlParamPo;

public class FlowControlParamDao extends MysqlRouteBase {

	private static final Logger logger =  Logger.getLogger(FlowControlParamDao.class);
	
	public FlowControlParamDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<FlowControlParamPo> findAll() {
		String sql = "select * from sentinel_flow_param order by app_name, param_info";
		
		final List<FlowControlParamPo> list = new LinkedList<FlowControlParamPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					FlowControlParamPo po = new FlowControlParamPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setParamInfo(rs.getString("param_info"));
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
	
	public List<FlowControlParamPo> find(final String appName, final boolean includeDisable) {
		String sql = "select * from sentinel_flow_param where app_name=? order by param_info";
		
		final List<FlowControlParamPo> list = new LinkedList<FlowControlParamPo>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int state = rs.getInt("state");
					if (state == Constants.DISABLE && !includeDisable) {
						return;
					}
					
					FlowControlParamPo po = new FlowControlParamPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setParamInfo(rs.getString("param_info"));
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
	
	public void add(FlowControlParamPo po){
		String sql = "INSERT INTO sentinel_flow_param(id, app_name, param_info, ref_app, " +
				"limit_flow, user, version, state) VALUES(?,?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getParamInfo(), po.getRefAppName(),
					po.getLimitFlow(), po.getUser(), po.getVersion(), Constants.ENABLE });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean remove(String id) {
		String sql = "delete from sentinel_flow_param where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		String sql = "update sentinel_flow_param set limit_flow=? where id=?";

		try {
			this.execute(sql, new Object[] { limitFlow, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean checkExist(String appName, String flowApp, String paramInfo) {
		String sql = "select * from sentinel_flow_param where app_name=? and ref_app=? and param_info=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, flowApp, paramInfo }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("paramName:" + appName + " flowApp:" + flowApp + " param:" + paramInfo + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		String sql = "update sentinel_flow_param set version=? where app_name=? and ref_app=?";

		try {
			this.execute(sql, new Object[] { version, appName, refApp });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersion(String id, String version) {
		String sql = "update sentinel_flow_param set version=? where id=?";

		try {
			this.execute(sql, new Object[] { version, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersionByName(String appName, String version) {
		String sql = "update sentinel_flow_param set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateState(String id) {
		String sql = "update sentinel_flow_param set state = (state + 1) mod 2 where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
