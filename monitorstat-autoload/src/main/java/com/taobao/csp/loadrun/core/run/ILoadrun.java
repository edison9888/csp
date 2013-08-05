
package com.taobao.csp.loadrun.core.run;

import java.util.Map;

import com.taobao.csp.loadrun.core.constant.ResultKey;

/**
 * 
 * @author xiaodu
 * @version 2011-7-15 обнГ02:00:52
 */
public interface ILoadrun {
	
	public String getLoadrunId();
	
	public Map<ResultKey, Double> getLoadResult();

}
