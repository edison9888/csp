package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.UserPermissionPo;

public class UserPermissionDao extends MysqlRouteBase {
	
private static final Logger logger =  Logger.getLogger(UserPermissionDao.class);
	
	public UserPermissionDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public void add(UserPermissionPo po){
		String sql = "INSERT INTO sentinel_user_permission(id, mail, app_name, wangwang, level) VALUES(?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getMail(), po.getAppName(), po.getWangwang(), po.getLevel()});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public Set<String> getAppsByUser(String mail){
		String sql = "select * from sentinel_user_permission where mail=?";
		
		final Set<String> set = new HashSet<String>();
		
		try {
			this.query(sql, new Object[] { mail }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					set.add(appName);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return set;
	}
	
	public List<UserPermissionPo> findAllUserPermissions() {
		String sql = "select * from sentinel_user_permission where level !=" 
			+ Constants.ADMINISTRATOR +" order by wangwang";
		
		final List<UserPermissionPo> list = new ArrayList<UserPermissionPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UserPermissionPo po = new UserPermissionPo();
					po.setId(rs.getString("id"));
					po.setMail(rs.getString("mail"));
					po.setAppName(rs.getString("app_name"));
					po.setWangwang(rs.getString("wangwang"));
					po.setLevel(rs.getInt("level"));
					po.setNumber(list.size() + 1);					
					list.add(po);
					
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public boolean isAdministrator(String mail){
		String sql = "select level from sentinel_user_permission where mail=?";
		
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[] { mail }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					int level = rs.getInt("level");
					if (level == Constants.ADMINISTRATOR) {
						list.add(level);
					}
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
