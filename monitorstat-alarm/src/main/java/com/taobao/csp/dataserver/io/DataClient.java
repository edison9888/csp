
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import java.util.concurrent.TimeoutException;

import com.taobao.csp.dataserver.io.exception.WriteErrorException;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * 下午8:37:48
 */
public interface DataClient {
	
	
	public DataConnect getDataConnect();
	
	/**
	 * 异步 发送,无需等待
	 *@author xiaodu
	 * @param obj
	 *TODO
	 */
	public void invoke(RequestPacket obj);
	
	/**
	 * 同步等待结果
	 *@author xiaodu
	 * @param obj
	 * @return
	 *TODO
	 */
	public ResponsePacket invokeSync(RequestPacket obj) throws TimeoutException,WriteErrorException;
	
	
	public void close();
	
	
	
	
	
	public boolean isUseable();

}
