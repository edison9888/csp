
package com.taobao.wwnotify.web;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author xiaodu
 * @version 2010-12-13 ÏÂÎç01:18:45
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//http://10.1.6.43:8080/wwnotify/notify.htm?mainTitle=oo&nick=ÆßÃð&subject=test&context=test&sender=Hudson&sign=fc828e1cd5a2ce025b80fbc8e6b40357cfad4438
		//mainTitle=ff&subject=test&context=test&nick=Ð¡¶Ä&sender=Hudson&sign=803ad6a3f59f239ec5c7f7061c0512bedb7c559e
		String si = DigestUtils.shaHex("xiaodu" + "test" + "test" + "asdfioasdjfoaofi!@#RQ$R@#$%WERGSGW#$");

		System.out.println(si);
		
	}

}
