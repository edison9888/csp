
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * ����5:09:18
 */
public class QueryHostResponse extends ResponsePacket{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3443620185699631717L;

	/**
	 * @param packetId
	 */
	public QueryHostResponse(long packetId, String keyName, Map<String, Map<String, Map<String,Object>>> valueMap) {
		super(packetId);
		this.keyName = keyName;
		this.valueMap = valueMap;
	}
	
	public QueryHostResponse(){
		super(-1);
	}

	
	private String keyName;
	
	/**
	 * valueMap�ṹ��	<ip,<time,<propertyName,value>>>
	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
	 * propertyName:	����������
	 * value:			��Ϥֵ������ʱ������
	 */
	private Map<String, Map<String, Map<String,Object>>> valueMap;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Map<String, Map<String, Map<String, Object>>> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Map<String, Map<String, Object>>> valueMap) {
		this.valueMap = valueMap;
	}
	

}
