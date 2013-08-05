
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.io.ConnectManager;
import com.taobao.csp.dataserver.io.DataConnect;
import com.taobao.csp.dataserver.io.ServerInfo;
import com.taobao.csp.dataserver.io.exception.ConnectCloseException;
import com.taobao.csp.dataserver.io.exception.NoServerException;

/**
 * @author xiaodu
 *
 * 下午3:28:56
 */
public class DefaultConnectManager implements ConnectManager{
	
	private static final Logger logger = Logger.getLogger(DefaultConnectManager.class);
	
	private ServerListManager serverListListen = new ServerListManager();
	
	public Map<ServerInfo,DataConnect> connectMap = new HashMap<ServerInfo, DataConnect>();
	
	
	
	
	public DataConnect selectConnect(String key) throws NoServerException {
		
		while(true){
			ServerInfo server = serverListListen.selectServer(key);
			try{
				DataConnect connect = createDataConnect(server);
				return connect;
			}catch (ConnectCloseException e) {
				logger.error("创建连接"+server+"失败",e);
				serverListListen.recoveryServer(server);
			}
		}
	}
	
	
	/**
	 * 创建 目标服务连接
	 *@author xiaodu
	 * @param server
	 * @return
	 * @throws Exception
	 *TODO
	 */
	private DataConnect createDataConnect(ServerInfo server) throws ConnectCloseException{
		
		DataConnect connect = connectMap.get(server);
		if(connect != null){
			return connect;
		}else{
			synchronized (connectMap) {
				connect = connectMap.get(server);
				if(connect != null){
					return connect;
				}else{
						connect = new DefaultDataConnect(server,this);
						connectMap.put(server, connect);
				}
			}
		}
		return connect;
	}

	@Override
	public List<DataConnect> selectConnects() throws NoServerException {
		
		List<DataConnect> connectlist = new ArrayList<DataConnect>();
		
		List<ServerInfo> serverList = serverListListen.selectServers();
		for(ServerInfo info:serverList){
			try {
				connectlist.add(createDataConnect(info));
			} catch (ConnectCloseException e) {
				logger.error("创建连接"+info+"失败",e);
				serverListListen.recoveryServer(info);
			}
		}
		return connectlist;
	}



	@Override
	public void removeConnect(DataConnect connect) {
		
		if(connect != null)
			connectMap.remove(connect.getServerInfo());
		
		serverListListen.recoveryServer(connect.getServerInfo());
		
	}



	
	
	
	
	

}
