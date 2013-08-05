package com.taobao.csp.capacity;

public class NumberUtil {
	public static String fromatNumber(String num) {
		try{
			long n = Long.valueOf(num)/10000;
			return n+"Íò";
		}catch (Exception e) {
			return "";
		}
		
	}

}
