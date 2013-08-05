
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.collect;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.dataserver.item.KeyScope;

/**
 * @author xiaodu
 *
 * ÏÂÎç7:30:55
 */
public class AppCollect {
	
	private String appName;
	
	private String ip;
	
	private long collectTime;
	
	private List<KeyCollect> keyList = new ArrayList<KeyCollect>();
	
	private List<ValueCollect> valueList = new ArrayList<ValueCollect>();
	
	
	
	public AppCollect(String app,String ip,long collectTime){
		this.appName = app;
		this.ip = ip;
		this.collectTime = collectTime;
	}
	
	public KeyCollect appendKey(String keyName,KeyScope scope){
		
		KeyCollect key = new KeyCollect(keyName,scope);
		key.setAppCollect(this);
		
		keyList.add(key);
		
		return key;
		
	}

	 String getAppName() {
		return appName;
	}

	 String getIp() {
		return ip;
	}

	 long getCollectTime() {
		return collectTime;
	}

	 List<KeyCollect> getKeyList() {
		return keyList;
	}

	 List<ValueCollect> getValueList() {
		return valueList;
	}
	
	

}
