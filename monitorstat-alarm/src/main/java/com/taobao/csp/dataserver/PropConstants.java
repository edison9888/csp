
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver;

/**
 * 
 * ����Լ��һ����������
 * @author xiaodu
 *
 * ����10:29:33
 */
public class PropConstants {
	
	/**
	 * ���������ʾ ����������ô��� 
	 */
	public static final String E_TIMES = "E-times";
	
	/**
	 * ���������ʾ ����ʱ�� ��������ʱ��
	 */
	public static final String C_TIME = "C-time";
	
	public static final String SUC_RATE = "sucRate";
	
	public static final String EXCEPTION = "exception";
	
	/**
	 * ������ʾ ��С����ҳ���С�������С
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
	
	public static final String NOTIFY_C_S = "s"; //ͬ���ɹ�
	public static final String NOTIFY_C_S_RT = "s_rt";//ͬ���ɹ� ����ʱ��
	public static final String NOTIFY_C_S_F = "s_f"; //ͬ������ʧ��
	
	public static final String NOTIFY_C_RA_S = "ra_s";//�ɿ��첽�ɹ�
	public static final String NOTIFY_C_RA_S_RT = "ra_s_rt";//�ɿ��첽�ɹ� ����ʱ��
	public static final String NOTIFY_C_RA_F = "ra_f";//�ɿ��첽ʧ��
	public static final String NOTIFY_C_WS = "ws";//ͬ�����͵ȴ�����200ms�ɹ�
	public static final String NOTIFY_C_TIMEOUT = "timeout";//ͬ�����ͳ�ʱ
	public static final String NOTIFY_C_RE = "re";//ͬ�����ͽ��һ��
	public static final String NOTIFY_C_NC = "nc";//ͬ������û������
	
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
