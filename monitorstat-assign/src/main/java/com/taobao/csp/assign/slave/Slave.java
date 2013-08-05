
package com.taobao.csp.assign.slave;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.taobao.csp.assign.classloader.slave.AssignJobClassLoader;
import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.JobReport;
import com.taobao.csp.assign.job.JobReportType;
import com.taobao.csp.assign.job.entry.JobReportEntry;
import com.taobao.csp.assign.packet.PacketType;
import com.taobao.csp.assign.packet.RequestPacket;


/**
 * @author xiaodu
 * @version 2011-7-4 上午11:08:17
 */
public class Slave implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Slave.class);
	
	
	private NioSocketConnector connector = null;
	
	private ConnectFuture future = null;
	
	private IoSession session = null;
	
	private String remoteAddress;
	
	private int remotePort;
	
	private boolean isConnect = false;
	
	private static final long CONNECT_HEART = 60*1000;
	private static final long CONNECT_TIMEOUT = 30*1000;
	
	
	private String slaveId = UUID.randomUUID().toString();
	
	private String siteName; // slave位于的机房
	
	private int threadCount; // 这个slave中可以运行的线程数量
	
	private AssignJobClassLoader assignJobClassLoader = null;
	
	private Thread thread = null;
	
	private Object lock = new Object();
	
	private Map<String,IJob> slaveJobEntry = new ConcurrentHashMap<String, IJob>();
	
	
	
	public AssignJobClassLoader getAssignJobClassLoader() {
		return assignJobClassLoader;
	}

	public void setAssignJobClassLoader(AssignJobClassLoader assignJobClassLoader) {
				
		Thread.currentThread().setContextClassLoader(assignJobClassLoader);	
		this.assignJobClassLoader = assignJobClassLoader;
	}
	
	public void reloadClassLoader(){
		synchronized (connector) {
			if(assignJobClassLoader != null){
				assignJobClassLoader.destroy();
			}
			assignJobClassLoader = new AssignJobClassLoader(remoteAddress,remotePort,this.getClass().getClassLoader(),slaveId);
			
			Thread.currentThread().setContextClassLoader(assignJobClassLoader);	
			
			logger.info("ClassLoader 被重置。。。");
		}
	}
	
	public String getSlaveId() {
		return slaveId;
	}
	
	public Slave(String ip,int port,String slaveId, String siteName, int threadCount) {
		
		this.remoteAddress = ip;
		this.remotePort = port;
		this.slaveId = slaveId;
		this.siteName = siteName;
		this.threadCount = threadCount;
	}
	/**
	 * 默认此slave的机房为CM3,可接受job并行数为1
	 * @param ip
	 * @param port
	 * @param slaveId
	 */
	public Slave(String ip,int port,String slaveId) {
		this(ip, port, slaveId, "CM3", 1);
	}
	
	
	public void startup(){
		
		connect();
		
		thread = new Thread(this);
		thread.setName("Slave - Thread - "+getSlaveId());
		thread.start();
		
	}
	
	
	private void connect() {
		try{
			if(connector !=null){
				connector.dispose();
			}
			
			connector = new NioSocketConnector();
			
			reloadClassLoader();
			
			// 创建接收数据的过滤器
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			
			
			// 对象序列化
			ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
			chain.addLast("myChain", filter);
			chain.addLast("threadpool",  new ExecutorFilter(Executors.newCachedThreadPool(new ThreadFactory() {
				@Override
				public Thread newThread(final Runnable r) {
					Thread thread = new Thread(r);
					thread.setContextClassLoader(assignJobClassLoader);
					thread.setName("Slave-mina-Pool");
					return thread;
				}
			})));
			
			
			// 设定服务器端的消息处理器:一个SamplMinaServerHandler对象,
			connector.setHandler(new SlaveHandler());
			// Set connect timeout.
			connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
			
			if(session != null){
				session.close(true);
			}
			
			if(future !=null){
				future.cancel();
			}
			
			future = null;
			session = null;
			
			future = connector.connect(new InetSocketAddress(
					this.remoteAddress, this.remotePort));
			
			future.awaitUninterruptibly();
			
			session = future.getSession();
			
			//设置salve往回写超时时间，默认为60s
			session.getConfig().setWriteTimeout(60*5);
			
			isConnect = true;
			
			reportMsg(new RequestPacket(getSlaveId(),siteName+"#"+threadCount,PacketType.Slave));
			
			logger.info("连接 ip:"+remoteAddress+":"+remotePort+" 成功! 此slaveID为：" + getSlaveId());
			
		}catch (Exception e) {
			logger.error("连接 ip:"+remoteAddress+":"+remotePort+" 失败! 将在"+CONNECT_HEART+"重新尝试连接",e );
			isConnect = false;
		}
		
		
	}
	
	
	
	private void doJob(IJob job){
		JobEntry entry = new JobEntry(job,new JobReport(){
			public void doJobReport(IJob job,JobReportType state, Object obj) {
				
				switch(state){
					case Complete :
					case Error:
					case Exception:
					case Lose:
						slaveJobEntry.remove(job.getJobId());
				}
				
				RequestPacket packet = new RequestPacket(UUID.randomUUID().toString(),new JobReportEntry(job,state,obj),PacketType.JobReport);
				reportMsg(packet);
			}},assignJobClassLoader);
		
		slaveJobEntry.put(job.getJobId(), job);
		
		logger.info("接收到一个新的job:"+job.getDetail());
		synchronized (connector) {
			ThreadManager.addExecuteTask(entry);
		}
	}
	
	
	
	public void reportMsg(Object obj){
		
		if(session!= null&&session.isConnected()){
			WriteFuture future = session.write(obj);
			future.addListener(new IoFutureListener<IoFuture>(){
				public void operationComplete(IoFuture future) {
					WriteFuture f = (WriteFuture)future;
					if(f.isWritten()){
//						logger.info("发送信息成功");
						return ;
					}
					logger.info("连接发送信息失败...");
				}});
		}else{
			isConnect = false;
			logger.info("发送信息失败!,连接已经断开...");
		}
	}
	
	private void doJob(byte[] bs){
		
		synchronized (lock) {
			ByteArrayInputStream bin = new ByteArrayInputStream(bs,0,bs.length);
			try {
				//以前的loader
				Thread.currentThread().setContextClassLoader(assignJobClassLoader);	
				ObjectInputStream in = new ObjectInputStream(bin){
					protected Class<?> resolveClass(ObjectStreamClass desc)
					throws IOException, ClassNotFoundException
				    {
						Class<?> clazz = null;
						String name = desc.getName();
						if(name.charAt(0) == '['){
							return super.resolveClass(desc);
						}else{						
							try{
								clazz = assignJobClassLoader.loadClass(name);
							}catch (Exception e) {
							}
						}
						if(clazz == null){
							return super.resolveClass(desc);
						}else{
							return clazz;
						}
				}};
				Object j = in.readObject();
				doJob((IJob)j);
			} catch (ClassNotFoundException e) {
				logger.info("",e);
			} catch (IOException e) {
				logger.info("",e);
			}
		}
	}
	
	private class SlaveHandler extends IoHandlerAdapter{

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			logger.error("SlaveHandler 信息处理失败",cause);
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			
			if(message instanceof RequestPacket){
				RequestPacket rp = (RequestPacket)message;
				
				switch(rp.getPacketType()){
				case Job:
					byte[] bs = (byte[])rp.getResource();
					doJob(bs);
					break;
				case stopJob:
					String jobId = (String)rp.getResource();
					
					IJob job = slaveJobEntry.get(jobId);
					
					if(job != null){
						job.stopJob();
					}
					
					break;
					
				}
			}
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			isConnect = false;
		}
	}
	
	@Override
	public void run() {
		while (true) {
			
			try {
				Thread.sleep(CONNECT_HEART);
			} catch (InterruptedException e) {
			}
			
			if(isConnect){
				
				reportMsg(new RequestPacket(PacketType.SlaveHeart));// 发送心跳信息
				
			}else{
				connect();
			}
		}
		
	}

}
