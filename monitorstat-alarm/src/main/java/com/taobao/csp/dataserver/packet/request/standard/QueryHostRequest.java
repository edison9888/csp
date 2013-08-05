
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import java.util.List;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 
 * ��ѯ����key ������,�������key��ÿ̨����������
 * @author xiaodu
 *
 * ����3:48:16
 */
public class QueryHostRequest extends RequestPacket{

	private static final long serialVersionUID = 1L;
	
	public QueryHostRequest(){
		super();
	}
	
	public QueryHostRequest(String keyName,List<String> ipList){
		this.keyName = keyName;
		this.ipList = ipList;
	}
	
	private String keyName;
	
	private List<String> ipList;
	
	
	

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
