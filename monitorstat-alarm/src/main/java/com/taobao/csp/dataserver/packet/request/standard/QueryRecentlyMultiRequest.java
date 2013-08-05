
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.packet.request.standard;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 请求单个key，并返回最近的一个值
 * 
 * @author xiaodu
 *
 * 上午9:15:31
 */
public class QueryRecentlyMultiRequest  extends RequestPacket{
	
	private static final long serialVersionUID = 1L;
	
	private List<String> keyList = new ArrayList<String>();
	
	
	public QueryRecentlyMultiRequest(){
		super();
	}
	
	public QueryRecentlyMultiRequest(String... keys){
		this();
		if(keys !=null){
			for(String key:keys){
				keyList.add(key);
			}
		}
	}

	public QueryRecentlyMultiRequest(List<String> keys){
		keyList.addAll(keys);
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}

}
