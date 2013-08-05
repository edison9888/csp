
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * 
 * ����������key������
 * 
 * @author xiaodu
 *
 * ����10:18:52
 */
public class QueryMultiResponse extends ResponsePacket{
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2146929913798274538L;


	/**
	 * @param packetId
	 */
	public QueryMultiResponse(long packetId,Map<String,Map<String, Map<String,Object>>> valueMap) {
		super(packetId);
		this.valueMap = valueMap;
	}
	public QueryMultiResponse(){
		super(-1);
	}
	
	
	/**
	 * valueMap�ṹ��<key,<time,<propertyName,value>>>
	 * key: ��Ҫ��ȡ��key������
	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
	 * propertyName:	����������
	 * value:			��Ϥֵ������ʱ������
	 */
	private Map<String,Map<String, Map<String,Object>>> valueMap;


	public Map<String, Map<String, Map<String, Object>>> getValueMap() {
		return valueMap;
	}


	public void setValueMap(Map<String, Map<String, Map<String, Object>>> valueMap) {
		this.valueMap = valueMap;
	}
	
	

}
