
package com.taobao.csp.btrace.core.client;
/**
 * 这个后面作为一个全局的 类，主要是负责接收，被修改后class 专递的信息
 * @author xiaodu
 * @version 2011-8-16 下午07:07:57
 */
public class SystemRuntime {
	
	
	public static void await(long timeout){
		Object obj = new Object(); //每次等待都是自身的对象，防止相互柱塞
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	
	

}
