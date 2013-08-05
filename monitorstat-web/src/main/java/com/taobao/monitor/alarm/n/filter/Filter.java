
package com.taobao.monitor.alarm.n.filter;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ����04:12:51
 */
public abstract class Filter {
	
	private long startExpiry;
	
	private long endExpiry;
	
	public Filter(long startExpiry,long endExpiry){
		
		this.startExpiry = startExpiry;
		this.endExpiry = endExpiry;
		
	}
	
	/**
	 * ������ʵ��һ��false ��ʾ����ǰ��������
	 * @param context
	 * @return
	 */
	protected abstract boolean valid(AlarmContext context);
	
	
	
	protected boolean timeExpiry(){
		
		long time = System.currentTimeMillis();
		if(time >=startExpiry && time <= endExpiry){
			return true;
		}else{
			return false;
		}
		
	}
	
	

}
