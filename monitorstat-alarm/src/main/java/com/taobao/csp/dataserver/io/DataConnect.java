
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import com.taobao.csp.dataserver.io.exception.ConnectCloseException;
import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * @author xiaodu
 *
 * обнГ8:44:24
 */
public interface DataConnect {
	
	public void write(RequestPacket request,ResponseCallBack callBack) throws ConnectCloseException;
	
	public boolean isConnect();
	
	public void close();
	
	public void scanDeadRequest();
	
	public ServerInfo getServerInfo();
	
	
	public interface ConnectEvent{
		
		public void lost(DataConnect conn);
		
	}
	
	
	
}
