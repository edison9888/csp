
/**
 * monitorstat-log
 */
package com.taobao.csp.log.example;

import com.taobao.csp.log.MonitorLog;

/**
 * @author xiaodu
 *
 * ионГ9:19:37
 */
public class LogLongTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i=0;
		while(i++ < 10000){
		
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
