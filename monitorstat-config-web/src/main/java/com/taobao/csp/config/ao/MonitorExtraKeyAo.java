package com.taobao.csp.config.ao;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.config.dao.MonitorExtraKeyDao;


public class MonitorExtraKeyAo {
	private static final Logger logger = Logger.getLogger(MonitorExtraKeyAo.class);

	private static MonitorExtraKeyAo  ao = new MonitorExtraKeyAo();
	private MonitorExtraKeyDao dao = new MonitorExtraKeyDao();
	private MonitorExtraKeyAo(){
		
	}

	public static  MonitorExtraKeyAo get(){
		return ao;
	}
	
	/**
	 * 添加用户和key的关联关系
	 * @param userId,appId,keyId
	 */
	public boolean addUserAndKeyRel(int userId, int appId, int keyId) {
		
		return dao.addUserAndKeyRel(userId, appId, keyId);
	}
	
	/**
	 * 添加用户和key的关联关系
	 * @param userId,appId,keyId
	 */
	public boolean addUserAndKeyRel(int userId, int appId, List<String> keyList) {
		
		return dao.addUserAndKeyRel(userId, appId, keyList);
	}
	
	/**
	 * 删除deleteUserAndKeyRel
	 * @param userId,appId,keyId
	 */
	public boolean deleteUserAndKeyRel(int userId, int appId, int keyId) {
		
		return dao.deleteUserAndKeyRel(userId, appId, keyId);
	}
	
	/**
	 * 删除deleteRelWithUserId
	 * @param userId
	 */
	public boolean deleteRelWithUserId(int userId) {
		
		return dao.deleteRelWithUserId(userId);
	}
	/**
	 * 根据用户名获取key
	 * 
	 * @return
	 */
	public List<Integer> findUserAndKeyRel(int userId,int appId) {
		
		return dao.findUserAndKeyRel(userId, appId);
	}
	
	/**
	 * 根据userId, appId更新keyId
	 * @param userId,appId,keyId
	 * @return
	 */
	public boolean updateUserAndKeyRel(int userId,int appId, int keyId){
		
		return dao.updateUserAndKeyRel(userId, appId, keyId);
	}
	
	/**
	 * 根据应用Id获取关联key
	 * 
	 * @return
	 */
	public List<Integer> findAppAndKeyRelByAppId(int appId) {
		
		return dao.findAppAndKeyRelByAppId(appId);
	}
}
