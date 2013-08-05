package com.taobao.csp.assign.classloader.master;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.taobao.csp.assign.classloader.ResourceEntry;
import com.taobao.csp.assign.packet.PacketType;
import com.taobao.csp.assign.packet.RequestPacket;
import com.taobao.csp.assign.packet.ResponsePacket;

/**
 * 
 * @author xiaodu
 * @version 2011-7-21 上午11:38:05
 */
public class CenterClassLoader implements Runnable{
	
	private static final Logger logger = Logger.getLogger(CenterClassLoader.class);
	

	private String repositoriesPath = null;

	private LinkedBlockingQueue<Entry> lookupQueue = new LinkedBlockingQueue<Entry>();
	
	private ClassLoader classLoader = null;
	
	private Thread thread = null;
	
	
	public CenterClassLoader(){
		this(null,Thread.currentThread().getContextClassLoader());
	}
	
	
	
	public CenterClassLoader(String path){
		this(path,Thread.currentThread().getContextClassLoader());
	}
	
	public CenterClassLoader(String path,ClassLoader classLoader){
		this.repositoriesPath = path;
		this.classLoader = classLoader;
		
		thread = new Thread(this);
		thread.setName("Assign - CenterClassLoader");
		thread.start();
	}
	
	public void putRequestPacket(IoSession session,RequestPacket packet){
		Entry entry = new Entry();
		entry.packet = packet;
		entry.session = session;
		entry.id = packet.getRequestId();
		lookupQueue.add(entry);
		
		logger.debug(session.getRemoteAddress()+" 请求资源:"+packet.getResource()+" 类型:"+packet.getPacketType());
		
	}
	
	
	
	
	
	private synchronized void findClassResource(Entry entry){
		RequestPacket packet = entry.packet;
		switch (packet.getPacketType()) {
		case Clazz:
				try {
					String classPath = changeClassNameToPath((String)packet.getResource());
					
					ResourceEntry resource = findResource(classPath);
					if(resource != null){
						
						ResponsePacket response = new ResponsePacket(packet.getRequestId(),resource,PacketType.Clazz);
						
						entry.session.write(response);
						logger.debug(entry.session.getRemoteAddress()+" 成功获取:"+packet.getResource()+" 类型:"+packet.getPacketType());
					}else{
						logger.warn(entry.session.getRemoteAddress()+" 无法获取:"+packet.getResource()+" 类型:"+packet.getPacketType());
					}
				} catch (Exception e) {}
			
			break;
		case File:
			try {
				ResourceEntry resource = findResource((String)packet.getResource());
				if(resource != null){
					
					ResponsePacket response = new ResponsePacket(packet.getRequestId(),resource,PacketType.File);
					
					entry.session.write(response);
					logger.debug(entry.session.getRemoteAddress()+" 成功获取:"+packet.getResource()+" 类型:"+packet.getPacketType());
				}else{
					logger.warn(entry.session.getRemoteAddress()+" 无法获取:"+packet.getResource()+" 类型:"+packet.getPacketType());
				}
			} catch (Exception e) {}
			break;
		case Jar:
			
			break;
		default:
			break;
		}
		
		
	}
	
	
	
	
	
	
	
	
	private String changeClassNameToPath(String className){
		 String tempPath = className.replace('.', '/');
	     String classPath = tempPath + ".class";
	     return classPath;
	}
	
	
	
	public ResourceEntry findResource(String className) throws Exception{
		ResourceEntry e = null;
		if(e != null){
			return e;
		}
		if(e == null){
			e = lookupExtendResource(className);
		}
		if(e == null){
			e = lookupLocal(className);
		}
		if(e == null){
			
			Thread.currentThread().getContextClassLoader().getResourceAsStream(className);
			
		}
		if(e != null){
			return e;
		}
		
		throw new Exception();
	}
	
	/**
	 * 通过自身loader 查找资源，
	 * @param name
	 * @return
	 */
	private synchronized ResourceEntry lookupLocal(String name){
		
		
		
		ResourceEntry resourceEntry = new ResourceEntry();
		
		if(classLoader != null){
			InputStream binaryStream = classLoader.getResourceAsStream(name);
			if(binaryStream != null){
				byte[] binaryContent = new byte[10240];
				
				byte[] buffer = new byte[2048];
				int write_pos = 0;
				
				try {
					while (true) {
						int len = binaryStream.read(buffer, 0, buffer.length);
						if(len<0){
							break;
						}
						int space_available = binaryContent.length - write_pos; //剩余				
						if (space_available < len){
							int need_space = write_pos + len;
							byte[] new_buffer = new byte[need_space * 2];;
							System.arraycopy(binaryContent, 0, new_buffer, 0, write_pos);
							binaryContent = new_buffer;
						}
						System.arraycopy(buffer, 0, binaryContent, write_pos, len);
						write_pos += len;
					}
					
				} catch (IOException e) {
					return null;
				}finally{
					try {
						binaryStream.close();
					} catch (IOException e) {
					}
				} 
				resourceEntry.setBinaryContent(binaryContent) ;
				resourceEntry.setBinaryLength(write_pos);
				return resourceEntry;
			}
			
		}
		return null;
	}
	
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	private synchronized ResourceEntry lookupExtendResource(String className) {
		
		if(repositoriesPath != null){
			File jarsPath = new File(repositoriesPath);
			if(jarsPath.isDirectory()){
				File[] files = jarsPath.listFiles();
				for(File f:files){
					if(f.isFile()&&f.getName().endsWith(".jar")){
						ResourceEntry e = lookupJar(f,className);
						if(e != null){
							return e;
						}
					}
				}
			}
		}
		return null;
	}

	
	
	/**
	 * 
	 * @param file
	 * @param className
	 * @return
	 */
	private ResourceEntry lookupJar(File file, String className) {
		
		String classPath = changeClassNameToPath(className);
		
		ResourceEntry resourceEntry = new ResourceEntry();
		try {
			JarFile jarFile = new JarFile(file);
			JarEntry jarEntry = jarFile.getJarEntry(classPath);
			if (jarEntry != null) {
				
				resourceEntry.setResourceName(className);

				int contentLength = (int) jarEntry.getSize();

				InputStream binaryStream = jarFile.getInputStream(jarEntry);
				if (binaryStream != null) {

					byte[] binaryContent = new byte[contentLength];

					int pos = 0;
					try {

						while (true) {
							int n = binaryStream.read(binaryContent, pos, binaryContent.length - pos);
							if (n <= 0)
								break;
							pos += n;
						}
						binaryStream.close();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
					resourceEntry.setBinaryContent(binaryContent) ;
					resourceEntry.setBinaryLength(contentLength);
					return resourceEntry;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		return null;
	}
	
	private class Entry{
		private String id;
		private IoSession session;
		private RequestPacket packet;
	}




	@Override
	public void run() {
		while(true){
			
			try {
				Entry packet = lookupQueue.poll(10, TimeUnit.MINUTES);
				if(packet != null){
					logger.debug("CenterClassLoader 等待获取资源队列:"+lookupQueue.size());
					findClassResource(packet);
				}
			} catch (InterruptedException e) {
			}
		}
		
	}
	
	
	public static void main(String[] args){
//		CenterClassLoader laoder = new CenterClassLoader();
//		try {
//			ResourceEntry resource = laoder.findResource("monitor_windows.properties");
//			
//			System.out.println(resource);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		ResourceBundle bundle = ResourceBundle.getBundle("monitor_windows");
		
	}

}
