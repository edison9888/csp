
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.taobao.csp.dataserver.io.ClientManger;
import com.taobao.csp.dataserver.io.DataClient;
import com.taobao.csp.dataserver.io.DataConnect;
import com.taobao.csp.dataserver.io.ResponseCallBack;
import com.taobao.csp.dataserver.io.exception.ConnectCloseException;
import com.taobao.csp.dataserver.io.exception.WriteErrorException;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;
import com.taobao.csp.dataserver.packet.response.standard.ErrorResponse;
import com.taobao.csp.dataserver.packet.response.standard.TimeOutResponse;

/**
 * @author xiaodu
 *
 * 上午9:35:45
 */
public class DefaultDataClient implements DataClient{
	
	private DataConnect connect = null;
	
	private ClientManger clientManager = null;
	
	
	public DefaultDataClient(DataConnect connect,ClientManger clientManager ){
		this.connect = connect;
		this.clientManager = clientManager;
	}

	@Override
	public void invoke(RequestPacket obj) {
		try {
			connect.write(obj,null);
		} catch (ConnectCloseException e) {
			close();
		}
	}

	@Override
	public ResponsePacket invokeSync(RequestPacket obj) throws TimeoutException, WriteErrorException {
		final ResponseWait wait = new ResponseWait();
		try {
			connect.write(obj, new ResponseCallBack() {
				
				@Override
				public void doMessageReceived(ResponsePacket obj) {
					wait.received(obj);
				}
			});
		} catch (ConnectCloseException e) {
			close();
		}
		wait.await(1,TimeUnit.SECONDS);
		
		ResponsePacket r = wait.getValue();
		if(r instanceof TimeOutResponse){
			TimeOutResponse timeout = (TimeOutResponse)r;
			throw new TimeoutException(timeout.getServerInfo()+" 请求 key超时");
		}
		
		if(r instanceof ErrorResponse){
			ErrorResponse error = (ErrorResponse)r;
			throw new WriteErrorException("往服务"+error.getServerInfo()+"写入发生错误!",error.getThrowable());
		}
		
		return r;
	}
	
	
	
	private class ResponseWait{
		
		private ReentrantLock    lock       = null;
		private Condition        cond       = null; 
		
		private ResponsePacket value = null;
		
		public ResponseWait(){
			this.lock       = new ReentrantLock();
			this.cond       = this.lock.newCondition();
		}
		
		public ResponsePacket getValue(){
			return value;
		}
		
		public void received(ResponsePacket value){
			lock.lock();
			this.value = value;
			cond.signal();
			lock.unlock();
		}
		
		public void await( long timeout,TimeUnit unit){
			lock.lock();
			try {
				cond.await(timeout, unit);
			} catch (InterruptedException e) {
			}finally{
				lock.unlock();
			}
		}
		
		
		
	}
	
	

	@Override
	public boolean isUseable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		clientManager.removeDataClient(this);
	}

	@Override
	public DataConnect getDataConnect() {
		return connect;
	}

	

	

}
