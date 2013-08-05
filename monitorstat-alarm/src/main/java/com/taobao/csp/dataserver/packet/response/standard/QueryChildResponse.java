
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
public class QueryChildResponse extends ResponsePacket{

	/**
	 * 
	 */
	private static final long serialVersionUID = 971867238064543279L;

	/**
	 * @param packetId
	 */
	public QueryChildResponse(long packetId, String keyName, Map<String, Map<String, Map<String,Object>>> valueMap) {
		super(packetId);
		this.keyName = keyName;
		this.valueMap = valueMap;
	}
	
	public QueryChildResponse(){
		super(-1);
	}
	
	private String keyName;
	
	/**
	 * valueMap�ṹ��	<childkey, <time,<propertyName,value>>>
	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
	 * propertyName:	����������
	 * value:			��Ϥֵ������ʱ������
	 */
	private Map<String, Map<String, Map<String,Object>>> valueMap;

	public Map<String, Map<String, Map<String, Object>>> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Map<String, Map<String, Object>>> valueMap) {
		this.valueMap = valueMap;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
