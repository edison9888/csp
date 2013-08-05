
package com.taobao.csp.assign.classloader;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.taobao.csp.assign.classloader.slave.AssignJobClassLoader;

/**
 * 
 * @author xiaodu
 * @version 2011-7-21 ÏÂÎç01:18:02
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {
//		TestClassLoader classLoader = new TestClassLoader();
//		Thread thread1 = new Thread(){
//			public void run(){
//				PrintTest test = new PrintTest();
//				test.print();
//				System.out.println("1");
//			}
//		};
//		thread1.setContextClassLoader(classLoader);
//		thread1.start();
//		
//		
//		TestClassLoader classLoader2 = new TestClassLoader();
//		Thread thread2 = new Thread(){
//			public void run(){
//				PrintTest test = new PrintTest();
//				test.print();
//				System.out.println("2");
//			}
//		};
//		thread2.setContextClassLoader(classLoader2);
//		thread2.start();
		
		
		
//		try {
//			TestClassLoader classLoader1 = new TestClassLoader();
//			Class c1= classLoader1.loadClass("com.taobao.csp.loadrun.core.FetchFeature");
//			//c1.getMethods()[0].invoke(c1.newInstance());
//			
//			TestClassLoader classLoader2 = new TestClassLoader();
//			Class c2 = classLoader2.loadClass("com.taobao.csp.loadrun.core.FetchFeature");
//			//c2.getMethods()[0].invoke(c2.newInstance());
//			
//		} catch (Exception e) {
//		}
		
		
		Thread th = new Thread(){

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		th.setDaemon(true);
		th.start();
		
		
	}
	
	
	
	


}
