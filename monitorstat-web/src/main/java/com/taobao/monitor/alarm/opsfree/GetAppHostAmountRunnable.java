package com.taobao.monitor.alarm.opsfree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.network.lb.LBRelateApp;
import com.taobao.monitor.alarm.network.lb.LoadBalanceConstants;
import com.taobao.monitor.common.po.HSFProviderHosts;
import com.taobao.util.CollectionUtil;

/**
 * 获取应用名称与机器列表的对应关系
 * @author baiyan
 *
 */
public class GetAppHostAmountRunnable implements Runnable{
	private static final Logger logger =  Logger.getLogger(GetAppHostAmountRunnable.class);
	private static List<String> appNameList = new ArrayList<String>();
	private static ConcurrentHashMap<String,HSFProviderHosts> appHostMap = new ConcurrentHashMap<String,HSFProviderHosts>();
	private static ConcurrentHashMap<String,List<LBRelateApp>> lbAppRelate = new ConcurrentHashMap<String,List<LBRelateApp>>();
	static{
		appNameList.add("tradeplatform");
		appNameList.add("itemcenter");
		appNameList.add("uicfinal");
		appNameList.add("tradeface");
		appNameList.add("tradelogs");
		appNameList.add("timeoutcenter");
		appNameList.add("cart");
		appNameList.add("uiclogin");
		appNameList.add("misccenter");
		appNameList.add("delivery");
		appNameList.add("shopcenter");
		
		for(String appName:appNameList){
			 HSFProviderHosts host = OpsfreeJsonUtil.getHostsByAppname(appName);
			 if(host == null){
				 logger.warn("host from opsFree is null,appName=" + appName);
				 continue;
			 }
			 appHostMap.put(appName, host);
		}
		//opsFree中机器数量发生变化后，重新生成LB设备与APP的映射表
		for(String lbName:LoadBalanceConstants.lbIpSegmentList.keySet()){
			lbAppRelate.put(lbName, findRelateAppsByLoadBlanceName(lbName));
		}
				
	}
	
	@Override
	public void run() {
		for(String appName:appNameList){
			 HSFProviderHosts host = OpsfreeJsonUtil.getHostsByAppname(appName);
			 if(host == null){
				 logger.warn("host from opsFree is null,appName=" + appName);
				 continue;
			 }
			 appHostMap.put(appName, host);
		}
		
		//opsFree中机器数量发生变化后，重新生成LB设备与APP的映射表
		for(String lbName:LoadBalanceConstants.lbIpSegmentList.keySet()){
			lbAppRelate.put(lbName, findRelateAppsByLoadBlanceName(lbName));
		}
		
	}

	/**
	 * 返回与这个LB关联的所有应用的列表
	 * @param lbName
	 * @return
	 */
	private static List<LBRelateApp> findRelateAppsByLoadBlanceName(String lbName){
		List<String> ipSegmentsList = LoadBalanceConstants.lbIpSegmentList.get(lbName);
		if(CollectionUtil.isEmpty(ipSegmentsList)){
			return null;
		}
		Set<String> ipSegmentStartSet= new HashSet<String>();
		for(String ipSegments:ipSegmentsList){
			ipSegmentStartSet.add(LoadBalanceConstants.ALL_IP_SEGMENT.equals(ipSegments)?"":ipSegments);
		}
		
		List<LBRelateApp> lBRelateAppList = new ArrayList<LBRelateApp>();
		
		Set<Entry<String, HSFProviderHosts>> entrySet = GetAppHostAmountRunnable.appHostMap.entrySet();
		for(Entry<String, HSFProviderHosts> entry:entrySet){
			HSFProviderHosts host = entry.getValue();
			int effectHostCount = 0;
			List<String> effectHostList = new ArrayList<String>();
			for(String hostIp:host.getAllHost()){
				if(isStart(hostIp,ipSegmentStartSet)) {
					effectHostCount ++ ;
					effectHostList.add(hostIp);
				}
			}
			LBRelateApp lBRelateApp = new LBRelateApp(lbName,effectHostCount,entry.getKey(),effectHostList);
			lBRelateAppList.add(lBRelateApp);
		}
		return lBRelateAppList;
	}
	
	private static boolean isStart(String hostIp,Set<String> ipSegmentStartSet){
		for(String ipSegmentStart:ipSegmentStartSet){
			if(hostIp.startsWith(ipSegmentStart)){
				return true;
			}
		}
		return false;
	}
	
	public static ConcurrentHashMap<String,List<LBRelateApp>> lbAppRelateMap(){
		return lbAppRelate;
	}
}