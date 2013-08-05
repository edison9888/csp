
package com.taobao.csp.dataserver.query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.taobao.csp.dataserver.io.ClientUtil;
import com.taobao.csp.dataserver.io.exception.NoServerException;
import com.taobao.csp.dataserver.io.exception.WriteErrorException;
import com.taobao.csp.dataserver.packet.RequestPacket;
import com.taobao.csp.dataserver.packet.ResponsePacket;
import com.taobao.csp.dataserver.packet.response.standard.MultiResponsePacket;

public class BaseQueryData {

	/**
	 * 通过单个key 指定请求某个服务器
	 *@author xiaodu
	 * @param keyName
	 * @param request
	 * @return
	 *TODO
	 */
	public static ResponsePacket invoke(String keyName,RequestPacket request){

		try {
			return ClientUtil.invokeWaitResponse(keyName, request);
		} catch (NoServerException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (WriteErrorException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 请求所有可用的数据主机
	 * @param request
	 * @return
	 * @throws NoServerException 
	 */
	public static List<ResponsePacket> invokesAll (RequestPacket request){

		ResponsePacket packet;
		try {
			packet = ClientUtil.invokeWaitResponse(request);
			
			if(packet instanceof MultiResponsePacket){
				MultiResponsePacket mutli = (MultiResponsePacket)packet;
				return mutli.getPacketList();
			}
		} catch (NoServerException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (WriteErrorException e) {
			e.printStackTrace();
		}
		return new ArrayList<ResponsePacket>();
	}
	

}
