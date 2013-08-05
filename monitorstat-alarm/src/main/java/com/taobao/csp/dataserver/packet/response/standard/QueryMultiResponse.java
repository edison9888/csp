
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.response.standard;

import java.util.Map;

import com.taobao.csp.dataserver.packet.ResponsePacket;

/**
 * 
 * 返回请求多个key的数据
 * 
 * @author xiaodu
 *
 * 上午10:18:52
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
	 * valueMap结构：<key,<time,<propertyName,value>>>
	 * key: 需要获取的key的名称
	 * time:			收集时间，格式为:yyyyMMddHHmm
	 * propertyName:	数据属性名
	 * value:			熟悉值，存入时的类型
	 */
	private Map<String,Map<String, Map<String,Object>>> valueMap;


	public Map<String, Map<String, Map<String, Object>>> getValueMap() {
		return valueMap;
	}


	public void setValueMap(Map<String, Map<String, Map<String, Object>>> valueMap) {
		this.valueMap = valueMap;
	}
	
	

}
