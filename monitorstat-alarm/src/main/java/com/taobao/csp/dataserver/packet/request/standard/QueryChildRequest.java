
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * ���� key�Ĳ�ѯ������������һ��keyȫ��������Ϣ
 * 
 * @author xiaodu
 *
 * ����4:53:14
 */
public class QueryChildRequest extends RequestPacket{
	
	private static final long serialVersionUID = 1L;
	
	
	public QueryChildRequest(){
		super();
	}
	public QueryChildRequest(String keyName){
		this.keyName = keyName;
	}
	
	private String keyName;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	
	
	

}
