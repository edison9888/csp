
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 *
 * 下午5:09:18
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
	 * valueMap结构：	<ip,<time,<propertyName,value>>>
	 * time:			收集时间，格式为:yyyyMMddHHmm
	 * propertyName:	数据属性名
	 * value:			熟悉值，存入时的类型
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
