package com.taobao.csp.capacity.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.db.impl.center.UserInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.po.UserInfoPo;
import com.taobao.monitor.common.util.TBProductCache;

public class AutoPermission {
	
	static String PE = "不摧";
	
	
	
	public static void main(String [] args) {
		
		printPePermission();
		
	}
	
	/***
	 * 打出一个pe权限的方法，自己用工具方法，直接拷得打印所有pe权限代码
	 */
	public static void printPePermission() {

		Map<String, Set<Integer>> peIps = new HashMap<String, Set<Integer>>();
		
		UserInfoDao userInfoDao = new UserInfoDao();
		
		List<AppInfoPo> apps = AppInfoAo.get().findAllDayApp();
		
		for (AppInfoPo po : apps) {
			String appName = po.getOpsName();
			int appId = po.getAppId();
			ProductLine productLine = TBProductCache.getProductLineByAppName(appName);
			
			String peS = productLine.getPe();
			
			if (peS != null && peS.length() > 0) {
				String [] peA = peS.split(",");
				for (String pe : peA) {
					Set<Integer> appS;
					if (peIps.containsKey(pe)) {
						appS = peIps.get(pe);
					} else {
						appS = new HashSet<Integer>();
						peIps.put(pe, appS);
					}
					
					appS.add(appId);
				}
			}
		}
		
		for (Map.Entry<String, Set<Integer>> entry : peIps.entrySet()) {
			String pe = entry.getKey();
			if (!PE.equals(pe)) {
				continue;
			}
			
			Set<Integer> appIds = entry.getValue();
			
			if (appIds.isEmpty()) continue;
			
			UserInfoPo userInfoPo = userInfoDao.getLoginUserPo(pe);
			if (userInfoPo == null) {
				// System.out.println(pe);
				continue;
			}
			int userId = userInfoPo.getId();
			
			String add = "addLoadrun:ALL";
			String edit = "editLoadrun:";
			String delete = "deleteLoadrun:";
			String auto = "autoLoadrun:";
			String manual = "manualLoadrun:";
			
			for (Integer appId : appIds) {
				edit = edit +  appId + ",";
				delete = delete +  appId + ",";
				auto = auto +  appId + ",";
				manual = manual +  appId + ",";
			}
			edit = edit.substring(0, edit.length() - 1);
			delete = delete.substring(0, delete.length() - 1);
			auto = auto.substring(0, auto.length() - 1);
			manual = manual.substring(0, manual.length() - 1);
			
			String permission = add + ";" + edit + ";" + delete + ";" + auto + ";" + manual + ";";
			
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("update ms_monitor_user set permission_desc = '");
			buffer.append(permission).append("' where id=").append(userId).append(";");
			
			System.out.println(buffer.toString());
			
		}
	}
	
	/***
	 * 打出所有pe权限sql，自己用的工具方法
	 */
	public static String printAllPePermission() {
		Map<String, Set<Integer>> peIps = new HashMap<String, Set<Integer>>();
		
		UserInfoDao userInfoDao = new UserInfoDao();
		
		List<AppInfoPo> apps = AppInfoAo.get().findAllDayApp();
		
		for (AppInfoPo po : apps) {
			String appName = po.getOpsName();
			int appId = po.getAppId();
			ProductLine productLine = TBProductCache.getProductLineByAppName(appName);
			
			String peS = productLine.getPe();
			
			if (peS != null && peS.length() > 0) {
				String [] peA = peS.split(",");
				for (String pe : peA) {
					Set<Integer> appS;
					if (peIps.containsKey(pe)) {
						appS = peIps.get(pe);
					} else {
						appS = new HashSet<Integer>();
						peIps.put(pe, appS);
					}
					
					appS.add(appId);
				}
			}
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		for (Map.Entry<String, Set<Integer>> entry : peIps.entrySet()) {
			String pe = entry.getKey();
			Set<Integer> appIds = entry.getValue();
			
			if (appIds.isEmpty()) continue;
			
			UserInfoPo userInfoPo = userInfoDao.getLoginUserPo(pe);
			if (userInfoPo == null) {
				// System.out.println(pe);
				continue;
			}
			int userId = userInfoPo.getId();
			
			String add = "addLoadrun:ALL";
			String edit = "editLoadrun:";
			String delete = "deleteLoadrun:";
			String auto = "autoLoadrun:";
			String manual = "manualLoadrun:";
			
			for (Integer appId : appIds) {
				edit = edit +  appId + ",";
				delete = delete +  appId + ",";
				auto = auto +  appId + ",";
				manual = manual +  appId + ",";
			}
			edit = edit.substring(0, edit.length() - 1);
			delete = delete.substring(0, delete.length() - 1);
			auto = auto.substring(0, auto.length() - 1);
			manual = manual.substring(0, manual.length() - 1);
			
			String permission = add + ";" + edit + ";" + delete + ";" + auto + ";" + manual + ";";
			
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("update ms_monitor_user set permission_desc = '");
			buffer.append(permission).append("' where id=").append(userId).append(";");
			
			sqlBuffer.append(buffer).append("\n");
			
		}
		return sqlBuffer.toString();
	}

}
