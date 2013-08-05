package com.taobao.csp.btrace.test;

public class InjectTest {

	public static void main(String[] args) {
		System.out.println("Load class InjectTest");
	}
	
	private int iparam = 1;
	private String strParam = "my value is 2";
	public int getIparam() {
		return iparam;
	}
	public void setIparam(int iparam) {
		this.iparam = iparam;
	}
	public String getStrParam() {
		return strParam;
	}
	public void setStrParam(String strParam) {
		this.strParam = strParam;
	}
	
	public static int getMoney() {
		return 1;
	}
}
