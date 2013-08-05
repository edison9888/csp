package com.taobao.csp.dataserver.packet;

import java.io.Serializable;

import com.taobao.csp.dataserver.io.IDGenerator;

/**
 * 
 * @author xiaodu
 *
 * 上午9:45:35
 */
public class RequestPacket  implements Serializable{

	/**
	 * 保证了可以添加域
	 */
	private static final long serialVersionUID = -1883160508123643571L;

	private static final int MAX_WAIT_TIME = 1000;

	private long timeout = System.currentTimeMillis()+MAX_WAIT_TIME;

	private long requestId;
	
	public RequestPacket(){
		this.requestId = IDGenerator.getNextId();
	}
	
	public long getRequestId() {
		return requestId;
	}
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
