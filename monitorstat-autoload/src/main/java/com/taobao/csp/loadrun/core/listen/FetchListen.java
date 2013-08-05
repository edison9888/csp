
package com.taobao.csp.loadrun.core.listen;

import com.taobao.csp.loadrun.core.result.FetchResult;

/**
 * 
 * @author xiaodu
 * @version 2011-6-22 обнГ02:55:03
 */
public interface FetchListen {
	
	public boolean valid(FetchResult result);
	
	public void start();
	
	public void complete();
	
	public void error(Exception e);

}
