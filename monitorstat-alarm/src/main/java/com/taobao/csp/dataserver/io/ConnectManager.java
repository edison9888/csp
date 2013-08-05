
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import java.util.List;

import com.taobao.csp.dataserver.io.exception.NoServerException;

/**
 * @author xiaodu
 *
 * ����8:55:30
 */
public interface ConnectManager {
	
	/**
	 * ͨ������key ��ȡָ����һ������
	 *@author xiaodu
	 * @param key
	 * @return
	 *TODO
	 */
	public DataConnect selectConnect(String key)throws NoServerException;
	
	/**
	 * ��ȡ����ǰ ȫ��������
	 *@author xiaodu
	 * @return
	 *TODO
	 */
	public List<DataConnect> selectConnects()throws NoServerException;
	
	
	public void removeConnect(DataConnect connect);
	
	
	
	
	
	
	
	

}
