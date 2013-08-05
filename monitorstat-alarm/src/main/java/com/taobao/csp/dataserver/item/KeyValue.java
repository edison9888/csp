
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaodu
 *
 * 下午10:09:37
 */
public class KeyValue {
	
	private KeyObject currentKey;
	
	//根据Scope 产生
	private Map<String,ValueObjectBox> valueMap = new HashMap<String, ValueObjectBox>();
	
	private KeyValue nextKey;
	
	public KeyValue(){
		
	}
	
	
	public void addItem(String type,String ip,KeyObject key,ValueObjectBox values){
		
		key.getKeyName();
		
		
		
	}


	public KeyObject getCurrentKey() {
		return currentKey;
	}


	public void setCurrentKey(KeyObject currentKey) {
		this.currentKey = currentKey;
	}


	public Map<String, ValueObjectBox> getValueMap() {
		return valueMap;
	}


	public void setValueMap(Map<String, ValueObjectBox> valueMap) {
		this.valueMap = valueMap;
	}


	public KeyValue getNextKey() {
		return nextKey;
	}


	public KeyValue setNextKey(KeyValue nextKey) {
		this.nextKey = nextKey;
		return this.nextKey;
	}
	
	

}
