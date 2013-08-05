
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * 上午9:17:34
 */
public class QueryRecentlyHostResponse extends ResponsePacket{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8392802017943278904L;
	//Map<Ip,Map<属性，最近值>>
	private Map<String,Map<String,DataEntry>> recentlyMap = new HashMap<String, Map<String,DataEntry>>();
	
	/**
	 * @param packetId
	 */
	public QueryRecentlyHostResponse(long packetId,Map<String,Map<String,DataEntry>> recentlyMap) {
		
		super(packetId);
		
		this.recentlyMap = recentlyMap;
	}
	
	public QueryRecentlyHostResponse(){
		super(-1);
	}

	public Map<String, Map<String, DataEntry>> getRecentlyMap() {
		return recentlyMap;
	}

	public void setRecentlyMap(Map<String, Map<String, DataEntry>> recentlyMap) {
		this.recentlyMap = recentlyMap;
	}


	

}
