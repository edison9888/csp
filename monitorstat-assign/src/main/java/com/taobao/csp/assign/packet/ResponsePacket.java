
package com.taobao.csp.assign.packet;


/**
 * 
 * @author xiaodu
 * @version 2011-8-8 ÏÂÎç03:56:52
 */
public class ResponsePacket extends BasePacket{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8216337174247483814L;
	
	public ResponsePacket(String uuid,Object entry,PacketType packetType){
		super(packetType);
		this.responseId = uuid;
		this.entry = entry;
	}
	
	
	private Object entry;
	
	private String responseId;


	public Object getEntry() {
		return entry;
	}


	public void setEntry(Object entry) {
		this.entry = entry;
	}


	public String getResponseId() {
		return responseId;
	}


	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}


	
	

}
