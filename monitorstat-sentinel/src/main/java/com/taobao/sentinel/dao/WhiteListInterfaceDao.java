package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.WhiteListInterfacePo;


public class WhiteListInterfaceDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(WhiteListInterfaceDao.class);
	
	public WhiteListInterfaceDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<WhiteListInterfacePo> findAll() {
		String sql = "select * from sentinel_white_interface order by app_name, interface_info";
		
		final List<WhiteListInterfacePo> list = new LinkedList<WhiteListInterfacePo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					WhiteListInterfacePo po = new WhiteListInterfacePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setRefAppName(rs.getString("ref_app"));
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
	
	public List<WhiteListInterfacePo> find(final String appName, final boolean includeDisable) {
		String sql = "select * from sentinel_white_interface where app_name=? order by interface_info";
		
		final List<WhiteListInterfacePo> list = new LinkedList<WhiteListInterfacePo>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int state = rs.getInt("state");
					if (state == Constants.DISABLE && !includeDisable) {
						return;
					}
					
					WhiteListInterfacePo po = new WhiteListInterfacePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setRefAppName(rs.getString("ref_app"));
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
	
	public void add(WhiteListInterfacePo po){
		String sql = "INSERT INTO sentinel_white_interface(id, app_name, interface_info, ref_app,user, version, state) VALUES(?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getInterfaceInfo(), po.getRefAppName(),
					po.getUser(), po.getVersion(), Constants.ENABLE });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public void add(String id, String appName, String interfaceInfo, String whiteApp,
			String user, String version) {
		String sql = "INSERT INTO sentinel_white_interface(id, app_name, interface_info, ref_app, user, version, state) VALUES(?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{ id, appName, interfaceInfo, whiteApp, 
					user, version, Constants.ENABLE });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean remove(String id) {
		String sql = "delete from sentinel_white_interface where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean checkExist(String appName, String whiteApp, String interfaceInfo) {
		String sql = "select * from sentinel_white_interface where app_name=? and ref_app=? and interface_info=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, whiteApp, interfaceInfo }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("interfaceName:" + appName + " whiteApp:" + whiteApp + " inteface:" + interfaceInfo + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		String sql = "update sentinel_white_interface set version=? where app_name=? and ref_app=?";

		try {
			this.execute(sql, new Object[] { version, appName, refApp });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersion(String id, String version) {
		String sql = "update sentinel_white_interface set version=? where id=?";

		try {
			this.execute(sql, new Object[] { version, id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateVersionByName(String appName, String version) {
		String sql = "update sentinel_white_interface set version=? where app_name=?";

		try {
			this.execute(sql, new Object[] { version, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean updateState(String id) {
		String sql = "update sentinel_white_interface set state = (state + 1) mod 2 where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
}
