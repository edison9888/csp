package com.taobao.csp.dataserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.zookeeper.KeeperException;

import com.taobao.csp.common.ZKClient;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.io.Heartbeat;
import com.taobao.csp.dataserver.io.seria.ObjectSeriaUtil;
import com.taobao.csp.dataserver.memcache.HbaseQueue;
import com.taobao.csp.dataserver.memcache.core.Memcache;
import com.taobao.csp.dataserver.memcache.event.CspBdbQueue;
import com.taobao.csp.dataserver.memcache.time.CspTimeSchedu;
import com.taobao.csp.dataserver.server.handle.DataHandler;


public class DataAcceptServer implements Runnable{
	
	private static final Logger logger = Logger.getLogger(DataAcceptServer.class);
	
	private SocketAcceptor acceptor ;//创建一个非阻塞的Server端Socket,用NIO
	
	private int serverPort = 15678;
	
	
	private Thread heartbeat = null;
	
	private int MAX_ACCEPT = Runtime.getRuntime().availableProcessors() *10;
	
	
	public DataAcceptServer(int port,int accpetThreads){
		if(port >0){
			serverPort = port;
		}
		if(accpetThreads >0){
			MAX_ACCEPT = accpetThreads;
		}
		
		heartbeat = new Thread(this);
		heartbeat.setName("DataAcceptServer - heartbeat: "+serverPort+":"+MAX_ACCEPT);
		heartbeat.start();
		
	}
	public void startup(){
		acceptor = new NioSocketAcceptor(MAX_ACCEPT);
		acceptor.setReuseAddress(true);
		//创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		
		ProtocolCodecFilter filter= new ProtocolCodecFilter(ObjectSeriaUtil.getProtocolCodecFactory());
		chain.addLast("myChain", filter);
		//设定服务器端的消息处理器:一个SamplMinaServerHandler对象,
		acceptor.setHandler(new DataHandler(this));
				
		//绑定端口,启动服务器
		try {
			acceptor.bind(new InetSocketAddress(serverPort));
		} catch (IOException e) {
			logger.info("Mina Server is Listing on:= " + serverPort,e);
			System.exit(-1);
		}
		logger.info("Mina Server is Listing on:= " + serverPort);
		
		registerAddress();
	}
	
	
	public void stop(){
		
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			String ipNode = Constants.ZK_DS_ROOT_NODE+"/"+address.getHostAddress()+":"+serverPort;
			ZKClient.get().delete(ipNode);
		} catch (UnknownHostException e) {
			logger.info("Mina Server is Listing on:= " + serverPort,e);
		}
		
		
	}
	
	
	
	private void createRootNode() throws KeeperException, InterruptedException{
		 ZKClient.get().mkdirPersistent(Constants.ZK_DS_ROOT_NODE);
	}
	
	
	private void createIpNode() throws KeeperException, InterruptedException{
		try {
			InetAddress address = InetAddress.getLocalHost();
			
			String ipNode = Constants.ZK_DS_ROOT_NODE+"/"+address.getHostAddress()+":"+serverPort;
			
			ZKClient.get().mkdirPersistent(ipNode);
		} catch (UnknownHostException e) {
			logger.info("Mina Server is Listing on:= " + serverPort,e);
			System.exit(-1);
		}
	}
	
	

	private  void registerAddress(){
		try {
			createRootNode();
			createIpNode();
		} catch (KeeperException e) {
			logger.info("zookeeper create node error",e);
		} catch (InterruptedException e) {
			logger.info("zookeeper create node error",e);
		}
		
	 }
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			heartbeat();
		}
	}
	
	
public void heartbeat(){
	
	Heartbeat beat = new Heartbeat();
	beat.setBeat(System.currentTimeMillis());
	
	long total = Runtime.getRuntime().totalMemory();
	long free = Runtime.getRuntime().freeMemory();
	long max = Runtime.getRuntime().maxMemory();
	
	String memInfo="total:"+total+":free:"+free+":max:"+max;
	String totalCount=":totalNum,"+Memcache.count.get();
	String writeIndex=":writeIndex="+CspBdbQueue.keyIndex.get()+",recover="+CspBdbQueue.totalRecovery+
			",queuesize="+HbaseQueue.get().getQueueSize()
			+",isWriteToBdb="+Constants.IN_BDB+
			",="+Constants.QUERY_DEAD_TIME+","+CspTimeSchedu.time_execute.getQueue().size();
	
	beat.setJvmMemory(memInfo+totalCount+writeIndex);
	
	logger.warn("monitorInfo:"+beat.getJvmMemory());
	
	InetAddress address;
	try {
		address = InetAddress.getLocalHost();
		String ipNode = Constants.ZK_DS_ROOT_NODE+"/"+address.getHostAddress()+":"+serverPort;
		ZKClient.get().setData(ipNode, beat);
	} catch (UnknownHostException e) {
	}
	
}
	


}
