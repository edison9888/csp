
package com.taobao.csp.alarm.service;

import com.taobao.csp.alarm.po.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 下午04:56:21
 */
public  interface Service<T> {
	

	/**
	 * 返回 true 表示，检查通过，false 表示停止
	 * @param context
	 * @return
	 */
	public  boolean  lookup(AlarmContext context);
	
	public  void init();
	
	public  void register(T t);
	
	public  void unregister(T t);
	
	
	
	
	

}
