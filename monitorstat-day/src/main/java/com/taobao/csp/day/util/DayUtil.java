package com.taobao.csp.day.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

public class DayUtil {
	
	public static Logger logger = Logger.getLogger(DayUtil.class);
	
	/***
	 * 检查获取应用的接口是否可用
	 * @return
	 */
	public static boolean checkGetAppInterfaceAvailable() {
		boolean available = true;
		String sampleApp = "detail";
		List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(sampleApp);
		
		if (hosts == null || hosts.size() == 0) {
			available = false;
		}
		
		return available;
	}
	
	
	/***
	 * 用于测试,key为应用名，value为机器列表
	 * @return
	 */
	public static Map<String, Set<String>> getTddlSimulate() {
		Map<String, Set<String>> appM = new HashMap<String, Set<String>>();
		
		Set<String> appS0 = new HashSet<String>();
		appS0.add("10.232.35.10");
		appM.put("logisticscenter", appS0);
		
//		Set<String> appS1 = new HashSet<String>();
//		appS1.add("10.232.20.102");
//		appM.put("udc", appS1);
//		
//		Set<String> appS2 = new HashSet<String>();
//		appS2.add("10.232.35.17");
//		appM.put("inventoryplatform", appS2);
//		
//		Set<String> appS5 = new HashSet<String>();
//		appS5.add("10.232.20.112");
//		appM.put("onlinecs", appS5);
		
		return appM;
	}
	
	public static String appendSizeUrl(String ip,  String logPath, long begin, long end) {
		StringBuilder sb = new StringBuilder("http://");
		sb.append(ip).append(":8082/get").append(logPath);
		sb.append("?begin=").append(begin).append("&end=").append(end);
		sb.append("&encode=text");
		
		return sb.toString();
	}
	
	public static String appendTailUrl(String ip,  String logPath, String taskId) {
		StringBuilder sb = new StringBuilder("http://");
		sb.append(ip).append(":8082/tail").append(logPath);
		sb.append("?task_id=").append(taskId);
		sb.append("&encode=text");
		
		return sb.toString();
	}

}
