
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.io.DataClient;
import com.taobao.csp.dataserver.io.DataConnect;
import com.taobao.csp.dataserver.io.ResponseCallBack;
import com.taobao.csp.dataserver.io.exception.ConnectCloseException;
import com.taobao.csp.dataserver.io.exception.WriteErrorException;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;
import com.taobao.csp.dataserver.packet.response.standard.ErrorResponse;
import com.taobao.csp.dataserver.packet.response.standard.MultiResponsePacket;
import com.taobao.csp.dataserver.packet.response.standard.TimeOutResponse;

/**
 * @author xiaodu
 *
 * 下午4:49:52
 */
public class MultiDataClient  implements DataClient{
	
	private static final Logger logger = Logger.getLogger(MultiDataClient.class);

	private List<DataConnect> list = new ArrayList<DataConnect>();
	
	
	public MultiDataClient(List<DataConnect> list){
		this.list = list;
	}
	

	@Override
	public ResponsePacket invokeSync(RequestPacket request) throws WriteErrorException {
		
		final ResponseWait wait = new ResponseWait();
		
		for(final DataConnect conn:list){
			try {
				conn.write(request, new ResponseCallBack() {
					@Override
					public void doMessageReceived(ResponsePacket r) {
						
						if(r instanceof TimeOutResponse){
							TimeOutResponse timeout = (TimeOutResponse)r;
							wait.receivedTimeOut(timeout);
							
							logger.error(conn.getServerInfo()+"请求超时");
							
						}else if(r instanceof ErrorResponse){
							ErrorResponse error = (ErrorResponse)r;
							wait.receivedError(error);
							
							logger.error(conn.getServerInfo()+"请求错误");
							
						}else {
							wait.received(r);
						}
					}
				});
			} catch (ConnectCloseException e) {
				conn.close();
			}
		}
		wait.await(list.size(),10, TimeUnit.SECONDS);
		
		MultiResponsePacket multi =(MultiResponsePacket) wait.getValue();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("写入失败服务:");
		for(ErrorResponse e:multi.getErrorList()){
			sb.append(""+e.getServerInfo()+",");
		}
		sb.append("写入超时服务:");
		for(TimeOutResponse e:multi.getTimeoutList()){
			sb.append(""+e.getServerInfo()+",");
		}
		
		if(multi.getErrorList().size()>0||multi.getTimeoutList().size()>0){
			throw new WriteErrorException(sb.toString());
		}
		
		return wait.getValue();
	}

	@Override
	public boolean isUseable() {
		return false;
	}

	@Override
	public void invoke(RequestPacket request) {
		for(DataConnect conn:list){
			try {
				conn.write(request, null);
			} catch (ConnectCloseException e) {
				conn.close();
			}
		}
	}
	
	
	private class ResponseWait{
		
		private ReentrantLock    lock       = null;
		private Condition        cond       = null; 
		private int              doneCount  = 0;
		
		private MultiResponsePacket multi =new MultiResponsePacket();;
		
		public ResponseWait(){
			this.lock       = new ReentrantLock();
			this.cond       = this.lock.newCondition();
			
		}
		
		public ResponsePacket getValue(){
			return multi;
		}
		
		public void received(ResponsePacket value){
			lock.lock();
			try {
				multi.getPacketList().add(value);
				this.doneCount++;
				cond.signal();
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
		}
		
		public void receivedError(ErrorResponse value){
			lock.lock();
			try {
				multi.getErrorList().add(value);
				this.doneCount++;
				cond.signal();
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
		}
		
		public void receivedTimeOut(TimeOutResponse value){
			lock.lock();
			try {
				multi.getTimeoutList().add(value);
				this.doneCount++;
				cond.signal();
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
		}
		
		
		
		public boolean await(int count, long timeout,TimeUnit unit){
			long t = unit.toNanos(timeout);
			lock.lock();
			try {
				while (this.doneCount < count) {
					if ((t = cond.awaitNanos(t)) <= 0) {
						return false;
					}
				}
			} catch (InterruptedException e) {
				return false;
			} finally {
				lock.unlock();
			}
			return true;
		}
		
		
		
	}
	@Override
	public void close() {
		
	}

	@Override
	public DataConnect getDataConnect() {
		return null;
	}

}
