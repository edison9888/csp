package com.taobao.test;

/**
 * 
 * @author zhongting.zy
 * 这个类用来测试注入，防止单例问题存在
 */
public class MutiDemo {
	public MutiDemo() {
	}
	
	private long mutiField = 1;					//测试类属性
	
	private static long staticMutiField;	//测试static属性
	
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
