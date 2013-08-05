
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 
 * 查询单个key 的请求,通过这个key 直接定位到这个key对应的数据
 * @author xiaodu
 *
 * 下午3:48:16
 */
public class QuerySingleRequest extends RequestPacket{

	private static final long serialVersionUID = 1L;
	
	private String keyName;

	
	public QuerySingleRequest(){
		super();
	}
	
	public QuerySingleRequest(String keyName){
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
}
