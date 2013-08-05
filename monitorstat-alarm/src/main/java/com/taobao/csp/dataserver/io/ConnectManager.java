
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import java.util.List;

import com.taobao.csp.dataserver.io.exception.NoServerException;

/**
 * @author xiaodu
 *
 * 下午8:55:30
 */
public interface ConnectManager {
	
	/**
	 * 通过单个key 获取指定的一个连接
	 *@author xiaodu
	 * @param key
	 * @return
	 *TODO
	 */
	public DataConnect selectConnect(String key)throws NoServerException;
	
	/**
	 * 获取到当前 全部的连接
	 *@author xiaodu
	 * @return
	 *TODO
	 */
	public List<DataConnect> selectConnects()throws NoServerException;
	
	
	public void removeConnect(DataConnect connect);
	
	
	
	
	
	
	
	

}
