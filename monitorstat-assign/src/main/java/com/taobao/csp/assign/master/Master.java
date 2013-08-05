
package com.taobao.csp.assign.master;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.taobao.csp.assign.classloader.master.CenterClassLoader;
import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.entry.JobReportEntry;
import com.taobao.csp.assign.packet.RequestPacket;
import com.taobao.csp.assign.packet.ResponsePacket;

/**
 * 
 * @author xiaodu
 * @version 2011-7-4 上午09:46:33
 */
public class Master implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Master.class);
	
	private int bindPort = 22881;// 服务器端绑定的端口
	
	private SocketAcceptor acceptor ;//创建一个非阻塞的Server端Socket,用NIO
	
	private Map<IoSession,SlaveProxy> slaveProxyMap = new ConcurrentHashMap<IoSession, SlaveProxy>();
	
	private Map<IoSession,ClassLoaderProxy> classLoaderProxy = new ConcurrentHashMap<IoSession, ClassLoaderProxy>();
	
	private CenterClassLoader centerClassLoader = new CenterClassLoader();
	
	private Object lock = new Object();
	private Object lock2 = new Object();
	private Object lock3 = new Object();
	
	/**
	 * 供外部一个接口可以调整server端decode缓冲区大小
	 */
	private ObjectSerializationCodecFactory factory;
	
	/**
	 * 
	 * @param port
	 * @param processorCount acceptor端io线程的最大数量，默认为机器cpu数+1
	 */
	public Master(int port, Integer processorCount) {
		this.bindPort = port;
		// 检测任务是否超时的线程
		new Thread(this).start();
		
		if (processorCount == null) {
			acceptor = new NioSocketAcceptor();
		} else {
			acceptor = new NioSocketAcceptor(processorCount);
		}
		
		acceptor.setReuseAddress(true);
		//创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		factory = new ObjectSerializationCodecFactory();
		ProtocolCodecFilter filter= new ProtocolCodecFilter(factory);
		chain.addLast("myChain", filter);
		
		//设定服务器端的消息处理器:一个SamplMinaServerHandler对象,
		acceptor.setHandler(new AcceptHandler());
				
		//绑定端口,启动服务器
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			logger.info("Mina Server is Listing on:= " + bindPort,e);		
		}
		logger.info("Mina Server is Listing on:= " + bindPort);	
	}
	
	public Master(int port){
		this(port, null);
	}

	/**
	 * 取得所有的连接上来的slave
	 * @return
	 */
	public List<SlaveProxy> getSlave(){
		List<SlaveProxy> list= new ArrayList<SlaveProxy>();
		list.addAll(slaveProxyMap.values());
		return list;
	}
	
	public List<ClassLoaderProxy> getClassLoader(){
		List<ClassLoaderProxy> list= new ArrayList<ClassLoaderProxy>();
		list.addAll(classLoaderProxy.values());
		return list;
	}
	
	
	/**
	 * 向CM3或CM4指派一个job
	 * @param job
	 * @return
	 */
	public JobFeature assignJob(IJob job){
		synchronized(lock3) {
			SlaveProxy proxy = lookupFreeSlave();
			logger.info("Master assignJob "+job.getJobId()+" "+job.getDetail().toString()+"to SlaveProxy:"+proxy.getSlaveId()+" "+proxy.getSession().getRemoteAddress());
			JobFeature jobFeature = proxy.submitJob(job);
			return jobFeature;
		}
	}
	
	/**
	 * 向CM5指派一个Job
	 * @author denghaichuan.pt
	 * @version 2012-2-20
	 * @param job
	 * @return
	 */
	public JobFeature assignJobToCM5(IJob job){
		synchronized(lock2) {
			SlaveProxy proxy = lookupFreeSlaveOfCM5();
			logger.info("Master assignJob "+job.getJobId()+" "+job.getDetail().toString()+"to CM5 SlaveProxy:"+proxy.getSlaveId()+" "+proxy.getSession().getRemoteAddress());
			JobFeature jobFeature = proxy.submitJob(job);
			return jobFeature;
		}
	}
	
	private void slaveReport(IoSession session, JobReportEntry jobReportEntry){
		
		logger.debug("slave report job:"+jobReportEntry.toString());
		
		SlaveProxy proxy = slaveProxyMap.get(session);
		if(proxy != null){
			proxy.jobReport(jobReportEntry);
		}
	}
	
	public Master(){
		this(22881);	
	}
	
	/**
	 * 设置decode对象最大大小
	 * The default value is 1048576(1MB).
	 * @author denghaichuan.pt
	 * @version 2012-2-28
	 * @param max
	 */
	public void setDecoderMaxObjectSize(int max) {
		factory.setDecoderMaxObjectSize(max);
	}
	
	private void acceptSlave(IoSession ioSession,SlaveProxy proxy){
		
		logger.info("master accept Slave:= " + proxy.getSlaveId()+" "+ioSession.getRemoteAddress());	
		
		slaveProxyMap.put(ioSession, proxy);
		lookme();
	}
	
	private void releseSlave(IoSession session){
		
		logger.info("master relese Slave:= "+session.getRemoteAddress());	
		
		SlaveProxy proxy = slaveProxyMap.remove(session);
		if(proxy != null){
			proxy.loseSlave();
		}
		session.close(true);
		
	}
	
	
	private void releseClassLoader(IoSession session){
		
		logger.info("master relese classLoader:= "+session.getRemoteAddress());	
		
		ClassLoaderProxy proxy = classLoaderProxy.remove(session);
		if(proxy != null){
			
		}
		session.close(true);
		
	}
	
	
	public void lookme(){
		synchronized (lock) {
			lock.notifyAll();
		}
		
		logger.info("Have Slave Free !");	
		
	}
	
	/**
	 * 向除CM5以外的所有机器发任务
	 * @return
	 */
	private SlaveProxy lookupFreeSlave(){
		while(true){
			for(Map.Entry<IoSession,SlaveProxy> entry:slaveProxyMap.entrySet()){
				SlaveProxy proxy = entry.getValue();
				if (!proxy.getSlaveSiteName().equalsIgnoreCase("CM5") && proxy.isfree()) {
					logger.info("获取空闲Slave="+proxy.toString());
					return proxy;
				}
			}
			logger.info("无空闲slave 进入等待...");
			
			synchronized (lock) {
				try {
					lock.wait(60000);
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	/**
	 * 向Cm5机房机器发送任务
	 * @author denghaichuan.pt
	 * @version 2012-2-20
	 * @return
	 */
	private SlaveProxy lookupFreeSlaveOfCM5() {
		while(true){
			for(Map.Entry<IoSession,SlaveProxy> entry:slaveProxyMap.entrySet()){
				SlaveProxy proxy = entry.getValue();
				if (proxy.getSlaveSiteName().equalsIgnoreCase("CM5") && proxy.isfree()) {
					logger.info("获取空闲Slave="+proxy.toString());
					return proxy;
				}
			}
			logger.info("无空闲slave 进入等待...");
			
			synchronized (lock) {
				try {
					lock.wait(60000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	private class AcceptHandler extends IoHandlerAdapter {
		
		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			//releseSlave(session);
			logger.error("master 发送消息出现异常:"+session.getRemoteAddress().toString(),cause);
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			if(message instanceof RequestPacket){
				RequestPacket rp = (RequestPacket)message;
				
				switch(rp.getPacketType()){
					case Clazz:
						centerClassLoader.putRequestPacket(session, (RequestPacket)message);
						break;
					case Jar:
						
						break;
					case File:
						centerClassLoader.putRequestPacket(session, (RequestPacket)message);
						break;
					case ClassCache:
						
						break;
					case Slave:
						SlaveProxy proxy = new SlaveProxy((String)rp.getRequestId(),session,Master.this,(String)rp.getResource());
						acceptSlave(session,proxy);
						logger.info("接收到slave Id:"+proxy.getSlaveId()+" 来源:"+session.getRemoteAddress().toString());
						break;
						
					case JobReport:
						JobReportEntry report = (JobReportEntry)rp.getResource();
						slaveReport(session, report);
						break;
					case ClassLoader:
						
						ClassLoaderProxy classLoaderproxy = new ClassLoaderProxy(session,(String)rp.getResource());
						classLoaderProxy.put(session, classLoaderproxy);
						logger.info("接收到 classloader 来源:"+session.getRemoteAddress().toString());
						break;
						
				}
			}else if(message instanceof ResponsePacket){
				ResponsePacket rp = (ResponsePacket)message;
				
				switch(rp.getPacketType()){
					case Clazz:
						break;
					case Jar:
						
						break;
					case File:
						
						break;
					case ClassCache:
						ClassLoaderProxy proxy = classLoaderProxy.get(session);
						if(proxy != null){
							proxy.acceptSlaceClass(rp);
						}
						break;
					case CleanCache:
						proxy = classLoaderProxy.get(session);
						if(proxy != null){
							proxy.acceptSlaceClass(rp);
						}
						break;
						
					case JobReport:
						break;
					case ClassLoader:
						
						break;
						
				}
			}else{
				logger.error("master 接收到信息无法识别"+message);
				
			}
			
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			
			
			if(slaveProxyMap.get(session)!=null){
				releseSlave(session);
			}
			if(classLoaderProxy.get(session) != null){
				releseClassLoader(session);
			}
			
			logger.info("失去 slave :"+session.getRemoteAddress().toString()+" 连接  ...");
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			
		}
	}

	/**
	 * 此线程不断轮询slave中的任务是否已经超时
	 * 超时则触发stop Job
	 */
	@Override
	public void run() {
		while(true) {
			
			for(Map.Entry<IoSession,SlaveProxy> entry : slaveProxyMap.entrySet()){
				SlaveProxy proxy = entry.getValue();
				Map<String, JobFeature> runMap = proxy.getRunMap();
				for (Map.Entry<String, JobFeature> entry1 : runMap.entrySet()) {
					JobFeature feature = entry1.getValue();
					if (feature.getJob().getDeadline() != -1l) {
						if (feature.isRun()) {
							if (feature.getJob().getDeadline() <= 0) {
								try {
									proxy.stopJob(feature.getJobId());
								} catch (Exception e) {
									logger.error("停止任务时发生异常", e);
									e.printStackTrace();
								}
							} else {
								feature.getJob().setDeadline(feature.getJob().getDeadline() - 60 * 1000);
							}
						}
					}
				}
				
			}
			
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}
