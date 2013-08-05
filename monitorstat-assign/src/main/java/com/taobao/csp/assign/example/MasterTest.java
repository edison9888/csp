
package com.taobao.csp.assign.example;

import com.taobao.csp.assign.master.Master;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 обнГ01:15:07
 */
public class MasterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Master master = new Master();
		master.assignJob(new LoadRunJob());
		
//		while(true){
//		
//			master.assignJob(new LoadRunJob());
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//			}
//		}

	}

}
