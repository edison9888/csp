
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 请求单个key，并返回最近的一个值
 * 
 * @author xiaodu
 *
 * 上午9:15:31
 */
public class QueryRecentlyChildRequest  extends RequestPacket{
	
	private static final long serialVersionUID = 1L;
	
	private String keyName;

	public QueryRecentlyChildRequest(){
		super();
	}
	
	public QueryRecentlyChildRequest(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	
	

}
