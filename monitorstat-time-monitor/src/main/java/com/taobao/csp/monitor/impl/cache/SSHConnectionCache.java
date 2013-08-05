
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.cache;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;

import com.taobao.monitor.MonitorLog;

/**
 * @author xiaodu
 *
 * 上午11:47:47
 */
public class SSHConnectionCache {
	
	private static final Logger logger =  Logger.getLogger(SSHConnectionCache.class);
	
	
	private static SSHConnectionCache cache = new SSHConnectionCache();
	
	public static SSHConnectionCache get(){
		return cache;
	}
	
	
	private Map<String,ConnEntry> connMap = new ConcurrentHashMap<String, ConnEntry>();
	
	public Connection getConnection(String ip,String name,String psw){
		
		
		long time = System.currentTimeMillis();
		
		ConnEntry conn = connMap.get(ip);
		if(conn == null){
			synchronized (connMap) {
				conn = connMap.get(ip);
				if(conn == null){
					conn = createSshConnect(ip,name,psw);
					connMap.put(ip, conn);
				}
			}
		}
		//1小时后重新连接
		if(conn.conn ==null){
			if(conn.time +60*1000*60 <System.currentTimeMillis()){
				releaseConnection(ip);
				logger.info(ip+"释放 等待重新连接！");
				return null;
			}
		}
		
		MonitorLog.addStat("csp_data_analyse.log", new String[]{"data_analyse","getSsh"}, new Long[]{1l,System.currentTimeMillis()-time});
		
		return conn.conn;
	}
	
	public void  releaseConnection(String ip){
		ConnEntry entry = connMap.remove(ip);
		if(entry != null){
			Connection c = entry.conn;
			if(c != null){
				try{
					c.close();
				}catch (Exception e) {
				}
			}
		}
	}
	
	
	private class ConnEntry {
		private Connection conn;
		private long time;
		
	}
	
	
	
	private  ConnEntry createSshConnect(String ip,String name,String psw){	
		
		long time = System.currentTimeMillis();
		
		ConnEntry entry = new ConnEntry();
		entry.time = System.currentTimeMillis();
		Connection conn = new Connection(ip);
		try {
			conn.connect(null,2000,2000);
			boolean isAuthenticated = conn.authenticateWithPassword(name,psw);
			if (isAuthenticated == false){
				logger.info(ip+"login fail:"+name+":"+psw);
				throw new IOException(ip+"login fail:"+name+":"+psw);
			}
			entry.conn = conn;
		} catch (IOException e) {
			try{
				conn.close();
			}catch (Exception e1) {
			}
			conn = null;
			entry.conn = null;
		}
		
		MonitorLog.addStat("csp_data_analyse.log", new String[]{"data_analyse","createSsh"}, new Long[]{1l,System.currentTimeMillis()-time});
		
		return entry;
	} 

}
