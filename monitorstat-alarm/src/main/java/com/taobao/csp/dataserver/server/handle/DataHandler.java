/**
**xiaodu
**/
package com.taobao.csp.dataserver.server.handle;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.MemcacheStandard;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.packet.request.standard.QueryChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyChildRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyHostRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyMultiRequest;
import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlySingleRequest;
import com.taobao.csp.dataserver.packet.request.standard.QuerySingleRequest;
import com.taobao.csp.dataserver.packet.request.standard.RequestSecondStandard;
import com.taobao.csp.dataserver.packet.request.standard.RequestStandard;
import com.taobao.csp.dataserver.packet.response.standard.QueryChildResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryHostResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryMultiResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyChildResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyHostResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyMultiResponse;
import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlySingleResponse;
import com.taobao.csp.dataserver.packet.response.standard.QuerySingleResponse;
import com.taobao.csp.dataserver.server.DataAcceptServer;
import com.taobao.monitor.MonitorLog;

public class DataHandler  extends IoHandlerAdapter{
	
	private static final Logger logger = Logger.getLogger(DataHandler.class);
	
	private DataAcceptServer server ;
	
	public DataHandler(DataAcceptServer server ){
		this.server = server;
	}
	
	public DataAcceptServer getServer() {
		return server;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.info(session+"--�����쳣",cause);
	}




	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
		logger.info(session+"--��������");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info(session+"--�ر�����");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		//���벿��
		if(message instanceof RequestSecondStandard) {
			RequestSecondStandard r = (RequestSecondStandard)message;
			MemcacheStandard.get().putData(r,true);
			MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "INSERT_SecondData", r.getIp() },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "INSERT_IP", r.getIp() },new Long[] { 1l});
		}else  if(message instanceof RequestStandard) {
			RequestStandard r = (RequestStandard)message;
			MemcacheStandard.get().putData(r,false);
			MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "INSERT_MinuteData", r.getIp() },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_WRITE_LOG, new String[] { "INSERT_IP", r.getIp() },new Long[] { 1l});
		}
		//������ݲ�ѯ
		else if(message instanceof QueryRecentlyMultiRequest) {	
			//TODO ���ص�������+���ݼ���safe��������version1
			QueryRecentlyMultiRequest r = (QueryRecentlyMultiRequest)message;
			Map<String,Map<String,DataEntry>> valueMap = MemcacheStandard.get().getRecentlyMultiData(r);
			QueryRecentlyMultiResponse response = new QueryRecentlyMultiResponse(r.getRequestId(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryRecentlyMultiRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}else if(message instanceof QueryRecentlySingleRequest) {	
			//TODO ���ص�������+���ݼ���safe��������version1
			QueryRecentlySingleRequest r = (QueryRecentlySingleRequest)message;
			Map<String,DataEntry> valueMap = MemcacheStandard.get().getRecentlySingleData(r);
			QueryRecentlySingleResponse response = new QueryRecentlySingleResponse(r.getRequestId(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryRecentlySingleRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}else if(message instanceof QueryRecentlyChildRequest) {	
			//TODO ������key+���ݼ���	����key��idתΪstring��������version1
			
			QueryRecentlyChildRequest r = (QueryRecentlyChildRequest)message;
			Map<String,Map<String,DataEntry>> valueMap = MemcacheStandard.get().getRecentlyChildData(r);
			QueryRecentlyChildResponse response = new QueryRecentlyChildResponse(r.getRequestId(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryRecentlyChildRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}else if(message instanceof QueryRecentlyHostRequest) {	
			//TODO	����ip ���� ���ݼ��� safe ��������version1
			
			QueryRecentlyHostRequest r = (QueryRecentlyHostRequest)message;
			Map<String,Map<String,DataEntry>> valueMap = MemcacheStandard.get().getRecentlyHostData(r);
			QueryRecentlyHostResponse response = new QueryRecentlyHostResponse(r.getRequestId(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryRecentlyHostRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}
		//��ѯ����
		else if(message instanceof QuerySingleRequest) {		//ʵʱ��key ����
			//TODO	����ʱ�� ���� ip���� safe ��������version1
			
			QuerySingleRequest r = (QuerySingleRequest)message;
			Map<String, Map<String,Object>> valueMap = MemcacheStandard.get().getSingleData(r);
			QuerySingleResponse response = new QuerySingleResponse(r.getRequestId(), r.getKeyName(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryRecentlyHostRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		} else if(message instanceof QueryHostRequest) {		//ʵʱ����������
			
			QueryHostRequest r = (QueryHostRequest)message;
			Map<String, Map<String, Map<String,Object>>> valueMap = MemcacheStandard.get().getHostData(r);
			QueryHostResponse response = new QueryHostResponse(r.getRequestId(), r.getKeyName(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryHostRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		} else if(message instanceof QueryChildRequest) {		//ʵʱ����ĳkey���ӹ�ϵ
			//TODO ������key+���ݼ���	����key��idתΪstring��������version1
			
			QueryChildRequest r = (QueryChildRequest)message;
			Map<String, Map<String, Map<String,Object>>> valueMap = MemcacheStandard.get().getChildData(r);
			QueryChildResponse response = new QueryChildResponse(r.getRequestId(), r.getKeyName(), valueMap);
			session.write(response);		
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryChildRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}else if(message instanceof QueryMultiRequest) {		//ʵʱ����ĳkey���ӹ�ϵ
			//TODO	������key str+���ݼ���	������version1
			
			QueryMultiRequest r = (QueryMultiRequest)message;
			Map<String, Map<String, Map<String,Object>>> valueMap = MemcacheStandard.get().getMultiData(r);
			QueryMultiResponse response = new QueryMultiResponse(r.getRequestId(), valueMap);
			session.write(response);
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QueryMultiRequest" },new Long[] { 1l});
			MonitorLog.addStat(Constants.RPC_READ_LOG, new String[] { "QUERY_ALL"},new Long[] { 1l});
		}
	}
	

}
