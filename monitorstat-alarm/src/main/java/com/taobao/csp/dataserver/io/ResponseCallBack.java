
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import com.taobao.csp.dataserver.packet.ResponsePacket;


/**
 * @author xiaodu
 *
 * ����8:54:11
 */
public interface ResponseCallBack {
	
	public void doMessageReceived(ResponsePacket obj);
	
	
}
