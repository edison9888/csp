package com.taobao.test;

/**
 * 
 * @author zhongting.zy
 * �������������ע�룬��ֹ�����������
 */
public class MutiDemo {
	public MutiDemo() {
	}
	
	private long mutiField = 1;					//����������
	
	private static long staticMutiField;	//����static����
	
	public void say() {
		System.out.println("begin:" + staticMutiField);
		if(staticMutiField < Long.MAX_VALUE) {
			staticMutiField ++;
		} else {
			staticMutiField = 0;
		}
		mutiField = mutiField - 100;
		System.out.println("end:" + staticMutiField);
	}
	
}
