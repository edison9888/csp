
package com.taobao.csp.assign.master;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.taobao.csp.assign.classloader.ClassEntry;
import com.taobao.csp.assign.packet.PacketType;
import com.taobao.csp.assign.packet.RequestPacket;
import com.taobao.csp.assign.packet.ResponsePacket;

/**
 * 
 * @author xiaodu
 * @version 2011-8-10 下午03:16:23
 */
public class ClassLoaderProxy {
	
	private IoSession session;
	
	private String slaveId;
	
	
	public String getSlaveId() {
		return slaveId;
	}



	public IoSession getSession() {
		return session;
	}

	/**
	 * 等待响应集合
	 */
	private static ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>> responses=
		new ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>>();
	
	
	public ClassLoaderProxy(IoSession session,String slaveId){
		this.session = session;
		this.slaveId = slaveId;
	}
	
	
	
	public void acceptSlaceClass(ResponsePacket rp){
		String uuid = rp.getResponseId();
		
		ArrayBlockingQueue<ResponsePacket> q = responses.get(uuid);
		
		if(q != null){
			q.add(rp);
		}
	}
	
	
//	public boolean cleanSlaveClassCache(){
//		
//		String uuid = UUID.randomUUID().toString();
//		ArrayBlockingQueue<ResponsePacket> queue = new ArrayBlockingQueue<ResponsePacket>(1);
//		
//		responses.put(uuid, queue);
//		
//		RequestPacket request = new RequestPacket(uuid,"",PacketType.CleanCache);
//		WriteFuture wf = session.write(request);
//		
//		try {
//			ResponsePacket packet = queue.poll(1, TimeUnit.MINUTES);
//			if(packet != null){
//				return (Boolean)packet.getEntry();
//			}
//			
//		} catch (InterruptedException e) {
//		}
//		
//		return false;
//	}
	
	public List<ClassEntry> querySlaveClassCache(){
		
		String uuid = UUID.randomUUID().toString();
		ArrayBlockingQueue<ResponsePacket> queue = new ArrayBlockingQueue<ResponsePacket>(1);
		
		responses.put(uuid, queue);
		
		RequestPacket request = new RequestPacket(uuid,"",PacketType.ClassCache);
		
		WriteFuture wf = session.write(request);
		
		wf.addListener(new IoFutureListener<WriteFuture>(){
			@Override
			public void operationComplete(WriteFuture future) {
				
				if(future.isWritten()){
					return ;
				}
				
			}});
		
		
		try {
			ResponsePacket packet = queue.poll(1, TimeUnit.MINUTES);
			if(packet != null){
				return (List<ClassEntry>)packet.getEntry();
			}
			
		} catch (InterruptedException e) {
		}
		return null;
	}
	
	
	

}
