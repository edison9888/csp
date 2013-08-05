
package com.taobao.jprof;
/**
 * 
 * @author xiaodu
 * @version 2010-7-15 обнГ01:45:31
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		for(int i=0;i<1000;i++){
			long t = TimeUtil.getNanoTime();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.err.println(TimeUtil.getNanoTime()-t);
		}		

	}

}
