
package com.taobao.csp.assign.classloader.slave;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.taobao.csp.assign.classloader.ClassEntry;
import com.taobao.csp.assign.classloader.ResourceEntry;
import com.taobao.csp.assign.packet.PacketType;
import com.taobao.csp.assign.packet.RequestPacket;
import com.taobao.csp.assign.packet.ResponsePacket;



/**
 * Slave ���� Center class loader
 * @author xiaodu
 * @version 2011-7-27 ����03:24:56
 */
public class AssignJobClassLoader extends SecureClassLoader {
	
	private static final Logger logger = Logger.getLogger(AssignJobClassLoader.class);
	//����
	private Map<String,ClassEntry> classMapCache = new ConcurrentHashMap<String, ClassEntry>();
	
	public Map<String, ClassEntry> getClassMapCache() {
		return classMapCache;
	}




	private String slaveId;
	
	
	private NioSocketConnector connector = new NioSocketConnector();
	
	private ConnectFuture future = null;
	
	private IoSession session = null;
	
	private String remoteAddress = null;
	
	private int remotePort = 22881;
	
//	private AtomicBoolean atomicStop; 
	private boolean stop = false;
	
	
	private Object lock = new Object();
	
	private AccessControlContext acc;
	
	 // HashMap that maps CodeSource to ProtectionDomain
    private HashMap pdcache = new HashMap(11);
	
	
	public void destroy(){
		stop = true;
		synchronized (lock) {
			lock.notifyAll();
		}
		
		synchronized (this) {			
			if(session != null){
				session.close(true);
				session = null;
			}
			
			if(future !=null){
				future.cancel();
				future = null;
			}
			if(connector != null){
				connector.dispose(true);
				connector = null;
			}
			if(classMapCache !=null){
				classMapCache.clear();
				classMapCache = null;
			}
		}
		
	}
	
	
	
	

	public AssignJobClassLoader(String remoteAddress,int port,ClassLoader cl,String slaveId){
		super(cl);
		
		
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
		    security.checkCreateClassLoader();
		}
		acc = AccessController.getContext();
		
		
		
		this.remoteAddress = remoteAddress;
		this.remotePort = port;
		this.slaveId = slaveId;
		
		
		// �����������ݵĹ�����
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		// �������л�
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
		chain.addLast("myChain", filter);
		
		// �趨�������˵���Ϣ������:һ��SamplMinaServerHandler����,
		connector.setHandler(new ClassLoaderHandler());
		// Set connect timeout.
		connector.setConnectTimeoutMillis(10000);
		
	}
	
	private synchronized void connect() {
		while(!stop){
			try{
				if(session != null){
					session.close(true);
				}
				
				if(future !=null){
					future.cancel();
				}
				
				future = null;
				session = null;
				
				future = connector.connect(new InetSocketAddress(
						remoteAddress, remotePort));
				
				future.awaitUninterruptibly();
				
				session = future.getSession();
				
				session.write(new RequestPacket(this.slaveId,PacketType.ClassLoader));
				
				
				logger.info("���� ip:"+remoteAddress+":"+remotePort+" �ɹ�! ");
				
				classMapCache.clear();
				
				break;
				
			}catch (Exception e) {
				logger.error("���� ip:"+remoteAddress+":"+remotePort+" ʧ��! �������³�������",e );
			}
			
			if(!stop){
				synchronized (lock) {
					try {
						logger.error("���� ip:"+remoteAddress+":"+remotePort+" ʧ��! ����30s�����³�������");
						lock.wait(30000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}
	
	private void sendRequest(RequestPacket entry){
		while(!stop){
			if(session !=null&&session.isConnected()){
				try{
					session.write(entry);
					logger.info("������Ϣ:"+entry);
					
					break;
				}catch (Exception e) {
					logger.error("������Ϣʧ��!",e );
				}
			}else{
			}
			connect();
		}
		
	}
	
	
	
	
	/*
	 * *
	 * �ȴ���Ӧ����
	 */
	private static ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>> responses=
		new ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>>();
	
	
	/**
	 * ���շ��͹�������
	 * @param entry
	 */
	public void acceptResponse(ResponsePacket entry){
		String uuid = entry.getResponseId();
		 ArrayBlockingQueue<ResponsePacket> q = responses.get(uuid);
		 if(q == null){
			 logger.warn(entry.getResponseId()+" ���󷵻��޷��ҵ��ȴ�����!"+entry.getPacketType()+":"+entry.getEntry());
		 }
		try {
			q.put(entry);
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	public InputStream getResourceAsStream(String name) {
		
		
		InputStream in = super.getResourceAsStream(name);
		if(in == null){
			
			RequestPacket entry = new RequestPacket(UUID.randomUUID().toString(),name,PacketType.File);
			
			String id = entry.getRequestId();
			ArrayBlockingQueue<ResponsePacket> q = new ArrayBlockingQueue<ResponsePacket>(1);
			responses.put(id, q);
			
			sendRequest(entry);
			
			ResponsePacket obj = null;
			try {
				logger.debug("��ʼ�ȴ���Ϣrequest id="+entry.getRequestId()+" SECONDS:20"+entry);
				obj = (ResponsePacket)q.poll(20, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
			if(obj !=null){
				ResourceEntry r = (ResourceEntry)obj.getEntry();
				in = new ByteArrayInputStream(r.getBinaryContent(), 0, r.getBinaryLength());
				return in;
			}
		}
		return in;
	}


	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return super.getResources(name);
	}


	/**
	 * ��master������в�ѯ��������Ϣ
	 * @param className
	 * @return
	 */
	public ResourceEntry findClassEntryInClassCenter(String className){
		
		RequestPacket entry = new RequestPacket(UUID.randomUUID().toString(),className,PacketType.Clazz);
		String id = entry.getRequestId();
		ArrayBlockingQueue<ResponsePacket> q = new ArrayBlockingQueue<ResponsePacket>(1);
		responses.put(id, q);
		sendRequest(entry);
		ResponsePacket obj = null;
		try {
			logger.debug("��ʼ�ȴ���Ϣrequest id="+id+" SECONDS:20");
			obj = (ResponsePacket)q.poll(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		if(obj !=null){
			return (ResourceEntry)obj.getEntry();
		}else{
			logger.debug("��ȡclassName��"+className+" ʧ�ܣ�");
		}
		return null;
		
	}
	

	@Override
	protected Class<?> findClass(final String classname) throws ClassNotFoundException {
		try {
		    return 
			AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
			    public Class<?> run() throws ClassNotFoundException {
			    	return defineClass(classname);
			    }
			}, acc);
		} catch (java.security.PrivilegedActionException pae) {
		    throw (ClassNotFoundException) pae.getException();
		}
	}
	
	
	
	private Class<?> defineClass(String classname) throws  ClassNotFoundException {
		
		 try{
			 int i = classname.lastIndexOf('.');
			 if (i != -1) {
				    String pkgname = classname.substring(0, i);
				    // Check if package already loaded.
				    Package pkg = getPackage(pkgname);
				    if (pkg == null) {
				    	  definePackage(pkgname, null, null, null, null, null, null, null);
				    }
				}
			 
			ResourceEntry entry = findClassEntryInClassCenter(classname);
			if(entry == null){
				throw new ClassNotFoundException(classname+" ��master�޷��ҵ�������Ϣ!");
			}		
			byte[] b = entry.getBinaryContent();
			
			 //�� �����ⲿ���ڵ�������� slave ����jar��·����
			URL url = AssignJobClassLoader.class.getProtectionDomain().getCodeSource().getLocation();
		    CodeSigner[] signers = AssignJobClassLoader.class.getProtectionDomain().getCodeSource().getCodeSigners();
		    
		    CodeSource cs = new CodeSource(url, signers);
			Class<?> clazz = this.defineClass(classname, b, 0, entry.getBinaryLength(),cs);
			
			ClassEntry ce = new ClassEntry();
			ce.setClassName(classname);
			ce.setClazz(clazz);
			ce.setCreateTime((new java.util.Date()).getTime());
			ce.setFileSize(entry.getBinaryLength());
			
			
			classMapCache.put(classname, ce);
			
			logger.debug("use cneter load class  :"+classname);
			
			return clazz;
		 }catch (Exception e) {
			throw new ClassNotFoundException(classname+" ��master�޷��ҵ�������Ϣ!");
		}
		 
	 }
	



	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		
		// ��ȡ�������̵߳�classloader��Ȼ�����ȡ��Map����
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		AssignJobClassLoader acl = (AssignJobClassLoader)cl;
		Map<String,ClassEntry> classMapCache = acl.getClassMapCache();
		ClassEntry r = classMapCache.get(name);
		
//		ClassEntry r = classMapCache.get(name);
		
		if(r != null){
			logger.debug("use cache load class  :"+name);
			return r.getClazz();
		}		
					
		return super.loadClass(name, resolve);
	}
	
	

	
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return loadClass(name,false);
	}






	private class ClassLoaderHandler extends IoHandlerAdapter{

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			logger.error("������Ϣʧ��",cause);
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			if(message instanceof ResponsePacket){
				ResponsePacket rp = (ResponsePacket)message;
				switch(rp.getPacketType()){
				case Clazz:
					acceptResponse((ResponsePacket)message);
					break;
				case File:
					acceptResponse((ResponsePacket)message);
				break;
				}
			}
			
			if(message instanceof RequestPacket){
				RequestPacket rp = (RequestPacket)message;
				switch(rp.getPacketType()){
					case ClassCache:
					
						List<ClassEntry> list = getClassListCache();
						
						ResponsePacket p = new ResponsePacket(rp.getRequestId(),list,PacketType.ClassCache);
						
						session.write(p);
					break;
				}
			}
			
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			logger.error("classloader ���� ip:"+remoteAddress+":"+remotePort+" ���Ͽ�");
		}
	}
	
	public List<ClassEntry> getClassListCache() {
		
		List<ClassEntry> list = new ArrayList<ClassEntry>();
		list.addAll(classMapCache.values());
		return list;
	}


	@Override
	protected void finalize() throws Throwable {
		logger.info("�ҿ���ʲôʱ�������...");
		super.finalize();
	}

	
	

}
