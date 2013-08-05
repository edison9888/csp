
package com.taobao.csp.assign.packet;

import java.util.UUID;


/**
 * 
 * @author xiaodu
 * @version 2011-8-8 ÏÂÎç03:59:13
 */
public class RequestPacket extends BasePacket{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2848878390462813040L;
	
	public RequestPacket(String id,Object name,PacketType key){
		super(key);
		this.requestId = id;
		this.resource = name;
	}
	
	public RequestPacket(PacketType key){
		super(key);
	}
	
	public RequestPacket(Object name,PacketType key){
		this(UUID.randomUUID().toString(),name,key);
	}
	
	private String requestId;

	private Object resource;

	public Object getResource() {
		return resource;
	}

	public void setResource(Object resource) {
		this.resource = resource;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "requestId="+this.requestId+"resource="+this.resource;
	}
	
	


	
	
	

}
