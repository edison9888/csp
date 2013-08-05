
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 
 * ��ѯ����key ������,ͨ�����key ֱ�Ӷ�λ�����key��Ӧ������
 * @author xiaodu
 *
 * ����3:48:16
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
