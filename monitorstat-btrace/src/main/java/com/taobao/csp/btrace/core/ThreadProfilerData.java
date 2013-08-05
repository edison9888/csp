
package com.taobao.csp.btrace.core;

import java.util.Stack;

/**
 * 
 * @author xiaodu
 * @version 2011-8-19 ионГ09:52:59
 */
public class ThreadProfilerData {
	
	
	public Stack<ProfilerInfo> infoQueue = new Stack<ProfilerInfo>();
	
	public int statckNum=0;
	
	public long lastTime = 0;
	
	
	
	

}
