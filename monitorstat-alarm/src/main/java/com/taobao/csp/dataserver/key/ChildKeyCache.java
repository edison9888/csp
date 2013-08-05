
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.key;

import java.util.List;

/**
 * @author xiaodu
 *
 * 下午3:00:27
 */
public interface ChildKeyCache {
	
	/**
	 * 设置key的子key
	 * @param parentKeyName
	 * @param childKeyName
	 */
	public void putKeyChildren(String parentKeyName,String childKeyName);
	
	/**
	 * 取得所有这个key下面的全部子key
	 * @param parentKeyName
	 * @return
	 */
	public List<String> getKeyChildren(String parentKeyName);
	
	
	public void clear();
	
	

}
