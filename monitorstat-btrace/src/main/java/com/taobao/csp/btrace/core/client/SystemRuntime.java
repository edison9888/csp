
package com.taobao.csp.btrace.core.client;
/**
 * ���������Ϊһ��ȫ�ֵ� �࣬��Ҫ�Ǹ�����գ����޸ĺ�class ר�ݵ���Ϣ
 * @author xiaodu
 * @version 2011-8-16 ����07:07:57
 */
public class SystemRuntime {
	
	
	public static void await(long timeout){
		Object obj = new Object(); //ÿ�εȴ���������Ķ��󣬷�ֹ�໥����
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	
	

}
