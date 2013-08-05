
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.taobao.csp.dataserver.io.ConnectManager;
import com.taobao.csp.dataserver.io.DataConnect;
import com.taobao.csp.dataserver.io.ResponseCallBack;
import com.taobao.csp.dataserver.io.ServerInfo;
import com.taobao.csp.dataserver.io.exception.ConnectCloseException;
import com.taobao.csp.dataserver.io.seria.ObjectSeriaUtil;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;
import com.taobao.csp.dataserver.packet.response.standard.ErrorResponse;
import com.taobao.csp.dataserver.packet.response.standard.TimeOutResponse;
import com.taobao.monitor.MonitorLog;

/**
 * @author xiaodu
 *
 * ����6:10:52
 */
public class DefaultDataConnect implements DataConnect{
	
	private static final Logger logger = Logger.getLogger(DefaultDataConnect.class);
	
	
	private static final int PROCESSORS_NUM = Runtime.getRuntime().availableProcessors()+1;
	
	private SocketConnector ioConnector;
	
	private CopyOnWriteArrayList<IoSession> ioSessionList = new CopyOnWriteArrayList<IoSession>();
	
	private ServerInfo serverInfo = null;
	
	private Map<Long,CallBackEntry> requestWaitResponseMap = new ConcurrentHashMap<Long, CallBackEntry>();
	
	private ConnectManager connectManager = null;
	
	
	private static MonitorDeadRequest scanDead = new MonitorDeadRequest();
	
	 private Random random = new Random();
	 
	 private int session_size = 5;
	
	
	public DefaultDataConnect(ServerInfo serverInfo,ConnectManager connectManager) throws ConnectCloseException{
		this.serverInfo = serverInfo;
		this.connectManager = connectManager;
		ioConnector = new NioSocketConnector(PROCESSORS_NUM);
		// �����������ݵĹ�����
		DefaultIoFilterChainBuilder chain = ioConnector.getFilterChain();
		// �������л�
		ProtocolCodecFilter filter = new ProtocolCodecFilter(ObjectSeriaUtil.getProtocolCodecFactory());
		chain.addLast("myChain", filter);
		ioConnector.setConnectTimeoutMillis(10000);
		
		ioConnector.setHandler(new IoHandlerAdapter() {
		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			
			if(message instanceof ResponsePacket){
				ResponsePacket response = (ResponsePacket)message;
				doMessageReceived(response);
			}
		}
	});
		
		
		createConnect();
		
		scanDead.addConnect(this);
	}
	
	public void createConnect() throws ConnectCloseException{
		logger.info("create connect=========");
		String ip = serverInfo.getIp();
		int port  = serverInfo.getPort();
		try{
			for(int i=0;i<session_size;i++){
				ConnectFuture future = ioConnector.connect(new InetSocketAddress(ip, port));
				future.awaitUninterruptibly(3000);
				IoSession ioSession = future.getSession();
				ioSessionList.add(ioSession);
			}
		}catch (Exception e) {
			logger.error("���� "+serverInfo+"ʧ��",e);
			throw new ConnectCloseException("����Զ�̷���ʧ�� "+serverInfo.toString(),e);
		}
		
		if(ioSessionList.size()==0){
			throw new ConnectCloseException("����Զ�̷���ʧ�� "+serverInfo.toString());
		}
		
	}
	
	

	@Override
	public void write(final RequestPacket request,final ResponseCallBack callBack) {
		
		
		long time = System.currentTimeMillis();
		
		IoSession ioSession = ioSessionList.get(random.nextInt(session_size));
		
		
		if(ioSession.isClosing()||!ioSession.isConnected()){
			close();
			return ;
		}
		
		
		if(callBack != null){
			requestWaitResponseMap.put(request.getRequestId(),new CallBackEntry(request, callBack));
		}
		
		WriteFuture writeFuture = ioSession.write(request);
		
		writeFuture.addListener(new IoFutureListener<WriteFuture>() {
			@Override
			public void operationComplete(WriteFuture future) {
				if(!future.isWritten()){
					if(callBack != null){
						ErrorResponse response = new ErrorResponse(request.getRequestId());
						response.setThrowable(future.getException());
						response.setServerInfo(serverInfo.toString());
						doMessageReceived(response);
					}
					logger.error("����"+request+" д��Ŀ�����ʧ��"+future.getSession().toString(),future.getException());
				}				
			}
		});
		writeFuture.awaitUninterruptibly(50, TimeUnit.MILLISECONDS);
		
		
		MonitorLog.addStat("dataclient", new String[]{"datasent:"+ioSession.getRemoteAddress()}, new Long[]{1l,System.currentTimeMillis()-time});
		
	}
	
	
	private void doMessageReceived(ResponsePacket  response){
		long id =response.getRequestId();
		CallBackEntry entry = requestWaitResponseMap.remove(id);
		if(entry != null){
			entry.getCallback().doMessageReceived(response);
		}else{
			logger.error(serverInfo+"���� ID:"+id+" ��ʧ");
		}
	}

	@Override
	public boolean isConnect() {
		
		IoSession ioSession = ioSessionList.get(random.nextInt(session_size));
		
		return ioSession!=null&&!ioSession.isClosing()&&ioSession.isConnected();
	}
	
	
	/**
	 * ����߳� ������ʱɨ�� request
	 * �ȴ���������г�ʱ��û����Ӧ�����󣬲�����Щ���
	 * @author xiaodu
	 *
	 * ����11:10:24
	 */
	public void scanDeadRequest(){
		
		Iterator<Map.Entry<Long,CallBackEntry>> it = requestWaitResponseMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Long,CallBackEntry> entry = it.next();
			ResponseCallBack callback = entry.getValue().getCallback();
			RequestPacket request = entry.getValue().getRequest();
			if(request.getTimeout()<System.currentTimeMillis()){
				it.remove();
				TimeOutResponse packet = new TimeOutResponse(request.getRequestId());
				packet.setServerInfo(serverInfo.toString());
				callback.doMessageReceived(packet);
				
				logger.warn(serverInfo+"��Ӧ�ȴ�������"+request+"��ʱ����ֱ�ӷ���һ����ResponsePacket");
				
			}
		}
		
	}
	
	private class CallBackEntry{
		
		private ResponseCallBack callback;
		
		private RequestPacket request;
		
		
		public CallBackEntry(RequestPacket request,ResponseCallBack callback){
			this.callback = callback;
			this.request = request;
		}


		public ResponseCallBack getCallback() {
			return callback;
		}


		public RequestPacket getRequest() {
			return request;
		}
		
		
		
	}
	


	@Override
	public void close() {
		
		for(int i=0;i<session_size;i++){
			IoSession ioSession = ioSessionList.get(i);
			if(ioSession != null)
				ioSession.close(true);
		}
		
		if(ioConnector != null)
			ioConnector.dispose();
		
		scanDead.removeConnect(this);
		connectManager.removeConnect(this);
		
		logger.info("���� close "+serverInfo+" ���Ӳ���");
		
	}


	@Override
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	
	

}
