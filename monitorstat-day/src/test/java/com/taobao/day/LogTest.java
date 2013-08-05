package com.taobao.day;

import com.taobao.csp.day.tddl.TddlLogKey;

public class LogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String appName1 = "";
		String dbFeature1= "";
		String dbName1= "";
		String dbIp1= "";
		String dbPort1= "";
		String sql1= "";
		String collectTime1= "";
		
		TddlLogKey hourKey1 = new TddlLogKey(appName1, dbFeature1, dbName1, dbIp1, dbPort1, sql1, collectTime1);
		
		String appName2 = "";
		String dbFeature2= "";
		String dbName2= "";
		String dbIp2= "";
		String dbPort2= "";
		String sql2= "";
		String collectTime2= "";
		
		TddlLogKey hourKey2 = new TddlLogKey(appName2, dbFeature2, dbName2, dbIp2, dbPort2, sql2, collectTime2);
		
		System.out.println(hourKey1.equals(hourKey2));

	}

}
