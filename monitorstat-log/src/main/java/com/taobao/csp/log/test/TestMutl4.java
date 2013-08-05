
/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * @author xiaodu
 *
 * обнГ4:54:12
 */
public class TestMutl4 {
	
	//8954
	//7816
	//8174
	//8909
	public static void main(String[] args) {
		
		
		for(int threadNum=1;threadNum<6;threadNum++){
			System.out.println("thread num:"+threadNum);
			StringBuilder sb = new StringBuilder();
			for(int size=0;size<4;size++){
				LongNoteLog1 log = new LongNoteLog1(2);
			CountDownLatch  c = new CountDownLatch(threadNum);
			
			List<AddThread> lsit = new ArrayList<AddThread>();
			
			for(int i=0;i<threadNum;i++){
				AddThread thread1 = new AddThread(log,c);
				thread1.start();
				lsit.add(thread1);
			}
			
			try {
				c.await();
			} catch (InterruptedException e) {
			}
			long time = 0;
			for(AddThread t:lsit){
				time+=t.time;
			}
			System.out.println(time);
			
			sb.append(log.getString()).append("\n");
			}
			System.out.println(sb);
		}
	

	}
	
	private static class AddThread extends Thread{
		
		public LongNoteLog1 log = null;
		public CountDownLatch count;
		
		public long time = 0l;
		
		public AddThread(LongNoteLog1 log,CountDownLatch count){
			this.log = log;
			this.count = count;
		}
		
		public void run(){
			
			for(int i=0;i<10000000;i++)
				log.addNote(new Long[]{1l,1l});
			
			long s = System.currentTimeMillis();
			for(int i=0;i<10000000;i++)
				log.addNote(new Long[]{1l,1l});
			
			
			time = (System.currentTimeMillis() - s);
			count.countDown();
			
			
		
			
		}
		
	}
	

}
