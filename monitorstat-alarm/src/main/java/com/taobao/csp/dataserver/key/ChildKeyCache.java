
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.key;

import java.util.List;

/**
 * @author xiaodu
 *
 * ����3:00:27
 */
public interface ChildKeyCache {
	
	/**
	 * ����key����key
	 * @param parentKeyName
	 * @param childKeyName
	 */
	public void putKeyChildren(String parentKeyName,String childKeyName);
	
	/**
	 * ȡ���������key�����ȫ����key
	 * @param parentKeyName
	 * @return
	 */
	public List<String> getKeyChildren(String parentKeyName);
	
	
	public void clear();
	
	

}
