
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.key;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.monitor.MonitorLog;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspAppKeyRelation;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyPropertyRelation;

/**
 * @author xiaodu
 *
 * 下午2:16:20
 */
public class DBMediaKeyCache {
	
	private static final Logger logger = Logger.getLogger(DBMediaKeyCache.class);
	
	private static DBMediaKeyCache cache = new DBMediaKeyCache();
	
	
	private DBMediaKeyCache(){
		init();
		
		Thread t=new Thread(){
			public void run(){
				while(true){
					try{
						NOTEXISTKeyId.clear();
						NOTEXISTKeyName.clear();
						
						TimeUnit.MINUTES.sleep(5);
					}catch(Exception e){
						logger.warn("not existkey exception",e);
					}
				}
			}
			
		};
		t.setName("clean not exist key");
		
		t.start();
	}
	
	public static DBMediaKeyCache get(){
		return cache;
	}
	
	
	private Map<String, Integer> keyNameMap = new ConcurrentHashMap<String, Integer>();
	private Map<Integer, String> keyIdMap = new ConcurrentHashMap<Integer, String>();
	private Map<String, Integer> keyName2LevelMap = new ConcurrentHashMap<String, Integer>();
	private Map<String, Integer> NOTEXISTKeyId = new ConcurrentHashMap<String, Integer>();
	private Map<Integer, String> NOTEXISTKeyName = new ConcurrentHashMap<Integer, String>();
	
	//应用与key的关系
	private Map<Integer,Set<Integer>> appKeyRelationMap = new ConcurrentHashMap<Integer, Set<Integer>>();
	//key 与属性的关系
	private Map<Integer,Set<String>> keyPropertyRelationMap = new ConcurrentHashMap<Integer, Set<String>>();
	
	public void init() {
		List<CspKeyInfo> list = KeyAo.get().findAllKeyInfos();
		for(CspKeyInfo info :list){
			keyNameMap.put(info.getKeyName(), info.getKeyId());
			keyIdMap.put( info.getKeyId(), info.getKeyName());
			keyName2LevelMap.put(info.getKeyName(), info.getKeyLevel());
		}
		
		List<CspAppKeyRelation> appList = KeyAo.get().findAllAppKeyRelation();
		for(CspAppKeyRelation app:appList){
			Set<Integer> set = appKeyRelationMap.get(app.getAppId());
			if(set == null){
				set =Collections.synchronizedSet(new HashSet<Integer>()) ;
				appKeyRelationMap.put(app.getAppId(), set);
			}
			set.add(app.getKeyId());
		}
		
		List<CspKeyPropertyRelation> proList = KeyAo.get().findAllKeyPropertyRelation();
		for(CspKeyPropertyRelation pr:proList){
			
			Set<String> set = keyPropertyRelationMap.get(pr.getKeyId());
			
			if(set == null){
				set =Collections.synchronizedSet(new HashSet<String>()) ;
				keyPropertyRelationMap.put(pr.getKeyId(), set);
			}
			set.add(pr.getPropertyName());
		}
		logger.warn("=====key cacah init end,"+keyNameMap.size()+","+keyPropertyRelationMap.size()+"======");
	}

	public boolean exist(String keyName) {
		return keyNameMap.containsKey(keyName);
	}
	//返回类型不是int 是考虑返回未空的情况
	public Integer getKeyLevelByKeyName(String keyName){
		 return keyName2LevelMap.get(keyName);
	}
	
	public Integer getKeyId(String keyName){
		if(StringUtils.isBlank(keyName)){
			return 0;
		}
		Integer id=keyNameMap.get(keyName);
		if(id!=null){
			return id;
		}else{
			//如果key在不存在的Map中，直接返回
			if(NOTEXISTKeyId.get(keyName)!=null){
				return null;
			}else{
				//不然查一次db
				CspKeyInfo tmp = KeyAo.get().getKeyInfo(keyName);
				if(tmp != null){
					keyNameMap.put(tmp.getKeyName(), tmp.getKeyId());
					keyIdMap.put( tmp.getKeyId(), tmp.getKeyName());
					keyName2LevelMap.put(tmp.getKeyName(), tmp.getKeyLevel());
					
					return tmp.getKeyId();
				}else{
					//db不存在加入不存在map中
					NOTEXISTKeyId.put(keyName, 1);
					return null;
						
				}
			}
		}
		
	}
	public String getKeyNameById(Integer id){
	
		String keyName=keyIdMap.get(id);
		if(keyName!=null){
			return keyName;
		}else{
			//如果key在不存在的Map中，直接返回
			if(NOTEXISTKeyName.get(id)!=null){
				return null;
			}else{
				//不然查一次db
				CspKeyInfo tmp = KeyAo.get().getKeyInfo(keyName);
				if(tmp != null){
					keyNameMap.put(tmp.getKeyName(), tmp.getKeyId());
					keyIdMap.put( tmp.getKeyId(), tmp.getKeyName());
					keyName2LevelMap.put(tmp.getKeyName(), tmp.getKeyLevel());
					
					return tmp.getKeyName();
				}else{
					//db不存在加入不存在map中
					NOTEXISTKeyName.put(id, "");
					return null;
						
				}
			}
		}
	}

	private void putKey(String keyName,String keyscope, String parentKey) {
		Integer keyId = keyNameMap.get(keyName);
		if(keyId == null){
			long t = System.currentTimeMillis();
			synchronized (keyNameMap) {
				keyId = keyNameMap.get(keyName);
				if(keyId ==null){
					logger.warn("=====not exist key"+keyName+","+parentKey+keyNameMap.size()+"======");
					CspKeyInfo info = new CspKeyInfo();
					info.setKeyName(keyName);
					info.setParentKeyName(parentKey);
					info.setKeyScope(keyscope);
					CspKeyInfo tmp = KeyAo.get().addKeyInfo(info);
					if(tmp != null){
						keyNameMap.put(tmp.getKeyName(), tmp.getKeyId());
						keyIdMap.put( tmp.getKeyId(), tmp.getKeyName());
						keyName2LevelMap.put(tmp.getKeyName(), tmp.getKeyLevel());
					}else{
						keyNameMap.put(keyName, -1);
						keyName2LevelMap.put(keyName, 0);
					}
					MonitorLog.addStat(Constants.COLLECTION_KEYSTATUS, new String[]{"NewKeyToDb"}, new Long[]{1l,System.currentTimeMillis()-t});
					logger.info("新增 keyName="+keyName+" parentKey="+parentKey+" ID="+tmp.getKeyId());
				}
			}
		}
	}
	
	public Integer put(String appName,String keyName,String[] propertyNames,
			String keyScope,String parentKey){
		long t = System.currentTimeMillis();
		
		putKey(keyName,keyScope,parentKey);
		Integer keyId = keyNameMap.get(keyName);
		if(keyId != null){
			for(String propertyName:propertyNames)
				putProperty(keyId,propertyName);
			
			AppInfoPo app = AppInfoCache.getAppInfoByAppName(appName);
			if(app != null){
				putAppKey(app.getAppId(),keyId);
			}
			return keyId;
		}
		MonitorLog.addStat(Constants.COLLECTION_KEYSTATUS, new String[]{"PutKey"}, 
				new Long[]{1l,System.currentTimeMillis()-t});
		return null;
	}
	
	
	private void putAppKey(int appId,int keyId){
		long t = System.currentTimeMillis();
		Set<Integer> set =  appKeyRelationMap.get(appId);
		if(set == null){
			synchronized (keyPropertyRelationMap) {
				set =  appKeyRelationMap.get(appId);
				if(set == null){
					set = Collections.synchronizedSet(new HashSet<Integer>());
					appKeyRelationMap.put(appId, set);
				}
			}
		}
		if(!set.contains(keyId)){
			synchronized (keyPropertyRelationMap) {
				if(!set.contains(keyId)){
					set.add(keyId);
					KeyAo.get().addAppKeyRelation(appId, keyId);
					MonitorLog.addStat(Constants.COLLECTION_KEYSTATUS, new String[]{"NewAppKeyRelationToDb"}, new Long[]{1l,System.currentTimeMillis()-t});
				}
			}
		}
		
	}
	
	
	private void putProperty(int keyId,String propertyName){
		long t = System.currentTimeMillis();
		Set<String> pn =  keyPropertyRelationMap.get(keyId);
		if(pn == null){
			synchronized (keyPropertyRelationMap) {
				pn =  keyPropertyRelationMap.get(keyId);
				if(pn == null){
					pn =Collections.synchronizedSet(new HashSet<String>()) ;
					keyPropertyRelationMap.put(keyId, pn);
				}
			}
		}
		if(!pn.contains(propertyName)){
			synchronized (keyPropertyRelationMap) {
				if(!pn.contains(propertyName)){
					pn.add(propertyName);
					CspKeyPropertyRelation cpr = new CspKeyPropertyRelation();
					cpr.setKeyId(keyId);
					cpr.setPropertyName(propertyName);
					KeyAo.get().addKeyPropertyRelation(cpr);
					MonitorLog.addStat(Constants.COLLECTION_KEYSTATUS, new String[]{"NewKeyPropertyRelation"}, new Long[]{1l,System.currentTimeMillis()-t});
				}
			}
		}
	}
	

}
