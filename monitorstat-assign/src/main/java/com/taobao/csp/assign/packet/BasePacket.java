
package com.taobao.csp.assign.packet;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-8-8 обнГ03:49:36
 */
public class BasePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1637424862310969208L;
	
	public BasePacket(PacketType packetType){
		this.packetType = packetType;
	}
	
	
	private PacketType packetType;


	public PacketType getPacketType() {
		return packetType;
	}


	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}
	
	

}
