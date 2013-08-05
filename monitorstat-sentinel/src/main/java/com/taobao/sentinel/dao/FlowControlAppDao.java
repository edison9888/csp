package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.FlowControlAppPo;

public class FlowControlAppDao extends MysqlRouteBase {

	private static final Logger logger =  Logger.getLogger(FlowControlAppDao.class);
	
	public FlowControlAppDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<FlowControlAppPo> findAll(String strategy) {
		String sql = "select * from sentinel_flow_app where strategy = ? order by app_name";
		
		final List<FlowControlAppPo> list = new LinkedList<FlowControlAppPo>();
		
		try {
			this.query(sql,  new Object[] { strategy } ,new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					FlowControlAppPo po = new FlowControlAppPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
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
	
	public List<FlowControlAppPo> find(final String appName, final boolean includeDisable, String strategy) {
		String sql = "select * from sentinel_flow_app where app_name=? and strategy=?";
		
		final List<FlowControlAppPo> list = new LinkedList<FlowControlAppPo>();
		
		try {
			this.query(sql, new Object[] { appName, strategy }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int state = rs.getInt("state");
					if (state == Constants.DISABLE && !includeDisable) {
						return;
					}
					
					FlowControlAppPo po = new FlowControlAppPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
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
	
	public void add(FlowControlAppPo po){
		String sql = "INSERT INTO sentinel_flow_app(id, app_name, ref_app, " +
				"limit_flow, user, version, state, strategy) VALUES(?,?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getRefAppName(), po.getLimitFlow(),
					po.getUser(), po.getVersion(), Constants.ENABLE, po.getStrategy()});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean remove(String id) {
		String sql = "delete from sentinel_flow_app where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		String sql = "update sentinel_flow_app set limit_flow=? where id=?";

		try {
			this.execute(sql, new Object[] { limitFlow, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean checkExist(String appName, String controlApp, String strategy) {
		String sql = "select * from sentinel_flow_app where app_name=? and ref_app=? and strategy=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, controlApp, strategy }, new SqlCallBack(){
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
	
	public boolean updateVersion(String appName, String refApp, String version) {
		String sql = "update sentinel_flow_app set version=? where app_name=? and ref_app=?";

		try {
			this.execute(sql, new Object[] { version, appName, refApp });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersion(String id, String version) {
		String sql = "update sentinel_flow_app set version=? where id=?";

		try {
			this.execute(sql, new Object[] { version, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersionByName(String appName, String version) {
		String sql = "update sentinel_flow_app set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateState(String id) {
		String sql = "update sentinel_flow_app set state = (state + 1) mod 2 where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
