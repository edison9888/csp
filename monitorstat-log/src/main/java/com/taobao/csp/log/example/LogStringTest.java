
/**
 * monitorstat-log
 */
package com.taobao.csp.log.example;

import com.taobao.csp.log.MonitorLog;

/**
 * @author xiaodu
 *
 * обнГ7:38:40
 */
public class LogStringTest {
	
	public static void main(String[] args) {
		
		MonitorLog.setWaitTime(2);
		int i=0;
		while(i++ < 100){
		
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new String[]{"wo ","ni ta "});
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
