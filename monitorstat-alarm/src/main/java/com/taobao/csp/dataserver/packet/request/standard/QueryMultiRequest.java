
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 一次性请求多个key的结果
 * @author xiaodu
 *
 * 上午10:09:52
 */
public class QueryMultiRequest extends RequestPacket{

	private static final long serialVersionUID = 1L;
	
	private List<String> keyList = new ArrayList<String>();
	
	public QueryMultiRequest(){
		super();
	}
	
	public QueryMultiRequest(String... keys){
		if(keys !=null){
			for(String key:keys){
				keyList.add(key);
			}
		}
	}

	public QueryMultiRequest(List<String> keys){
		keyList.addAll(keys);
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
}
