
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 * ��key��ѯ�����ص����ݸ�ʽ
 * ����5:09:18
 */
public class QuerySingleResponse extends ResponsePacket{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4423502502337686585L;

	/**
	 * @param packetId
	 */
	public QuerySingleResponse(long packetId, String keyName, Map<String, Map<String,Object>> valueMap) {
		super(packetId);
		this.keyName = keyName;
		this.valueMap = valueMap;
	}

	public QuerySingleResponse(){
		super(-1);
	}
	
	private String keyName;
	
	/**
	 * valueMap�ṹ��<time,<propertyName,value>>
	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
	 * propertyName:	����������
	 * value:			��Ϥֵ������ʱ������
	 */
	private Map<String, Map<String,Object>> valueMap;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Map<String, Map<String, Object>> getValueMap() {
		return valueMap;
	}
	
}
