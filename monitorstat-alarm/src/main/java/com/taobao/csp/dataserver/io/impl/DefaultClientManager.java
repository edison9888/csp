
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.dataserver.io.ClientManger;
import com.taobao.csp.dataserver.io.ConnectManager;
import com.taobao.csp.dataserver.io.DataClient;
import com.taobao.csp.dataserver.io.DataConnect;
import com.taobao.csp.dataserver.io.exception.NoServerException;

/**
 * @author xiaodu
 *
 * ÏÂÎç1:48:13
 */
public class DefaultClientManager implements ClientManger{
	
	private ConnectManager connectManager = null;
	
	private Map<DataConnect,DataClient> dataClientMap = new ConcurrentHashMap<DataConnect, DataClient>();
	
	
	public  DefaultClientManager(){
		connectManager = new DefaultConnectManager();
	}
	
	@Override
	public DataClient selectSingleClient(String keyName) throws NoServerException  {
		
		DataConnect connect = connectManager.selectConnect(keyName);
		if(connect == null){
			return null;
		}
		return createDataClient(connect);
	}
	
	
	private DataClient createDataClient(DataConnect connect){
		
		DataClient client = dataClientMap.get(connect);
		if(client != null)
			return client;
		
		synchronized (dataClientMap) {
			client = dataClientMap.get(connect);
			if(client == null){
				client =  new DefaultDataClient(connect,this);
				dataClientMap.put(connect, client);
			}
		}
		return client;
	}
	
	

	@Override
	public DataClient selectMutilClient() throws NoServerException {
		
		List<DataConnect> connectList = connectManager.selectConnects();
		
		return new MultiDataClient(connectList);
	}
	
	
	

	@Override
	public void removeDataClient(DataClient client) {
		
		if(client == null){
			return ;
		}
		dataClientMap.remove(client.getDataConnect());
	}

	
	
	
	
	

}
