package com.taobao.monitor.alarm.source.change;

import java.util.List;

import com.taobao.monitor.alarm.source.dao.KeySourceDependencyDao;
import com.taobao.monitor.alarm.source.po.KeySourcePo;
import com.taobao.monitor.common.po.KeyPo;

public class KeySourceInfoAo {
	private static KeySourceInfoAo ao = new KeySourceInfoAo();
	private KeySourceDependencyDao keySourceDependencyDao = new KeySourceDependencyDao();

	public static  KeySourceInfoAo get(){
		return ao;
	}
	
	public boolean addKeySourcePo(KeySourcePo po){
		return keySourceDependencyDao.addKeySourcePo(po);
	}
	
	public boolean safeAddKeySourcePo(KeySourcePo po){
		return keySourceDependencyDao.safeAddKeySourcePo(po);
	}
	
	public boolean updateKeySourcePo(KeySourcePo po){
		return keySourceDependencyDao.updateKeySourcePo(po);
	}
	
	public boolean deleteKeySourcePo(int keyId, int sourceAppId) {
		return keySourceDependencyDao.deleteKeySourcePo(keyId, sourceAppId);
	}
	
	public List<KeySourcePo> findAllKeySourcePosByAppId(int appId){
		return keySourceDependencyDao.findAllKeySourcePosByAppId(appId);
	}
	public List<KeySourcePo> queryKeySourceByKeyId(int keyId){
		return keySourceDependencyDao.queryKeySourceByKeyId(keyId);
	}
	
	public Integer countAppKeyByAppId(int appId) {
		return keySourceDependencyDao.countAppKeyByAppId(appId);
	}
	
	public List<KeyPo> queryAppKeyByAppIdInPage(int appId, int pageNum, int pageSize) {
		if( pageNum>0 && pageSize>0){
			return keySourceDependencyDao.queryAppKeyByAppIdInPage(appId, pageNum, pageSize);
		} else {
			return null;
		}
	}

	public Integer countAppKeyLikeName(String keyname, Integer appId) {
		return keySourceDependencyDao.countAppKeyLikeName(keyname, appId);
	}
	
	public List<KeyPo> queryAppKeyLikeNameInPage(String keyname, Integer appId, int pageNum, int pageSize) {
		if( pageNum > 0 && pageSize>0 ){
			return keySourceDependencyDao.queryAppKeyLikeNameInPage(keyname, appId, pageNum, pageSize);
		} else {
			return null;
		}
	}
	
}
