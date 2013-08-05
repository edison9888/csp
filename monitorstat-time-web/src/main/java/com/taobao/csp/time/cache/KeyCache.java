package com.taobao.csp.time.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.CspKeyInfo;

public class KeyCache {
	private static Logger logger = Logger.getLogger(KeyCache.class);
	private Map<String,CspKeyInfo> keyNameMap = new ConcurrentHashMap<String,CspKeyInfo>();
	private Map<Integer,CspKeyInfo> keyIdMap = new  ConcurrentHashMap<Integer,CspKeyInfo>();
	private static KeyCache keyCache = new KeyCache();
	private KeyCache(){
	}
	public static KeyCache getCache(){
		return keyCache;
	}
/*
 *  获得key级别
 */	
	public Integer getLevel(String keyName){
		CspKeyInfo cki =  keyNameMap.get(keyName);
		if(cki==null){
			cki = KeyAo.get().getKeyInfo(keyName);
			if(cki!=null){
				keyNameMap.put(keyName, cki);
				keyIdMap.put(cki.getKeyId(), cki);
			}
		}
		if(cki!=null){
			return cki.getKeyLevel();
		}
		return null;
	}
	public CspKeyInfo getKeyInfo(String keyName){
		CspKeyInfo cki =  keyNameMap.get(keyName);
		if(cki==null){
			cki = KeyAo.get().getKeyInfo(keyName);
			if(cki!=null){
				keyNameMap.put(keyName, cki);
				keyIdMap.put(cki.getKeyId(), cki);
			}
		}
		return cki;
	}
	public CspKeyInfo getKeyInfo(Integer keyId){
		CspKeyInfo cki =  keyIdMap.get(keyId);
		if(cki==null){
			cki = new CspKeyInfo();
			KeyAo.get().findKeyByKeyId(keyId, cki);
			if(cki!=null){
				keyNameMap.put(cki.getKeyName(), cki);
				keyIdMap.put(cki.getKeyId(), cki);
			}
		}
		return cki;
	}
	public static void main(String args[]){
		for(Integer i=0;i<10;i++){
			Integer keyLevel = KeyCache.getCache().getLevel(KeyConstants.HSF_CONSUMER);
		}
	}
}
