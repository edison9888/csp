
/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;


/**
 * @author xiaodu
 *
 * 
 */
public class Test1 {
	
	/**
	 * 4471
	 * 4416
	 * @param args
	 */
	public static void main(String[] args) {
		
		LongNoteLog log = new LongNoteLog(2);
		
		for(int i=0;i<100000000;i++)
			log.addNote(new Long[]{1l,1l});
		
		long s = System.currentTimeMillis();
		for(int i=0;i<100000000;i++)
			log.addNote(new Long[]{1l,1l});
		System.out.println((System.currentTimeMillis() - s));
		StringBuilder sb = new StringBuilder();
		
		System.out.println(log.getString());
		

	}

}
