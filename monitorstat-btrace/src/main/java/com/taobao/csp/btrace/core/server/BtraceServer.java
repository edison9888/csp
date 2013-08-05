
package com.taobao.csp.btrace.core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 ����09:15:49
 */
public class BtraceServer {
	
	private BtraceServer(){
	}
	
	
	private static BtraceServer btraceServer = new BtraceServer();
	
	public final static int SERVER_PORT = 45687;
	
	//�����洢client��map��������Ϊ��Զ�̵��Զ��JVM�����õġ�
	private  Map<String,BtraceHandle> btraceClientMap = new ConcurrentHashMap<String, BtraceHandle>();
	
	private boolean start = false;
	
	public static BtraceServer get(){
		return btraceServer;
	}
	
	public BtraceHandle getBtraceHandle(String ip){
		return btraceClientMap.get(ip);
	}
	
	
	public void removeBtraceHandle(String ip){
		btraceClientMap.remove(ip);
	}
	
	public  synchronized void startServer(){
		
		if(!start){
			start = true;
			
			Thread thread  = new Thread(){
				
				public void run(){
					ServerSocket server = null;
					
					try {
						server = new ServerSocket(SERVER_PORT);
						server.setReuseAddress(true);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
					
					
					while(true){
						try {
							Socket socket = server.accept();
							//Server����һ��socket���ӣ��ͼ�¼һ��client��BtraceHandle����
							BtraceHandle handle = new BtraceHandle(socket);
							handle.startup(btraceServer);
							btraceClientMap.put(socket.getInetAddress().getHostAddress(), handle);
							
							System.out.println("��ȡ����:"+socket);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
					}
					
					
				}
				
			};
			thread.setDaemon(false);
			thread.start();
			
		}
		
	}
	

}
