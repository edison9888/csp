
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver;

/**
 * 
 * 这里约定一个属性名称
 * @author xiaodu
 *
 * 上午10:29:33
 */
public class PropConstants {
	
	/**
	 * 这个常量表示 次数，如调用次数 
	 */
	public static final String E_TIMES = "E-times";
	
	/**
	 * 这个常量表示 消费时间 ，如请求时间
	 */
	public static final String C_TIME = "C-time";
	
	public static final String SUC_RATE = "sucRate";
	
	public static final String EXCEPTION = "exception";
	
	/**
	 * 用来表示 大小，如页面大小，请求大小
	 */
	public static final String R_SIZE = "R-size";
	
	/**
	 * cpu
	 */
	public static final String CPU = "CPU";
	
	/**
	 * LOAD
	 */
	public static final String LOAD = "LOAD";
	
	/**
	 * Swap 
	 */
	public static final String SWAP = "SWAP";
	
	/**
	 * Memory 
	 */
	public static final String JVMMEMORY = "JVMMEMORY";
	
	/**
	 * GC 
	 */
	public static final String JVMGC = "JVMGC";
	
	/**
	 * FULLGC 
	 */
	public static final String JVMFULLGC = "JVMFULLGC";
	
	/**
	 * CMSGC 
	 */
	public static final String JVMCMSGC= "JVMCMSGC";
	
	public static final String P_SIZE= "P-size";
	public static final String C_200= "C-200";
	public static final String C_302= "C-302";
	public static final String C_OTHER= "c-other";
	
	public static final String NOTIFY_C_S = "s"; //同步成功
	public static final String NOTIFY_C_S_RT = "s_rt";//同步成功 消耗时间
	public static final String NOTIFY_C_S_F = "s_f"; //同步发送失败
	
	public static final String NOTIFY_C_RA_S = "ra_s";//可靠异步成功
	public static final String NOTIFY_C_RA_S_RT = "ra_s_rt";//可靠异步成功 消耗时间
	public static final String NOTIFY_C_RA_F = "ra_f";//可靠异步失败
	public static final String NOTIFY_C_WS = "ws";//同步发送等待超过200ms成功
	public static final String NOTIFY_C_TIMEOUT = "timeout";//同步发送超时
	public static final String NOTIFY_C_RE = "re";//同步发送结果一次
	public static final String NOTIFY_C_NC = "nc";//同步发送没有连接
	
	public static final String DATASOURCE_IN_USE = "InUse";
	public static final String DATASOURCE_AVAILABLE = "Available";
	
	public static final String THREAD_BLOCKED = "BLOCKED";
	public static final String THREAD_WAITING = "WAITING";
	public static final String THREAD_TERMINATED = "TERMINATED";
	public static final String THREAD_TIMEDWAITING = "TIMEDWAITING";
	public static final String THREAD_RUNNABLE= "RUNNABLE";
	
	public static final String THREAD_POOL_MAX = "max";
	public static final String THREAD_POOL_CURRENT = "current";
	public static final String GETCOUNT = "getCount";
	
	public static final String TDOD = "TDOD";	
	public static final String PV_SS = "PV_SS";
	public static final String FAIL_PERCENT = "failpercent";	//For cpgw
	
	//for chongzhideliver
	public static final String SUCCESS_INVOKE = "successInvoke";
	public static final String FAILURE_INVOKE = "failureInvoke";
	public static final String TIMEOUT_INVOKE = "timeoutInvoke";
}
