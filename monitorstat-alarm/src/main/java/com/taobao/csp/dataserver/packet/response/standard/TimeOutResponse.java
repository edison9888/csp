
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * обнГ2:17:59
 */
public class TimeOutResponse extends ResponsePacket{

	/**
	 * @param requestId
	 */
	public TimeOutResponse(long requestId) {
		super(requestId);
	}
	
	public TimeOutResponse() {
		super(-1);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4804469049838972780L;
	
	
	private String serverInfo;


	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	
	

}
