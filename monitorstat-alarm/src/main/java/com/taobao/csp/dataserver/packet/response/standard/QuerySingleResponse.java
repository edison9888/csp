
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * @author xiaodu
 * 单key查询，返回的数据格式
 * 下午5:09:18
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
	 * valueMap结构：<time,<propertyName,value>>
	 * time:			收集时间，格式为:yyyyMMddHHmm
	 * propertyName:	数据属性名
	 * value:			熟悉值，存入时的类型
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
