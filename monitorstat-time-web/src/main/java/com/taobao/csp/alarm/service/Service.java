
package com.taobao.csp.alarm.service;

import com.taobao.csp.alarm.po.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ����04:56:21
 */
public  interface Service<T> {
	

	/**
	 * ���� true ��ʾ�����ͨ����false ��ʾֹͣ
	 * @param context
	 * @return
	 */
	public  boolean  lookup(AlarmContext context);
	
	public  void init();
	
	public  void register(T t);
	
	public  void unregister(T t);
	
	
	
	
	

}
