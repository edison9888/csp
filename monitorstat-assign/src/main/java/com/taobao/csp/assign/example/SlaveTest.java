
package com.taobao.csp.assign.example;

import com.taobao.csp.assign.slave.Slave;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 обнГ01:15:35
 */
public class SlaveTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ip = "127.0.0.1";
		String port = "22881";
		String siteName = "CM3";
		
		
		try {
			Slave slave = new Slave(ip,Integer.parseInt(port),ip,siteName, 1);
			slave.startup();
			
			
		} catch (NumberFormatException e) {
		} catch (Exception e) {
		}


	}

}
