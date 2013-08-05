
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import java.util.List;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 请求单个key，并返回最近的一个值
 * 
 * @author xiaodu
 *
 * 上午9:15:31
 */
public class QueryRecentlyHostRequest  extends RequestPacket{
	
	private static final long serialVersionUID = 1L;
	
	private String keyName;
	private List<String> ipList;
	
	
	public QueryRecentlyHostRequest(){
		super();
	}
	
	public QueryRecentlyHostRequest(String keyName,List<String> ipList) {
		this.keyName = keyName;
		this.ipList = ipList;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public List<String> getIpList() {
		return ipList;
	}

	public void setIpList(List<String> ipList) {
		this.ipList = ipList;
	}
	
}
