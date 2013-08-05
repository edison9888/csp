
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.example;

import java.util.Date;
import java.util.Map;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.query.QueryUtil;

/**
 * @author xiaodu
 *
 * ÏÂÎç5:25:15
 */
public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Thread thread1 = new Thread(){
			public void run(){
				
				for(int i=0;i<3000;i++){
					Map<String, Map<String, Object>> map4;
					try {
						map4 = QueryUtil.querySingleRealTime("detail","PV-refer`http://item.taobao.com/item.htm");
						 System.out.println(map4);
						 map4 = QueryUtil.querySingleRealTime("detail","PV-refer`1");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-refer2");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-ref3");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-4");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-5");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-6");
						 Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				
			}
		};
		
		Thread thread2 = new Thread(){
			public void run(){
				
				for(int i=0;i<3000;i++){
					Map<String, Map<String, Object>> map4;
					try {
						map4 = QueryUtil.querySingleRealTime("detail","PV-refer`http://item.taobao.com/item.htm");
						 System.out.println(map4);
						 map4 = QueryUtil.querySingleRealTime("detail","PV-6");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-7");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-8");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-9");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-11");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-112");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-13");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-14");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-15");
						 map4 = QueryUtil.querySingleRealTime("detail","PV-17");
						 Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				
			}
		};
		
		thread1.start();
		thread2.start();
		
	
		
		
		
		
//		try {
//			Map<String, Map<String, Object>> map4;
//			for(int i=0;i<100;i++){
//			 Map<String, Map<String, Map<String, Object>>> map44 = QueryUtil.queryChildRealTime("detail","PV-refer");
//			 System.out.println(map44);
//			}
//			
//			for(int i=0;i<10;i++){
//				
//			map4 = QueryUtil.querySingleRealTime("detail","PV-refer`http://item.taobao.com/item.htm");
////			 System.out.println(map4);
//			 
//			 Map<String, Map<String, Map<String, Object>>> map44 = QueryUtil.queryChildRealTime("detail","PV-refer");
////				 System.out.println(map44);
//			 
//			 map4 = QueryUtil.querySingleRealTime("detail", KeyConstants.SEARCH_CONSUMER);
////			 System.out.println(map4);
//			}
//			
//			long t = System.currentTimeMillis();
//			for(int i=0;i<10000;i++){
//			
//			map4 = QueryUtil.querySingleRealTime("detail","PV-refer`http://item.taobao.com/item.htm");
////			 System.out.println(map4);
//			 
//			 Map<String, Map<String, Map<String, Object>>> map44 = QueryUtil.queryChildRealTime("detail","PV-refer");
////				 System.out.println(map44);
//			 
//			 map4 = QueryUtil.querySingleRealTime("detail", KeyConstants.SEARCH_CONSUMER);
////			 System.out.println(map4);
//			}
//			
//			System.out.println(System.currentTimeMillis() - t);
//			
////			 Map<Date,String> m = QueryHistoryUtil.querySingle("detail", "PV-refer`http://item.taobao.com/item.htm", "E-times", new Date());
////			 System.out.println(m);
//			 
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
       


	}

}
