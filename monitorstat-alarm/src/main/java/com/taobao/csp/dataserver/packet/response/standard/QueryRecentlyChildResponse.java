
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
public class QueryRecentlyChildResponse extends ResponsePacket{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1899191760961364279L;
	//Map<childKey,Map<属性，最近值>>
	private Map<String,Map<String ,DataEntry>> recentlyMap = new HashMap<String, Map<String,DataEntry>>();
	
	/**
	 * @param packetId
	 */
	public QueryRecentlyChildResponse(long packetId,Map<String,Map<String ,DataEntry>> recentlyMap) {
		super(packetId);
		this.recentlyMap = recentlyMap;
	}
	
	public QueryRecentlyChildResponse(){
		super(-1);
	}

	public Map<String, Map<String, DataEntry>> getRecentlyMap() {
		return recentlyMap;
	}

	public void setRecentlyMap(Map<String, Map<String, DataEntry>> recentlyMap) {
		this.recentlyMap = recentlyMap;
	}


	
	
	
	

}
