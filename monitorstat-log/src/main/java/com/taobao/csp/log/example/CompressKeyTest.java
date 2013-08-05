
/**
 * monitorstat-log
 */
package com.taobao.csp.log.example;

import com.taobao.csp.log.MonitorLog;

/**
 * @author xiaodu
 *
 * 上午9:19:37
 */
public class CompressKeyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		MonitorLog.setCompress(true);
		MonitorLog.setMaxCompressKeySize(9999);
		MonitorLog.setWaitTime(2);
		
		int i=0;
		while(i++ < 1000){
		
		MonitorLog.addStat(new String[]{"key1我哦我我为点发送到","key1我哦我我为点发送到","key1我哦我我为点发送到"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1我哦我我为点发送到","key1我哦我我为点发送到","key1我哦我我为点发送到"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1我哦我我为点发送到","key1我哦我我为点发送到","key1我哦我我为点发送到"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		MonitorLog.addStat(new String[]{"key1","key2","key3"}, new Long[]{1l,2l,3l});
		
		try {
			Thread.sleep(1000);
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
