
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

/**
 * @author xiaodu
 *
 * обнГ6:23:06
 */
public interface DataCollector {
	
	
	public String getName();
	
	public void collect(CallBack call);
	
	public void release();
	
	
	public interface CallBack{
		
		
		public void readerLine(String line);
		
		
	}
	

}
