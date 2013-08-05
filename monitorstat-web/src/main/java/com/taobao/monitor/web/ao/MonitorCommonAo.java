
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
 * @version 2010-10-25 ����10:55:42
 */
public class MonitorCommonAo {
	
	private MonitorCommonAo(){}
	private static MonitorCommonAo ao = new MonitorCommonAo();
	
	public static MonitorCommonAo get(){
		return ao;
	}
	
	
	private MonitorTimeDao dao = new MonitorTimeDao();
	
	/**
	 * ��ѯ������key
	 * @return
	 */
	public Map<Integer,KeyPo> findAllKey(){
		return dao.findAllKey();
	}
	
	/**
	 * ��ѯ������key
	 * 
	 * @return
	 */
	public Map<Integer, SitePo> findAllSite() {
		return dao.findAllSite();
	}
	
	 /**
     * ȡ������Ӧ�õ�key
     * @param appId
     * @return
     */
    public List<KeyPo> findAllAppKey(int appId){
    	return dao.findAllAppKey(appId);
    }
    
	/**
	 * ��ȡȫ��app
	 * @return
	 */
	public List<AppInfoPo> findAllApp(){
		return dao.findAllApp();
	}

	/**
	 * ����key info
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
	 * ����keyName ǰ׺ like ���ҳ�����key
	 * @param keyName
	 * @return
	 */
	public List<KeyPo> findAllKey(String keyName){
		return dao.findAllKey(keyName);
	}

	/**
	 * ���� key ���� ģ����ѯ��������ص� key ��Ϣ���������б�
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
	 * ��ȡ���е�ǰ����
	 * @return
	 */
	public List<ReportInfoPo> findAllReport(){
		return dao.findAllReport();
	}
	
	
	public void addAppKeyRelation(int keyId,int appId) {
		dao.addAppKeyRelation(keyId, appId);
	}
	

}
