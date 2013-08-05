
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * ÉÏÎç11:02:19
 */
public class MultiResponsePacket extends ResponsePacket {

	/**
	 * @param requestId
	 */
	public MultiResponsePacket() {
		super(-1);
	}

	private static final long serialVersionUID = 1L;
	
	public List<ResponsePacket> packetList =  Collections.synchronizedList(new ArrayList<ResponsePacket>());
	
	public List<ErrorResponse> errorList =  Collections.synchronizedList(new ArrayList<ErrorResponse>());
	
	public List<TimeOutResponse> timeoutList =  Collections.synchronizedList(new ArrayList<TimeOutResponse>());

	public List<ResponsePacket> getPacketList() {
		return packetList;
	}

	public void setPacketList(List<ResponsePacket> packetList) {
		this.packetList = packetList;
	}

	public List<ErrorResponse> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorResponse> errorList) {
		this.errorList = errorList;
	}

	public List<TimeOutResponse> getTimeoutList() {
		return timeoutList;
	}

	public void setTimeoutList(List<TimeOutResponse> timeoutList) {
		this.timeoutList = timeoutList;
	}
	
	
	
	
	
	

}
