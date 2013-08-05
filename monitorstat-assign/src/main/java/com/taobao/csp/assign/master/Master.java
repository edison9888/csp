
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
 * @version 2011-7-4 ����09:46:33
 */
public class Master implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Master.class);
	
	private int bindPort = 22881;// �������˰󶨵Ķ˿�
	
	private SocketAcceptor acceptor ;//����һ����������Server��Socket,��NIO
	
	private Map<IoSession,SlaveProxy> slaveProxyMap = new ConcurrentHashMap<IoSession, SlaveProxy>();
	
	private Map<IoSession,ClassLoaderProxy> classLoaderProxy = new ConcurrentHashMap<IoSession, ClassLoaderProxy>();
	
	private CenterClassLoader centerClassLoader = new CenterClassLoader();
	
	private Object lock = new Object();
	private Object lock2 = new Object();
	private Object lock3 = new Object();
	
	/**
	 * ���ⲿһ���ӿڿ��Ե���server��decode��������С
	 */
	private ObjectSerializationCodecFactory factory;
	
	/**
	 * 
	 * @param port
	 * @param processorCount acceptor��io�̵߳����������Ĭ��Ϊ����cpu��+1
	 */
	public Master(int port, Integer processorCount) {
		this.bindPort = port;
		// ��������Ƿ�ʱ���߳�
		new Thread(this).start();
		
		if (processorCount == null) {
			acceptor = new NioSocketAcceptor();
		} else {
			acceptor = new NioSocketAcceptor(processorCount);
		}
		
		acceptor.setReuseAddress(true);
		//�����������ݵĹ�����
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		factory = new ObjectSerializationCodecFactory();
		ProtocolCodecFilter filter= new ProtocolCodecFilter(factory);
		chain.addLast("myChain", filter);
		
		//�趨�������˵���Ϣ������:һ��SamplMinaServerHandler����,
		acceptor.setHandler(new AcceptHandler());
				
		//�󶨶˿�,����������
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
	 * ȡ�����е�����������slave
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
	 * ��CM3��CM4ָ��һ��job
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
	 * ��CM5ָ��һ��Job
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
	 * ����decode��������С
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
	 * ���CM5��������л���������
	 * @return
	 */
	private SlaveProxy lookupFreeSlave(){
		while(true){
			for(Map.Entry<IoSession,SlaveProxy> entry:slaveProxyMap.entrySet()){
				SlaveProxy proxy = entry.getValue();
				if (!proxy.getSlaveSiteName().equalsIgnoreCase("CM5") && proxy.isfree()) {
					logger.info("��ȡ����Slave="+proxy.toString());
					return proxy;
				}
			}
			logger.info("�޿���slave ����ȴ�...");
			
			synchronized (lock) {
				try {
					lock.wait(60000);
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	/**
	 * ��Cm5����������������
	 * @author denghaichuan.pt
	 * @version 2012-2-20
	 * @return
	 */
	private SlaveProxy lookupFreeSlaveOfCM5() {
		while(true){
			for(Map.Entry<IoSession,SlaveProxy> entry:slaveProxyMap.entrySet()){
				SlaveProxy proxy = entry.getValue();
				if (proxy.getSlaveSiteName().equalsIgnoreCase("CM5") && proxy.isfree()) {
					logger.info("��ȡ����Slave="+proxy.toString());
					return proxy;
				}
			}
			logger.info("�޿���slave ����ȴ�...");
			
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
			logger.error("master ������Ϣ�����쳣:"+session.getRemoteAddress().toString(),cause);
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
						logger.info("���յ�slave Id:"+proxy.getSlaveId()+" ��Դ:"+session.getRemoteAddress().toString());
						break;
						
					case JobReport:
						JobReportEntry report = (JobReportEntry)rp.getResource();
						slaveReport(session, report);
						break;
					case ClassLoader:
						
						ClassLoaderProxy classLoaderproxy = new ClassLoaderProxy(session,(String)rp.getResource());
						classLoaderProxy.put(session, classLoaderproxy);
						logger.info("���յ� classloader ��Դ:"+session.getRemoteAddress().toString());
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
				logger.error("master ���յ���Ϣ�޷�ʶ��"+message);
				
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
			
			logger.info("ʧȥ slave :"+session.getRemoteAddress().toString()+" ����  ...");
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			
		}
	}

	/**
	 * ���̲߳�����ѯslave�е������Ƿ��Ѿ���ʱ
	 * ��ʱ�򴥷�stop Job
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
									logger.error("ֹͣ����ʱ�����쳣", e);
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
