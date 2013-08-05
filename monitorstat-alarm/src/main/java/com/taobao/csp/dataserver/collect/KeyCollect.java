
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.collect;

import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;

/**
 * @author xiaodu
 *
 * ÏÂÎç7:33:22
 */
public class KeyCollect {
	
	private String keyName;
	
	private KeyScope scope;
	
	private AppCollect appCollect;
	
	public KeyCollect(String name,KeyScope scope){
		this.keyName = name;
		this.scope = scope;
	}
	
	public KeyCollect appendKey(String keyName,KeyScope scope){
		
		KeyCollect key = new KeyCollect(keyName,scope);
		key.setAppCollect(appCollect);
		appCollect.getKeyList().add(key);
		return key;
	}
	
	
	public ValueCollect appendValue(String propertyName,Float value,ValueOperate operate){
		ValueCollect vc = new ValueCollect(propertyName, value, operate);
		appCollect.getValueList().add(vc);
		return vc;
	}
	
	
	 String getKeyName() {
		return keyName;
	}

	 KeyScope getScope() {
		return scope;
	}

	 AppCollect getAppCollect() {
		return appCollect;
	}

	 void setAppCollect(AppCollect appCollect) {
		this.appCollect = appCollect;
	}




	

}
