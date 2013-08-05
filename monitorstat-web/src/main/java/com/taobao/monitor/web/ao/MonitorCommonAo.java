
package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.web.cache.KeyCache;
import com.taobao.monitor.web.core.dao.impl.MonitorTimeDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.web.vo.ReportInfoPo;
import com.taobao.monitor.web.vo.SitePo;

/**
 * 
 * @author xiaodu
 * @version 2010-10-25 上午10:55:42
 */
public class MonitorCommonAo {
	
	private MonitorCommonAo(){}
	private static MonitorCommonAo ao = new MonitorCommonAo();
	
	public static MonitorCommonAo get(){
		return ao;
	}
	
	
	private MonitorTimeDao dao = new MonitorTimeDao();
	
	/**
	 * 查询出所有key
	 * @return
	 */
	public Map<Integer,KeyPo> findAllKey(){
		return dao.findAllKey();
	}
	
	/**
	 * 查询出所有key
	 * 
	 * @return
	 */
	public Map<Integer, SitePo> findAllSite() {
		return dao.findAllSite();
	}
	
	 /**
     * 取得所有应用的key
     * @param appId
     * @return
     */
    public List<KeyPo> findAllAppKey(int appId){
    	return dao.findAllAppKey(appId);
    }
    
	/**
	 * 获取全部app
	 * @return
	 */
	public List<AppInfoPo> findAllApp(){
		return dao.findAllApp();
	}

	/**
	 * 根据key info
	 * @param po
	 * @return
	 */
	public boolean updateKeyInfo(KeyPo po){
		boolean b = dao.updateKeyInfo(po);
		if(b){
			KeyCache.get().getKey(po.getKeyId()).setAliasName(po.getAliasName());
			KeyCache.get().getKey(po.getKeyId()).setKeyType(po.getKeyType());
		}
		return b;
	}
	/**
	 * 根据keyName 前缀 like 查找出所有key
	 * @param keyName
	 * @return
	 */
	public List<KeyPo> findAllKey(String keyName){
		return dao.findAllKey(keyName);
	}

	/**
	 * 根据 key 名称 模糊查询出所有相关的 key 信息，并返回列表
	 * @param name
	 * @return
	 */
	public List<KeyPo> findKeyLikeName(String keyname,Integer appid){
		
		if(keyname==null&&appid==null){
			return new ArrayList<KeyPo>();
		}
		
		return dao.findKeyLikeName(keyname,appid);
	}
	
	/**
	 * 获取所有当前报表
	 * @return
	 */
	public List<ReportInfoPo> findAllReport(){
		return dao.findAllReport();
	}
	
	
	public void addAppKeyRelation(int keyId,int appId) {
		dao.addAppKeyRelation(keyId, appId);
	}
	

}
