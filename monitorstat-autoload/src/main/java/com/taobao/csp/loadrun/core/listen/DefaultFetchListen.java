
package com.taobao.csp.loadrun.core.listen;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.result.FetchResult;

/**
 * 
 * @author xiaodu
 * @version 2011-6-22 обнГ02:55:03
 */
public class DefaultFetchListen implements FetchListen{
	
	private static final Logger logger = Logger.getLogger(DefaultFetchListen.class);

	public void complete() {
		
	}

	public void error(Exception e) {
		
	}

	public void modify() {
		
	}

	public void start() {
		
	}

	public boolean valid(FetchResult result) {
		return false;
	}
	

}
