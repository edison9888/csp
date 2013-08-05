
package com.taobao.csp.btrace.core.script.test;

import com.taobao.csp.btrace.core.ProfilerInfo;
import com.taobao.csp.btrace.core.client.TransformerProxy;

/**
 * 
 * @author xiaodu
 */
public class Test {
	
	
	private int i;;
	
	
	public void g(){
		
		
		System.out.println(this.i);
		
//		¡¢BtraceRunTime.test(i);
		
	}
	
	public String gh(){
		
		//BtraceRunTime.test(map);
		return null;
	}
	
	
	public static String  test1(){
		
		return test2();
	}
	
	
	
	
	public static String  test2(){
		ClassA a = new ClassA();
		return a.methodA("", "");
	}
	
	
	
	public static class ClassA {
		
		
		public String  methodA(String a,String b){
			
			
			ClassB cb = new ClassB();
			
			return cb.methodB();
			
		}

	}
	
	public static class ClassB {
		
		public String methodB(){
			return "b";
		}

	}

	
	
	public void print(){
		
		
		
		try{
			ProfilerInfo info = null;
			while((info=TransformerProxy.queue.poll())!=null){
				StringBuffer sb = new StringBuffer();
				for(String f:info.getParamValues()){
					sb.append(f).append(",");
				}
				System.out.println(info.getMethodId()+"="+info.getMethodStackNum()+"="+sb+"="+info.getReturnValue());
				
			}}catch (Exception e) {
				// TODO: handle exception
			}
		
		
	}
	
	
}
