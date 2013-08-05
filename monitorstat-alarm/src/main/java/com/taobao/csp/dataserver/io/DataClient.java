
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
 * ����8:37:48
 */
public interface DataClient {
	
	
	public DataConnect getDataConnect();
	
	/**
	 * �첽 ����,����ȴ�
	 *@author xiaodu
	 * @param obj
	 *TODO
	 */
	public void invoke(RequestPacket obj);
	
	/**
	 * ͬ���ȴ����
	 *@author xiaodu
	 * @param obj
	 * @return
	 *TODO
	 */
	public ResponsePacket invokeSync(RequestPacket obj) throws TimeoutException,WriteErrorException;
	
	
	public void close();
	
	
	
	
	
	public boolean isUseable();

}
