package com.taobao.test;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author zhongting.zy
 * 这个类是用来测试Field的值的
 */
public class TestFieldThread2 extends Thread {

	
	public static String strField = "org";
	
	@Override
	public void run() {
		while(true) {
			
			MutiDemo demo = new MutiDemo();
			demo.say();	
			
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void increase(String str) {
		try {
			MutiDemo demo = new MutiDemo();
			demo.say();	
			
//			synchronized(list) {	//独占list
//				//保持对象的引用，不释放
//				if (list.size() < Long.MAX_VALUE - 1) {
//					list.add(demo);
//				} else {
//					list.clear();
//					list.add(demo);
//				}
//				demo.say();				
//			}

		} catch (Exception e) {
			String errMsg = e.toString();
		}
	}
	
	public void test (String str) {
		new TestFieldThread2().start();
	}
//	public static void main(String[] args) {
//		
//	}
}
