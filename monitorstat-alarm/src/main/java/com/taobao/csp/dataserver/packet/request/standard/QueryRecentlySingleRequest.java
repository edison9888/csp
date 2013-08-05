
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * ���󵥸�key�������������һ��ֵ
 * 
 * @author xiaodu
 *
 * ����9:15:31
 */
public class QueryRecentlySingleRequest  extends RequestPacket{
	
	private static final long serialVersionUID = 1L;
	
	private String keyName;
	public QueryRecentlySingleRequest(){
		super();
	}
	
	public QueryRecentlySingleRequest(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
