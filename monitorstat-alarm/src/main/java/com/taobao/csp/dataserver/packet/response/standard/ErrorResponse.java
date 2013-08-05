
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * ÏÂÎç2:21:06
 */
public class ErrorResponse extends ResponsePacket{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4555105948733717263L;

	/**
	 * @param requestId
	 */
	public ErrorResponse(long requestId) {
		super(requestId);
	}

	private Throwable throwable;
	
	private String serverInfo;

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	

}
