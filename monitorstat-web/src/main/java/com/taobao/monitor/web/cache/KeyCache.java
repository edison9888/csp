package com.taobao.monitor.web.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;

/**
 * 缓存了所有key的数据
 * @author xiaodu
 * @version 2010-9-21 上午11:45:31
 */
public class KeyCache {
	
	private static final Logger logger = Logger.getLogger(KeyCache.class);

	private static KeyCache cache = new KeyCache();

	private KeyCache() {
	}

	public static KeyCache get() {
		return cache;
	}

	private static Map<Integer, KeyPo> keyMap = new ConcurrentHashMap<Integer, KeyPo>();
	
	private static Map<Integer,Map<Integer, KeyPo>> appKeyMap = new ConcurrentHashMap<Integer, Map<Integer, KeyPo>>();

	public KeyPo getKey(int keyId) {
			KeyPo keyPo = keyMap.get(keyId);
			synchronized (cache) {
				keyPo = keyMap.get(keyId);
				if (keyPo == null) {
					logger.info("no cache  keyId = "+keyId);
					resetCache();
					keyPo = keyMap.get(keyId);
				}
				if (keyPo == null) {
					keyPo = new KeyPo();
					keyMap.put(keyId, keyPo);
					keyPo.setKeyId(keyId);
					keyPo.setKeyName("no key");
				}
			}
			
			return keyPo;
	}
	
	public List<KeyPo> getAppKey(int appId){
		Map<Integer, KeyPo> map = appKeyMap.get(appId);
		List<KeyPo> list = new ArrayList<KeyPo>();
		if(map != null){
			list.addAll(map.values());
		}
		return list;
	}

	public void resetCache() {
		Map<Integer, KeyPo> map = KeyAo.get().findAllKey();
		
		
		
		keyMap.putAll(map);
		
		List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
		for(AppInfoPo po:listApp){
			Map<Integer, KeyPo> tmpMap = appKeyMap.get(po.getAppId());
			if(tmpMap == null){
				tmpMap = new HashMap<Integer, KeyPo>();
				appKeyMap.put(po.getAppId(), tmpMap);
			}
			List<KeyPo> keyList = KeyAo.get().findAllAppKey(po.getAppId());
			for(KeyPo keypo:keyList){
				KeyPo key = keyMap.get(keypo.getKeyId());
				if(key!=null){
					tmpMap.put(keypo.getKeyId(), key);
				}
			}
			
			
		}
		
	}

}