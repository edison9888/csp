
package com.taobao.csp.loadrun.core.valid;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.result.FetchResult;

/**
 * 
 * @author xiaodu
 * @version 2011-6-29 ионГ11:16:06
 */
public class DefaultValid implements Valid{
	
	private Map<ResultKey,Double> limitMap = new HashMap<ResultKey, Double>();
	
	
	public void putValid(ResultKey key,Double value){
		limitMap.put(key, value);
	}
	

	public boolean valid(FetchResult result) {
		
		Map<ResultKey,Double> map = result.getResult();
		if(map != null){
			for(Map.Entry<ResultKey,Double> entry:limitMap.entrySet()){
				
				ResultKey key = entry.getKey();
				Double value = entry.getValue();
				
				Double r = map.get(key);
				
				if(r != null){
					if(r >= value){
						return false;
					}
				}
				
			}
		}
		return true;
	}

}
