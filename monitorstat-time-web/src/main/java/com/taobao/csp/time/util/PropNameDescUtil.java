
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.util;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.dataserver.PropConstants;

/**
 * @author xiaodu
 *
 * 下午5:09:48
 */
public class PropNameDescUtil {
	
	private static Map<String,String> propertyNameMap = new HashMap<String, String>();
	static{
		propertyNameMap.put(PropConstants.E_TIMES, "请求量");
		propertyNameMap.put(PropConstants.C_200, "200状态码");
		propertyNameMap.put(PropConstants.C_302, "302状态码");
		propertyNameMap.put(PropConstants.C_OTHER, "其它状态码");
		propertyNameMap.put(PropConstants.C_TIME, "响应时间(ms)");
		propertyNameMap.put(PropConstants.P_SIZE, "请求大小(字节)");
		propertyNameMap.put("rt_error", "响应时间大于1s");
		propertyNameMap.put("rt_100", "响应时间小于100ms");
		propertyNameMap.put("rt_500", "响应时间100~500ms");
		propertyNameMap.put("rt_1000", "响应时间500~1000ms");
		
		propertyNameMap.put(PropConstants.JVMGC, "GC次数");
		propertyNameMap.put(PropConstants.JVMMEMORY, "内存使用率(%)");
		propertyNameMap.put(PropConstants.JVMFULLGC, "full gc次数");
		propertyNameMap.put(PropConstants.JVMCMSGC, "cms次数");
		
		propertyNameMap.put(PropConstants.EXCEPTION, "异常量");
		
		propertyNameMap.put(PropConstants.THREAD_BLOCKED, "阻塞中");
		propertyNameMap.put(PropConstants.THREAD_TERMINATED, "终止");
		propertyNameMap.put(PropConstants.THREAD_TIMEDWAITING, "限时等待");
		propertyNameMap.put(PropConstants.THREAD_RUNNABLE, "正在执行线程");
		
		propertyNameMap.put(PropConstants.DATASOURCE_IN_USE, "已经使用连接池");
		propertyNameMap.put(PropConstants.DATASOURCE_AVAILABLE, "可用连接池数");
		
		propertyNameMap.put(PropConstants.THREAD_POOL_CURRENT, "已经使用线程池");
		propertyNameMap.put(PropConstants.THREAD_POOL_MAX, "最大线程池");
		
		
		
		
		propertyNameMap.put(PropConstants.NOTIFY_C_S, "同步成功");
		propertyNameMap.put(PropConstants.NOTIFY_C_S_RT, "同步成功 消耗时间");
		propertyNameMap.put(PropConstants.NOTIFY_C_S_F, "同步发送失败");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_S, "可靠异步成功");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_S_RT, "可靠异步成功 消耗时间");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_F, "可靠异步失败");
		propertyNameMap.put(PropConstants.NOTIFY_C_WS, "同步发送等待超过200ms成功");
		propertyNameMap.put(PropConstants.NOTIFY_C_TIMEOUT, "同步发送超时");
		propertyNameMap.put(PropConstants.NOTIFY_C_RE, "同步发送结果一次");
		propertyNameMap.put(PropConstants.NOTIFY_C_NC, "同步发送没有连接");
		
		propertyNameMap.put(PropConstants.FAIL_PERCENT, "失败率(%)");
		
		propertyNameMap.put(PropConstants.SUCCESS_INVOKE, "成功请求量");
		propertyNameMap.put(PropConstants.FAILURE_INVOKE, "失败请求量");
		propertyNameMap.put(PropConstants.TIMEOUT_INVOKE, "超时请求量");
		
	}
	
	/**
	 * 获取属性名称的描述，如果找不到返回原值
	 *@author xiaodu
	 * @param propName
	 * @return
	 *TODO
	 */
	public static String getDesc(String propName){
		String name = propertyNameMap.get(propName);
		if(name !=null){
			return name;
		}
		return propName;
	}
	

}
