
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;


/**
 * @author xiaodu
 *
 * обнГ6:26:37
 */
public interface DataAnalyse {
	
	
	public void analyseOneLine(String line) ;
	
	public void doAnalyse();
	
	public void submit();
	
	public void release();

}
