
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import java.util.concurrent.TimeoutException;

import com.taobao.csp.dataserver.io.exception.NoServerException;
import com.taobao.csp.dataserver.io.exception.WriteErrorException;
import com.taobao.csp.dataserver.io.impl.DefaultClientManager;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * ÉÏÎç9:43:23
 */
public class ClientUtil {
	
	public static DefaultClientManager clientManager  = new DefaultClientManager();
	
	public static void invoke(String key,RequestPacket pack) throws NoServerException{
		DataClient dataClient = clientManager.selectSingleClient(key);
		dataClient.invoke(pack);
	}
	
	public static ResponsePacket invokeWaitResponse(String key,RequestPacket pack) throws NoServerException, TimeoutException, WriteErrorException{
		DataClient dataClient = clientManager.selectSingleClient(key);
		return dataClient.invokeSync(pack);
	}
	
	public static void invoke(RequestPacket pack)throws NoServerException{
		DataClient dataClient =clientManager.selectMutilClient();
		dataClient.invoke(pack);
	}
	
	public static ResponsePacket invokeWaitResponse(RequestPacket pack)throws NoServerException, TimeoutException, WriteErrorException{
		
		DataClient dataClient =clientManager.selectMutilClient();
		return dataClient.invokeSync(pack);
	}

}
