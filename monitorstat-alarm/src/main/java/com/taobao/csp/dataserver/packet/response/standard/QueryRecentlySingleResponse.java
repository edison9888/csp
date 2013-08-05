
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * ����9:17:34
 */
public class QueryRecentlySingleResponse extends ResponsePacket{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3844624063386973974L;
	
	//Map<���ԣ����ֵ>
	private Map<String,DataEntry> propertyMap;
	/**
	 * @param packetId
	 */
	public QueryRecentlySingleResponse(long packetId,Map<String,DataEntry> propertyMap) {
		super(packetId);
		this.propertyMap = propertyMap;
	}
	
	public QueryRecentlySingleResponse(){
		super(-1);
	}
	
	
	public Map<String, DataEntry> getPropertyMap() {
		return propertyMap;
	}
	public void setPropertyMap(Map<String, DataEntry> propertyMap) {
		this.propertyMap = propertyMap;
	}

	
	

}
