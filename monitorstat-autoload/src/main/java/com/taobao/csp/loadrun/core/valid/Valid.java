
package com.taobao.csp.loadrun.core.valid;

import com.taobao.csp.loadrun.core.result.FetchResult;

/**
 * 
 * @author xiaodu
 * @version 2011-6-24 下午03:01:12
 */
public interface Valid {
	
	/**
	 * 判断值是否已经超出阀值，
	 * @param result
	 * @return false 超过 true 正常
	 */
	public boolean valid(FetchResult result);

}
