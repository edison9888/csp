package com.taobao.monitor.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;


/***
 * 应用分组的工具类
 * @author youji.zj 2012-08-15
 *
 */
public class GroupManager implements ManagerListener {
	
	private static GroupManager manager = new GroupManager();
	
	
	private Map<String, Map<String, List<String>>> groupInfos = new HashMap<String, Map<String,List<String>>>();
	private Map<String, Map<String, String>> ipToGroupMap = new HashMap<String, Map<String, String>>();
	
	private DiamondManager diamondManager = new DefaultDiamondManager("CSP_GROUP", "com.taobao.csp.group.config", this);
	
	private GroupManager() {
		init();
	}
	
	public static GroupManager get() {
		return manager;
	}
	
	/***
	 * 代表应用的分组，主要用于容量相关
	 * @param appId
	 * @return
	 */
	public static int getAppId(int appId) {
		int transAppId = appId;
		
		// l0-c
		if (appId == 8 ) {
			transAppId = 376;
		}
		
		// tp-g1
		if (appId == 322) {
			transAppId = 383;
		}
		
		// ump-detail
		if (appId == 338) {
			transAppId = 432;
		}
		
		// sell-normal
		if(appId == 47) {
			transAppId = 700;
		}
		
		return transAppId;
	}
	
	public Map<String, Map<String, List<String>>> getGroupInfo() {
			return groupInfos;
	}
	
	/***
	 * 获取应用的分组信息
	 * @param appName
	 * @return
	 */
	public Map<String, List<String>> getGroupInfoByAppName(String appName) {
			return groupInfos.get(appName);
	}
	
	/***
	 * 查询分组
	 * @param appName
	 * @param ip
	 * @return
	 */
	public String getGroupByAppAndIp(String appName, String ip) {
			Map<String, List<String>>  groupIp = groupInfos.get(appName);
			if (groupIp != null) {
				for (Map.Entry<String, List<String>> entry : groupIp.entrySet()) {
					String keyGroup = entry.getKey();
					List<String> ipL =  entry.getValue();
					if (ipL.contains(ip)) {
						return keyGroup;
					}
				}
			}
			
			return null;
	}
	
	public String getGroupByAppAndIpEx(String appName, String ip) {
			if(ipToGroupMap.containsKey(appName)) {
				return ipToGroupMap.get(appName).get(ip);
			}
			return null;
	}
	
	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveConfigInfo(String configInfo) {
		init();
	}
	
	private void init() {
		String groupS = diamondManager.getAvailableConfigureInfomation(10000);
		String [] groupA = groupS.split("\n");
		
		
		Map<String, Map<String, List<String>>> tmp = new HashMap<String, Map<String,List<String>>>();
		
			ipToGroupMap.clear();
			
			for (String group : groupA) {
				String[] valueA = group.trim().split("-");
				if (valueA.length == 3) {
					String appName = valueA[0];
					String hostIp = valueA[1];
					String groupName = valueA[2];
					
					Map<String, List<String>> appGroup;
					if (tmp.containsKey(appName)) {
						appGroup = tmp.get(appName);
					} else {
						appGroup = new HashMap<String, List<String>>();
						tmp.put(appName, appGroup);
					}
					
					List<String> groupIp;
					if (appGroup.containsKey(groupName)) {
						groupIp = appGroup.get(groupName);
					} else {
						groupIp = new ArrayList<String>();
						appGroup.put(groupName, groupIp);
					}
					
					if (!groupIp.contains(hostIp)) {
						groupIp.add(hostIp);
					}
					
					//add by 中亭，存一份，按照应用名,IP 得出groupname
					Map<String, String> ipGroupMap;
					if(ipToGroupMap.containsKey(appName)) {
						ipGroupMap = ipToGroupMap.get(appName);
					} else {
						ipGroupMap = new HashMap<String, String>();
						ipToGroupMap.put(appName, ipGroupMap);
					}
					ipGroupMap.put(hostIp, groupName);
				}
			}
			
			groupInfos = tmp;
	}
	
	public static void main(String [] args) {
		GroupManager manager = GroupManager.get();
		String appName = "itemcenter"; 
		Map<String, List<String>> map = manager.getGroupInfoByAppName(appName);
		for(String groupName: map.keySet()) {
			System.out.println(groupName);
			List<String> ips = map.get(groupName);
			for(String ip : ips) {
				if(!manager.getGroupByAppAndIp(appName, ip).equals(manager.getGroupByAppAndIpEx(appName, ip))) {
					System.out.println("false->" + ip);
				} else {
					//System.out.println(ip);
				}
			}
			System.out.println("over");
		}
	}

}
