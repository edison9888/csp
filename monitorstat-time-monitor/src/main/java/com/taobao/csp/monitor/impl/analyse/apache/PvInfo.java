
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.apache;

/**
 * @author xiaodu
 *
 * 上午9:50:42
 */
public class PvInfo {
	
	public int allPv;
	
	public int pv;
	
	public int hitpv;
	
	public long rt;
	
	public int pagesize;
	
	public int c200;
	
	public int c204;
	
	public int c302;
	
	public int c304;
	
	public int rterror;//1000ms以上
	
	public int rt100;//100ms以下
	
	public int rt500;//100ms~500ms之间的
	
	public int rt1000;//500ms~1000ms之间的

	
	
	

}
