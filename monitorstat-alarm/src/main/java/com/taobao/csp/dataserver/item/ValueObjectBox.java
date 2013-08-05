
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaodu
 *
 * ����9:27:07
 * @param <T>
 */
public class ValueObjectBox implements Serializable {
	
	private static final long serialVersionUID = -2688136382482093448L;
	
	//properyname - value
	private Map<String,ValueObject> valueMap= new HashMap<String, ValueObject>();
	
	/**
	 * û����׷�ӣ������޸�
	 * @param name
	 * @param value
	 * @param operate
	 * @return
	 */
	public ValueObjectBox append(String name,Object value,ValueOperate operate){
		ValueObject v = valueMap.get(name);
		if(v == null){
			v = new ValueObject(name,value,operate);
			valueMap.put(name, v);
		} else {
			v.setName(name);
			v.setValue(value);
			v.setOperate(operate);
		}
		return this;
	}

	public ValueObject getValueObjectByKey(String name) {
		return valueMap.get(name);
	}
	
	public Collection<ValueObject> getMapCollection() {
		return valueMap.values();		
	}
	
	public void clear() {
		valueMap.clear();
	}
	
	public int getMapSize() {
		return valueMap.size();
	}
}
