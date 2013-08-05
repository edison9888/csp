
package com.taobao.monitor.alarm.n.filter;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 下午04:12:51
 */
public abstract class Filter {
	
	private long startExpiry;
	
	private long endExpiry;
	
	public Filter(long startExpiry,long endExpiry){
		
		this.startExpiry = startExpiry;
		this.endExpiry = endExpiry;
		
	}
	
	/**
	 * 返回如实是一个false 表示，当前将被过滤
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
