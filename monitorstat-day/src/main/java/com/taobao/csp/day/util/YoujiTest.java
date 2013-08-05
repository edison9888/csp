package com.taobao.csp.day.util;

import java.util.Calendar;

public class YoujiTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		
		System.out.println(calendar.getTime());

	}

}
