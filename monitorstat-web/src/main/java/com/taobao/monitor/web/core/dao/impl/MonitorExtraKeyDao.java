package com.taobao.monitor.web.core.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * ���ö���key������dao
 * @author wuhaiqian.pt
 *
 */
public class MonitorExtraKeyDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(MonitorExtraKeyDao.class);

	public MonitorExtraKeyDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}
	/**
	 * ����û���key�Ĺ�����ϵ
	 * @param userId,appId,keyId
	 */
	public boolean addUserAndKeyRel(int userId, int appId, int keyId) {
		try {
			
			String sql = "insert into ms_monitor_extra_user_key_define "
				+ "(user_id, app_id, key_id) values(?,?,?)";
			this.execute(sql, new Object[] {userId, appId, keyId});
		} catch (Exception e) {
			logger.error("addUserAndKeyRel ����,", e);
			return false;
		}
		
		return true;
	}
	/**
	 * ����û���key�Ĺ�����ϵ
	 * @param userId,appId,keyId
	 */
	public boolean addUserAndKeyRel(int userId, int appId, List<String> keyList) {
	
		try {
			
			String sql = "insert into ms_monitor_extra_user_key_define "
				+ "(user_id, app_id, key_id) values(?,?,?)";

			for(String keyId : keyList) {
			
				if(!isExistedRel(userId, appId, Integer.parseInt(keyId))) {
					
					this.execute(sql, new Object[] {userId, appId, keyId});
				}
			}
			
//			this.executeBatch(sql, parms, dbRoute)
		} catch (Exception e) {
			logger.error("addUserAndKeyRel ����,", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * ɾ��deleteUserAndKeyRel
	 * @param userId,appId,keyId
	 */
	public boolean deleteUserAndKeyRel(int userId, int appId, int keyId) {
		String sql = "delete from ms_monitor_extra_user_key_define where user_id=? and app_id=? and key_id=?";
		try {
			this.execute(sql, new Object[] { userId,appId,keyId });
		} catch (SQLException e) {
			logger.error("deleteUserAndKeyRel: ", e);
			return false;
		}
		return true;
	}
	/**
	 * ɾ��deleteRelWithUserId
	 * @param userId
	 */
	public boolean deleteRelWithUserId(int userId) {
		String sql = "delete from ms_monitor_extra_user_key_define where user_id=?";
		try {
			this.execute(sql, new Object[] { userId});
		} catch (SQLException e) {
			logger.error("deleteRelWithUserId: ", e);
			return false;
		}
		return true;
	}
	
	/**
	 * ����userid��appid��ȡkey
	 * 
	 * @return
	 */
	public List<Integer> findUserAndKeyRel(int userId,int appId) {
		String sql = "select key_id from ms_monitor_extra_user_key_define where user_id=? and app_id=? ";

		final List<Integer> keyList = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{userId, appId}, new SqlCallBack() {
		
				public void readerRows(ResultSet rs) throws Exception {

					keyList.add(rs.getInt("key_id"));

				}
			});
		} catch (Exception e) {
			logger.error("findUserAndKeyRel: ", e);
		}
		return keyList;
	}
	
/**
 * �鿴�Ƿ��Ѿ����ڹ���
 * @param userId
 * @param appId
 * @param keyId
 * @return
 */
	public boolean isExistedRel(int userId,int appId, int keyId) {
		String sql = "select key_id from ms_monitor_extra_user_key_define where user_id=? and app_id=? and key_id=?";
		
		final List<Integer> keyList = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{userId, appId, keyId}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					keyList.add(rs.getInt("key_id"));
					
				}
			});
		} catch (Exception e) {
			logger.error("isExistedRel: ", e);
		}
		
		if(keyList.size() > 0) {
			return true;
		}
		return false;
	}
	/**
	 * ����Ӧ��Id��ȡ����key
	 * 
	 * @return
	 */
	public List<Integer> findAppAndKeyRelByAppId(int appId) {
		String sql = "select key_id from ms_monitor_app_key where app_id=? ";
		
		final List<Integer> keyList = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					keyList.add(rs.getInt("key_id"));
				}
			});
		} catch (Exception e) {
			logger.error("findAppAndKeyRelByAppId: ", e);
		}
		return keyList;
	}
	
	/**
	 * ����userId, appId����keyId
	 * @param userId,appId,keyId
	 * @return
	 */
	public boolean updateUserAndKeyRel(int userId,int appId, int keyId){
		String sql = "update ms_monitor_extra_user_key_define set key_id=? where user_id=? and app_id=? ";
		try {
			this.execute(sql, new Object[]{keyId, userId, appId});
		} catch (SQLException e) {
			logger.error("updateUserAndKeyRel: ", e);
			return false;
		}
		return true;
	}
	
	
}
