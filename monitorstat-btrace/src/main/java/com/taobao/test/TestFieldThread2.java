package com.taobao.test;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author zhongting.zy
 * ���������������Field��ֵ��
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
			
//			synchronized(list) {	//��ռlist
//				//���ֶ�������ã����ͷ�
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
