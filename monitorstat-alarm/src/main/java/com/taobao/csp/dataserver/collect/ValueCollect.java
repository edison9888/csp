
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.collect;

import java.util.List;

import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;

/**
 * @author xiaodu
 *
 * ÏÂÎç7:36:39
 */
public class ValueCollect {
	
	private String propertyName;
	
	private Object value;
	
	private ValueOperate operate;
	
	private KeyCollect keyCollect;
	
	
	public ValueCollect(String propertyName,Object value,ValueOperate operate){
		this.propertyName = propertyName;
		this.value = value;
		this.operate = operate;
	}
	
	public ValueCollect appendValue(String propertyName,Object value,ValueOperate operate){
		ValueCollect collect = new ValueCollect(propertyName, value, operate);
		collect.setKeyCollect(keyCollect);
		keyCollect.getAppCollect().getValueList().add(collect);
		return collect;
	}
	
	public void submit(){
		AppCollect app = keyCollect.getAppCollect();
		
		 List<KeyCollect> keyList = app.getKeyList();
		 List<ValueCollect> valueList = app.getValueList();
		 
		 String[] keys = new String[keyList.size()];
		 KeyScope[] scopes = new KeyScope[keyList.size()];
		 for(int i=0;i<keyList.size();i++){
			 KeyCollect key = keyList.get(i);
			 keys[i] = key.getKeyName();
			 scopes[i] = key.getScope();
		 }
		 
		 
		 String[] propertyNames = new String[valueList.size()];
		 Object[] values = new Object[valueList.size()];
		 ValueOperate[] os = new ValueOperate[valueList.size()];
		 
		 for(int i=0;i<valueList.size();i++){
			 ValueCollect value = valueList.get(i);
			 propertyNames[i] = value.getPropertyName();
			 values[i] = value.getValue();
			 os[i] = value.getOperate();
		 }
		 
		 
		 try {
			CollectDataUtilMulti.collect(app.getAppName(), app.getIp(), app.getCollectTime(), keys, scopes, propertyNames, values, os);
		} catch (Exception e) {
		}
	}


	 String getPropertyName() {
		return propertyName;
	}


	 void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}


	 Object getValue() {
		return value;
	}


	 void setValue(Object value) {
		this.value = value;
	}


	 ValueOperate getOperate() {
		return operate;
	}


	 void setOperate(ValueOperate operate) {
		this.operate = operate;
	}


	 KeyCollect getKeyCollect() {
		return keyCollect;
	}


	 void setKeyCollect(KeyCollect keyCollect) {
		this.keyCollect = keyCollect;
	}
	
	

}
